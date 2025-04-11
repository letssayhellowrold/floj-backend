package com.flexilearnoj.flexilearnojbackendquestionservice.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.flexilearnoj.flexilearnojbackendcommon.common.ErrorCode;
import com.flexilearnoj.flexilearnojbackendcommon.constant.CommonConstant;
import com.flexilearnoj.flexilearnojbackendcommon.exception.BusinessException;
import com.flexilearnoj.flexilearnojbackendcommon.utils.SqlUtils;
import com.flexilearnoj.flexilearnojbackendmodel.model.dto.questionSubmit.QuestionSubmitAddRequest;
import com.flexilearnoj.flexilearnojbackendmodel.model.dto.questionSubmit.QuestionSubmitQueryRequest;
import com.flexilearnoj.flexilearnojbackendmodel.model.entity.Question;
import com.flexilearnoj.flexilearnojbackendmodel.model.entity.QuestionSubmit;
import com.flexilearnoj.flexilearnojbackendmodel.model.entity.User;
import com.flexilearnoj.flexilearnojbackendmodel.model.enums.QuestionSubmitLanguageEnum;
import com.flexilearnoj.flexilearnojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.flexilearnoj.flexilearnojbackendmodel.model.vo.QuestionSubmitVO;
import com.flexilearnoj.flexilearnojbackendquestionservice.mapper.QuestionSubmitMapper;
import com.flexilearnoj.flexilearnojbackendquestionservice.service.QuestionService;
import com.flexilearnoj.flexilearnojbackendquestionservice.service.QuestionSubmitService;
import com.flexilearnoj.flexilearnojbackendquestionservice.rabbitmq.MyMessageProducer;
import com.flexilearnoj.flexilearnojbackendserviceclient.service.UserFeignClient;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author Lenovo
* @description 针对表【question_submit(题目提交)】的数据库操作Service实现
* @createDate 2024-11-04 12:29:45
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    private MyMessageProducer myMessageProducer;
    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        long questionId = questionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 校验编程语言是否合理
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if(languageEnum == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"编程语言错误");
        }
        long userId = loginUser.getId();
        // 每个用户串行提交题目
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(language);
        // 设置提交的初始状态
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());// 判题状态枚举类
        String judgeInfoString = "";
        questionSubmit.setJudgeInfo(judgeInfoString);// 空对象

        boolean save  = this.save(questionSubmit);
        if(!save)
        {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"数据提交失败");
        }
        Long questionSubmitId = questionSubmit.getId();
        // 生产者发送消息
        myMessageProducer.sendMessage("code_exchange", "my_routingKey", String.valueOf(questionSubmitId));

//        // 异步执行判题服务
//        CompletableFuture.runAsync(()->{
//            judgeService.doJudge(questionSubmitId);
//        });
        return questionSubmitId;
    }
    /**
     * 获取查询包装类（需要知道用户可能根据哪些字段查询，根据前端传来的请求对象，得到 mybatis 框架支持的查询 QueryWrapper 类）
     * 是一个方法类
     * @param questionSubmitSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitSubmitQueryRequest == null) {
            return queryWrapper;
        }

        String language = questionSubmitSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitSubmitQueryRequest.getUserId();
        String sortField = questionSubmitSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitSubmitQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);// eq 相等判断
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        // 如果状态合法才将其写入查询条件
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status)!=null, "status", status);
        queryWrapper.eq("isDelete",false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 单条查询信息
     * @param questionSubmitSubmit
     * @param loginUser
     * @return
     */
    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmitSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitSubmitVO = QuestionSubmitVO.objToVo(questionSubmitSubmit);// 对象转封装类
        // 对包装类脱敏：仅本人和管理员可以查看敏感信息
        long userId = loginUser.getId();
        if(userId != questionSubmitSubmit.getUserId() && !userFeignClient.isAdmin(loginUser))
        {
            // 当前查询者不是题目提交者也不是管理员，将代码隐去
            questionSubmitSubmitVO.setCode(null);
        }
        return questionSubmitSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitSubmitList = questionSubmitSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitSubmitVOPage = new Page<>(questionSubmitSubmitPage.getCurrent(), questionSubmitSubmitPage.getSize(), questionSubmitSubmitPage.getTotal());
        if (CollUtil.isEmpty(questionSubmitSubmitList)) {
            return questionSubmitSubmitVOPage;
        }
        // 对得到的每一条提交信息，都转化为VO类
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitSubmitList.stream()
                .map(questionSubmit -> getQuestionSubmitVO(questionSubmit, loginUser))
                .collect(Collectors.toList());
        questionSubmitSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitSubmitVOPage;
    }
}





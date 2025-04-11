package com.flexilearnoj.flexilearnojbackendpostservice.controller;


import com.flexilearnoj.flexilearnojbackendcommon.common.BaseResponse;
import com.flexilearnoj.flexilearnojbackendcommon.common.ResultUtils;
import com.flexilearnoj.flexilearnojbackendpostservice.service.PostThumbService;
import com.flexilearnoj.flexilearnojbackendserviceclient.service.UserFeignClient;

import com.flexilearnoj.flexilearnojbackendmodel.model.dto.postthumb.PostThumbAddRequest;
import com.flexilearnoj.flexilearnojbackendcommon.exception.BusinessException;
import com.flexilearnoj.flexilearnojbackendmodel.model.dto.postthumb.PostThumbCheckRequest;
import com.flexilearnoj.flexilearnojbackendcommon.common.ErrorCode;
import com.flexilearnoj.flexilearnojbackendmodel.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子点赞接口
 *
 * 
 * 
 */
@RestController
@RequestMapping("/post_thumb")
@Slf4j
public class PostThumbController {

    @Resource
    private PostThumbService postThumbService;

    @Resource
    private UserFeignClient userService;

    /**
     * 点赞 / 取消点赞
     *
     * @param postThumbAddRequest
     * @param request
     * @return resultNum 本次点赞变化数
     */
    @PostMapping("/")
    public BaseResponse<Integer> doThumb(@RequestBody PostThumbAddRequest postThumbAddRequest,
                                         HttpServletRequest request) {
        if (postThumbAddRequest == null || postThumbAddRequest.getPostId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        long postId = postThumbAddRequest.getPostId();
        int result = postThumbService.doPostThumb(postId, loginUser);
        return ResultUtils.success(result);
    }
    /**
     * 检查用户是否已经点赞了某个帖子
     *
     * @param postThumbCheckRequest 包含 userId 和 postId 的请求体
     * @return BaseResponse 包含是否点赞的布尔值
     */
    @PostMapping("/check_has_thumbed")
    public BaseResponse<Boolean> checkHasThumbed(@RequestBody PostThumbCheckRequest postThumbCheckRequest) {
        if (postThumbCheckRequest == null || postThumbCheckRequest.getUserId() == null || postThumbCheckRequest.getPostId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean hasThumbed = postThumbService.hasThumbed(postThumbCheckRequest.getUserId(), postThumbCheckRequest.getPostId());
        return ResultUtils.success(hasThumbed);
    }

}

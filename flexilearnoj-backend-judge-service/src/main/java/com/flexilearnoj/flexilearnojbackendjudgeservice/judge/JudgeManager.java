package com.flexilearnoj.flexilearnojbackendjudgeservice.judge;

import com.flexilearnoj.flexilearnojbackendjudgeservice.judge.strategy.JudgeContext;
import com.flexilearnoj.flexilearnojbackendjudgeservice.judge.strategy.JudgeStrategy;
import com.flexilearnoj.flexilearnojbackendjudgeservice.judge.strategy.DefaultJudgeStrategy;
import com.flexilearnoj.flexilearnojbackendmodel.model.codesandbox.JudgeInfo;
import com.flexilearnoj.flexilearnojbackendmodel.model.entity.QuestionSubmit;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 判题管理（简化调用）
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     */
    List<JudgeInfo> doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        // 根据情况调用不同的判题服务实现类
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();

        return judgeStrategy.doJudge(judgeContext);
    }

}

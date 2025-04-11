package com.flexilearnoj.flexilearnojbackendjudgeservice.judge.strategy;


import com.flexilearnoj.flexilearnojbackendmodel.model.codesandbox.JudgeInfo;
import java.util.List;

/**
 * 判题策略
 */
public interface JudgeStrategy {

    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    List<JudgeInfo> doJudge(JudgeContext judgeContext);
}

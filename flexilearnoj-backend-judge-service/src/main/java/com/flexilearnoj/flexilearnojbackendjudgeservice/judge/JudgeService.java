package com.flexilearnoj.flexilearnojbackendjudgeservice.judge;


import com.flexilearnoj.flexilearnojbackendmodel.model.entity.QuestionSubmit;

/**
 * 判题服务
 */
public interface JudgeService {

    /**
     * 判题
     */
    QuestionSubmit doJudge(long questionSubmitId);
}

package com.flexilearnoj.flexilearnojbackendjudgeservice.judge.codesandbox;

import com.flexilearnoj.flexilearnojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.flexilearnoj.flexilearnojbackendmodel.model.codesandbox.ExecuteCodeRequest;

/**
 * 代码沙箱接口定义
 */
public interface CodeSandbox {

    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}

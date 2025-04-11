package com.flexilearnoj.flexilearnojbackendserviceclient.service;


import com.flexilearnoj.flexilearnojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 判题服务
 */
@FeignClient(name = "flexilearnoj-backend-judge-service", path = "/api/judge/inner")
public interface JudgeFeignClient {

    /**
     * 判题
     */
    @PostMapping("/do")
    QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId);
}
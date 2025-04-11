package com.flexilearnoj.flexilearnojbackendpostservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.flexilearnoj.flexilearnojbackendcommon.common.BaseResponse;
import com.flexilearnoj.flexilearnojbackendcommon.common.ErrorCode;
import com.flexilearnoj.flexilearnojbackendcommon.common.ResultUtils;
import com.flexilearnoj.flexilearnojbackendcommon.exception.BusinessException;
import com.flexilearnoj.flexilearnojbackendcommon.exception.ThrowUtils;
import com.flexilearnoj.flexilearnojbackendmodel.model.dto.post.PostQueryRequest;
import com.flexilearnoj.flexilearnojbackendmodel.model.dto.postfavour.PostFavourAddRequest;
import com.flexilearnoj.flexilearnojbackendmodel.model.dto.postfavour.PostFavourCheckRequest;
import com.flexilearnoj.flexilearnojbackendmodel.model.dto.postfavour.PostFavourQueryRequest;
import com.flexilearnoj.flexilearnojbackendmodel.model.entity.Post;
import com.flexilearnoj.flexilearnojbackendmodel.model.entity.User;
import com.flexilearnoj.flexilearnojbackendmodel.model.vo.PostVO;
import com.flexilearnoj.flexilearnojbackendpostservice.service.PostFavourService;
import com.flexilearnoj.flexilearnojbackendpostservice.service.PostService;
import com.flexilearnoj.flexilearnojbackendserviceclient.service.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子收藏接口
 *
 * 
 * 
 */
@RestController
@RequestMapping("/post_favour")
@Slf4j
public class PostFavourController {

    @Resource
    private PostFavourService postFavourService;

    @Resource
    private PostService postService;

    @Resource
    private UserFeignClient userFeignClient;

    /**
     * 收藏 / 取消收藏
     *
     * @param postFavourAddRequest
     * @param request
     * @return resultNum 收藏变化数
     */
    @PostMapping("/")
    public BaseResponse<Integer> doPostFavour(@RequestBody PostFavourAddRequest postFavourAddRequest,
                                              HttpServletRequest request) {
        if (postFavourAddRequest == null || postFavourAddRequest.getPostId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能操作
        final User loginUser = userFeignClient.getLoginUser(request);
        long postId = postFavourAddRequest.getPostId();
        int result = postFavourService.doPostFavour(postId, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 获取我收藏的帖子列表
     *
     * @param postQueryRequest
     * @param request
     */
    @PostMapping("/my/list/page")
    public BaseResponse<Page<PostVO>> listMyFavourPostByPage(@RequestBody PostQueryRequest postQueryRequest,
                                                             HttpServletRequest request) {
        if (postQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userFeignClient.getLoginUser(request);
        long current = postQueryRequest.getCurrent();
        long size = postQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Post> postPage = postFavourService.listFavourPostByPage(new Page<>(current, size),
                postService.getQueryWrapper(postQueryRequest), loginUser.getId());
        return ResultUtils.success(postService.getPostVOPage(postPage, request));
    }

    /**
     * 获取用户收藏的帖子列表
     *
     * @param postFavourQueryRequest
     * @param request
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<PostVO>> listFavourPostByPage(@RequestBody PostFavourQueryRequest postFavourQueryRequest,
            HttpServletRequest request) {
        if (postFavourQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = postFavourQueryRequest.getCurrent();
        long size = postFavourQueryRequest.getPageSize();
        Long userId = postFavourQueryRequest.getUserId();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20 || userId == null, ErrorCode.PARAMS_ERROR);
        Page<Post> postPage = postFavourService.listFavourPostByPage(new Page<>(current, size),
                postService.getQueryWrapper(postFavourQueryRequest.getPostQueryRequest()), userId);
        return ResultUtils.success(postService.getPostVOPage(postPage, request));
    }

    /**
     * 检查用户是否已经点赞了某个帖子
     *
     * @param postFavourCheckRequest 包含 userId 和 postId 的请求体
     * @return BaseResponse 包含是否点赞的布尔值
     */
    @PostMapping("/check_has_favoured")
    public BaseResponse<Boolean> checkHasFavoured(@RequestBody PostFavourCheckRequest postFavourCheckRequest) {
        if (postFavourCheckRequest == null || postFavourCheckRequest.getUserId() == null || postFavourCheckRequest.getPostId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean hasThumbed = postFavourService.hasFavoured(postFavourCheckRequest.getUserId(), postFavourCheckRequest.getPostId());
        return ResultUtils.success(hasThumbed);
    }
}

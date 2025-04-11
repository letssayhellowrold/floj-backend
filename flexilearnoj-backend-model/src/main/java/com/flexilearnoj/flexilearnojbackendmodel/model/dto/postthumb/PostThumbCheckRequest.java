package com.flexilearnoj.flexilearnojbackendmodel.model.dto.postthumb;

import lombok.Data;

import java.io.Serializable;

/**
 * 帖子点赞检查请求
 */
@Data
public class PostThumbCheckRequest implements Serializable {

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 帖子 id
     */
    private Long postId;

    private static final long serialVersionUID = 1L;
}
package com.flexilearnoj.flexilearnojbackendmodel.model.dto.post;

import com.flexilearnoj.flexilearnojbackendcommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 查询请求
 *
 * 
 * 
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PostQueryRequest extends PageRequest implements Serializable {

    /**
     * 必须包含的 id
     */
    private Long id;

    /**
     * 必须不包含的 id
     */
    private Long notId;
    /**
     * 根据题目 id 搜索帖子
     */
    private Long questionId;

    /**
     * 搜索词
     */
    private String searchText;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 至少有一个标签
     */
    private List<String> orTags;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 收藏用户 id
     */
    private Long favourUserId;

    private static final long serialVersionUID = 1L;
}
package me.zhengjie.service.dto;

import lombok.Data;

/**
 * @Description
 * @Author nwl
 * @Create 2023-02-26 下午4:27
 */
@Data
public class KnowledgeSmallDto {

    private Long id;

    /** 名称 */
    private String name;

    /** 描述 */
    private String description;

    /** 渲染文本 */
    private String descriptionHtml;
}

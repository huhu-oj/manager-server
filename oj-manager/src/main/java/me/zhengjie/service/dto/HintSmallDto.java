package me.zhengjie.service.dto;

import lombok.Data;

/**
 * @Description
 * @Author nwl
 * @Create 2023-02-26 下午4:19
 */
@Data
public class HintSmallDto {

    private Long id;

    /** 描述 */
    private String description;

    /** 渲染文本 */
    private String descriptionHtml;
}

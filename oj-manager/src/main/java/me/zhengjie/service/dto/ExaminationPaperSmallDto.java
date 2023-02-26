package me.zhengjie.service.dto;

import lombok.Data;

/**
 * @Description
 * @Author nwl
 * @Create 2023-02-26 下午4:35
 */
@Data
public class ExaminationPaperSmallDto {

    private Long id;

    /** 名称 */
    private String name;

    /** 描述 */
    private String description;

    /** 描述渲染文本 */
    private String descriptionHtml;
}

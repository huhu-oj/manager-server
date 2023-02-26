package me.zhengjie.service.dto;

import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author nwl
 * @Create 2023-02-26 下午4:20
 */
@Data
public class SolutionSmallDto {

    private Long id;

    /** 标题 */
    private String title;

    /** 描述 */
    private String description;

    /** 渲染文本 */
    private String descriptionHtml;

    private List<LabelSmallDto> labels;
}

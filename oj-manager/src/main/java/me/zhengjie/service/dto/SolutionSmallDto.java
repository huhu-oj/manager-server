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

    private String description;

    private List<LabelSmallDto> labels;
}

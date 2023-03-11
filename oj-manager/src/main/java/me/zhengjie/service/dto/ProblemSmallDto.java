package me.zhengjie.service.dto;

import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author nwl
 * @Create 2023-02-26 下午4:29
 */
@Data
public class ProblemSmallDto {

    private Long id;

    /** 标题 */
    private String title;

    private List<ProblemKnowledgeDto> problemKnowledges;
    private List<LabelSmallDto> labels;
}

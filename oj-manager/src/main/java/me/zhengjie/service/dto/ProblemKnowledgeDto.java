package me.zhengjie.service.dto;

import lombok.Data;

/**
 * @Description
 * @Author nwl
 * @Create 2023-02-26 下午4:29
 */
@Data
public class ProblemKnowledgeDto {

    private Long problemId;

    private KnowledgeSmallDto knowledge;

    private Integer weight;
}

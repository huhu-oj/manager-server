package me.zhengjie.service.dto;

import lombok.Data;

/**
 * @Description
 * @Author nwl
 * @Create 2023-02-26 下午4:35
 */
@Data
public class ExaminationPaperProblemDto {

//    private Long id;


//    private Long examinationPaperId;

    private ProblemSmallDto problem;

    private Double score;

}

package me.zhengjie.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @Description
 * @Author nwl
 * @Create 2023-03-06 下午6:37
 */
@Entity
@Getter
@Setter
@Table(name = "oj_examination_paper_problem")
public class ExaminationPaperProblem {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "examination_paper_id")
    private Long examinationPaperId;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    @JSONField(serialize = false)
    private Problem problem;

    @Column(name = "score")
    private Double score;
}

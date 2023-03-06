package me.zhengjie.domain;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Description
 * @Author nwl
 * @Create 2023-03-05 下午8:54
 */
@Entity
@Getter
@Setter
@Table(name = "oj_problem_knowledge")
public class ProblemKnowledge implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;
//    @ManyToOne
//    @JoinColumn(name = "problem_id")
//    @JSONField(serialize = false)
//    private Problem problem;

    @Column(name = "problem_id")
    private Long problemId;
    @ManyToOne
    @JSONField(serialize = false)
    @JoinColumn(name = "knowledge_id")
    private Knowledge knowledge;

    @Column(name = "weight")
    private Integer weight;
}

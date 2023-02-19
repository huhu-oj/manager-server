/*
*  Copyright 2019-2020 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package me.zhengjie.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
* @website https://eladmin.vip
* @description /
* @author nwl
* @date 2023-02-14
**/
@Entity
@Getter
@Setter
@Table(name="oj_problem")
public class Problem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "`title`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "标题")
    private String title;

    @Column(name = "`description`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "描述")
    private String description;

    @Column(name = "`create_time`")
    @CreationTimestamp
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @Column(name = "`update_time`")
    @UpdateTimestamp
    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;

    @Column(name = "`description_html`")
    @ApiModelProperty(value = "渲染文本")
    private String descriptionHtml;

//    @JSONField(serialize = false)
    @OneToMany(cascade={CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE})
    @JoinColumn(name = "`problem_id`")
    private List<Hint> hints;
    @JSONField(serialize = false)
    @OneToMany(mappedBy = "problem")
    private List<Solution> solutions;
    @JSONField(serialize = false)
    @OneToMany(mappedBy = "problem")
    private List<StandardIo> standardIos;
    @JSONField(serialize = false)
    @OneToMany(mappedBy = "problem")
    private List<AnswerRecord> answerRecords;
    @JSONField(serialize = false)
    @ManyToMany(mappedBy = "problems")
    private List<ExaminationPaper> examinationPapers;
    @JSONField(serialize = false)
    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(
            name = "oj_problem_knowledge",
            joinColumns = {@JoinColumn(name = "problem_id")},
            inverseJoinColumns = {@JoinColumn(name = "knowledge_id")}
    )
    private List<Knowledge> knowledges;
    @JSONField(serialize = false)
    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(
            name = "oj_problem_label",
            joinColumns = {@JoinColumn(name = "problem_id")},
            inverseJoinColumns = {@JoinColumn(name = "label_id")}
    )
    private List<Label> labels;
    public void copy(Problem source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}

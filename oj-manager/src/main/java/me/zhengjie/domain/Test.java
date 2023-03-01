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
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
* @website https://eladmin.vip
* @description /
* @author nwl
* @date 2023-02-13
**/
@Entity
@Getter
@Setter
@Table(name="oj_test")
public class Test implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "`title`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "标题")
    private String title;

    @Column(name = "`description`")
    @NotBlank
    @ApiModelProperty(value = "备注")
    private String description;

//    @Column(name = "`examination_paper_id`",nullable = false)
//    @NotNull
//    @ApiModelProperty(value = "试卷id")
//    private Long examinationPaperId;
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "`examination_paper_id`")
    @NotNull
    @ApiModelProperty(value = "试卷id")
    private ExaminationPaper examinationPaper;

    @Column(name = "`start_time`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "开始时间")
    private Timestamp startTime;

    @Column(name = "`end_time`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "结束时间")
    private Timestamp endTime;

    @Column(name = "`enabled`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @Column(name = "`create_time`",nullable = false)
    @NotNull
    @CreationTimestamp
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @Column(name = "`update_time`",nullable = false)
    @NotNull
    @UpdateTimestamp
    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;

    @Column(name = "`user_id`")
    @NotNull
    @ApiModelProperty(value = "所属用户")
    private Long userId;
    public void copy(Test source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}

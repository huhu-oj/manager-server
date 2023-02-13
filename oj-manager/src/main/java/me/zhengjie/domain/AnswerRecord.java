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

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author nwl
* @date 2023-02-13
**/
@Entity
@Data
@Table(name="oj_answer_record")
public class AnswerRecord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "`problem_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "所属题目")
    private Long problemId;

    @Column(name = "`user_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "所属用户")
    private Long userId;

    @Column(name = "`code`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "代码")
    private String code;

    @Column(name = "`execute_time`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "执行时间")
    private Long executeTime;

    @Column(name = "`language_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "所属语言")
    private Long languageId;

    @Column(name = "`log`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "日志")
    private String log;

    @Column(name = "`error`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "错误日志")
    private String error;

    @Column(name = "`pass_num`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "通过数")
    private Integer passNum;

    @Column(name = "`not_pass_num`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "未通过数")
    private Integer notPassNum;

    @Column(name = "`execute_result_id`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "执行结果")
    private Long executeResultId;

    @Column(name = "`note`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "备注")
    private String note;

    @Column(name = "`create_time`")
    @CreationTimestamp
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @Column(name = "`update_time`")
    @UpdateTimestamp
    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;

    public void copy(AnswerRecord source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}

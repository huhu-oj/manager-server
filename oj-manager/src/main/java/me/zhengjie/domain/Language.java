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
* @date 2023-02-07
**/
@Entity
@Data
@Table(name="oj_language")
public class Language implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "`name`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "语言名称")
    private String name;

    @Column(name = "`compile_statement`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "编译语句")
    private String compileStatement;

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

    public void copy(Language source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}

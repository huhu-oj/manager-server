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
import java.util.List;

/**
* @website https://eladmin.vip
* @description /
* @author nwl
* @date 2023-02-13
**/
@Entity
@Getter
@Setter
@Table(name="oj_judge_machine")
public class JudgeMachine implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "`name`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "名称")
    private String name;

    @Column(name = "`username`")
    @NotBlank
    @ApiModelProperty(value = "主机账号")
    private String username;

    @Column(name = "`password`")
    @NotBlank
    @ApiModelProperty(value = "密码")
    private String password;

    @Column(name = "`url`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "主机地址")
    private String url;

    @Column(name = "`enabled`")
    @NotNull
    @ApiModelProperty(value = "是否启动")
    private Boolean enabled;

    @Column(name = "`create_time`")
    @CreationTimestamp
    @ApiModelProperty(value = "createTime")
    private Timestamp createTime;

    @Column(name = "`update_time`")
    @UpdateTimestamp
    @ApiModelProperty(value = "updateTime")
    private Timestamp updateTime;

    @Column(name = "`support_language`")
    @ApiModelProperty(value = "支持的语言")
    private String supportLanguage;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name = "oj_judge_machine_language",
            joinColumns = {@JoinColumn(name = "judge_machine_id")},
            inverseJoinColumns = {@JoinColumn(name = "language_id")}
    )
    private List<Language> languages;
    public void copy(JudgeMachine source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}

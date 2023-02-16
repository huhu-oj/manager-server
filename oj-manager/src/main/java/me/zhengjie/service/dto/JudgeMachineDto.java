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
package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.domain.Language;

import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;

/**
* @website https://eladmin.vip
* @description /
* @author nwl
* @date 2023-02-13
**/
@Data
public class JudgeMachineDto implements Serializable {

    private Long id;

    /** 名称 */
    private String name;

    /** 主机账号 */
    private String username;

    /** 密码 */
    private String password;

    /** 主机地址 */
    private String url;

    /** 是否启动 */
    private Integer enabled;

    private Timestamp createTime;

    private Timestamp updateTime;

    /** 支持的语言 */
    private String supportLanguage;
    private List<Language> languages;
}
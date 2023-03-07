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
import me.zhengjie.domain.Problem;

import java.io.Serializable;
import java.sql.Timestamp;

/**
* @website https://eladmin.vip
* @description /
* @author nwl
* @date 2023-02-13
**/
@Data
public class AnswerRecordDto implements Serializable {

    private Long id;

    /** 所属题目 */
    private ProblemSmallDto problem;

    /** 所属用户 */
    private Long userId;

    /** 代码 */
    private String code;

    /** 执行时间 */
    private Long executeTime;

    /** 所属语言 */
    private LanguageSmallDto language;
    private TestSmallDto test;

    /** 日志 */
    private String log;

    /** 错误日志 */
    private String error;

    /** 通过数 */
    private Integer passNum;

    /** 未通过数 */
    private Integer notPassNum;

    /** 执行结果 */
    private ExecuteResultSmallDto executeResult;

    /** 备注 */
    private String note;

    /** 创建时间 */
    private Timestamp createTime;

    /** 更新时间 */
    private Timestamp updateTime;
}

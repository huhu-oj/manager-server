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

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
* @website https://eladmin.vip
* @description /
* @author nwl
* @date 2023-02-14
**/
@Data
public class ProblemDto implements Serializable {

    private Long id;

    /** 标题 */
    private String title;

    /** 描述 */
    private String description;

    /** 创建时间 */
    private Timestamp createTime;

    /** 更新时间 */
    private Timestamp updateTime;

    /** 渲染文本 */
    private String descriptionHtml;
    private List<HintSmallDto> hints;
    private List<SolutionSmallDto> solutions;
    private List<StandardIoSmallDto> standardIos;
    private List<AnswerRecordSmallDto> answerRecords;
    private List<KnowledgeSmallDto> knowledges;
    private List<LabelSmallDto> labels;
}

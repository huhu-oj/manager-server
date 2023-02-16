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
package me.zhengjie.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.domain.Problem;
import me.zhengjie.service.dto.ProblemDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @website https://eladmin.vip
* @author nwl
* @date 2023-02-14
**/
@Mapper(componentModel = "spring",uses = {HintMapper.class, AnswerRecordMapper.class,SolutionMapper.class, ExaminationPaperMapper.class, LanguageMapper.class, KnowledgeMapper.class, StandardIoMapper.class},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProblemMapper extends BaseMapper<ProblemDto, Problem> {

}
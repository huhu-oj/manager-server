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
package me.zhengjie.service;

import me.zhengjie.domain.AnswerRecord;
import me.zhengjie.service.dto.AnswerRecordDto;
import me.zhengjie.service.dto.AnswerRecordQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @website https://eladmin.vip
* @description 服务接口
* @author nwl
* @date 2023-02-13
**/
public interface AnswerRecordService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(AnswerRecordQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<AnswerRecordDto>
    */
    List<AnswerRecordDto> queryAll(AnswerRecordQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return AnswerRecordDto
     */
    AnswerRecordDto findById(Long id);

    /**
    * 创建
    * @param resources /
    * @return AnswerRecordDto
    */
    AnswerRecordDto create(AnswerRecord resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(AnswerRecord resources);

    /**
    * 多选删除
    * @param ids /
    */
    void deleteAll(Long[] ids);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<AnswerRecordDto> all, HttpServletResponse response) throws IOException;
}
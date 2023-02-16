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
package me.zhengjie.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.AnswerRecord;
import me.zhengjie.repository.AnswerRecordRepository;
import me.zhengjie.service.AnswerRecordService;
import me.zhengjie.service.dto.AnswerRecordDto;
import me.zhengjie.service.dto.AnswerRecordQueryCriteria;
import me.zhengjie.service.mapstruct.AnswerRecordMapper;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* @website https://eladmin.vip
* @description 服务实现
* @author nwl
* @date 2023-02-13
**/
@Service
@RequiredArgsConstructor
public class AnswerRecordServiceImpl implements AnswerRecordService {

    private final AnswerRecordRepository answerRecordRepository;
    private final AnswerRecordMapper answerRecordMapper;

    @Override
    public Map<String,Object> queryAll(AnswerRecordQueryCriteria criteria, Pageable pageable){
        Page<AnswerRecord> page = answerRecordRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(answerRecordMapper::toDto));
    }

    @Override
    public List<AnswerRecordDto> queryAll(AnswerRecordQueryCriteria criteria){
        return answerRecordMapper.toDto(answerRecordRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public AnswerRecordDto findById(Long id) {
        AnswerRecord answerRecord = answerRecordRepository.findById(id).orElseGet(AnswerRecord::new);
        ValidationUtil.isNull(answerRecord.getId(),"AnswerRecord","id",id);
        return answerRecordMapper.toDto(answerRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AnswerRecordDto create(AnswerRecord resources) {
        return answerRecordMapper.toDto(answerRecordRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AnswerRecord resources) {
        AnswerRecord answerRecord = answerRecordRepository.findById(resources.getId()).orElseGet(AnswerRecord::new);
        ValidationUtil.isNull( answerRecord.getId(),"AnswerRecord","id",resources.getId());
        answerRecord.copy(resources);
        answerRecordRepository.save(answerRecord);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            answerRecordRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<AnswerRecordDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AnswerRecordDto answerRecord : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("所属题目", answerRecord.getProblem().getTitle());
            map.put("所属用户", answerRecord.getUserId());
            map.put("代码", answerRecord.getCode());
            map.put("执行时间", answerRecord.getExecuteTime());
            map.put("所属语言", answerRecord.getLanguage().getName());
            map.put("日志", answerRecord.getLog());
            map.put("错误日志", answerRecord.getError());
            map.put("通过数", answerRecord.getPassNum());
            map.put("未通过数", answerRecord.getNotPassNum());
            map.put("执行结果", answerRecord.getExecuteResult().getName());
            map.put("备注", answerRecord.getNote());
            map.put("创建时间", answerRecord.getCreateTime());
            map.put("更新时间", answerRecord.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
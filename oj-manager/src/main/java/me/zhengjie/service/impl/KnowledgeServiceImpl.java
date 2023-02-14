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

import me.zhengjie.domain.Knowledge;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.repository.KnowledgeRepository;
import me.zhengjie.service.KnowledgeService;
import me.zhengjie.service.dto.KnowledgeDto;
import me.zhengjie.service.dto.KnowledgeQueryCriteria;
import me.zhengjie.service.mapstruct.KnowledgeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @website https://eladmin.vip
* @description 服务实现
* @author nwl
* @date 2023-02-14
**/
@Service
@RequiredArgsConstructor
public class KnowledgeServiceImpl implements KnowledgeService {

    private final KnowledgeRepository knowledgeRepository;
    private final KnowledgeMapper knowledgeMapper;

    @Override
    public Map<String,Object> queryAll(KnowledgeQueryCriteria criteria, Pageable pageable){
        Page<Knowledge> page = knowledgeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(knowledgeMapper::toDto));
    }

    @Override
    public List<KnowledgeDto> queryAll(KnowledgeQueryCriteria criteria){
        return knowledgeMapper.toDto(knowledgeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public KnowledgeDto findById(Long id) {
        Knowledge knowledge = knowledgeRepository.findById(id).orElseGet(Knowledge::new);
        ValidationUtil.isNull(knowledge.getId(),"Knowledge","id",id);
        return knowledgeMapper.toDto(knowledge);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KnowledgeDto create(Knowledge resources) {
        return knowledgeMapper.toDto(knowledgeRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Knowledge resources) {
        Knowledge knowledge = knowledgeRepository.findById(resources.getId()).orElseGet(Knowledge::new);
        ValidationUtil.isNull( knowledge.getId(),"Knowledge","id",resources.getId());
        knowledge.copy(resources);
        knowledgeRepository.save(knowledge);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            knowledgeRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<KnowledgeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (KnowledgeDto knowledge : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", knowledge.getName());
            map.put("描述", knowledge.getDescription());
            map.put("创建时间", knowledge.getCreateTime());
            map.put("更新时间", knowledge.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
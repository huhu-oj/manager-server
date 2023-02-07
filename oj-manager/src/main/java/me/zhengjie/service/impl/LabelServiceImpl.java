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

import me.zhengjie.domain.Label;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.repository.LabelRepository;
import me.zhengjie.service.LabelService;
import me.zhengjie.service.dto.LabelDto;
import me.zhengjie.service.dto.LabelQueryCriteria;
import me.zhengjie.service.mapstruct.LabelMapper;
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
* @date 2023-02-07
**/
@Service
@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;

    @Override
    public Map<String,Object> queryAll(LabelQueryCriteria criteria, Pageable pageable){
        Page<Label> page = labelRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(labelMapper::toDto));
    }

    @Override
    public List<LabelDto> queryAll(LabelQueryCriteria criteria){
        return labelMapper.toDto(labelRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public LabelDto findById(Long id) {
        Label label = labelRepository.findById(id).orElseGet(Label::new);
        ValidationUtil.isNull(label.getId(),"Label","id",id);
        return labelMapper.toDto(label);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LabelDto create(Label resources) {
        return labelMapper.toDto(labelRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Label resources) {
        Label label = labelRepository.findById(resources.getId()).orElseGet(Label::new);
        ValidationUtil.isNull( label.getId(),"Label","id",resources.getId());
        label.copy(resources);
        labelRepository.save(label);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            labelRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<LabelDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (LabelDto label : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("标签名", label.getName());
            map.put("链接", label.getUrl());
            map.put("创建时间", label.getCreateTime());
            map.put(" updateTime",  label.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
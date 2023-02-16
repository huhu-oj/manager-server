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
import me.zhengjie.domain.StandardIo;
import me.zhengjie.repository.StandardIoRepository;
import me.zhengjie.service.StandardIoService;
import me.zhengjie.service.dto.StandardIoDto;
import me.zhengjie.service.dto.StandardIoQueryCriteria;
import me.zhengjie.service.mapstruct.StandardIoMapper;
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
public class StandardIoServiceImpl implements StandardIoService {

    private final StandardIoRepository standardIoRepository;
    private final StandardIoMapper standardIoMapper;

    @Override
    public Map<String,Object> queryAll(StandardIoQueryCriteria criteria, Pageable pageable){
        Page<StandardIo> page = standardIoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(standardIoMapper::toDto));
    }

    @Override
    public List<StandardIoDto> queryAll(StandardIoQueryCriteria criteria){
        return standardIoMapper.toDto(standardIoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public StandardIoDto findById(Long id) {
        StandardIo standardIo = standardIoRepository.findById(id).orElseGet(StandardIo::new);
        ValidationUtil.isNull(standardIo.getId(),"StandardIo","id",id);
        return standardIoMapper.toDto(standardIo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StandardIoDto create(StandardIo resources) {
        return standardIoMapper.toDto(standardIoRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(StandardIo resources) {
        StandardIo standardIo = standardIoRepository.findById(resources.getId()).orElseGet(StandardIo::new);
        ValidationUtil.isNull( standardIo.getId(),"StandardIo","id",resources.getId());
        standardIo.copy(resources);
        standardIoRepository.save(standardIo);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            standardIoRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<StandardIoDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StandardIoDto standardIo : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("输入", standardIo.getInput());
            map.put("输出", standardIo.getOutput());
            map.put("所属题目", standardIo.getProblem().getTitle());
            map.put(" createTime",  standardIo.getCreateTime());
            map.put(" updateTime",  standardIo.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
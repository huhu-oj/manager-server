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
import me.zhengjie.domain.ExecuteResult;
import me.zhengjie.repository.ExecuteResultRepository;
import me.zhengjie.service.ExecuteResultService;
import me.zhengjie.service.dto.ExecuteResultDto;
import me.zhengjie.service.dto.ExecuteResultQueryCriteria;
import me.zhengjie.service.mapstruct.ExecuteResultMapper;
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
public class ExecuteResultServiceImpl implements ExecuteResultService {

    private final ExecuteResultRepository executeResultRepository;
    private final ExecuteResultMapper executeResultMapper;

    @Override
    public Map<String,Object> queryAll(ExecuteResultQueryCriteria criteria, Pageable pageable){
        Page<ExecuteResult> page = executeResultRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(executeResultMapper::toDto));
    }

    @Override
    public List<ExecuteResultDto> queryAll(ExecuteResultQueryCriteria criteria){
        return executeResultMapper.toDto(executeResultRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ExecuteResultDto findById(Integer id) {
        ExecuteResult executeResult = executeResultRepository.findById(id).orElseGet(ExecuteResult::new);
        ValidationUtil.isNull(executeResult.getId(),"ExecuteResult","id",id);
        return executeResultMapper.toDto(executeResult);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExecuteResultDto create(ExecuteResult resources) {
        return executeResultMapper.toDto(executeResultRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ExecuteResult resources) {
        ExecuteResult executeResult = executeResultRepository.findById(resources.getId()).orElseGet(ExecuteResult::new);
        ValidationUtil.isNull( executeResult.getId(),"ExecuteResult","id",resources.getId());
        executeResult.copy(resources);
        executeResultRepository.save(executeResult);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            executeResultRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ExecuteResultDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ExecuteResultDto executeResult : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", executeResult.getName());
            map.put("颜色", executeResult.getColor());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
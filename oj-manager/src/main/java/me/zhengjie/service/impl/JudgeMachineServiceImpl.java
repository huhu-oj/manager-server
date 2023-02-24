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

import me.zhengjie.domain.JudgeMachine;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.repository.JudgeMachineRepository;
import me.zhengjie.service.JudgeMachineService;
import me.zhengjie.service.dto.JudgeMachineDto;
import me.zhengjie.service.dto.JudgeMachineQueryCriteria;
import me.zhengjie.service.mapstruct.JudgeMachineMapper;
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
* @date 2023-02-13
**/
@Service
@RequiredArgsConstructor
public class JudgeMachineServiceImpl implements JudgeMachineService {

    private final JudgeMachineRepository judgeMachineRepository;
    private final JudgeMachineMapper judgeMachineMapper;

    @Override
    public Map<String,Object> queryAll(JudgeMachineQueryCriteria criteria, Pageable pageable){
        Page<JudgeMachine> page = judgeMachineRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(judgeMachineMapper::toDto));
    }

    @Override
    public List<JudgeMachineDto> queryAll(JudgeMachineQueryCriteria criteria){
        return judgeMachineMapper.toDto(judgeMachineRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public JudgeMachineDto findById(Long id) {
        JudgeMachine judgeMachine = judgeMachineRepository.findById(id).orElseGet(JudgeMachine::new);
        ValidationUtil.isNull(judgeMachine.getId(),"JudgeMachine","id",id);
        return judgeMachineMapper.toDto(judgeMachine);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JudgeMachineDto create(JudgeMachine resources) {
        return judgeMachineMapper.toDto(judgeMachineRepository.save(resources));
    }

    @Override
    public JudgeMachineDto save(JudgeMachine resources) {
        if (resources.getId() != null) {
            JudgeMachine judgeMachine = judgeMachineRepository.findById(resources.getId()).orElseGet(JudgeMachine::new);
            Boolean enabled = judgeMachine.getEnabled();
            judgeMachine.copy(resources);
            judgeMachine.setEnabled(enabled);
            return judgeMachineMapper.toDto(judgeMachineRepository.save(judgeMachine));
        } else {
            return create(resources);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(JudgeMachine resources) {
        JudgeMachine judgeMachine = judgeMachineRepository.findById(resources.getId()).orElseGet(JudgeMachine::new);
        ValidationUtil.isNull( judgeMachine.getId(),"JudgeMachine","id",resources.getId());
        judgeMachine.copy(resources);
        judgeMachineRepository.save(judgeMachine);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            judgeMachineRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<JudgeMachineDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (JudgeMachineDto judgeMachine : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", judgeMachine.getName());
            map.put("主机账号", judgeMachine.getUsername());
            map.put("密码", judgeMachine.getPassword());
            map.put("主机地址", judgeMachine.getUrl());
            map.put("是否启动", judgeMachine.getEnabled());
            map.put(" createTime",  judgeMachine.getCreateTime());
            map.put(" updateTime",  judgeMachine.getUpdateTime());
            map.put("支持的语言", judgeMachine.getSupportLanguage());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
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

import me.zhengjie.domain.OjRole;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.repository.OjRoleRepository;
import me.zhengjie.service.OjRoleService;
import me.zhengjie.service.dto.OjRoleDto;
import me.zhengjie.service.dto.OjRoleQueryCriteria;
import me.zhengjie.service.mapstruct.OjRoleMapper;
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
@Deprecated
public class OjRoleServiceImpl implements OjRoleService {

    private final OjRoleRepository ojRoleRepository;
    private final OjRoleMapper ojRoleMapper;

    @Override
    public Map<String,Object> queryAll(OjRoleQueryCriteria criteria, Pageable pageable){
        Page<OjRole> page = ojRoleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(ojRoleMapper::toDto));
    }

    @Override
    public List<OjRoleDto> queryAll(OjRoleQueryCriteria criteria){
        return ojRoleMapper.toDto(ojRoleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public OjRoleDto findById(Long id) {
        OjRole ojRole = ojRoleRepository.findById(id).orElseGet(OjRole::new);
        ValidationUtil.isNull(ojRole.getId(),"OjRole","id",id);
        return ojRoleMapper.toDto(ojRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OjRoleDto create(OjRole resources) {
        return ojRoleMapper.toDto(ojRoleRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(OjRole resources) {
        OjRole ojRole = ojRoleRepository.findById(resources.getId()).orElseGet(OjRole::new);
        ValidationUtil.isNull( ojRole.getId(),"OjRole","id",resources.getId());
        ojRole.copy(resources);
        ojRoleRepository.save(ojRole);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            ojRoleRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<OjRoleDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (OjRoleDto ojRole : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", ojRole.getName());
            map.put(" createTime",  ojRole.getCreateTime());
            map.put(" updateTime",  ojRole.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}

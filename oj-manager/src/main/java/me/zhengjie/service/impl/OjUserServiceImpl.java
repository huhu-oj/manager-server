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

import me.zhengjie.domain.OjUser;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.repository.OjUserRepository;
import me.zhengjie.service.OjUserService;
import me.zhengjie.service.dto.OjUserDto;
import me.zhengjie.service.dto.OjUserQueryCriteria;
import me.zhengjie.service.mapstruct.OjUserMapper;
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
public class OjUserServiceImpl implements OjUserService {

    private final OjUserRepository ojUserRepository;
    private final OjUserMapper ojUserMapper;

    @Override
    public Map<String,Object> queryAll(OjUserQueryCriteria criteria, Pageable pageable){
        Page<OjUser> page = ojUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(ojUserMapper::toDto));
    }

    @Override
    public List<OjUserDto> queryAll(OjUserQueryCriteria criteria){
        return ojUserMapper.toDto(ojUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public OjUserDto findById(Long id) {
        OjUser ojUser = ojUserRepository.findById(id).orElseGet(OjUser::new);
        ValidationUtil.isNull(ojUser.getId(),"OjUser","id",id);
        return ojUserMapper.toDto(ojUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OjUserDto create(OjUser resources) {
        return ojUserMapper.toDto(ojUserRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(OjUser resources) {
        OjUser ojUser = ojUserRepository.findById(resources.getId()).orElseGet(OjUser::new);
        ValidationUtil.isNull( ojUser.getId(),"OjUser","id",resources.getId());
        ojUser.copy(resources);
        ojUserRepository.save(ojUser);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            ojUserRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<OjUserDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (OjUserDto ojUser : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", ojUser.getName());
            map.put("密码", ojUser.getPassword());
            map.put("班级", ojUser.getClassId());
            map.put(" createTime",  ojUser.getCreateTime());
            map.put(" updateTime",  ojUser.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}

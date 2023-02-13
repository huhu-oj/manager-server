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

import me.zhengjie.domain.Clazz;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.repository.ClazzRepository;
import me.zhengjie.service.ClazzService;
import me.zhengjie.service.dto.ClazzDto;
import me.zhengjie.service.dto.ClazzQueryCriteria;
import me.zhengjie.service.mapstruct.ClazzMapper;
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
public class ClazzServiceImpl implements ClazzService {

    private final ClazzRepository clazzRepository;
    private final ClazzMapper clazzMapper;

    @Override
    public Map<String,Object> queryAll(ClazzQueryCriteria criteria, Pageable pageable){
        Page<Clazz> page = clazzRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(clazzMapper::toDto));
    }

    @Override
    public List<ClazzDto> queryAll(ClazzQueryCriteria criteria){
        return clazzMapper.toDto(clazzRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ClazzDto findById(Long id) {
        Clazz clazz = clazzRepository.findById(id).orElseGet(Clazz::new);
        ValidationUtil.isNull(clazz.getId(),"Clazz","id",id);
        return clazzMapper.toDto(clazz);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClazzDto create(Clazz resources) {
        return clazzMapper.toDto(clazzRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Clazz resources) {
        Clazz clazz = clazzRepository.findById(resources.getId()).orElseGet(Clazz::new);
        ValidationUtil.isNull( clazz.getId(),"Clazz","id",resources.getId());
        clazz.copy(resources);
        clazzRepository.save(clazz);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            clazzRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ClazzDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ClazzDto clazz : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", clazz.getName());
            map.put(" createTime",  clazz.getCreateTime());
            map.put(" updateTime",  clazz.getUpdateTime());
            map.put("年级", clazz.getGradeId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
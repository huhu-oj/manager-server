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

import me.zhengjie.domain.Language;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.repository.LanguageRepository;
import me.zhengjie.service.LanguageService;
import me.zhengjie.service.dto.LanguageDto;
import me.zhengjie.service.dto.LanguageQueryCriteria;
import me.zhengjie.service.mapstruct.LanguageMapper;
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
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;
    private final LanguageMapper languageMapper;

    @Override
    public Map<String,Object> queryAll(LanguageQueryCriteria criteria, Pageable pageable){
        Page<Language> page = languageRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(languageMapper::toDto));
    }

    @Override
    public List<LanguageDto> queryAll(LanguageQueryCriteria criteria){
        return languageMapper.toDto(languageRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public LanguageDto findById(Long id) {
        Language language = languageRepository.findById(id).orElseGet(Language::new);
        ValidationUtil.isNull(language.getId(),"Language","id",id);
        return languageMapper.toDto(language);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LanguageDto create(Language resources) {
        return languageMapper.toDto(languageRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Language resources) {
        Language language = languageRepository.findById(resources.getId()).orElseGet(Language::new);
        ValidationUtil.isNull( language.getId(),"Language","id",resources.getId());
        language.copy(resources);
        languageRepository.save(language);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            languageRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<LanguageDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (LanguageDto language : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("语言名称", language.getName());
            map.put("编译语句", language.getCompileStatement());
            map.put("创建时间", language.getCreateTime());
            map.put("更新时间", language.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
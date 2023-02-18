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
import me.zhengjie.domain.Hint;
import me.zhengjie.domain.Problem;
import me.zhengjie.repository.HintRepository;
import me.zhengjie.repository.ProblemRepository;
import me.zhengjie.service.HintService;
import me.zhengjie.service.dto.HintDto;
import me.zhengjie.service.dto.HintQueryCriteria;
import me.zhengjie.service.mapstruct.HintMapper;
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
* @date 2023-02-14
**/
@Service
@RequiredArgsConstructor
public class HintServiceImpl implements HintService {

    private final HintRepository hintRepository;
    private final HintMapper hintMapper;

    private final ProblemRepository problemRepository;
    @Override
    public Map<String,Object> queryAll(HintQueryCriteria criteria, Pageable pageable){
        Page<Hint> page = hintRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(hintMapper::toDto));
    }

    @Override
    public List<HintDto> queryAll(HintQueryCriteria criteria){
        return hintMapper.toDto(hintRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public HintDto findById(Long id) {
        Hint hint = hintRepository.findById(id).orElseGet(Hint::new);
        ValidationUtil.isNull(hint.getId(),"Hint","id",id);
        return hintMapper.toDto(hint);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HintDto create(Hint resources) {
        Problem problem = problemRepository.findById(resources.getProblem().getId()).orElseThrow(RuntimeException::new);
        resources.setProblem(problem);
        return hintMapper.toDto(hintRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Hint resources) {
        Hint hint = hintRepository.findById(resources.getId()).orElseGet(Hint::new);
        ValidationUtil.isNull( hint.getId(),"Hint","id",resources.getId());
        hint.copy(resources);
        Problem problem = problemRepository.findById(resources.getProblem().getId()).orElseThrow(RuntimeException::new);
        hint.setProblem(problem);
        hintRepository.save(hint);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            hintRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<HintDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (HintDto hint : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("描述", hint.getDescription());
            map.put("所属题目", hint.getProblem().getTitle());
            map.put("创建时间", hint.getCreateTime());
            map.put("更新时间", hint.getUpdateTime());
            map.put("渲染文本", hint.getDescriptionHtml());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}

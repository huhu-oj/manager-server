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

import me.zhengjie.domain.Problem;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.repository.ProblemRepository;
import me.zhengjie.service.ProblemService;
import me.zhengjie.service.dto.ProblemDto;
import me.zhengjie.service.dto.ProblemQueryCriteria;
import me.zhengjie.service.mapstruct.ProblemMapper;
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
public class ProblemServiceImpl implements ProblemService {

    private final ProblemRepository problemRepository;
    private final ProblemMapper problemMapper;


    @Override
    public Map<String,Object> queryAll(ProblemQueryCriteria criteria, Pageable pageable){
        Page<Problem> page = problemRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(problemMapper::toDto));
    }


    @Override
    public List<ProblemDto> queryAll(ProblemQueryCriteria criteria){
        return problemMapper.toDto(problemRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ProblemDto findById(Long id) {
        Problem problem = problemRepository.findById(id).orElseGet(Problem::new);
        ValidationUtil.isNull(problem.getId(),"Problem","id",id);
        return problemMapper.toDto(problem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProblemDto create(Problem resources) {
        return problemMapper.toDto(problemRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Problem resources) {
        Problem problem = problemRepository.findById(resources.getId()).orElseGet(Problem::new);
        ValidationUtil.isNull( problem.getId(),"Problem","id",resources.getId());
        problem.copy(resources);
        problemRepository.save(problem);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            problemRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ProblemDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ProblemDto problem : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("标题", problem.getTitle());
            map.put("描述", problem.getDescription());
            map.put("创建时间", problem.getCreateTime());
            map.put("更新时间", problem.getUpdateTime());
            map.put("渲染文本", problem.getDescriptionHtml());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
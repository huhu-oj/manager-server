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
import me.zhengjie.domain.Label;
import me.zhengjie.domain.Problem;
import me.zhengjie.domain.Solution;
import me.zhengjie.repository.LabelRepository;
import me.zhengjie.repository.ProblemRepository;
import me.zhengjie.repository.SolutionRepository;
import me.zhengjie.service.SolutionService;
import me.zhengjie.service.dto.SolutionDto;
import me.zhengjie.service.dto.SolutionQueryCriteria;
import me.zhengjie.service.mapstruct.SolutionMapper;
import me.zhengjie.utils.*;
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
import java.util.stream.Collectors;

/**
* @website https://eladmin.vip
* @description 服务实现
* @author nwl
* @date 2023-02-14
**/
@Service
@RequiredArgsConstructor
public class SolutionServiceImpl implements SolutionService {

    private final SolutionRepository solutionRepository;
    private final SolutionMapper solutionMapper;

    private final ProblemRepository problemRepository;
    private final LabelRepository labelRepository;
    @Override
    public Map<String,Object> queryAll(SolutionQueryCriteria criteria, Pageable pageable){
        Page<Solution> page = solutionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(solutionMapper::toDto));
    }

    @Override
    public List<SolutionDto> queryAll(SolutionQueryCriteria criteria){
        return solutionMapper.toDto(solutionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public SolutionDto findById(Long id) {
        Solution solution = solutionRepository.findById(id).orElseGet(Solution::new);
        ValidationUtil.isNull(solution.getId(),"Solution","id",id);
        return solutionMapper.toDto(solution);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SolutionDto create(Solution resources) {
        Problem problem = problemRepository.findById(resources.getProblem().getId()).orElseThrow(RuntimeException::new);
        resources.setProblem(problem);

        List<Label> labels = labelRepository.findAllById(resources.getLabels().stream().map(Label::getId).collect(Collectors.toList()));
        resources.setLabels(labels);
        resources.setUserId(SecurityUtils.getCurrentUserId());
        return solutionMapper.toDto(solutionRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Solution resources) {
        Solution solution = solutionRepository.findById(resources.getId()).orElseGet(Solution::new);
        ValidationUtil.isNull( solution.getId(),"Solution","id",resources.getId());

        Problem problem = problemRepository.findById(resources.getProblem().getId()).orElseThrow(RuntimeException::new);
        resources.setProblem(problem);

        List<Label> labels = labelRepository.findAllById(resources.getLabels().stream().map(Label::getId).collect(Collectors.toList()));
        resources.setLabels(labels);

        resources.setUserId(SecurityUtils.getCurrentUserId());

        solution.copy(resources);
        solutionRepository.save(solution);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            solutionRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<SolutionDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SolutionDto solution : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("标题", solution.getTitle());
            map.put("描述", solution.getDescription());
            map.put("所属题目", solution.getProblem().getTitle());
            map.put("创建时间", solution.getCreateTime());
            map.put("更新时间", solution.getUpdateTime());
            map.put("渲染文本", solution.getDescriptionHtml());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}

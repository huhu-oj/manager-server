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
import me.zhengjie.domain.ExaminationPaper;
import me.zhengjie.domain.ExaminationPaperProblem;
import me.zhengjie.repository.ExaminationPaperProblemRepository;
import me.zhengjie.repository.ExaminationPaperRepository;
import me.zhengjie.service.ExaminationPaperService;
import me.zhengjie.service.dto.ExaminationPaperDto;
import me.zhengjie.service.dto.ExaminationPaperProblemQueryCriteria;
import me.zhengjie.service.dto.ExaminationPaperQueryCriteria;
import me.zhengjie.service.mapstruct.ExaminationPaperMapper;
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
public class ExaminationPaperServiceImpl implements ExaminationPaperService {

    private final ExaminationPaperRepository examinationPaperRepository;
    private final ExaminationPaperMapper examinationPaperMapper;

    private final ExaminationPaperProblemRepository examinationPaperProblemRepository;

    @Override
    public Map<String,Object> queryAll(ExaminationPaperQueryCriteria criteria, Pageable pageable){
        Page<ExaminationPaper> page = examinationPaperRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(examinationPaperMapper::toDto));
    }

    @Override
    public List<ExaminationPaperDto> queryAll(ExaminationPaperQueryCriteria criteria){
        return examinationPaperMapper.toDto(examinationPaperRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ExaminationPaperDto findById(Long id) {
        ExaminationPaper examinationPaper = examinationPaperRepository.findById(id).orElseGet(ExaminationPaper::new);
        ValidationUtil.isNull(examinationPaper.getId(),"ExaminationPaper","id",id);
        return examinationPaperMapper.toDto(examinationPaper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExaminationPaperDto create(ExaminationPaper resources) {
        return examinationPaperMapper.toDto(examinationPaperRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ExaminationPaper resources) {
        ExaminationPaper examinationPaper = examinationPaperRepository.findById(resources.getId()).orElseGet(ExaminationPaper::new);
        ValidationUtil.isNull( examinationPaper.getId(),"ExaminationPaper","id",resources.getId());
         //手动删除关联表
        ExaminationPaperProblemQueryCriteria criteria = new ExaminationPaperProblemQueryCriteria();
        criteria.setExaminationPaperId(resources.getId());
        List<ExaminationPaperProblem> todelete = examinationPaperProblemRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        examinationPaperProblemRepository.deleteAll(todelete);

        examinationPaper.copy(resources);
        examinationPaperRepository.save(examinationPaper);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            examinationPaperRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ExaminationPaperDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ExaminationPaperDto examinationPaper : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", examinationPaper.getName());
            map.put("描述", examinationPaper.getDescription());
            map.put("创建时间", examinationPaper.getCreateTime());
            map.put("更新时间", examinationPaper.getUpdateTime());
            map.put("描述渲染文本", examinationPaper.getDescriptionHtml());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}

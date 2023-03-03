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
import me.zhengjie.domain.Test;
import me.zhengjie.repository.ExaminationPaperRepository;
import me.zhengjie.repository.TestRepository;
import me.zhengjie.service.TestService;
import me.zhengjie.service.dto.TestDto;
import me.zhengjie.service.dto.TestQueryCriteria;
import me.zhengjie.service.mapstruct.TestMapper;
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
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;
    private final TestMapper testMapper;

    private final ExaminationPaperRepository examinationPaperRepository;

    @Override
    public Map<String,Object> queryAll(TestQueryCriteria criteria, Pageable pageable){
        Page<Test> page = testRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(testMapper::toDto));
    }

    @Override
    public List<TestDto> queryAll(TestQueryCriteria criteria){
        return testMapper.toDto(testRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public TestDto findById(Long id) {
        Test test = testRepository.findById(id).orElseGet(Test::new);
        ValidationUtil.isNull(test.getId(),"Test","id",id);
        return testMapper.toDto(test);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TestDto create(Test resources) {
        ExaminationPaper paper = examinationPaperRepository.findById(resources.getExaminationPaper().getId()).orElseThrow(RuntimeException::new);
        resources.setExaminationPaper(paper);
        return testMapper.toDto(testRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Test resources) {
        Test test = testRepository.findById(resources.getId()).orElseGet(Test::new);
        ValidationUtil.isNull( test.getId(),"Test","id",resources.getId());
        test.copy(resources);
        ExaminationPaper paper = examinationPaperRepository.findById(resources.getExaminationPaper().getId()).orElseThrow(RuntimeException::new);
        test.setExaminationPaper(paper);
        testRepository.save(test);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            testRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<TestDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TestDto test : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("标题", test.getTitle());
            map.put("备注", test.getDescription());
            map.put("试卷id", test.getExaminationPaper().getName());
            map.put("开始时间", test.getStartTime());
            map.put("结束时间", test.getEndTime());
            map.put("是否启用", test.getEnabled());
            map.put("创建时间", test.getCreateTime());
            map.put("更新时间", test.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<TestDto> batchQueryById(List<Long> ids) {
        return testMapper.toDto(testRepository.findAllById(ids));
    }
}

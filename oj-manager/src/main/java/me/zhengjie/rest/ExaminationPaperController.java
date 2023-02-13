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
package me.zhengjie.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.domain.ExaminationPaper;
import me.zhengjie.service.ExaminationPaperService;
import me.zhengjie.service.dto.ExaminationPaperQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://eladmin.vip
* @author nwl
* @date 2023-02-13
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "试卷管理")
@RequestMapping("/api/examinationPaper")
public class ExaminationPaperController {

    private final ExaminationPaperService examinationPaperService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('examinationPaper:list')")
    public void exportExaminationPaper(HttpServletResponse response, ExaminationPaperQueryCriteria criteria) throws IOException {
        examinationPaperService.download(examinationPaperService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询试卷")
    @ApiOperation("查询试卷")
    @PreAuthorize("@el.check('examinationPaper:list')")
    public ResponseEntity<Object> queryExaminationPaper(ExaminationPaperQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(examinationPaperService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增试卷")
    @ApiOperation("新增试卷")
    @PreAuthorize("@el.check('examinationPaper:add')")
    public ResponseEntity<Object> createExaminationPaper(@Validated @RequestBody ExaminationPaper resources){
        return new ResponseEntity<>(examinationPaperService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改试卷")
    @ApiOperation("修改试卷")
    @PreAuthorize("@el.check('examinationPaper:edit')")
    public ResponseEntity<Object> updateExaminationPaper(@Validated @RequestBody ExaminationPaper resources){
        examinationPaperService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除试卷")
    @ApiOperation("删除试卷")
    @PreAuthorize("@el.check('examinationPaper:del')")
    public ResponseEntity<Object> deleteExaminationPaper(@RequestBody Long[] ids) {
        examinationPaperService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
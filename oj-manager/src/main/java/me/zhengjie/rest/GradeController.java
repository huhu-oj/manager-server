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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.domain.Grade;
import me.zhengjie.service.GradeService;
import me.zhengjie.service.dto.GradeQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* @website https://eladmin.vip
* @author nwl
* @date 2023-02-13
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "oj:年级管理")
@RequestMapping("/api/grade")
@Deprecated
public class GradeController {

    private final GradeService gradeService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('grade:list')")
    public void exportGrade(HttpServletResponse response, GradeQueryCriteria criteria) throws IOException {
        gradeService.download(gradeService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询年级")
    @ApiOperation("查询年级")
    @PreAuthorize("@el.check('grade:list')")
    public ResponseEntity<Object> queryGrade(GradeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(gradeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增年级")
    @ApiOperation("新增年级")
    @PreAuthorize("@el.check('grade:add')")
    public ResponseEntity<Object> createGrade(@Validated @RequestBody Grade resources){
        return new ResponseEntity<>(gradeService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改年级")
    @ApiOperation("修改年级")
    @PreAuthorize("@el.check('grade:edit')")
    public ResponseEntity<Object> updateGrade(@Validated @RequestBody Grade resources){
        gradeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除年级")
    @ApiOperation("删除年级")
    @PreAuthorize("@el.check('grade:del')")
    public ResponseEntity<Object> deleteGrade(@RequestBody Long[] ids) {
        gradeService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("all")
    @Log("查询所有年级")
    @ApiOperation("查询所有年级")
    @PreAuthorize("@el.check('grade:list')")
    public ResponseEntity<Object> queryAllGrade(GradeQueryCriteria criteria){
        return new ResponseEntity<>(gradeService.queryAll(criteria),HttpStatus.OK);
    }
}

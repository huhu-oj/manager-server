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
import me.zhengjie.domain.Clazz;
import me.zhengjie.service.ClazzService;
import me.zhengjie.service.dto.ClazzQueryCriteria;
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
@Api(tags = "班级管理")
@RequestMapping("/api/clazz")
public class ClazzController {

    private final ClazzService clazzService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('clazz:list')")
    public void exportClazz(HttpServletResponse response, ClazzQueryCriteria criteria) throws IOException {
        clazzService.download(clazzService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询班级")
    @ApiOperation("查询班级")
    @PreAuthorize("@el.check('clazz:list')")
    public ResponseEntity<Object> queryClazz(ClazzQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(clazzService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增班级")
    @ApiOperation("新增班级")
    @PreAuthorize("@el.check('clazz:add')")
    public ResponseEntity<Object> createClazz(@Validated @RequestBody Clazz resources){
        return new ResponseEntity<>(clazzService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改班级")
    @ApiOperation("修改班级")
    @PreAuthorize("@el.check('clazz:edit')")
    public ResponseEntity<Object> updateClazz(@Validated @RequestBody Clazz resources){
        clazzService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除班级")
    @ApiOperation("删除班级")
    @PreAuthorize("@el.check('clazz:del')")
    public ResponseEntity<Object> deleteClazz(@RequestBody Long[] ids) {
        clazzService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
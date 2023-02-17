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
import me.zhengjie.domain.Test;
import me.zhengjie.service.TestService;
import me.zhengjie.service.dto.TestQueryCriteria;
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
@Api(tags = "oj:测验管理")
@RequestMapping("/api/test")
public class TestController {

    private final TestService testService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('test:list')")
    public void exportTest(HttpServletResponse response, TestQueryCriteria criteria) throws IOException {
        testService.download(testService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询测验")
    @ApiOperation("查询测验")
    @PreAuthorize("@el.check('test:list')")
    public ResponseEntity<Object> queryTest(TestQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(testService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增测验")
    @ApiOperation("新增测验")
    @PreAuthorize("@el.check('test:add')")
    public ResponseEntity<Object> createTest(@Validated @RequestBody Test resources){
        return new ResponseEntity<>(testService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改测验")
    @ApiOperation("修改测验")
    @PreAuthorize("@el.check('test:edit')")
    public ResponseEntity<Object> updateTest(@Validated @RequestBody Test resources){
        testService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除测验")
    @ApiOperation("删除测验")
    @PreAuthorize("@el.check('test:del')")
    public ResponseEntity<Object> deleteTest(@RequestBody Long[] ids) {
        testService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
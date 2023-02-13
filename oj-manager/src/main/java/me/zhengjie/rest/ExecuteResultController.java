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
import me.zhengjie.domain.ExecuteResult;
import me.zhengjie.service.ExecuteResultService;
import me.zhengjie.service.dto.ExecuteResultQueryCriteria;
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
@Api(tags = "执行结果管理")
@RequestMapping("/api/executeResult")
public class ExecuteResultController {

    private final ExecuteResultService executeResultService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('executeResult:list')")
    public void exportExecuteResult(HttpServletResponse response, ExecuteResultQueryCriteria criteria) throws IOException {
        executeResultService.download(executeResultService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询执行结果")
    @ApiOperation("查询执行结果")
    @PreAuthorize("@el.check('executeResult:list')")
    public ResponseEntity<Object> queryExecuteResult(ExecuteResultQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(executeResultService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增执行结果")
    @ApiOperation("新增执行结果")
    @PreAuthorize("@el.check('executeResult:add')")
    public ResponseEntity<Object> createExecuteResult(@Validated @RequestBody ExecuteResult resources){
        return new ResponseEntity<>(executeResultService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改执行结果")
    @ApiOperation("修改执行结果")
    @PreAuthorize("@el.check('executeResult:edit')")
    public ResponseEntity<Object> updateExecuteResult(@Validated @RequestBody ExecuteResult resources){
        executeResultService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除执行结果")
    @ApiOperation("删除执行结果")
    @PreAuthorize("@el.check('executeResult:del')")
    public ResponseEntity<Object> deleteExecuteResult(@RequestBody Integer[] ids) {
        executeResultService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
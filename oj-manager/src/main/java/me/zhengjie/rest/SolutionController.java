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
import me.zhengjie.domain.Solution;
import me.zhengjie.service.SolutionService;
import me.zhengjie.service.dto.SolutionQueryCriteria;
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
@Api(tags = "题解管理")
@RequestMapping("/api/solution")
public class SolutionController {

    private final SolutionService solutionService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('solution:list')")
    public void exportSolution(HttpServletResponse response, SolutionQueryCriteria criteria) throws IOException {
        solutionService.download(solutionService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询题解")
    @ApiOperation("查询题解")
    @PreAuthorize("@el.check('solution:list')")
    public ResponseEntity<Object> querySolution(SolutionQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(solutionService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增题解")
    @ApiOperation("新增题解")
    @PreAuthorize("@el.check('solution:add')")
    public ResponseEntity<Object> createSolution(@Validated @RequestBody Solution resources){
        return new ResponseEntity<>(solutionService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改题解")
    @ApiOperation("修改题解")
    @PreAuthorize("@el.check('solution:edit')")
    public ResponseEntity<Object> updateSolution(@Validated @RequestBody Solution resources){
        solutionService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除题解")
    @ApiOperation("删除题解")
    @PreAuthorize("@el.check('solution:del')")
    public ResponseEntity<Object> deleteSolution(@RequestBody Long[] ids) {
        solutionService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
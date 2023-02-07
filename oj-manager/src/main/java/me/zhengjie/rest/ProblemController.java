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
import me.zhengjie.domain.Problem;
import me.zhengjie.service.ProblemService;
import me.zhengjie.service.dto.ProblemQueryCriteria;
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
* @date 2023-02-07
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "问题管理")
@RequestMapping("/api/problem")
public class ProblemController {

    private final ProblemService problemService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('problem:list')")
    public void exportProblem(HttpServletResponse response, ProblemQueryCriteria criteria) throws IOException {
        problemService.download(problemService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询问题")
    @ApiOperation("查询问题")
    @PreAuthorize("@el.check('problem:list')")
    public ResponseEntity<Object> queryProblem(ProblemQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(problemService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增问题")
    @ApiOperation("新增问题")
    @PreAuthorize("@el.check('problem:add')")
    public ResponseEntity<Object> createProblem(@Validated @RequestBody Problem resources){
        return new ResponseEntity<>(problemService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改问题")
    @ApiOperation("修改问题")
    @PreAuthorize("@el.check('problem:edit')")
    public ResponseEntity<Object> updateProblem(@Validated @RequestBody Problem resources){
        problemService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除问题")
    @ApiOperation("删除问题")
    @PreAuthorize("@el.check('problem:del')")
    public ResponseEntity<Object> deleteProblem(@RequestBody Long[] ids) {
        problemService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
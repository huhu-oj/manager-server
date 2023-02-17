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
import me.zhengjie.domain.Label;
import me.zhengjie.service.LabelService;
import me.zhengjie.service.dto.LabelQueryCriteria;
import org.springframework.beans.BeanUtils;
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
* @date 2023-02-14
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "oj:标签管理")
@RequestMapping("/api/label")
public class LabelController {

    private final LabelService labelService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('label:list')")
    public void exportLabel(HttpServletResponse response, LabelQueryCriteria criteria) throws IOException {
        labelService.download(labelService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询标签")
    @ApiOperation("查询标签")
    @PreAuthorize("@el.check('label:list')")
    public ResponseEntity<Object> queryLabel(LabelQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(labelService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增标签")
    @ApiOperation("新增标签")
    @PreAuthorize("@el.check('label:add')")
    public ResponseEntity<Object> createLabel(@Validated @RequestBody Label resources){
        return new ResponseEntity<>(labelService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改标签")
    @ApiOperation("修改标签")
    @PreAuthorize("@el.check('label:edit')")
    public ResponseEntity<Object> updateLabel(@Validated @RequestBody Label resources){
        labelService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除标签")
    @ApiOperation("删除标签")
    @PreAuthorize("@el.check('label:del')")
    public ResponseEntity<Object> deleteLabel(@RequestBody Long[] ids) {
        labelService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("all")
    @Log("查询所有标签")
    @ApiOperation("查询所有标签")
    @PreAuthorize("@el.check('label:list')")
    public ResponseEntity<Object> queryAllLabel(LabelQueryCriteria criteria){
        return new ResponseEntity<>(labelService.queryAll(criteria),HttpStatus.OK);
    }
}
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
import me.zhengjie.domain.StandardIo;
import me.zhengjie.service.StandardIoService;
import me.zhengjie.service.dto.StandardIoQueryCriteria;
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
@Api(tags = "标准输入输出管理")
@RequestMapping("/api/standardIo")
public class StandardIoController {

    private final StandardIoService standardIoService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('standardIo:list')")
    public void exportStandardIo(HttpServletResponse response, StandardIoQueryCriteria criteria) throws IOException {
        standardIoService.download(standardIoService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询标准输入输出")
    @ApiOperation("查询标准输入输出")
    @PreAuthorize("@el.check('standardIo:list')")
    public ResponseEntity<Object> queryStandardIo(StandardIoQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(standardIoService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增标准输入输出")
    @ApiOperation("新增标准输入输出")
    @PreAuthorize("@el.check('standardIo:add')")
    public ResponseEntity<Object> createStandardIo(@Validated @RequestBody StandardIo resources){
        return new ResponseEntity<>(standardIoService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改标准输入输出")
    @ApiOperation("修改标准输入输出")
    @PreAuthorize("@el.check('standardIo:edit')")
    public ResponseEntity<Object> updateStandardIo(@Validated @RequestBody StandardIo resources){
        standardIoService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除标准输入输出")
    @ApiOperation("删除标准输入输出")
    @PreAuthorize("@el.check('standardIo:del')")
    public ResponseEntity<Object> deleteStandardIo(@RequestBody Long[] ids) {
        standardIoService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
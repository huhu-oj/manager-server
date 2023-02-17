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
import me.zhengjie.domain.Hint;
import me.zhengjie.service.HintService;
import me.zhengjie.service.dto.HintQueryCriteria;
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
@Api(tags = "oj:提示管理")
@RequestMapping("/api/hint")
public class HintController {

    private final HintService hintService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('hint:list')")
    public void exportHint(HttpServletResponse response, HintQueryCriteria criteria) throws IOException {
        hintService.download(hintService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询提示")
    @ApiOperation("查询提示")
    @PreAuthorize("@el.check('hint:list')")
    public ResponseEntity<Object> queryHint(HintQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(hintService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增提示")
    @ApiOperation("新增提示")
    @PreAuthorize("@el.check('hint:add')")
    public ResponseEntity<Object> createHint(@Validated @RequestBody Hint resources){
        return new ResponseEntity<>(hintService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改提示")
    @ApiOperation("修改提示")
    @PreAuthorize("@el.check('hint:edit')")
    public ResponseEntity<Object> updateHint(@Validated @RequestBody Hint resources){
        hintService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除提示")
    @ApiOperation("删除提示")
    @PreAuthorize("@el.check('hint:del')")
    public ResponseEntity<Object> deleteHint(@RequestBody Long[] ids) {
        hintService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
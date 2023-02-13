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
import me.zhengjie.domain.OjUser;
import me.zhengjie.service.OjUserService;
import me.zhengjie.service.dto.OjUserQueryCriteria;
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
@Api(tags = "用户管理")
@RequestMapping("/api/ojUser")
public class OjUserController {

    private final OjUserService ojUserService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('ojUser:list')")
    public void exportOjUser(HttpServletResponse response, OjUserQueryCriteria criteria) throws IOException {
        ojUserService.download(ojUserService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询用户")
    @ApiOperation("查询用户")
    @PreAuthorize("@el.check('ojUser:list')")
    public ResponseEntity<Object> queryOjUser(OjUserQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(ojUserService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增用户")
    @ApiOperation("新增用户")
    @PreAuthorize("@el.check('ojUser:add')")
    public ResponseEntity<Object> createOjUser(@Validated @RequestBody OjUser resources){
        return new ResponseEntity<>(ojUserService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改用户")
    @ApiOperation("修改用户")
    @PreAuthorize("@el.check('ojUser:edit')")
    public ResponseEntity<Object> updateOjUser(@Validated @RequestBody OjUser resources){
        ojUserService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除用户")
    @ApiOperation("删除用户")
    @PreAuthorize("@el.check('ojUser:del')")
    public ResponseEntity<Object> deleteOjUser(@RequestBody Long[] ids) {
        ojUserService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
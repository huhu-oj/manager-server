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
import me.zhengjie.domain.Role;
import me.zhengjie.service.RoleService;
import me.zhengjie.service.dto.RoleQueryCriteria;
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
@Api(tags = "角色管理")
@RequestMapping("/api/role")
public class RoleController {

    private final RoleService roleService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('role:list')")
    public void exportRole(HttpServletResponse response, RoleQueryCriteria criteria) throws IOException {
        roleService.download(roleService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询角色")
    @ApiOperation("查询角色")
    @PreAuthorize("@el.check('role:list')")
    public ResponseEntity<Object> queryRole(RoleQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(roleService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增角色")
    @ApiOperation("新增角色")
    @PreAuthorize("@el.check('role:add')")
    public ResponseEntity<Object> createRole(@Validated @RequestBody Role resources){
        return new ResponseEntity<>(roleService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改角色")
    @ApiOperation("修改角色")
    @PreAuthorize("@el.check('role:edit')")
    public ResponseEntity<Object> updateRole(@Validated @RequestBody Role resources){
        roleService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除角色")
    @ApiOperation("删除角色")
    @PreAuthorize("@el.check('role:del')")
    public ResponseEntity<Object> deleteRole(@RequestBody Long[] ids) {
        roleService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
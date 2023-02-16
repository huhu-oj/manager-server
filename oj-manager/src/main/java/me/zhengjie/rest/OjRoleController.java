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
import me.zhengjie.domain.OjRole;
import me.zhengjie.service.OjRoleService;
import me.zhengjie.service.dto.OjRoleQueryCriteria;
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
@RequestMapping("/api/ojRole")
@Deprecated
public class OjRoleController {

    private final OjRoleService ojRoleService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('ojRole:list')")
    public void exportOjRole(HttpServletResponse response, OjRoleQueryCriteria criteria) throws IOException {
        ojRoleService.download(ojRoleService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询角色")
    @ApiOperation("查询角色")
    @PreAuthorize("@el.check('ojRole:list')")
    public ResponseEntity<Object> queryOjRole(OjRoleQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(ojRoleService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增角色")
    @ApiOperation("新增角色")
    @PreAuthorize("@el.check('ojRole:add')")
    public ResponseEntity<Object> createOjRole(@Validated @RequestBody OjRole resources){
        return new ResponseEntity<>(ojRoleService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改角色")
    @ApiOperation("修改角色")
    @PreAuthorize("@el.check('ojRole:edit')")
    public ResponseEntity<Object> updateOjRole(@Validated @RequestBody OjRole resources){
        ojRoleService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除角色")
    @ApiOperation("删除角色")
    @PreAuthorize("@el.check('ojRole:del')")
    public ResponseEntity<Object> deleteOjRole(@RequestBody Long[] ids) {
        ojRoleService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

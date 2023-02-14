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
import me.zhengjie.domain.Knowledge;
import me.zhengjie.service.KnowledgeService;
import me.zhengjie.service.dto.KnowledgeQueryCriteria;
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
@Api(tags = "知识点管理")
@RequestMapping("/api/knowledge")
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('knowledge:list')")
    public void exportKnowledge(HttpServletResponse response, KnowledgeQueryCriteria criteria) throws IOException {
        knowledgeService.download(knowledgeService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询知识点")
    @ApiOperation("查询知识点")
    @PreAuthorize("@el.check('knowledge:list')")
    public ResponseEntity<Object> queryKnowledge(KnowledgeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(knowledgeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增知识点")
    @ApiOperation("新增知识点")
    @PreAuthorize("@el.check('knowledge:add')")
    public ResponseEntity<Object> createKnowledge(@Validated @RequestBody Knowledge resources){
        return new ResponseEntity<>(knowledgeService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改知识点")
    @ApiOperation("修改知识点")
    @PreAuthorize("@el.check('knowledge:edit')")
    public ResponseEntity<Object> updateKnowledge(@Validated @RequestBody Knowledge resources){
        knowledgeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除知识点")
    @ApiOperation("删除知识点")
    @PreAuthorize("@el.check('knowledge:del')")
    public ResponseEntity<Object> deleteKnowledge(@RequestBody Long[] ids) {
        knowledgeService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
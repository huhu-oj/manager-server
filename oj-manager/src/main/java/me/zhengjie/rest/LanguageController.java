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
import me.zhengjie.domain.Language;
import me.zhengjie.service.LanguageService;
import me.zhengjie.service.dto.LanguageQueryCriteria;
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
* @date 2023-02-07
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "oj:编程语言管理")
@RequestMapping("/api/language")
public class LanguageController {

    private final LanguageService languageService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('language:list')")
    public void exportLanguage(HttpServletResponse response, LanguageQueryCriteria criteria) throws IOException {
        languageService.download(languageService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询编程语言")
    @ApiOperation("查询编程语言")
    @PreAuthorize("@el.check('language:list')")
    public ResponseEntity<Object> queryLanguage(LanguageQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(languageService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增编程语言")
    @ApiOperation("新增编程语言")
    @PreAuthorize("@el.check('language:add')")
    public ResponseEntity<Object> createLanguage(@Validated @RequestBody Language resources){
        return new ResponseEntity<>(languageService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改编程语言")
    @ApiOperation("修改编程语言")
    @PreAuthorize("@el.check('language:edit')")
    public ResponseEntity<Object> updateLanguage(@Validated @RequestBody Language resources){
        languageService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除编程语言")
    @ApiOperation("删除编程语言")
    @PreAuthorize("@el.check('language:del')")
    public ResponseEntity<Object> deleteLanguage(@RequestBody Long[] ids) {
        languageService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
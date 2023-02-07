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
import me.zhengjie.domain.AnswerRecord;
import me.zhengjie.service.AnswerRecordService;
import me.zhengjie.service.dto.AnswerRecordQueryCriteria;
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
@Api(tags = "做题记录管理")
@RequestMapping("/api/answerRecord")
public class AnswerRecordController {

    private final AnswerRecordService answerRecordService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('answerRecord:list')")
    public void exportAnswerRecord(HttpServletResponse response, AnswerRecordQueryCriteria criteria) throws IOException {
        answerRecordService.download(answerRecordService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询做题记录")
    @ApiOperation("查询做题记录")
    @PreAuthorize("@el.check('answerRecord:list')")
    public ResponseEntity<Object> queryAnswerRecord(AnswerRecordQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(answerRecordService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增做题记录")
    @ApiOperation("新增做题记录")
    @PreAuthorize("@el.check('answerRecord:add')")
    public ResponseEntity<Object> createAnswerRecord(@Validated @RequestBody AnswerRecord resources){
        return new ResponseEntity<>(answerRecordService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改做题记录")
    @ApiOperation("修改做题记录")
    @PreAuthorize("@el.check('answerRecord:edit')")
    public ResponseEntity<Object> updateAnswerRecord(@Validated @RequestBody AnswerRecord resources){
        answerRecordService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除做题记录")
    @ApiOperation("删除做题记录")
    @PreAuthorize("@el.check('answerRecord:del')")
    public ResponseEntity<Object> deleteAnswerRecord(@RequestBody Long[] ids) {
        answerRecordService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
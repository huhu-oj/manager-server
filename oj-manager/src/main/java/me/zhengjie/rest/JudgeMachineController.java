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
import me.zhengjie.annotation.rest.AnonymousPostMapping;
import me.zhengjie.domain.JudgeMachine;
import me.zhengjie.service.JudgeMachineService;
import me.zhengjie.service.dto.JudgeMachineDto;
import me.zhengjie.service.dto.JudgeMachineQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @website https://eladmin.vip
* @author nwl
* @date 2023-02-13
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "oj:判题机管理接口管理")
@RequestMapping("/api/judgeMachine")
public class JudgeMachineController {

    private final JudgeMachineService judgeMachineService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('judgeMachine:list')")
    public void exportJudgeMachine(HttpServletResponse response, JudgeMachineQueryCriteria criteria) throws IOException {
        judgeMachineService.download(judgeMachineService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询判题机管理接口")
    @ApiOperation("查询判题机管理接口")
    @PreAuthorize("@el.check('judgeMachine:list')")
    public ResponseEntity<Object> queryJudgeMachine(JudgeMachineQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(judgeMachineService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增判题机管理接口")
    @ApiOperation("新增判题机管理接口")
    @PreAuthorize("@el.check('judgeMachine:add')")
    public ResponseEntity<Object> createJudgeMachine(@Validated @RequestBody JudgeMachine resources){
        return new ResponseEntity<>(judgeMachineService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改判题机管理接口")
    @ApiOperation("修改判题机管理接口")
    @PreAuthorize("@el.check('judgeMachine:edit')")
    public ResponseEntity<Object> updateJudgeMachine(@Validated @RequestBody JudgeMachine resources){
        judgeMachineService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除判题机管理接口")
    @ApiOperation("删除判题机管理接口")
    @PreAuthorize("@el.check('judgeMachine:del')")
    public ResponseEntity<Object> deleteJudgeMachine(@RequestBody Long[] ids) {
        judgeMachineService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("rand")
    @Log("随机返回判题机接口")
    @ApiOperation("随机返回判题机接口")
    @PreAuthorize("@el.check('judgeMachine:list')")
    public ResponseEntity<Object> randGetJudgeMachine(Long languageId) {
        JudgeMachineQueryCriteria criteria = new JudgeMachineQueryCriteria();
        criteria.setEnabled(true);
        List<JudgeMachineDto> judgeMachineDtos = judgeMachineService.queryAll(criteria).stream()
                .filter(judgeMachineDto -> judgeMachineDto.getLanguages().stream()
                        .allMatch(language -> languageId == null || Objects.equals(language.getId(), languageId)))
                .collect(Collectors.toList());
        if (judgeMachineDtos.size() == 0) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(judgeMachineDtos.get((int)Math.floor(Math.random() * judgeMachineDtos.size())),HttpStatus.OK);
    }
    @Log("判题机心跳")
    @ApiOperation("判题机心跳")
    @AnonymousPostMapping("health")
    public ResponseEntity<Object> health(@RequestBody JudgeMachine judgeMachine) {
        JudgeMachineDto dto = null;
        try {
            dto = judgeMachineService.getByUrl(judgeMachine);
        } catch (Exception e) {
            dto = judgeMachineService.save(judgeMachine);
        }
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }
    @Log("获取在线并且启用的判题机列表")
    @ApiOperation("获取在线并且启用的判题机列表")
    @GetMapping("online")
    public ResponseEntity<Object> getOnlineHost(@RequestParam(value = "onlyEnabled",required = false,defaultValue = "false") Boolean isEnabled) {
        return new ResponseEntity<>(judgeMachineService.getHosts(isEnabled),HttpStatus.OK);
    }

}

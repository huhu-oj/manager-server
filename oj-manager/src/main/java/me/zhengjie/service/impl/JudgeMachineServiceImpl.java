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
package me.zhengjie.service.impl;

import cn.hutool.cron.CronUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.JudgeMachine;
import me.zhengjie.domain.Language;
import me.zhengjie.repository.JudgeMachineRepository;
import me.zhengjie.repository.LanguageRepository;
import me.zhengjie.service.JudgeMachineService;
import me.zhengjie.service.dto.JudgeMachineDto;
import me.zhengjie.service.dto.JudgeMachineQueryCriteria;
import me.zhengjie.service.mapstruct.JudgeMachineMapper;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
* @website https://eladmin.vip
* @description 服务实现
* @author nwl
* @date 2023-02-13
**/
@Service
@RequiredArgsConstructor
public class JudgeMachineServiceImpl implements JudgeMachineService {

    private final JudgeMachineRepository judgeMachineRepository;
    private final JudgeMachineMapper judgeMachineMapper;
    private final LanguageRepository languageRepository;
    private final HashMap<Long,JudgeMachineDto> onlineJudgeMachineMap = new HashMap<>();

    @PostConstruct
    private void init() {
        JudgeMachineQueryCriteria criteria = new JudgeMachineQueryCriteria();
        criteria.setEnabled(true);
        List<JudgeMachineDto> judgeMachineDtos = queryAll(criteria);
        judgeMachineDtos.forEach(dto->{
            onlineJudgeMachineMap.put(dto.getId(),dto);
        });
        CronUtil.schedule("*/12 * * * * *", (Runnable) this::checkOnlineHost);

        // 支持秒级别定时任务
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }
    @Transactional
    void checkOnlineHost() {
        //遍历map
        onlineJudgeMachineMap.forEach((id,host)->{

            if (host == null) {
                return;
            }
            if (!host.getEnabled()) {
                //更新数据库
                JudgeMachine judgeMachine = judgeMachineMapper.toEntity(host);
                judgeMachine.setEnabled(false);
                update(judgeMachine);
                onlineJudgeMachineMap.put(id,null);
                return;
            }
            host.setEnabled(false);
        });
    }
    @Override
    public Map<String,Object> queryAll(JudgeMachineQueryCriteria criteria, Pageable pageable){
        Page<JudgeMachine> page = judgeMachineRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(judgeMachineMapper::toDto));
    }

    @Override
    public List<JudgeMachineDto> queryAll(JudgeMachineQueryCriteria criteria){
        return judgeMachineMapper.toDto(judgeMachineRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public JudgeMachineDto findById(Long id) {
        JudgeMachine judgeMachine = judgeMachineRepository.findById(id).orElseGet(JudgeMachine::new);
        ValidationUtil.isNull(judgeMachine.getId(),"JudgeMachine","id",id);
        return judgeMachineMapper.toDto(judgeMachine);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JudgeMachineDto create(JudgeMachine resources) {
        return judgeMachineMapper.toDto(judgeMachineRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JudgeMachineDto save(JudgeMachine resources) {
        if (resources.getId() != null && resources.getId() != 0) {
            JudgeMachine judgeMachine = judgeMachineRepository.findById(resources.getId()).orElseGet(JudgeMachine::new);
            judgeMachine.copy(resources);
            JudgeMachineDto judgeMachineDto = judgeMachineMapper.toDto(judgeMachineRepository.save(judgeMachine));

            onlineJudgeMachineMap.put(judgeMachineDto.getId(),judgeMachineDto);
            if (!HttpRequest.post(judgeMachineDto.getUrl()+"/api/v1/config")
                    .body(JSONUtil.toJsonStr(judgeMachineDto))
                    .execute().isOk()) {
                throw new RuntimeException("修改判题机配置失败");
            }
            return judgeMachineDto;
        } else {
            JudgeMachineDto judgeMachineDto = create(resources);
            onlineJudgeMachineMap.put(judgeMachineDto.getId(),judgeMachineDto);
            if (!HttpRequest.post(judgeMachineDto.getUrl()+"/api/v1/config")
                    .body(JSONUtil.toJsonStr(judgeMachineDto))
                    .execute().isOk()) {
                throw new RuntimeException("修改判题机配置失败");
            }
            return judgeMachineDto;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(JudgeMachine resources) {
        JudgeMachine judgeMachine = judgeMachineRepository.findById(resources.getId()).orElseGet(JudgeMachine::new);
        ValidationUtil.isNull( judgeMachine.getId(),"JudgeMachine","id",resources.getId());
        judgeMachine.copy(resources);
        judgeMachineRepository.save(judgeMachine);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            judgeMachineRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<JudgeMachineDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (JudgeMachineDto judgeMachine : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", judgeMachine.getName());
            map.put("主机账号", judgeMachine.getUsername());
            map.put("密码", judgeMachine.getPassword());
            map.put("主机地址", judgeMachine.getUrl());
            map.put("是否启动", judgeMachine.getEnabled());
            map.put(" createTime",  judgeMachine.getCreateTime());
            map.put(" updateTime",  judgeMachine.getUpdateTime());
            map.put("支持的语言", judgeMachine.getSupportLanguage());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public JudgeMachineDto getByUrl(JudgeMachine request) {
//        System.out.println(onlineJudgeMachineMap);
        JudgeMachineQueryCriteria criteria = new JudgeMachineQueryCriteria();
        criteria.setUrl(request.getUrl());
        JudgeMachine judgeMachine = judgeMachineRepository.findOne((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)).orElseThrow(RuntimeException::new);
        JudgeMachineDto judgeMachineDto = judgeMachineMapper.toDto(judgeMachine);
        //检查更新
        if (!judgeMachine.getSupportLanguage().equals(request.getSupportLanguage())) {
            //获取需要更新的语言
            List<String> languageNames = Arrays.asList(request.getSupportLanguage().split(","));
            List<Language> languages = languageRepository.findByNameIn(languageNames);
            //更新语言
            judgeMachine.setLanguages(languages);
            judgeMachine.setSupportLanguage(request.getSupportLanguage());
            update(judgeMachine);
            judgeMachineDto = judgeMachineMapper.toDto(judgeMachine);

        }
        //维护在线列表
        judgeMachineDto.setEnabled(request.getEnabled());
        onlineJudgeMachineMap.put(judgeMachineDto.getId(),judgeMachineDto);
        return judgeMachineDto;
    }

    @Override
    public List<JudgeMachineDto> getHosts(Boolean onlyEnabled) {
        if (!onlyEnabled) {
            return onlineJudgeMachineMap.values().stream()
                    .filter(Objects::nonNull).collect(Collectors.toList());
        }

       return onlineJudgeMachineMap.values().stream()
               .filter(judgeMachineDto -> judgeMachineDto != null && judgeMachineDto.getEnabled())
               .collect(Collectors.toList());
    }
}

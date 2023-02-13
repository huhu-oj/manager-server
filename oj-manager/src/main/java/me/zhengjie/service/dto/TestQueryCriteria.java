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
package me.zhengjie.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.util.List;
import me.zhengjie.annotation.Query;

/**
* @website https://eladmin.vip
* @author nwl
* @date 2023-02-13
**/
@Data
public class TestQueryCriteria{

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String title;

    /** 大于等于 */
    @Query(type = Query.Type.GREATER_THAN)
    private Timestamp startTime;

    /** 小于等于 */
    @Query(type = Query.Type.LESS_THAN)
    private Timestamp endTime;

    /** 大于等于 */
    @Query(type = Query.Type.GREATER_THAN)
    private Timestamp createTime;

    /** 小于等于 */
    @Query(type = Query.Type.LESS_THAN)
    private Timestamp updateTime;
}
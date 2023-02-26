package me.zhengjie.service.dto;

import lombok.Data;

/**
 * @Description
 * @Author nwl
 * @Create 2023-02-26 下午4:26
 */
@Data
public class ExecuteResultSmallDto {

    private Integer id;

    /** 名称 */
    private String name;

    /** 颜色 */
    private String color;
}

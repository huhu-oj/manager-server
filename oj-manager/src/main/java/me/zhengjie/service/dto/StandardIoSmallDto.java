package me.zhengjie.service.dto;

import lombok.Data;

/**
 * @Description
 * @Author nwl
 * @Create 2023-02-26 下午4:21
 */
@Data
public class StandardIoSmallDto {

    private Long id;

    /** 输入 */
    private String input;

    /** 输出 */
    private String output;

}

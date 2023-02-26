package me.zhengjie.service.dto;

import lombok.Data;

/**
 * @Description
 * @Author nwl
 * @Create 2023-02-26 下午4:21
 */
@Data
public class LabelSmallDto {

    private Long id;

    /** 标签名 */
    private String name;

    /** 链接 */
    private String url;
}

package me.zhengjie.service.dto;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @Description
 * @Author nwl
 * @Create 2023-02-26 下午4:24
 */
@Data
public class AnswerRecordSmallDto {

    private Long id;

    /** 所属用户 */
    private Long userId;

    /** 代码 */
    private String code;

    /** 执行时间 */
    private Long executeTime;

    /** 所属语言 */
    private LanguageSmallDto language;

    /** 日志 */
    private String log;

    /** 错误日志 */
    private String error;

    /** 通过数 */
    private Integer passNum;

    /** 未通过数 */
    private Integer notPassNum;

    /** 执行结果 */
    private ExecuteResultSmallDto executeResult;

    /** 备注 */
    private String note;

    /** 创建时间 */
    private Timestamp createTime;
}

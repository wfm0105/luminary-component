package com.luminary.component.logger.model;

import lombok.Data;

/**
 *
 * 日志对象
 *
 * @author wulinfeng
 * @date 2018/7/17 19:43
 */
@Data
public class EsLogVO {

    private String host; // hostName
    private String ip; // ip
    private String profile; // 环境
    private String message; // 日志信息
    private String dateTime; // 日期
    private String logger; // logback appender
    private String level; // 级别
    private String thread; // 线程
    private String throwable; // 错误信息
    private Location location; 
    private String traceId; // 链路跟踪id
    private String rpcId; // 链路中每步调用id

    @Data
    public static class Location {
        private String className; // 类名
        private String method; // 方法名
        private String file; // 文件
        private String line; // 行号
    }

}

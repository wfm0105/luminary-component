package com.luminroy.component.logger.model;

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

    private String host;
    private String ip;
    private String profile;
    private String message;
    private long dateTime;
    private String logger;
    private String level;
    private String thread;
    private String throwable;
    private Location location;
    private String traceId;
    private String rpcId;

    @Data
    public static class Location {
        private String className;
        private String method;
        private String file;
        private String line;
    }

}

package com.luminary.component.util.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class HostUtil {

    public static String getHostName() {
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            log.error("未知的主机！", e);
            e.printStackTrace();
        }
        return addr.getHostName();
    }

    public static String getIP() {
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            log.error("未知的主机！", e);
            e.printStackTrace();
        }
        return addr.getHostAddress();
    }

    public static String getIP(HttpServletRequest request) {
        String clientIP = "";
        String clientIPList = request.getHeader("X-Forwarded-For");
        // 根据 RFC 7239标准，如果存在代理，X-Forwarded-for头不为空，为client，proxy1，proxy2，注意，可以被伪造
        // 不包含最后一个代理ip，最后一个代理ip需要通过remoteAddr获得。remoteAddr无法伪造
        if(StringUtils.isEmpty(clientIPList)) {
            log.info("X-Forwarded-For为空！");
            // X-Real-IP可能是代理ip，也可能是真实设备ip，可以被伪造
            clientIPList = request.getHeader("X-Real-IP");
        }

        if(!StringUtils.isEmpty(clientIPList)) {
            log.info("X-Real-IP不为空！");
            clientIP = getIPfromXREALIP(clientIPList);
        }

        if(StringUtils.isEmpty(clientIP)) {
            log.info("从PROXY_FORWARDED_FOR取值！");
            clientIPList = request.getHeader("PROXY_FORWARDED_FOR");
        }

        if(StringUtils.isEmpty(clientIPList)) {
            log.info("从remoteAddr取值！");
            clientIP = request.getRemoteAddr();
        } else {
            log.info("从PROXY_FORWARDED_FOR取值！");
            clientIP = getIPfromXREALIP(clientIPList);
        }

        if(!isIP(clientIP)) {
            clientIP = request.getRemoteAddr();
        }

        log.info("clientIP:"+clientIP);
        return clientIP;
    }

    private static String getIPfromXREALIP(String clientIPList) {
        String clientIP = "";
        if(StringUtils.contains(clientIPList, ",")) {
            String[] clientIPArray = StringUtils.split(clientIPList, ",");
            // 返回第一个地址
            clientIP = clientIPArray[0];
        } else {
            clientIP = clientIPList;
        }
        return clientIP;
    }

    public static boolean isIP(String ip) {
        if(ip.length() < 7 || ip.length() > 15 || "".equals(ip))
        {
            return false;
        }
        /**
         * 判断IP格式和范围
         */
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(ip);

        return mat.find();
    }

}

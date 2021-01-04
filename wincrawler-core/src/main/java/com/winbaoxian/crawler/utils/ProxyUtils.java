package com.winbaoxian.crawler.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ProxyUtils {

    INSTANCE;

    private static final Logger log = LoggerFactory.getLogger(ProxyUtils.class);

    private final static char CHAR_COLON = ':';
    private final static String FETCH_IP_URL_TEMPLATE = "http://ip.ipjldl.com/index.php/api/entry?method=proxyServer.generate_api_url&packid=0&fa=0&fetch_key=&groupid=0&qty=1&time=%s&pro=&city=&port=1&format=txt&ss=1&css=&dt=1&specialTxt=3&specialJson=&usertype=2";
    public final static int TIME_VALUE_SHORT = 100;
    public final static int TIME_VALUE_MEDIUM = 101;
    public final static int TIME_VALUE_LONG = 102;
    private static final String FEE_URL = "http://ip.ipjldl.com/index.php/api/entry?method=proxyServer.generate_api_url&packid=0&fa=0&fetch_key=&qty=1&time=1&pro=&city=&port=1&format=txt&ss=1&css=&dt=1&specialTxt=3&specialJson=";

    private String[] ajaxProxy = null;

    public String getSeleniumProxy(int useProxyMethod) {
        int timeValue = useProxyMethod == 1 ? TIME_VALUE_SHORT : useProxyMethod == 2 ? TIME_VALUE_MEDIUM : useProxyMethod == 3 ? TIME_VALUE_LONG : TIME_VALUE_SHORT;
        return getSeleniumProxyString(timeValue);
    }

    public String getSeleniumProxyString(int timeValue) {
        String ipPortStr = getProxyIpPortString(timeValue);
        if (StringUtils.isBlank(ipPortStr)) {
            throw new RuntimeException("获取代理地址出错");
        }
        log.info("获取到Selenium代理地址：{}", ipPortStr);
        return ipPortStr;
    }

    public String[] getAjaxProxyIpPort() {
        if (ArrayUtils.isEmpty(ajaxProxy)) {
            changeAjaxProxy();
        }
        return ajaxProxy;
    }

    public void changeAjaxProxy() {
        String ipPortStr = getProxyIpPortString(TIME_VALUE_MEDIUM);
        if (StringUtils.isBlank(ipPortStr)) {
            throw new RuntimeException("获取代理地址出错");
        }
        log.info("获取到Ajax代理地址：{}", ipPortStr);
        ajaxProxy = StringUtils.split(ipPortStr, CHAR_COLON);
    }

    //需要时实现该方法，使用代理精灵获取代理地址
    private String getProxyIpPortString(int time) {
        int tryCount = 3;
        while (tryCount-- > 0) {
            try {
                String fetchIpUrl = String.format(FETCH_IP_URL_TEMPLATE, time);
                String res = HttpUtils.INSTANCE.doGet(fetchIpUrl);
                if (StringUtils.isBlank(res)) {
                    continue;
                }
                if (res.contains("已用完") || res.contains("有效IP数量不够")) {
                    res = HttpUtils.INSTANCE.doGet(FEE_URL);
                }
                if (isIpStr(res)) {
                    return res;
                } else if (res.contains("不是白名单IP")) {
                    throw new Exception("不是白名单IP");
                } else if (res.contains("当前用户不允许获取")) {
                    throw new Exception("当前用户不允许获取");
                }
                Thread.sleep(2000L);
            } catch (Exception e) {
                log.error("getProxyIpPortString error", e);
            }
        }
        return null;
    }

    private boolean isIpStr(String ipStr) {
        if (StringUtils.isBlank(ipStr)) {
            return false;
        }
        String regex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}:\\d+";
        Pattern pattern = Pattern.compile(regex);    // 编译正则表达式
        Matcher matcher = pattern.matcher(ipStr);
        return matcher.matches();
    }

    public boolean isInvalidProxy(String bodyStr) {
        if (StringUtils.isBlank(bodyStr)) {
            return false;
        }
        if (bodyStr.contains("407 Proxy Authentication Required") || bodyStr.contains("502 Bad Gateway")) {
            return true;
        }
        return false;
    }

}

package com.winbaoxian.crawler.utils;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public enum Base64Utils {

    INSTANCE;

    public byte[] toBytes(String base64Str) {
        if (StringUtils.isBlank(base64Str)) {
            return null;
        }
        String[] parts = StringUtils.splitByWholeSeparator(base64Str, "base64,");
        if (ArrayUtils.isEmpty(parts) || parts.length < 2) {
            return null;
        }
        return org.springframework.util.Base64Utils.decodeFromString(parts[1]);
    }

}

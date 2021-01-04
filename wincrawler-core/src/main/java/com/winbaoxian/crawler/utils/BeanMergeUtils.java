package com.winbaoxian.crawler.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

public enum BeanMergeUtils {

    INSTANCE;
    private Logger log = LoggerFactory.getLogger(getClass());

    public void copyProperties(Object orig, Object dest) {
        try {
            this.copyPropertiesWithThrowE(orig, dest);
        } catch (Exception e) {
            log.error("BeanMergeUtils.copyProperties failed,orig:{}, dest:{}", JSON.toJSONString(orig), JSON.toJSONString(dest));
        }
    }

    private void copyPropertiesWithThrowE(Object orig, Object dest) throws IllegalAccessException, InvocationTargetException {
        if (dest == null) {
            throw new IllegalArgumentException("No destination bean specified");
        } else if (orig == null) {
            throw new IllegalArgumentException("No origin bean specified");
        } else {
            if (this.log.isDebugEnabled()) {
                this.log.debug("BeanUtils.copyProperties(" + dest + ", " + orig + ")");
            }

            int var5;
            int var6;
            String name;
            Object value;

            PropertyDescriptor[] origDescriptors = this.getPropertyUtils().getPropertyDescriptors(orig);
            PropertyDescriptor[] var14 = origDescriptors;
            var5 = origDescriptors.length;

            for (var6 = 0; var6 < var5; ++var6) {
                PropertyDescriptor origDescriptor = var14[var6];
                name = origDescriptor.getName();
                if (!"class".equals(name) && this.getPropertyUtils().isReadable(orig, name) && this.getPropertyUtils().isWriteable(dest, name)) {
                    try {
                        value = this.getPropertyUtils().getSimpleProperty(orig, name);
                        if (value != null) {
                            BeanUtils.copyProperty(dest, name, value);
                        }
                    } catch (NoSuchMethodException var10) {
                        ;
                    }
                }
            }

        }
    }

    private PropertyUtilsBean getPropertyUtils() {
        return BeanUtilsBean.getInstance().getPropertyUtils();
    }


}

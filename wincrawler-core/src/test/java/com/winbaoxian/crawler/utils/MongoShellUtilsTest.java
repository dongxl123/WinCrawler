package com.winbaoxian.crawler.utils;

import com.winbaoxian.common.freemarker.utils.JsonUtils;
import com.winbaoxian.crawler.BaseTest;
import com.winbaoxian.crawler.model.core.MongoCommandDTO;
import org.testng.annotations.Test;

/**
 * @author dongxuanliang252
 * @date 2019-04-04 9:51
 */
public class MongoShellUtilsTest extends BaseTest {

    @Test
    public void testParseShellCommand() {
        MongoCommandDTO commandDTO = MongoShellUtils.INSTANCE.parseShellCommand("db.test.findOne({id:111})");
        System.out.println(JsonUtils.INSTANCE.toJSONString(commandDTO));
    }

}

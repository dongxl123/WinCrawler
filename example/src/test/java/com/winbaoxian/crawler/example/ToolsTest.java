package com.winbaoxian.crawler.example;

import com.winbaoxian.common.freemarker.functions.*;
import com.winbaoxian.common.freemarker.utils.JsonUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
public class ToolsTest {

    @Test
    public void ftl2String() {
        String content = readFile("src/test/java/Test.ftl");
        System.out.println(StringEscapeUtils.escapeJava(content));
    }

    @Test
    public void string2Ftl() {
        String content = readFile("src/test/java/Test.txt");
        System.out.println(StringEscapeUtils.unescapeJava(content));
    }

    @Test
    public void testRender() throws IOException, TemplateException {
        Configuration configuration = Configuration.getDefaultConfiguration();
        initFreeMarker(configuration);
        String content = readFile("src/test/java/Test.ftl");
        String model = readFile("src/test/java/Model.json");
        Template template = new Template(null, content, configuration);
        String ret = FreeMarkerTemplateUtils.processTemplateIntoString(template, JsonUtils.INSTANCE.parseObject(model));
        System.out.println(ret);
    }

    private void initFreeMarker(Configuration configuration) throws TemplateException {
        configuration.setSetting(Configuration.NUMBER_FORMAT_KEY, "0.######");
        configuration.setSharedVariable("random", new RandomFunction());
        configuration.setSharedVariable("range", new RangeFunction());
        configuration.setSharedVariable("toJSONString", new ToJSONStringFunction());
        configuration.setSharedVariable("toJSON", new ToJSONFunction());
        configuration.setSharedVariable("toCookieString", new ToCookieStringFunction());
        configuration.setSharedVariable("descartes", new DescartesFunction());
        configuration.setSharedVariable("repeat", new RepeatFunction());
        configuration.setSharedVariable("matchNumber", new MatchNumberFunction());
        configuration.setSharedVariable("extractTagValues", new ExtractTagValuesFunction());
        configuration.setSharedVariable("getBirthday", new GetBirthdayFunction());
        configuration.setSharedVariable("escapeString", new EscapeStringFunction());
        configuration.setSharedVariable("jsoup", new JsoupFunction());
        configuration.setSharedVariable("jsoupXpath", new JsoupXpathFunction());
        configuration.setSharedVariable("aes", new AESFunction());
        configuration.setSharedVariable("base64", new Base64Function());
        configuration.setSharedVariable("md5", new Md5Function());
        configuration.setSharedVariable("hmac", new HmacFunction());
        configuration.setSharedVariable("tripleDES", new TripleDESFunction());
        configuration.setSharedVariable("jsonPathExtract", new JsonPathExtractFunction());
    }


    private String readFile(String fileName) {
        BufferedReader sbr = null;
        try {
            sbr = new BufferedReader(new FileReader(fileName));
            Stream<String> lineStreams = sbr.lines();
            List<String> lines = lineStreams.collect(Collectors.toList());
            return StringUtils.join(lines, "\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sbr != null) {
                try {
                    sbr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    @Test
    public void ss() throws TemplateException, IOException {
        String body = "http://bxsadmin.wyins.net.cn/auth?callback=http://preservation-group-admin.wyins.net.cn/&ticket=ST-7907-HNCzBVSiKdcydP9N6Lh0-test2";
        Configuration configuration = Configuration.getDefaultConfiguration();
        initFreeMarker(configuration);
        String content = "${.now}";
        Template template = new Template(null, content, configuration);
        Map<String, Object> model = new HashMap<>(  );
        model.put("body", body);
        String ret = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        System.out.printf("111");
    }



    @Test
    public void test() {
        String content = readFile("src/test/java/Model.json");
        System.out.println(StringEscapeUtils.escapeJava(content));
    }
}

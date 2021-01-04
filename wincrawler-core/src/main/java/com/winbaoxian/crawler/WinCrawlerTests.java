package com.winbaoxian.crawler;

import com.winbaoxian.crawler.core.WinCrawlerExecutor;
import com.winbaoxian.crawler.model.dto.CrawlerTaskDTO;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.xml.XmlTest;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * @author dongxuanliang252
 * @date 2019-6-27 17:58:45
 */
@SpringBootTest
public class WinCrawlerTests extends AbstractTestNGSpringContextTests {

    @Resource
    private WinCrawlerExecutor winCrawlerExecutor;
    private String sql;

    @Parameters({"sql"})
    @BeforeClass
    public void beforeClass(String sql) {
        System.setProperty("webdriver.chrome.driver", "../chromedriver.exe");
        this.sql = sql;
    }

    /**
     * 查询测试用例
     * parallel = true
     *
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @DataProvider(name = "taskList")
    public Object[] getData() throws SQLException, ClassNotFoundException {
        CrawlerTaskDTO[] dataArray = winCrawlerExecutor.getTaskList(sql);
        return dataArray;
    }

    @BeforeMethod
    public void beforeMethod() {
    }


    @Test(dataProvider = "taskList")
    public void crawl(CrawlerTaskDTO taskDTO) throws Throwable {
        winCrawlerExecutor.executeCrawlTask(taskDTO);
    }

    @Test(dataProvider = "taskList")
    public void export(CrawlerTaskDTO taskDTO) throws Throwable {
        winCrawlerExecutor.executeExportTask(taskDTO);
    }

    @AfterMethod
    public void afterMethod(ITestContext a, XmlTest b, Method c, Object[] d, ITestResult e) {
    }

    @AfterClass
    public void afterClass() {

    }


}

package xiuery.xselenium.runner;

import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xiuery.xselenium.common.WebDriver;
import xiuery.xselenium.config.JEConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RunnerJavaExample implements RunnerBase {

    @Autowired
    private JEConfig JEConfig;

    @Autowired
    private WebDriver webDriver;

    public void execute() {
        try {
            webDriver.openBrowser(JEConfig.getBrowser());
            webDriver.maxWindow();
            webDriver.get(JEConfig.getUrl());

            searchClassName();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            webDriver.quit();
        }
    }

    private void searchClassName() throws Exception {
        List<Map<String, String>> result = new ArrayList<>();

        List<WebElement> elements = webDriver.getElements("xpath=>//*[@id='api-list']/li");

        for (WebElement element: elements) {
            Map<String, String> className = new HashMap<>();

            String name = webDriver.getText(element, "xpath=>./div[1]/a", true);
            String desc = webDriver.getText(element, "xpath=>./div[2]", true);

            className.put("name", name);
            className.put("desc", desc);

            // 新开页面获取code example
            // webDriver.openWindowByJS(webDriver.getAttribute(element, "xpath=>./div[1]/a", "href").trim());
            // webDriver.switchWindowByTitle(name, 5);
            //
            // List<Map<String, String>> codeExample = searchCodeExample();

            result.add(className);

            System.out.println(className);

            break;
        }
    }

    private List<Map<String, String>> searchCodeExample() throws Exception {
        List<Map<String, String>> result = new ArrayList<>();

        List<WebElement> elements = webDriver.getElements("class=>examplebox");

        for (WebElement element: elements) {
            Map<String, String> codeExample = new HashMap<>();

            String xpath_project = "xpath=>./div[@class='exampleboxheader']/table/tbody/tr/td[1]/span[1]";
            String xpath_file = "xpath=>./div[@class='exampleboxheader']/table/tbody/tr/td[1]/span[2]";
            String xpath_code = "xpath=>./div[@class='exampleboxbody']/pre";
            String xpath_source_code = "./div[@class='exampleboxheader']/table/tbody/tr/td[1]/a";
            codeExample.put("project", webDriver.getText(element, xpath_project, true));
            codeExample.put("file", webDriver.getText(element, xpath_file, true));
            codeExample.put("code", webDriver.getText(element, xpath_code, true));

            // 新打开source code窗口下载源码
            webDriver.openWindowByJS(webDriver.getAttribute(element, xpath_source_code, "href", true));
            codeExample.put("path", dlSourceCode());

            System.out.println(codeExample);

            result.add(codeExample);
        }

        // 关闭当前窗口并返回上一个窗口
        webDriver.close();

        return result;
    }

    /**
     * 下载完成返回一个源码路径
     */
    private String dlSourceCode() {
        String path = "";

        // 保存一下title
        String title = webDriver.getTitle();

        webDriver.switchWindowByTitle("Java Source Code");
        // 关闭当前窗口并返回上一个页面
        webDriver.close();
        webDriver.switchWindowByTitle(title, 3);

        return path;
    }
}

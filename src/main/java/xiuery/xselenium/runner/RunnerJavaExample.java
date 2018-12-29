package xiuery.xselenium.runner;

import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xiuery.xselenium.common.WebDriver;
import xiuery.xselenium.config.JEConfig;
import xiuery.xselenium.entity.JavaApi;
import xiuery.xselenium.service.JavaApiService;

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

    @Autowired
    private JavaApiService javaApiService;

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
            String method = webDriver.getText(element, "xpath=>./div[2]/span", true);
            String description = webDriver.getText(element, "xpath=>./div[2]", true);

            className.put("name", name);
            className.put("description", description);

            // 获取code example
            String newUrl = webDriver.getAttribute(element, "xpath=>./div[1]/a", "href", true);
            List<Map<String, String>> codeExample = searchCodeExample(newUrl, name);

            result.add(className);

            System.out.println(className);
            System.out.println(codeExample);

            // 保存至库
            for (Map<String, String> ce: codeExample) {
                JavaApi javaApi = new JavaApi();
                javaApi.setName(name);
                javaApi.setMethod(Integer.parseInt(method));
                javaApi.setDescription(description);
                javaApi.setExampleTitle(ce.get("example_title"));
                javaApi.setProject(ce.get("project"));
                javaApi.setFile(ce.get("file"));
                javaApi.setCode(ce.get("code"));
                javaApi.setPath(ce.get("path"));

                javaApiService.save(javaApi);
            }
        }
    }

    private List<Map<String, String>> searchCodeExample(String url, String newTitle) throws Exception {
        // 保存当前窗体title，打开新窗口，处理完成再返回本窗口
        String title = webDriver.getTitle();
        webDriver.openWindowByJS(url);
        webDriver.switchWindowByTitle(newTitle);

        List<Map<String, String>> result = new ArrayList<>();

        List<WebElement> elements = webDriver.getElements("class=>examplebox");

        for (int i= 0; i < elements.size(); i++) {
            WebElement element = elements.get(i);

            Map<String, String> codeExample = new HashMap<>();
            codeExample.put("exampleTitle", "Example " + String.valueOf(i));

            String xpath_project = "xpath=>./div[@class='exampleboxheader']/table/tbody/tr/td[1]/span[1]";
            String xpath_file = "xpath=>./div[@class='exampleboxheader']/table/tbody/tr/td[1]/span[2]";
            String xpath_code = "xpath=>./div[@class='exampleboxbody']/pre";
            String xpath_source_code = "xpath=>./div[@class='exampleboxheader']/table/tbody/tr/td[1]/a";
            codeExample.put("project", webDriver.getText(element, xpath_project, true));
            codeExample.put("file", webDriver.getText(element, xpath_file, true));
            codeExample.put("code", webDriver.getText(element, xpath_code, true));

            // 新打开source code窗口下载源码
            String newUrl = webDriver.getAttribute(element, xpath_source_code, "href", true);
            codeExample.put("path", dlSourceCode(newUrl));

            result.add(codeExample);
        }

        // 关闭当前窗口并返回上一个窗口
        webDriver.close();
        webDriver.switchWindowByTitle(title);

        return result;
    }

    /**
     * 下载完成返回一个源码路径
     */
    private String dlSourceCode(String url) {
        // 保存当前窗体title，打开新窗口，处理完成再返回本窗口
        String title = webDriver.getTitle();
        webDriver.openWindowByJS(url);
        webDriver.switchWindowByTitle("Java Source Code");

        String path = "";

        // 关闭当前窗口并返回上一个页面
        webDriver.close();
        webDriver.switchWindowByTitle(title, 3);

        return path;
    }
}

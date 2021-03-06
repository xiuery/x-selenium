package xiuery.xselenium.runner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xiuery.xselenium.common.FileResource;
import xiuery.xselenium.common.WebDriver;
import xiuery.xselenium.config.JEConfig;
import xiuery.xselenium.entity.JavaApi;
import xiuery.xselenium.entity.ProjectCode;
import xiuery.xselenium.service.JavaApiService;
import xiuery.xselenium.service.ProjectCodeService;

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

    @Autowired
    private ProjectCodeService projectCodeService;

    @Autowired
    private FileResource fileResource;

    private String lang = "JAVA";

    private static final Logger logger = LoggerFactory.getLogger(RunnerJavaExample.class);

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

            logger.info("current class name: {}", name);

            // 获取code example
            String newUrl = webDriver.getAttribute(element, "xpath=>./div[1]/a", "href", true);
            List<Map<String, String>> codeExample = searchCodeExample(newUrl, name);

            result.add(className);

            // 保存至库
            for (Map<String, String> ce: codeExample) {
                JavaApi javaApi = new JavaApi();
                javaApi.setName(name);
                javaApi.setMethod(Integer.parseInt(method));
                javaApi.setDescription(description);
                javaApi.setExampleTitle(ce.get("exampleTitle"));
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

            String project = webDriver.getText(element, xpath_project, true);
            codeExample.put("project", project);
            codeExample.put("file", webDriver.getText(element, xpath_file, true));
            codeExample.put("code", webDriver.getText(element, xpath_code, true));

            logger.info("\texample title: {}", codeExample.get("exampleTitle"));

            // 是否已经下载过了
            QueryWrapper<ProjectCode> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", project).ne("path", "");
            ProjectCode projectCode = projectCodeService.getOne(queryWrapper);
            if (projectCode == null) {
                // 新打开source code窗口下载源码
                String newUrl = webDriver.getAttribute(element, xpath_source_code, "href", true);

                String codePath = dlSourceCode(newUrl);
                codeExample.put("path", codePath);

                // 下载完成,保存
                ProjectCode pc = new ProjectCode();
                pc.setName(project);
                pc.setLang(lang);
                pc.setDescription("");
                pc.setPath(codePath);
                projectCodeService.save(pc);
            }else {
                codeExample.put("path", projectCode.getPath());
            }

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
        // 下载源码生成文件
        try {
            String project = webDriver.getText("xpath=>//*[@id=\"sidebar\"]/div[1]/span", true);

            logger.info("\t\tproject: {}", project);

            // 展开所有文件目录
            expandAll();

            // 根路径
            WebElement rootElement = webDriver.getElement("xpath=>//*[@id=\"treeview\"]/ul/li");
            String rootPath = webDriver.getText(rootElement, "xpath=>./a", true);
            path = fileResource.getCodePath() + "/" + lang + "/" + rootPath;

            // 子路径文件下载
            createSourceCode(webDriver.getElements(rootElement, "xpath=>./ul/li"), lang + "/" + rootPath);
        } catch (Exception e) {
            logger.error("\t\tproject download fail: {}", e.getMessage());
        }

        // 关闭当前窗口并返回上一个页面
        webDriver.close();
        webDriver.switchWindowByTitle(title, 3);

        return path;
    }

    /**
     * 递归生成源码目录文件
     */
    private void createSourceCode(List<WebElement> elements, String path) {
        for (WebElement element: elements) {
            // 判断是目录还是文件
            try {
                if (isFolder(element)) {
                    // 展开目录
                    // expandFolder(element);
                    String newPath = path + "/" + webDriver.getText(element, "xpath=>./a", true);
                    createSourceCode(webDriver.getElements(element, "xpath=>./ul/li"), newPath);
                } else {
                    //
                    String filename = webDriver.getText(element, "xpath=>./a/span", true);
                    String code = webDriver.getText("id=>codeinner", true);
                    fileResource.fileWrite(path, filename, code);

                    logger.info("\t\t\tfile: {}", path + "/" + filename);
                }
            } catch (Exception e) {
                logger.error("\t\t\tsource code download fail: {}", e.getMessage());
            }

            webDriver.sleep(100);
        }
    }

    private boolean isFolder(WebElement element) {
        return webDriver.getAttribute(element, "data-jstree") == null;
    }

    /**
     * 逐个展开会重新加载元素导致失败，一次性全部展开节点
     */
    private void expandAll() {
        webDriver.executeJS("while(true) {" +
                "\tvar nodes = document.getElementsByClassName(\"jstree-closed\");" +
                "\tif (nodes.length > 0) {" +
                "\t\t$.jstree.reference(document.getElementsByClassName(\"jstree-closed\")).open_node(document.getElementsByClassName(\"jstree-closed\"),false,false);\n" +
                "\t} else {" +
                "\t\tbreak;\t" +
                "\t}" +
                "}"
        );
    }
}

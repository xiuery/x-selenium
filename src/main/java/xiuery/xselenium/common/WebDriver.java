package xiuery.xselenium.common;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class WebDriver {

    private String useBrowser;
    private Select select = null;
    private Alert alert = null;
    private long timeOutSeconds = 30;
    private RemoteWebDriver driver;

    public void openBrowser(String browser) throws Exception {

        String lowerBrowser = browser.toLowerCase();

        switch (lowerBrowser) {
            case "ff":
            case "firefox":
                useBrowser = "ff";

                System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
                driver = new FirefoxDriver();
                break;
            case "ff_headless":
            case "firefox_headless":
                useBrowser = "ff";

                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setHeadless(true);

                System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
                driver = new FirefoxDriver(firefoxOptions);
                break;
            case "chrome":
                useBrowser = "chrome";

                System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
                driver = new ChromeDriver();
                break;
            case "chrome_headless":
                useBrowser = "chrome";

                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setHeadless(true);

                System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
                driver = new ChromeDriver(chromeOptions);
                break;
            default:
                throw new Exception("Support browser: ff, firefox, ff_headless, firefox_headless, chrome, chrome_headless");
        }
    }

    public void get(String url) {
        driver.get(url);
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageSource() {
        return driver.getPageSource();
    }

    public void maxWindow() {
        driver.manage().window().maximize();
    }

    public void setWindowSize(int width, int height) {
        driver.manage().window().setSize(new Dimension(width, height));
    }

    public void back() {
        driver.navigate().back();
    }

    public void forward() {
        // 下一个页面，如果已经到最后，什么也不做
        driver.navigate().forward();
    }

    public void refresh() {
        driver.navigate().refresh();
    }

    public void toUrl(String url) {
        driver.navigate().to(url);
    }

    public void toUrlByJS(String url) {
        executeJS(String.format("window.location.href=\"%s\";", url));
    }

    public void executeJS(String js) {
        ((JavascriptExecutor) driver).executeScript(js);
    }

    public void click(String css) throws Exception {
        getElement(css).click();
    }

    public void click(WebElement element) {
        element.click();
    }

    public void click(WebElement element, String css) throws Exception {
        getElement(element, css).click();
    }

    public void submit(String css) throws Exception {
        getElement(css).submit();
    }

    public void submit(WebElement element, String css) throws Exception {
        getElement(element, css).submit();
    }

    public String getText(String css) throws Exception {
        return getElement(css).getText();
    }

    public String getText(String css, boolean trim) throws Exception {
        String text = getText(css);

        if (trim) {
            return text.trim();
        }

        return text;
    }

    public String getText(WebElement element, String css) throws Exception {
        return getElement(element, css).getText();
    }

    public String getText(WebElement element, String css, boolean trim) throws Exception {
        String text = getText(element, css);

        if (trim) {
            return text.trim();
        }

        return text;
    }

    /**
     * 根据locator获取元素属性
     */
    public String getAttribute(String css, String attribute) throws Exception {
        return getElement(css).getAttribute(attribute);
    }

    public String getAttribute(String css, String attribute, boolean trim) throws Exception {
        String att = getAttribute(css, attribute);

        if (trim) {
            return att.trim();
        }

        return att;
    }

    /**
     * 根据元素对象获取属性
     */
    public String getAttribute(WebElement element, String attribute) {
        return element.getAttribute(attribute);
    }

    public String getAttribute(WebElement element, String attribute, boolean trim) {
        String att = element.getAttribute(attribute);

        if (trim) {
            return att.trim();
        }

        return att;
    }

    /**
     * 获取子类元素属性
     */
    public String getAttribute(WebElement element, String css, String attribute) throws Exception {
        return getElement(element, css).getAttribute(attribute);
    }

    public String getAttribute(WebElement element, String css, String attribute, boolean trim) throws Exception {
        String att = getAttribute(element, css, attribute);

        if (trim) {
            return att.trim();
        }

        return att;
    }

    public void clear(String css) throws Exception {
        getElement(css).clear();
    }

    public void clear(WebElement element, String css) throws Exception {
        getElement(element, css).clear();
    }

    public void type(String css, String key) throws Exception {
        getElement(css).sendKeys(key);
    }

    public void type(WebElement element, String css, String key) throws Exception {
        getElement(element, css).sendKeys(key);
    }

    public void selectByText(String css, String text) throws Exception {
        // 下拉选择框根据选项文本值选择
        select = new Select(getElement(css));
        select.selectByVisibleText(text);
    }

    public void selectByText(WebElement element, String css, String text) throws Exception {
        // 下拉选择框根据选项文本值选择
        select = new Select(getElement(element, css));
        select.selectByVisibleText(text);
    }

    public void selectByIndex(String css, int index) throws Exception {
        // 拉选择框根据索引值选择
        select = new Select(getElement(css));
        select.selectByIndex(index);
    }

    public void selectByIndex(WebElement element, String css, int index) throws Exception {
        // 拉选择框根据索引值选择
        select = new Select(getElement(element, css));
        select.selectByIndex(index);
    }

    public void selectByValue(String css, String value) throws Exception {
        // 下列选择框根据元素属性值(value)选择
        select = new Select(getElement(css));
        select.selectByValue(value);
    }

    public void selectByValue(WebElement element, String css, String value) throws Exception {
        // 下列选择框根据元素属性值(value)选择
        select = new Select(getElement(element, css));
        select.selectByValue(value);
    }

    /**
     * 获取弹出对话框内容
     */
    public String getAlertText() {
        alert = driver.switchTo().alert();
        return alert.getText();
    }

    /**
     * 关闭或取消弹出对话框
     */
    public void alertDismiss() {
        alert = driver.switchTo().alert();
        alert.dismiss();
    }

    /**
     * 弹出对话框点击确定
     */
    public void alertAccept() {
        alert = driver.switchTo().alert();
        alert.accept();
    }

    /**
     * 弹出对话框输入文本字符串
     */
    public void typeAlert(String text) {
        alert = driver.switchTo().alert();
        alert.sendKeys(text);
    }

    /**
     * 切换frame
     */
    public void switchToFrame(String css) throws Exception {
        driver.switchTo().frame(getElement(css));
    }

    /**
     * 切换到父frame
     */
    public void switchToParentFrame() {
        driver.switchTo().parentFrame();
    }

    /**
     * 聚焦在顶部窗口/第一个frame上
     */
    public void switchToFirstFrame() {
        driver.switchTo().defaultContent();
    }

    public Map<String, String> getCookie(String name) {
        Cookie cookies = driver.manage().getCookieNamed(name);
        if (cookies != null) {
            Map<String, String> cookie = new HashMap<>();
            cookie.put("name", cookies.getName());
            cookie.put("value", cookies.getValue());
            cookie.put("path", cookies.getPath());
            cookie.put("domain", cookies.getDomain());
            cookie.put("expiry", cookies.getExpiry().toString());
            return cookie;
        }
        return null;
    }

    public Set<Cookie> getCookies() {
        return driver.manage().getCookies();
    }

    public void addCookie(String name, String value) {
        driver.manage().addCookie(new Cookie(name, value));
    }

    public void addCookie(String name, String value, String path) {
        driver.manage().addCookie(new Cookie(name, value, path));
    }

    public void deleteCookie(String name) {
        driver.manage().deleteCookieNamed(name);
    }

    public void deleteCookies() {
        driver.manage().deleteAllCookies();
    }

    private Map<String, String> cssSplit(String css) throws Exception {
        Map<String, String> cssMap = new HashMap<>();

        if (css.contains("=>")) {
            String[] cssS = css.split("=>", 2);

            cssMap.put("by", cssS[0]);
            cssMap.put("value", cssS[1]);

            return cssMap;
        } else {
            throw new Exception("Please enter the correct targeting elements");
        }
    }

    public By getLocator(String css) throws Exception {
        Map<String, String> cssMap = cssSplit(css);

        By locator = null;

        String by = cssMap.get("by");
        if (by.equals("id")) {
            locator = By.id(cssMap.get("value"));
        } else if (by.equals("name")) {
            locator = By.name(cssMap.get("value"));
        } else if (by.equals("class")) {
            locator = By.className(cssMap.get("value"));
        } else if (by.equals("link")) {
            locator = By.linkText(cssMap.get("value"));
        } else if (by.equals("css")) {
            locator = By.cssSelector(cssMap.get("value"));
        } else if (by.equals("tag")) {
            locator = By.tagName(cssMap.get("value"));
        } else if (by.equals("xpath")) {
            locator = By.xpath(cssMap.get("value"));
        } else {
            throw new Exception("Support css: id, name, class, link, css, tag, xpath");
        }

        return locator;
    }

    public WebElement getElement(String css) throws Exception {
        waitUntilElementVisible(css, 30);
        return driver.findElement(getLocator(css));
    }

    public WebElement getElement(WebElement element, String css) throws Exception {
        return element.findElement(getLocator(css));
    }

    public List<WebElement> getElements(String css) throws Exception {
        waitUntilElementVisible(css, 30);
        return driver.findElements(getLocator(css));
    }

    public List<WebElement> getElements(WebElement element, String css) throws Exception {
        return element.findElements(getLocator(css));
    }

    public Boolean waitUntilContainText(String text, long seconds) throws Exception{
        // 指定时间内等待直到页面包含文本字符串
        try {
            return new WebDriverWait(driver, seconds, 200)
                    .until(ExpectedConditions.textToBePresentInElement(getElement("tag=>body"), text));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Boolean waitUntilContainText(String text) throws Exception{
        try {
            return new WebDriverWait(driver, timeOutSeconds, 200)
                    .until(ExpectedConditions.textToBePresentInElement(getElement("tag=>body"), text));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Boolean waitUntilElementVisible(String css, int seconds) throws Exception{
        // 指定时间内等待直到元素存在于页面的DOM上并可见, 可见性意味着该元素不仅被显示, 而且具有大于0的高度和宽度
        try {
            new WebDriverWait(driver, seconds, 200)
                    .until(ExpectedConditions.visibilityOfElementLocated(getLocator(css)));
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Boolean waitUntilElementVisible(String css) throws Exception{
        try {
            new WebDriverWait(driver, timeOutSeconds, 200)
                    .until(ExpectedConditions.visibilityOfElementLocated(getLocator(css)));
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public String getWindowHandle() {
        return driver.getWindowHandle();
    }

    public Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }

    public void switchWindow(String handle) {
        driver.switchTo().window(handle);
    }

    public void switchWindowByTitle(String title, int time) {
        Set<String> windowHandles = getWindowHandles();

        int counter = 0;

        while (counter < time) {
            Iterator iterator = windowHandles.iterator();
            while (iterator.hasNext()) {
                String windowHandle = (String) iterator.next();
                switchWindow(windowHandle);

                // 判断是否是指定的title
                if (getTitle().contains(title)) {
                    return;
                }

                sleep(100);
            }

            counter ++;
            sleep(1000);
        }
    }

    public void switchWindowByTitle(String title) {
        switchWindowByTitle(title, 10);
    }

    public void openWindowByJS(String url) {
        executeJS(String.format("window.open(\"%s\");", url));
    }

    public void closeOtherWindow() {
        // 关闭其他所有页面，留当前一个
        String handle = getWindowHandle();
        for (String windowHandle : getWindowHandles()) {
            if(!windowHandle.equals(handle)) {
                close();
            }else {
                switchWindow(windowHandle);
            }

            sleep(100);
        }
    }

    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // 什么也不做，无关紧要
        }
    }

    public void close() {
        driver.close();
    }

    public void quit() {
        driver.quit();
    }
}

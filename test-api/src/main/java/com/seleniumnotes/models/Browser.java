package com.seleniumnotes.models;

import com.seleniumnotes.Config;
import com.seleniumnotes.models.browser.Browsers;
import com.seleniumnotes.models.users.Visitor;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static com.seleniumnotes.Config.WAITTIME_ELEMENTOCCURENCE;
import static com.seleniumnotes.Config.WAITTIME_INSTANT;
import static com.seleniumnotes.Config.WAITTIME_SMALL;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.support.PageFactory.initElements;

public abstract class Browser {

    private static final Logger LOGGER = LogManager.getLogger(Browser.class);

    protected WebDriver webDriver;
    protected HtmlUnitDriver unitDriver;
    private Page page;
    private Visitor visitor;

    public static Browser openThe(Page page, Set<Cookie> cookies) {
        return Browsers.runDefault().open(page, cookies);
    }

    public void resizeWindow(int x, int y) {
        webDriver.manage().window().setSize(new Dimension(x, y));
    }

    protected abstract void initInLocal();

    public final Browser byMe(Visitor visitor) {
        this.visitor = visitor;
        return this;
    }

    private Browser open(Page page, Set<Cookie> cookies) {
        initInLocal();

        this.page = page;
        this.page.setBrowser(this);
        webDriver.get(page.url());
        if (webDriver != unitDriver) {
            waitForAjax();
        }

        if (cookies != null) {
            cookies.forEach((cookie) -> webDriver.manage().addCookie(cookie));
        }

        return this;
    }

    public void takeScreenshot(String scrFilename) {
        File scrFile = ((TakesScreenshot) webDriver).getScreenshotAs(
                OutputType.FILE);
        File directory = new File(Config.screenshotPath);

        if (!directory.exists())
            directory.mkdir();

        File outputFile = new File(Config.screenshotPath, scrFilename + ".png");

        try {
            FileUtils.copyFile(scrFile, outputFile);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public final Browser changePage(Page page) {
        this.page = page;
        this.page.setBrowser(this);
        return this;
    }

    public <P extends Page> Browser goTo(P page) {
        this.page = page;
        this.page.setBrowser(this);
        webDriver.get(page.url());
        waitForAjax();
        return this;
    }

    public void refresh() {
        webDriver.get(page.url());
        waitForAjax();
    }

    public void refreshCurrentUrl() {
        webDriver.get(currentURL());
        waitForAjax();
    }

    public void goBack() {
        webDriver.navigate().back();
        waitForAjax();
    }

    public void goForward() {
        webDriver.navigate().forward();
    }

    public final Page page() {
        return this.page;
    }

    public final Visitor visitor() {
        return this.visitor;
    }

    public void loadElements(Object webComponent) {
        initElements(webDriver, webComponent);
    }

    public void loadElements() {
        initElements(webDriver, page);
    }

    public String currentURL() {
        return webDriver.getCurrentUrl();
    }

    public void close() {
        try {
            webDriver.quit();
        } catch (Exception ex) {
            LOGGER.error("Exception on closing webdriver", ex);
        }
    }

    public WebElement findElement(By by) {
        return webDriver.findElement(by);
    }

    private WebElement findElement(By by, WebElement element) {
        return element.findElement(by);
    }

    public List<WebElement> findElements(By by) {
        return webDriver.findElements(by);
    }

    public void waitForAjax() {
        try {
            WebDriverWait myWait = new WebDriverWait(webDriver, WAITTIME_SMALL);
            ExpectedCondition<Boolean> conditionToCheck = input -> {
                JavascriptExecutor jsDriver = (JavascriptExecutor) webDriver;
                boolean stillRunningAjax = (Boolean) jsDriver
                        .executeScript("return window.jQuery != undefined && jQuery.active != 0");
                return !stillRunningAjax;
            };

            myWait.until(conditionToCheck);
        } catch (Throwable ex) {
            LOGGER.warn("Ajax Waiting Time Expired");
        }
    }

    public void wait(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (Exception ex) {
            LOGGER.error("Exception on selenium waiting", ex);
        }
    }

    public void scrollTo(WebElement element) {
        findElementUntilClickable(WAITTIME_ELEMENTOCCURENCE, element);
        scrollTo(element.getLocation().x, element.getLocation().y - 200);
    }

    public void scrollTo(int x, int y) {
        js("scrollTo(" + x + "," + y + ");");
    }

    public Object js(String script) {
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        Object response = js.executeScript(script);
        waitForAjax();

        return response;
    }

    public void clickTo(By by) {
        clickTo(findElementUntilClickable(WAITTIME_ELEMENTOCCURENCE, by));
    }

    public void clickTo(WebElement element) {
        scrollTo(element);
        element.click();
        waitForAjax();
    }

    public WebElement findElementUntilVisible(int timeoutInSeconds, By by) {
        return new FluentWait<>(webDriver).
                withTimeout(timeoutInSeconds, SECONDS).
                pollingEvery(WAITTIME_INSTANT, MILLISECONDS).
                ignoring(NotFoundException.class).ignoring(NoSuchElementException.class).
                until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public WebElement findElementUntilVisible(int timeoutInSeconds, WebElement element) {
        return new FluentWait<>(webDriver).
                withTimeout(timeoutInSeconds, SECONDS).
                pollingEvery(WAITTIME_INSTANT, MILLISECONDS).
                ignoring(NotFoundException.class).ignoring(NoSuchElementException.class).
                until(ExpectedConditions.visibilityOf(element));
    }

    public List<WebElement> findElementsUntilVisible(int timeoutInSeconds, By by) {
        return new FluentWait<>(webDriver).
                withTimeout(timeoutInSeconds, SECONDS).
                pollingEvery(500, MILLISECONDS).
                ignoring(NotFoundException.class).ignoring(NoSuchElementException.class).
                until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
    }

    public WebElement findElementUntilClickable(int timeoutInSeconds, By by) {
        return new FluentWait<>(webDriver).
                withTimeout(timeoutInSeconds, SECONDS).
                pollingEvery(WAITTIME_INSTANT, MILLISECONDS).
                ignoring(NotFoundException.class).ignoring(NoSuchElementException.class).
                until(ExpectedConditions.elementToBeClickable(by));
    }

    public WebElement findElementUntilClickable(int timeoutInSeconds, WebElement element) {
        return new FluentWait<>(webDriver).
                withTimeout(timeoutInSeconds, SECONDS).
                pollingEvery(WAITTIME_INSTANT, MILLISECONDS).
                ignoring(NotFoundException.class).ignoring(NoSuchElementException.class).
                until(ExpectedConditions.elementToBeClickable(element));
    }

    public boolean isElementPresent(By by) {
        return !findElements(by).isEmpty();
    }

    public boolean isElementPresent(WebElement element) {
        try {
            element.isDisplayed();
            return true;
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }
}

package com.seleniumnotes.models.browser;

import com.seleniumnotes.models.Browser;
import org.openqa.selenium.firefox.FirefoxDriver;

import static com.seleniumnotes.Config.WAITTIME_INSTANT;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class Firefox extends Browser {

    @Override
    protected void initInLocal() {
        webDriver = new FirefoxDriver();
        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(WAITTIME_INSTANT, MILLISECONDS);
    }
}
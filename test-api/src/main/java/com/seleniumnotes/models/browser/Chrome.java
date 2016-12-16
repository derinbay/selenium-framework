package com.seleniumnotes.models.browser;

import com.seleniumnotes.models.Browser;
import org.openqa.selenium.chrome.ChromeDriver;

import static com.seleniumnotes.Config.WAITTIME_INSTANT;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Created by Taylan on 17/12/2016.
 */
public class Chrome extends Browser {

    @Override
    protected void initInLocal() {
        webDriver = new ChromeDriver();
        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(WAITTIME_INSTANT, MILLISECONDS);
    }
}

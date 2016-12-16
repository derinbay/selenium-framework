package com.seleniumnotes.models.browser;

import com.seleniumnotes.models.Browser;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Unit extends Browser {

    @Override
    protected void initInLocal() {
        DesiredCapabilities unitCapabilities = DesiredCapabilities.htmlUnit();
        unitCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        unitDriver = new HtmlUnitDriver();
        unitDriver.setJavascriptEnabled(true);
        webDriver = unitDriver;
    }
}

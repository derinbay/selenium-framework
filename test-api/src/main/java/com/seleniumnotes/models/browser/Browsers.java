package com.seleniumnotes.models.browser;

import com.seleniumnotes.models.Browser;
import com.seleniumnotes.Config;

public class Browsers {

    public static String defaultBrowserName() {
        return Config.defaultBrowserName;
    }

    public static Browser run(BrowserType browserType) {
        Browser browser = null;

        switch (browserType) {
            case FIREFOX:
                browser = new Firefox();
                break;

            case INTERNETEXPLORER:
                browser = new InternetExplorer();
                break;

            case HTMLUNIT:
                browser = new Unit();
                break;

            default:
            case CHROME:
                browser = new Chrome();
                break;
        }

        return browser;
    }

    public static Browser runDefault() {
        return run(BrowserType.byName(defaultBrowserName()));
    }
}

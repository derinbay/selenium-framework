package com.seleniumnotes.models;

import com.seleniumnotes.models.users.Visitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Page<V extends Visitor> extends WebComponent {

    private static final Logger logger = LogManager.getLogger(Page.class);

    protected String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setBrowser(Browser browser) {
        this.browser = browser;
    }

    public String url() {
        return this.url;
    }

    public String currentUrl() {
        return this.browser.currentURL();
    }

    public void refresh() {
        browser.refresh();
    }
}

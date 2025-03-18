package org.gexron.driver;

public class ProxyWebDriverFactory {

    public ProxyWebDriver getProxyWebDriver() {
        return new UberWebDriver();
    }
}

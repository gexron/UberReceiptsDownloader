package org.gexron.driver;

public class ProxyWebDriverFactory {

    public ProxyWebDriver getProxyWebDriver(String tripProvider) {
        return new UberWebDriver();
    }
}

package org.gexron.authentication;

import org.openqa.selenium.WebDriver;

public class BrowserAuthenticationFlow implements AuthenticationFlow {

    private WebDriver driver;

    public BrowserAuthenticationFlow(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public void process() {
        waitForUserLogin(driver);
    }

    private void waitForUserLogin(WebDriver driver) {
        while (true) {
            try {
                if (driver.getTitle().toLowerCase().contains("my trips")) {
                    break;
                }
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

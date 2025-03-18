package org.gexron;

import org.gexron.driver.ProxyWebDriver;
import org.gexron.driver.ProxyWebDriverFactory;
import org.gexron.receipts_download.Trip;

public class UberReceiptsDownloader {

    public static void main(String[] args) {
        ProxyWebDriver driver = new ProxyWebDriverFactory().getProxyWebDriver("Uber");

        try {
            driver.get(Trip.UBER_TRIPS_URL);
            driver.authenticate();
            driver.downloadMonthReceipts();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            driver.quit();
        }
    }
}
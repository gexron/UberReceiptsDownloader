package org.gexron.driver;

import org.gexron.authentication.AuthenticationFlow;
import org.gexron.authentication.BrowserAuthenticationFlow;
import org.gexron.driver.web_elements.UberWebElementsDirectory;
import org.gexron.receiptsdownload.ReceiptsDownloadFlow;
import org.gexron.receiptsdownload.UberReceiptsDownloadFlow;

import java.util.logging.Logger;

public class UberWebDriver extends ProxyWebDriver {

    private static final Logger logger = Logger.getAnonymousLogger();

    private AuthenticationFlow authenticationFlow;
    private ReceiptsDownloadFlow receiptsDownloadFlow;

    UberWebDriver() {
        super();
        webElementsDirectory = UberWebElementsDirectory.getInstance();
        authenticationFlow = new BrowserAuthenticationFlow(driver);
        receiptsDownloadFlow = new UberReceiptsDownloadFlow(driver, driverWait, webElementsDirectory);
    }

    @Override
    public void authenticate() {
        logger.info("Started authentication");
        authenticationFlow.process();
        logger.info("Finished authentication");
    }

    @Override
    public void downloadMonthReceipts() {
        logger.info("Started downloading month's receipts");
        receiptsDownloadFlow.process();
        logger.info("Finished downloading month's receipts");
    }

}

package org.gexron.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public interface WebElementsDirectory {

    WebElement getUsernameTextbox(WebDriverWait driverWait);

    WebElement getPasswordTextbox(WebDriverWait driverWait);

    List<WebElement> getOtpTextbox(WebDriverWait driverWait);

    WebElement getMoreButton(WebDriver driver);

    WebElement getTripsSectionHeader(WebDriverWait driverWait);

    WebElement getTripsPeriodListButton(WebDriverWait driverWait);

    WebElement getCurrentMonthOption(WebDriverWait driverWait);

    List<WebElement> getTripsDivsAfterInitialLoading(WebDriver driver);

    List<WebElement> getTripsDivsAfterLoadingMoreResults(WebDriver driver);

    WebElement getViewReceiptButton(WebDriverWait driverWait);

    WebElement getDownloadPdfButton(WebDriverWait driverWait);

    WebElement getContinueButton(WebDriverWait driverWait);

    WebElement getTitle(WebDriverWait driverWait);

    String getClickScript();
}

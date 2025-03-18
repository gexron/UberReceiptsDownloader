package org.gexron.driver.web_elements;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Collections;
import java.util.List;

public class UberWebElementsDirectory implements WebElementsDirectory {
    private static UberWebElementsDirectory uberWebElementsDirectory;

    private static final String PHONE_EMAIL_ID = "PHONE_NUMBER_or_EMAIL_ADDRESS";
    private static final String OTP_XPATH = "//input[contains(@id, 'OTP')]";
    private static final String PASSWORD_ID = "PASSWORD";
    private static final String TRIPS_PERIOD_LIST_XPATH = "//div/div[@data-baseweb='select'][2]/div";
    private static final String TRIPS_SECTION_HEADER_XPATH = "//h3";
    private static final String MORE_BUTTON_XPATH = "//button[@data-baseweb='button']";
    private static final String CURRENT_MONTH_OPTION_XPATH = "//ul[@data-baseweb='menu']/li[3]";//TODO: return value back to 3
    private static final String FIRST_LOAD_LAST_TRIP_DIV_XPATH = "//main/div/div[1]/div[3]/div";
    private static final String FIRST_LOAD_TRIPS_DIVS_XPATH = "//main/div/div[1]/div[4]/span/div";
    private static final String MORE_LOADED_TRIPS_DIVS_XPATH = "//main/div/div/div[2]/span/div";
    private static final String VIEW_RECEIPT_BUTTON_XPATH = "//div[@data-tracking-name='cta-button-row']/div/button";
    private static final String DOWNLOAD_PDF_LINK_XPATH = "//a[@data-tracking-name='download-receipt-pdf-link']";
    private static final String TITLE_XPATH = "//h1";
    private static final String FORWARD_BUTTON_ID = "forward-button";

    // First load
    ////main/div/div[1]/div[3]/div
    ////main/div/div[1]/div[4]/span/div

    // After loading more results
    ////main/div/div/div[2]/span/div

    public static UberWebElementsDirectory getInstance() {
        if (uberWebElementsDirectory == null)
            uberWebElementsDirectory = new UberWebElementsDirectory();
        return uberWebElementsDirectory;
    }
    private UberWebElementsDirectory() {
        
    }
    @Override
    public WebElement getUsernameTextbox(WebDriverWait driverWait) {
        return driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(PHONE_EMAIL_ID)));
    }

    @Override
    public WebElement getPasswordTextbox(WebDriverWait driverWait) {
        return driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(PASSWORD_ID)));
    }

    @Override
    public List<WebElement> getOtpTextbox(WebDriverWait driverWait) {
        return driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(OTP_XPATH)));
    }

    @Override
    public WebElement getMoreButton(WebDriver driver) {
        return driver.findElement(By.xpath(MORE_BUTTON_XPATH));
    }

    @Override
    public WebElement getTripsSectionHeader(WebDriverWait driverWait) {
        return driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(TRIPS_SECTION_HEADER_XPATH)));
    }

    @Override
    public WebElement getTripsPeriodListButton(WebDriverWait driverWait) {
        return driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(TRIPS_PERIOD_LIST_XPATH)));
    }

    @Override
    public WebElement getCurrentMonthOption(WebDriverWait driverWait) {
        return driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CURRENT_MONTH_OPTION_XPATH)));
    }

    @Override
    public List<WebElement> getTripsDivsAfterInitialLoading(WebDriver driver) {
        List<WebElement> tripsDivs = driver.findElements(By.xpath(FIRST_LOAD_TRIPS_DIVS_XPATH));
        WebElement lastTripDiv;
        try {
            lastTripDiv = driver.findElement(By.xpath(FIRST_LOAD_LAST_TRIP_DIV_XPATH));
            tripsDivs.add(lastTripDiv);
        }
        catch (NoSuchElementException e) {
            return Collections.emptyList();
        }
        return tripsDivs;
    }

    @Override
    public List<WebElement> getTripsDivsAfterLoadingMoreResults(WebDriver driver) {
        return driver.findElements(By.xpath(MORE_LOADED_TRIPS_DIVS_XPATH));
    }

    @Override
    public WebElement getViewReceiptButton(WebDriverWait driverWait) {
        return driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(VIEW_RECEIPT_BUTTON_XPATH)));
    }

    @Override
    public WebElement getDownloadPdfButton(WebDriverWait driverWait) {
        return driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DOWNLOAD_PDF_LINK_XPATH)));
    }

    @Override
    public WebElement getTitle(WebDriverWait driverWait) {
        return driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(TITLE_XPATH)));
    }

    public WebElement getContinueButton(WebDriverWait driverWait) {
        return driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(FORWARD_BUTTON_ID)));
    }

    public String getClickScript() {
        return "let btn = arguments[0];" +
                "btn.dispatchEvent(new MouseEvent('mousedown', { bubbles: true }));" +
                "btn.dispatchEvent(new MouseEvent('mouseup', { bubbles: true }));" +
                "btn.dispatchEvent(new MouseEvent('click', { bubbles: true }));";
    }
}

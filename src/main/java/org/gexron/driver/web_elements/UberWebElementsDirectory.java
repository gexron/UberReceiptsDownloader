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

    private static final String TRIPS_SECTION_HEADER_XPATH = "//h3";
    private static final String MORE_BUTTON_XPATH = "//button[@data-baseweb='button']";
    private static final String FIRST_LOAD_LAST_TRIP_DIV_XPATH = "//main/div/div[1]/div[3]/div";
    private static final String FIRST_LOAD_TRIPS_DIVS_XPATH = "//main/div/div[1]/div[4]/span/div";
    private static final String MORE_LOADED_TRIPS_DIVS_XPATH = "//main/div/div/div[2]/span/div";

    public static UberWebElementsDirectory getInstance() {
        if (uberWebElementsDirectory == null)
            uberWebElementsDirectory = new UberWebElementsDirectory();
        return uberWebElementsDirectory;
    }
    private UberWebElementsDirectory() {

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

    public String getClickScript() {
        return "let btn = arguments[0];" +
                "btn.dispatchEvent(new MouseEvent('mousedown', { bubbles: true }));" +
                "btn.dispatchEvent(new MouseEvent('mouseup', { bubbles: true }));" +
                "btn.dispatchEvent(new MouseEvent('click', { bubbles: true }));";
    }
}

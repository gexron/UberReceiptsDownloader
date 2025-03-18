package org.gexron.driver.web_elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public interface WebElementsDirectory {

    WebElement getMoreButton(WebDriver driver);

    WebElement getTripsSectionHeader(WebDriverWait driverWait);

    List<WebElement> getTripsDivsAfterInitialLoading(WebDriver driver);

    List<WebElement> getTripsDivsAfterLoadingMoreResults(WebDriver driver);

    List<String> getTripStartAndEnd(WebDriver driver);

    WebElement getSingleTripSectionHeader(WebDriverWait driverWait);

    String getClickScript();
}

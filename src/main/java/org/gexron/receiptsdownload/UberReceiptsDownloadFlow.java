package org.gexron.receiptsdownload;

import org.gexron.driver.WebElementsDirectory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class UberReceiptsDownloadFlow implements ReceiptsDownloadFlow {

    private static final Logger logger = Logger.getAnonymousLogger();

    private WebDriver driver;
    private WebDriverWait driverWait;
    private WebElementsDirectory webElementsDirectory;

    private boolean moreResultsLoaded;

    public UberReceiptsDownloadFlow(WebDriver driver, WebDriverWait driverWait, WebElementsDirectory webElementsDirectory) {
        this.driver = driver;
        this.driverWait = driverWait;
        this.webElementsDirectory = webElementsDirectory;
    }

    @Override
    public void process() {
        logger.info("Selecting current month");
        LocalDateTime monthSelected = selectMonth();

        logger.info("Loading all trips");
        List<Trip> trips = loadAllTrips(monthSelected);

        if (trips.isEmpty()) {
            logger.info("No trips found!");
            return;
        }

        logger.info("Start filtering trips");
        List<String> receiptDownloadLinks = getCompletedTripsReceiptsDownloadLinks(trips);

        logger.info("Start downloading receipts");
        downloadReceipts(receiptDownloadLinks);
        logger.info("Finished downloading receipts");
    }

    private void downloadReceipts(List<String> receiptDownloadLinks) {
        receiptDownloadLinks.forEach(url -> {
            ((JavascriptExecutor) driver).executeScript("window.open('" + url + "', '_blank');");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private List<String> getCompletedTripsReceiptsDownloadLinks(List<Trip> trips) {
        return trips
                .stream()
                .filter(trip -> !(trip.getTripCost().contains("EGP0") || trip.getTripCost().contains("EGP 0")))
                .map(Trip::getTripReceiptDownloadUrl)
                .collect(Collectors.toList());
    }

    private LocalDateTime selectMonth() {
        Scanner sc = new Scanner(System.in);
        String currentMonth = LocalDate.now().getMonth().toString();
        String previousMonth = LocalDate.now().minusMonths(1L).getMonth().toString();
        System.out.println("Type A or B then press 'Enter':\nA) " + currentMonth + "\nB) " + previousMonth);

        LocalDateTime monthSelected = null;

        while (monthSelected == null) {
            char optionSelected = sc.next().toLowerCase().charAt(0);
            switch (optionSelected) {
                case 'a':
                    monthSelected = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).withDayOfMonth(1); break;
                case 'b':
                    monthSelected = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).withDayOfMonth(1).minusMonths(1L); break;
                default:
                    System.out.println("Invalid option. Please try again");
            }
        }
        return monthSelected;
    }

    private void clickMore() {
        WebElement selectMenu = webElementsDirectory.getMoreButton(driver);

        ((JavascriptExecutor) driver).executeScript(webElementsDirectory.getClickScript(), selectMenu);
    }

    private void waitTripsSectionLoaded() {
        webElementsDirectory.getTripsSectionHeader(driverWait);
    }

    private List<WebElement> getTripsDivs() {
        if (moreResultsLoaded) {
            return webElementsDirectory.getTripsDivsAfterLoadingMoreResults(driver);
        }
        return webElementsDirectory.getTripsDivsAfterInitialLoading(driver);
    }

    private List<Trip> getTrips() {
        return getTripsDivs()
                .stream()
                .map(tripDiv -> {
                    Trip trip = new Trip();
                    trip.setId(tripDiv.getAttribute("href").split("/")[4]);
                    String costAndStatus = tripDiv.findElement(By.xpath("./div[2]/div[2]/div/div[1]")).getText().split("\n")[1];
                    trip.setTripCost(costAndStatus.split(" • ")[0]);
                    return trip;
                })
                .collect(Collectors.toList());
    }

    private List<Trip> loadAllTrips(LocalDateTime monthSelected) {
        long startTimeInMs = monthSelected.toEpochSecond(ZoneOffset.UTC) * 1000;
        startTimeInMs = 1704060000000L;
        long endTimeInMs = monthSelected.plusMonths(1L).minusSeconds(1L).toEpochSecond(ZoneOffset.UTC) * 1000;

        String uberUrl = Trip.UBER_TRIPS_URL + "?from="
                + startTimeInMs
                + "&to="
                + endTimeInMs;

        List<Trip> trips = new ArrayList<>(50);
        driver.get(uberUrl);
        while(true) {
            waitTripsSectionLoaded();
            try {
                trips.addAll(this.getTrips());
                clickMore();
                moreResultsLoaded = true;
                logger.info("More results loaded");
            }
            catch (NoSuchElementException e) {
                break;
            }
        }
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return trips;
    }
}

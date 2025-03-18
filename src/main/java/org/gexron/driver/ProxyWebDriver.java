package org.gexron.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.gexron.driver.web_elements.WebElementsDirectory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public abstract class ProxyWebDriver {

    private static final Logger logger = Logger.getAnonymousLogger();

    WebDriver driver;
    WebDriverWait driverWait;
    WebElementsDirectory webElementsDirectory;

    ProxyWebDriver() {
        String userDataDir = getBrowserUserDataDir();
        if (userDataDir == null) {
            logger.info("Could not detect Browser user data directory. Exiting...");
            System.exit(1);
        }

        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();
            options.addArguments("user-data-dir=" + userDataDir);
            options.addArguments("profile-directory=Default");
            options.setExperimentalOption("excludeSwitches", new String[]{"load-extension", "enable-automation"});
            driver = new ChromeDriver(options);
        }
        else {
            WebDriverManager.edgedriver().setup();

            EdgeOptions options = new EdgeOptions();
            Map<String, Object> edgeOptions = new HashMap<>();
            options.setCapability("ms:edgeOptions", edgeOptions);
            driver = new EdgeDriver(options);
        }

        driver.manage().timeouts().pageLoadTimeout(50L, TimeUnit.SECONDS);
        driverWait = new WebDriverWait(driver, 20L,500L);
    }

    public void get(String url) {
        driver.get(url);
    }

    public void quit() {
        driver.quit();
    }

    private static String getBrowserUserDataDir() {
        String os = System.getProperty("os.name").toLowerCase();
        String userHome = System.getProperty("user.home");

        String browserProfilePath = null;
        if (os.contains("win")) {
            browserProfilePath = Paths.get(userHome, "AppData", "Local", "Microsoft Edge", "User Data").toString();
        } else if (os.contains("mac")) {
            browserProfilePath = Paths.get(userHome, "Library", "Application Support", "Google", "Chrome").toString();
        }

        if (browserProfilePath != null && Files.exists(Paths.get(browserProfilePath))) {
            return browserProfilePath;
        }
        return null;
    }

    public abstract void authenticate();

    public abstract void downloadMonthReceipts();
}

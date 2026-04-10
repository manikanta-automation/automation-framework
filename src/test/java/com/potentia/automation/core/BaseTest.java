package com.potentia.automation.core;

import java.time.Duration;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.potentia.automation.utils.ConfigReader;



public class BaseTest {
	protected static WebDriver driver;

	 @BeforeSuite
	 public void setUpSuite() {
		 String browser = ConfigReader.get("browser");
		    boolean headless = Boolean.parseBoolean(ConfigReader.get("headless"));

		    if (browser.equalsIgnoreCase("chrome")) {

		        ChromeOptions options = new ChromeOptions();
		        options.addArguments("--start-maximized");
		        options.addArguments("--remote-allow-origins=*");
		        options.setPageLoadStrategy(PageLoadStrategy.EAGER);

		        if (headless) {
		            options.addArguments("--headless=new");
		            options.addArguments("--window-size=1920,1080");
		        }

		        // ✅ Selenium Manager auto-matches ChromeDriver to your Chrome version
		        driver = new ChromeDriver(options);

		    } else {
		        throw new RuntimeException("Unsupported browser in config.properties: " + browser);
		    }

		    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

		    int pageLoadTimeout = Integer.parseInt(ConfigReader.get("pageLoadTimeout"));
		    driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));

		    driver.get(ConfigReader.get("url"));
		}
    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {
        if (driver != null) {
            driver.quit();
        }
    }
}

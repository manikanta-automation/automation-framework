package com.potentia.automation.pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SuperClientsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public SuperClientsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // =========================
    // Locators
    // =========================
    private final By pageHeading = By.xpath("//h1[normalize-space()='My Super Clients']");
    private final By superClientLinks = By.xpath("//ol[contains(@class,'user-super-client-list')]//a");
    private final By profileDropdownButton = By.cssSelector("div.user-profile-info > button.dropdown-toggle");
    private final By profileDropdownMenu = By.xpath("//ul[contains(@class,'dropdown-menu') and @aria-labelledby='profilearea']");

    private final By profileOption = By.xpath("//a[contains(@href,'facprofile') and normalize-space()='Profile']");
    private final By mySuperClientsOption = By.xpath("//a[contains(@href,'fachome') and contains(normalize-space(),'My Super Clients')]");
    private final By changePasswordOption = By.xpath("//a[contains(@href,'passwordchange') and contains(normalize-space(),'Change Password')]");
    private final By signOutOption = By.id("user_body_signOut");

    private final By loggedInUserName = By.id("user_currentAccount_primary");
    private final By currentAccountName = By.id("user_signedInTitle");

    private final By headerLogo = By.xpath("//img[contains(@class,'logo') and @alt='V-Connect']");
    private final By footerLogo = By.xpath("//footer[contains(@class,'footer')]//img");

    // =========================
    // Reusable Wait Methods
    // =========================
    private WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    private WebElement waitForPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    
    private List<WebElement> waitForAllVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    private WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    // =========================
    // Page Actions / Validations
    // =========================
    public boolean isMySuperClientsHeadingDisplayed() {
        try {
            return waitForVisibility(pageHeading).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getPageHeadingText() {
        return waitForVisibility(pageHeading).getText().trim();
    }

    public boolean isSuperClientListDisplayed() {
        try {
            return !waitForAllVisible(superClientLinks).isEmpty();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public int getSuperClientCount() {
        return driver.findElements(superClientLinks).size();
    }

    public List<String> getAllSuperClientNames() {
        List<String> names = new ArrayList<>();
        List<WebElement> clients = waitForAllVisible(superClientLinks);

        for (WebElement client : clients) {
            names.add(client.getText().trim());
        }
        return names;
    }

    public boolean areAllSuperClientNamesNonEmpty() {
        List<WebElement> clients = waitForAllVisible(superClientLinks);
        for (WebElement client : clients) {
            if (client.getText() == null || client.getText().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean areAllSuperClientLinksClickable() {
        List<WebElement> clients = waitForAllVisible(superClientLinks);
        for (WebElement client : clients) {
            if (!client.isDisplayed() || !client.isEnabled()) {
                return false;
            }
        }
        return true;
    }

    public boolean areAllSuperClientLinksHavingHref() {
        List<WebElement> clients = waitForAllVisible(superClientLinks);
        for (WebElement client : clients) {
            String href = client.getAttribute("href");
            if (href == null || href.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void clickSuperClientByName(String superClientName) {
        By dynamicClientLink = By.xpath(
                "//ol[contains(@class,'user-super-client-list')]//a[normalize-space()='" + superClientName + "']"
        );
        waitForClickable(dynamicClientLink).click();
    }

    public boolean isProfileDropdownVisible() {
        try {
            WebElement profileButton = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(profileDropdownButton)
            );
            return profileButton.isDisplayed() && profileButton.isEnabled();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void openProfileDropdown() {
    	WebElement dropdownButton = wait.until(ExpectedConditions.elementToBeClickable(profileDropdownButton));

        try {
            dropdownButton.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdownButton);
        }

        wait.until(ExpectedConditions.attributeToBe(profileDropdownButton, "aria-expanded", "true"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(profileDropdownMenu));
    }

    public boolean isProfileOptionDisplayed() {
        try {
            return waitForVisibility(profileOption).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isMySuperClientsOptionDisplayed() {
        try {
            return waitForVisibility(mySuperClientsOption).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isChangePasswordOptionDisplayed() {
        try {
            return waitForVisibility(changePasswordOption).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isSignOutOptionDisplayed() {
        try {
            return waitForVisibility(signOutOption).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getLoggedInUserName() {
        return waitForVisibility(loggedInUserName).getText().trim();
    }

    public String getCurrentAccountName() {
        return waitForVisibility(currentAccountName).getText().trim();
    }

    public void clickProfileOption() {
        wait.until(ExpectedConditions.elementToBeClickable(profileOption)).click();
    }

    public boolean isUserNavigatedToProfilePage() {
        return driver.getCurrentUrl().contains("/facprofile");
    }
    
    public void clickMySuperClientsOption() {
        waitForClickable(mySuperClientsOption).click();
    }

    public void clickChangePasswordOption() {
        waitForClickable(changePasswordOption).click();
    }

    public void clickSignOut() {
        waitForClickable(signOutOption).click();
    }

    public boolean isHeaderLogoDisplayed() {
    	try {
            WebElement logo = waitForPresence(headerLogo);
            return logo.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isFooterLogoDisplayed() {
    	 try {
             WebElement logo = waitForPresence(footerLogo);
             return logo.isDisplayed();
         } catch (TimeoutException e) {
             return false;
         }
    }

    public boolean isCurrentUrlContains(String partialUrl) {
        return driver.getCurrentUrl().contains(partialUrl);
    }
    public void printAllSuperClients() {

        List<WebElement> superClients = driver.findElements(
                By.xpath("//ol[contains(@class,'user-super-client-list')]//a")
        );

        System.out.println("Total Super Clients: " + superClients.size());

        for (int i = 0; i < superClients.size(); i++) {

            String clientName = superClients.get(i).getText().trim();

            System.out.println((i + 1) + ". " + clientName);
        }
    }
}

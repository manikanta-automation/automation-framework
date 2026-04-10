package com.potentia.automation.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(12));
    }

    private final By emailInput = By.id("username");
    private final By passwordInput = By.id("password");
    private final By signInBtn = By.cssSelector("button[type='submit']");
    private final By rememberMe = By.id("rememberMe");
    private final By passwordEyeIcon  = By.id("togglePassword");

    // Forgot password (from your HTML)
    private final By forgotPasswordBtn = By.cssSelector("button[data-bs-target='#forgotPwdModal']");
    private final By forgotHeading = By.id("exampleModalLabel");
    private final By forgotEmailInput = By.id("email");
    private final By forgotSubmitBtn = By.xpath("//button[normalize-space()='Submit']");
    private final By forgotModal = By.id("forgotPwdModal");
    private final By forgotCloseBtn = By.xpath("//div[@id='forgotPwdModal']//button[normalize-space()='Close' or @aria-label='Close' or contains(@class,'btn-close')]");

    // Messages (keep as per your current app)
    private final By incorrectPasswordMsg = By.xpath("//p[contains(normalize-space(), 'Invalid password')]");
    private final By forgotEmailRequiredMsg = By.xpath("//div[normalize-space()='Please enter your email.']");
    private final By forgotEmailInvalidMsg = By.xpath("//div[normalize-space()='Please enter a valid email address.']");
    private final By unregisteredAccountMsg = By.xpath("//div[@role='alert' and contains(normalize-space(), \"Couldn't find your account\")]");
    private final By forgotSuccessMsg = By.xpath("//h6[normalize-space()='Success']");
    private final By serverErrorToast = By.xpath("//div[contains(text(),'Server not responding')]");

    // --- Actions ---
    public void waitForPageReady() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
        wait.until(ExpectedConditions.elementToBeClickable(signInBtn));
    }

    public void enterEmail(String email) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
        el.clear();
        el.sendKeys(email);
    }

    public void enterPassword(String password) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
        el.clear();
        el.sendKeys(password);
    }

    public void clickSignIn() {
        wait.until(ExpectedConditions.elementToBeClickable(signInBtn)).click();
    }

    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickSignIn();
    }
    public boolean isPasswordEyeDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(passwordEyeIcon)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getPasswordFieldType() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput))
                .getAttribute("type");
    }

    public void clickPasswordEyeIcon() {
        wait.until(ExpectedConditions.elementToBeClickable(passwordEyeIcon )).click();
    }

    // --- Remember me ---
    public boolean isRememberMeDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(rememberMe)).isDisplayed();
    }

    public boolean isRememberMeEnabled() {
        return driver.findElement(rememberMe).isEnabled();
    }

    public boolean isRememberMeSelected() {
        return driver.findElement(rememberMe).isSelected();
    }

    // --- Login errors ---
    public String getEmailErrorMsg() {
        // Try to fetch the closest error text near email section
        return getFieldErrorTextNear(emailInput);
    }

    public String getPasswordErrorMsg() {
        // Try to fetch the closest error text near password section
        return getFieldErrorTextNear(passwordInput);
    }

    private String getFieldErrorTextNear(By inputLocator) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(inputLocator));

        // search in the same form-group container
        WebElement container = input.findElement(By.xpath("./ancestor::div[contains(@class,'form-group')][1]"));
        // find first visible error-like element inside container
        for (WebElement el : container.findElements(By.xpath(".//*[self::div or self::span][contains(.,'This field is required') or contains(.,'Invalid email')]"))) {
            if (el.isDisplayed()) {
				return el.getText().trim();
			}
        }

        // fallback: global search (less ideal but prevents null)
        try {
            WebElement any = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(),'This field is required') or contains(text(),'Invalid email address')]")
            ));
            return any.getText().trim();
        } catch (TimeoutException e) {
            return "";
        }
    }

    public String getIncorrectPasswordMsg() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(incorrectPasswordMsg)).getText().trim();
    }

    // --- Forgot password ---
    public boolean isForgotPasswordLinkDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(forgotPasswordBtn)).isDisplayed();
    }

    public boolean isForgotPasswordLinkEnabled() {
        return driver.findElement(forgotPasswordBtn).isEnabled();
    }

    public void openForgotPasswordModal() {
    	 wait.until(ExpectedConditions.elementToBeClickable(forgotPasswordBtn)).click();
         wait.until(ExpectedConditions.visibilityOfElementLocated(forgotModal));
    }

    public boolean isForgotPasswordHeadingDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(forgotHeading)).isDisplayed();
    }

    public void submitForgotPassword(String email) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(forgotEmailInput));
        el.clear();
        el.sendKeys(email);
        wait.until(ExpectedConditions.elementToBeClickable(forgotSubmitBtn)).click();
    }

    public String getForgotEmailError() {
        // Could be required OR invalid
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(forgotEmailRequiredMsg)).getText().trim();
        } catch (TimeoutException e) {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(forgotEmailInvalidMsg)).getText().trim();
        }
    }

    public String getUnregisteredAccountError() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(unregisteredAccountMsg)).getText().trim();
    }

    public String getForgotSuccessMsg() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(forgotSuccessMsg)).getText().trim();
    }

    public boolean isForgotCloseDisplayed() {
    	openForgotPasswordModal(); // ensures modal open
        return wait.until(ExpectedConditions.visibilityOfElementLocated(forgotCloseBtn)).isDisplayed();
    }

    public boolean isForgotCloseEnabled() {
        return driver.findElement(forgotCloseBtn).isEnabled();
    }

    public void clickForgotClose() {
    	wait.until(ExpectedConditions.elementToBeClickable(forgotCloseBtn)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(forgotModal));
    }
    public boolean isServerErrorDisplayed() {
        return !driver.findElements(serverErrorToast).isEmpty()
                && driver.findElement(serverErrorToast).isDisplayed();
    }
    public String getServerErrorMessageText() {
        return driver.findElement(serverErrorToast).getText().trim();
    }
}

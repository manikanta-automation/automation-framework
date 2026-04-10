package com.potentia.automation.tests;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.potentia.automation.core.BaseTest;
import com.potentia.automation.listeners.ExcelReportListener;
import com.potentia.automation.pages.LoginPage;
import com.potentia.automation.utils.ConfigReader;


@Listeners(ExcelReportListener.class)
public class LoginTest extends BaseTest {
	 @Test(priority = 1)
	    public void TC_Login_01_emptyEmailAndPasswordTest() {
	        LoginPage loginPage = new LoginPage(driver);

	        loginPage.login("", "");

	        Assert.assertTrue(
	                loginPage.getEmailErrorMsg().contains(ConfigReader.get("REQUIRED_EMAIL_ERROR")),
	                "Email error mismatch!"
	        );

	        Assert.assertTrue(
	                loginPage.getPasswordErrorMsg().contains(ConfigReader.get("REQUIRED_PASSWORD_ERROR")),
	                "Password error mismatch!"
	        );
	    }

	    @Test(priority = 2)
	    public void TC_Login_02_emptyEmailTest() {
	        LoginPage loginPage = new LoginPage(driver);

	        loginPage.login("", ConfigReader.get("password"));

	        Assert.assertTrue(
	                loginPage.getEmailErrorMsg().contains(ConfigReader.get("REQUIRED_EMAIL_ERROR")),
	                "Email error mismatch!"
	        );
	    }

	    @Test(priority = 3)
	    public void TC_Login_03_emptyPasswordTest() {
	        LoginPage loginPage = new LoginPage(driver);

	        loginPage.login(ConfigReader.get("email"), "");

	        Assert.assertTrue(
	                loginPage.getPasswordErrorMsg().contains(ConfigReader.get("REQUIRED_PASSWORD_ERROR")),
	                "Password error mismatch!"
	        );
	    }

	    @Test(priority = 4)
	    public void TC_Login_04_invalidEmailTest() {
	        LoginPage loginPage = new LoginPage(driver);

	        loginPage.login(ConfigReader.get("invalid_email"), ConfigReader.get("password"));

	        Assert.assertTrue(
	                loginPage.getEmailErrorMsg().contains(ConfigReader.get("INVALID_EMAIL_ERROR")),
	                "Invalid email message mismatch!"
	        );
	    }

	    @Test(priority = 5)
	    public void TC_Login_05_invalidPasswordTest() {
	        LoginPage loginPage = new LoginPage(driver);

	        loginPage.login(ConfigReader.get("email"), ConfigReader.get("invalid_password"));

	        Assert.assertTrue(
	                loginPage.getIncorrectPasswordMsg().contains(ConfigReader.get("INCORRECT_PASSWORD_ERROR")),
	                "Incorrect password message mismatch!"
	        );
	    }
	    @Test(priority = 6)
	    public void TC_Login_6_passwordEyeIconToggleTest() {
	        LoginPage login = new LoginPage(driver);
	        login.waitForPageReady();

	        Assert.assertTrue(login.isPasswordEyeDisplayed(), "Eye icon is not displayed");

	        String initialType = login.getPasswordFieldType();
	        Assert.assertEquals(initialType, "password", "Password should be hidden by default (type=password)");

	        // Click → should become visible (type=text)
	        login.clickPasswordEyeIcon();
	        String afterFirstClick = login.getPasswordFieldType();
	        Assert.assertEquals(afterFirstClick, "text", "Password should be visible after clicking eye icon (type=text)");

	        // Click again → should become hidden back (type=password)
	        login.clickPasswordEyeIcon();
	        String afterSecondClick = login.getPasswordFieldType();
	        Assert.assertEquals(afterSecondClick, "password", "Password should be hidden again after second click (type=password)");
	    }

	    @Test(priority = 7)
	    public void TC_Login_07_keepMeLoggedInCheckboxTest() {
	        LoginPage loginPage = new LoginPage(driver);

	        Assert.assertTrue(loginPage.isRememberMeDisplayed(), "RememberMe checkbox not displayed");
	        Assert.assertTrue(loginPage.isRememberMeEnabled(), "RememberMe checkbox should be enabled");
	        Assert.assertTrue(loginPage.isRememberMeSelected(), "RememberMe checkbox should be selected by default");
	    }

	    @Test(priority = 8)
	    public void TC_Login_08_forgotPasswordLinkVisibleTest() {
	        LoginPage loginPage = new LoginPage(driver);

	        Assert.assertTrue(loginPage.isForgotPasswordLinkDisplayed(), "Forgot password link not displayed");
	        Assert.assertTrue(loginPage.isForgotPasswordLinkEnabled(), "Forgot password link not enabled");
	    }

	    @Test(priority = 9)
	    public void TC_Login_09_openForgotPasswordModalTest() {
	        LoginPage loginPage = new LoginPage(driver);

	        loginPage.openForgotPasswordModal();
	        Assert.assertTrue(loginPage.isForgotPasswordHeadingDisplayed(), "Forgot password modal heading not displayed");
	    }

	    @Test(priority = 10)
	    public void TC_Login_10_emptyForgotPasswordEmailValidation() {
	        LoginPage loginPage = new LoginPage(driver);

	        loginPage.submitForgotPassword("");

	        Assert.assertTrue(
	                loginPage.getForgotEmailError().contains(ConfigReader.get("required_forgetPassword_email_error")),
	                "Forgot email required error mismatch!"
	        );
	    }

	    @Test(priority = 11)
	    public void TC_Login_11_invalidForgotPasswordEmailValidation() {
	        LoginPage loginPage = new LoginPage(driver);

	        loginPage.submitForgotPassword(ConfigReader.get("invalid_email"));

	        Assert.assertTrue(
	                normalize(loginPage.getForgotEmailError()).contains(normalize(ConfigReader.get("invalid_forgetPassword_email_error"))),
	                "Forgot email invalid error mismatch!"
	        );
	    }

	    @Test(priority = 12)
	    public void TC_Login_12_unregisteredEmailValidation() {
	        LoginPage loginPage = new LoginPage(driver);

	        loginPage.submitForgotPassword(ConfigReader.get("invalid_register_email"));

	        Assert.assertTrue(
	                loginPage.getUnregisteredAccountError().contains(ConfigReader.get("invalid_Register_email_error")),
	                "Unregistered email error mismatch!"
	        );
	    }

	    @Test(priority = 13)
	    public void TC_Login_13_forgotPasswordSuccessMsgTest() {
	        LoginPage loginPage = new LoginPage(driver);

	        loginPage.submitForgotPassword(ConfigReader.get("email"));

	        Assert.assertTrue(
	                loginPage.getForgotSuccessMsg().contains(ConfigReader.get("forget_password_success_Msg")),
	                "Forgot password success message mismatch!"
	        );
	        driver.navigate().refresh();
	    }


	    @Test(priority = 14, groups = "loginSuccess")
	    public void TC_Login_14_verifyValidLogin() {
	    	LoginPage loginPage = new LoginPage(driver);
	        String email = ConfigReader.get("email");
	        String password = ConfigReader.get("password");

	        loginPage.login(email, password);

	        // Assertion: Check if home page URL is loaded
	        String expectedUrl = ConfigReader.get("homeUrl");
	        Assert.assertEquals(driver.getCurrentUrl(), expectedUrl, "Login failed or home page not loaded!");
	    }

	    private String normalize(String s) {
	        return s == null ? "" : s.replaceAll("\\s+", " ").trim();
	    }
}


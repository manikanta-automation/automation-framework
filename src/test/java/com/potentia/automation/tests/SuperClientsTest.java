package com.potentia.automation.tests;

import java.util.List;


import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.potentia.automation.core.BaseTest;
import com.potentia.automation.listeners.ExcelReportListener;
import com.potentia.automation.pages.LoginPage;
import com.potentia.automation.pages.SuperClientsPage;
import com.potentia.automation.utils.ConfigReader;
@Listeners(ExcelReportListener.class)
public class SuperClientsTest extends BaseTest {

    @BeforeClass  
    public void setupTest() {
        
        
        LoginPage loginPage = new LoginPage(driver);
        String email = ConfigReader.get("email");
        String password = ConfigReader.get("password");

        loginPage.login(email, password);

        // Assertion: Check if home page URL is loaded
        String expectedUrl = ConfigReader.get("homeUrl");
        Assert.assertEquals(driver.getCurrentUrl(), expectedUrl, "Login failed or home page not loaded!");
    }

    @Test(priority = 1)
    public void TC_SC_01_verifyFacilitatorLandsOnMySuperClientsPageAndVarifyMYSuperClientsHeadingDisplayed() {
    	SuperClientsPage superClientsPage = new SuperClientsPage(driver);
        Assert.assertTrue(
                superClientsPage.isMySuperClientsHeadingDisplayed(),
                "Facilitator did not land on My Super Clients page."
        );

        Assert.assertEquals(
                superClientsPage.getPageHeadingText(),
                "My Super Clients",
                "Page heading mismatch."
        );
    }

    @Test(priority = 2)
    public void TC_SC_02_verifySuperClientListDisplayed() {
    	SuperClientsPage superClientsPage = new SuperClientsPage(driver);
        Assert.assertTrue(
                superClientsPage.isSuperClientListDisplayed(),
                "Super client list is not displayed."
        );
        superClientsPage.printAllSuperClients();
    }
    @Test(priority = 3)
    public void TC_SC_03_verifyEachSuperClientNameIsClickable() {
    	SuperClientsPage superClientsPage = new SuperClientsPage(driver);
        Assert.assertTrue(
                superClientsPage.areAllSuperClientLinksClickable(),
                "One or more super client links are not clickable."
        );
    }

    @Test(priority = 4)
    public void TC_SC_04_verifySuperClientCountGreaterThanZero() {
    	SuperClientsPage superClientsPage = new SuperClientsPage(driver);
        int count = superClientsPage.getSuperClientCount();
        Assert.assertTrue(count > 0, "No super clients are displayed for the facilitator.");
    }

    @Test(priority = 5)
    public void TC_SC_05_verifySuperClientNamesAreNotBlank() {
    	SuperClientsPage superClientsPage = new SuperClientsPage(driver);
        Assert.assertTrue(
                superClientsPage.areAllSuperClientNamesNonEmpty(),
                "One or more super client names are blank."
        );
    }

    @Test(priority = 6)
    public void TC_SC_06_verifySuperClientLinksHaveHref() {
    	SuperClientsPage superClientsPage = new SuperClientsPage(driver);
        Assert.assertTrue(
                superClientsPage.areAllSuperClientLinksHavingHref(),
                "One or more super client links do not have valid href."
        );
    }

    @Test(priority = 7)
    public void TC_SC_07_verifyProfileDropdownVisible() {
    	SuperClientsPage superClientsPage = new SuperClientsPage(driver);
        Assert.assertTrue(
                superClientsPage.isProfileDropdownVisible(),
                "Profile dropdown is not visible."
        );
    }

    @Test(priority = 8)
    public void TC_SC_08_verifyProfileDropdownOptions() {
    	SuperClientsPage superClientsPage = new SuperClientsPage(driver);
        superClientsPage.openProfileDropdown();

        Assert.assertTrue(superClientsPage.isProfileOptionDisplayed(), "Profile option is not displayed.");
        Assert.assertTrue(superClientsPage.isMySuperClientsOptionDisplayed(), "My Super Clients option is not displayed.");
        Assert.assertTrue(superClientsPage.isChangePasswordOptionDisplayed(), "Change Password option is not displayed.");
        Assert.assertTrue(superClientsPage.isSignOutOptionDisplayed(), "Sign out option is not displayed.");
    }
    
    @Test(priority = 9)
    public void TC_SC_09_verifyCurrentAccountNameDisplayed() {
    	SuperClientsPage superClientsPage = new SuperClientsPage(driver);

        String actualAccountName = superClientsPage.getCurrentAccountName();
        Assert.assertFalse(actualAccountName.isBlank(), "Current account name is blank.");
    }

    @Test(priority = 10)
    public void TC_SC_10_verifyLoggedInUserNameDisplayed() {
    	SuperClientsPage superClientsPage = new SuperClientsPage(driver);

        String actualUserName = superClientsPage.getLoggedInUserName();
        Assert.assertFalse(actualUserName.isBlank(), "Logged in user name is blank.");
    }   
 
    @Test(priority = 11)
    public void TC_SC_11_verifyHeaderLogoDisplayed() {
    	SuperClientsPage superClientsPage = new SuperClientsPage(driver);
    	Assert.assertTrue(superClientsPage.isHeaderLogoDisplayed(), "Header logo is not displayed.");
    }

    @Test(priority = 12)
    public void TC_SC_12_verifyFooterLogoDisplayed() {
    	SuperClientsPage superClientsPage = new SuperClientsPage(driver);
        Assert.assertTrue(
                superClientsPage.isFooterLogoDisplayed(),
                "Footer logo is not displayed."
        );
    }
    @Test(priority = 13)
    public void TC_SC_13_clickSuperClientByName() {
    	SuperClientsPage superClientsPage = new SuperClientsPage(driver);
        List<String> clients = superClientsPage.getAllSuperClientNames();
        Assert.assertTrue(clients.size() > 0, "No super clients available to click.");

        String clientToOpen = clients.get(0);
        superClientsPage.clickSuperClientByName(clientToOpen);

        Assert.assertTrue(
                superClientsPage.isCurrentUrlContains("/batchlist/"),
                "User was not redirected to batch list page after clicking super client: " + clientToOpen
        );
    }

}

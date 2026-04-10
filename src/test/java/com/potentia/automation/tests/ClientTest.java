package com.potentia.automation.tests;

import java.nio.file.Paths;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.potentia.automation.core.BaseTest;
import com.potentia.automation.pages.ClientPage;
import com.potentia.automation.pages.LoginPage;
import com.potentia.automation.pages.SuperClientsPage;
import com.potentia.automation.utils.ConfigReader;






public class ClientTest extends BaseTest {

  @BeforeClass  
  public void setupTest() {
      
      
      LoginPage loginPage = new LoginPage(driver);
      String email = ConfigReader.get("email");
      String password = ConfigReader.get("password");

      loginPage.login(email, password);

      // Assertion: Check if home page URL is loaded
      String expectedUrl = ConfigReader.get("homeUrl");
      Assert.assertEquals(driver.getCurrentUrl(), expectedUrl, "Login failed or home page not loaded!");
      SuperClientsPage superClientsPage = new SuperClientsPage(driver);
      List<String> clients = superClientsPage.getAllSuperClientNames();
      Assert.assertTrue(clients.size() > 0, "No super clients available to click.");

      String clientToOpen = clients.get(1);
      superClientsPage.clickSuperClientByName(clientToOpen);

      Assert.assertTrue(
              superClientsPage.isCurrentUrlContains("/batchlist/"),
              "User was not redirected to batch list page after clicking super client: " + clientToOpen
      );
  }

    @Test(priority = 1)
    public void TC_CL_01_verifyNavigationToClientsPage() {
    	ClientPage clientPage = new ClientPage(driver);
    	clientPage.clickClientsFromMasters();

        // Validation
        Assert.assertTrue(
                driver.getCurrentUrl().contains("clientlist"),
                "Clients page is not opened"
        );
        Assert.assertTrue(clientPage.isClientListHeadingDisplayed(), "Client List heading is not displayed.");
        Assert.assertEquals(clientPage.getClientListHeadingText(), "Client List", "Client List page heading mismatch.");
        Assert.assertTrue(driver.getCurrentUrl().contains("clientlist"), "Clients page is not opened.");
    }

    @Test(priority = 2)
    public void TC_CL_02_verifyClientsPageUIElements() {
    	ClientPage clientPage = new ClientPage(driver);
        Assert.assertTrue(clientPage.isAddClientButtonDisplayed(), "Add Client button is not displayed.");
        Assert.assertTrue(clientPage.isAddClientButtonEnabled(), "Add Client button is not enabled.");
        Assert.assertTrue(clientPage.isSearchBoxDisplayed(), "Search box is not displayed.");
        Assert.assertTrue(clientPage.isRowsDropdownDisplayed(), "Rows dropdown is not displayed.");
        Assert.assertTrue(clientPage.isClientTableDisplayed(), "Client table is not displayed.");

        Assert.assertTrue(clientPage.getTableHeaderTexts().contains("Name"), "Name column header missing.");
        Assert.assertTrue(clientPage.getTableHeaderTexts().contains("Industry"), "Industry column header missing.");
        Assert.assertTrue(clientPage.getTableHeaderTexts().contains("Created Date"), "Created Date column header missing.");
        Assert.assertTrue(clientPage.getTableHeaderTexts().contains("Status"), "Status column header missing.");
        Assert.assertTrue(clientPage.getTableHeaderTexts().contains("Actions"), "Actions column header missing.");
    }

    @Test(priority = 3)
    public void TC_CL_03_verifyAddClientPopupUI() {
    	ClientPage clientPage = new ClientPage(driver);
        clientPage.clickAddClient();

        Assert.assertTrue(clientPage.isAddClientPopupDisplayed(), "Add Client popup is not displayed.");
        Assert.assertEquals(clientPage.getAddClientPopupTitle(), "Add Client", "Popup title mismatch.");
        Assert.assertTrue(clientPage.isNameFieldDisplayed(), "Name field is not displayed.");
        Assert.assertTrue(clientPage.isIndustryFieldDisplayed(), "Industry field is not displayed.");
        Assert.assertTrue(clientPage.isSaveButtonDisplayed(), "Save button is not displayed.");
        Assert.assertTrue(clientPage.isCancelButtonDisplayed(), "Cancel button is not displayed.");

        clientPage.clickCancel();
    }

    @Test(priority = 4)
    public void TC_CL_04_verifyMandatoryValidation() {
    	ClientPage clientPage = new ClientPage(driver);
        clientPage.clickAddClient();
        clientPage.clickSave();

        Assert.assertEquals(clientPage.getValidationErrorCount(), 2, "Required field validation count mismatch.");
        Assert.assertEquals(clientPage.getValidationErrorText(0), "* This field is required", "Validation message mismatch.");
        Assert.assertEquals(clientPage.getValidationErrorText(1), "* This field is required", "Validation message mismatch.");

        clientPage.clickCancel();
    }

    @Test(priority = 5)
    public void TC_CL_05_verifyCancelButtonClosesPopup() {
    	ClientPage clientPage = new ClientPage(driver);
        clientPage.clickAddClient();
        clientPage.enterName("Temp Client");
        clientPage.enterIndustry("Temp Industry");
        clientPage.clickCancel();

        Assert.assertTrue(clientPage.isAddClientButtonDisplayed(), "Popup did not close after clicking Cancel.");
    }

    @Test(priority = 6)
    public void TC_CL_06_verifyCloseIconClosesPopup() {
    	ClientPage clientPage = new ClientPage(driver);
        clientPage.clickAddClient();
        clientPage.closePopupUsingX();

        Assert.assertTrue(clientPage.isAddClientButtonDisplayed(), "Popup did not close after clicking Close icon.");
    }

    @Test(priority = 7)
    public void TC_CL_07_verifyNameMaxLengthValidation() {
    		ClientPage clientPage = new ClientPage(driver);
    		clientPage.clickAddClient();

    	    String longName = "A".repeat(151);
    	    clientPage.enterName(longName);
    	    clientPage.enterIndustry("Automation");
    	    clientPage.clickSave();

    	    String fieldError = clientPage.getValidationErrorText(0);
    	    String alertError = clientPage.getAlertMessageText();

    	    boolean isErrorDisplayed =
    	            fieldError.contains("max 150 characters allowed")
    	            || alertError.contains("max 150 characters allowed");

    	    // ✅ Step 1: Fail if NO error displayed
    	    Assert.assertTrue(
    	            !fieldError.isEmpty() || !alertError.isEmpty(),
    	            "BUG: Client name is allowing more than 150 characters (No validation message displayed)"
    	    );

    	    // ✅ Step 2: Validate correct error message
    	    Assert.assertTrue(
    	            isErrorDisplayed,
    	            "Incorrect validation message. FieldError: [" + fieldError +
    	            "] AlertError: [" + alertError + "]"
    	    );


    }

    @Test(priority = 8)
    public void TC_CL_08_verifyIndustryMaxLengthValidation() {
    	ClientPage clientPage = new ClientPage(driver);
        clientPage.clickAddClient();

        String longIndustry = "B".repeat(151);
        clientPage.enterName("Industry Validation Client");
        clientPage.enterIndustry(longIndustry);
        clientPage.clickSave();

        Assert.assertTrue(
                clientPage.getValidationErrorText(0).contains("max 150 characters allowed")
                        || clientPage.getAlertMessageText().contains("max 150 characters allowed"),
                "Industry max length validation message is not displayed."
        );


    }

    @Test(priority = 9)
    public void TC_CL_09_verifyInvalidLogoUploadValidation() {
    	ClientPage clientPage = new ClientPage(driver);
    	String fileName = "1730973498_1991-HBR-TeachingPeople-HowtoLearn-chris_argyris.pdf";
    	clientPage.clickAddClient();
	    String filePath = Paths.get(
	            System.getProperty("user.dir"),
	            "src", "test", "resources",
	            "testdata",
	            fileName
	    ).toString();

	    clientPage.uploadFile(filePath);
	    clientPage.waitUntilLogoFileIsSelected(fileName);
	   
	    clientPage.clickSave();
	    Assert.assertTrue(
	    		clientPage.isLogoFileTypeErrorDisplayed(),
	            "Invalid file type error is not displayed"
	    );

	    // Verify error message
	    Assert.assertEquals(
	    		clientPage.getLogoFileTypeErrorText(),
	            "Sorry we do not recognize this file type. Please upload png, jpg, gif file formats only."
	    );
	    // Click remove (X)
    	clientPage.clickRemoveLogo();
    	clientPage.clickOkBtn();
        // Verify logo is removed
    	Assert.assertTrue(clientPage.isLogoDeleteSuccessMsgDisplayed(), "Logo is not deleted");
        Assert.assertTrue(
        		clientPage.isLogoCleared(),
                "Uploaded logo was not removed"
        );
        clientPage.clickCancel();
    }

    @Test(priority = 10)
    public void TC_CL_10_verifyAddClientSuccessfully() {
    	ClientPage clientPage = new ClientPage(driver);
    	driver.navigate().refresh();
        clientPage.clickAddClient();
        clientPage.enterName("createdClientName");
        clientPage.enterIndustry("createdClientIndustry");
        clientPage.clickSave();

        String alertText = clientPage.getAlertMessageText();
        Assert.assertEquals(alertText, "Client has been added successfully.", "Client success message mismatch.");
        clientPage.waitForAlertToDisappear();

        Assert.assertTrue(clientPage.isClientPresent("createdClientName"), "Newly created client is not present in table.");
        Assert.assertEquals(clientPage.getClientIndustryFromRow("createdClientName"), "createdClientIndustry", "Industry value mismatch.");
    }

    @Test(priority = 11)
    public void TC_CL_11_verifyDuplicateClientValidation() {
    	ClientPage clientPage = new ClientPage(driver);
        clientPage.clickAddClient();
        clientPage.enterName("createdClientName");
        clientPage.enterIndustry("createdClientIndustry");
        clientPage.clickSave();

        String message = clientPage.getAlertMessageText();
        Assert.assertTrue(
                message.contains("already exists"),
                "Duplicate client validation message is not displayed."
        );

    }

    @Test(priority = 12)
    public void TC_CL_12_verifyEditClientAndUpdateDetails() throws InterruptedException {
    	ClientPage clientPage = new ClientPage(driver);
        clientPage.clickEditForClient("createdClientName");
        Thread.sleep(2000);
        clientPage.enterName("Test client vconnect");
        clientPage.clickUpdateBtn();

        String alertText = clientPage.getAlertMessageText();
        Assert.assertTrue(
                alertText.contains("updated successfully") || alertText.contains("Client has been updated"),
                "Client update success message is not displayed."
        );
        clientPage.waitForAlertToDisappear();

        Assert.assertTrue(clientPage.isClientPresent("Test client vconnect"), "Updated client name is not present in table.");
    }

    @Test(priority = 13)
    public void TC_CL_13_verifySearchForExistingClient() {
    	ClientPage clientPage = new ClientPage(driver);
    	driver.navigate().refresh();
        clientPage.searchClient("createdClientName");

        Assert.assertTrue(clientPage.isClientPresent("createdClientName"), "Search did not return the expected client.");

        clientPage.clearClientSearch();
    }

    @Test(priority = 14)
    public void TC_CL_14_verifySearchForNonExistingClient() {
    	ClientPage clientPage = new ClientPage(driver);
        clientPage.searchClient("Non Existing Client 123456789");

        Assert.assertTrue(clientPage.isNoDataMessageDisplayed(), "No data message is not displayed for non-existing search.");

        clientPage.clearClientSearch();
        driver.navigate().refresh();
    }

    @Test(priority = 15)
    public void TC_CL_15_verifyShowRowsFunctionality() {
    	ClientPage clientPage = new ClientPage(driver);
        clientPage.selectRowsCount("10");
        int rowCount10 = clientPage.getVisibleRowCount();
        Assert.assertTrue(rowCount10 <= 10, "Visible row count is greater than selected rows count 10.");

        clientPage.selectRowsCount("25");
        int rowCount25 = clientPage.getVisibleRowCount();
        Assert.assertTrue(rowCount25 <= 25, "Visible row count is greater than selected rows count 25.");
    }

    @Test(priority = 16)
    public void TC_CL_16_verifyActiveClientFilter() {
    	ClientPage clientPage = new ClientPage(driver);
        clientPage.selectActiveFilter();

        Assert.assertTrue(clientPage.verifyAllStatusAs("Active"), "Active filter is not working correctly.");

        clientPage.clearFilter();
    }

    @Test(priority = 17)
    public void TC_CL_17_verifyInactiveClientFilter() {
    	ClientPage clientPage = new ClientPage(driver);
        clientPage.selectInactiveFilter();

        Assert.assertTrue(clientPage.verifyAllStatusAs("Inactive"), "Inactive filter is not working correctly.");

        clientPage.clearFilter();
    }

    @Test(priority = 18)
    public void TC_CL_18_verifyClearFilter() {
    	ClientPage clientPage = new ClientPage(driver);
        clientPage.selectActiveFilter();
        clientPage.clearFilter();

        Assert.assertTrue(
                clientPage.verifyMixedStatusesPresent() || clientPage.getEntriesInfoText().contains("entries"),
                "Clear filter is not working correctly."
        );
    }

    @Test(priority = 19)
    public void TC_CL_19_verifyInactivateClient() {
    	ClientPage clientPage = new ClientPage(driver);
    	String clientName = "ABC Program Client";

        clientPage.ensureClientIsActive(clientName);
        

        clientPage.clickInactivateForClient(clientName);
        clientPage.clickOkBtn();

        Assert.assertTrue(clientPage.isInactiveClientSuccessMsgDisplayed(),
                "Inactive success message is not displayed.");

        clientPage.waitForAlertToDisappear();

        Assert.assertEquals(clientPage.getClientStatusFromRow(clientName),
                "Inactive",
                "Client status is not updated to Inactive.");
    }

    @Test(priority = 20)
    public void TC_CL_20_verifyDeleteClient() {
    	ClientPage clientPage = new ClientPage(driver);
    	String clientName = "ABC Program Client";

        clientPage.clickDeleteForClient(clientName);
        clientPage.clickOkBtn();

        String deleteSuccessMsg = clientPage.getDeleteSuccessMessageText();
        String deleteBlockedMsg = clientPage.getDeleteBlockedMessageText();

        if (!deleteSuccessMsg.isEmpty()) {
            Assert.assertTrue(
                    deleteSuccessMsg.contains("deleted successfully"),
                    "Delete success message is incorrect: " + deleteSuccessMsg
            );

            Assert.assertFalse(
                    clientPage.isClientPresent(clientName),
                    "Client is still present even after successful delete."
            );

        } else if (!deleteBlockedMsg.isEmpty()) {
            Assert.assertEquals(
                    deleteBlockedMsg,
                    "Selected Client(s) cannot be deleted as it is mapped 1 or more batches",
                    "Mapped client delete error message mismatch."
            );

            Assert.assertTrue(
                    clientPage.isClientPresent(clientName),
                    "Mapped client should not be deleted, but it is missing from table."
            );

        } else {
            Assert.fail("Neither delete success message nor mapped-client error message was displayed.");
        }
    }
}

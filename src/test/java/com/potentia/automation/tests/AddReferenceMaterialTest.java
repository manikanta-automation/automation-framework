package com.potentia.automation.tests;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.potentia.automation.core.BaseTest;
import com.potentia.automation.listeners.ExcelReportListener;
import com.potentia.automation.pages.AddReferenceMaterialPage;
import com.potentia.automation.pages.LoginPage;
import com.potentia.automation.pages.SuperClientsPage;
import com.potentia.automation.utils.ConfigReader;
@Listeners(ExcelReportListener.class)
public class AddReferenceMaterialTest extends BaseTest {
	@BeforeClass
    public void pageSetup() {
    	LoginPage loginPage = new LoginPage(driver);
        String email = ConfigReader.get("email");
        String password = ConfigReader.get("password");

        loginPage.login(email, password);

        // Assertion: Check if home page URL is loaded
        String expectedUrl = ConfigReader.get("homeUrl");
        Assert.assertEquals(driver.getCurrentUrl(), expectedUrl, "Login failed or home page not loaded!");
    }
	@Test(priority = 1)
    public void TC_SC_1_clickSuperClientByName() {
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
	@Test(priority = 2)
    public void TC_MB_clickBatchByNameAcrossTabs() {
        String batchName = "Auto Batch 1775109143724"; // take from config/testdata if needed

        AddReferenceMaterialPage referenceMaterialPage = new AddReferenceMaterialPage(driver);

        boolean isClicked = referenceMaterialPage.clickBatchByNameAcrossTabs(batchName);

        Assert.assertTrue(
            isClicked,
            "Batch was not found in Active, Planned, or Inactive tabs: " + batchName
        );
    }
	 @Test(priority = 3)
	 public void TC_003_clickAddActivityBtn() {
		 AddReferenceMaterialPage referenceMaterialPage = new AddReferenceMaterialPage(driver);
		 referenceMaterialPage.addActivityBtn();
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
         WebElement myActivityHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(), 'Add Activity')]")));
         String expectedHeading = "Add Activity";
         String actualHeading = myActivityHeading.getText().trim();
         if(expectedHeading.equals(actualHeading)) {
        	 System.out.println("Page landed on Add Activity Screen");
         } else {
        	 Assert.fail("Not Landed on expected add activity screen");
         }
	 }
	 @Test(priority = 4)
	 public void TC_004_clickActivityName() throws InterruptedException {
		 AddReferenceMaterialPage referenceMaterialPage = new AddReferenceMaterialPage(driver);
		 WebElement referenceMaterialButton = driver.findElement(By.xpath("//label[span[text()='Reference Material']]"));
		 Actions actions = new Actions(driver);
		 actions.moveToElement(referenceMaterialButton).perform();
		 Thread.sleep(5000);
		 WebElement tooltip = driver.findElement(By.xpath("//*[contains(text(), 'Use this activity to share content')]"));
		 //Assert.assertTrue(tooltip.isDisplayed(), "Tooltip not visible on mouse hover.");
		 
		 referenceMaterialPage.selectActivity();
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
         WebElement myActivityHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(), 'Add Activity')]")));
         String expectedHeading = "Add Activity - Reference Material";
         String actualHeading = myActivityHeading.getText().trim();
         System.out.println(actualHeading);
         if(expectedHeading.equals(actualHeading)) {
        	 System.out.println("Page landed on Add Activity - Reference Material Screen");
         } else {
        	 Assert.fail("Not Landed on expected add activity - Reference Material screen");
         }
	 }
	 @Test(priority = 5)
	 public void TC_005_validateEmptyAssignmentActivityTitle () {
		 AddReferenceMaterialPage referenceMaterialPage = new AddReferenceMaterialPage(driver);
		 referenceMaterialPage.addActivityTitle("");
		 referenceMaterialPage.clickReferenceMaterialSaveAndContinue();
		 String expectedError = "Please enter activity title";
		 if (referenceMaterialPage.getActivityTitle().isEmpty()) {
			 if (referenceMaterialPage.isActivityTitleErrorMessageDisplayed()) {
				 String actualError = referenceMaterialPage.getActivityTitleErrorMessageText();
				 Assert.assertEquals(actualError, expectedError,
		                "Mismatch in error message when activity title is empty.");
				 System.out.println("Error message displayed correctly: " + actualError);
			 } else {
				 Assert.fail("Error message was not displayed when activity title was left empty.");
			 }
		 } else {
			 Assert.fail("Activity title field is not empty, test setup failed.");
		 }
	}
	 @Test(priority = 6)
		public void TC_006_validateAssignmentActivityTitleLength () {
		 AddReferenceMaterialPage referenceMaterialPage = new AddReferenceMaterialPage(driver);
		 String overLimitTitle = referenceMaterialPage.generateText(151);
		 	referenceMaterialPage.addActivityTitle(overLimitTitle);
		 	referenceMaterialPage.clickReferenceMaterialSaveAndContinue();
			String expectedError = "Activity title is too long, max 150 characters allowed";

			// Assert error message is displayed
			Assert.assertTrue(
					referenceMaterialPage.isActivityTitleLengthErrorMessageDisplayed(),
			    "Error message for long activity title is not displayed."
			);

			// Get the actual error and assert it's correct
			String actualError = referenceMaterialPage.getActivityTitleLengthErrorMessageText();
			Assert.assertEquals(
			    actualError,
			    expectedError,
			    "Mismatch in activity title length error message."
			);

			System.out.println("Correct error shown when title exceeds character limit: " + actualError);
		}
	 @Test(priority = 7)
		public void TC_007_validateActivityInstructionsLength () {
		 AddReferenceMaterialPage referenceMaterialPage = new AddReferenceMaterialPage(driver);
			 referenceMaterialPage.clearActivityTitle();
			 referenceMaterialPage.addActivityTitle("Refe - 1");
			 referenceMaterialPage.clickActivityDiscriptionDropDownArrow();
			 referenceMaterialPage.addActivityDiscription("Add Description");
			 referenceMaterialPage.addActivityInstruction("Add Instruction");
			 referenceMaterialPage.clickReferenceMaterialSaveAndContinue();
		}
	 @Test(priority = 8)
	    public void TC_8_verifyDueDateFieldVisibleAndEnabled() {
		 AddReferenceMaterialPage referenceMaterialPage = new AddReferenceMaterialPage(driver);
	    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    	wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("act_endDate")));
	        Assert.assertTrue(referenceMaterialPage.isDueDateFieldDisplayed(), "Due Date field is not visible");
	        Assert.assertTrue(referenceMaterialPage.isDueDateFieldEnabled(), "Due Date field is not enabled");
	    }
	 @Test(priority = 9)
	    public void TC_9_verifySuperActivitiesAvailable() {
		 AddReferenceMaterialPage referenceMaterialPage = new AddReferenceMaterialPage(driver);
	        String result = referenceMaterialPage.printSuperActivities();
	        System.out.println(result);

	        // Assertion
	        if (result.contains("No super activities are available")) {
	            Assert.assertTrue(result.contains("No super activities are available"));
	        } else {
	            Assert.assertTrue(result.contains("Available Super Activities"));
	        }
	    }
	 @Test(priority = 10)
	    public void TC_10_varifyAllowResponseAfterDeadlineToolTip() throws InterruptedException {
	    	WebElement allowResponseAfterDeadlineToolTip = driver.findElement(By.xpath(
	    	        "//label[contains(normalize-space(), 'Allow responses after deadline')]" +
	    	                "//em[contains(@data-bs-original-title, 'Allow pax to submit the activity after deadline')]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(allowResponseAfterDeadlineToolTip).perform();
			Thread.sleep(5000);
			WebElement tooltip = driver.findElement(By.xpath("//*[contains(text(), 'Allow pax to submit the activity after deadline')]"));
			Assert.assertTrue(tooltip.isDisplayed(), "Tooltip not visible on mouse hover.");
	    }
	    @Test(priority = 11)
	    public void TC_11_checkboxDefaultStateForAllowResponseAfterDeadLine() {
	    	AddReferenceMaterialPage referenceMaterialPage = new AddReferenceMaterialPage(driver);
	        Assert.assertFalse(referenceMaterialPage.isAllowResponseAfterDeadlineCheckboxSelected(), "Checkbox should be unselected by default");
	    }

	    @Test(priority = 12)
	    public void TC_12_checkboxToggleViaAllowResponseAfterDeadLineCheckboxClick() {
	    	AddReferenceMaterialPage referenceMaterialPage = new AddReferenceMaterialPage(driver);
	    	referenceMaterialPage.clickAllowResponseAfterDeadlineCheckbox();
	        Assert.assertTrue(referenceMaterialPage.isAllowResponseAfterDeadlineCheckboxSelected(), "Checkbox should be selected after click");

	        referenceMaterialPage.clickAllowResponseAfterDeadlineCheckbox();
	        Assert.assertFalse(referenceMaterialPage.isAllowResponseAfterDeadlineCheckboxSelected(), "Checkbox should be unselected after second click");
	    }

	    @Test(priority = 13)
	    public void TC_13_checkboxToggleViaAllowResponseAfterDeadLineLabelClick() {
	    	AddReferenceMaterialPage referenceMaterialPage = new AddReferenceMaterialPage(driver);
	    	referenceMaterialPage.clickResponseAfterDeadlineLabel();
	        Assert.assertTrue(referenceMaterialPage.isAllowResponseAfterDeadlineCheckboxSelected(), "Checkbox should be selected by label click");

	        referenceMaterialPage.clickResponseAfterDeadlineLabel();
	        Assert.assertFalse(referenceMaterialPage.isAllowResponseAfterDeadlineCheckboxSelected(), "Checkbox should be unselected by label click again");
	    }
	    @Test(priority = 14)
	    public void TC_14_varifyReadOnlyToolTip() throws InterruptedException {
	    	WebElement readOnlyToolTip = driver.findElement(By.xpath(
	    	        "//label[contains(normalize-space(), 'Read only')]" +
	    	                "//em[contains(@data-bs-original-title, 'Read-only access for reference material.')]"));
			Actions actions = new Actions(driver);
			actions.moveToElement(readOnlyToolTip).perform();
			Thread.sleep(5000);
			WebElement tooltip = driver.findElement(By.xpath("//*[contains(text(), 'Read-only access for reference material.')]"));
			Assert.assertTrue(tooltip.isDisplayed(), "Tooltip not visible on mouse hover.");
	    }
	    @Test(priority = 15)
	    public void TC_15_checkboxDefaultStateForReadOnly() {
	    	AddReferenceMaterialPage referenceMaterialPage = new AddReferenceMaterialPage(driver);
	        Assert.assertFalse(referenceMaterialPage.isReadOnlyCheckboxSelected(), "Checkbox should be unselected by default");
	    }
	    @Test(priority = 16)
	    public void TC_16_checkboxToggleViaReadOnlyCheckboxClick() {
	    	AddReferenceMaterialPage referenceMaterialPage = new AddReferenceMaterialPage(driver);
	    	referenceMaterialPage.clickReadOnlyCheckbox();
	        Assert.assertTrue(referenceMaterialPage.isReadOnlyCheckboxSelected(), "Checkbox should be selected after click");

	        referenceMaterialPage.clickReadOnlyCheckbox();
	        Assert.assertFalse(referenceMaterialPage.isReadOnlyCheckboxSelected(), "Checkbox should be unselected after second click");
	    }

	    @Test(priority = 17)
	    public void TC_17_checkboxToggleViaReadOnlyLabelClick() {
	    	AddReferenceMaterialPage referenceMaterialPage = new AddReferenceMaterialPage(driver);
	    	referenceMaterialPage.clickReadOnlyLabel();
	        Assert.assertTrue(referenceMaterialPage.isReadOnlyCheckboxSelected(), "Checkbox should be selected by label click");

	        referenceMaterialPage.clickReadOnlyLabel();
	        Assert.assertFalse(referenceMaterialPage.isReadOnlyCheckboxSelected(), "Checkbox should be unselected by label click again");
	    }
	    @Test(priority = 18)
		 public void TC_uploadSingleFile() {
	    	AddReferenceMaterialPage referenceMaterialPage = new AddReferenceMaterialPage(driver);
			 String fileName = "1765879784_1730973498_1991-HBR-TeachingPeople-HowtoLearn-chris_argyris.pdf";

			    String filePath = Paths.get(
			            System.getProperty("user.dir"),
			            "src", "main", "resources",
			            "testdata",
			            fileName
			    ).toString();

			    referenceMaterialPage.uploadFile(filePath);

			    Assert.assertTrue(
			    		referenceMaterialPage.isFileUploaded(fileName),
			            "File was not uploaded successfully"
			    );
		 }
}

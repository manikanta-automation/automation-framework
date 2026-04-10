package com.potentia.automation.tests;


import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.potentia.automation.core.BaseTest;
import com.potentia.automation.pages.AddMagicWheelOneWheelPage;
import com.potentia.automation.pages.AddReferenceMaterialPage;
import com.potentia.automation.pages.LoginPage;
import com.potentia.automation.pages.SuperClientsPage;
import com.potentia.automation.utils.ConfigReader;

public class MagicWheelOneWheelTest extends BaseTest{
	
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

        AddMagicWheelOneWheelPage addMagicWheelOneWheelPage = new AddMagicWheelOneWheelPage(driver);

        boolean isClicked = addMagicWheelOneWheelPage.clickBatchByNameAcrossTabs(batchName);

        Assert.assertTrue(
            isClicked,
            "Batch was not found in Active, Planned, or Inactive tabs: " + batchName
        );
    }
	 @Test(priority = 3)
	 public void TC_003_clickAddActivityBtn() {
		 AddMagicWheelOneWheelPage addMagicWheelOneWheelPage = new AddMagicWheelOneWheelPage(driver);
		 addMagicWheelOneWheelPage.addActivityBtn();
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
		 AddMagicWheelOneWheelPage addMagicWheelOneWheelPage = new AddMagicWheelOneWheelPage(driver);
		 WebElement assignmentButton = driver.findElement(By.xpath("//label[span[text()='Magic Wheel']]"));
		 Actions actions = new Actions(driver);
		 actions.moveToElement(assignmentButton).perform();
		 Thread.sleep(5000);
		 WebElement tooltip = driver.findElement(By.xpath("//*[contains(text(), 'A game where one or two spin wheel')]"));
		 //Assert.assertTrue(tooltip.isDisplayed(), "Tooltip not visible on mouse hover.");
		 
		 addMagicWheelOneWheelPage.selectActivity();
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
         WebElement myActivityHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(), 'Add Activity')]")));
         String expectedHeading = "Add Activity - Magic Wheel";
         String actualHeading = myActivityHeading.getText().trim();
         System.out.println(actualHeading);
         if(expectedHeading.equals(actualHeading)) {
        	 System.out.println("Page landed on Add Activity - Magic wheel Screen");
         } else {
        	 Assert.fail("Not Landed on expected add activity - Magic wheel screen");
         }
	 }
	 
	 @Test(priority = 5)
	 public void TC_005_validateEmptyAssignmentActivityTitle () {
		 AddMagicWheelOneWheelPage addMagicWheelOneWheelPage = new AddMagicWheelOneWheelPage(driver);
		 addMagicWheelOneWheelPage.addActivityTitle("");
		 addMagicWheelOneWheelPage.clickAssignmentSaveAndContinue();
		 String expectedError = "Please enter activity title";
		 if (addMagicWheelOneWheelPage.getActivityTitle().isEmpty()) {
			 if (addMagicWheelOneWheelPage.isActivityTitleErrorMessageDisplayed()) {
				 String actualError = addMagicWheelOneWheelPage.getActivityTitleErrorMessageText();
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
		 AddMagicWheelOneWheelPage addMagicWheelOneWheelPage = new AddMagicWheelOneWheelPage(driver);
		 String overLimitTitle = addMagicWheelOneWheelPage.generateText(151);
		 addMagicWheelOneWheelPage.addActivityTitle(overLimitTitle);
		 addMagicWheelOneWheelPage.clickAssignmentSaveAndContinue();
		String expectedError = "Activity title is too long, max 150 characters allowed";

		// Assert error message is displayed
		Assert.assertTrue(
				addMagicWheelOneWheelPage.isActivityTitleLengthErrorMessageDisplayed(),
		    "Error message for long activity title is not displayed."
		);

		// Get the actual error and assert it's correct
		String actualError = addMagicWheelOneWheelPage.getActivityTitleLengthErrorMessageText();
		Assert.assertEquals(
		    actualError,
		    expectedError,
		    "Mismatch in activity title length error message."
		);

		System.out.println("Correct error shown when title exceeds character limit: " + actualError);
	}
	@Test(priority = 7)
	public void TC_007_validateActivityInstructionsLength () {
		AddMagicWheelOneWheelPage addMagicWheelOneWheelPage = new AddMagicWheelOneWheelPage(driver);
		addMagicWheelOneWheelPage.clearActivityTitle();
		addMagicWheelOneWheelPage.addActivityTitle("Magic Wheel - one level checking batch");
		addMagicWheelOneWheelPage.clickActivityDiscriptionDropDownArrow();
		addMagicWheelOneWheelPage.addActivityDiscription("Add Description");
		addMagicWheelOneWheelPage.addActivityInstruction("Add Instructions");
	}
	@Test(priority = 8)
	public void TC_verifyMagicOneWheelButton() {
		AddMagicWheelOneWheelPage addMagicWheelOneWheelPage = new AddMagicWheelOneWheelPage(driver);
		addMagicWheelOneWheelPage.scrollToMagicOneWheelTab();

		    Assert.assertTrue(
		    		addMagicWheelOneWheelPage.isMagicOneWheelBtnPresent(),
		        "One Wheel tab is not present"
		    );

		    Assert.assertTrue(
		    		addMagicWheelOneWheelPage.isOneWheelTabActive(),
		        "One Wheel tab is not active"
		    );
	}
	 @Test(priority = 9)
	 public void TC_varifySegmentSetUpdropdown() {
		 AddMagicWheelOneWheelPage addMagicWheelOneWheelPage = new AddMagicWheelOneWheelPage(driver);
		 addMagicWheelOneWheelPage.scrollIntoSetmentSetUp();
		 String defaultValue = addMagicWheelOneWheelPage.getDefaultSelectedOption();
         Assert.assertEquals(defaultValue, "Label", "Default option is incorrect");
         addMagicWheelOneWheelPage.selectSegmentSetUp("Label + Description");
	        Assert.assertEquals(addMagicWheelOneWheelPage.getSelectedSegmentSetUp(), "Label + Description", "Failed to select 'connect@potentia.in'");
	 }
	 @Test(priority = 10)
	    public void TC_verifyLabelOnlyVisibility() {
		 AddMagicWheelOneWheelPage addMagicWheelOneWheelPage = new AddMagicWheelOneWheelPage(driver);

		 addMagicWheelOneWheelPage.selectSegmentSetUp("Label");

	        Assert.assertTrue(
	        		addMagicWheelOneWheelPage.isLabelInputVisible(),
	                "Label input should be visible when 'Label' is selected"
	        );

	        Assert.assertFalse(
	        		addMagicWheelOneWheelPage.isDescriptionInputVisible(),
	                "Description input should NOT be visible when 'Label' is selected"
	        );
	    }
	 @Test(priority = 11)
	    public void TC_verifyLabelAndDescriptionVisibility() {
		 AddMagicWheelOneWheelPage addMagicWheelOneWheelPage = new AddMagicWheelOneWheelPage(driver);
		 addMagicWheelOneWheelPage.selectSegmentSetUp("Label + Description");

	        Assert.assertTrue(
	        		addMagicWheelOneWheelPage.isLabelInputVisible(),
	                "Label input should be visible when 'Label + Description' is selected"
	        );

	        Assert.assertTrue(
	        		addMagicWheelOneWheelPage.isDescriptionInputVisible(),
	                "Description input should be visible when 'Label + Description' is selected"
	        );
	        addMagicWheelOneWheelPage.selectSegmentSetUp("Label");
	    }
	 /* =========================
     TEST 12: Section displayed
     ========================= */
	  @Test(priority = 12)
	  public void TC_verifySegmentColorsSectionDisplayed() {
		  AddMagicWheelOneWheelPage addMagicWheelOneWheelPage = new AddMagicWheelOneWheelPage(driver);
	      Assert.assertTrue(
	    		  addMagicWheelOneWheelPage.isSegmentColorsSectionDisplayed(),
	              "Segment Colors section is NOT displayed");
	  }
	
	  /* =====================================
	     TEST 13: All radios enabled
	     ===================================== */
	  @Test(priority = 13)
	  public void TC_verifyAllSegmentColorRadiosEnabled() {
		  AddMagicWheelOneWheelPage addMagicWheelOneWheelPage = new AddMagicWheelOneWheelPage(driver);
	      List<WebElement> radios = addMagicWheelOneWheelPage.getAllSegmentColorRadios();
	
	      Assert.assertTrue(radios.size() > 0, "No Segment Color radio buttons found");
	
	      for (int i = 0; i < radios.size(); i++) {
	          Assert.assertTrue(
	        		  addMagicWheelOneWheelPage.isRadioEnabled(i),
	                  "Radio button at index " + i + " is NOT enabled");
	      }
	  }
	
	  /* =====================================
	     TEST 14: All radios clickable/selectable
	     ===================================== */
	  @Test(priority = 14)
	  public void TC_verifyAllSegmentColorRadiosClickable() {
		  AddMagicWheelOneWheelPage addMagicWheelOneWheelPage = new AddMagicWheelOneWheelPage(driver);
	      List<WebElement> radios = addMagicWheelOneWheelPage.getAllSegmentColorRadios();
	      List<WebElement> labels = addMagicWheelOneWheelPage.getAllSegmentColorLabels();
	
	      for (int i = 0; i < radios.size(); i++) {
	
	    	  addMagicWheelOneWheelPage.clickRadio(i);
	
	          Assert.assertTrue(
	        		  addMagicWheelOneWheelPage.isRadioSelected(i),
	                  "Radio button not selectable: " + labels.get(i).getText());
	      }
	  }
	  @Test(priority = 15)
	  public void TC_verifySegmentColorReflectsOnAllFields() {
		  AddMagicWheelOneWheelPage addMagicWheelOneWheelPage = new AddMagicWheelOneWheelPage(driver);
	      // BEFORE selection
	      String labelBgBefore = addMagicWheelOneWheelPage.getLabelBgColor(0);
	      String labelTextBefore = addMagicWheelOneWheelPage.getLabelTextColor(0);
	      String bgPickerBefore = addMagicWheelOneWheelPage.getBgPickerValue(0);
	      String textPickerBefore = addMagicWheelOneWheelPage.getTextPickerValue(0);

	      // Select Segment Color → Grey+Blue
	      addMagicWheelOneWheelPage.clickRadio(2);

	      // AFTER selection
	      String labelBgAfter = addMagicWheelOneWheelPage.getLabelBgColor(0);
	      String labelTextAfter = addMagicWheelOneWheelPage.getLabelTextColor(0);
	      String bgPickerAfter = addMagicWheelOneWheelPage.getBgPickerValue(0);
	      String textPickerAfter = addMagicWheelOneWheelPage.getTextPickerValue(0);

	      // ✅ All should change
	      Assert.assertNotEquals(labelBgAfter, labelBgBefore,
	              "Label background color should change");

	      Assert.assertNotEquals(labelTextAfter, labelTextBefore,
	              "Label text color should change");

	      Assert.assertNotEquals(bgPickerAfter, bgPickerBefore,
	              "BG color picker value should change");

	      Assert.assertNotEquals(textPickerAfter, textPickerBefore,
	              "Text color picker value should change");
	  }
	  @Test(priority = 16)
	    public void verifyDefaultSegmentLabelsAndEnterText() {
		  AddMagicWheelOneWheelPage addMagicWheelOneWheelPage = new AddMagicWheelOneWheelPage(driver);
	        // Step 1: Scroll to Segment Description section
		  addMagicWheelOneWheelPage.scrollToSegmentSection();

	        // Step 2: Verify 3 label fields are visible
	        Assert.assertTrue(
	        		addMagicWheelOneWheelPage.areThreeLabelFieldsVisible(),
	                "Default 3 Segment label fields are NOT visible"
	        );

	        // Step 3: Enter text into label fields
	        addMagicWheelOneWheelPage.enterSegmentLabels(
	                Arrays.asList("Quality", "Communication", "Team Work")
	        );
	    }
	  @Test(priority = 17)
	    public void validateSegmentBgAndTextColors() {
		  AddMagicWheelOneWheelPage addMagicWheelOneWheelPage = new AddMagicWheelOneWheelPage(driver);
	        int segmentCount = addMagicWheelOneWheelPage.getSegmentCount();
	        Assert.assertEquals(segmentCount, 3, "Default segment count is not 3");

	        for (int i = 0; i < segmentCount; i++) {

	            // ===== BG COLOR VALIDATION =====
	            String expectedBgHex = addMagicWheelOneWheelPage.getBgPickerValue(i);
	            String actualBgRgba = addMagicWheelOneWheelPage.getActualBgColor(i);
	            String actualBgHex = Color.fromString(actualBgRgba).asHex();

	            Assert.assertEquals(
	                    actualBgHex,
	                    expectedBgHex,
	                    "BG color mismatch at segment index: " + i
	            );

	            // ===== TEXT COLOR VALIDATION =====
	            String expectedTextHex = addMagicWheelOneWheelPage.getTextPickerValue(i);
	            String actualTextRgba = addMagicWheelOneWheelPage.getActualTextColor(i);
	            String actualTextHex = Color.fromString(actualTextRgba).asHex();

	            Assert.assertEquals(
	                    actualTextHex,
	                    expectedTextHex,
	                    "Text color mismatch at segment index: " + i
	            );
	        }
	    }
	  @Test(priority = 18)
	  public void verifyAddAndDeleteSegmentDynamically() {

		  AddMagicWheelOneWheelPage addMagicWheelOneWheelPage = new AddMagicWheelOneWheelPage(driver);
	      int initialCount = addMagicWheelOneWheelPage.getSegmentCount();

	      // ➕ Add
	      addMagicWheelOneWheelPage.addSegment();
	      int afterAddCount = addMagicWheelOneWheelPage.getSegmentCount();
	      Assert.assertEquals(afterAddCount, initialCount + 1,
	              "Segment count did not increase after Add");

	      addMagicWheelOneWheelPage.enterTextInLastSegment("Leadership");

	      // ➖ Delete
	      addMagicWheelOneWheelPage.deleteLastSegment();
	      int afterDeleteCount = addMagicWheelOneWheelPage.getSegmentCount();
	      Assert.assertEquals(afterDeleteCount, initialCount,
	              "Segment count did not decrease after Delete");
	  }
	  @Test(priority = 19)
	  public void verifyDeleteNotAllowedWhenMinimumSegmentsReached() {
		  AddMagicWheelOneWheelPage addMagicWheelOneWheelPage = new AddMagicWheelOneWheelPage(driver);
	      int countBefore = addMagicWheelOneWheelPage.getSegmentCount();
	      Assert.assertEquals(countBefore, 3,
	              "Segment count is not minimum (3)");

	      // Try delete
	      addMagicWheelOneWheelPage.deleteLastSegment();
	      String expectedError = "Minimum 3 wheel segments are required for this activity to function.";

	      // Validate error message
	      Assert.assertTrue(
	    		  addMagicWheelOneWheelPage.isMinimum3SegmentErrorMessage(),
			    "minimum 3 label fileds mandatory error message not displayed."
			);

			// Get the actual error and assert it's correct
			String actualError = addMagicWheelOneWheelPage.getMinimumSegmentErrorMessage();
			Assert.assertEquals(
			    actualError,
			    expectedError,
			    "Mismatch in Label field minimum 3 required error message"
			);

	      // Validate count remains same
	      int countAfter = addMagicWheelOneWheelPage.getSegmentCount();
	      Assert.assertEquals(countAfter, 3,
	              "Segment count changed even though delete is restricted");
	  }
	  @Test(priority = 20)
	  public void varifyEmptySpinDurationField() {
		  AddMagicWheelOneWheelPage addMagicWheelOneWheelPage = new AddMagicWheelOneWheelPage(driver);
		  String defaultValue = addMagicWheelOneWheelPage.getSpinDuration();
	      Assert.assertEquals(defaultValue, "3", "Default option is incorrect");
	      
	      addMagicWheelOneWheelPage.clearSpinDurationField();
	      addMagicWheelOneWheelPage.clickAssignmentSaveAndContinue();
	      String expectedError = "Please specify seconds to spin.";

	      // Validate error message
	      Assert.assertTrue(
	    		  addMagicWheelOneWheelPage.isSpinDurationErrorMessage(),
			    "Empty Spin Duration Validation Error message is not displayed"
			);

			// Get the actual error and assert it's correct
			String actualError = addMagicWheelOneWheelPage.getSpinDurationErrorMessage();
			Assert.assertEquals(
			    actualError,
			    expectedError,
			    "mismatch Empty Spin duration validation error message "
			);
			
			addMagicWheelOneWheelPage.spinDurationfield("abcXYZ");

		    String actualValue = addMagicWheelOneWheelPage.getSpinDuration();

		    Assert.assertTrue(
		            actualValue.isEmpty(),
		            "Number field accepted characters: " + actualValue
		    );
		    
		    addMagicWheelOneWheelPage.spinDurationfield("5");

		    String actualintValue = addMagicWheelOneWheelPage.getSpinDuration();

		    Assert.assertEquals(actualintValue, "5",
		            "Number field did not accept digits correctly");
	         
	  }
	  @Test(priority = 21)
	  public void varifyMaxSpinAttemptsField() {
		  AddMagicWheelOneWheelPage addMagicWheelOneWheelPage = new AddMagicWheelOneWheelPage(driver);
		  String defaultValue = addMagicWheelOneWheelPage.getMaxSpinAttempts();
	      Assert.assertEquals(defaultValue, "1", "Default option is incorrect");
	      
	      addMagicWheelOneWheelPage.clearMaxSpinAttemptsField();
	      addMagicWheelOneWheelPage.clickAssignmentSaveAndContinue();
	      String expectedError = "Please specify Max Spin Attempts.";

	      // Validate error message
	      Assert.assertTrue(
	    		  addMagicWheelOneWheelPage.isMaxSpinAttemptsErrorMessage(),
			    "Empty Max Spin attempts Validation Error message is not displayed"
			);

			// Get the actual error and assert it's correct
			String actualError = addMagicWheelOneWheelPage.getMaxSpinAttemptsErrorMessage();
			Assert.assertEquals(
			    actualError,
			    expectedError,
			    "mismatch Empty Max Spin Attempts validation error message "
			);
			
			addMagicWheelOneWheelPage.maxSpinAttemptsfield("abcXYZ");

		    String actualValue = addMagicWheelOneWheelPage.getMaxSpinAttempts();

		    Assert.assertTrue(
		            actualValue.isEmpty(),
		            "Number field accepted characters: " + actualValue
		    );
		    
		    addMagicWheelOneWheelPage.maxSpinAttemptsfield("3");

		    String actualintValue = addMagicWheelOneWheelPage.getMaxSpinAttempts();

		    Assert.assertEquals(actualintValue, "3",
		            "Number field did not accept digits correctly");
	         
	  }
	  @Test(priority = 22)
	  public void varifyWheelDiaMeterDropDown() {
		  AddMagicWheelOneWheelPage addMagicWheelOneWheelPage = new AddMagicWheelOneWheelPage(driver);
		  String defaultValue = addMagicWheelOneWheelPage.getDefaultSelectedWheelDiameterOption();
		  Assert.assertEquals(defaultValue, "Small", "Default option is incorrect");
		  addMagicWheelOneWheelPage.selectWheelDiaMeter("Regular");
		  Assert.assertEquals(addMagicWheelOneWheelPage.getSelectedWheelDiameter(), "Regular", "Failed to select 'connect@potentia.in'");
	  }
	  @Test(priority = 23)
	  public void varifyWheelChoiceRepeatfield() {
		  AddMagicWheelOneWheelPage addMagicWheelOneWheelPage = new AddMagicWheelOneWheelPage(driver);
		 Assert.assertTrue(addMagicWheelOneWheelPage.isWheelchoiceRepeatfieldDisplayed(), "Wheel choice Repeat Field not displayed");
		 addMagicWheelOneWheelPage.clickOnWheelChoiceRepeat();
	  }
	  @Test(priority = 24)
	  public void varifyPreviewOfWheel() {
		  AddMagicWheelOneWheelPage addMagicWheelOneWheelPage = new AddMagicWheelOneWheelPage(driver);
		  addMagicWheelOneWheelPage.scrollIntoWheelPreview();
		  addMagicWheelOneWheelPage.clickOnWheelPreview();
		  addMagicWheelOneWheelPage.clickOnClosePreview();
		  addMagicWheelOneWheelPage.clickAssignmentSaveAndContinue();
	  }
	  @Test(priority = 25)
	  public void validateActvityDueDateAndTimeFields() {
		  AddMagicWheelOneWheelPage addMagicWheelOneWheelPage = new AddMagicWheelOneWheelPage(driver);
		  Assert.assertTrue(addMagicWheelOneWheelPage.isDateFieldDisplayed(), "Date field not displayed");
	        Assert.assertTrue(addMagicWheelOneWheelPage.isTimeFieldDisplayed(), "Time field not displayed");
	        Assert.assertTrue(addMagicWheelOneWheelPage.isDateFieldEnabled(), "Date field disabled");
	        Assert.assertTrue(addMagicWheelOneWheelPage.isTimeFieldEnabled(), "Time field disabled");
	        String dateValue = addMagicWheelOneWheelPage.getActivityDueDateValue();
	        String timeValue = addMagicWheelOneWheelPage.getActivityDueTimeValue();

	        Assert.assertFalse(dateValue.isEmpty(), "Date value is empty");
	        Assert.assertFalse(timeValue.isEmpty(), "Time value is empty");

	        System.out.println("Due Date : " + dateValue);
	        System.out.println("Due Time : " + timeValue);
	        addMagicWheelOneWheelPage.openCalendar();
	        Assert.assertTrue(
	        		addMagicWheelOneWheelPage.isCalendarDisplayed(),
	                "Calendar is NOT displayed"
	        );

	        Assert.assertTrue(
	        		addMagicWheelOneWheelPage.isTodayHighlighted(),
	                "Today date is NOT highlighted"
	        );

	        Assert.assertTrue(
	        		addMagicWheelOneWheelPage.arePastDatesDisabled(),
	                "Past dates are NOT disabled"
	        );

	        // Select dynamic future date
	        LocalDate futureDate = LocalDate.now().plusDays(2);
	        addMagicWheelOneWheelPage.selectFutureDate(futureDate);

	        /* -------- TIME VALIDATION -------- */

	        addMagicWheelOneWheelPage.setTime("18:30");

	        String selectedTime = addMagicWheelOneWheelPage.getSelectedTime();
	        Assert.assertEquals(
	                selectedTime,
	                "06:30 PM",
	                "Time selection failed"
	        );
	  }
}

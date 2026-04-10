package com.potentia.automation.tests;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.potentia.automation.core.BaseTest;
import com.potentia.automation.listeners.ExcelReportListener;
import com.potentia.automation.pages.AddNewBatchPage;
import com.potentia.automation.pages.LoginPage;
import com.potentia.automation.pages.SuperClientsPage;
import com.potentia.automation.utils.ConfigReader;
@Listeners(ExcelReportListener.class)
public class AddNewBatchTest extends BaseTest {
	  private String batchTitle;
	  private String addProgramGenre;
	  private String newTitle;
	  private String clientName;
	  private String industry;
	  private String storedFacilitatorName;
	  private String storedFacilitatorType;
	  private String storedEmailFrom;
	  private List<Boolean> privilegeSnapshot;
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
    public void TC_ANB_02_verifyFacilitatorCanOpenCreateBatchFromAnyLandingScreen() {
        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

        addNewBatchPage.waitForPageToLoad();
        addNewBatchPage.clickNewBatch();

        Assert.assertTrue(addNewBatchPage.isCreateBatchModalDisplayed(), "Create Batch modal is not displayed.");
        Assert.assertEquals(
        		addNewBatchPage.getCreateBatchModalTitle(),
            "Create and publish your batch in 3 simple steps!",
            "Modal title mismatch."
        );
        Assert.assertTrue(addNewBatchPage.isCopyBatchButtonDisplayed(), "Copy Batch button is not displayed.");
        Assert.assertTrue(addNewBatchPage.isAddManuallyButtonDisplayed(), "Add Manually button is not displayed.");

        addNewBatchPage.clickAddManually();


    }
	 @Test(priority = 3)
	    public void TC_AB_BS_03_verifyBasicSetupTabIsActiveAndRemainingTabsAreDisabled() {
		 	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	        Assert.assertTrue(
	        		addNewBatchPage.isBasicSetupTabActive(),
	            "Basic Setup tab should be active by default."
	        );

	        Assert.assertTrue(
	        		addNewBatchPage.areRemainingTabsDisabled(),
	            "Remaining tabs should be disabled on landing."
	        );

	        Assert.assertFalse(
	        		addNewBatchPage.isTabClickable(addNewBatchPage.getProgramInfoTabLocator()),
	            "Program Info tab should not be clickable."
	        );

	        Assert.assertFalse(
	        		addNewBatchPage.isTabClickable(addNewBatchPage.getFacilitatorInfoTabLocator()),
	            "Facilitator Info tab should not be clickable."
	        );

	        Assert.assertFalse(
	        		addNewBatchPage.isTabClickable(addNewBatchPage.getMessagesTabLocator()),
	            "Messages tab should not be clickable."
	        );

	        Assert.assertFalse(
	        		addNewBatchPage.isTabClickable(addNewBatchPage.getBatchAddedTabLocator()),
	            "Batch Added tab should not be clickable."
	        );

	        // Optional color check
	        String actualHex = addNewBatchPage.rgbaToHex(
	        		addNewBatchPage.getBasicSetupTabBackgroundColor()
	        );

	        System.out.println("Basic Setup tab active color: " + actualHex);
	    }
	 @Test(priority = 4)
	    public void TC_AB_BS_04_verifyMandatoryValidationOnSaveAndExitWithoutBatchTitle() {
		 	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
		 	addNewBatchPage.clickSaveAndExit();

	        Assert.assertTrue(
	        		addNewBatchPage.isValidationMessageDisplayed("Please enter batch title."),
	            "Mandatory validation message for empty batch title is not displayed on Save & Exit."
	        );
	    }

	    @Test(priority = 5)
	    public void TC_AB_BS_05_verifyMandatoryValidationOnSaveAndContinueWithoutBatchTitle() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.clickSaveAndContinue();

	        Assert.assertTrue(
	        		addNewBatchPage.isValidationMessageDisplayed("Please enter batch title."),
	            "Mandatory validation message for empty batch title is not displayed on Save & Continue."
	        );
	    }
	    @Test(priority = 6)
	    public void TC_AB_BS_06_verifyBatchTitleMaxLengthValidationOnSaveAndExit() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	        String overLimitTitle = addNewBatchPage.generateText(151);
	        addNewBatchPage.enterBatchTitle(overLimitTitle);
	        addNewBatchPage.clickSaveAndExit();

	        Assert.assertTrue(
	        		addNewBatchPage.isValidationMessageDisplayed("Batch title is too long, max 150 characters allowed"),
	            "Batch title max length validation message is not displayed on Save & Exit."
	        );
	    }

	    @Test(priority = 7)
	    public void TC_AB_BS_07_verifyBatchTitleMaxLengthValidationOnSaveAndContinue() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	        String overLimitTitle = addNewBatchPage.generateText(151);
	        addNewBatchPage.enterBatchTitle(overLimitTitle);
	        addNewBatchPage.clickSaveAndContinue();

	        Assert.assertTrue(
	        		addNewBatchPage.isValidationMessageDisplayed("Batch title is too long, max 150 characters allowed"),
	            "Batch title max length validation message is not displayed on Save & Continue."
	        );
	    }

	    @Test(priority = 8)
	    public void TC_AB_BS_08_verifyBatchTitleHelpText() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	        Assert.assertEquals(
	        		addNewBatchPage.getBatchTitleHelpText(),
	            "Unique name for the batch within a Client",
	            "Batch Title help text is incorrect."
	        );
	    }
	    @Test(priority = 9)
	    public void TC_AB_BS_09_verifyDescriptionDropdownExpandAndHelpText() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.expandDescription();

	        Assert.assertTrue(
	        		addNewBatchPage.isDescriptionExpanded(),
	            "Description field should be visible after expanding."
	        );

	        Assert.assertEquals(
	        		addNewBatchPage.getDescriptionHelpText(),
	            "Brief description of the batch",
	            "Description help text is incorrect."
	        );
	    }
	    @Test(priority = 10)
	    public void TC_AB_BS_10_verifyDescriptionMaxLengthValidationOnSaveAndExit() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.enterBatchTitle("Valid Batch Title");
	    	addNewBatchPage.enterDescription(addNewBatchPage.generateText(1001));
	    	addNewBatchPage.clickSaveAndExit();

	        Assert.assertTrue(
	        		addNewBatchPage.isValidationMessageDisplayed("Batch description is too long, max 1000 characters allowed"),
	            "Description max length validation message is not displayed on Save & Exit."
	        );
	    }

	    @Test(priority = 11)
	    public void TC_AB_BS_11_verifyDescriptionMaxLengthValidationOnSaveAndContinue() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.enterBatchTitle("Valid Batch Title");
	    	addNewBatchPage.enterDescription(addNewBatchPage.generateText(1001));
	    	addNewBatchPage.clickSaveAndContinue();

	        Assert.assertTrue(
	        		addNewBatchPage.isValidationMessageDisplayed("Batch description is too long, max 1000 characters allowed"),
	            "Description max length validation message is not displayed on Save & Continue."
	        );
	    }
	    @Test(priority = 12)
	    public void TC_AB_BS_12_verifyVisibleLikelyActivitiesCheckboxesWorking() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	        List<String> failures = addNewBatchPage.validateAllVisibleActivityCheckboxSelection();

	        Assert.assertTrue(
	            failures.isEmpty(),
	            "Visible activity checkbox validation failures: " + failures
	        );
	    }

	    @Test(priority = 13)
	    public void TC_AB_BS_13_verifyVisibleLikelyActivitiesHelpTextsPresent() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	        Map<String, String> helpTexts = addNewBatchPage.getVisibleActivitiesHelpTexts();

	        Assert.assertFalse(helpTexts.isEmpty(), "Visible activity help texts map should not be empty.");

	        for (Map.Entry<String, String> entry : helpTexts.entrySet()) {
	            Assert.assertFalse(
	                entry.getValue().trim().isEmpty(),
	                "Help text is missing for visible activity: " + entry.getKey()
	            );
	            System.out.println(entry.getKey() + " -> " + entry.getValue());
	        }
	    }
	    @Test(priority = 14)
	    public void TC_AB_BS_14_verifyViewMoreActivitiesDropdownAndCheckboxesWorking() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.expandViewMoreActivities();

	        Assert.assertTrue(
	        		addNewBatchPage.isMoreActivitiesExpanded(),
	            "View More Activities section should be expanded."
	        );

	        Assert.assertTrue(
	        		addNewBatchPage.getMoreActivitiesCount() > 0,
	            "More activities should be displayed after expanding."
	        );

	        List<String> failures = addNewBatchPage.validateAllMoreActivityCheckboxSelection();

	        Assert.assertTrue(
	            failures.isEmpty(),
	            "More activity checkbox validation failures: " + failures
	        );
	    }
	    @Test(priority = 15)
	    public void TC_AB_BS_15_verifyMoreActivitiesHelpTextsPresent() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	        Map<String, String> helpTexts = addNewBatchPage.getMoreActivitiesHelpTexts();

	        Assert.assertFalse(helpTexts.isEmpty(), "More activity help texts map should not be empty.");

	        for (Map.Entry<String, String> entry : helpTexts.entrySet()) {
	            Assert.assertFalse(
	                entry.getValue().trim().isEmpty(),
	                "Help text is missing for more activity: " + entry.getKey()
	            );
	            System.out.println(entry.getKey() + " -> " + entry.getValue());
	        }
	    }
	    @Test(priority = 16)
	    public void TC_AB_BS_16_verifyButtonsAreDisplayed() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	        Assert.assertTrue(addNewBatchPage.isCancelButtonDisplayed(), "Cancel button is not displayed.");
	        Assert.assertTrue(addNewBatchPage.isSaveAndExitButtonDisplayed(), "Save & Exit button is not displayed.");
	        Assert.assertTrue(addNewBatchPage.isSaveAndContinueButtonDisplayed(), "Save & Continue button is not displayed.");
	    }
	    @Test(priority = 17)
	    public void TC_AB_BS_17_verifyFacilitatorCanCreateBatchUsingSaveAndExitInBasicSetUpTab() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        String batchTitle = "Auto Batch " + System.currentTimeMillis();
	        String batchDescription = "This batch is created through automation using Save and Exit flow.";

	        // Enter valid batch details
	        addNewBatchPage.enterBatchTitle(batchTitle);
	        addNewBatchPage.enterDescription(batchDescription);

	        // Select activities
	        addNewBatchPage.selectAssignmentActivity();
	        addNewBatchPage.selectReferenceMaterialActivity();

	        // Click Save & Exit
	        addNewBatchPage.clickSaveAndExit();
	        
	        addNewBatchPage.waitForPageToLoad();

	        // Verify success message
	        Assert.assertTrue(
	        		addNewBatchPage.isBatchCreatedSuccessfullyMessageDisplayed(),
	                "When Click on Save And Exit Button Server Not responding error is displayed"
	        );
	        addNewBatchPage.waitForPageToLoad();
	        // Verify Planned tab is active
	        Assert.assertTrue(
	        		addNewBatchPage.waitForPlannedTabToBecomeActive(),
	        	    "Facilitator is not landed on Planned tab after clicking Save & Exit."
	        	);

	        // Optional: Verify created batch is present
	        Assert.assertTrue(
	        		addNewBatchPage.isBatchPresentInPlannedList(batchTitle),
	                "Created batch is not present in Planned tab list."
	        );
	    }
	    @Test(priority = 18)
	    public void TC_AB_BS_18_verifyFacilitatorLandsOnProgramInfoPageAfterClickingSaveAndContinueInBasicSetUpTab() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.clickNewBatch();
	    	addNewBatchPage.clickAddManually();
	    	batchTitle = "Auto Batch " + System.currentTimeMillis();
	        String batchDescription = "This batch is created through automation using Save and Exit flow.";

	        // Enter valid batch details
	        addNewBatchPage.enterBatchTitle(batchTitle);
	        addNewBatchPage.enterDescription(batchDescription);

	        // Select activities
	        addNewBatchPage.selectAssignmentActivity();
	        addNewBatchPage.selectReferenceMaterialActivity();
	        
	        addNewBatchPage.clickSaveAndContinue();
	        
	     // Step 5: Wait for Program Info page
	        addNewBatchPage.waitForProgramInfoPageToLoad();
	        
	        // Step 6: Validate Program Info heading
	        Assert.assertTrue(
	        		addNewBatchPage.isProgramInfoHeadingDisplayed(),
	                "Program Info heading is not displayed after clicking Save & Continue."
	        );

	        // Step 7: Validate Program Info tab is active
	        Assert.assertTrue(
	        		addNewBatchPage.isProgramInfoTabActive(),
	                "Program Info tab is not active after clicking Save & Continue."
	        );
	    }
	    
	    @Test(priority = 19)
	    public void TC_AB_PI_19_verifyProgramInfoTabStateAfterSaveAndContinueinBasicSetUpTabAndCheckingModificationsinBasicSetUpTab() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        // Step 6: Verify remaining future tabs are disabled
	        Assert.assertTrue(
	        		addNewBatchPage.isFacilitatorInfoTabDisabled(),
	                "Facilitator Info tab should be disabled."
	        );

	        Assert.assertTrue(
	        		addNewBatchPage.isMessagesTabDisabled(),
	                "Messages tab should be disabled."
	        );

	        Assert.assertTrue(
	        		addNewBatchPage.isBatchAddedTabDisabled(),
	                "Batch Added tab should be disabled."
	        );

	        // Optional color print
	        System.out.println("Basic Setup tab color: " + addNewBatchPage.getBasicSetupTabColorHex());
	        System.out.println("Program Info tab color: " + addNewBatchPage.getProgramInfoTabColorHex());
	        
	        String basicSetupTabClass = addNewBatchPage.getBasicSetupTabClass();

	        Assert.assertTrue(
	        		addNewBatchPage.isBasicSetupTabClickable(),
	            "Basic Setup tab click validation failed. " +
	            "Expected: tab should be clickable after landing on Program Info. " +
	            "Actual: tab contains class = '" + basicSetupTabClass + "'."
	        );

	        // Step 7: Click Basic Setup tab and verify navigation
	        addNewBatchPage.clickBasicSetupTab();

	        Assert.assertTrue(
	        		addNewBatchPage.isBasicSetupHeadingDisplayed(),
	                "User is not navigated back to Basic Setup page after clicking Basic Setup tab."
	        );
	        batchTitle = "Auto Batch " + System.currentTimeMillis();
	        String batchDescription = "This batch is created through automation using Save and Exit flow.";
	        addNewBatchPage.enterBatchTitle(batchTitle);
	        addNewBatchPage.enterDescription(batchDescription);

	        // Select activities
	        addNewBatchPage.selectAssignmentActivity();
	        addNewBatchPage.selectReferenceMaterialActivity();
	        addNewBatchPage.clickSaveAndContinue();
	        
		     // Step 5: Wait for Program Info page
		        addNewBatchPage.waitForProgramInfoPageToLoad();
		        
		        // Step 6: Validate Program Info heading
		        Assert.assertTrue(
		        		addNewBatchPage.isProgramInfoHeadingDisplayed(),
		                "Program Info heading is not displayed after clicking Save & Continue."
		        );
	    }
	    @Test(priority = 20)
	    public void TC_AB_PI_20_verifyProgramInfoDateFieldsDisplayedAndEnabled() {
	        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        addNewBatchPage.waitForProgramInfoPageToLoad();

	        Assert.assertTrue(addNewBatchPage.isStartDateDisplayed(), "Start Date field is not displayed.");
	        Assert.assertTrue(addNewBatchPage.isEndDateDisplayed(), "End Date field is not displayed.");
	        Assert.assertTrue(addNewBatchPage.isStartDateEnabled(), "Start Date field is not enabled.");
	        Assert.assertTrue(addNewBatchPage.isEndDateEnabled(), "End Date field is not enabled.");
	    }
	    @Test(priority = 21)
	    public void TC_AB_PI_21_verifyDefaultStartDateIsTodayDate() {
	        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        addNewBatchPage.waitForProgramInfoPageToLoad();

	        Assert.assertEquals(
	                addNewBatchPage.getStartDateValue(),
	                addNewBatchPage.getTodayDateInUiFormat(),
	                "Default Start Date is not equal to today's date."
	        );
	    }
	    @Test(priority = 22)
	    public void TC_AB_PI_22_verifyDefaultEndDateIsOneMonthAfterStartDate() {
	        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        addNewBatchPage.waitForProgramInfoPageToLoad();

	        String startDate = addNewBatchPage.getStartDateValue();
	        String expectedEndDate = addNewBatchPage.getOneMonthAfterDate(startDate);

	        Assert.assertEquals(
	                addNewBatchPage.getEndDateValue(),
	                expectedEndDate,
	                "Default End Date is not one month after Start Date."
	        );
	    }
	    @Test(priority = 23)
	    public void TC_AB_PI_23_verifyStartDateHelpText() {
	        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        addNewBatchPage.waitForProgramInfoPageToLoad();

	        Assert.assertEquals(
	                addNewBatchPage.getStartDateHelpText(),
	                "Batch start date",
	                "Start Date help text is incorrect."
	        );
	    }
	    @Test(priority = 24)
	    public void TC_AB_PI_24_verifyEndDateHelpText() {
	        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        addNewBatchPage.waitForProgramInfoPageToLoad();

	        Assert.assertEquals(
	                addNewBatchPage.getEndDateHelpText(),
	                "Batch close date.  Post this date, access restricted for participants of this batch.",
	                "End Date help text is incorrect."
	        );
	    }
	    @Test(priority = 25)
	    public void TC_AB_PI_25_verifyStartDateRequiredValidationOnSaveAndExit() {
	        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        addNewBatchPage.waitForProgramInfoPageToLoad();
	        addNewBatchPage.clearStartDate();
	        addNewBatchPage.clickSaveAndExitOnProgramInfo();

	        Assert.assertTrue(
	                addNewBatchPage.isStartDateRequiredValidationDisplayed(),
	                "Please enter Start Date validation is not displayed on Save & Exit."
	        );
	    }
	    @Test(priority = 26)
	    public void TC_AB_PI_26_verifyStartDateRequiredValidationOnSaveAndContinue() {
	        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        addNewBatchPage.waitForProgramInfoPageToLoad();
	        addNewBatchPage.clearStartDate();
	        addNewBatchPage.clickSaveAndContinueOnProgramInfo();

	        Assert.assertTrue(
	                addNewBatchPage.isStartDateRequiredValidationDisplayed(),
	                "Please enter Start Date validation is not displayed on Save & Continue."
	        );
	    }
	    @Test(priority = 27)
	    public void TC_AB_PI_27_verifyUserCanEnterValidStartDate() {
	        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        addNewBatchPage.waitForProgramInfoPageToLoad();

	        String newStartDate = addNewBatchPage.getTodayDateInUiFormat();
	        addNewBatchPage.enterStartDate(newStartDate);

	        Assert.assertEquals(
	                addNewBatchPage.getStartDateValue(),
	                newStartDate,
	                "Valid Start Date is not entered correctly."
	        );
	    }
	    @Test(priority = 28)
	    public void TC_AB_PI_28_verifyEndDateRequiredValidationOnSaveAndExit() {
	        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        addNewBatchPage.waitForProgramInfoPageToLoad();
	        addNewBatchPage.clearEndDate();
	        addNewBatchPage.clickSaveAndExitOnProgramInfo();

	        Assert.assertTrue(
	                addNewBatchPage.isEndDateRequiredValidationDisplayed(),
	                "Please enter End Date validation is not displayed on Save & Exit."
	        );
	    }
	    @Test(priority = 29)
	    public void TC_AB_PI_29_verifyEndDateRequiredValidationOnSaveAndContinue() {
	        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        addNewBatchPage.waitForProgramInfoPageToLoad();
	        addNewBatchPage.clearEndDate();
	        addNewBatchPage.clickSaveAndContinueOnProgramInfo();

	        Assert.assertTrue(
	                addNewBatchPage.isEndDateRequiredValidationDisplayed(),
	                "Please enter End Date validation is not displayed on Save & Continue."
	        );
	    }
	    
	    @Test(priority = 30)
	    public void TC_AB_PI_30_verifyUserCanEnterValidEndDate() {
	        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        addNewBatchPage.waitForProgramInfoPageToLoad();

	        String newEndDate = addNewBatchPage.getOneMonthAfterTodayInUiFormat();
	        addNewBatchPage.enterEndDate(newEndDate);

	        Assert.assertEquals(
	                addNewBatchPage.getEndDateValue(),
	                newEndDate,
	                "Valid End Date is not entered correctly."
	        );
	    }
	    @Test(priority = 31)
	    public void TC_AB_PI_31_verifyEndDateIsNotBeforeStartDate() {
	        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        addNewBatchPage.waitForProgramInfoPageToLoad();

	        addNewBatchPage.enterStartDate(addNewBatchPage.getTodayDateInUiFormat());
	        addNewBatchPage.enterEndDate(addNewBatchPage.getOneMonthAfterTodayInUiFormat());

	        Assert.assertTrue(
	                addNewBatchPage.isEndDateAfterOrEqualStartDate(),
	                "End Date should be greater than or equal to Start Date."
	        );
	    }
	    @Test(priority = 32)
	    public void TC_AB_PI_32_verifyPastDatesDisabledInStartDatePicker() {
	        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        addNewBatchPage.waitForProgramInfoPageToLoad();

	        Assert.assertTrue(
	                addNewBatchPage.arePastDatesDisabledInOpenStartDatePicker(),
	                "Past dates are selectable in Start Date picker."
	        );
	    }
	    @Test(priority = 33)
	    public void TC_AB_PI_33_verifyPastDatesDisabledInEndDatePicker() {
	        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        addNewBatchPage.waitForProgramInfoPageToLoad();

	        Assert.assertTrue(
	                addNewBatchPage.arePastDatesDisabledInOpenEndDatePicker(),
	                "Past dates are selectable in End Date picker."
	        );
	    }
	    @Test(priority = 34)
	    public void TC_PI_34_verifyProgramGenreAndProgramTitleDefaultState() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.waitForProgramInfoPageToLoad();

	        Assert.assertTrue(addNewBatchPage.isProgramGenreDisplayed(), "Program Genre dropdown is not displayed.");
	        Assert.assertTrue(addNewBatchPage.isProgramTitleDisplayed(), "Program Title dropdown is not displayed.");
	        Assert.assertTrue(addNewBatchPage.isAddGenreButtonDisplayed(), "+ Genre button is not displayed.");
	        Assert.assertTrue(addNewBatchPage.isAddTitleButtonDisplayed(), "+ Title button is not displayed.");

	        Assert.assertEquals(
	        		addNewBatchPage.getDefaultProgramGenreText(),
	                "--Select Program Genre--",
	                "Default Program Genre text is incorrect."
	        );

	        Assert.assertEquals(
	        		addNewBatchPage.getDefaultProgramTitleText(),
	                "",
	                "Default Program Title text should be empty before selecting Program Genre."
	        );
	    }

	    @Test(priority = 35)
	    public void TC_PI_35_verifyProgramGenreAndProgramTitleHelpText() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.waitForProgramInfoPageToLoad();

	        Assert.assertEquals(
	        		addNewBatchPage.getProgramGenreHelpText(),
	                "A theme encompassing a group of programs/ courses",
	                "Program Genre help text is incorrect."
	        );

	        Assert.assertEquals(
	        		addNewBatchPage.getProgramTitleHelpText(),
	                "A session or course designed to develop specific skills or knowledge in participants",
	                "Program Title help text is incorrect."
	        );
	    }

	    @Test(priority = 36)
	    public void TC_PI_36_verifyProgramGenreSearchFunctionalityAndverifyProgramGenreSearchNoResultsFound() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.waitForProgramInfoPageToLoad();

	    	addNewBatchPage.openProgramGenreDropdown();
	    	addNewBatchPage.searchInSelect2("Demo Program");

	        Assert.assertTrue(
	        		addNewBatchPage.isSelect2ResultPresent("Demo Program"),
	                "Expected Program Genre search result is not displayed."
	        );
	        
	        addNewBatchPage.searchInSelect2("NoSuchGenreAutomationXYZ");

	        Assert.assertTrue(
	        		addNewBatchPage.isNoResultsFoundDisplayed(),
	                "No results found message is not displayed for unavailable Program Genre."
	        );
	    }


	    @Test(priority = 37)
	    public void TC_PI_37_verifyProgramTitleDropdownPopulatesAfterSelectingProgramGenre() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.waitForProgramInfoPageToLoad();

	    	addNewBatchPage.selectProgramGenre("Program Genre");

	        List<String> titles = addNewBatchPage.getProgramTitleOptionsFromSelect();

	        Assert.assertTrue(
	                titles.size() > 0,
	                "Program Title dropdown did not populate after selecting Program Genre."
	        );
	    }

	    @Test(priority = 38)
	    public void TC_PI_38_verifyAddProgramGenrePopupDisplays() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.waitForProgramInfoPageToLoad();

	    	addNewBatchPage.clickAddGenreButton();

	        Assert.assertTrue(addNewBatchPage.isAddProgramGenrePopupDisplayed(), "Add Program Genre popup is not displayed.");
	        Assert.assertEquals(
	        		addNewBatchPage.getAddProgramGenrePopupTitle(),
	                "Add Program Genre",
	                "Add Program Genre popup title is incorrect."
	        );
	    }

	    @Test(priority = 39)
	    public void TC_PI_39_verifyAddProgramGenreMandatoryValidation() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.waitForProgramInfoPageToLoad();
	    	
	    	addNewBatchPage.clickAddProgramGenrePopupAddButton();

	        Assert.assertTrue(
	        		addNewBatchPage.isMandatoryValidationDisplayed(),
	                "Mandatory validation is not displayed in Add Program Genre popup."
	        );
	    }

	    @Test(priority = 40)
	    public void TC_PI_40_verifyProgramGenreFieldMaxLengthValidation() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.waitForProgramInfoPageToLoad();

	    	addNewBatchPage.enterProgramGenreName(addNewBatchPage.generateText(151));
	    	addNewBatchPage.enterLikelyProgramTitleAtIndex(0, "Likely Title 1");
	    	addNewBatchPage.clickAddProgramGenrePopupAddButton();

	        Assert.assertTrue(
	        		addNewBatchPage.isProgramGenreLengthValidationDisplayed(),
	                "Program Genre max length validation is not displayed."
	        );
	    }

	    @Test(priority = 41)
	    public void TC_PI_41_verifyShortTitleFieldMaxLengthValidation() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.waitForProgramInfoPageToLoad();

	    	addNewBatchPage.enterProgramGenreName("Genre Automation");
	    	addNewBatchPage.enterShortTitle(addNewBatchPage.generateText(151));
	    	addNewBatchPage.enterLikelyProgramTitleAtIndex(0, "Likely Title 1");
	    	addNewBatchPage.clickAddProgramGenrePopupAddButton();

	        Assert.assertTrue(
	        		addNewBatchPage.isShortTitleLengthValidationDisplayed(),
	                "Short Title max length validation is not displayed."
	        );
	    }

	    @Test(priority = 42)
	    public void TC_PI_42_verifyLikelyProgramTitleAddAndRemoveFunctionality() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.waitForProgramInfoPageToLoad();

	        int initialCount = addNewBatchPage.getLikelyProgramTitleFieldCount();
	        addNewBatchPage.clickAddLikelyProgramTitleButtonAtIndex(0);

	        Assert.assertEquals(
	        		addNewBatchPage.getLikelyProgramTitleFieldCount(),
	                initialCount + 1,
	                "New likely program title field was not added."
	        );

	        addNewBatchPage.clickRemoveLikelyProgramTitleButtonAtIndex(0);

	        Assert.assertEquals(
	        		addNewBatchPage.getLikelyProgramTitleFieldCount(),
	                initialCount,
	                "Likely program title field was not removed."
	        );
	    }

	    @Test(priority = 43)
	    public void TC_PI_43_verifyLikelyProgramTitleMaxLengthValidation() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.waitForProgramInfoPageToLoad();

	    	addNewBatchPage.enterProgramGenreName("Genre Automation");
	    	addNewBatchPage.enterShortTitle("");
	    	addNewBatchPage.enterLikelyProgramTitleAtIndex(0, addNewBatchPage.generateText(151));
	    	addNewBatchPage.clickAddProgramGenrePopupAddButton();

	        Assert.assertTrue(
	        		addNewBatchPage.isLikelyProgramTitleLengthValidationDisplayed(),
	                "Likely Program Title max length validation is not displayed."
	        );
	    }

	    @Test(priority = 44)
	    public void TC_PI_44_verifyDescriptionMaxLengthValidationInAddProgramGenrePopup() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.waitForProgramInfoPageToLoad();

	    	addNewBatchPage.enterProgramGenreName("Genre Automation");
	    	addNewBatchPage.enterLikelyProgramTitleAtIndex(0, "Likely Title 1");
	    	addNewBatchPage.enterProgramGenreDescription(addNewBatchPage.generateText(1001));
	    	addNewBatchPage.clickAddProgramGenrePopupAddButton();

	        Assert.assertTrue(
	        		addNewBatchPage.isDescriptionLengthValidationDisplayed(),
	                "Description max length validation is not displayed."
	        );
	    }

	    @Test(priority = 45)
	    public void TC_PI_45_verifyNewProgramGenreIsAddedAndSelectedInProgramGenreDropDown() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	    	String programGenre = "Auto Genre " + System.currentTimeMillis();
	        String shortTitle = "Auto Short";
	        List<String> likelyTitles = Arrays.asList("Likely Title 1", "Likely Title 2");
	        String description = "Automation description";

	        addNewBatchPage.addProgramGenre(programGenre, shortTitle, likelyTitles, description);
	        addNewBatchPage.clickAddProgramGenrePopupAddButton();

	        addNewBatchPage.waitForProgramInfoTabOrServerError();

	        if (addNewBatchPage.isServerErrorDisplayed()) {
	            Assert.fail("After clicking Add Program Genre, server error displayed: "
	                    + addNewBatchPage.getServerErrorMessageText());
	        }

	        Assert.assertEquals(
	                addNewBatchPage.getDefaultProgramTitleText(),
	                "--Select Title--",
	                "Program Title dropdown default text is not '--Select Title--' after adding Program Genre."
	        );
	    }

	    @Test(priority = 46)
	    public void TC_PI_46_verifyProgramTitleOptionsMatchLikelyTitlesFromAddedProgramGenre() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    			addNewBatchPage.waitForProgramInfoPageToLoad();

	        String programGenre = "Auto Genre Map " + System.currentTimeMillis();
	        List<String> likelyTitles = Arrays.asList("Map Title 1", "Map Title 2", "Map Title 3");
	        
	        addNewBatchPage.clickAddGenreButton();
	        addNewBatchPage.addProgramGenre(programGenre, "MapShort", likelyTitles, "Desc");
	        addNewBatchPage.clickAddProgramGenrePopupAddButton();
	        
	        addNewBatchPage.waitForProgramInfoTabOrServerError();

	        if (addNewBatchPage.isServerErrorDisplayed()) {
	            Assert.fail("After clicking Add Program Genre, server error displayed: "
	                    + addNewBatchPage.getServerErrorMessageText());
	        }

	        Set<String> expectedTitles = new LinkedHashSet<>(likelyTitles);

	        Assert.assertTrue(
	        		addNewBatchPage.doProgramTitleOptionsMatchExpected(expectedTitles),
	                "Program Title dropdown options do not match added likely titles."
	        );
	    }

	    @Test(priority = 47)
	    public void TC_PI_47_verifyAddProgramTitlePopupDisplays() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.waitForProgramInfoPageToLoad();

	    	addNewBatchPage.clickAddTitleButton();

	        Assert.assertTrue(addNewBatchPage.isAddProgramTitlePopupDisplayed(), "Add Program Title popup is not displayed.");
	        Assert.assertEquals(
	        		addNewBatchPage.getAddProgramTitlePopupTitle(),
	                "Add Program Title",
	                "Add Program Title popup title is incorrect."
	        );
	    }

	    @Test(priority = 48)
	    public void TC_PI_48_verifyAddProgramTitleMandatoryValidation() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.waitForProgramInfoPageToLoad();
	    	
	    	addNewBatchPage.clickAddProgramTitlePopupAddButton();

	        Assert.assertTrue(
	        		addNewBatchPage.isMandatoryValidationDisplayed(),
	                "Mandatory validation is not displayed in Add Program Title popup."
	        );
	    }

	    @Test(priority = 49)
	    public void TC_PI_49_verifyProgramTitleMaxLengthValidationInAddProgramTitlePopup() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.waitForProgramInfoPageToLoad();

	    	addNewBatchPage.enterNewProgramTitle(addNewBatchPage.generateText(151));
	    	addNewBatchPage.clickAddProgramTitlePopupAddButton();

	        Assert.assertTrue(
	        		addNewBatchPage.isProgramTitleLengthValidationDisplayed(),
	                "Program Title max length validation is not displayed."
	        );
	        addNewBatchPage.cancelAddProgramTitlePopup();
	    }

	    @Test(priority = 50)
	    public void TC_PI_50_verifyNewProgramTitleIsAddedToProgramTitleDropdown() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.waitForProgramInfoPageToLoad();

	        // Precondition: select an existing genre first
	    	addProgramGenre = "Program Genre";
	    	addNewBatchPage.selectProgramGenre(addProgramGenre);

	    	newTitle = "Auto Program Title " + System.currentTimeMillis();

	        addNewBatchPage.addProgramTitle(newTitle);

	        List<String> titles = addNewBatchPage.getProgramTitleOptionsFromSelect();

	        Assert.assertTrue(
	                titles.stream().anyMatch(t -> t.trim().equals(newTitle)),
	                "Newly added Program Title is not present in Program Title dropdown."
	        );
	    }
	    @Test(priority = 51)
	    public void TC_TA_51_verifyTargetAudienceLabelIsDisplayed() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	        Assert.assertTrue(addNewBatchPage.isLabelDisplayed(), "Target Audience label is not displayed.");
	        Assert.assertTrue(
	        		addNewBatchPage.getLabelText().contains("Target Audience"),
	            "Target Audience label text is incorrect."
	        );
	    }

	    @Test(priority = 52)
	    public void TC_TA_52_verifyDropdownIsVisibleAndEnabled() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	        Assert.assertTrue(addNewBatchPage.isDropdownDisplayed(), "Target Audience dropdown is not visible.");
	        Assert.assertTrue(addNewBatchPage.isDropdownEnabled(), "Target Audience dropdown is not enabled.");
	    }

	    @Test(priority = 53)
	    public void TC_TA_53_verifyDefaultSelectedOption() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	        String expectedDefault = "--Select Target Audience--";
	        String actualDefault = addNewBatchPage.getDefaultSelectedOption();

	        Assert.assertEquals(actualDefault, expectedDefault, "Default dropdown option is incorrect.");
	    }

	    @Test(priority = 54)
	    public void TC_TA_54_verifyAllDropdownOptions() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	        List<String> expectedOptions = Arrays.asList(
	            "--Select Target Audience--",
	            "Junior Mgmt",
	            "Middle Mgmt",
	            "Senior Mgmt",
	            "Top Mgmt",
	            "Jr + Middle Mgmt",
	            "Middle + Sr Mgmt",
	            "All Mix"
	        );

	        List<String> actualOptions = addNewBatchPage.getAllDropdownOptions();

	        Assert.assertEquals(actualOptions, expectedOptions, "Dropdown options are not matching.");
	    }

	    @Test(priority = 55)
	    public void TC_TA_55_verifyDropdownOptionsCount() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	        int expectedCount = 8;
	        int actualCount = addNewBatchPage.getOptionsCount();

	        Assert.assertEquals(actualCount, expectedCount, "Dropdown options count is incorrect.");
	    }

	    @Test(priority = 56)
	    public void TC_TA_56_verifyNoDuplicateOptionsPresent() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	        Assert.assertFalse(addNewBatchPage.hasDuplicateOptions(), "Duplicate dropdown options are present.");
	    }

	    @Test(priority = 57)
	    public void TC_TA_57_verifyOptionValueMapping() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	        List<String> expectedValues = Arrays.asList("", "1", "2", "3", "4", "5", "6", "7");
	        List<String> actualValues = addNewBatchPage.getAllOptionValues();

	        Assert.assertEquals(actualValues, expectedValues, "Dropdown option values are not mapped correctly.");
	    }

	    @Test(priority = 58)
	    public void TC_TA_58_verifyUserCanSelectJuniorMgmt() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.selectTargetAudience("Junior Mgmt");
	        Assert.assertEquals(
	        		addNewBatchPage.getSelectedTargetAudience(),
	            "Junior Mgmt",
	            "Junior Mgmt option was not selected successfully."
	        );
	    }

	    @Test(priority = 59)
	    public void TC_TA_59_verifyUserCanSelectMiddleMgmt() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.selectTargetAudience("Middle Mgmt");
	        Assert.assertEquals(
	        		addNewBatchPage.getSelectedTargetAudience(),
	            "Middle Mgmt",
	            "Middle Mgmt option was not selected successfully."
	        );
	    }

	    @Test(priority = 60)
	    public void TC_TA_60_verifyUserCanSelectSeniorMgmt() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.selectTargetAudience("Senior Mgmt");
	        Assert.assertEquals(
	        		addNewBatchPage.getSelectedTargetAudience(),
	            "Senior Mgmt",
	            "Senior Mgmt option was not selected successfully."
	        );
	    }

	    @Test(priority = 61)
	    public void TC_TA_61_verifyUserCanSelectTopMgmt() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.selectTargetAudience("Top Mgmt");
	        Assert.assertEquals(
	        		addNewBatchPage.getSelectedTargetAudience(),
	            "Top Mgmt",
	            "Top Mgmt option was not selected successfully."
	        );
	    }

	    @Test(priority = 62)
	    public void TC_TA_62_verifyUserCanSelectJrAndMiddleMgmt() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.selectTargetAudience("Jr + Middle Mgmt");
	        Assert.assertEquals(
	        		addNewBatchPage.getSelectedTargetAudience(),
	            "Jr + Middle Mgmt",
	            "Jr + Middle Mgmt option was not selected successfully."
	        );
	    }

	    @Test(priority = 63)
	    public void TC_TA_63_verifyUserCanSelectMiddleAndSrMgmt() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.selectTargetAudience("Middle + Sr Mgmt");
	        Assert.assertEquals(
	        		addNewBatchPage.getSelectedTargetAudience(),
	            "Middle + Sr Mgmt",
	            "Middle + Sr Mgmt option was not selected successfully."
	        );
	    }

	    @Test(priority = 64)
	    public void TC_TA_64_verifyUserCanSelectAllMix() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.selectTargetAudience("All Mix");
	        Assert.assertEquals(
	        		addNewBatchPage.getSelectedTargetAudience(),
	            "All Mix",
	            "All Mix option was not selected successfully."
	        );
	    }

	    @Test(priority = 65)
	    public void TC_TA_65_verifySelectedValuePersistsAfterClickOutside() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.selectTargetAudience("Senior Mgmt");

	        // click body or any safe location
	        driver.findElement(org.openqa.selenium.By.tagName("body")).click();

	        Assert.assertEquals(
	        		addNewBatchPage.getSelectedTargetAudience(),
	            "Senior Mgmt",
	            "Selected value did not persist after clicking outside."
	        );
	    }
	    @Test(priority = 66)
	    public void TC_CL_66_verifyClientDropdownOpensAndSearchBoxDisplayed() {
	        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        addNewBatchPage.openClientDropdown();

	        Assert.assertTrue(addNewBatchPage.isClientDropdownOpened(), "Client dropdown is not opened.");
	        Assert.assertTrue(addNewBatchPage.isClientSearchBoxDisplayed(), "Client search box is not displayed.");
	    }
	    @Test(priority = 67)
	    public void TC_CL_67_verifyInvalidClientSearchShowsNoResults() {
	        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        addNewBatchPage.searchClient("InVaildClient");

	        Assert.assertTrue(addNewBatchPage.isNoResultsFoundDisplayed(), "'No results found' message is not displayed.");
	        addNewBatchPage.clickOutsideToCloseDropdown();
	    }
	    @Test(priority = 68)
	    public void TC_CL_68_verifyMandatoryFieldValidation() {
	        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        addNewBatchPage.clickAddClientButton();
	        addNewBatchPage.clickAddButtonInPopup();

	        Assert.assertEquals(
	        		addNewBatchPage.getMandatoryErrorMessage(),
	                "All fields marked with an asterix are mandatory. Please provide details before submitting.",
	                "Mandatory field validation message mismatch."
	        );
	    }
	    @Test(priority = 69)
	    public void TC_CL_69_verifyClientNameLengthValidation() {
	        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        addNewBatchPage.enterClientName(addNewBatchPage.getStringWithLength(151));
	        addNewBatchPage.enterIndustry("IT");
	        addNewBatchPage.clickAddButtonInPopup();

	        Assert.assertEquals(
	        		addNewBatchPage.getClientNameLengthErrorMessage(),
	                "Client Name is too long, max 150 characters allowed",
	                "Client name length validation message mismatch."
	        );
	    }
	    @Test(priority = 70)
	    public void TC_CL_70_verifyIndustryLengthValidation() {
	        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        addNewBatchPage.enterClientName("Automation Client " + System.currentTimeMillis());
	        addNewBatchPage.enterIndustry(addNewBatchPage.getStringWithLength(151));
	        addNewBatchPage.clickAddButtonInPopup();

	        Assert.assertEquals(
	        		addNewBatchPage.getIndustryLengthErrorMessage(),
	                "Industry is too long, max 150 characters allowed",
	                "Industry length validation message mismatch."
	        );
	    }
	    @Test(priority = 71)
	    public void TC_CL_71_verifyDuplicateClientValidation() {
	        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        addNewBatchPage.enterClientName("Test Client");
	        addNewBatchPage.enterIndustry("IT");
	        addNewBatchPage.clickAddButtonInPopup();

	        Assert.assertEquals(
	        		addNewBatchPage.getDuplicateClientErrorMessage(),
	                "This client already exists. Please add a client with a different name.",
	                "Duplicate client validation message mismatch."
	        );
	    }
	    @Test(priority = 72)
	    public void TC_CL_72_verifyLogoDeletePopupCancelKeepsLogo() {
	        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);


	        addNewBatchPage.enterClientName("Client " + System.currentTimeMillis());
	        addNewBatchPage.enterIndustry("IT");
	        addNewBatchPage.uploadLogo(System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\lotus.jpg");

	        Assert.assertTrue(addNewBatchPage.isLogoPreviewDisplayed(), "Uploaded logo preview is not displayed.");
	        Assert.assertTrue(addNewBatchPage.isRemoveLogoIconDisplayed(), "Remove logo icon is not displayed.");

	        addNewBatchPage.clickRemoveLogoIcon();

	        Assert.assertTrue(addNewBatchPage.isDeleteLogoConfirmationPopupDisplayed(),
	                "Delete logo confirmation popup is not displayed.");

	        addNewBatchPage.clickDeleteLogoCancelButton();

	        Assert.assertTrue(addNewBatchPage.isDeleteLogoConfirmationPopupClosed(),
	                "Delete logo confirmation popup is not closed after clicking Cancel.");

	        Assert.assertTrue(addNewBatchPage.isLogoPreviewDisplayed(),
	                "Logo should remain visible after clicking Cancel on delete popup.");
	    }
	    
	    @Test(priority = 73)
	    public void TC_CL_73_verifyLogoDeletePopupOkDeletesLogo() {
	        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        addNewBatchPage.enterClientName("Client " + System.currentTimeMillis());
	        addNewBatchPage.enterIndustry("IT");
	        addNewBatchPage.uploadLogo(System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\lotus.jpg");

	        Assert.assertTrue(addNewBatchPage.isLogoPreviewDisplayed(), "Uploaded logo preview is not displayed.");
	        Assert.assertTrue(addNewBatchPage.isRemoveLogoIconDisplayed(), "Remove logo icon is not displayed.");

	        addNewBatchPage.clickRemoveLogoIcon();

	        Assert.assertTrue(addNewBatchPage.isDeleteLogoConfirmationPopupDisplayed(),
	                "Delete logo confirmation popup is not displayed.");

	        addNewBatchPage.clickDeleteLogoOkButton();

	        Assert.assertEquals(
	        		addNewBatchPage.getLogoDeletedSuccessMessage(),
	                "Logo deleted successfully",
	                "Logo deleted success message mismatch."
	        );

	        Assert.assertTrue(addNewBatchPage.isLogoDeleted(),
	                "Logo is not deleted after clicking OK on delete popup.");
	    }
	    @Test(priority = 74)
	    public void TC_CL_74_verifyValidClientAddOrCaptureServerError() {
	        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        clientName = "Client " + System.currentTimeMillis();
	        industry = "IT Services";
	        String logoPath = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\lotus.jpg";

	        addNewBatchPage.addClient(clientName, industry, logoPath);
	        
	        addNewBatchPage.waitForProgramInfoOrServerError();

	        if (addNewBatchPage.isServerErrorDisplayed()) {
	            Assert.fail("Client not added. Server error message: " + addNewBatchPage.getServerErrorMessage());
	        }

	        Assert.assertTrue(addNewBatchPage.isProgramInfoTabDisplayed(), "Program Info tab is not displayed after adding client.");
	    }
	    @Test(priority = 75)
	    public void TC_CL_75_verifyFacilitatorCanCreateBatchUsingSaveAndExitInProgramInfoTab() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        // Click Save & Exit
	    	addNewBatchPage.clickProgramInfoSaveAndExit();
	        
	    	addNewBatchPage.waitForPageToLoad();

	        // Verify success message
	        Assert.assertTrue(
	        		addNewBatchPage.isBatchCreatedSuccessfullyMessageDisplayed(),
	                "When Click on Save And Exit Button Server Not responding error is displayed"
	        );
	        addNewBatchPage.waitForPageToLoad();
	        // Verify Planned tab is active
	        Assert.assertTrue(
	        		addNewBatchPage.waitForPlannedTabToBecomeActive(),
	        	    "Facilitator is not landed on Planned tab after clicking Save & Exit."
	        	);

	        // Optional: Verify created batch is present
	        Assert.assertTrue(
	        		addNewBatchPage.isBatchPresentInPlannedList(batchTitle),
	                "Created batch is not present in Planned tab list."
	        );
	    }
	    @Test(priority = 76)
	    public void TC_AB_FI_76_verifyFacilitatorLandsOnFacilitatorInfoPageAfterClickingSaveAndContinueInProgramInfoTab() {
	    	AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);
	    	addNewBatchPage.clickNewBatch();
	    	addNewBatchPage.clickAddManually();
	    	batchTitle = "Auto Batch " + System.currentTimeMillis();
	        String batchDescription = "This batch is created through automation using Save and Exit flow.";

	        // Enter valid batch details
	        addNewBatchPage.enterBatchTitle(batchTitle);
	        addNewBatchPage.enterDescription(batchDescription);

	        // Select activities
	        addNewBatchPage.selectAssignmentActivity();
	        addNewBatchPage.selectReferenceMaterialActivity();
	        
	        addNewBatchPage.clickSaveAndContinue();
	        
	     // Step 5: Wait for Program Info page
	        addNewBatchPage.waitForProgramInfoPageToLoad();
	        
	        // Step 6: Validate Program Info heading
	        Assert.assertTrue(
	        		addNewBatchPage.isProgramInfoHeadingDisplayed(),
	                "Program Info heading is not displayed after clicking Save & Continue."
	        );

	        // Step 7: Validate Program Info tab is active
	        Assert.assertTrue(
	        		addNewBatchPage.isProgramInfoTabActive(),
	                "Program Info tab is not active after clicking Save & Continue."
	        );
	        addProgramGenre = "Program Genre";
	    	addNewBatchPage.selectProgramGenre(addProgramGenre);

	    	newTitle = "Auto Program Title " + System.currentTimeMillis();

	        addNewBatchPage.addProgramTitle(newTitle);
	        addNewBatchPage.selectTargetAudience("Senior Mgmt");
	        addNewBatchPage.clickAddClientButton();
	        clientName = "Client " + System.currentTimeMillis();
	        industry = "IT Services";
	        String logoPath = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\lotus.jpg";

	        addNewBatchPage.addClient(clientName, industry, logoPath);
	        // click body or any safe location
	        driver.findElement(org.openqa.selenium.By.tagName("body")).click();
	    }
	    @Test(priority = 77)
	    public void TC_AB_FI_77_verifyFacilitatorLandsOnFacilitatorInfoAfterProgramInfoSaveAndContinue() {
	        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        addNewBatchPage.clickProgramInfoSaveAndContinue();
	        addNewBatchPage.waitForFacilitatorInfoPageToLoad();

	        Assert.assertTrue(addNewBatchPage.isFacilitatorInfoHeadingDisplayed(),
	                "Facilitator Info heading is not displayed after clicking Save & Continue on Program Info.");
	    }
	    @Test(priority = 78, dependsOnMethods = "TC_AB_FI_77_verifyFacilitatorLandsOnFacilitatorInfoAfterProgramInfoSaveAndContinue")
	    public void TC_AB_FI_78_verifyFacilitatorInfoTabStateAndNavigationState() {
	        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        Assert.assertTrue(addNewBatchPage.isFacilitatorInfoTabActive(), "Facilitator Info tab is not active.");
	        Assert.assertTrue(addNewBatchPage.isMessagesTabDisabled(), "Messages tab should be disabled.");
	        Assert.assertTrue(addNewBatchPage.isBatchAddedTabDisabled(), "Batch Added tab should be disabled.");
	        Assert.assertTrue(addNewBatchPage.isBasicSetupTabCompletedOrClickable(), "Basic Setup tab should be completed/clickable.");
	        Assert.assertTrue(addNewBatchPage.isProgramInfoTabCompletedOrClickable(), "Program Info tab should be completed/clickable.");

	        System.out.println("Facilitator Info tab color = " + addNewBatchPage.getFacilitatorInfoTabColorHex());
	    }
	    @Test(priority = 79, dependsOnMethods = "TC_AB_FI_77_verifyFacilitatorLandsOnFacilitatorInfoAfterProgramInfoSaveAndContinue")
	    public void TC_AB_FI_79_verifyDefaultFacilitatorRowAndControls() {
	        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        Assert.assertTrue(addNewBatchPage.getFacilitatorRowCount() >= 1, "At least one facilitator row should be present.");

	        storedFacilitatorName = addNewBatchPage.getFirstSelectedFacilitatorName();
	        storedFacilitatorType = addNewBatchPage.getFirstSelectedFacilitatorType();

	        Assert.assertFalse(storedFacilitatorName.isBlank(), "Default facilitator name should be selected.");
	        Assert.assertFalse(storedFacilitatorType.isBlank(), "Default facilitator type should be selected.");

	        Assert.assertTrue(addNewBatchPage.isFirstFacilitatorNameDropdownDisabled(), "Default facilitator name dropdown should be disabled.");
	        Assert.assertTrue(addNewBatchPage.isFirstFacilitatorTypeDropdownDisabled(), "Default facilitator type dropdown should be disabled.");

	        Assert.assertTrue(addNewBatchPage.isFirstBatchPrivilegesIconDisplayed(), "Batch Privileges icon is not displayed.");
	        Assert.assertTrue(addNewBatchPage.isFirstAddFacilitatorIconDisplayed(), "Add Facilitator icon is not displayed.");
	        Assert.assertTrue(addNewBatchPage.isFirstDeleteFacilitatorIconDisplayed(), "Delete Facilitator icon is not displayed.");
	    }
	    @Test(priority = 80)
	    public void TC_AB_FI_80_verifyHelpTextsAndSendEmailFromDefaultSelection() {
	        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        Assert.assertTrue(
	        		addNewBatchPage.getTypeHelpText().contains("Role of the Facilitator for this program batch"),
	                "Type help text is incorrect."
	        );

	        Assert.assertTrue(
	        		addNewBatchPage.getSendEmailFromHelpText().contains("Email ID used for sending mails to Pax"),
	                "Send Email From help text is incorrect."
	        );

	        storedEmailFrom = addNewBatchPage.getSelectedSendEmailFrom();
	        Assert.assertFalse(storedEmailFrom.isBlank(), "Send Email From should have a default selected email.");

	        Assert.assertTrue(addNewBatchPage.isFacilitatorInfoCancelButtonDisplayed(), "Cancel button is not displayed.");
	        Assert.assertTrue(addNewBatchPage.isFacilitatorInfoSaveAndExitButtonDisplayed(), "Save & Exit button is not displayed.");
	        Assert.assertTrue(addNewBatchPage.isFacilitatorInfoSaveAndContinueButtonDisplayed(), "Save & Continue button is not displayed.");
	    }
	    @Test(priority = 81)
	    public void TC_AB_FI_81_verifyBatchPrivilegesPopupOpensAndTitleContainsFacilitatorName() {
	        AddNewBatchPage addNewBatchPage = new AddNewBatchPage(driver);

	        addNewBatchPage.openFirstFacilitatorPrivilegesPopup();

	        Assert.assertTrue(addNewBatchPage.isPrivilegesModalDisplayed(), "Privileges popup is not displayed.");
	        Assert.assertTrue(addNewBatchPage.isPrivilegesModalTitleContains(storedFacilitatorName),
	                "Privileges popup title does not contain facilitator name. Actual title: " + addNewBatchPage.getPrivilegesModalTitle());
	        Assert.assertTrue(addNewBatchPage.arePrivilegesPopupButtonsDisplayed(), "Close/Update buttons are not displayed in privileges popup.");

	        addNewBatchPage.closePrivilegesPopupFromX();
	    }
	    @Test(priority = 82)
	    public void TC_AB_FI_82_verifyNonEditableCrossMarkPrivilegesAreDisplayed() {
	        AddNewBatchPage page = new AddNewBatchPage(driver);
	        page.openFirstFacilitatorPrivilegesPopup();
	        Assert.assertTrue(
	                page.validateNonEditableCrossIconCounts(),
	                "Non-editable privilege X-mark counts are incorrect. " +
	                "Batch=" + page.getCrossIconCountForPrivilegeRow("Batch") +
	                ", Facilitator=" + page.getCrossIconCountForPrivilegeRow("Facilitator") +
	                ", Pax=" + page.getCrossIconCountForPrivilegeRow("Pax") +
	                ", Activity=" + page.getCrossIconCountForPrivilegeRow("Activity")
	        );
	    }
	    
	    @Test(priority = 83)
	    public void TC_AB_FI_83_verifyAddAllBulkCheckboxControlsOnlyEditableAddColumnPrivileges() {
	        AddNewBatchPage page = new AddNewBatchPage(driver);

	        page.verifyBulkColumnCheckboxBehavior(
	                By.id("add_All"),
	                page.getEditableAddColumnCheckboxes()
	        );
	    }
	    @Test(priority = 84)
	    public void TC_AB_FI_84_verifyEditAllBulkCheckboxControlsOnlyEditableEditColumnPrivileges() {
	        AddNewBatchPage page = new AddNewBatchPage(driver);

	        page.verifyBulkColumnCheckboxBehavior(
	                By.id("edit_All"),
	                page.getEditableEditColumnCheckboxes()
	        );
	    }
	    @Test(priority = 85)
	    public void TC_AB_FI_85_verifyDeleteAllBulkCheckboxControlsOnlyEditableDeleteColumnPrivileges() {
	        AddNewBatchPage page = new AddNewBatchPage(driver);

	        page.verifyBulkColumnCheckboxBehavior(
	                By.id("delete_All"),
	                page.getEditableDeleteColumnCheckboxes()
	        );
	    }
	    @Test(priority = 87)
	    public void TC_AB_FI_87_verifyViewAllBulkCheckboxControlsOnlyEditableViewColumnPrivileges() {
	        AddNewBatchPage page = new AddNewBatchPage(driver);

	        page.verifyBulkColumnCheckboxBehavior(
	                By.id("view_All"),
	                page.getEditableViewColumnCheckboxes()
	        );
	    }
	    @Test(priority = 88)
	    public void TC_AB_FI_88_verifyCloseAllBulkCheckboxControlsOnlyEditableCloseColumnPrivileges() {
	        AddNewBatchPage page = new AddNewBatchPage(driver);

	        page.verifyBulkColumnCheckboxBehavior(
	                By.id("close_All"),
	                page.getEditableCloseColumnCheckboxes()
	        );
	    }
	    @Test(priority = 89)
	    public void TC_AB_FI_89_verifyInviteAllBulkCheckboxControlsOnlyEditableInviteColumnPrivileges() {
	        AddNewBatchPage page = new AddNewBatchPage(driver);

	        page.verifyBulkColumnCheckboxBehavior(
	                By.id("invite_all"),
	                page.getEditableInviteColumnCheckboxes()
	        );
	    }
	    @Test(priority = 90)
	    public void TC_AB_FI_90_verifyPrivilegesArePersistedAfterUpdate() {
	        AddNewBatchPage page = new AddNewBatchPage(driver);

	        // Make some changes
	        page.setCheckbox(By.id("add_All"), false);
	        page.setCheckbox(By.id("edit_All"), false);
	        page.setCheckbox(By.id("delete_All"), true);
	        page.setCheckbox(By.id("view_All"), true);
	        page.setCheckbox(By.id("publish_activity"), true);
	        page.setCheckbox(By.id("reminder"), true);
	        page.setCheckbox(By.id("view_response"), true);
	        page.setCheckbox(By.id("reset_response"), true);
	        page.setCheckbox(By.id("comment_categorization"), true);
	        page.setCheckbox(By.id("add_inbox"), true);

	        privilegeSnapshot = page.capturePrivilegeStateSnapshot();
	        page.clickPrivilegesUpdate();

	        page.openFirstFacilitatorPrivilegesPopup();
	        List<Boolean> actualSnapshot = page.capturePrivilegeStateSnapshot();

	        Assert.assertEquals(
	                actualSnapshot,
	                privilegeSnapshot,
	                "Privileges are not persisted after clicking Update."
	        );

	        page.closePrivilegesPopupFromX();
	    }
	    @Test(priority = 91)
	    public void TC_AB_FI_91_verifyFacilitatorCanBeAddedMultipleTimes() {
	        AddNewBatchPage page = new AddNewBatchPage(driver);
	        page.waitForFacilitatorInfoTabToBeVisible();
	        int initialCount = page.getFacilitatorinfoRowCount();
	        int timesToAdd = 3;

	        page.addFacilitatorRowsMultipleTimes(timesToAdd);

	        int finalCount = page.getFacilitatorRowCount();

	        Assert.assertEquals(
	                finalCount,
	                initialCount + timesToAdd,
	                "Facilitator rows were not added correctly multiple times."
	        );

	        for (int row = initialCount + 1; row <= finalCount; row++) {
	            Assert.assertTrue(
	                    page.isFacilitatorNameDropdownDisplayed(row),
	                    "Facilitator Name dropdown is not displayed for row: " + row
	            );

	            Assert.assertTrue(
	                    page.isFacilitatorTypeDropdownDisplayed(row),
	                    "Facilitator Type dropdown is not displayed for row: " + row
	            );
	        }
	    }
	    @Test(priority = 92, dependsOnMethods = "TC_AB_FI_91_verifyFacilitatorCanBeAddedMultipleTimes")
	    public void TC_AB_FI_92_verifyFacilitatorCanBeDeletedMultipleTimes() {
	        AddNewBatchPage page = new AddNewBatchPage(driver);

	        int initialCount = page.getFacilitatorRowCount();
	        Assert.assertTrue(initialCount > 1, "At least two facilitator rows are required for multiple delete validation.");

	        while (page.getFacilitatorRowCount() > 1) {
	            int beforeCount = page.getFacilitatorRowCount();

	            page.clickDeleteFacilitatorIconByRow(beforeCount);

	            Assert.assertTrue(
	                    page.isDeleteConfirmationPopupDisplayed(),
	                    "Delete confirmation popup is not displayed for row count: " + beforeCount
	            );

	            page.confirmDeleteFacilitator();

	            int afterCount = page.getFacilitatorRowCount();

	            Assert.assertEquals(
	                    afterCount,
	                    beforeCount - 1,
	                    "Facilitator row was not deleted correctly. Before count = "
	                            + beforeCount + ", After count = " + afterCount
	            );
	        }

	        Assert.assertEquals(
	                page.getFacilitatorRowCount(),
	                1,
	                "Only one facilitator row should remain after deleting multiple rows."
	        );
	    }
	    @Test(priority = 93, dependsOnMethods = "TC_AB_FI_92_verifyFacilitatorCanBeDeletedMultipleTimes")
	    public void TC_AB_FI_93_verifyLastRemainingFacilitatorCannotBeDeleted() {
	        AddNewBatchPage page = new AddNewBatchPage(driver);

	        Assert.assertEquals(
	                page.getFacilitatorRowCount(),
	                1,
	                "Exactly one facilitator row should be present before validating minimum facilitator restriction."
	        );

	        page.clickDeleteFacilitatorIconByRow(1);

	        Assert.assertTrue(
	                page.isMinimumFacilitatorValidationDisplayed(),
	                "Validation message was not displayed when trying to delete the last remaining facilitator."
	        );
	    }
	    @Test(priority = 94)
	    public void TC_AB_FI_94_verifyDeletePopupCancelKeepsFacilitatorRow() {
	        AddNewBatchPage page = new AddNewBatchPage(driver);

	        // Ensure at least one extra row exists
	        if (page.getFacilitatorRowCount() < 2) {
	            page.addFacilitatorRowFromLastRow();
	        }

	        int beforeCount = page.getFacilitatorRowCount();

	        page.clickDeleteFacilitatorIconByRow(beforeCount);

	        Assert.assertTrue(
	                page.isDeleteConfirmationPopupDisplayed(),
	                "Delete confirmation popup is not displayed."
	        );

	        page.cancelDeleteFacilitator();

	        int afterCount = page.getFacilitatorRowCount();

	        Assert.assertEquals(
	                afterCount,
	                beforeCount,
	                "Facilitator row count changed after clicking Cancel in delete confirmation popup."
	        );
	    }

}

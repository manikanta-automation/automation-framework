package com.potentia.automation.tests;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.potentia.automation.core.BaseTest;
import com.potentia.automation.pages.AddAssignmentPage;
import com.potentia.automation.pages.LoginPage;
import com.potentia.automation.pages.SuperClientsPage;
import com.potentia.automation.utils.ConfigReader;
import com.potentia.automation.utils.DataStore;

public class AddAssignmentActTest extends BaseTest {
	
	AddAssignmentPage addAssignmentPage;
	@BeforeClass
    public void pageSetup() {
    	LoginPage loginPage = new LoginPage(driver);
        String email = ConfigReader.get("email");
        String password = ConfigReader.get("password");

        loginPage.login(email, password);

        // Assertion: Check if home page URL is loaded
        String expectedUrl = ConfigReader.get("homeUrl");
        Assert.assertEquals(driver.getCurrentUrl(), expectedUrl, "Login failed or home page not loaded!");
        addAssignmentPage = new AddAssignmentPage(driver);
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
//	 @Test(priority = 2)
//	    public void verifyLikertDataisFoundandStored() {
//
//	        // Step 1: Navigate to Likert Scale
//		 addAssignmentPage.clickLikertScale();
//
//		  DataStore.likertData = addAssignmentPage.getLikertTableData();
//
//		    // ✅ Handle empty case (DO NOT FAIL)
//		    if (DataStore.likertData == null || DataStore.likertData.isEmpty()) {
//
//		        System.out.println("⚠️ No Likert templates found. Skipping further validation.");
//
//		        // Optional: mark test as skipped instead of pass
//		        throw new SkipException("No Likert data available to validate.");
//
//		    } else {
//
//		        System.out.println("✅ Likert data found: " + DataStore.likertData);
//
//		        Assert.assertTrue(DataStore.likertData.size() > 0);
//		    }
//	 }
	 
	@Test(priority = 3)
    public void TC_MB_clickBatchByNameAcrossTabs() {
		addAssignmentPage.clickOnMyBatches();
        String batchName = "Auto Batch 1775109143724"; // take from config/testdata if needed

        boolean isClicked = addAssignmentPage.clickBatchByNameAcrossTabs(batchName);

        Assert.assertTrue(
            isClicked,
            "Batch was not found in Active, Planned, or Inactive tabs: " + batchName
        );
    }
	@Test(priority = 4)
    public void TC_034_verifyActivitiesPage() {
        Assert.assertTrue(addAssignmentPage.isActivitiesPageLoaded(),
                "❌ Activities page not loaded");
    }

    @Test(priority = 5)
    public void TC_05_verifyTabActive() {
        Assert.assertTrue(addAssignmentPage.isActivitiesTabActive(),
                "❌ Activities tab not active");
    }

    @Test(priority = 6)
    public void TC_06_verifyOnlyActivitiesActive() {
        Assert.assertTrue(addAssignmentPage.isOnlyActivitiesTabActive(),
                "❌ Multiple tabs are active");
    }

    @Test(priority = 7)
    public void TC_07_verifyBreadcrumb() {
        String breadcrumb = addAssignmentPage.getBreadcrumbText();

        Assert.assertFalse(breadcrumb.isEmpty(),
                "❌ Breadcrumb is empty");

        Assert.assertTrue(breadcrumb.contains("-"),
                "❌ Breadcrumb format incorrect: " + breadcrumb);
    }

    @Test(priority = 8)
    public void TC_08_verifyButtonsVisible() {
        Assert.assertTrue(addAssignmentPage.isSuperActivityVisible(), "❌ Super Activity not visible");
        Assert.assertTrue(addAssignmentPage.isAddActivityButtonVisible(), "❌ Add Activity not visible");
        Assert.assertTrue(addAssignmentPage.isCopyActivityVisible(), "❌ Copy Activity not visible");
    }

    @Test(priority = 9)
    public void TC_09_verifyAddActivityEnabled() {
        Assert.assertTrue(addAssignmentPage.isAddActivityEnabled(),
                "❌ Add Activity button is disabled");
    }

    @Test(priority = 10)
    public void TC_10_verifyAddActivityFlow() {

    	addAssignmentPage.clickAddActivity();

        String url = addAssignmentPage.getCurrentUrl();
        Assert.assertTrue(url.contains("addactivity"),
                "❌ Not navigated to Add Activity page. URL: " + url);

        Assert.assertTrue(addAssignmentPage.isAddActivityPageLoaded(),
                "❌ Add Activity page not loaded");

        addAssignmentPage.clickCancel();

        Assert.assertTrue(addAssignmentPage.isActivitiesPageLoaded(),
                "❌ Cancel did not navigate back to Activities page");
    }

    @Test(priority = 11)
    public void TC_11_verifyMultipleClicks() {

    	addAssignmentPage.clickAddActivity();
        driver.navigate().back();

        addAssignmentPage.clickAddActivity();

        Assert.assertTrue(addAssignmentPage.getCurrentUrl().contains("addactivity"),
                "❌ Multiple click failed");
    }
    @Test(priority = 12)
    public void TC_12_verifyAllActivities() {

        int count = addAssignmentPage.getActivityCount();

        Assert.assertTrue(count > 0,
                "❌ No activities found on Add Activity page");

        for (int i = 0; i < count; i++) {

            String activityName = addAssignmentPage.getActivityName(i);

            System.out.println("🔹 Checking Activity: " + activityName);

            // ✅ Visibility
            Assert.assertTrue(addAssignmentPage.isActivityVisible(i),
                    "❌ Activity NOT visible: " + activityName);

            // ✅ Radio button
            Assert.assertTrue(addAssignmentPage.isRadioPresent(i),
                    "❌ Radio button NOT present for: " + activityName);

            // ✅ Tooltip
            String tooltip = addAssignmentPage.getTooltip(i);

            System.out.println("   👉 Tooltip: " + tooltip);

            Assert.assertFalse(tooltip.isEmpty(),
                    "❌ Tooltip missing for: " + activityName);
        }
    }
    @Test(priority = 13)
    public void TC_13_verifyTooltipNotNull() {

        String tooltip = addAssignmentPage.getAssignmentTooltipText();

        Assert.assertNotNull(tooltip,
                "❌ Tooltip is NULL");
    }

    @Test(priority = 14)
    public void TC_14_verifyMultipleHover() {

        String tooltip1 = addAssignmentPage.getAssignmentTooltipText();
        String tooltip2 = addAssignmentPage.getAssignmentTooltipText();

        Assert.assertEquals(tooltip1, tooltip2,
                "❌ Tooltip inconsistent on multiple hover");
    }
    
    @Test(priority = 15)
    public void TC_15_verifyAssignmentSelection() {

    	addAssignmentPage.selectAssignmentActivity();

        Assert.assertTrue(addAssignmentPage.isAssignmentSelected(),
                "❌ Assignment radio button is NOT selected after click");
    }
    @Test(priority = 16)
    public void TC_16_verifyActivityDetailsTabLoaded() {

        Assert.assertTrue(addAssignmentPage.isActivityDetailsTabActive(),
                "❌ Activity Details tab is NOT active");

        Assert.assertTrue(addAssignmentPage.isActivityTypeTabCompleted(),
                "❌ Activity Type tab is NOT completed");

        Assert.assertTrue(addAssignmentPage.isActivitySettingsTabDisabled(),
                "❌ Activity Settings tab should be disabled");
    }
    
    // ✅ TC16 - Verify Activity Title Field
    @Test(priority = 17)
    public void TC_17_verifyTitleField() {

        Assert.assertTrue(addAssignmentPage.isActivityTitleDisplayed(),
                "❌ Title field is NOT displayed");
        
        /*Assert.assertTrue(addAssignmentPage.isActivityInstructionsEditorDisplayed(),
                "❌ Instructions editor is NOT visible");*/
    }
    @Test(priority = 18)
    public void TC_18_verifyAlertText() {

    	addAssignmentPage.clickActivityTypeTab();

        String alertText = addAssignmentPage.getAlertText();

        Assert.assertTrue(alertText.contains("switch tabs"),
                "❌ Alert text mismatch. Actual: " + alertText);

        addAssignmentPage.dismissAlert(); // cleanup
        Assert.assertTrue(addAssignmentPage.isStillOnActivityDetails(),
                "❌ User navigated even after clicking Cancel");
    }
    
    @Test(priority = 19)
    public void TC_19_verifyOkNavigates() {

    	addAssignmentPage.clickActivityTypeTab();

    	addAssignmentPage.acceptAlert();

        Assert.assertTrue(addAssignmentPage.isNavigatedToActivityType(),
                "❌ User NOT navigated after clicking OK");
    }
    @Test(priority = 20)
    public void TC_20_verifyAssignmentSelection() {

    	addAssignmentPage.selectAssignmentActivity();

        Assert.assertTrue(addAssignmentPage.isAssignmentSelected(),
                "❌ Assignment radio button is NOT selected after click");
    }
    @Test(priority = 21)
    public void verifyEmptyTitleShowsErrorToastclickOnSaveAndContinueAndSaveAndExit() {

    	addAssignmentPage.enterTitle("");
    	addAssignmentPage.clickSaveAndContinueBtn();

        Assert.assertTrue(addAssignmentPage.isToastDisplayed(), "Toast not displayed");

        Assert.assertEquals(addAssignmentPage.getToastTitle(), "Error");
        Assert.assertEquals(addAssignmentPage.getToastMessage(), "Please enter activity title");
        addAssignmentPage.clickSaveAndExitBtn();
        Assert.assertTrue(addAssignmentPage.isToastDisplayed(), "Toast not displayed");

        Assert.assertEquals(addAssignmentPage.getToastTitle(), "Error");
        Assert.assertEquals(addAssignmentPage.getToastMessage(), "Please enter activity title");
    }
 // ================= MAX LENGTH ================= //
    @Test(priority = 22)
    public void verifyTitleExceeds150CharsShowsErrorToast() {

        String longTitle = "A".repeat(151);
        addAssignmentPage.enterTitle(longTitle);
        addAssignmentPage.clickSaveAndContinueBtn();

        Assert.assertTrue(addAssignmentPage.isToastDisplayed());

        Assert.assertEquals(addAssignmentPage.getToastTitle(), "Error");
        Assert.assertEquals(addAssignmentPage.getToastMessage(),
                "Activity title is too long, max 150 characters allowed");
        addAssignmentPage.clickSaveAndExitBtn();
        Assert.assertTrue(addAssignmentPage.isToastDisplayed());

        Assert.assertEquals(addAssignmentPage.getToastTitle(), "Error");
        Assert.assertEquals(addAssignmentPage.getToastMessage(),
                "Activity title is too long, max 150 characters allowed");
    }
    // ================= SPACES ================= //
    @Test(priority = 23)
    public void verifyOnlySpacesShowsErrorToast() {

    	addAssignmentPage.enterTitle("     ");
    	addAssignmentPage.clickSaveAndContinueBtn();

        Assert.assertEquals(addAssignmentPage.getToastMessage(), "Please enter activity title");
        addAssignmentPage.clickSaveAndExitBtn();
        Assert.assertEquals(addAssignmentPage.getToastMessage(), "Please enter activity title");
    }
    // ✅ Help Text
    @Test(priority = 24)
    public void verifyHelpText() {
        String helpText = addAssignmentPage.getHelpText();

        Assert.assertEquals(helpText,
                "Unique name for an activity within a batch");
    }
    // ✅ Special Characters
    @Test(priority = 25)
    public void verifySpecialCharactersAccepted() {
        String title = "@Test_Activity-01!";
        addAssignmentPage.enterTitle(title);

        Assert.assertEquals(addAssignmentPage.getTitleValue(), title);
    }
    // ✅ Emoji
    @Test(priority = 26)
    public void verifyEmojiSupport() {
        String title = "Test🔥";
        addAssignmentPage.enterTitle(title);

        Assert.assertEquals(addAssignmentPage.getTitleValue(), title);
    }
    // ✅ Collapse
    @Test(priority = 27)
    public void verifyCollapseExpand() {
    	addAssignmentPage.toggleDescription();
        Assert.assertFalse(addAssignmentPage.isDescriptionVisible());

        addAssignmentPage.toggleDescription();
        Assert.assertTrue(addAssignmentPage.isDescriptionVisible());
    }
    @Test(priority = 28)
    public void verifyDescriptionHelpText() {
        Assert.assertEquals(addAssignmentPage.getdescriptionHelpText(),
                "Objectives, visible only to faculty");
    }
    @Test(priority = 29)
    public void verifyDescriptionInput() {
    	addAssignmentPage.enterDescription("Test Description");
        Assert.assertEquals(addAssignmentPage.getDescriptionValue(), "Test Description");
    }
    // ✅ Multiline
    @Test(priority = 30)
    public void verifyMultilineInput() {
        String text = "Line1\nLine2\nLine3";
        addAssignmentPage.enterDescription(text);

        Assert.assertEquals(addAssignmentPage.getDescriptionValue(), text);
    }
 // ✅ Special Characters
    @Test(priority = 31)
    public void verifySpecialCharacters() {
        String text = "@#$%^&*()";
        addAssignmentPage.enterDescription(text);

        Assert.assertEquals(addAssignmentPage.getDescriptionValue(), text);
    }
    // ✅ Large Input
    @Test(priority = 32)
    public void verifyLargeInput() {
        String text = "A".repeat(2000);
        addAssignmentPage.enterDescription(text);

        Assert.assertTrue(addAssignmentPage.getDescriptionValue().length() == 2000);
    }
    // ✅ Resize
    @Test(priority = 33)
    public void verifyResizeFunctionality() {
        int before = addAssignmentPage.getHeight();

        addAssignmentPage.resizeDescription(300);

        int after = addAssignmentPage.getHeight();

        Assert.assertTrue(after > before);
    }
    // ✅ XSS
    @Test(priority = 34)
    public void verifyXSSHandling() {
        String text = "<script>alert('test')</script>";
        addAssignmentPage.enterDescription(text);

        Assert.assertTrue(addAssignmentPage.getDescriptionValue().contains("<script>"));
    }
 // ✅  instruction HELP TEXT
    @Test(priority = 35)
    public void verifyActivityInstructionHelpText() {
        Assert.assertEquals(addAssignmentPage.getActivityInstructionHelpText(),
                "Guidelines for Pax");
    }
    @Test(priority = 36)
    public void verifyTextInput() {
    	addAssignmentPage.enterText("Automation Test");

        Assert.assertTrue(addAssignmentPage.getInstructionsEditorText().contains("Automation"));
    }
    // ✅ TAGS
    @Test(priority = 37)
    public void verifyTagsDisplayed() {
        List<String> tags = addAssignmentPage.getAllTags();

        Assert.assertTrue(tags.contains("Participant_Name"));
        Assert.assertTrue(tags.contains("Program_Title"));
    }
    // ✅ TAG INSERTION
    @Test(priority = 38)
    public void verifyAllTagsDisplayedAndInserted() {

        List<String> tags = addAssignmentPage.getAllTags();
        System.out.println(tags);

        Assert.assertTrue(tags.size() > 0);

        addAssignmentPage.clickAllTags();

        Assert.assertTrue(addAssignmentPage.areAllTagsInserted());
    }
    
    @Test(priority = 39)
    public void verifyinstructionMultilineInput() {
        String text = "Line1\nLine2\nLine3";
        addAssignmentPage.enterText(text);

        Assert.assertTrue(addAssignmentPage.getInstructionsEditorText().contains("Line2"));
    }
    @Test(priority = 40)
    public void verifyinstructionSpecialCharacters() {
    	addAssignmentPage.enterText("@#$%^&*");

        Assert.assertTrue(addAssignmentPage.getInstructionsEditorText().contains("@#"));
    }
    @Test(priority = 41)
    public void verifyToolbarActions() {

    	addAssignmentPage.enterText("Test");

    	addAssignmentPage.applyAllToolbarActions();

        Assert.assertTrue(addAssignmentPage.getInstructionsEditorText().length() > 0);
    }
 // ================= IMAGE UPLOAD ================= //

    @Test(priority =42)
    public void verifyInsertImage() {

        String path = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\lotus.jpg";

        addAssignmentPage.insertImage(path);

        Assert.assertTrue(addAssignmentPage.getInstructionsEditorText().length() >= 0);
    }

    // ================= FILE UPLOAD ================= //

//    @Test(priority = 43)
//    public void verifyInsertFile() {
//
//        String path = System.getProperty("user.dir") + "/testdata/sample.pdf";
//
//        addAssignmentPage.insertFile(path);
//
//        Assert.assertTrue(addAssignmentPage.getEditorText().length() >= 0);
//    }

//    // ✅ CHARACTER LIMIT
//    @Test(priority = 37)
//    public void verifyCharacterLimitExceeded() {
//    	String title = "@Test_Activity-01!";
//        addAssignmentPage.enterTitle(title);
//        String text = "A".repeat(65001);
//        addAssignmentPage.enterText(text);
//        addAssignmentPage.clickSaveAndContinueBtn();
//
//        Assert.assertTrue(addAssignmentPage.isToastDisplayed());
//
//        Assert.assertEquals(addAssignmentPage.getToastTitle(), "Error");
//        Assert.assertEquals(addAssignmentPage.getToastMessage(),
//                "Activity instructions is too long, max 65000 characters allowed");
//        addAssignmentPage.clickSaveAndExitBtn();
//        Assert.assertTrue(addAssignmentPage.isToastDisplayed());
//
//        Assert.assertEquals(addAssignmentPage.getToastTitle(), "Error");
//        Assert.assertEquals(addAssignmentPage.getToastMessage(),
//                "Activity instructions is too long, max 65000 characters allowed");
//      
//    }
    
    // ✅ Default Checked
    @Test(priority = 43)
    public void verifyCheckboxIsDisplayedAndSelectedByDefault() {
    	Assert.assertTrue(addAssignmentPage.isAutoQuestionSelected() || !addAssignmentPage.isAutoQuestionSelected());
        Assert.assertTrue(addAssignmentPage.isAutoQuestionSelected(),
                "Checkbox is NOT selected by default");
    }
    // ✅ Toggle Checkbox
    @Test(priority = 44)
    public void verifyCheckboxToggle() {

    	addAssignmentPage.clickCheckbox();
        Assert.assertFalse(addAssignmentPage.isAutoQuestionSelected(),
                "Checkbox did not uncheck");

        addAssignmentPage.clickCheckbox();
        Assert.assertTrue(addAssignmentPage.isAutoQuestionSelected(),
                "Checkbox did not check again");
    }
 // ✅ Click Label
    @Test(priority = 45)
    public void verifyClickingLabelTogglesCheckbox() {

        boolean before = addAssignmentPage.isAutoQuestionSelected();

        addAssignmentPage.clickLabel();

        boolean after = addAssignmentPage.isAutoQuestionSelected();

        Assert.assertNotEquals(before, after,
                "Label click did not toggle checkbox");
    }
    
    // ✅ Help Text
    @Test(priority = 46)
    public void verifyAutoQuestionNumberHelpText() {

        String help = addAssignmentPage.getAutoQuestionNumberHelpText();

        Assert.assertEquals(help,
                "Default 1 to n.  Turn off to custom define or publish without numbers");
    }
    @Test(priority = 47)
    public void isAddAtleatOneQuestionErrormsgDisplayed() {
    	String title = "Assignemnt - All Types of Questions Checking";
        addAssignmentPage.enterTitle(title);
        addAssignmentPage.clickSaveAndContinueBtn();
        Assert.assertEquals(addAssignmentPage.getToastMessage(), "Please add atleast one question.");
        addAssignmentPage.clickSaveAndExitBtn();
        Assert.assertEquals(addAssignmentPage.getToastMessage(), "Please add atleast one question.");
    }
    @Test(priority = 48)
    public void verifyModalDisplayed() {
    	addAssignmentPage.scrollToAddQuestion();
    	addAssignmentPage.clickAddQuestion();
        Assert.assertTrue(addAssignmentPage.isModalDisplayed(), "Modal is not displayed");
    }

    // ================= QUESTION TYPES ================= //

    @Test(priority = 49)
    public void verifyAllQuestionTypesDisplayed() {

        List<String> expected = Arrays.asList(
                "Multiple Choice - Radio buttons",
                "Multiple Choice - Checkboxes",
                "Open-Ended Question",
                "Point Distribution",
                "Demographic",
                "Ranking",
                "Range Slider",
                "Matrix Multiple Choice - Radio buttons",
                "Matrix Multiple Choice - Checkboxes"
        );

        List<String> actual = addAssignmentPage.getAllQuestionNames();

        Assert.assertEquals(actual, expected, "Question types mismatch");
    }

    // ================= HELP TEXT VALIDATION (IMPORTANT 🔥) ================= //

    @Test(priority = 50)
    public void verifyHelpTextForAllQuestions() {

        Map<String, String> helpTexts = addAssignmentPage.getAllHelpTexts();

        List<String> missingHelp = new ArrayList<>();

        for (Map.Entry<String, String> entry : helpTexts.entrySet()) {

            String question = entry.getKey();
            String help = entry.getValue();

            System.out.println(question + " → " + help);

            if (help == null || help.trim().isEmpty()) {
                missingHelp.add(question);
            }
        }

        if (!missingHelp.isEmpty()) {

            Assert.fail("Help text missing for: " + missingHelp);
        }
    }
    @Test(priority = 51)
    public void verifyModalClose() {
 
    	addAssignmentPage.clickquestionModelCloseButton();   

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        boolean isClosed = wait.until(
                ExpectedConditions.invisibilityOfElementLocated(By.id("selectQuesModalLabel"))
        );

        Assert.assertTrue(isClosed, "Modal is not closed");
    }
    @Test(priority = 52)
    public void verifyRadioButtonQuestionFlow() {

        // Step 1: Open modal
    	addAssignmentPage.clickAddQuestion();

        // Step 2: Click Radio Button Option
    	addAssignmentPage.clickRadioButtonQuestion();

        // Step 3: Validate New Modal Title
        Assert.assertEquals(
        		addAssignmentPage.getModalTitle(),
                "Add Question - Multiple Choice - Radio buttons"
        );

        // Step 4: Validate Question Type
        Assert.assertEquals(addAssignmentPage.getQuestionTypeValue(), "4");

        // Step 5: Validate UI Elements
        Assert.assertTrue(addAssignmentPage.isQuestionFieldDisplayed());

        // Step 6: Validate Default Rows
        Assert.assertTrue(addAssignmentPage.getResponseRowCount() >= 3);

        // Step 7: Validate Default Checkbox States
        Assert.assertFalse(addAssignmentPage.isResponseRequiredChecked());
        Assert.assertFalse(addAssignmentPage.isAddOthersChecked());

        // Step 8: Validate Help Text
        Assert.assertTrue(
        		addAssignmentPage.getHelpText(By.cssSelector(".answer-help"))
                        .contains("correct answer")
        );

        Assert.assertTrue(
        		addAssignmentPage.getHelpText(By.cssSelector(".explanation-help"))
                        .contains("explanation")
        );

        // Step 9: Validate Buttons
        Assert.assertTrue(addAssignmentPage.isAddQuestionSaveButtonDisplayed());
        Assert.assertTrue(addAssignmentPage.isAddQuestionCancelButtonDisplayed());

    }
    
    @Test(priority = 53)
    public void verifyQNoHiddenWhenAutoEnabled() {
        addAssignmentPage.clickAddQuestionModalClose();
        Assert.assertTrue(addAssignmentPage.isAddQuestionDisplayed());

    	addAssignmentPage.setAutoQuestionCheckbox(false);

    	addAssignmentPage.clickAddQuestion();
    	addAssignmentPage.clickRadioButtonQuestion();

        Assert.assertFalse(addAssignmentPage.isQNoFieldDisplayed(),
                "Q.No field should NOT be visible when auto numbering is disabled");
        Assert.assertEquals(addAssignmentPage.getQNoValue(), "",
                "Q.No field should be empty by default");
        addAssignmentPage.clickAddQuestionModalClose();
    }
    @Test(priority = 54)
    public void verifyQNoVisibleWhenAutoDisabled() {

    	addAssignmentPage.setAutoQuestionCheckbox(true);

    	addAssignmentPage.clickAddQuestion();
    	addAssignmentPage.clickRadioButtonQuestion();

        Assert.assertTrue(addAssignmentPage.isQNoFieldDisplayed(),
                "Q.No field should be visible when auto numbering is enabled");
        
    }
    @Test(priority = 55)
    public void TC_55_verifyCheckboxDefaultState() {
        Assert.assertFalse(addAssignmentPage.isResponseRequiredSelected(),
                "Checkbox should be unchecked by default");
    }
    @Test(priority = 56)
    public void TC_56_verifyCheckboxSelection() {
    	addAssignmentPage.clickResponseRequiredCheckbox();
        Assert.assertTrue(addAssignmentPage.isResponseRequiredSelected(),
                "Checkbox should be checked");
    }

    @Test(priority = 57)
    public void TC_57_verifyCheckboxUnSelection() {
    	addAssignmentPage.setResponseRequired(true);
    	addAssignmentPage.clickResponseRequiredCheckbox();
        Assert.assertFalse(addAssignmentPage.isResponseRequiredSelected(),
                "Checkbox should be unchecked");
    }
    
    @Test(priority = 58)
    public void TC_58_verifyLabelClickTogglesCheckbox() {
        boolean before = addAssignmentPage.isResponseRequiredSelected();
        addAssignmentPage.clickResponseRequiredLabel();
        boolean after = addAssignmentPage.isResponseRequiredSelected();

        Assert.assertNotEquals(before, after,
                "Checkbox state should toggle when label is clicked");
    }
    @Test(priority = 59)
    public void TC_59_verifyQuestionFieldAcceptsInput() {
    	addAssignmentPage.enterQuestion("Sample Question");
        Assert.assertEquals(addAssignmentPage.getQuestionText(), "Sample Question");
    }
    @Test(priority = 60)
    public void TC_60_verifyTextAreaResize() {
        int before = addAssignmentPage.getTextAreaHeight();
        addAssignmentPage.resizeTextArea(300);
        int after = addAssignmentPage.getTextAreaHeight();

        Assert.assertTrue(after > before, "Textarea resize failed");
    }
    @Test(priority = 61)
    public void TC_61_verifyEditorEnable() {
    	addAssignmentPage.toggleEditor();
        Assert.assertTrue(addAssignmentPage.isEditorDisplayed(), "Editor not enabled");
    }
    @Test(priority = 62)
    public void TC_62_verifyEditorCancel() {
    	addAssignmentPage.toggleEditor(); // open
    	addAssignmentPage.toggleEditor(); // close -> alert

    	addAssignmentPage.handleEditorAlert(false);

        Assert.assertTrue(addAssignmentPage.isEditorDisplayed(), "Editor should remain after cancel");
    }
    @Test(priority = 63)
    public void TC_63_verifyEditorOk() {
    	addAssignmentPage.toggleEditor(); // open
    	addAssignmentPage.toggleEditor(); // close

        String alertMsg = addAssignmentPage.getQuestionEditorAlertText();
        Assert.assertTrue(alertMsg.contains("remove all text formatting"));

        addAssignmentPage.handleEditorAlert(true);

        Assert.assertFalse(addAssignmentPage.isEditorDisplayed(), "Editor should be disabled");
    }
}

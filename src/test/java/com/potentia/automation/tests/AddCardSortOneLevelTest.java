package com.potentia.automation.tests;

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
import com.potentia.automation.pages.AddCardSortOneLevelPage;
import com.potentia.automation.pages.LoginPage;
import com.potentia.automation.pages.SuperClientsPage;
import com.potentia.automation.utils.ConfigReader;
@Listeners(ExcelReportListener.class)
public class AddCardSortOneLevelTest extends BaseTest {
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

        AddCardSortOneLevelPage addCardSortOneLevelPage = new AddCardSortOneLevelPage(driver);

        boolean isClicked = addCardSortOneLevelPage.clickBatchByNameAcrossTabs(batchName);

        Assert.assertTrue(
            isClicked,
            "Batch was not found in Active, Planned, or Inactive tabs: " + batchName
        );
    }
	 @Test(priority = 3)
	 public void TC_003_clickAddActivityBtn() {
		 AddCardSortOneLevelPage addCardSortOneLevelPage = new AddCardSortOneLevelPage(driver);
		 addCardSortOneLevelPage.addActivityBtn();
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
		 AddCardSortOneLevelPage addCardSortOneLevelPage = new AddCardSortOneLevelPage(driver);
		 WebElement assignmentButton = driver.findElement(By.xpath("//label[span[text()='Card Sort']]"));
		 Actions actions = new Actions(driver);
		 actions.moveToElement(assignmentButton).perform();
		 Thread.sleep(5000);
		 WebElement tooltip = driver.findElement(By.xpath("//*[contains(text(), 'A gamified version for learning/ testing/ identifying preferences of a concept (one or two levels) wherein the learning elements act as clues')]"));
		 //Assert.assertTrue(tooltip.isDisplayed(), "Tooltip not visible on mouse hover.");
		 
		 addCardSortOneLevelPage.selectActivity();
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
         WebElement myActivityHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(), 'Add Activity')]")));
         String expectedHeading = "Add Activity - Card Sort";
         String actualHeading = myActivityHeading.getText().trim();
         System.out.println(actualHeading);
         if(expectedHeading.equals(actualHeading)) {
        	 System.out.println("Page landed on Add Activity - Card Sort Screen");
         } else {
        	 Assert.fail("Not Landed on expected add activity - Card Sort screen");
         }
	 }
	 
	 @Test(priority = 5)
	 public void TC_005_validateEmptyAssignmentActivityTitle () {
		 AddCardSortOneLevelPage addCardSortOneLevelPage = new AddCardSortOneLevelPage(driver);
		 addCardSortOneLevelPage.addActivityTitle("");
		 addCardSortOneLevelPage.clickCardSortSaveAndContinue();
		 String expectedError = "Please enter activity title";
		 if (addCardSortOneLevelPage.getActivityTitle().isEmpty()) {
			 if (addCardSortOneLevelPage.isActivityTitleErrorMessageDisplayed()) {
				 String actualError = addCardSortOneLevelPage.getActivityTitleErrorMessageText();
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
		AddCardSortOneLevelPage addCardSortOneLevelPage = new AddCardSortOneLevelPage(driver);
		 String overLimitTitle = addCardSortOneLevelPage.generateText(151);
		addCardSortOneLevelPage.addActivityTitle(overLimitTitle);
		addCardSortOneLevelPage.clickCardSortSaveAndContinue();
		String expectedError = "Activity title is too long, max 150 characters allowed";

		// Assert error message is displayed
		Assert.assertTrue(
				addCardSortOneLevelPage.isActivityTitleLengthErrorMessageDisplayed(),
		    "Error message for long activity title is not displayed."
		);

		// Get the actual error and assert it's correct
		String actualError = addCardSortOneLevelPage.getActivityTitleLengthErrorMessageText();
		Assert.assertEquals(
		    actualError,
		    expectedError,
		    "Mismatch in activity title length error message."
		);

		System.out.println("Correct error shown when title exceeds character limit: " + actualError);
	}
	@Test(priority = 7)
	public void TC_007_validateActivityInstructionsLength () {
		AddCardSortOneLevelPage addCardSortOneLevelPage = new AddCardSortOneLevelPage(driver);
		addCardSortOneLevelPage.clearActivityTitle();
		addCardSortOneLevelPage.addActivityTitle("card Sort 1");
		addCardSortOneLevelPage.clickActivityDiscriptionDropDownArrow();
		addCardSortOneLevelPage.addActivityDiscription("Add Description ");
		addCardSortOneLevelPage.addActivityInstruction("Add instructions");
		
	}
    @Test(priority = 8, description = "TC8 - Verify One - level is selected by default")
    public void TC_8_verifyOneLevelSelectedByDefault() {
    	AddCardSortOneLevelPage addCardSortOneLevelPage = new AddCardSortOneLevelPage(driver);
    	addCardSortOneLevelPage.clickCardSortOneLevel();
        Assert.assertTrue(addCardSortOneLevelPage.isOneLevelSelectedByDefault(), "One - level is NOT selected by default.");
    }
    @Test(priority = 9, description = "Verify Default, Custom and Remaing Color radio buttons are present and clickable")
    public void TC_09_verifyDefaultAndCustomRadioButtons() {
    	AddCardSortOneLevelPage addCardSortOneLevelPage = new AddCardSortOneLevelPage(driver);
        // Verify elements are displayed
        Assert.assertTrue(addCardSortOneLevelPage.isDefaultRadioDisplayed(), "Default radio button not displayed");
        Assert.assertTrue(addCardSortOneLevelPage.isCustomRadioDisplayed(), "Custom radio button not displayed");
        Assert.assertTrue(addCardSortOneLevelPage.isGrayColorRadioDisplayed(), "Gray Color radio button not displayed");
        Assert.assertTrue(addCardSortOneLevelPage.isGreenColorRadioRadioDisplayed(), "Green Color radio button not displayed");
        Assert.assertTrue(addCardSortOneLevelPage.isMagentaColorRadioDisplayed(), "Magenta Color radio button not displayed");
        Assert.assertTrue(addCardSortOneLevelPage.isYellowRadioDisplayed(), "Yellow Color radio button not displayed");
        Assert.assertTrue(addCardSortOneLevelPage.isPastelPinkRadioDisplayed(), "Pastel Pink color radio button not displayed");
        Assert.assertTrue(addCardSortOneLevelPage.isPastelGreenRadioDisplayed(), "Pastel Green Color radio button not displayed");
        Assert.assertTrue(addCardSortOneLevelPage.isPurpleRadioDisplayed(), "Purple color radio button not displayed");
        Assert.assertTrue(addCardSortOneLevelPage.isBrownRadioDisplayed(), "Brown Color radio button not displayed");

        // Verify elements are enabled
        Assert.assertTrue(addCardSortOneLevelPage.isDefaultRadioEnabled(), "Default radio button not clickable");
        Assert.assertTrue(addCardSortOneLevelPage.isCustomRadioEnabled(), "Custom radio button not clickable");
        Assert.assertTrue(addCardSortOneLevelPage.isGrayColorRadioEnabled(), "Gray Color radio button not clickable");
        Assert.assertTrue(addCardSortOneLevelPage.isGreenColorRadioEnabled(), "Green Color radio button not clickable");
        Assert.assertTrue(addCardSortOneLevelPage.isMagentaColorRadioEnabled(), "Magenta Color radio button not clickable");
        Assert.assertTrue(addCardSortOneLevelPage.isYellowColorRadioEnabled(), "Yellow Color radio button not clickable");
        Assert.assertTrue(addCardSortOneLevelPage.isPastelPinkColorRadioEnabled(), "Pastel Pink Color radio button not clickable");
        Assert.assertTrue(addCardSortOneLevelPage.isPastelGreenColorRadioEnabled(), "pastel Green Color radio button not clickable");
        Assert.assertTrue(addCardSortOneLevelPage.isPurpelColorRadioEnabled(), "Purple Color radio button not clickable");
        Assert.assertTrue(addCardSortOneLevelPage.isBrownColorRadioEnabled(), "Brown Color radio button not clickable");
    }

    @Test(priority = 10, description = "Verify clicking on Default, Custom and Remaing Color options works")
    public void TC_10_verifyClickFunctionality() {
    	AddCardSortOneLevelPage addCardSortOneLevelPage = new AddCardSortOneLevelPage(driver);
        // Click Default and verify
    	addCardSortOneLevelPage.clickDefaultRadio();
        Assert.assertTrue(addCardSortOneLevelPage.isDefaultSelected(), "Default radio not selected after click");

        // Click Custom and verify
        addCardSortOneLevelPage.clickCustomRadio();
        Assert.assertTrue(addCardSortOneLevelPage.isCustomSelected(), "Custom radio not selected after click");
        
     // Click Gray and verify
        addCardSortOneLevelPage.clickGrayColorRadio();
        Assert.assertTrue(addCardSortOneLevelPage.isGrayColorRadioSelected(), "Gray radio not selected after click");
        
     // Click Green and verify
        addCardSortOneLevelPage.clickGreenRadio();
        Assert.assertTrue(addCardSortOneLevelPage.isGreenColorRadioSelected(), "Green radio not selected after click");
        
     // Click Magenta and verify
        addCardSortOneLevelPage.clickMagentaRadio();
        Assert.assertTrue(addCardSortOneLevelPage.isMagentaColorRadioSelected(), "Magenta radio not selected after click");
        
     // Click Yellow and verify
        addCardSortOneLevelPage.clickYellowRadio();
        Assert.assertTrue(addCardSortOneLevelPage.isYellowColorRadioSelected(), "Yellow radio not selected after click");
        
     // Click Pastel Pink and verify
        addCardSortOneLevelPage.clickPastelPinkRadio();
        Assert.assertTrue(addCardSortOneLevelPage.isPastelPinkColorRadioSelected(), "Pastel Pink radio not selected after click");
        
     // Click Pastel Green and verify
        addCardSortOneLevelPage.clickPastelGreenRadio();
        Assert.assertTrue(addCardSortOneLevelPage.isPastelGreenColorRadioSelected(), "Pastel Green radio not selected after click");
        
     // Click Purple and verify
        addCardSortOneLevelPage.clickPurpleRadio();
        Assert.assertTrue(addCardSortOneLevelPage.isPurpelColorRadioSelected(), "Purple radio not selected after click");
        
        // Click Brown and verify
        addCardSortOneLevelPage.clickBrownRadio();
        Assert.assertTrue(addCardSortOneLevelPage.isBrownColorRadioSelected(), "Brown radio not selected after click");
        
    }

    @Test(priority = 11, description = "Verify color pickers are visible for Custom option")
    public void TC_11_verifyColorPickersVisibleForCustom() {
    	AddCardSortOneLevelPage addCardSortOneLevelPage = new AddCardSortOneLevelPage(driver);
    	addCardSortOneLevelPage.clickCustomRadio();
        Assert.assertTrue(addCardSortOneLevelPage.isBackgroundColorPickerVisible(), "Background color picker not visible");
        Assert.assertTrue(addCardSortOneLevelPage.isFontColorPickerVisible(), "Font color picker not visible");
    }
    @Test(priority = 12)
    public void TC_12_verifyMultipleCardDescriptionsCanBeEntered() {
    	AddCardSortOneLevelPage addCardSortOneLevelPage = new AddCardSortOneLevelPage(driver);
        String baseText = "Automation Description";
        addCardSortOneLevelPage.enterTextInAllCardDescriptions(baseText);

        // Verify all inputs have expected values
        List<WebElement> allFields = addCardSortOneLevelPage.getAllCardDescriptionFields();
        int i = 1;
        for (WebElement field : allFields) {
            String expected = baseText + " " + i;
            String actual = field.getAttribute("value");
            Assert.assertEquals(actual, expected, "Mismatch in card description " + i);
            i++;
        }
    }
    @Test(priority = 13)
    public void TC_13_addCardDescriptionFileds() {
    	AddCardSortOneLevelPage addCardSortOneLevelPage = new AddCardSortOneLevelPage(driver);
    	if(addCardSortOneLevelPage.getCardDescriptionFieldCount() == 2) {
    		addCardSortOneLevelPage.clickDeleteCardButton(1);
    		String expectedError = "Minimum two cards required";
    		if(addCardSortOneLevelPage.isMinimumCardCountErrorMessageDisplayed()) {
    			String actualError = addCardSortOneLevelPage.getMinimumCardCountErrorMessageText();
    			Assert.assertEquals(expectedError, actualError, "Minimum Card count error Message is MIsmatched");
    		}else {
    			Assert.fail(expectedError + "Message is not Displayed");
    		}
    	} else {
    		Assert.fail("Card count is More then 2");
    	}
    	addCardSortOneLevelPage.clickAddCardButton(3);
    	addCardSortOneLevelPage.clickDeleteCardButton(1);
    	
    	String baseText = "Automation Description";
    	addCardSortOneLevelPage.enterTextInAllCardDescriptions(baseText);

        // Verify all inputs have expected values
        List<WebElement> allFields = addCardSortOneLevelPage.getAllCardDescriptionFields();
        int i = 1;
        for (WebElement field : allFields) {
            String expected = baseText + " " + i;
            String actual = field.getAttribute("value");
            Assert.assertEquals(actual, expected, "Mismatch in card description " + i);
            i++;
        }
        addCardSortOneLevelPage.clickCardSortSaveAndContinue();
    }
    @Test(priority = 14)
    public void TC_14_validateEmptyCardsToBeChosen() {
    	AddCardSortOneLevelPage addCardSortOneLevelPage = new AddCardSortOneLevelPage(driver);
    	String expectedError = "Please enter Cards to be Chosen";
    	if(addCardSortOneLevelPage.isCardsToBeChosenEmptyErrorMessageDisplayed()) {
    		String actualError = addCardSortOneLevelPage.getCardsToBeChosenEmptyErrorMessageText();
    		Assert.assertEquals(expectedError, actualError, "Please enter Cards to be Chosen error message is Mismatched");
    	} else {
    		Assert.fail(expectedError + "Error Message is not displayed");
    	}
    }
    @Test(priority = 15)
    public void TC_15_validateCardsToBeChosenisZero() {
    	AddCardSortOneLevelPage addCardSortOneLevelPage = new AddCardSortOneLevelPage(driver);
    	addCardSortOneLevelPage.enterCardsToBeChosen("0");
    	String expectedError = "No of Card Assigned should not be less than 1.";
    	if(addCardSortOneLevelPage.isCardsToBeChosenIsZeroErrorMessageDisplayed()) {
    		String actualError = addCardSortOneLevelPage.getCardsToBeChosenIsZeroErrorMessageText();
    		Assert.assertEquals(expectedError, actualError, "error message is mismatched");
    	} else {
    		Assert.fail(expectedError + "Error message is not Displayed");
    	}
    	
    }
    @Test(priority = 16)
    public void TC_16_validateCardsToBeChosenIsMorethenCards() {
    	AddCardSortOneLevelPage addCardSortOneLevelPage = new AddCardSortOneLevelPage(driver);
    	int Count = addCardSortOneLevelPage.getCardDescriptionFieldCount() + 1;
    	String cardCount = String.valueOf(Count);
    	addCardSortOneLevelPage.enterCardsToBeChosen(cardCount);
    	String expectedErrorForCards = "Cards to be chosen cannot exceed the number of cards created";
    	if(addCardSortOneLevelPage.isCardsToBeChosenGreaterthenCardsErrorMessageDisplayed()) {    		
    		String actualError = addCardSortOneLevelPage.getCardsToBeChosenGreatThenCardsErrorMessageText();
    		Assert.assertEquals(expectedErrorForCards, actualError, "error message is mismatched");
    	} else {
    		Assert.fail(expectedErrorForCards + "Error message is not Displayed");
    	}
    }
}

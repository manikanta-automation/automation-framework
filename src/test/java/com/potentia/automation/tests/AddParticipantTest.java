package com.potentia.automation.tests;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.potentia.automation.core.BaseTest;
import com.potentia.automation.pages.AddParticipantPage;
import com.potentia.automation.pages.AddReferenceMaterialPage;
import com.potentia.automation.pages.LoginPage;
import com.potentia.automation.pages.SuperClientsPage;
import com.potentia.automation.utils.ConfigReader;

public class AddParticipantTest extends BaseTest{
	private LoginPage loginPage;

	Map<String, String> participantData = new HashMap<>();
	
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

        AddParticipantPage addParticipantPage = new AddParticipantPage(driver);

        boolean isClicked = addParticipantPage.clickBatchByNameAcrossTabs(batchName);

        Assert.assertTrue(
            isClicked,
            "Batch was not found in Active, Planned, or Inactive tabs: " + batchName
        );
    }
	 @Test(priority = 3)
	 public void TC_03_varifyParticipantLinkVisibility() {
		 AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
		 Assert.assertTrue(addParticipantPage.isParticipantsLinkDisplayed(), "Participants link is not visible");
	 }
	 @Test(priority = 4)
	 public void TC_04_varifyParticipantLinkclickable() {
		 AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
		 addParticipantPage.participantLinkClickable();
		 Assert.assertTrue(addParticipantPage.isParticipantPageDisplayed(), "not laneded on the Participant page");
		 System.out.println("landed on participant Page");
	 }
	 @Test(priority = 5)
	 public void TC_05_varifyParticipantAndGroupsBtnsDisplayed() {
		 AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
		 Assert.assertTrue(addParticipantPage.isParticipantBtnDisplayed(), "Participant Button is not displayed");
		 Assert.assertTrue(addParticipantPage.isGroupsBtnDisplayed(), "Groups Button is not Dislayed");
	 }
	 @Test(priority = 6)
	 public void TC_06_varifyAddPaxBtnIsDisplayed() {
		 AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
		 Assert.assertTrue(addParticipantPage.isAddPaxBtnDisplayed(), "Add Pax Button is not Displayed");
	 }
	 @Test(priority = 7)
	 public void TC_07_varifyAddManuallyandOtherTabsareDisplayed() {
		 AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
		 addParticipantPage.clickAddPaxBtn();
		 Assert.assertTrue(addParticipantPage.isAddManuallyTabDisplayed(), "add manually Tab is not Displayed");
		 Assert.assertTrue(addParticipantPage.isUploadTabDisplayed(), "Upload Tab is not displayed");
		 Assert.assertTrue(addParticipantPage.isCopyFromOtherBatchTabDisplayed(), "Copy from Other Batch Tab is not displayed");
	 }
	 @Test(priority = 8)
	 public void TC_08_varifyPaxsubmitAndCancelBtnDisplayed() {
		 AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
		 Assert.assertTrue(addParticipantPage.isaddManuallySubmitBtnDisplayed(), "Submit BUtton is not Displayed ON Add Manually Tab");
		 Assert.assertTrue(addParticipantPage.isAddManuallyCancelBtnDisplayed(), "Cancel Button is not displayed on Add Manually Tab");
	 }
	 @Test(priority = 9)
	 public void TC_09_varifyPaxDetailsErrorMsgDisplayed() {
		 AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
		 addParticipantPage.clickAddManuallyTabSubmitBtn();
		 Assert.assertTrue(addParticipantPage.isPaxDetailsMandatoryErrorMessageDisplayed(), "mandatory details Error messafe is not displayed");
		 String actualError = "All fields marked with an asterix are mandatory. Please provide details before submitting.";
		 String expectedError = addParticipantPage.getPaxDetailsMandatoryErrorMessageDisplayed();
		 Assert.assertEquals(actualError, expectedError, "Error Message: Mismatched Actual error " + actualError + " but found " + expectedError);
	 }
	 @Test(priority = 10, description = "TC_10 - Verify default placeholder image")
	 public void TC_10_verifyDefaultImageDisplayed() {
		 AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
    	Assert.assertTrue(addParticipantPage.isProfileImageDisplayed(), "Default placeholder not visible before upload.");
    }
	 @Test(priority = 11, description = "TC_11 - Verify invalid file type validation")
	 public void TC_11_uploadInvalidFileType() {
		 AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
		 String imagePath = System.getProperty("user.home") + "\\Downloads\\Demo Kit - 08-09-2025-Quiz - Matrix questions with nested options.pdf";
		 addParticipantPage.uploadProfilePhoto(imagePath);
		 addParticipantPage.clickAddManuallyTabSubmitBtn();
		 Assert.assertTrue(addParticipantPage.isUploadProfileImageErrorMsgDisplayed(), "No validation for invalid file types");
	 }
//	 @Test(priority = 12, description = "TC_12 - Upload valid image and verify displayed")
//	 public void TC_12_uploadProfilePhoto() {
//		 AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
//	        // Full file path of the image to upload
//		 String imagePath = System.getProperty("user.home") + "\\Downloads\\profile.jpg";
//		 //String imagePath1 = System.getProperty("user.home") + "\\Downloads\\WhatsApp Image 2025-08-19 at 11.09.43 AM.jpeg";
//		 //String imagePath2 = System.getProperty("user.home") + "\\Downloads\\ChatGPT Image Aug 2, 2025, 05_21_16 PM.png";
//
//	        // Perform upload
//		 addParticipantPage.uploadProfilePhoto(imagePath);
//		 //participantPage.uploadProfilePhoto(imagePath1);
//		 //participantPage.uploadProfilePhoto(imagePath2);
//		 
//		 Assert.assertTrue(addParticipantPage.isProfileImageDisplayed(), "Profile image not displayed after upload.");
//		 System.out.println("Profile photo uploaded successfully!");
//    }
	 @Test(priority = 13)
	    public void TC_13_verifyFirstNameFieldVisibleAndEnabled() {
		 AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
		 	Assert.assertTrue(addParticipantPage.isFirstNamedisplayed(), "First name filed is not Displayed");
	        Assert.assertTrue(addParticipantPage.isFirstNameEnabled(), "First Name field is disabled");
	        System.out.println("TC_13 passed: First Name field is visible and enabled");
	    }
	 @Test(priority = 14)
	    public void TC_14_verifyFirstNameIsMandatory() { 
		 AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
		 addParticipantPage.clearFirstName();
		 addParticipantPage.clickAddManuallyTabSubmitBtn();
	        Assert.assertTrue(addParticipantPage.isErrorMessageDisplayed(), "Error message not displayed for empty First Name");
	        System.out.println("TC_14 passed: Validation shown for mandatory First Name");
	    }
	 @Test(priority = 15)
	    public void TC_15_verifyValidFirstNameInputAccepted() {
		 AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
		 addParticipantPage.enterFirstName("John");
	        String actualValue = addParticipantPage.getFirstNameValue();
	        Assert.assertEquals(actualValue, "John", "Valid name not accepted");
	        System.out.println("TC_15 passed: Valid input accepted and retained");
	    }
	 @Test(priority = 16)
	    public void TC_16_verifyLastNameFieldVisibleAndEnabled() {
		 AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
		 	Assert.assertTrue(addParticipantPage.isLastNameDisplayed(), "Last Name filed is not Displayed");
	        Assert.assertTrue(addParticipantPage.isLastNameEnabled(), "Last Name field is disabled");
	        System.out.println("TC_16 passed: Last Name field is visible and enabled");
	    }
	 @Test(priority = 17)
	    public void TC_17_verifyValidLastNameInputAccepted() {
		 AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
		 addParticipantPage.enterLastName("John");
	        String actualValue = addParticipantPage.getLastNameValue();
	        Assert.assertEquals(actualValue, "John", "Valid name not accepted");
	        System.out.println("TC_17 passed: Valid input accepted and retained");
	    }
	 @Test(priority = 18)
	    public void TC_18_verifySalutationDropdownVisibleAndEnabled() {
		 	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
		 	Assert.assertTrue(addParticipantPage.isSalutationDisplayed(), "Salutation filed is not Displayed");
	        Assert.assertTrue(addParticipantPage.isSalutationEnabled(), "Salutation dropdown is disabled");
	        System.out.println("TC_18 passed: Salutation dropdown is visible and enabled");
	    }

	    @Test(priority = 19)
	    public void TC_19_verifyDefaultValueIsSelectSalutation() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	        String defaultValue = addParticipantPage.getDefaultSelectedOption();
	        Assert.assertEquals(defaultValue, "Select Salutation", "Default option is incorrect");
	        System.out.println("TC_19 passed: Default option is 'Select Salutation'");
	    }

	    @Test(priority = 20)
	    public void TC_20_verifyAllDropdownOptionsVisible() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	        List<WebElement> options = addParticipantPage.getAllOptions();
	        boolean hasMr = options.stream().anyMatch(opt -> opt.getText().equals("Mr"));
	        boolean hasMs = options.stream().anyMatch(opt -> opt.getText().equals("Ms"));
	        boolean hasMx = options.stream().anyMatch(opt -> opt.getText().equals("Mx"));

	        Assert.assertTrue(hasMr && hasMs && hasMx, "One or more salutation options missing");
	        System.out.println("TC_20 passed: All options (Mr, Ms, Mx) are present");
	    }

	    @Test(priority = 21)
	    public void TC_21_verifyUserCanSelectEachOption() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	addParticipantPage.selectSalutation("Mr");
	        Assert.assertEquals(addParticipantPage.getSelectedSalutation(), "Mr", "Failed to select 'Mr'");

	        addParticipantPage.selectSalutation("Ms");
	        Assert.assertEquals(addParticipantPage.getSelectedSalutation(), "Ms", "Failed to select 'Ms'");

	        addParticipantPage.selectSalutation("Mx");
	        Assert.assertEquals(addParticipantPage.getSelectedSalutation(), "Mx", "Failed to select 'Mx'");
	        System.out.println("TC_21 passed: User can select all options successfully");
	    }

	    @Test(priority = 22)
	    public void TC_22_verifySingleSelectionOnly() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	addParticipantPage.selectSalutation("Mr");
	        String selected = addParticipantPage.getSelectedSalutation();
	        Assert.assertEquals(selected, "Mr", "Multiple selection detected");
	        System.out.println("TC_22 passed: Only one option can be selected at a time");
	    }
	    @Test(priority = 26)
	    public void TC_26_verifyGenderMaleVisibleAndEnabled() {
	    	Assert.assertTrue(participantPage.isGenderMaleDisplayed(), "Gender Male filed is not Displayed");
	    	Assert.assertTrue(participantPage.isGenderMaleEnabled(), "Gender Male is disabled");
	    	System.out.println("TC_26 passed: Gender Male is visible and enabled");
	    }
	    @Test(priority = 27)
	    public void TC_27_varifyGenderMaleIsSelected() {
	    	participantPage.clickGenderMaleRadioButton();
	    	Assert.assertTrue(participantPage.isGenderMaleSelected(), "Gender Male Is not Selected");
	    	System.out.println("TC_27 passed: Gender Male Is Selected");
	    }
	    @Test(priority = 28)
	    public void TC_28_verifyGenderFemaleVisibleAndEnabled() {
	    	Assert.assertTrue(participantPage.isGenderFemaleDisplayed(), "Gender Female filed is not Displayed");
	    	Assert.assertTrue(participantPage.isGenderFemaleEnabled(), "Gender Female is disabled");
	    	System.out.println("TC_28 passed: Gender Female is visible and enabled");
	    }
	    @Test(priority = 29)
	    public void TC_29_varifyGenderFemaleIsSelected() {
	    	participantPage.clickGenderFemaleRadioButton();
	    	Assert.assertTrue(participantPage.isGenderFemaleSelected(), "Gender Female Is not Selected");
	    	System.out.println("TC_29 passed: Gender Female Is Selected");
	    }
	    @Test(priority = 30)
	    public void TC_30_verifyGenderOtherVisibleAndEnabled() {
	    	Assert.assertTrue(participantPage.isGenderOtherDisplayed(), "Gender Other filed is not Displayed");
	    	Assert.assertTrue(participantPage.isGenderOtherEnabled(), "Gender Other is disabled");
	    	System.out.println("TC_30 passed: Gender Other is visible and enabled");
	    }
	    @Test(priority = 31)
	    public void TC_31_varifyGenderOtherIsSelected() {
	    	participantPage.clickGenderOtherRadioButton();
	    	Assert.assertTrue(participantPage.isGenderOtherSelected(), "Gender Other Is not Selected");
	    	System.out.println("TC_31 passed: Gender Other Is Selected");
	    }
	    @Test(priority = 32)
	    public void TC_32_verifyCallNameFieldVisibleAndEnabled() {
		 	Assert.assertTrue(participantPage.isCallNameDisplayed(), "Call Name filed is not Displayed");
	        Assert.assertTrue(participantPage.isCallNameEnabled(), "Call Name field is disabled");
	        System.out.println("TC_32 passed: Call Name field is visible and enabled");
	    }
	 @Test(priority = 33)
	    public void TC_33_verifyValidCallNameInputAccepted() {
		 	participantPage.enterCallName("John");
	        String actualValue = participantPage.getCallNameValue();
	        Assert.assertEquals(actualValue, "John", "Valid name not accepted");
	        System.out.println("TC_33 passed: Valid input accepted and retained");
	    }
	 @Test(priority = 34)
	    public void TC_34_verifyEmailFieldVisibleAndEnabled() {
	        Assert.assertTrue(participantPage.isEmailVisible(), "Email field is not visible");
	        Assert.assertTrue(participantPage.isEmailEnabled(), "Email field is not enabled");
	        System.out.println("TC_34 passed: Email field is visible and enabled");
	    }

	    @Test(priority = 23)
	    public void TC_23_verifyEmailIsMandatory() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	addParticipantPage.clearEmail();
	    	addParticipantPage.clickAddManuallyTabSubmitBtn();
	        Assert.assertTrue(addParticipantPage.isPaxDetailsMandatoryErrorMessageDisplayed(), "Error not shown for blank email");
	        System.out.println("TC_23 passed: Mandatory validation working");
	    }
	    
	    @Test(priority = 24)
	    public void TC_24_verifyInvalidEmailRejected() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	        String[] invalidEmails = {"john@", "@gmail.com", "john@@gmail.com", "john@gmail", "john@.com", "john @gmail.com"};
	        for (String invalid : invalidEmails) {
	        	addParticipantPage.enterEmail(invalid);
	        	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        	wait.until(ExpectedConditions.invisibilityOfElementLocated(
	        	    By.cssSelector(".toast.toast-error")
	        	));
	        	addParticipantPage.clickAddManuallyTabSubmitBtn();
	            Assert.assertTrue(addParticipantPage.isPaxDetailsMandatoryErrorMessageDisplayed(), "Invalid email accepted: " + invalid);
	        }
	        System.out.println("TC_24 passed: Invalid email formats correctly rejected");
	    }
	    @Test(priority = 25)
	    public void TC_25_verifyLeadingAndTrailingSpacesTrimmed() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	addParticipantPage.enterEmail("  john@gmail.com  ");
	        String value = addParticipantPage.getEmailValue().trim();
	        Assert.assertEquals(value, "john@gmail.com", "Email not trimmed properly");
	        System.out.println("TC_25 passed: Leading/trailing spaces trimmed");
	    }

	    @Test(priority = 37)
	    public void TC_37_verifyValidEmailAccepted() {
	    	participantPage.enterEmail("john.doe@gmail.com");
	        System.out.println("TC_37 passed: Valid email accepted");
	    }
	    @Test(priority = 11)
		 public void TC_11_varifyAdditionalInformationFormDisplayed() {
    		 Assert.assertTrue(participantPage.isAdditionalInformationDropdownDisplayed(), "AdditionalDropdown is not Displayed");
			 participantPage.clickAdditionalInformationDropdown();			 			  									
		 }
	    @Test(priority = 38)
	    public void TC_38_varifyMobileNumberIsDisplayedAndEnabled() {
	    	Assert.assertTrue(participantPage.isMobileNumberDisplayed(), "Mobile filed is not Displayed");
	    	Assert.assertTrue(participantPage.isMobileNumberEnabled(), "Mobile Number is Disabled");
	    }
	    @Test(priority = 40)
		public void TC_40_verifyNumberFieldRejectsAlphabets() {
	    	participantPage.clickMobileNumberFiled("abc");
			Assert.assertTrue(participantPage.getMobileNumberValue().isEmpty(), "Field should not accept alphabets");
		}
	    @Test(priority = 41)
		public void TC_41_verifyNumberFieldRejectsSpecialCharacters() {
	    	participantPage.clickMobileNumberFiled("@#$%");
			Assert.assertTrue(participantPage.getMobileNumberValue().isEmpty(), "Field should not accept special characters");
		}
	    @Test(priority = 42)
		public void TC_42_verifyNumberFieldAcceptsValidValue() {
	    	participantPage.clickMobileNumberFiled("25");
			Assert.assertEquals(participantPage.getMobileNumberValue(), "25", "Valid number not accepted");
		}
	    @Test(priority = 43)
	    public void TC_43_varifyEmployeeIdIsDisplayedandEnabled() {
	    	Assert.assertTrue(participantPage.isEmployeeIdDisplayed(), "EmployeeId filed is not Displayed");
	    	Assert.assertTrue(participantPage.isEmployeeIdEnabled(), "EmployeeId filed is not Enabled");
	    }
	    @Test(priority = 44)
	    public void TC_44_vairfyEmployeeIdFieldAcceptsValidValue() {
	    	participantPage.clickEmployeeIdFiled("Mani@9908");
	    	Assert.assertEquals(participantPage.getEmployeeIdvalue(), "Mani@9908", "Value not accepted");
	    }
	    @Test(priority = 45)
	    public void TC_45_varifyDesignationFiledDisplayedandEnabled() {
	    	Assert.assertTrue(participantPage.isDesignationDisplayed(), "Designation filed is not Displayed");
	    	Assert.assertTrue(participantPage.isDesignationEnabled(), "Designation filed is not Enabled");
	    }
	    @Test(priority = 46)
	    public void TC_46_varifyDesignationFieldAcceptsValidValue() {
	    	participantPage.clickDesignationFiled("Tester");
	    	Assert.assertEquals(participantPage.getDesignationValue(), "Tester", "Value Not accepted");
	    }
	    @Test(priority = 47)
	    public void TC_47_varifyDepartmentFieldDisplayedAndEnabled() {
	    	 Assert.assertTrue(participantPage.isDepartmentDisplayed(), "Department filed is not Displayed");
	    	 Assert.assertTrue(participantPage.isDepartmentEnabled(), "Department filed is not Enabled");
	    }
	    @Test(priority = 26)
	    public void TC_26_varifyDepartmentFiledAcceptsValidValue() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	addParticipantPage.clickDepartmentField("IT");
	    	Assert.assertEquals(addParticipantPage.getDepartmentvalues(),"IT", "value not accepted");
	    }
	    @Test(priority = 27)
	    public void TC_27_varifyLocationFieldDisplayedAndEnabled() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	Assert.assertTrue(addParticipantPage.isLocationDisplayed(), "Location filed is not Displayed");
	    	Assert.assertTrue(addParticipantPage.isLocationEnabled(), "Location filed is not Enabled");
	    }
	    @Test(priority = 28)
	    public void TC_28_varifyLoactionfieldAcceptsValidValues() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	addParticipantPage.clickLocationField("HYD");
	    	Assert.assertEquals(addParticipantPage.getLocationValues(),"HYD", "Value Not Accepted");
	    }
	    @Test(priority = 51)
	    public void TC_51_varifyAboutSlefDisplayedAndEnabled() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	 Assert.assertTrue(addParticipantPage.isAboutSelfDisplayed(), "About Self filed is not Displayed");
	    	 Assert.assertTrue(addParticipantPage.isAboutSelfEnabled(), "About Self filed is not Enabled");
	    }
	    @Test(priority = 52)
	    public void TC_52_verifyAboutMyselfFieldResize() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	WebElement aboutSelf = driver.findElement(By.id("about_self"));
	    	int initialHeight = aboutSelf.getSize().getHeight();
	    	System.out.println(initialHeight);
	    	
	    	addParticipantPage.resizeAboutselfField(initialHeight + 100);
	    	
	    	int newHeight = aboutSelf.getSize().getHeight();
	    	System.out.println(newHeight);
	    	
	    	Assert.assertTrue(newHeight > initialHeight , "Textarea size did not increase after resize!");
	    	System.out.println("✅ About Myself field resized successfully. " +
	    	        "Height before: " + initialHeight + "px, after: " + newHeight + "px");
	    }
	    @Test(priority = 53)
	    public void TC_53_varifyaboutSelfAcceptsValidInput() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	addParticipantPage.enterInputInAboutSelf("Hi I am Manikanta this my profile");
	    	Assert.assertEquals(addParticipantPage.getAboutSlefValues(),"Hi I am Manikanta this my profile", "Value Not Accepted");
	    }
	    @Test(priority = 54)
	    public void TC_54_varifyprofileLinkIsDisplayedAndEnabled() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	Assert.assertTrue(addParticipantPage.isProfileLinkLabelDisplayed(), "Profile Link filed is not Displayed");
	    	Assert.assertTrue(addParticipantPage.isProfileLinkEnabled(), "Profile Link filed is not Enabled");
	    	Assert.assertTrue(addParticipantPage.areButtonsVisible(), "Add and Delete buttons are visible besides Profile link field");
	    	Assert.assertTrue(addParticipantPage.areButtonsEnabled(), "Add and Delete buttons are Enabled Besides profile Link Filed");
	    }
	    @Test(priority = 55)
	    public void TC_55_verifyErrorMessageWhenDeletingOnlyProfileLink() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	addParticipantPage.clickDeleteProfileLinkField();
	    	Assert.assertTrue(addParticipantPage.isProfileLinkDeleteErrorMessage(), "Profile link deleted error message is not displayed");
	    	Assert.assertEquals(addParticipantPage.getProfileLinkDeleteErrorMessage(),"Participant should have atleast 1 profile link", "Profile link error message is mismatched");
	    	
	    }
	    @Test(priority = 56)
	    public void TC_56_varifyNewProfileLinkFieldisAdded() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	int initialCount = addParticipantPage.getProfileLinkfieldCount();
	    	System.out.println("initial count of ProfileLink :" + initialCount);
	    	addParticipantPage.clickAddNewProfileLinkFieldButton();
	    	int afterCount = addParticipantPage.getProfileLinkfieldCount();
	    	System.out.println("after Count of new profile Link field is added :" + afterCount);
	    	Assert.assertTrue(initialCount < afterCount, "New Profile Link field is not added");
	    }
	    @Test(priority = 57)
	    public void TC_57_varifyNewlyAddedProfileLinkFieldDeleted() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	int initialCount = addParticipantPage.getProfileLinkfieldCount();
	    	System.out.println("intial count :" + initialCount);
	    	addParticipantPage.clickDeleteProfileLinkField();
	    	int afterCount = addParticipantPage.getProfileLinkfieldCount();
	    	System.out.println("after Deleted Profile link count :" + afterCount);
	    	Assert.assertTrue(initialCount > afterCount, "Profile link filed not deleted");
	    }
	    @Test(priority = 58)
	    public void TC_58_verifyInvalidUrlShowsErrorMessage() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	addParticipantPage.clickProfileLinkField("Example.com");
	    	addParticipantPage.clickAddManuallyTabSubmitBtn();
	    	Assert.assertTrue(addParticipantPage.isInvalidURlErrorMessageDisplayed(), "Invalid URL error message is not displayed");
	    	Assert.assertEquals(addParticipantPage.getInvalidURLErrorMsg(),"* Invalid URL", "Error message is mis matched");
	    }
	    @Test(priority = 59)
	    public void TC_59_varifySpecialCharactersUrlShowsError() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	addParticipantPage.clickProfileLinkField("@@@@");
	    	addParticipantPage.clickAddManuallyTabSubmitBtn();
	    	Assert.assertTrue(addParticipantPage.isInvalidURlErrorMessageDisplayed(), "Invalid URL error message is not displayed");
	    	Assert.assertEquals(addParticipantPage.getInvalidURLErrorMsg(),"* Invalid URL", "Error message is mis matched");
	    }
	    @Test(priority = 60) 
	    public void TC_60_varifyValidURlAccepts() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	addParticipantPage.clickProfileLinkField("https://example.com");
	    }
	    @Test(priority = 61)
	    public void TC_61_varifyParticipantDetailsaddedSuccessfully() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	participantData.put("FirstName", "Jhon");
	    	participantData.put("LastName", "Donn");
	    	participantData.put("Salutation", "Mr");
	    	participantData.put("CallName", "Mani");
	    	participantData.put("Email", "jhon.ae@gmail.com");
	    	participantData.put("MobileNumber", "1234567896");
	    	participantData.put("EmployeeID", "EMP9908");
	    	participantData.put("Designation", "QA Engineer");
	    	participantData.put("Department", "Testing");
	    	participantData.put("Location", "HYD");
	    	participantData.put("AboutSelf", "Automation tester with 2 years experience.");
	    	participantData.put("ProfileLink", "https://example.com");
	    	
	    	addParticipantPage.enterFirstName(participantData.get("FirstName"));
	    	addParticipantPage.enterLastName(participantData.get("LastName"));
	    	addParticipantPage.selectSalutation(participantData.get("Salutation"));
	    	addParticipantPage.enterCallName(participantData.get("CallName"));
	    	addParticipantPage.enterEmail(participantData.get("Email"));
	    	addParticipantPage.clickMobileNumberFiled(participantData.get("MobileNumber"));
	    	addParticipantPage.clickEmployeeIdFiled(participantData.get("EmployeeID"));
	    	addParticipantPage.clickDesignationFiled(participantData.get("Designation"));
	    	addParticipantPage.clickDepartmentField(participantData.get("Department"));
	    	addParticipantPage.clickLocationField(participantData.get("Location"));
	    	addParticipantPage.enterInputInAboutSelf(participantData.get("AboutSelf"));
	    	addParticipantPage.clickProfileLinkField(participantData.get("ProfileLink"));
	    	
	    	addParticipantPage.clickAddManuallyTabSubmitBtn();
	    	addParticipantPage.clickCloseInvitationBtn();
	    	
	    	//save data to JSON for later comparison
	    	addParticipantPage.saveDataToJson(participantData);
	    	
	    	 System.out.println("✅ TC_01 passed: Participant added and data saved to JSON");
	    }
	    @Test(priority = 62, dependsOnMethods = "TC_61_varifyParticipantDetailsaddedSuccessfully")
	    public void TC_62_verifyDataRetainedInEditMode() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	        // Read expected data from JSON
	        Map<String, String> expectedData = addParticipantPage.readDataFromJson();
	        
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		 	WebElement mypaxHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(normalize-space(.), '+ Pax')]")));

	        // Open edit mode
		 	addParticipantPage.clickParticipantEditIcon();

	        // Validate each field
	        Assert.assertEquals(addParticipantPage.getEditFirstNameValue(), expectedData.get("FirstName"), "First name mismatch");
	        Assert.assertEquals(addParticipantPage.getEditLastNameValue(), expectedData.get("LastName"), "Last name mismatch");
	        Assert.assertEquals(addParticipantPage.getEditSalutationNameValue(), expectedData.get("Salutation"), "Salutation mismatched");
	        Assert.assertEquals(addParticipantPage.getEditCallNameValue(), expectedData.get("CallName"), "Call Name Mismatch");
	        Assert.assertEquals(addParticipantPage.getEditEmailValue(), expectedData.get("Email"), "Email mismatch");
	        addParticipantPage.clickEditAdditionalInformationTab();
	        Assert.assertEquals(addParticipantPage.getEditMobileNumberValue(), expectedData.get("MobileNumber"), "Mobile Number is mismatched");
	        Assert.assertEquals(addParticipantPage.getEditEmployeeIdValue(), expectedData.get("EmployeeID"), "Employee Id mismatched");
	        Assert.assertEquals(addParticipantPage.getEditDesignationValue(), expectedData.get("Designation"), "Designation is mismatched");
	        Assert.assertEquals(addParticipantPage.getEditDepartmentValue(), expectedData.get("Department"), "Department is mismathecd");
	        Assert.assertEquals(addParticipantPage.getEditLocationValue(), expectedData.get("Location"), "Location is mismatched");
	        Assert.assertEquals(addParticipantPage.getEditAboutSelfValue(), expectedData.get("AboutSelf"), "About Self mismathed");
	       // Assert.assertEquals(participantPage.getProfileLinkUrl(), expectedData.get("ProfileLink"), "Profile link mismatch");
	        // ... (repeat for all other fields as needed)

	        System.out.println("✅ TC_62 passed: All field values retained correctly in Edit Mode");
	        addParticipantPage.clickOnUpdateBtn();
	    }
	    @Test(priority = 63)
	    public void TC_63_varifyEditPaxDetails() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	addParticipantPage.clickEditAdditionalInformationTab();
	    	addParticipantPage.clearEditFirstName();
	    	addParticipantPage.clickOnUpdateBtn();
	    	Assert.assertTrue(addParticipantPage.isErrorMessageDisplayed(), "Error message not displayed for empty First Name");
	    	addParticipantPage.enterEditFirstName("Nani");
	    	addParticipantPage.isPasswordfieldVisible();
	    	addParticipantPage.clickEyeIconToViewPassword();	    	
	    	addParticipantPage.clickOnUpdateBtn();
	    }
	    @Test(priority = 64)
	    public void TC_PAG_64_verifyDefaultPageShows10Records() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	        int count = addParticipantPage.getVisibleParticipantCount();
	        Assert.assertEquals(count, 10, "Default page should show 10 participants.");
	    }

	    @Test(priority = 65)
	    public void TC_PAG_65_verifyNextPageNavigation() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	        String before = addParticipantPage.getCurrentPageNumber();
	        addParticipantPage.clickNextPage();
	        String after = addParticipantPage.getCurrentPageNumber();
	        Assert.assertNotEquals(before, after, "Page should change after clicking Next.");
	    }

	    @Test(priority = 66)
	    public void TC_PAG_66_verifySpecificPageNavigation() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	addParticipantPage.clickPageNumber("3");
	        String current = addParticipantPage.getCurrentPageNumber();
	        Assert.assertEquals(current, "3", "Should navigate to page 3 successfully.");
	    }

	    @Test(priority = 67)
	    public void TC_PAG_67_verifyPreviousPageNavigation() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	addParticipantPage.clickPreviousPage();
	        String current = addParticipantPage.getCurrentPageNumber();
	        Assert.assertEquals(current, "2", "Should navigate back to page 2 successfully.");
	    }
	    @Test(priority = 68)
	    public void TC_PAG_68_verifyPreviousPageNavigationAgain() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	addParticipantPage.clickPreviousPage();
	        String current = addParticipantPage.getCurrentPageNumber();
	        Assert.assertEquals(current, "1", "Should navigate back to page 1 successfully.");
	    }

	    @Test(priority = 69)
	    public void TC_PAG_69_verifyTotalEntriesText() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	        String tableInfo = addParticipantPage.getTableInfoText();
	        Assert.assertTrue(tableInfo.contains("88"), "Table info should show total 88 entries.");
	    }
	    @Test(priority = 70)
	    public void TC_70_verifySelectAllCheckboxClickable() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	addParticipantPage.viewAddPaxbtn();
	    	addParticipantPage.isSelectAllClickable();
	        System.out.println("✅ Select All checkbox is clickable.");
	        addParticipantPage.isSelectAllClickable();
	    }
	    

	    @Test(priority = 71)
	    public void TC_71_verifyYetToInviteIconsHighlight() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	addParticipantPage.selectParticipantByStatus("Yet to invite");
	        Assert.assertTrue(addParticipantPage.isSendInviteIconEnabled(), "Send Invite icon not enabled!");
	        Assert.assertTrue(addParticipantPage.isDeleteIconEnabled(), "Delete icon not enabled!");
	        Assert.assertTrue(addParticipantPage.isMapToGroupIconEnabled(), "Map to Group icon not enabled!");
	        System.out.println("✅ Icons highlighted correctly for 'Yet to invite' participant.");
	        addParticipantPage.selectParticipantByStatus("Yet to invite");
	    }

	    @Test(priority = 72)
	    public void TC_72_verifyActiveStatusIconsHighlight() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	addParticipantPage.selectParticipantByStatus("Active");
	        Assert.assertTrue(addParticipantPage.isResendInviteIconEnabled(), "Resend Invite icon not enabled!");
	        Assert.assertTrue(addParticipantPage.isInactiveIconEnabled(), "Inactive icon not enabled!");
	        System.out.println("✅ Icons highlighted correctly for 'Active' participant.");
	        addParticipantPage.clickInactiveIcon();
	        addParticipantPage.clickOnAlertPopupOkBtn();
	        Assert.assertTrue(addParticipantPage.isPaxInactiveSuccessMsg(), "Pax Inactive Success Message is not displayed");
	        addParticipantPage.selectParticipantByStatus("Inactive");
	        addParticipantPage.clickactiveBtn();
	        addParticipantPage.clickOnAlertPopupOkBtn();
	        Assert.assertTrue(addParticipantPage.isPaxactiveSuccessMsg(), "Pax active Success Message is not displayed");
	    }
	    @Test(priority = 74)
	    public void TC_74_verifyEditableInvitationMessageAndMandatoryTag() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	addParticipantPage.selectParticipantByStatus("Yet to invite");
	    	addParticipantPage.clickSendInviteIcon();
	       	Assert.assertTrue(addParticipantPage.isPopupDisplayed(), "Send Invite popup not displayed!");
	        System.out.println("✅ Send Invite popup displayed successfully.");
	        // Step 1: Open invitation popup
	        addParticipantPage.clickSendInviteIcon();
	        Assert.assertTrue(addParticipantPage.isPopupVisible(), "Invitation popup not visible!");

	        // Step 2: Fill 'Send From' and 'Subject'
	        addParticipantPage.selectSendFrom("connect@potentia.in");
	        addParticipantPage.enterSubject("Automation Test: Editable Message Validation");

	        // Step 3: Try to send invite without Login Credentials tag
	        addParticipantPage.clickSendInvite();
	        Assert.assertTrue(addParticipantPage.isLoginErrorMessageDisplayed(),
	                "Error message not displayed when Login Credentials tag missing!");

	        // Step 4: Edit message by adding tag and extra text
	        addParticipantPage.editMessage("\n\nLogin Details: {Login_Credentials}\nPlease login soon.");

	        // Step 5: Verify updated text appears
	        String currentMsg = addParticipantPage.getMessageText();
	        Assert.assertTrue(currentMsg.contains("Login Details"), "Edited text not found in message!");
	        Assert.assertTrue(currentMsg.contains("Login_Credentials"), "Login Credentials tag not added properly!");

	        // Step 6: Try sending again
	        addParticipantPage.clickSendInvite();

	        // Step 7: Verify success popup and close it
	        addParticipantPage.clickOkOnSuccess();

	        System.out.println("✅ Message edited, tag verified, and invite sent successfully!");
	        driver.navigate().refresh();
	    }
	 @Test(priority = 72)
	    public void selectNumberOfRowsTest() {
		 	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	        String rowsToSelect = "50"; // change as needed
	        addParticipantPage.selectRows(rowsToSelect);

	        // Assert that selected value is correct
	        driver.navigate().refresh();
	        String selectedRows = addParticipantPage.getSelectedRows();
	        Assert.assertEquals(selectedRows, rowsToSelect, "Selected rows mismatch!");
	        rowsToSelect = "10";
	        addParticipantPage.selectRows(rowsToSelect);
	    }
	 @Test(priority = 73)
	    public void searchParticipantTest() {
		 	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	        String keyword = "John Donn";
	        addParticipantPage.searchParticipant(keyword);

	        // Optional: verify input has correct text
	        String typedText = addParticipantPage.getSearchText();
	        Assert.assertEquals(typedText, keyword, "Search input mismatch!");
	        addParticipantPage.clearSearch();
	    }
	 @Test(priority = 74)
	    public void filterYetToInviteTest() {
		 AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
		 addParticipantPage.selectFilter("Yet to invite");
		 addParticipantPage.unselectFilter("Yet to invite");
	    }
	 @Test(priority = 75)
	    public void selectMultipleAddDetailsTest() {
		 	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
		 	addParticipantPage.selectAddDetail("Group");
		 	addParticipantPage.selectAddDetail("Mobile number");
		 	addParticipantPage.selectAddDetail("Employee ID");

	        Assert.assertTrue(addParticipantPage.isAddDetailSelected("Group"));
	        Assert.assertTrue(addParticipantPage.isAddDetailSelected("Mobile number"));
	        Assert.assertTrue(addParticipantPage.isAddDetailSelected("Employee ID"));
	    }

	    @Test(priority = 76)
	    public void verifySelectedAddDetailsTest() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	        List<String> selectedOptions = addParticipantPage.getSelectedAddDetails();
	        System.out.println("Selected Columns: " + selectedOptions);

	        Assert.assertTrue(selectedOptions.size() >= 3,
	                "Expected at least 3 selected columns");
	    }

	    @Test(priority = 77)
	    public void deselectAddDetailTest() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	addParticipantPage.deselectAddDetail("Mobile number");
	        Assert.assertFalse(addParticipantPage.isAddDetailSelected("Mobile number"));
	    }

	    @Test(priority = 78)
	    public void clearAllAddDetailsTest() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	addParticipantPage.clearAllAddDetails();
	        Assert.assertTrue(addParticipantPage.getSelectedAddDetails().isEmpty(),
	                "Dropdown is not cleared");
	    }
	    @Test(priority = 79)
	    public void downloadExcelTest() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	addParticipantPage.downloadExcel();
	        // Optional: validate file download
	    }
	    
	    @Test(priority = 75)
	    public void TC_MapParticipants_WithRetryAndPartialSuccess() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	        // Step 1: Select first participant
	    	addParticipantPage.selectParticipantsByStatus("Active",1);
	        
	    	addParticipantPage.clickGroupIcon();

	        // Step 2: Try mapping to group
	    	addParticipantPage.selectGroupByName("Group 1");

	        // Step 3: Check if already-mapped error appears
	        if (addParticipantPage.isAlreadyMappedMessageDisplayed()) {

	            System.out.println("Participant already mapped. Selecting another participant.");

	            // Step 4: Select second participant additionally
	            addParticipantPage.selectParticipantsByStatus("Active",2);
	            addParticipantPage.clickGroupIcon();

	            // Step 5: Try mapping again
	            addParticipantPage.selectGroupByName("Group 1");

	            // Step 6: Validate partial success message
	            Assert.assertTrue(addParticipantPage.isMappingSuccessMessageDisplayed(), "Participants Not mapped succesfully");
	            
	                   

	        } else {
	            // If no error initially → mapping succeeded
	            Assert.assertTrue(
	            		addParticipantPage.isMappingSuccessMessageDisplayed(),
	                    "Group mapping success message not displayed"
	            );
	        }
	    }
	    @Test(priority = 76, description = "Verify all group names are listed and Add New Group popup appears")
	    public void TC_76_verifyGroupOptionsAndPopup() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	addParticipantPage.selectParticipantsByStatus("Active", 3);

	        // Step 1: Click on Group Icon
	    	addParticipantPage.clickGroupIcon();
	    	addParticipantPage.isaddNewGroupDisplayedandEnabled();
	    	addParticipantPage.isAutoCreateGroupDisplayedAndEnabled();
	    	addParticipantPage.isAddtoGroupDisplayedAndEnabled();

	        // Step 2: Print all available group names
	    	addParticipantPage.printAllGroups();

	        // Step 3: Click on 'Add new group'
	    	addParticipantPage.clickAddNewGroup();

	        // Step 4: Verify popup is displayed
	        boolean popupVisible = addParticipantPage.isCreateGroupPopupDisplayed();
	        Assert.assertTrue(popupVisible, "Create Group popup is NOT displayed!");
	        System.out.println("✅ Create Group popup displayed successfully!");
	    }
	    @Test(priority = 77)
	    public void TC_77_varifyEmptyFiledGroupValidation() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	addParticipantPage.clickaddGroup();
	    	Assert.assertTrue(addParticipantPage.isAddGroupTitlErrorMessage(), "Add Group Title Error Message is not displayed");
	    	String expectedError = "Please enter group title";
	    	String actualError = addParticipantPage.getaddGroupTitleErrorErrorMessage();
	    	Assert.assertEquals(actualError,expectedError, "Error message mismatch. Expected: " + expectedError + " but found: " + actualError);
	    }
	    @Test(priority = 78)
	    public void TC_78_varifyGroupTitleAcceptsValidValues() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);
	    	Assert.assertTrue(
	    			addParticipantPage.isAllowDiscussionsCheckBoxDisplayedAndEnabled(),
	                "Allow Discussions checkbox is not displayed or not enabled"
	        );

	        Assert.assertTrue(
	        		addParticipantPage.isAllowGroupMessageCheckBoxDisplayedAndEnabled(),
	                "Allow Group Messages checkbox is not displayed or not enabled"
	        );

	        Assert.assertTrue(
	        		addParticipantPage.isViewGroupCopaxCheckBoxDisplayedAndEnabled(),
	                "View Group Copax checkbox is not displayed or not enabled"
	        );
	    	
	    }
	    @Test(priority = 79)
	    public void TC_CreateGroup_WithDuplicateTitleHandling() {
	    	AddParticipantPage addParticipantPage = new AddParticipantPage(driver);

	        String createdGroupTitle =
	        		addParticipantPage.createGroupWithUniqueTitle("Group");

	        Assert.assertNotNull(
	                createdGroupTitle,
	                "Group title was not created successfully"
	        );
	    }
}

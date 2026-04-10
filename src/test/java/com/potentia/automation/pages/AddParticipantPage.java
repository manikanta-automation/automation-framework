package com.potentia.automation.pages;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AddParticipantPage {
	private WebDriver driver;
	private WebDriverWait wait;
	private final JavascriptExecutor js;
	Actions actions;
	
	public AddParticipantPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		this.js = (JavascriptExecutor) driver;
		PageFactory.initElements(driver, this);
	}
	 // Top-level tabs
    private final By activeTab = By.id("activeBatch");
    private final By plannedTab = By.id("plannedBatch");
    private final By inactiveTab = By.id("inactiveBatch");

    // Batch list
    private final By batchListContainer = By.cssSelector(".bach_info_list");
    private final By batchCards = By.cssSelector(".bach_info_list .list-item-container");
	private By participantlink = By.xpath("//a[contains(normalize-space(.), 'Participants')]");
	private By checkingParticipantScreen = By.id("pills-home-tab");
	private By groupsBtn = By.id("pills-groups-tab2");
	private By addPaxBtn = By.xpath("//a[contains(normalize-space(.), '+ Pax')]");
	private By addManuallyTab = By.id("home-tab"); 
	private By uploadTab = By.id("profile-tab"); 
	private By copyfromOtherBatchTab = By.id("contact-tab"); 
	private By paxAddManuallySubmitBtn = By.xpath("//div[@id='addmanually']//a[text()='Submit' and contains(@class,'custom-btn-primary')]");
	private By paxAddManuallyCancelBtn = By.xpath("//div[@id='addmanually']//a[text()= 'Cancel']");
	private By profileImg = By.id("image_select");
	private By firstName = By.id("fname");
	private By lastName = By.id("lname");
	private By salutationDropdown = By.id("salutation");
	private By genderMale = By.id("gender_male");
	private By genderFemale = By.id("gender_female");
	private By genderOther = By.id("gender_other");
	private By callName = By.id("call_name");
	private By emailField = By.id("email");
	private By addititonalInformationDropdown = By.id("accordioninformation");
	private By mobileNumber = By.id("mobile");
	private By employeeId = By.id("empid");
	private By designation = By.id("designation");
	private By department = By.id("department");
	private By location = By.id("location");
	private By about_self = By.id("about_self");
	private By profilelink = By.id("form-validation-field-0");

    @FindBy(xpath = "//a[contains(@class,'btn-success')]")
    WebElement addButton;

    @FindBy(xpath = "//a[contains(@class,'btn-danger')]")
    WebElement deleteButton;
    private By uploadProfilePicInput = By.id("selectImgFile");
    private By profileImage  = By.id("image_select");
    private By errorMessage = By.xpath("//div[contains(text(),'This field is required')]");
    private By emailErrorMessage = By.xpath("//*[contains(text(), 'Invalid email address')]");
    private final String jsonFilePath = System.getProperty("user.dir") + "/participantData.json";
    private By closeInvitation = By.xpath("//a[contains(text(), 'Close')]");
    private By participantEditIcon = By.xpath("//a[normalize-space(text())='Jhon Donn']/ancestor::tr//a[@title='Edit Pax']");
    private By editFirstName = By.id("edit_fname");
    private By editLastName = By.id("edit_lname");
    private By editSalutation = By.id("edit_salutation");
    private By editCallName = By.id("edit_call_name");
    private By editEmail = By.id("edit_email");
    private By editaddititonalInformationDropdown = By.id("editPaxAccordion");
    private By editMobileNumber = By.id("edit_mobile");
    private By editEmployeeId = By.id("edit_empid");
    private By editDesignation = By.id("edit_designation");
    private By editDepartment = By.id("edit_department");
    private By editLocation = By.id("edit_location");
    private By editAboutSelf = By.id("edit_about_self");
    private By editPassword = By.id("edit_password");
    private By eyeIcon = By.xpath("//span[contains(@class, 'input-group-text toggle-password-input')]");
    private By paxUpdateBtn = By.xpath("//a[text()='Update' and contains(@class,'custom-btn-primary')]");
    private By paginationButtons = By.xpath("//a[contains(@class,'paginate_button') and not(contains(@class,'disabled'))]");
    private By nextButton = By.id("paxDataTable_next");
    private By previousButton = By.id("paxDataTable_previous");
    private By currentPage = By.xpath("//a[contains(@class,'paginate_button current')]");
    private By participantRows = By.xpath("//table[@id='paxDataTable']//tbody/tr");
    private By tableInfo = By.id("paxDataTable_info");
    private By selectAllCheckbox = By.id("chk_checkAllPax");
    private By participantsRows = By.cssSelector("tbody#sortParticipantResp tr");
    private By sendInviteIcon = By.xpath("//a[contains(@class,'btn-invite-pax')]");
    private By resendInviteIcon = By.xpath("//a[contains(@class,'btn-resend')]");
    private By deleteIcon = By.xpath("//a[contains(@class,'delete-user')]");
    private By mapToGroupIcon = By.xpath("//div[contains(@class,'group-pax-icon')]");
    private By activeIcon = By.xpath("(//a[contains(@class, 'activate-user')])[1]");
    private By inactiveIcon = By.xpath("//a[contains(@class,'inactivate-user me-2')]");
    private By popupDialog = By.xpath("//div[contains(@class,'modal') and contains(@style,'display: block')]");
    private By inactivealertOkBtn = By.xpath("//button[contains(@class,'swal-button--confirm') and text()='OK']");
    private By popupTitle = By.xpath("//h5[@id='invitePaxModalLabel' and text()='Invitation']");
    private By sendFromDropdown = By.id("inviteFrom");
    private By subjectField = By.id("paxInvSubject");
    private By messageFrame = By.xpath("//div[contains(@class,'fr-box')]");
    private By messageEditableArea = By.xpath("//div[@class='fr-element fr-view']");
    private By sendInviteButton = By.xpath("//button[contains(text(),'Send Invite')]");
    private By previewButton = By.xpath("//button[contains(text(),'Preview')]");
    private By cancelButton = By.xpath("//button[contains(text(),'Cancel')]");
    private By okButton = By.xpath("//button[contains(@class,'swal-button--confirm') and text()='OK']");
    private By groupIcon = By.cssSelector(".group-pax-icon button"); 
    private By dropdownMenu = By.cssSelector(".dropdown-menu.show");
    private By groupList = By.cssSelector(".groups-dropdown li > a");
    private By addNewGroupOption = By.xpath("//a[contains(text(),'Add new group')]");
    private By autoCreateGroup = By.xpath("//a[contains(text(),'Auto create group')]");
    private By addToGroup = By.xpath("//p[contains(text(),'Add to Group')]");
    
    private By createGroupPopup = By.id("addGroupModalLabel");
    private By addGrouptitle = By.id("groupTitle");
    private By allowDiscussionInput = By.id("allow_group_discussions");
    private By allowDiscussionLabel =
            By.xpath("//input[@id='allow_group_discussions']/parent::label");
    private By allowGroupMessagesInput = By.id("allow_group_messages");
    private By allowGroupMessagesLabel =
            By.xpath("//input[@id='allow_group_messages']/parent::label");
    private By viewGroupCopaxInput = By.id("view_group_copax");
    private By viewGroupCopaxLabel =
            By.xpath("//input[@id='view_group_copax']/parent::label");
    private By addGroup = By.id("btnAddGroup");
    private By duplicateGroupTitleError = By.xpath("//div[contains(text(),'Group title already exist')]");
    @FindBy(id = "paxDataTable_length")
    private WebElement rowsDropdown;
    @FindBy(id = "searchPax")
    private WebElement searchInput;
    @FindBy(id = "paxFilterDropDownMenu")
    private WebElement filterIcon;

    // All filter checkboxes
    @FindBy(css = "input.chk-filter-pax")
    private List<WebElement> filterCheckboxes;
    @FindBy(id = "paxAddlDetails")
    private WebElement addDetailsDropdown;
    @FindBy(id = "paxDownloadDropDownMenu")
    private WebElement paxDowload;

    
    
	
    public void waitForMyBatchesPageToLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(batchListContainer));
        wait.until(ExpectedConditions.visibilityOfElementLocated(activeTab));
    }

    /**
     * Searches batch in Active -> Planned -> Inactive tabs and clicks the batch when found.
     * Returns true if batch is found and clicked, otherwise false.
     */
    public boolean clickBatchByNameAcrossTabs(String batchName) {
        waitForMyBatchesPageToLoad();

        List<By> tabsToSearch = Arrays.asList(activeTab, plannedTab, inactiveTab);

        for (By tab : tabsToSearch) {
            openTab(tab);

            if (isBatchPresentInCurrentTab(batchName)) {
                clickBatchInCurrentTab(batchName);
                return true;
            }
        }
        return false;
    }

    private void openTab(By tabLocator) {
        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(tabLocator));
        scrollIntoView(tab);
        safeClick(tab);

        waitForBatchListRefresh();
        wait.until(ExpectedConditions.visibilityOfElementLocated(batchListContainer));
    }

    private boolean isBatchPresentInCurrentTab(String batchName) {
        String normalizedBatchName = normalize(batchName);

        List<WebElement> cards = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(batchCards));

        for (WebElement card : cards) {
            try {
                if (!card.isDisplayed()) {
                    continue;
                }

                String dataTitle = normalize(card.getAttribute("data-title"));
                String visibleTitle = "";

                List<WebElement> titleElements = card.findElements(By.cssSelector(".list-item-title h4"));
                if (!titleElements.isEmpty()) {
                    visibleTitle = normalize(titleElements.get(0).getText());
                }

                if (normalizedBatchName.equals(dataTitle) || normalizedBatchName.equals(visibleTitle)) {
                    return true;
                }
            } catch (StaleElementReferenceException e) {
                return isBatchPresentInCurrentTab(batchName);
            }
        }
        return false;
    }

    private void clickBatchInCurrentTab(String batchName) {
        String normalizedBatchName = normalize(batchName);

        List<WebElement> cards = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(batchCards));

        for (WebElement card : cards) {
            try {
                if (!card.isDisplayed()) {
                    continue;
                }

                String dataTitle = normalize(card.getAttribute("data-title"));
                String visibleTitle = "";

                List<WebElement> titleElements = card.findElements(By.cssSelector(".list-item-title h4"));
                if (!titleElements.isEmpty()) {
                    visibleTitle = normalize(titleElements.get(0).getText());
                }

                if (normalizedBatchName.equals(dataTitle) || normalizedBatchName.equals(visibleTitle)) {
                    WebElement batchLink = card.findElement(By.cssSelector(".list-item-title a"));
                    scrollIntoView(batchLink);
                    safeClick(batchLink);
                    return;
                }
            } catch (StaleElementReferenceException e) {
                clickBatchInCurrentTab(batchName);
                return;
            }
        }

        throw new NoSuchElementException("Batch not found in current tab: " + batchName);
    }

    private void waitForBatchListRefresh() {
        try {
            wait.until((ExpectedCondition<Boolean>) driver -> {
                List<WebElement> cards = driver.findElements(batchCards);
                return cards.size() >= 0;
            });
        } catch (TimeoutException e) {
            // Intentionally ignored; page may still be usable
        }
    }

    private void safeClick(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", element);
        }
    }

    private void scrollIntoView(WebElement element) {
        js.executeScript(
            "arguments[0].scrollIntoView({block:'center', inline:'nearest'});",
            element
        );
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().replaceAll("\\s+", " ").toLowerCase();
    }
	
	public void clickSuperClient(String clientName) {
		By clientlocater = By.xpath("//a[text() = \'"+clientName+"\']");
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	        
	     wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("preloader")));

		driver.findElement(clientlocater).click();
	}
	public void clickBatchByName(String existingBatchName) {
		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("preloader")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text() = 'My Batches']")));
        By exsitingBatch = By.xpath("//h4[text() = \'"+existingBatchName+"\']");
        driver.findElement(exsitingBatch).click();
   	}
	public boolean isParticipantsLinkDisplayed() {
		return driver.findElement(participantlink).isDisplayed();
	}
	public void participantLinkClickable() {
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("preloader")));
		driver.findElement(participantlink).click();
		
	}
	public boolean isParticipantPageDisplayed () {
		WebElement participantScreen = wait.until(ExpectedConditions.visibilityOfElementLocated(checkingParticipantScreen));
		String expectedHeading = "Participants";
		String actualHeading = participantScreen.getText().trim();
		return expectedHeading.equalsIgnoreCase(actualHeading);	
	}
	public void clickParticipantTab() {
		 wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("preloader")));
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		    // Wait for any toast / loader to disappear
		    wait.until(ExpectedConditions.invisibilityOfElementLocated(
		            By.cssSelector(".toast, .alert, .modal-backdrop")
		    ));

		    WebElement tab = wait.until(
		            ExpectedConditions.presenceOfElementLocated(
		                    By.cssSelector("button[data-bs-target='#pills-participants']")
		            )
		    );

		    ((JavascriptExecutor) driver)
		            .executeScript("arguments[0].click();", tab);

		    // Verify Participants tab content is visible
		    wait.until(ExpectedConditions.visibilityOfElementLocated(
		            By.id("pills-participants")
		    ));
	}
	public boolean isParticipantBtnDisplayed() {
		return driver.findElement(checkingParticipantScreen).isDisplayed();
	}
	public boolean isGroupsBtnDisplayed() {
		return driver.findElement(groupsBtn).isDisplayed();
	}
	public boolean isAddPaxBtnDisplayed() {
		return driver.findElement(addPaxBtn).isDisplayed();
	}
	public void clickAddPaxBtn() {
		driver.findElement(addPaxBtn).click();
	}
	public boolean isAddManuallyTabDisplayed() {
		WebElement addManually = wait.until(ExpectedConditions.visibilityOfElementLocated(addManuallyTab));
		String expectedHeading = "Add Manually";
		String actualHeading = addManually.getText().trim();
		return expectedHeading.equalsIgnoreCase(actualHeading);	
	}
	public boolean isUploadTabDisplayed() {
		WebElement upload = wait.until(ExpectedConditions.visibilityOfElementLocated(uploadTab));
		String expectedHeading = "Upload";
		String actualHeading = upload.getText().trim();
		return expectedHeading.equalsIgnoreCase(actualHeading);	
	}
	public boolean isCopyFromOtherBatchTabDisplayed() {
		WebElement copyFromOtherBatch = wait.until(ExpectedConditions.visibilityOfElementLocated(copyfromOtherBatchTab));
		String expectedHeading = "Copy from other Batch";
		String actualHeading = copyFromOtherBatch.getText().trim();
		return expectedHeading.equalsIgnoreCase(actualHeading);	
	}
	public boolean isaddManuallySubmitBtnDisplayed() {
		return driver.findElement(paxAddManuallySubmitBtn).isDisplayed();
	}
	public boolean isAddManuallyCancelBtnDisplayed() {
		return driver.findElement(paxAddManuallyCancelBtn).isDisplayed();
	}
	public void clickAddManuallyTabSubmitBtn() {
		driver.findElement(paxAddManuallySubmitBtn).click();
	}
	public boolean isPaxDetailsMandatoryErrorMessageDisplayed() {
		return driver.findElement(By.xpath("//*[contains(text(), 'All fields marked with an asterix are mandatory. Please provide details before submitting.')]")).isDisplayed();
	}
	public String getPaxDetailsMandatoryErrorMessageDisplayed() {
		WebElement PaxDetailsMandatoryError = driver.findElement(By.xpath("//*[contains(text(), 'All fields marked with an asterix are mandatory. Please provide details before submitting.')]"));
		return PaxDetailsMandatoryError.getText().trim();
	}
	public boolean isProfileImgDisplayed() {
		return driver.findElement(profileImg).isDisplayed();
	}
	
    public void clickUploadProfilePhotoEditIocn() {
    	driver.findElement(uploadProfilePicInput).click();
    }
    public void uploadProfilePhoto(String filePath) {
        WebElement uploadInput = driver.findElement(uploadProfilePicInput);
        uploadInput.sendKeys(filePath);
    }
    public boolean isUploadProfileImageErrorMsgDisplayed() {
    	return driver.findElement(By.xpath("//div[contains(text(),'Sorry we do not recognize this file type')]")).isDisplayed();
    }
    public boolean isProfileImageDisplayed() {
        try {
            WebElement img = wait.until(ExpectedConditions.visibilityOfElementLocated(profileImage));
            return img.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    public boolean isFirstNamedisplayed() {
		return driver.findElement(firstName).isDisplayed();
	}
    public boolean isFirstNameEnabled() {
		return driver.findElement(firstName).isEnabled();
	}
    public void enterFirstName(String name) {
        WebElement input = driver.findElement(firstName);
        input.clear();
        input.sendKeys(name);
    }

    public String getFirstNameValue() {
        return driver.findElement(firstName).getAttribute("value");
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return driver.findElement(errorMessage).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void clearFirstName() {
        driver.findElement(firstName).clear();
    }
    public boolean isLastNameDisplayed() {
		return driver.findElement(lastName).isDisplayed();
	}
    public boolean isLastNameEnabled() {
		return driver.findElement(lastName).isEnabled();
	}
    public void enterLastName(String name) {
        WebElement input = driver.findElement(lastName);
        input.clear();
        input.sendKeys(name);
    }
    public String getLastNameValue() {
        return driver.findElement(lastName).getAttribute("value");
    }
    public void clearLastName() {
        driver.findElement(lastName).clear();
    }
    public boolean isSalutationDisplayed() {
		return driver.findElement(salutationDropdown).isDisplayed();
	}
    public boolean isSalutationEnabled() {
		return driver.findElement(salutationDropdown).isEnabled();
	}
    public String getDefaultSelectedOption() {
        Select select = new Select(driver.findElement(salutationDropdown));
        return select.getFirstSelectedOption().getText().trim();
    }

    public List<WebElement> getAllOptions() {
        Select select = new Select(driver.findElement(salutationDropdown));
        return select.getOptions();
    }

    public void selectSalutation(String salutation) {
        Select select = new Select(driver.findElement(salutationDropdown));
        select.selectByVisibleText(salutation);
    }

    public String getSelectedSalutation() {
        Select select = new Select(driver.findElement(salutationDropdown));
        return select.getFirstSelectedOption().getText().trim();
    }
    public boolean isGenderMaleDisplayed() {
		return driver.findElement(genderMale).isDisplayed();
	}
    public boolean isGenderMaleEnabled() {
    	return driver.findElement(genderMale).isEnabled();
    }
    public void clickGenderMaleRadioButton() {
    	driver.findElement(genderMale).click();
    }
    public boolean isGenderMaleSelected() {
    	return driver.findElement(genderMale).isSelected();
    }
	public boolean isGenderFemaleDisplayed() {
		return driver.findElement(genderFemale).isDisplayed();
	}
	public boolean isGenderFemaleEnabled() {
    	return driver.findElement(genderFemale).isEnabled();
    }
    public void clickGenderFemaleRadioButton() {
    	driver.findElement(genderFemale).click();
    }
    public boolean isGenderFemaleSelected() {
    	return driver.findElement(genderFemale).isSelected();
    }
	public boolean isGenderOtherDisplayed() {
		return driver.findElement(genderOther).isDisplayed();
	}
	public boolean isGenderOtherEnabled() {
    	return driver.findElement(genderOther).isEnabled();
    }
    public void clickGenderOtherRadioButton() {
    	driver.findElement(genderOther).click();
    }
    public boolean isGenderOtherSelected() {
    	return driver.findElement(genderOther).isSelected();
    }
    public boolean isCallNameDisplayed() {
		return driver.findElement(callName).isDisplayed();
	}
    public boolean isCallNameEnabled() {
		return driver.findElement(callName).isEnabled();
	}
    public void enterCallName(String call_name) {
        WebElement input = driver.findElement(callName);
        input.clear();
        input.sendKeys(call_name);
    }
    public String getCallNameValue() {
        return driver.findElement(callName).getAttribute("value");
    }
    public void clearCallName() {
        driver.findElement(callName).clear();
    }
    public boolean isEmailVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).isDisplayed();
    }

    public boolean isEmailEnabled() {
        return driver.findElement(emailField).isEnabled();
    }

    public void enterEmail(String email) {
        WebElement input = driver.findElement(emailField);
        input.clear();
        input.sendKeys(email);
    }

    public String getEmailValue() {
        return driver.findElement(emailField).getAttribute("value");
    }

    public boolean isEmailErrorMessageDisplayed() {
        try {
            return driver.findElement(emailErrorMessage).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void clearEmail() {
        driver.findElement(emailField).clear();
    }
    public void clickAdditionalInformationDropdown() {
		driver.findElement(addititonalInformationDropdown).click();
	}
    public boolean isAdditionalInformationDropdownDisplayed() {
    	return driver.findElement(addititonalInformationDropdown).isDisplayed();
    }
    public boolean isMobileNumberDisplayed() {
		return driver.findElement(mobileNumber).isDisplayed();
	}
    public boolean isMobileNumberEnabled() {
    	return driver.findElement(mobileNumber).isEnabled();
    }
    public void clickMobileNumberFiled(String number) {
    	driver.findElement(mobileNumber).clear();
    	driver.findElement(mobileNumber).click();
    	driver.findElement(mobileNumber).sendKeys(number);
    }
    public String getMobileNumberValue() {
    	return driver.findElement(mobileNumber).getAttribute("value");
    }
	public boolean isEmployeeIdDisplayed() {
		return driver.findElement(employeeId).isDisplayed();
	}
	public boolean isEmployeeIdEnabled() {
		return driver.findElement(employeeId).isEnabled();
	}
	public void clickEmployeeIdFiled(String id) {
		driver.findElement(employeeId).clear();
		driver.findElement(employeeId).click();
		driver.findElement(employeeId).sendKeys(id);
	}
	public String getEmployeeIdvalue() {
		return driver.findElement(employeeId).getAttribute("value");
	}
	public boolean isDesignationDisplayed() {
		return driver.findElement(designation).isDisplayed();
	}
	public boolean isDesignationEnabled() {
		return driver.findElement(designation).isEnabled();
	}
	public void clickDesignationFiled(String role) {
		driver.findElement(designation).clear();
		driver.findElement(designation).click();
		driver.findElement(designation).sendKeys(role);
	}
	public String getDesignationValue() {
		return driver.findElement(designation).getAttribute("value");
	}
	public boolean isDepartmentDisplayed() {
		return driver.findElement(department).isDisplayed();
	}
	public boolean isDepartmentEnabled() {
		return driver.findElement(department).isEnabled();
	}
	public void clickDepartmentField(String dept) {
		driver.findElement(department).clear();
		driver.findElement(department).click();
		driver.findElement(department).sendKeys(dept);
	}
	public String getDepartmentvalues() {
		return driver.findElement(department).getAttribute("value");
	}
	public boolean isLocationDisplayed() {
		return driver.findElement(location).isDisplayed();
	}
	public boolean isLocationEnabled() {
		return driver.findElement(location).isEnabled();
	}
	public void clickLocationField(String loc) {
		driver.findElement(location).clear();
		driver.findElement(location).click();
		driver.findElement(location).sendKeys(loc);
	}
	public String getLocationValues() {
		return driver.findElement(location).getAttribute("value");
	}
	public boolean isAboutSelfDisplayed() {
		return driver.findElement(about_self).isDisplayed();
	}
	public boolean isAboutSelfEnabled() {
		return driver.findElement(about_self).isEnabled();
	}
	/**
	 * Force resize the 'About Myself' textarea using JavaScript.
	 * @param newHeight new height in pixels
	 */
	public void resizeAboutselfField(int newHeight) {
		 WebElement field = driver.findElement(about_self);
		    JavascriptExecutor js = (JavascriptExecutor) driver;
		    js.executeScript(
		        "arguments[0].style.height = arguments[1] + 'px';", field, newHeight);
	}
	public void enterInputInAboutSelf(String self) {
		driver.findElement(about_self).clear();
		driver.findElement(about_self).click();
		driver.findElement(about_self).sendKeys(self);
	}
	public String getAboutSlefValues() {
		return driver.findElement(about_self).getAttribute("value");
	}
	public boolean isProfileLinkLabelDisplayed() {
        return driver.findElement(profilelink).isDisplayed();
    }
	public boolean isProfileLinkEnabled() {
		return driver.findElement(profilelink).isEnabled();
	}
	public boolean areButtonsVisible() {
        return addButton.isDisplayed() && deleteButton.isDisplayed();
    }
	public boolean areButtonsEnabled() {
		return addButton.isEnabled() && deleteButton.isEnabled();
	}
	public void clickAddNewProfileLinkFieldButton() {
		addButton.click();
	}
	public int getProfileLinkfieldCount() {
		List<WebElement> count = driver.findElements(profilelink);
		return count.size();
	}
	public void clickDeleteProfileLinkField() {
		deleteButton.click();
	}
	public boolean isProfileLinkDeleteErrorMessage() {
		return driver.findElement(By.xpath("//div[contains(text(),'Participant should have atleast 1 profile link')]")).isDisplayed();
	}
	public String getProfileLinkDeleteErrorMessage() {
		WebElement profileLinkError = driver.findElement(By.xpath("//div[contains(text(), 'Participant should have atleast 1 profile link')]"));
		return profileLinkError.getText().trim();
	}
	public void clickProfileLinkField(String link) {
		driver.findElement(profilelink).clear();
		driver.findElement(profilelink).click();
		driver.findElement(profilelink).sendKeys(link);
	}
	public String getProfileLinkUrl() {
		return driver.findElement(profilelink).getAttribute("value");
	}
	public boolean isInvalidURlErrorMessageDisplayed() {
		return driver.findElement(By.xpath("//div[contains(text(), '* Invalid URL')]")).isDisplayed();
	}
	public String getInvalidURLErrorMsg() {
		WebElement url = driver.findElement(By.xpath("//div[contains(text(), '* Invalid URL')]"));
		return url.getText().trim();
	}

	public void saveDataToJson(Map<String, String> data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(jsonFilePath), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	public Map<String, String> readDataFromJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File(jsonFilePath), new TypeReference<Map<String, String>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
	public void clickCloseInvitationBtn() {
		driver.findElement(closeInvitation).click();
	}
	public void clickParticipantEditIcon() {
		driver.findElement(participantEditIcon).click();
	}
	public String getEditEmailValue() {
		return driver.findElement(editEmail).getText().trim();
		
	}
	public String getEditFirstNameValue () {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    wait.until(driver -> !driver.findElement(editFirstName).getAttribute("value").isEmpty());
	    return driver.findElement(editFirstName).getAttribute("value");
	}
	public String getEditLastNameValue () {
		return driver.findElement(editLastName).getAttribute("value");
	}
	public String getEditSalutationNameValue () {
		return driver.findElement(editSalutation).getAttribute("value");
	}
	public String getEditCallNameValue () {
		return driver.findElement(editCallName).getAttribute("value");
	}
	public void clickEditAdditionalInformationTab() {
		driver.findElement(editaddititonalInformationDropdown).click();
	}
	public String getEditMobileNumberValue () {
		return driver.findElement(editMobileNumber).getAttribute("value");
	}
	public String getEditEmployeeIdValue () {
		return driver.findElement(editEmployeeId).getAttribute("value");
	}
	public String getEditDesignationValue () {
		return driver.findElement(editDesignation).getAttribute("value");
	}
	public String getEditDepartmentValue () {
		return driver.findElement(editDepartment).getAttribute("value");
	}
	public String getEditLocationValue () {
		return driver.findElement(editLocation).getAttribute("value");
	}
	public String getEditAboutSelfValue () {
		return driver.findElement(editAboutSelf).getAttribute("value");
	}
	public void clearEditFirstName() {
		driver.findElement(editFirstName).clear();
	}
	public void enterEditFirstName(String EditFName) {
		driver.findElement(editFirstName).sendKeys(EditFName);
	}
	public boolean isPasswordfieldVisible() {
		return driver.findElement(editPassword).isDisplayed();
	}
	public void clickEyeIconToViewPassword() {
		driver.findElement(eyeIcon).click();
	}
	public void clickOnUpdateBtn() {
		driver.findElement(paxUpdateBtn).click();
	}
	public int getVisibleParticipantCount() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(participantRows));
        return driver.findElements(participantRows).size();
    }

    // ✅ Get current page number
    public String getCurrentPageNumber() {
        return driver.findElement(currentPage).getText().trim();
    }

    // ✅ Click next page
    public void clickNextPage() {
        WebElement nextBtn = wait.until(ExpectedConditions.elementToBeClickable(nextButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextBtn);
        nextBtn.click();
    }

    // ✅ Click previous page
    public void clickPreviousPage() {
        WebElement prevBtn = wait.until(ExpectedConditions.elementToBeClickable(previousButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", prevBtn);
        prevBtn.click();
    }

    // ✅ Click a specific page number dynamically
    public void clickPageNumber(String pageNum) {
        String xpath = "//a[contains(@class,'paginate_button') and normalize-space(text())='" + pageNum + "']";
        WebElement pageButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", pageButton);
        pageButton.click();
    }

    // ✅ Get table info text
    public String getTableInfoText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(tableInfo)).getText();
    }
    public void viewAddPaxbtn() {
    	WebElement participant = wait.until(ExpectedConditions.elementToBeClickable(checkingParticipantScreen));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", participant);
    }
    public boolean  isSelectAllClickable() {
    	 try {
    	        // Locate the visible clickable span next to the checkbox
    	        WebElement selectAllSpan = wait.until(ExpectedConditions.elementToBeClickable(
    	            By.xpath("//input[@id='chk_checkAllPax']/following-sibling::span[@class='checkmark']")
    	        ));

    	        // Use JavaScript click to ensure interaction even if styled/hidden
    	        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", selectAllSpan);
    	        System.out.println("Select All checkbox clicked successfully via visible span.");
    	        return true;

    	    } catch (Exception e) {
    	        e.printStackTrace();
    	        return false;
    	    }
    }

    // ✅ 2. Select participant by status (e.g., "Yet to invite", "Active")
    public void selectParticipantByStatus(String statusText) {
        List<WebElement> rows = driver.findElements(participantRows);
        for (WebElement row : rows) {
            String status = row.findElement(By.xpath(".//td[5]")).getText().trim();
            if (status.equalsIgnoreCase(statusText)) {
                WebElement checkbox = row.findElement(By.xpath(".//input[@type='checkbox']"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
                break;
            }
        }
    }

    // ✅ 3. Check if icons are enabled
    public boolean isSendInviteIconEnabled() {
        return driver.findElement(sendInviteIcon).isEnabled();
    }

    public boolean isDeleteIconEnabled() {
        return driver.findElement(deleteIcon).isEnabled();
    }

    public boolean isMapToGroupIconEnabled() {
        return driver.findElement(mapToGroupIcon).isEnabled();
    }

    public boolean isResendInviteIconEnabled() {
        return driver.findElement(resendInviteIcon).isEnabled();
    }

    public boolean isInactiveIconEnabled() {
        return driver.findElement(inactiveIcon).isEnabled();
    }

    // ✅ 4. Click Send Invite icon
    public boolean clickSendInviteIcon() {
    	try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement sendInviteBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//a[contains(@class,'btn-invite-pax')]")));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", sendInviteBtn);
            Thread.sleep(500);

            try {
                sendInviteBtn.click();
            } catch (ElementClickInterceptedException e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sendInviteBtn);
                System.out.println("Send Invite button clicked using JavaScript as it was intercepted.");
            }

            System.out.println("Send Invite button clicked successfully!");
            return true;

        } catch (Exception e) {
            System.out.println("Failed to click Send Invite button: " + e.getMessage());
            return false;
        }
    }

    // ✅ 5. Verify popup displayed
    public boolean isPopupDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(popupDialog)).isDisplayed();
    }
    public boolean clickInactiveIcon() {
    	try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement inactiveBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//a[contains(@class,'inactivate-user me-2')]")));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", inactiveBtn);
            Thread.sleep(500);

            try {
            	inactiveBtn.click();
            } catch (ElementClickInterceptedException e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", inactiveBtn);
                System.out.println("Send Invite button clicked using JavaScript as it was intercepted.");
            }

            System.out.println("Send Invite button clicked successfully!");
            return true;

        } catch (Exception e) {
            System.out.println("Failed to click Send Invite button: " + e.getMessage());
            return false;
        }
    }
    public void clickOnAlertPopupOkBtn () {
    	driver.findElement(inactivealertOkBtn).click();
    }
    public boolean isPaxInactiveSuccessMsg() {
		return driver.findElement(By.xpath("//div[contains(text(),'1 out of 1 participant(s) have been inactivated successfully')]")).isDisplayed();
	}
    public void clickactiveBtn() {
    	driver.findElement(activeIcon).click();
    }
    public boolean isPaxactiveSuccessMsg() {
    	return driver.findElement(By.xpath("//div[contains(text(), '1 out of 1 participant(s) have been activated successfully')]")).isDisplayed();
    }
    public boolean isPopupVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(popupTitle)).isDisplayed();
    }

    public void selectSendFrom(String emailText) {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(sendFromDropdown));
        Select select = new Select(dropdown);
        select.selectByVisibleText(emailText);
    }

    public void enterSubject(String subjectText) {
        WebElement subject = wait.until(ExpectedConditions.visibilityOfElementLocated(subjectField));
        subject.clear();
        subject.sendKeys(subjectText);
    }

    // ✅ Editable Message Handling (inside rich text editor)
    public void editMessage(String additionalText) {
        WebElement editable = wait.until(ExpectedConditions.visibilityOfElementLocated(messageEditableArea));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", editable);
        editable.click();
        editable.sendKeys(Keys.END);  // Move to end of existing text
        editable.sendKeys("\n" + additionalText);  // Append new text
    }

    // ✅ Get current message content
    public String getMessageText() {
        WebElement editable = wait.until(ExpectedConditions.visibilityOfElementLocated(messageEditableArea));
        return editable.getText();
    }

    public void clickSendInvite() {
        WebElement sendBtn = wait.until(ExpectedConditions.elementToBeClickable(sendInviteButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", sendBtn);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sendBtn);
    }

    public boolean isLoginErrorMessageDisplayed() {
    	return driver.findElement(By.xpath("//div[contains(text(), 'Invitation message must contain Login_Credentials')]")).isDisplayed();
    }

    public void clickOkOnSuccess() {
        try {
            WebElement ok = wait.until(ExpectedConditions.elementToBeClickable(okButton));
            ok.click();
        } catch (Exception e) {
            System.out.println("OK button not found: " + e.getMessage());
        }
    }
    public void selectParticipantsByStatus(String statusText, int count) {
        List<WebElement> rows = driver.findElements(participantRows);
        int selected = 0;

        for (WebElement row : rows) {
            if (selected == count) break;

            String status = row.findElement(By.xpath(".//td[5]"))
                               .getText().trim();

            if (status.equalsIgnoreCase(statusText)) {
                WebElement checkbox = row.findElement(
                        By.xpath(".//input[@type='checkbox']")
                );

                if (!checkbox.isSelected()) {
                    ((JavascriptExecutor) driver)
                            .executeScript("arguments[0].click();", checkbox);
                    selected++;
                }
            }
        }
    }
    public void clickGroupIcon() {
        wait.until(ExpectedConditions.elementToBeClickable(groupIcon)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownMenu));
    }
    public boolean isaddNewGroupDisplayedandEnabled() {
    	 WebElement element = driver.findElement(addNewGroupOption);
    	    return element.isDisplayed() && element.isEnabled();
    }
    public boolean isAutoCreateGroupDisplayedAndEnabled() {
    	WebElement element = driver.findElement(autoCreateGroup);
	    return element.isDisplayed() && element.isEnabled();
    }
    public boolean isAddtoGroupDisplayedAndEnabled() {
    	WebElement element = driver.findElement(addToGroup);
	    return element.isDisplayed() && element.isEnabled();
    }

    // Print all available groups
    public void printAllGroups() {
        List<WebElement> groups = driver.findElements(groupList);
        System.out.println("Available Groups:");
        for (WebElement group : groups) {
            System.out.println("- " + group.getText().trim());
        }
    }

    // Click on Add New Group
    public void clickAddNewGroup() {
        wait.until(ExpectedConditions.elementToBeClickable(addNewGroupOption)).click();
    }

    // Verify Create Group popup is displayed
    public boolean isCreateGroupPopupDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(createGroupPopup));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
    public void addGroupTitleFiled(String groupTitle) {
    	driver.findElement(addGrouptitle).clear();
    	driver.findElement(addGrouptitle).sendKeys(groupTitle);
    }
    public boolean isAllowDiscussionsCheckBoxDisplayedAndEnabled() {
    	WebElement label = driver.findElement(allowDiscussionLabel);
        WebElement input = driver.findElement(allowDiscussionInput);

        return label.isDisplayed() && input.isEnabled();
    }
    public void clickallowDiscussions() {
    	driver.findElement(allowDiscussionLabel).click();
    }
    public boolean isAllowGroupMessageCheckBoxDisplayedAndEnabled() {
    	WebElement label = driver.findElement(allowGroupMessagesLabel);
        WebElement input = driver.findElement(allowGroupMessagesInput);

        return label.isDisplayed() && input.isEnabled();
    }
    public void clickallowGroupMessages() {
    	driver.findElement(allowGroupMessagesLabel).click();
    }
    public boolean isViewGroupCopaxCheckBoxDisplayedAndEnabled() {
    	WebElement label = driver.findElement(viewGroupCopaxLabel);
        WebElement input = driver.findElement(viewGroupCopaxInput);

        return label.isDisplayed() && input.isEnabled();
    }
    public void clickviewGroupCopax() {
    	driver.findElement(viewGroupCopaxLabel).click();
    }
    public void clickaddGroup() {
    	driver.findElement(addGroup).click();
    }
    public boolean isAddGroupTitlErrorMessage() {
		return driver.findElement(By.xpath("//div[contains(text(),'Please enter group title')]")).isDisplayed();
	}
	public String getaddGroupTitleErrorErrorMessage() {
		WebElement profileLinkError = driver.findElement(By.xpath("//div[contains(text(), 'Please enter group title')]"));
		return profileLinkError.getText().trim();
	}
	public boolean isDuplicateGroupTitleErrorDisplayed() {
	    List<WebElement> error =
	            driver.findElements(duplicateGroupTitleError);
	    return !error.isEmpty() && error.get(0).isDisplayed();
	}
	public String createGroupWithUniqueTitle(String baseTitle) {

	    int attempt = 1;
	    String finalTitle;

	    while (true) {
	        finalTitle = baseTitle + "_" + attempt;
	        addGroupTitleFiled(finalTitle);
	        clickaddGroup();

	        // wait briefly for validation
	        try {
	            Thread.sleep(600);
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	        }

	        if (!isDuplicateGroupTitleErrorDisplayed()) {
	            break; // group created
	        }

	        attempt++;
	    }
	    return finalTitle;
	}
	public boolean isAlreadyMappedMessageDisplayed() {
	    return !driver.findElements(
	            By.xpath("//div[contains(text(),'already part of this group')]")
	    ).isEmpty();
	}

	public boolean isMappingSuccessMessageDisplayed() {
	    return !driver.findElements(
	            By.xpath("//div[contains(text(),'added to the group successfully')]")
	    ).isEmpty();
	}

	public String getMappingMessageText() {
	    return driver.findElement(
	            By.xpath("//div[contains(@class,'alert')]")
	    ).getText().trim();
	}
	public void selectGroupByName(String groupName) {
	    By groupLocator =
	            By.xpath("(//a[normalize-space(text())='" + groupName + "'])[1]");

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    WebElement group =
	            wait.until(ExpectedConditions.elementToBeClickable(groupLocator));

	    group.click();
	}
	public void selectRows(String numberOfRows) {
        wait.until(ExpectedConditions.visibilityOf(rowsDropdown));
        Select select = new Select(rowsDropdown);
        select.selectByValue(numberOfRows);
    }

    // Optional: method to get currently selected value
    public String getSelectedRows() {
        Select select = new Select(rowsDropdown);
        return select.getFirstSelectedOption().getText();
    }
    public void searchParticipant(String keyword) {
        wait.until(ExpectedConditions.visibilityOf(searchInput));
        searchInput.clear();
        searchInput.sendKeys(keyword);
        // Optional: press Enter if search triggers on Enter key
        // searchInput.sendKeys(Keys.ENTER);
    }
    public void clearSearch() {
    	wait.until(ExpectedConditions.visibilityOf(searchInput));
    	searchInput.clear();
    }
    public String getSearchText() {
        return searchInput.getAttribute("value");
    }
    public void openFilterDropdown() {
        wait.until(ExpectedConditions.elementToBeClickable(filterIcon)).click();
    }

    // Select filter by name (Yet to invite / Active / Inactive)
    public void selectFilter(String filterName) {
        openFilterDropdown();

        By checkboxLocator = By.xpath(
            "//label[normalize-space(text())='" + filterName + "']//span[contains(@class,'checkmark')]"
        );

        WebElement checkbox = wait.until(
            ExpectedConditions.elementToBeClickable(checkboxLocator)
        );

        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    // Optional: unselect filter
    public void unselectFilter(String filterName) {
    	openFilterDropdown();
        By checkboxLocator = By.xpath(
            "//label[normalize-space(text())='" + filterName + "']//span[contains(@class,'checkmark')]"
        );

        WebElement checkbox = wait.until(
            ExpectedConditions.visibilityOfElementLocated(checkboxLocator)
        );

        checkbox.click();
    }

    private Select getSelect() {
        wait.until(ExpectedConditions.visibilityOf(addDetailsDropdown));
        return new Select(addDetailsDropdown);
    }

    // Select option by visible text
    public void selectAddDetail(String optionText) {
        Select select = getSelect();
        if (!select.isMultiple()) {
            throw new RuntimeException("Dropdown is not multi-select");
        }
        select.selectByVisibleText(optionText);
    }

    // Select option by value
    public void selectAddDetailByValue(String value) {
        Select select = getSelect();
        select.selectByValue(value);
    }

    // Deselect option
    public void deselectAddDetail(String optionText) {
        Select select = getSelect();
        select.deselectByVisibleText(optionText);
    }

    // Clear all selections
    public void clearAllAddDetails() {
        Select select = getSelect();
        select.deselectAll();
    }

    // Get all selected options
    public List<String> getSelectedAddDetails() {
        Select select = getSelect();
        return select.getAllSelectedOptions()
                     .stream()
                     .map(WebElement::getText)
                     .collect(Collectors.toList());
    }

    // Check if an option is selected
    public boolean isAddDetailSelected(String optionText) {
        return getSelectedAddDetails().contains(optionText);
    }
    public void clickonDownloadDropDown() {
    	paxDowload.click();
    }
    public void downloadExcel() {
    	clickonDownloadDropDown();

        By excelLocator = By.xpath(
            "//ul[contains(@class,'dropdown-menu')]//a[normalize-space()='Excel']"
        );

        wait.until(ExpectedConditions.elementToBeClickable(excelLocator)).click();
    }
}

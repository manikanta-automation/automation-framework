package com.potentia.automation.pages;


import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;



public class AddNewBatchPage {
 	private final WebDriver driver;
    private final WebDriverWait wait;
    private final Actions actions;
    private final JavascriptExecutor js;
    
    private static final DateTimeFormatter UI_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public AddNewBatchPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.actions = new Actions(driver);
        this.js = (JavascriptExecutor) driver;
    }

    // ---------- Locators ----------
    private final By pageLoader = By.xpath("//*[contains(@class,'loader') or contains(@class,'preloader')]");
    private final By plannedTab = By.id("plannedBatch");
    private final By myBatchesHeader = By.xpath("//h1[normalize-space()='My Batches']");
    private final By getStartedButton = By.xpath("//a[normalize-space()='Get Started']");
    private final By newBatchButton = By.xpath("//button[normalize-space()='New Batch' or contains(normalize-space(.), 'New Batch')]");
    private final By emptyStateMessage = By.xpath("//*[contains(normalize-space(),'You have no active batches')]");
    private final By activeBatchTab = By.id("activeBatch");
    //private final By createBatchModal = By.id("createBatchModal");
    
    // ========== Create Batch modal locators ==========
    private final By createBatchModal = By.xpath("//div[contains(@class,'modal-content')][.//h5[normalize-space()='Create and publish your batch in 3 simple steps!']]");
    private final By createBatchModalTitle = By.xpath("//h5[normalize-space()='Create and publish your batch in 3 simple steps!']");
    private final By copyBatchButtonInModal = By.xpath("//div[contains(@class,'modal-content')]//a[normalize-space()='Copy Batch']");
    private final By addManuallyButton = By.xpath("//div[contains(@class,'modal-content')]//a[normalize-space()='Add Manually']");
    private final By modalCloseButton = By.xpath("//div[contains(@class,'modal-content')]//button[@aria-label='Close']");
    
    // =========================
    // Basic SetUp locators
    // =========================
    private final By basicSetupHeading = By.xpath("//h3[normalize-space()='Basic Setup']");
    private final By activeTab = By.id("ex3-tab-1");
    private final By programInfoTab = By.id("ex3-tab-2");
    private final By facilitatorInfoTab = By.id("ex3-tab-3");
    private final By messagesTab = By.id("ex3-tab-4");
    private final By batchAddedTab = By.id("ex3-tab-5");

    private final By batchTitleInput = By.id("title");
    private final By batchTitleHelpIcon = By.xpath("//label[@for='title']//em[contains(@class,'info')]");
    private final By descriptionToggle = By.xpath("//a[contains(@class,'collapse-btn') and contains(normalize-space(.),'Description')]");
    private final By descriptionTextArea = By.id("description");
    private final By descriptionHelpIcon = By.xpath("//a[contains(normalize-space(.),'Description')]//em[contains(@class,'info')]");

    private final By saveAndExitButton = By.id("btnBatchBasicSave");
    private final By saveAndContinueButton = By.id("btnBasicSaveNext");
    private final By cancelButton = By.xpath("//button[normalize-space()='Cancel']");

    private final By visibleActivityCheckboxes = By.xpath("(//div[contains(@class,'lkly_check')])[1]//input[@type='checkbox']");
    private final By visibleActivityLabels = By.xpath("//label[normalize-space()='Likely Activities']/following::div[contains(@class,'lkly_check')][1]//div[contains(@class,'form-check')]");

    private final By viewMoreActivitiesToggle = By.xpath("//a[contains(normalize-space(.),'View More Activities')]");
    private final By moreActivitiesContainer = By.id("moreActivityList");
    private final By moreActivityCheckboxes = By.xpath("//div[@id='moreActivityList']//input[@type='checkbox']");
    private final By moreActivityLabels = By.xpath("//div[@id='moreActivityList']//div[contains(@class,'form-check')]");
    
    // Visible likely activities
    private final By assignmentCheckbox = By.id("4");
    private final By referenceMaterialCheckbox = By.id("2");
    
    //Server Not responding
    private final By serverErrorToast = By.xpath("//div[contains(text(),'Server not responding')]");

    // Success message
    private final By batchCreatedSuccessMessage =
            By.xpath("//div[contains(@class,'alert-success') and contains(text(),'Batch has been added successfully')]");
    
    // Program Info Date Locators
    private final By programInfoHeading = By.xpath("//h3[normalize-space()='Program Info']");
    private final By startDateInput = By.id("launch_date");
    private final By endDateInput = By.id("end_date");
    private final By startDateCalendarIcon = By.xpath("//div[@id='start_date']//span[contains(@class,'input-group-text')]");
    private final By endDateCalendarIcon = By.xpath("//div[@id='endDate']//span[contains(@class,'input-group-text')]");

    private final By startDateHelpIcon = By.xpath("//label[@for='launch_date']//em[contains(@class,'info')]");
    private final By endDateHelpIcon = By.xpath("//label[@for='end_date']//em[contains(@class,'info')]");

    private final By programInfoSaveAndExitButton = By.xpath("//div[@id='ex3-tabs-2']//button[normalize-space()='Save & Exit']");
    private final By programInfoSaveAndContinueButton = By.xpath("//div[@id='ex3-tabs-2']//button[normalize-space()='Save & Continue']");

    // Generic validation locator examples
    private final By errorToast = By.xpath("//div[contains(@class,'toast') and contains(@class,'toast-error')]");
    private final By startDateRequiredMessage = By.xpath("//*[contains(normalize-space(),'Please enter Start Date')]");
    private final By endDateRequiredMessage = By.xpath("//*[contains(normalize-space(),'Please enter End Date')]");
    private final By genericDatePickerDays = By.xpath("//td[contains(@class,'day')]");
    
    // Program Info Program Genre and Title locators
    private final By programGenreDropdown = By.id("program");
    private final By programTitleDropdown = By.id("programTitle");
    private final By programGenreHelpIcon = By.xpath("//label[@for='program']//em[contains(@class,'info')]");
    private final By programTitleHelpIcon = By.xpath("//label[@for='programTitle']//em[contains(@class,'info')]");

    private final By programGenreSelect2 = By.xpath("//span[@id='select2-program-container']");
    private final By programGenreSelectedText = By.id("select2-program-container");
    private final By programTitleSelect2 = By.xpath("//span[@id='select2-programTitle-container']");

    private final By programGenreDropdownArrow = By.xpath("//span[@id='select2-program-container']/ancestor::span[contains(@class,'select2-selection')]");
    private final By programTitleDropdownArrow = By.xpath("//span[@id='select2-programTitle-container']/ancestor::span[contains(@class,'select2-selection')]");

    private final By select2SearchInput = By.xpath("//input[contains(@class,'select2-search__field')]");
    private final By select2Results = By.xpath("//ul[contains(@class,'select2-results__options')]//li");
    private final By select2NoResults = By.xpath("//li[contains(@class,'select2-results__message') and normalize-space()='No results found']");

    private final By addGenreButton = By.xpath("//a[contains(normalize-space(.),'+ Genre')]");
    private final By addTitleButton = By.id("btnAddProgramTitle");

    // ---------------- Add Program Genre popup ----------------
    private final By addProgramGenreModal = By.xpath("//h5[normalize-space()='Add Program Genre']/ancestor::div[contains(@class,'modal-content')]");
    private final By addProgramGenreModalTitle = By.id("addProgramModalLabel");
    private final By addProgramGenreCloseButton = By.xpath("//h5[normalize-space()='Add Program Genre']/ancestor::div[contains(@class,'modal-content')]//button[@aria-label='Close']");
    private final By addProgramGenreCancelButton = By.xpath("//h5[normalize-space()='Add Program Genre']/ancestor::div[contains(@class,'modal-content')]//button[normalize-space()='Cancel']");
    private final By addProgramGenreAddButton = By.id("btnAddNewProgram");

    private final By programGenreInput = By.id("master_title");
    private final By shortTitleInput = By.id("short_title");
    private final By likelyProgramTitleInputs = By.xpath("//table[@id='tblTitleOptions']//input[@name='title_options[]']");
    private final By addLikelyTitleButtons = By.xpath("//table[@id='tblTitleOptions']//a[contains(@class,'add')]");
    private final By removeLikelyTitleButtons = By.xpath("//table[@id='tblTitleOptions']//a[contains(@class,'delete')]");
    private final By programGenreDescriptionInput = By.id("prog_description");

    // ---------------- Add Program Title popup ----------------
    private final By addProgramTitleModal = By.xpath("//h5[normalize-space()='Add Program Title']/ancestor::div[contains(@class,'modal-content')]");
    private final By addProgramTitleModalTitle = By.id("programModalLabel");
    private final By addProgramTitleCloseButton = By.xpath("//h5[normalize-space()='Add Program Title']/ancestor::div[contains(@class,'modal-content')]//button[@aria-label='Close']");
    private final By addProgramTitleCancelButton = By.xpath("//h5[normalize-space()='Add Program Title']/ancestor::div[contains(@class,'modal-content')]//button[normalize-space()='Cancel']");
    private final By addProgramTitleAddButton = By.id("btnAddProgram");
    private final By newProgramTitleInput = By.id("new_program_name");

    // ---------------- Generic errors / toast ----------------
    private final By mandatoryValidationMessage = By.xpath("//*[contains(normalize-space(),'All fields marked with an asterix are mandatory. Please provide details before submitting.')]");
    private final By programGenreLengthValidation = By.xpath("//*[contains(normalize-space(),'Program Genre is too long, max 150 characters allowed')]");
    private final By shortTitleLengthValidation = By.xpath("//*[contains(normalize-space(),'Short title is too long, max 150 characters allowed')]");
    private final By likelyProgramTitleLengthValidation = By.xpath("//*[contains(normalize-space(),'Likely Program Title is too long, max 150 characters allowed')]");
    private final By descriptionLengthValidation = By.xpath("//*[contains(normalize-space(),'Description is too long, max 1000 characters allowed')]");
    private final By programTitleLengthValidation = By.xpath("//*[contains(normalize-space(),'Program title is too long, max 150 characters allowed')]");
    
    // Target Audience Locators
    private final By targetAudienceLabel = By.xpath("//label[@for='category']");
    private final By targetAudienceDropdown = By.id("category");
    private final By targetAudienceOptions = By.xpath("//select[@id='category']/option");
    
    // =========================
    // Client dropdown
    // =========================
    private final By clientDropdownField = By.id("client");
    private final By clientFieldBlock = By.xpath("//label[normalize-space()='Client']/ancestor::div[contains(@class,'mb-3')]");
    private final By clientDropdownArrow = By.xpath("//label[normalize-space()='Client']/ancestor::div[contains(@class,'mb-3')]//span[@class='select2-selection__arrow']");
    private final By clientDropdownRenderedText = By.id("select2-client-container");
    private final By clientOptions = By.xpath("//ul[@id='select2-client-results']/li");

    private final By clientSearchBox = By.xpath("//input[@class='select2-search__field' and @placeholder='Search']");
    private final By clientResultsContainer = By.id("select2-client-results");
    private final By noResultsFound = By.xpath("//ul[@id='select2-client-results']//li[normalize-space()='No results found']");
    // =========================
    // + Client button
    // =========================
    private final By addClientButton = By.xpath("//a[@data-bs-target='#addClient-modal' and normalize-space()='+ Client']");
    // =========================
    // Add Client popup
    // =========================
    private final By addClientModal = By.xpath("//div[contains(@class,'modal-content') and contains(@class,'add-client')]");
    private final By addClientModalTitle = By.id("clientModalLabel");
    private final By clientNameField = By.id("name");
    private final By industryField = By.id("industry");
    private final By logoInput = By.id("logo");
    private final By logoPreviewImage = By.id("image_select");
    private final By removeLogoIcon = By.cssSelector("a.reset-select-image");
    private final By deleteLogoConfirmPopup = By.xpath("//*[contains(text(),'Are you sure you want to delete the logo?')]");
    private final By deleteLogoCancelButton = By.xpath("//div[contains(@class,'swal-modal') and not(contains(@style,'display: none'))]//button[normalize-space()='Cancel']");
    private final By deleteLogoOkButton = By.xpath("//button[normalize-space()='OK']");
    private final By clientAddButtonInPopup = By.id("btnAddClient");
    private final By clientCancelButtonInPopup = By.xpath("//div[contains(@class,'modal-footer')]//button[@data-bs-dismiss='modal' and normalize-space()='Cancel']");

    // =========================
    // Common messages
    // =========================
    private final By mandatoryErrorMessage = By.xpath("//*[contains(text(),'All fields marked with an asterix are mandatory')]");
    private final By duplicateClientErrorMessage = By.xpath("//*[contains(text(),'This client already exists. Please add a client with a different name.')]");
    private final By clientNameLengthErrorMessage = By.xpath("//*[contains(text(),'Client Name is too long, max 150 characters allowed')]");
    private final By industryLengthErrorMessage = By.xpath("//*[contains(text(),'Industry is too long, max 150 characters allowed')]");
    private final By logoDeletedSuccessMessage = By.xpath("//*[contains(text(),'Logo deleted successfully')]");
    private final By invalidLogoErrorMessage = By.xpath("//*[contains(text(),'Sorry we do not recognize this file type. Please upload png, jpg, gif file formats only.')]");
    private final By serverErrorMessage = By.xpath("//*[contains(text(),'Server not responding')]");
    private final By programInfoSaveAndExitBtn = By.xpath("(//div[contains(@class,'save_cncl_btns')]//button[normalize-space()='Save & Exit'])[2]");
    private final By programInfoSaveAndContinue = By.xpath("(//div[contains(@class,'save_cncl_btns')]//button[normalize-space()='Save & Continue'])[2]");
    
    //===========
    //Facilitator info
    //===========
    private final By facilitatorInfoPane =
            By.xpath("//div[@id='ex3-tabs-3' and contains(@class,'active') and contains(@class,'show')]");
    private final By facilitatorInfoHeading =
            By.xpath("//div[@id='ex3-tabs-3']//h3[normalize-space()='Facilitator Info']");
    private final By facilitatorRows =
            By.xpath("//div[@id='ex3-tabs-3']//div[contains(@class,'batch-facilitator-list')]//div[contains(@class,'batch-facilitator')]");
    private final By firstFacilitatorRow =
            By.xpath("(//div[@id='ex3-tabs-3']//div[contains(@class,'batch-facilitator-list')]//div[contains(@class,'batch-facilitator')])[1]");

    private final By firstFacilitatorNameDropdown =
            By.xpath("(//div[@id='ex3-tabs-3']//select[@name='facilitators[]'])[1]");
    private final By firstFacilitatorTypeDropdown =
            By.xpath("(//div[@id='ex3-tabs-3']//select[@name='fac_type[]'])[1]");

    private final By firstBatchPrivilegesIcon =
            By.xpath("(//div[@id='ex3-tabs-3']//a[contains(@class,'fac-batch-privileges')])[1]");
    private final By firstAddFacilitatorIcon =
            By.xpath("(//div[@id='ex3-tabs-3']//a[contains(@class,'add-batch-fac')])[1]");
    private final By firstDeleteFacilitatorIcon =
            By.xpath("(//div[@id='ex3-tabs-3']//a[contains(@class,'delete-batch-fac')])[1]");
    private final By typeHelpIcon =
            By.xpath("//div[@id='ex3-tabs-3']//div[contains(@class,'fac_role')]//em[contains(@class,'info')]");
    private final By sendEmailFromHelpIcon =
            By.xpath("//div[@id='ex3-tabs-3']//label[contains(normalize-space(.),'Send Email From')]//em[contains(@class,'info')]");

    private final By sendEmailFromDropdown = By.id("emailFrom");

    private final By facilitatorInfoCancelButton =
            By.xpath("//div[@id='ex3-tabs-3']//button[normalize-space()='Cancel']");
    private final By facilitatorInfoSaveAndExitButton =
            By.xpath("//div[@id='ex3-tabs-3']//button[normalize-space()='Save & Exit']");
    private final By facilitatorInfoSaveAndContinueButton =
            By.xpath("//div[@id='ex3-tabs-3']//button[normalize-space()='Save & Continue']");
    private final By typeHelpTooltip =
            By.xpath("//div[contains(@class,'tooltip') and contains(.,'Role of the Facilitator for this program batch')]");
    private final By sendEmailFromHelpTooltip =
            By.xpath("//div[contains(@class,'tooltip') and contains(.,'Email ID used for sending mails to Pax')]");
    private final By privilegesModal =
            By.xpath("//div[contains(@class,'modal-content')][.//h5[@id='facTypeLabel']]");
    private final By privilegesModalTitle = By.id("facTypeLabel");
    private final By privilegesCloseIcon =
            By.xpath("//h5[@id='facTypeLabel']/ancestor::div[contains(@class,'modal-content')]//button[@aria-label='Close']");
    private final By privilegesCloseButton =
            By.xpath("//h5[@id='facTypeLabel']/ancestor::div[contains(@class,'modal-content')]//button[normalize-space()='Close']");
    private final By privilegesUpdateButton =
            By.xpath("//h5[@id='facTypeLabel']/ancestor::div[contains(@class,'modal-content')]//button[normalize-space()='Update']");

    // Column bulk checkboxes
    private final By addAllCheckbox = By.xpath("//div[contains(@class,'modal-content')][.//h5[@id='facTypeLabel']]//input[@id='add_All']");
    private final By editAllCheckbox = By.id("edit_All");
    private final By deleteAllCheckbox = By.id("delete_All");
    private final By viewAllCheckbox = By.id("view_All");
    private final By closeAllCheckbox = By.id("close_All");
    private final By inviteAllCheckbox = By.id("invite_all");

    // Main privilege row checkboxes
    private final By editBatchCheckbox = By.id("edit_batch");
    private final By deleteBatchCheckbox = By.id("delete_batch");
    private final By viewBatchCheckbox = By.id("view_batch");
    private final By closeBatchCheckbox = By.id("close_batch");

    private final By addUserCheckbox = By.id("add_user");
    private final By deleteUserCheckbox = By.id("delete_user");

    private final By addPaxCheckbox = By.id("add_pax");
    private final By editPaxCheckbox = By.id("edit_pax");
    private final By deletePaxCheckbox = By.id("delete_pax");
    private final By viewPaxCheckbox = By.id("view_pax");
    private final By invitePaxCheckbox = By.id("invite_pax");

    private final By addActivityCheckbox = By.id("add_activity");
    private final By editActivityCheckbox = By.id("edit_activity");
    private final By deleteActivityCheckbox = By.id("delete_activity");
    private final By viewActivityCheckbox = By.id("view_activity");

    // Additional privilege checkboxes
    private final By publishActivityCheckbox = By.id("publish_activity");
    private final By reminderCheckbox = By.id("reminder");
    private final By viewResponseCheckbox = By.id("view_response");
    private final By resetResponseCheckbox = By.id("reset_response");
    private final By affinityGroupingCheckbox = By.id("comment_categorization");
    private final By inboxCheckbox = By.id("add_inbox");
 // =========================
    // Non-editable X mark cells
    // =========================
    private final By batchAddCross =
            By.xpath("//table[contains(@class,'main_prevlg')]//tr[th[normalize-space()='Batch']]//img[contains(@src,'check_cross.png')]");

    private final By batchInviteCross =
            By.xpath("(//table[contains(@class,'main_prevlg')]//tr[th[normalize-space()='Batch']]//img[contains(@src,'check_cross.png')])[2]");

    // Facilitator row
    private final By facilitatorEditCross =
            By.xpath("(//table[contains(@class,'main_prevlg')]//tr[th[normalize-space()='Facilitator']]//img[contains(@src,'check_cross.png')])[1]");

    private final By facilitatorViewCross =
            By.xpath("(//table[contains(@class,'main_prevlg')]//tr[th[normalize-space()='Facilitator']]//img[contains(@src,'check_cross.png')])[2]");

    private final By facilitatorCloseCross =
            By.xpath("(//table[contains(@class,'main_prevlg')]//tr[th[normalize-space()='Facilitator']]//img[contains(@src,'check_cross.png')])[3]");

    private final By facilitatorInviteCross =
            By.xpath("(//table[contains(@class,'main_prevlg')]//tr[th[normalize-space()='Facilitator']]//img[contains(@src,'check_cross.png')])[4]");

    // Pax row
    private final By paxCloseCross =
            By.xpath("//table[contains(@class,'main_prevlg')]//tr[th[normalize-space()='Pax']]//img[contains(@src,'check_cross.png')]");

    // Activity row
    private final By activityCloseCross =
            By.xpath("(//table[contains(@class,'main_prevlg')]//tr[th[normalize-space()='Activity']]//img[contains(@src,'check_cross.png')])[1]");

    private final By activityInviteCross =
            By.xpath("(//table[contains(@class,'main_prevlg')]//tr[th[normalize-space()='Activity']]//img[contains(@src,'check_cross.png')])[2]");


    private final By facilitatorSection =
            By.xpath("//div[@id='ex3-tabs-3' or .//h3[normalize-space()='Facilitator Info']]");

    private final By facilitatorNameDropdowns =
            By.xpath("//div[@id='ex3-tabs-3']//select[@name='facilitators[]']");

    private final By facilitatorTypeDropdowns =
            By.xpath("//div[@id='ex3-tabs-3']//select[@name='fac_type[]']");

    private final By addFacilitatorIcons =
            By.xpath("//div[@id='ex3-tabs-3']//a[contains(@title,'Add facilitator') or contains(@data-bs-original-title,'Add facilitator')]");

    private final By deleteFacilitatorIcons =
            By.xpath("//div[@id='ex3-tabs-3']//a[contains(@onclick,'delete') or contains(@title,'Delete') or contains(@data-bs-original-title,'Delete')]");

    // Delete confirmation popup
    private final By deleteFacilitatorConfirmPopup =
            By.xpath("//div[contains(@class,'swal-text') and contains(text(),'Are you sure you want to delete the facilitator?')]");

    private final By deleteFacilitatorConfirmCancelButton =
            By.xpath("//div[contains(@class,'swal-modal') and not(contains(@style,'display: none'))]//button[normalize-space()='Cancel']");

    private final By deleteFacilitatorConfirmOkButton =
            By.xpath("//div[contains(@class,'swal-modal') and not(contains(@style,'display: none'))]//button[normalize-space()='OK']");

    private final By minimumFacilitatorValidation =
            By.xpath("//*[contains(normalize-space(),'Batch should have at least one facilitator')]");
    
    // ---------- Public methods ----------

    public enum LandingScreen {
        EMPTY_STATE,
        BATCH_LIST,
        UNKNOWN
    }

    // ========== Page load ==========
    public void waitForPageToLoad() {
    	 try {
    	        wait.until(ExpectedConditions.or(
    	                ExpectedConditions.visibilityOfElementLocated(myBatchesHeader),
    	                ExpectedConditions.visibilityOfElementLocated(serverErrorToast)
    	        ));

    	        if (!driver.findElements(serverErrorToast).isEmpty()) {
    	            String errorText = driver.findElement(serverErrorToast).getText().trim();
    	            Assert.fail("My Batches page did not load. Error message displayed: " + errorText);
    	        }

    	        wait.until(ExpectedConditions.visibilityOfElementLocated(myBatchesHeader));

    	    } catch (TimeoutException e) {
    	        Assert.fail("My Batches page did not load within expected time.");
    	    }
    }

    // ========== Landing screen detection ==========
    public LandingScreen getCurrentLandingScreen() {
        waitForPageToLoad();

        if (isElementDisplayed(getStartedButton) || isElementDisplayed(emptyStateMessage)) {
            return LandingScreen.EMPTY_STATE;
        }

        if (isElementDisplayed(newBatchButton) || isElementDisplayed(activeBatchTab)) {
            return LandingScreen.BATCH_LIST;
        }

        return LandingScreen.UNKNOWN;
    }

    // ========== Entry point handling ==========
    public void clickCreateBatchEntryPoint() {
        LandingScreen landingScreen = getCurrentLandingScreen();

        switch (landingScreen) {
            case EMPTY_STATE:
                clickGetStarted();
                break;

            case BATCH_LIST:
                clickNewBatch();
                break;

            default:
                throw new IllegalStateException("Unable to identify My Batches landing screen.");
        }
    }

    public void clickGetStarted() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(getStartedButton));
        scrollIntoView(element);
        safeClick(element);
    }

    public void clickNewBatch() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(newBatchButton));
        scrollIntoView(element);
        safeClick(element);
        waitForCreateBatchModal();
    }

    // ========== Modal methods ==========
    public void waitForCreateBatchModal() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(createBatchModal));
        wait.until(ExpectedConditions.visibilityOfElementLocated(createBatchModalTitle));
    }

    public boolean isCreateBatchModalDisplayed() {
        return isElementDisplayed(createBatchModal);
    }

    public String getCreateBatchModalTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(createBatchModalTitle)).getText().trim();
    }

    public boolean isCopyBatchButtonDisplayed() {
        return isElementDisplayed(copyBatchButtonInModal);
    }

    public boolean isAddManuallyButtonDisplayed() {
        return isElementDisplayed(addManuallyButton);
    }

    public void clickAddManually() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(addManuallyButton));
        scrollIntoView(element);
        safeClick(element);
    }

    public void clickCopyBatchFromModal() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(copyBatchButtonInModal));
        scrollIntoView(element);
        safeClick(element);
    }

    public void closeCreateBatchModal() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(modalCloseButton));
        safeClick(element);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(createBatchModal));
    }

    // Optional: validate navigation after Add Manually click
    public boolean isOnCreateBatchPage() {
        try {
            return wait.until(ExpectedConditions.urlContains("createbatch"));
        } catch (TimeoutException e) {
            return false;
        }
    }

    // ========== Utility methods ==========
    private boolean isElementDisplayed(By locator) {
        try {
            List<WebElement> elements = driver.findElements(locator);
            return !elements.isEmpty() && elements.get(0).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private void safeClick(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
            scrollIntoView(element);
            jsClick(element);
        }
    }

    private void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center', inline:'nearest'});", element
        );
    }

    private void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }
    
 // Generic validation / alert locator
    private final By validationMessageLocator = By.xpath(
        "//*[contains(normalize-space(),'Please enter batch title.') " +
        "or contains(normalize-space(),'Batch title is too long, max 150 characters allowed') " +
        "or contains(normalize-space(),'Batch description is too long, max 1000 characters allowed')]"
    );

    // =========================
    // Tab validations
    // =========================
    public boolean isBasicSetupTabActive() {
        WebElement tab = wait.until(ExpectedConditions.visibilityOfElementLocated(activeTab));
        String classes = tab.getAttribute("class");
        String ariaSelected = tab.getAttribute("aria-selected");
        return classes.contains("active") && "true".equalsIgnoreCase(ariaSelected);
    }

//    public boolean isTabDisabled(By tabLocator) {
//        WebElement tab = wait.until(ExpectedConditions.visibilityOfElementLocated(tabLocator));
//        String classes = tab.getAttribute("class");
//        return classes != null && classes.contains("disabled");
//    }

    public boolean areRemainingTabsDisabled() {
        return isTabDisabled(programInfoTab)
                && isTabDisabled(facilitatorInfoTab)
                && isTabDisabled(messagesTab)
                && isTabDisabled(batchAddedTab);
    }

    public boolean isTabClickable(By tabLocator) {
        try {
            WebElement tab = wait.until(ExpectedConditions.visibilityOfElementLocated(tabLocator));
            return tab.isEnabled() && !tab.getAttribute("class").contains("disabled");
        } catch (Exception e) {
            return false;
        }
    }

    public String getBasicSetupTabBackgroundColor() {
        WebElement tab = wait.until(ExpectedConditions.visibilityOfElementLocated(activeTab));
        return tab.getCssValue("background-color");
    }

    // =========================
    // Buttons
    // =========================
    public boolean isCancelButtonDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(cancelButton)).isDisplayed();
    }

    public boolean isSaveAndExitButtonDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(saveAndExitButton)).isDisplayed();
    }

    public boolean isSaveAndContinueButtonDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(saveAndContinueButton)).isDisplayed();
    }

    public void clickSaveAndExit() {
        safeClick(wait.until(ExpectedConditions.elementToBeClickable(saveAndExitButton)));
    }

    public void clickSaveAndContinue() {
    	WebElement button = wait.until(ExpectedConditions.elementToBeClickable(saveAndContinueButton));
        scrollIntoView(button);
        safeClick(button);
    }
    
 // =========================
    // Batch Title
    // =========================
    public void enterBatchTitle(String value) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(batchTitleInput));
        scrollIntoView(input);
        input.clear();
        input.sendKeys(value);
    }

    public String getBatchTitleValue() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(batchTitleInput)).getAttribute("value");
    }

    public String getBatchTitleHelpText() {
        return getTooltipText(batchTitleHelpIcon);
    }
    
    // =========================
    // Description
    // =========================
    public void expandDescription() {
        WebElement toggle = wait.until(ExpectedConditions.elementToBeClickable(descriptionToggle));
        scrollIntoView(toggle);

        if (!isDescriptionExpanded()) {
            safeClick(toggle);
            wait.until(ExpectedConditions.visibilityOfElementLocated(descriptionTextArea));
        }
    }

    public boolean isDescriptionExpanded() {
        try {
            WebElement area = driver.findElement(descriptionTextArea);
            return area.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void enterDescription(String value) {
        expandDescription();
        WebElement area = wait.until(ExpectedConditions.visibilityOfElementLocated(descriptionTextArea));
        area.clear();
        area.sendKeys(value);
    }

    public String getDescriptionValue() {
        expandDescription();
        return wait.until(ExpectedConditions.visibilityOfElementLocated(descriptionTextArea)).getAttribute("value");
    }

    public String getDescriptionHelpText() {
        return getTooltipText(descriptionHelpIcon);
    }
    // =========================
    // Validation messages
    // =========================
    public String getValidationMessage() {
        WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(validationMessageLocator));
        return msg.getText().trim();
    }

    public boolean isValidationMessageDisplayed(String expectedMessage) {
        try {
            By locator = By.xpath("//*[contains(normalize-space(),\"" + expectedMessage + "\")]");
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
    // =========================
    // Likely Activities - visible section
    // =========================
    public List<WebElement> getVisibleActivityCheckboxElements() {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(visibleActivityCheckboxes));
    }

    public int getVisibleActivityCount() {
        return getVisibleActivityCheckboxElements().size();
    }

    public void toggleVisibleActivityCheckboxById(String checkboxId) {
        By locator = By.id(checkboxId);
        WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        scrollIntoView(checkbox);
        jsClick(checkbox);
    }

    public boolean isCheckboxSelectedById(String checkboxId) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id(checkboxId))).isSelected();
    }

    public Map<String, String> getVisibleActivitiesHelpTexts() {
    	 wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(visibleActivityLabels));
    	    List<WebElement> activityBlocks = driver.findElements(visibleActivityLabels);

    	    if (activityBlocks.isEmpty()) {
    	        throw new RuntimeException("Visible activity help text elements were not found.");
    	    }

    	    Map<String, String> result = new LinkedHashMap<>();

    	    for (WebElement block : activityBlocks) {
    	        String labelText = block.getText().replace("?", "").trim();
    	        WebElement helpIcon = block.findElement(By.xpath(".//em[contains(@class,'info')]"));
    	        String helpText = getTooltipText(helpIcon);
    	        result.put(labelText, helpText);
    	    }

    	    return result;
    }
 // =========================
    // More Activities
    // =========================
    public void expandViewMoreActivities() {
        WebElement toggle = wait.until(ExpectedConditions.elementToBeClickable(viewMoreActivitiesToggle));
        scrollIntoView(toggle);

        if (!isMoreActivitiesExpanded()) {
            safeClick(toggle);
            wait.until(ExpectedConditions.visibilityOfElementLocated(moreActivitiesContainer));
        }
    }

    public boolean isMoreActivitiesExpanded() {
        try {
            WebElement container = driver.findElement(moreActivitiesContainer);
            return container.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public int getMoreActivitiesCount() {
        expandViewMoreActivities();
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(moreActivityCheckboxes)).size();
    }

    public void toggleMoreActivityCheckboxById(String checkboxId) {
        expandViewMoreActivities();
        WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(checkboxId)));
        scrollIntoView(checkbox);
        jsClick(checkbox);
    }

    public Map<String, String> getMoreActivitiesHelpTexts() {
        expandViewMoreActivities();

        List<WebElement> activityBlocks = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(moreActivityLabels));
        Map<String, String> result = new LinkedHashMap<>();

        for (WebElement block : activityBlocks) {
            String labelText = block.getText().replace("?", "").trim();
            WebElement helpIcon = block.findElement(By.xpath(".//em[contains(@class,'info')]"));
            String helpText = getTooltipText(helpIcon);
            result.put(labelText, helpText);
        }

        return result;
    }
 // =========================
    // Bulk validation helpers
    // =========================
    public List<String> validateAllVisibleActivityCheckboxSelection() {
        List<String> failures = new ArrayList<>();

        for (WebElement checkbox : getVisibleActivityCheckboxElements()) {
            String id = checkbox.getAttribute("id");

            scrollIntoView(checkbox);
            jsClick(checkbox);

            if (!isCheckboxSelectedById(id)) {
                failures.add("Checkbox not selected for visible activity id: " + id);
                continue;
            }

            jsClick(checkbox);

            if (isCheckboxSelectedById(id)) {
                failures.add("Checkbox not deselected for visible activity id: " + id);
            }
        }

        return failures;
    }

    public List<String> validateAllMoreActivityCheckboxSelection() {
        expandViewMoreActivities();

        List<String> failures = new ArrayList<>();
        List<WebElement> checkboxes = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(moreActivityCheckboxes));

        for (WebElement checkbox : checkboxes) {
            String id = checkbox.getAttribute("id");

            scrollIntoView(checkbox);
            jsClick(checkbox);

            if (!isCheckboxSelectedById(id)) {
                failures.add("Checkbox not selected for more activity id: " + id);
                continue;
            }

            jsClick(checkbox);

            if (isCheckboxSelectedById(id)) {
                failures.add("Checkbox not deselected for more activity id: " + id);
            }
        }

        return failures;
    }
 // =========================
    // Utility methods
    // =========================
    private String getTooltipText(By helpIconLocator) {
        WebElement icon = wait.until(ExpectedConditions.visibilityOfElementLocated(helpIconLocator));
        return getTooltipText(icon);
    }

    private String getTooltipText(WebElement icon) {
        scrollIntoView(icon);

        String tooltip = icon.getAttribute("data-bs-original-title");
        if (tooltip != null && !tooltip.trim().isEmpty()) {
            return tooltip.trim();
        }

        tooltip = icon.getAttribute("title");
        if (tooltip != null && !tooltip.trim().isEmpty()) {
            return tooltip.trim();
        }

        actions.moveToElement(icon).perform();

        tooltip = icon.getAttribute("data-bs-original-title");
        if (tooltip != null) {
            return tooltip.trim();
        }

        return "";
    }

    public String generateText(int length) {
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length) {
            sb.append("A");
        }
        return sb.substring(0, length);
    }

    public String rgbaToHex(String rgbaValue) {
        return Color.fromString(rgbaValue).asHex();
    }

    // tab locator getters if needed in tests
    public By getProgramInfoTabLocator() {
        return programInfoTab;
    }

    public By getFacilitatorInfoTabLocator() {
        return facilitatorInfoTab;
    }

    public By getMessagesTabLocator() {
        return messagesTab;
    }

    public By getBatchAddedTabLocator() {
        return batchAddedTab;
    }
    private void selectCheckbox(By locator) {
        WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        scrollIntoView(checkbox);

        if (!checkbox.isSelected()) {
            jsClick(checkbox);
        }
    }
    public void selectAssignmentActivity() {
        selectCheckbox(assignmentCheckbox);
    }

    public void selectReferenceMaterialActivity() {
        selectCheckbox(referenceMaterialCheckbox);
    }
    public boolean isBatchCreatedSuccessfullyMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(batchCreatedSuccessMessage)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
    public boolean isPlannedTabActive() {
    	try {
            WebElement plannedTabLink = wait.until(ExpectedConditions.visibilityOfElementLocated(plannedTab));

            String linkClass = plannedTabLink.getAttribute("class");
            if (linkClass != null && linkClass.contains("active")) {
                return true;
            }

            WebElement parentLi = plannedTabLink.findElement(By.xpath("./parent::li"));
            String parentClass = parentLi.getAttribute("class");
            if (parentClass != null && parentClass.contains("active")) {
                return true;
            }

            String ariaSelected = plannedTabLink.getAttribute("aria-selected");
            if ("true".equalsIgnoreCase(ariaSelected)) {
                return true;
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean waitForPlannedTabToBecomeActive() {
        try {
            return wait.until(driver -> {
                WebElement plannedTabLink = driver.findElement(plannedTab);

                String linkClass = plannedTabLink.getAttribute("class");
                if (linkClass != null && linkClass.contains("active")) {
                    return true;
                }

                WebElement parentLi = plannedTabLink.findElement(By.xpath("./parent::li"));
                String parentClass = parentLi.getAttribute("class");
                if (parentClass != null && parentClass.contains("active")) {
                    return true;
                }

                String ariaSelected = plannedTabLink.getAttribute("aria-selected");
                return "true".equalsIgnoreCase(ariaSelected);
            });
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isBatchPresentInPlannedList(String batchName) {
        try {
            By batchNameLocator = By.xpath("//*[normalize-space()='" + batchName + "']");
            return wait.until(ExpectedConditions.visibilityOfElementLocated(batchNameLocator)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
    public void waitForProgramInfoPageToLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(programInfoHeading));
        wait.until(ExpectedConditions.visibilityOfElementLocated(startDateInput));
        wait.until(ExpectedConditions.visibilityOfElementLocated(endDateInput));
        wait.until(ExpectedConditions.visibilityOfElementLocated(programGenreDropdown));
        wait.until(ExpectedConditions.visibilityOfElementLocated(programTitleDropdown));
        wait.until(ExpectedConditions.visibilityOfElementLocated(targetAudienceDropdown));
        wait.until(ExpectedConditions.visibilityOfElementLocated(clientDropdownField));
    }
    public boolean isProgramInfoHeadingDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(programInfoHeading)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isProgramInfoTabActive() {
        try {
            WebElement tab = wait.until(ExpectedConditions.visibilityOfElementLocated(programInfoTab));
            return isTabActive(tab);
        } catch (Exception e) {
            return false;
        }
    }
    private boolean isTabActive(WebElement tab) {
        String tabClass = tab.getAttribute("class");
        if (tabClass != null && tabClass.contains("active")) {
            return true;
        }

        String ariaSelected = tab.getAttribute("aria-selected");
        if ("true".equalsIgnoreCase(ariaSelected)) {
            return true;
        }

        try {
            WebElement parentLi = tab.findElement(By.xpath("./ancestor::li[1]"));
            String parentClass = parentLi.getAttribute("class");
            return parentClass != null && parentClass.contains("active");
        } catch (Exception e) {
            return false;
        }
    }
    public boolean isBasicSetupTabClickable() {
        WebElement tab = wait.until(ExpectedConditions.visibilityOfElementLocated(activeTab));
        String classes = tab.getAttribute("class");
        return !classes.contains("disabled");
    }
    public String getBasicSetupTabClass() {
        WebElement tab = wait.until(ExpectedConditions.visibilityOfElementLocated(activeTab));
        return tab.getAttribute("class");
    }

    public void clickBasicSetupTab() {
        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(activeTab));
        scrollIntoView(tab);
        tab.click();
    }
    public boolean isFacilitatorInfoTabDisabled() {
        return isTabDisabled(facilitatorInfoTab);
    }

    public boolean isMessagesTabDisabled() {
        return isTabDisabled(messagesTab);
    }

    public boolean isBatchAddedTabDisabled() {
        return isTabDisabled(batchAddedTab);
    }
    public String getBasicSetupTabColorHex() {
        WebElement tab = wait.until(ExpectedConditions.visibilityOfElementLocated(activeTab));
        return getTabBackgroundColorHex(tab);
    }

    public String getProgramInfoTabColorHex() {
        WebElement tab = wait.until(ExpectedConditions.visibilityOfElementLocated(programInfoTab));
        return getTabBackgroundColorHex(tab);
    }

    public boolean isBasicSetupHeadingDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(basicSetupHeading)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    private boolean isTabDisabled(By tabLocator) {
    	try {
            WebElement tab = wait.until(ExpectedConditions.visibilityOfElementLocated(tabLocator));

            // Check tab itself
            String tabClass = tab.getAttribute("class");
            if (tabClass != null && tabClass.contains("disabled")) {
                return true;
            }

            // Check parent li
            WebElement parentLi = tab.findElement(By.xpath("./ancestor::li[1]"));
            String parentClass = parentLi.getAttribute("class");
            return parentClass != null && parentClass.contains("disabled");

        } catch (Exception e) {
            return false;
        }
    }

    private String getTabBackgroundColorHex(WebElement tab) {
        String bgColor = tab.getCssValue("background-color");
        return Color.fromString(bgColor).asHex();
    }
    public boolean isStartDateDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(startDateInput)).isDisplayed();
    }

    public boolean isEndDateDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(endDateInput)).isDisplayed();
    }

    public boolean isStartDateEnabled() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(startDateInput)).isEnabled();
    }

    public boolean isEndDateEnabled() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(endDateInput)).isEnabled();
    }
    public String getStartDateValue() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(startDateInput))
                .getAttribute("value")
                .trim();
    }

    public String getEndDateValue() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(endDateInput))
                .getAttribute("value")
                .trim();
    }
    public String getTodayDateInUiFormat() {
        return LocalDate.now().format(UI_DATE_FORMAT);
    }

    public String getOneMonthAfterTodayInUiFormat() {
        return LocalDate.now().plusMonths(1).format(UI_DATE_FORMAT);
    }
    public String getOneMonthAfterDate(String dateText) {
        return LocalDate.parse(dateText, UI_DATE_FORMAT).plusMonths(1).format(UI_DATE_FORMAT);
    }
    public void clearStartDate() {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(startDateInput));
        scrollIntoView(input);
        input.click();
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(Keys.DELETE);
    }

    public void clearEndDate() {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(endDateInput));
        scrollIntoView(input);
        input.click();
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(Keys.DELETE);
    }
    public void enterStartDate(String date) {
    	waitForToastToDisappear();
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(startDateInput));
        scrollIntoView(input);
        input.click();
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(Keys.DELETE);
        input.sendKeys(date);
        input.sendKeys(Keys.TAB);
    }

    public void enterEndDate(String date) {
    	waitForToastToDisappear();
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(endDateInput));
        scrollIntoView(input);
        input.click();
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(Keys.DELETE);
        input.sendKeys(date);
        input.sendKeys(Keys.TAB);
    }
    public void clickSaveAndExitOnProgramInfo() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(programInfoSaveAndExitButton));
        scrollIntoView(button);
        safeClick(button);
    }

    public void clickSaveAndContinueOnProgramInfo() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(programInfoSaveAndContinueButton));
        scrollIntoView(button);
        safeClick(button);
    }
    public boolean isStartDateRequiredValidationDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(startDateRequiredMessage)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
    public void waitForToastToDisappear() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(errorToast));
        } catch (Exception e) {
            // ignore if toast is already gone or not present
        }
    }

    public boolean isEndDateRequiredValidationDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(endDateRequiredMessage)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
    public String getStartDateHelpText() {
        return getTooltipText(startDateHelpIcon);
    }

    public String getEndDateHelpText() {
        return getTooltipText(endDateHelpIcon);
    }

    public boolean isEndDateAfterOrEqualStartDate() {
        String start = getStartDateValue();
        String end = getEndDateValue();

        if (start.isBlank() || end.isBlank()) {
            return false;
        }

        LocalDate startDate = LocalDate.parse(start, UI_DATE_FORMAT);
        LocalDate endDate = LocalDate.parse(end, UI_DATE_FORMAT);
        return !endDate.isBefore(startDate);
    }
    /**
     * Use this only if your datepicker renders disabled past dates with class names like:
     * old, disabled, prev, etc.
     * You may need to adjust based on actual datepicker HTML.
     */
    public boolean arePastDatesDisabledInOpenStartDatePicker() {
        WebElement icon = wait.until(ExpectedConditions.elementToBeClickable(startDateCalendarIcon));
        safeClick(icon);

        List<WebElement> days = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(genericDatePickerDays));
        for (WebElement day : days) {
            String classes = day.getAttribute("class");
            String text = day.getText().trim();

            if (!text.isEmpty() && classes != null && classes.contains("old") && !classes.contains("disabled")) {
                return false;
            }
        }
        return true;
    }

    public boolean arePastDatesDisabledInOpenEndDatePicker() {
    	 WebElement icon = wait.until(ExpectedConditions.elementToBeClickable(endDateCalendarIcon));
    	    safeClick(icon);

    	    WebElement picker = wait.until(ExpectedConditions.visibilityOfElementLocated(
    	            By.xpath("//div[contains(@class,'datepicker-dropdown') and contains(@style,'display: block')]")));

    	    String monthYearText = picker.findElement(By.xpath(".//th[contains(@class,'datepicker-switch')]")).getText().trim();
    	    DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");

    	    java.time.YearMonth visibleMonth = java.time.YearMonth.parse(monthYearText, monthYearFormatter);
    	    java.time.LocalDate today = java.time.LocalDate.now();

    	    List<WebElement> dayCells = picker.findElements(By.xpath(".//td[contains(@class,'day')]"));

    	    for (WebElement dayCell : dayCells) {
    	        String text = dayCell.getText().trim();
    	        if (text.isEmpty()) {
    	            continue;
    	        }

    	        int day = Integer.parseInt(text);
    	        String classes = dayCell.getAttribute("class");

    	        java.time.LocalDate cellDate;

    	        if (classes.contains("old")) {
    	            cellDate = visibleMonth.minusMonths(1).atDay(day);
    	        } else if (classes.contains("new")) {
    	            cellDate = visibleMonth.plusMonths(1).atDay(day);
    	        } else {
    	            cellDate = visibleMonth.atDay(day);
    	        }

    	        boolean isBeforeToday = cellDate.isBefore(today);
    	        boolean isDisabled = classes.contains("disabled");

    	        if (isBeforeToday && !isDisabled) {
    	            System.out.println("Past date is selectable: " + cellDate + " | classes: " + classes);
    	            return false;
    	        }
    	    }

    	    return true;
    }
    
    // ---------------- Program Genre / Program Title defaults ----------------
    public boolean isProgramGenreDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(programGenreSelect2)).isDisplayed();
    }

    public boolean isProgramTitleDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(programTitleSelect2)).isDisplayed();
    }

    public boolean isAddGenreButtonDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(addGenreButton)).isDisplayed();
    }

    public boolean isAddTitleButtonDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(addTitleButton)).isDisplayed();
    }

    public String getDefaultProgramGenreText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(programGenreSelect2)).getText().trim();
    }

    public String getDefaultProgramTitleText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(programTitleSelect2)).getText().trim();
    }

    public String getProgramGenreHelpText() {
        return getTooltipText(programGenreHelpIcon);
    }

    public String getProgramTitleHelpText() {
        return getTooltipText(programTitleHelpIcon);
    }
    
 // ---------------- Select2 dropdown helpers ----------------
    public void openProgramGenreDropdown() {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(programGenreDropdownArrow));
        scrollIntoView(dropdown);
        safeClick(dropdown);
        wait.until(ExpectedConditions.visibilityOfElementLocated(select2SearchInput));
    }

    public void openProgramTitleDropdown() {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(programTitleDropdownArrow));
        scrollIntoView(dropdown);
        safeClick(dropdown);
        wait.until(ExpectedConditions.visibilityOfElementLocated(select2SearchInput));
    }

    public List<String> getProgramGenreOptionsFromSelect() {
        Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(programGenreDropdown)));
        List<String> values = new ArrayList<>();
        for (WebElement option : select.getOptions()) {
            values.add(option.getText().trim());
        }
        return values;
    }

    public List<String> getProgramTitleOptionsFromSelect() {
        Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(programTitleDropdown)));
        List<String> values = new ArrayList<>();
        for (WebElement option : select.getOptions()) {
            values.add(option.getText().trim());
        }
        return values;
    }

    public void searchInSelect2(String searchText) {
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(select2SearchInput));
        searchBox.clear();
        searchBox.sendKeys(searchText);
    }

    public boolean isNoResultsFoundDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(select2NoResults)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSelect2ResultPresent(String expectedText) {
        List<WebElement> results = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(select2Results));
        for (WebElement result : results) {
            if (result.getText().trim().equalsIgnoreCase(expectedText.trim())) {
                return true;
            }
        }
        return false;
    }

    public void selectProgramGenre(String visibleText) {
        Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(programGenreDropdown)));
        select.selectByVisibleText(visibleText);
    }

    public void selectProgramTitle(String visibleText) {
        Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(programTitleDropdown)));
        select.selectByVisibleText(visibleText);
    }
    public boolean isProgramGenrePresentInDropdown(String genreName) {
        return getProgramGenreOptionsFromSelect().stream()
                .anyMatch(option -> option.equals(genreName));
    }

    public String getSelectedProgramGenreText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(programGenreSelectedText))
                .getText()
                .trim();
    }

    public String getSelectedProgramTitleText() {
        Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(programTitleDropdown)));
        return select.getFirstSelectedOption().getText().trim();
    }
 // ---------------- Add Program Genre popup ----------------
    public void clickAddGenreButton() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(addGenreButton));
        scrollIntoView(button);
        safeClick(button);
        wait.until(ExpectedConditions.visibilityOfElementLocated(addProgramGenreModal));
    }

    public boolean isAddProgramGenrePopupDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(addProgramGenreModal)).isDisplayed();
    }

    public String getAddProgramGenrePopupTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(addProgramGenreModalTitle)).getText().trim();
    }

    public void closeAddProgramGenrePopup() {
        safeClick(wait.until(ExpectedConditions.elementToBeClickable(addProgramGenreCloseButton)));
    }

    public void cancelAddProgramGenrePopup() {
        safeClick(wait.until(ExpectedConditions.elementToBeClickable(addProgramGenreCancelButton)));
    }

    public void clickAddProgramGenrePopupAddButton() {
        safeClick(wait.until(ExpectedConditions.elementToBeClickable(addProgramGenreAddButton)));
    }

    public void waitForProgramInfoTabOrServerError() {
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(programInfoHeading),
                    ExpectedConditions.visibilityOfElementLocated(serverErrorToast)
            ));
        } catch (TimeoutException e) {
            Assert.fail("After clicking Add, neither Program Info tab nor server error message was displayed.");
        }
    }
    public boolean isServerErrorDisplayed() {
        return !driver.findElements(serverErrorToast).isEmpty()
                && driver.findElement(serverErrorToast).isDisplayed();
    }
    public String getServerErrorMessageText() {
        return driver.findElement(serverErrorToast).getText().trim();
    }
    public void enterProgramGenreName(String value) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(programGenreInput));
        input.clear();
        input.sendKeys(value);
    }

    public void enterShortTitle(String value) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(shortTitleInput));
        input.clear();
        input.sendKeys(value);
    }

    public void enterLikelyProgramTitleAtIndex(int index, String value) {
        List<WebElement> inputs = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(likelyProgramTitleInputs));
        WebElement input = inputs.get(index);
        input.clear();
        input.sendKeys(value);
    }

    public int getLikelyProgramTitleFieldCount() {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(likelyProgramTitleInputs)).size();
    }

    public void clickAddLikelyProgramTitleButtonAtIndex(int index) {
        List<WebElement> buttons = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(addLikelyTitleButtons));
        safeClick(buttons.get(index));
    }

    public void clickRemoveLikelyProgramTitleButtonAtIndex(int index) {
        List<WebElement> buttons = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(removeLikelyTitleButtons));
        safeClick(buttons.get(index));
    }

    public void enterProgramGenreDescription(String value) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(programGenreDescriptionInput));
        input.clear();
        input.sendKeys(value);
    }

    public boolean isMandatoryValidationDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(mandatoryValidationMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isProgramGenreLengthValidationDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(programGenreLengthValidation)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isShortTitleLengthValidationDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(shortTitleLengthValidation)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLikelyProgramTitleLengthValidationDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(likelyProgramTitleLengthValidation)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDescriptionLengthValidationDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(descriptionLengthValidation)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void addProgramGenre(String programGenre, String shortTitle, List<String> likelyTitles, String description) {
     
        enterProgramGenreName(programGenre);

        if (shortTitle != null) {
            enterShortTitle(shortTitle);
        }

        for (int i = 0; i < likelyTitles.size(); i++) {
            if (i == 0) {
                enterLikelyProgramTitleAtIndex(0, likelyTitles.get(0));
            } else {
                clickAddLikelyProgramTitleButtonAtIndex(i - 1);
                enterLikelyProgramTitleAtIndex(i, likelyTitles.get(i));
            }
        }

        if (description != null) {
            enterProgramGenreDescription(description);
        }

    }
 // ---------------- Add Program Title popup ----------------
    public void clickAddTitleButton() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(addTitleButton));
        scrollIntoView(button);
        safeClick(button);
        wait.until(ExpectedConditions.visibilityOfElementLocated(addProgramTitleModal));
    }

    public boolean isAddProgramTitlePopupDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(addProgramTitleModal)).isDisplayed();
    }

    public String getAddProgramTitlePopupTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(addProgramTitleModalTitle)).getText().trim();
    }

    public void closeAddProgramTitlePopup() {
        safeClick(wait.until(ExpectedConditions.elementToBeClickable(addProgramTitleCloseButton)));
    }

    public void cancelAddProgramTitlePopup() {
        safeClick(wait.until(ExpectedConditions.elementToBeClickable(addProgramTitleCancelButton)));
    }

    public void enterNewProgramTitle(String value) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(newProgramTitleInput));
        input.clear();
        input.sendKeys(value);
    }

    public void clickAddProgramTitlePopupAddButton() {
        safeClick(wait.until(ExpectedConditions.elementToBeClickable(addProgramTitleAddButton)));
    }

    public boolean isProgramTitleLengthValidationDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(programTitleLengthValidation)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void addProgramTitle(String value) {
        clickAddTitleButton();
        enterNewProgramTitle(value);
        clickAddProgramTitlePopupAddButton();
    }
 // ---------------- Mapping validation ----------------
    public boolean doProgramTitleOptionsMatchExpected(Set<String> expectedTitles) {
        List<String> actualOptions = getProgramTitleOptionsFromSelect();
        Set<String> actualNormalized = new LinkedHashSet<>();

        for (String option : actualOptions) {
            String text = option.trim();
            if (!text.isEmpty() && !text.equalsIgnoreCase("--Select Title--")) {
                actualNormalized.add(text);
            }
        }

        return actualNormalized.equals(expectedTitles);
    }
    // Target Audience methods
    public void waitForFieldToBeReady() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(targetAudienceDropdown));
        wait.until(ExpectedConditions.elementToBeClickable(targetAudienceDropdown));
        scrollIntoView(targetAudienceDropdown);
    }

    public void scrollIntoView(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        js.executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", element);
    }

    public boolean isLabelDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(targetAudienceLabel)).isDisplayed();
    }

    public String getLabelText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(targetAudienceLabel)).getText().trim();
    }
    public boolean isDropdownDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(targetAudienceDropdown)).isDisplayed();
    }

    public boolean isDropdownEnabled() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(targetAudienceDropdown)).isEnabled();
    }

    public String getDefaultSelectedOption() {
        Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(targetAudienceDropdown)));
        return select.getFirstSelectedOption().getText().trim();
    }
    public void selectTargetAudience(String visibleText) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(targetAudienceDropdown));
        scrollIntoView(targetAudienceDropdown);

        Select select = new Select(dropdown);
        select.selectByVisibleText(visibleText);

        wait.until(driver ->
        select.getFirstSelectedOption().getText().trim().equals(visibleText)
        );
    }
    public String getSelectedTargetAudience() {
        Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(targetAudienceDropdown)));
        return select.getFirstSelectedOption().getText().trim();
    }
    public List<String> getAllDropdownOptions() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(targetAudienceDropdown));

        List<WebElement> optionElements = driver.findElements(targetAudienceOptions);
        List<String> optionTexts = new ArrayList<>();

        for (WebElement option : optionElements) {
            try {
                optionTexts.add(option.getText().trim());
            } catch (StaleElementReferenceException e) {
                optionElements = driver.findElements(targetAudienceOptions);
                optionTexts.clear();
                for (WebElement refreshed : optionElements) {
                    optionTexts.add(refreshed.getText().trim());
                }
                break;
            }
        }
        return optionTexts;
    }
    public List<String> getAllOptionValues() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(targetAudienceDropdown));
        List<WebElement> optionElements = driver.findElements(targetAudienceOptions);
        List<String> values = new ArrayList<>();

        for (WebElement option : optionElements) {
            values.add(option.getAttribute("value"));
        }
        return values;
    }
    public int getOptionsCount() {
        return getAllDropdownOptions().size();
    }
    public boolean hasDuplicateOptions() {
        List<String> options = getAllDropdownOptions();
        Set<String> unique = new LinkedHashSet<>(options);
        return options.size() != unique.size();
    }

    public String getNativeValidationMessage() {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(targetAudienceDropdown));
        return (String) js.executeScript("return arguments[0].validationMessage;", dropdown);
    }
    // =========================
    // Client Dropdown methods
    // =========================
    public void openClientDropdown() {
    	 WebElement rendered = wait.until(ExpectedConditions.visibilityOfElementLocated(clientDropdownRenderedText));
    	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", rendered);

    	    try {
    	        wait.until(ExpectedConditions.elementToBeClickable(rendered)).click();
    	    } catch (Exception e) {
    	        WebElement arrow = wait.until(ExpectedConditions.visibilityOfElementLocated(clientDropdownArrow));
    	        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", arrow);
    	    }

    	    wait.until(ExpectedConditions.visibilityOfElementLocated(clientSearchBox));
    	    wait.until(ExpectedConditions.visibilityOfElementLocated(clientResultsContainer));
    }

    public boolean isClientDropdownOpened() {
        return isDisplayed(clientResultsContainer);
    }

    public boolean isClientSearchBoxDisplayed() {
        return isDisplayed(clientSearchBox);
    }

    public void searchClient(String clientName) {
    	 WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(clientSearchBox));
    	    searchBox.click();
    	    searchBox.sendKeys(Keys.CONTROL, "a");
    	    searchBox.sendKeys(Keys.DELETE);
    	    searchBox.sendKeys(clientName);
    }

    public void selectClient(String clientName) {

    	openClientDropdown();
        searchClient(clientName);

        By clientOption = By.xpath("//ul[@id='select2-client-results']//li[@role='treeitem' and normalize-space()=\"" + clientName + "\"]");
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(clientOption));
        option.click();
    }

    public String getSelectedClientText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(clientDropdownArrow)).getText().trim();
    }

    public String getDefaultClientText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(clientDropdownArrow)).getText().trim();
    }

    public int getClientOptionsCount() {
        openClientDropdown();
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(clientOptions)).size();
    }
    // =========================
    // + Client popup methods
    // =========================
    public void clickAddClientButton() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(addClientButton));
        scrollIntoView(element);
        element.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(addClientModal));
    }

    public boolean isAddClientPopupDisplayed() {
        return isDisplayed(addClientModal);
    }

    public String getAddClientPopupTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(addClientModalTitle)).getText().trim();
    }

    public boolean isClientNameFieldDisplayed() {
        return isDisplayed(clientNameField);
    }

    public boolean isIndustryFieldDisplayed() {
        return isDisplayed(industryField);
    }

    public boolean isLogoFieldDisplayed() {
        return isDisplayed(logoInput);
    }

    public boolean isAddButtonDisplayed() {
        return isDisplayed(clientAddButtonInPopup);
    }

    public boolean isClientCancelButtonDisplayed() {
        return isDisplayed(clientCancelButtonInPopup);
    }

    public void enterClientName(String clientName) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(clientNameField));
        element.clear();
        element.sendKeys(clientName);
    }

    public void enterIndustry(String industry) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(industryField));
        element.clear();
        element.sendKeys(industry);
    }

    public void uploadLogo(String filePath) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(logoInput));
        js.executeScript("arguments[0].style.display='block'; arguments[0].style.visibility='visible';", element);
        element.sendKeys(Path.of(filePath).toAbsolutePath().toString());
    }

    public void clickAddButtonInPopup() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(clientAddButtonInPopup));
        scrollIntoView(element);
        element.click();
    }

    public void clickCancelButtonInPopup() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(clientCancelButtonInPopup));
        element.click();
    }

    public void addClient(String clientName, String industry, String logoPath) {
        enterClientName(clientName);
        enterIndustry(industry);
        if (logoPath != null && !logoPath.trim().isEmpty()) {
            uploadLogo(logoPath);
        }
        clickAddButtonInPopup();
    }

    // =========================
    // Validations
    // =========================
    public String getMandatoryErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(mandatoryErrorMessage)).getText().trim();
    }

    public String getDuplicateClientErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(duplicateClientErrorMessage)).getText().trim();
    }

    public String getClientNameLengthErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(clientNameLengthErrorMessage)).getText().trim();
    }

    public String getIndustryLengthErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(industryLengthErrorMessage)).getText().trim();
    }

    public String getInvalidLogoErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(invalidLogoErrorMessage)).getText().trim();
    }

    public String getServerErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(serverErrorMessage)).getText().trim();
    }

    public boolean isProgramInfoTabDisplayed() {
        return isDisplayed(programInfoHeading);
    }

    public void waitForProgramInfoOrServerError() {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(programInfoHeading),
                ExpectedConditions.visibilityOfElementLocated(serverErrorMessage)
        ));
    }

    // =========================
    // Utility
    // =========================
    private boolean isDisplayed(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }


    public String getStringWithLength(int length) {
        return "A".repeat(Math.max(0, length));
    }
    public boolean isLogoPreviewDisplayed() {
        try {
            WebElement image = wait.until(ExpectedConditions.visibilityOfElementLocated(logoPreviewImage));
            String src = image.getAttribute("src");
            return image.isDisplayed() && src != null && !src.trim().isEmpty();
        } catch (TimeoutException e) {
            return false;
        }
    }
    public boolean isRemoveLogoIconDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(removeLogoIcon)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
    public void clickRemoveLogoIcon() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(removeLogoIcon));
        element.click();
    }
    public boolean isDeleteLogoConfirmationPopupDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(deleteLogoConfirmPopup)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
    public void clickDeleteLogoCancelButton() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(deleteLogoCancelButton));
        element.click();
    }
    public void clickDeleteLogoOkButton() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(deleteLogoOkButton));
        element.click();
    }
    public boolean isDeleteLogoConfirmationPopupClosed() {
        try {
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(deleteLogoConfirmPopup));
        } catch (TimeoutException e) {
            return false;
        }
    }
    public String getLogoDeletedSuccessMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(logoDeletedSuccessMessage))
                   .getText()
                   .trim();
    }
    public boolean isLogoDeleted() {
        try {
            wait.until(ExpectedConditions.attributeToBe(logoPreviewImage, "src", ""));
            return true;
        } catch (TimeoutException e) {
            String src = driver.findElement(logoPreviewImage).getAttribute("src");
            return src == null || src.trim().isEmpty();
        }
    }
    public void clickOutsideToCloseDropdown() {
        Actions actions = new Actions(driver);
        actions.moveByOffset(10, 10).click().perform();
    }
    public void clickProgramInfoSaveAndExit() {
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(programInfoSaveAndExitBtn));

        // Scroll into view
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});", button
        );

        // Wait until clickable
        wait.until(ExpectedConditions.elementToBeClickable(button));

        try {
            button.click();
        } catch (Exception e) {
            // fallback (very important for your project issues)
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        }
    }
    public void clickProgramInfoSaveAndContinue() {
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(programInfoSaveAndContinue));

        // Scroll into view
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});", button
        );

        // Wait until clickable
        wait.until(ExpectedConditions.elementToBeClickable(button));

        try {
            button.click();
        } catch (Exception e) {
            // fallback (very important for your project issues)
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        }
    }
    
    //Facilitator Info
    //============
    
    private WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private List<WebElement> visibles(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }
    private boolean isDisabled(By locator) {
        WebElement element = visible(locator);
        return !element.isEnabled() || element.getAttribute("disabled") != null;
    }
    private void safeClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        scrollIntoView(element);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
            js.executeScript("arguments[0].click();", element);
        }
    }
    private String text(By locator) {
        return visible(locator).getText().trim();
    }
    private WebElement present(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    private void waitForLoaderToDisappearIfPresent() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(pageLoader));
        } catch (Exception e) {
            // ignore if loader not present
        }
    }
    public void waitForFacilitatorInfoPageToLoad() {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(facilitatorInfoHeading),
                ExpectedConditions.visibilityOfElementLocated(serverErrorToast)
        ));

        if (isDisplayed(serverErrorToast)) {
            throw new AssertionError("Server error displayed while loading Facilitator Info: " + getServerErrorMessage());
        }

        visible(facilitatorInfoHeading);
        visible(facilitatorInfoPane);
    }
    public boolean isFacilitatorInfoHeadingDisplayed() {
        return isDisplayed(facilitatorInfoHeading);
    }
    public boolean isFacilitatorInfoTabActive() {
        return visible(facilitatorInfoTab).getAttribute("class").contains("active");
    }
    public boolean isBasicSetupTabCompletedOrClickable() {
        String clazz = visible(activeTab).getAttribute("class");
        return clazz.contains("prgs_cmplt") || !clazz.contains("disabled");
    }

    public boolean isProgramInfoTabCompletedOrClickable() {
        String clazz = visible(programInfoTab).getAttribute("class");
        return clazz.contains("prgs_cmplt") || !clazz.contains("disabled");
    }
    public String getFacilitatorInfoTabColorHex() {
        return rgbaToHex(visible(facilitatorInfoTab).getCssValue("background-color"));
    }
    public int getFacilitatorRowCount() {
        return visibles(facilitatorRows).size();
    }
    public String getFirstSelectedFacilitatorName() {
        return new Select(visible(firstFacilitatorNameDropdown)).getFirstSelectedOption().getText().trim();
    }

    public String getFirstSelectedFacilitatorType() {
        return new Select(visible(firstFacilitatorTypeDropdown)).getFirstSelectedOption().getText().trim();
    }

    public boolean isFirstFacilitatorNameDropdownDisabled() {
        return isDisabled(firstFacilitatorNameDropdown);
    }

    public boolean isFirstFacilitatorTypeDropdownDisabled() {
        return isDisabled(firstFacilitatorTypeDropdown);
    }
    public boolean isFirstBatchPrivilegesIconDisplayed() {
        return isDisplayed(firstBatchPrivilegesIcon);
    }

    public boolean isFirstAddFacilitatorIconDisplayed() {
        return isDisplayed(firstAddFacilitatorIcon);
    }

    public boolean isFirstDeleteFacilitatorIconDisplayed() {
        return isDisplayed(firstDeleteFacilitatorIcon);
    }
    public String getTypeHelpText() {
        safeClick(typeHelpIcon);
        return visible(typeHelpTooltip).getText().trim();
    }

    public String getSendEmailFromHelpText() {
        safeClick(sendEmailFromHelpIcon);
        return visible(sendEmailFromHelpTooltip).getText().trim();
    }
    public String getSelectedSendEmailFrom() {
        return new Select(visible(sendEmailFromDropdown)).getFirstSelectedOption().getText().trim();
    }

    public void selectSendEmailFrom(String email) {
        Select select = new Select(visible(sendEmailFromDropdown));
        select.selectByVisibleText(email);
    }

    public List<String> getSendEmailFromOptions() {
        Select select = new Select(visible(sendEmailFromDropdown));
        List<String> values = new ArrayList<>();
        for (WebElement option : select.getOptions()) {
            values.add(option.getText().trim());
        }
        return values;
    }

    // ---------------- Page buttons ----------------
    public boolean isFacilitatorInfoCancelButtonDisplayed() {
        return isDisplayed(facilitatorInfoCancelButton);
    }

    public boolean isFacilitatorInfoSaveAndExitButtonDisplayed() {
        return isDisplayed(facilitatorInfoSaveAndExitButton);
    }

    public boolean isFacilitatorInfoSaveAndContinueButtonDisplayed() {
        return isDisplayed(facilitatorInfoSaveAndContinueButton);
    }

    public void clickFacilitatorInfoSaveAndContinue() {
        safeClick(facilitatorInfoSaveAndContinueButton);
    }
    public void openFirstFacilitatorPrivilegesPopup() {
        safeClick(firstBatchPrivilegesIcon);
        wait.until(ExpectedConditions.visibilityOfElementLocated(privilegesModal));
    }
    public void waitForPrivilegesPopupToLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(privilegesModal));
        wait.until(ExpectedConditions.visibilityOfElementLocated(privilegesModalTitle));
    }
    public void openPrivilegesPopupForFacilitatorRow(int rowIndex1Based) {
        By rowPrivilegesIcon = By.xpath("(//div[@id='ex3-tabs-3']//a[contains(@class,'fac-batch-privileges')])[" + rowIndex1Based + "]");
        safeClick(rowPrivilegesIcon);
        wait.until(ExpectedConditions.visibilityOfElementLocated(privilegesModal));
    }

    public boolean isPrivilegesModalDisplayed() {
        return isDisplayed(privilegesModal);
    }

    public String getPrivilegesModalTitle() {
        return text(privilegesModalTitle);
    }

    public boolean isPrivilegesModalTitleContains(String expected) {
        return getPrivilegesModalTitle().contains(expected);
    }

    public boolean arePrivilegesPopupButtonsDisplayed() {
        return isDisplayed(privilegesCloseButton) && isDisplayed(privilegesUpdateButton);
    }

    public void closePrivilegesPopupFromCloseButton() {
        safeClick(privilegesCloseButton);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(privilegesModal));
    }

    public void closePrivilegesPopupFromX() {
        safeClick(privilegesCloseIcon);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(privilegesModal));
    }

    public void clickPrivilegesUpdate() {
        safeClick(privilegesUpdateButton);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(privilegesModal));
    }

    public boolean isCheckboxSelected(By locator) {
        return present(locator).isSelected();
    }

    public void setCheckbox(By locator, boolean expectedState) {
        WebElement checkbox = present(locator);
        scrollIntoView(checkbox);

        waitForPrivilegesPopupToLoad();

        boolean currentState = checkbox.isSelected();
        if (currentState != expectedState) {
            try {
                js.executeScript("arguments[0].click();", checkbox);
            } catch (Exception e) {
                checkbox = present(locator);
                js.executeScript("arguments[0].click();", checkbox);
            }

            wait.until(d -> {
                try {
                    return present(locator).isSelected() == expectedState;
                } catch (Exception ex) {
                    return false;
                }
            });
        }
    }

    public void toggleCheckbox(By locator) {
        WebElement checkbox = present(locator);
        scrollIntoView(checkbox);

        waitForPrivilegesPopupToLoad();

        boolean oldState = checkbox.isSelected();

        try {
            js.executeScript("arguments[0].click();", checkbox);
        } catch (Exception e) {
            checkbox = present(locator);
            js.executeScript("arguments[0].click();", checkbox);
        }

        wait.until(d -> {
            try {
                return present(locator).isSelected() != oldState;
            } catch (Exception ex) {
                return false;
            }
        });
    }

    public List<By> getEditablePrivilegeCheckboxes() {
        return Arrays.asList(
                addAllCheckbox, editAllCheckbox, deleteAllCheckbox, viewAllCheckbox, closeAllCheckbox, inviteAllCheckbox,
                editBatchCheckbox, deleteBatchCheckbox, viewBatchCheckbox, closeBatchCheckbox,
                addUserCheckbox, deleteUserCheckbox,
                addPaxCheckbox, editPaxCheckbox, deletePaxCheckbox, viewPaxCheckbox, invitePaxCheckbox,
                addActivityCheckbox, editActivityCheckbox, deleteActivityCheckbox, viewActivityCheckbox,
                publishActivityCheckbox, reminderCheckbox, viewResponseCheckbox, resetResponseCheckbox,
                affinityGroupingCheckbox, inboxCheckbox
        );
    }

    public boolean validateAllEditablePrivilegeCheckboxesToggle() {
    	waitForPrivilegesPopupToLoad();

        for (By locator : getAllEditablePrivilegeCheckboxes()) {
            boolean oldState = isCheckboxSelected(locator);
            toggleCheckbox(locator);

            boolean newState = isCheckboxSelected(locator);
            if (oldState == newState) {
                return false;
            }

            setCheckbox(locator, oldState);
        }
        return true;
    }

    public void applySamplePrivilegeChanges() {
        setCheckbox(editBatchCheckbox, true);
        setCheckbox(deleteBatchCheckbox, true);
        setCheckbox(addUserCheckbox, true);
        setCheckbox(addPaxCheckbox, true);
        setCheckbox(deletePaxCheckbox, true);
        setCheckbox(addActivityCheckbox, true);
        setCheckbox(publishActivityCheckbox, true);
        setCheckbox(resetResponseCheckbox, true);
    }

    public List<Boolean> captureSamplePrivilegeStates() {
        return Arrays.asList(
                isCheckboxSelected(editBatchCheckbox),
                isCheckboxSelected(deleteBatchCheckbox),
                isCheckboxSelected(addUserCheckbox),
                isCheckboxSelected(addPaxCheckbox),
                isCheckboxSelected(deletePaxCheckbox),
                isCheckboxSelected(addActivityCheckbox),
                isCheckboxSelected(publishActivityCheckbox),
                isCheckboxSelected(resetResponseCheckbox)
        );
    }
 // =========================
    // Editable / non-editable sets
    // =========================
    public List<By> getAllEditablePrivilegeCheckboxes() {
        return Arrays.asList(
                addAllCheckbox, editAllCheckbox, deleteAllCheckbox, viewAllCheckbox, closeAllCheckbox, inviteAllCheckbox,
                editBatchCheckbox, deleteBatchCheckbox, viewBatchCheckbox, closeBatchCheckbox,
                addUserCheckbox, deleteUserCheckbox,
                addPaxCheckbox, editPaxCheckbox, deletePaxCheckbox, viewPaxCheckbox, invitePaxCheckbox,
                addActivityCheckbox, editActivityCheckbox, deleteActivityCheckbox, viewActivityCheckbox,
                publishActivityCheckbox, reminderCheckbox, viewResponseCheckbox, resetResponseCheckbox,
                affinityGroupingCheckbox, inboxCheckbox
        );
    }

    public List<By> getNonEditableCrossIcons() {
        return Arrays.asList(
                batchAddCross, batchInviteCross,
                facilitatorEditCross, facilitatorViewCross, facilitatorCloseCross, facilitatorInviteCross,
                paxCloseCross,
                activityCloseCross, activityInviteCross
        );
    }
    public boolean areAllNonEditableCrossIconsDisplayed() {
        for (By locator : getNonEditableCrossIcons()) {
            if (!isDisplayed(locator)) {
                return false;
            }
        }
        return true;
    }
    // =========================
    // Column specific validation
    // =========================
    public List<By> getEditableAddColumnCheckboxes() {
        return Arrays.asList(
                addUserCheckbox,
                addPaxCheckbox,
                addActivityCheckbox
        );
    }

    public List<By> getEditableEditColumnCheckboxes() {
        return Arrays.asList(
                editBatchCheckbox,
                editPaxCheckbox,
                editActivityCheckbox
        );
    }
    public void assertColumnState(List<By> checkboxes, boolean expectedState) {
        for (By locator : checkboxes) {
            wait.until(d -> isCheckboxSelected(locator) == expectedState);
        }
    }

    public void setAllCheckboxes(List<By> checkboxes, boolean state) {
        for (By locator : checkboxes) {
            setCheckbox(locator, state);
        }
    }

    public void verifyBulkColumnCheckboxBehavior(By bulkCheckbox, List<By> childCheckboxes) {
        // select all
        setCheckbox(bulkCheckbox, true);
        assertColumnState(childCheckboxes, true);

        // unselect all
        setCheckbox(bulkCheckbox, false);
        assertColumnState(childCheckboxes, false);
    }
    public List<By> getEditableDeleteColumnCheckboxes() {
        return Arrays.asList(
                deleteBatchCheckbox,
                deleteUserCheckbox,
                deletePaxCheckbox,
                deleteActivityCheckbox
        );
    }

    public List<By> getEditableViewColumnCheckboxes() {
        return Arrays.asList(
                viewBatchCheckbox,
                viewPaxCheckbox,
                viewActivityCheckbox
        );
    }

    public List<By> getEditableCloseColumnCheckboxes() {
        return Arrays.asList(
                closeBatchCheckbox
        );
    }

    public List<By> getEditableInviteColumnCheckboxes() {
        return Arrays.asList(
                invitePaxCheckbox
        );
    }
 // =========================
    // Persistence helpers
    // =========================
    public List<Boolean> capturePrivilegeStateSnapshot() {
        List<Boolean> states = new ArrayList<>();

        List<By> snapshotLocators = Arrays.asList(
                addAllCheckbox, editAllCheckbox, deleteAllCheckbox, viewAllCheckbox, closeAllCheckbox, inviteAllCheckbox,
                editBatchCheckbox, deleteBatchCheckbox, viewBatchCheckbox, closeBatchCheckbox,
                addUserCheckbox, deleteUserCheckbox,
                addPaxCheckbox, editPaxCheckbox, deletePaxCheckbox, viewPaxCheckbox, invitePaxCheckbox,
                addActivityCheckbox, editActivityCheckbox, deleteActivityCheckbox, viewActivityCheckbox,
                publishActivityCheckbox, reminderCheckbox, viewResponseCheckbox, resetResponseCheckbox,
                affinityGroupingCheckbox, inboxCheckbox
        );

        for (By locator : snapshotLocators) {
            states.add(isCheckboxSelected(locator));
        }
        return states;
    }
    public int getCrossIconCountForPrivilegeRow(String rowName) {
        By locator = By.xpath(
                "//table[contains(@class,'main_prevlg')]//tr[th[normalize-space()='" + rowName + "']]//img[contains(@src,'check_cross.png')]"
        );
        return driver.findElements(locator).size();
    }

    public boolean validateNonEditableCrossIconCounts() {
        return getCrossIconCountForPrivilegeRow("Batch") == 2
                && getCrossIconCountForPrivilegeRow("Facilitator") == 4
                && getCrossIconCountForPrivilegeRow("Pax") == 1
                && getCrossIconCountForPrivilegeRow("Activity") == 2;
    }
    public boolean validateAllEditableCheckboxesToggle() {
        waitForPrivilegesPopupToLoad();

        List<Object[]> checkboxes = Arrays.asList(
                new Object[]{"add_All", addAllCheckbox},
                new Object[]{"edit_All", editAllCheckbox},
                new Object[]{"delete_All", deleteAllCheckbox},
                new Object[]{"view_All", viewAllCheckbox},
                new Object[]{"close_All", closeAllCheckbox},
                new Object[]{"invite_all", inviteAllCheckbox},

                new Object[]{"edit_batch", editBatchCheckbox},
                new Object[]{"delete_batch", deleteBatchCheckbox},
                new Object[]{"view_batch", viewBatchCheckbox},
                new Object[]{"close_batch", closeBatchCheckbox},

                new Object[]{"add_user", addUserCheckbox},
                new Object[]{"delete_user", deleteUserCheckbox},

                new Object[]{"add_pax", addPaxCheckbox},
                new Object[]{"edit_pax", editPaxCheckbox},
                new Object[]{"delete_pax", deletePaxCheckbox},
                new Object[]{"view_pax", viewPaxCheckbox},
                new Object[]{"invite_pax", invitePaxCheckbox},

                new Object[]{"add_activity", addActivityCheckbox},
                new Object[]{"edit_activity", editActivityCheckbox},
                new Object[]{"delete_activity", deleteActivityCheckbox},
                new Object[]{"view_activity", viewActivityCheckbox},

                new Object[]{"publish_activity", publishActivityCheckbox},
                new Object[]{"reminder", reminderCheckbox},
                new Object[]{"view_response", viewResponseCheckbox},
                new Object[]{"reset_response", resetResponseCheckbox},
                new Object[]{"comment_categorization", affinityGroupingCheckbox},
                new Object[]{"add_inbox", inboxCheckbox}
        );

        for (Object[] item : checkboxes) {
            String name = (String) item[0];
            By locator = (By) item[1];

            try {
                boolean oldState = isCheckboxSelected(locator);
                System.out.println("Before toggle -> " + name + " = " + oldState);

                toggleCheckbox(locator);

                boolean newState = isCheckboxSelected(locator);
                System.out.println("After toggle -> " + name + " = " + newState);

                if (oldState == newState) {
                    System.out.println("Checkbox did not change state: " + name);
                    return false;
                }

                setCheckbox(locator, oldState);

            } catch (Exception e) {
                System.out.println("Failed checkbox: " + name + " | Error: " + e.getMessage());
                throw e;
            }
        }
        return true;
    }
    public int getFacilitatorinfoRowCount() {
        int nameCount = driver.findElements(facilitatorNameDropdowns).size();
        int typeCount = driver.findElements(facilitatorTypeDropdowns).size();

        if (nameCount != typeCount) {
            throw new AssertionError("Facilitator Name dropdown count and Type dropdown count are not matching. Name count = "
                    + nameCount + ", Type count = " + typeCount);
        }

        return nameCount;
    }

    public void waitForFacilitatorRowCountToBe(int expectedCount) {
        wait.until(driver -> getFacilitatorRowCount() == expectedCount);
    }

    public boolean isFacilitatorNameDropdownDisplayed(int rowIndex1Based) {
        By locator = By.xpath("(//div[@id='ex3-tabs-3']//select[@name='facilitators[]'])[" + rowIndex1Based + "]");
        return present(locator).isDisplayed();
    }

    public boolean isFacilitatorTypeDropdownDisplayed(int rowIndex1Based) {
        By locator = By.xpath("(//div[@id='ex3-tabs-3']//select[@name='fac_type[]'])[" + rowIndex1Based + "]");
        return present(locator).isDisplayed();
    }
    public void clickAddFacilitatorIconByRow(int rowIndex1Based) {
        By locator = By.xpath("(//div[@id='ex3-tabs-3']//a[contains(@title,'Add facilitator') or contains(@data-bs-original-title,'Add facilitator')])[" + rowIndex1Based + "]");
        safeClick(locator);
    }

    public int addFacilitatorRowFromLastRow() {
        int beforeCount = getFacilitatorRowCount();

        clickAddFacilitatorIconByRow(beforeCount);

        wait.until(driver -> getFacilitatorRowCount() == beforeCount + 1);

        return getFacilitatorRowCount();
    }

    public void addFacilitatorRowsMultipleTimes(int times) {
        for (int i = 0; i < times; i++) {
            int beforeCount = getFacilitatorRowCount();
            int afterCount = addFacilitatorRowFromLastRow();

            if (afterCount != beforeCount + 1) {
                throw new AssertionError("Facilitator row was not added correctly. Before count = "
                        + beforeCount + ", After count = " + afterCount);
            }
        }
    }
    public void clickDeleteFacilitatorIconByRow(int rowIndex1Based) {
        By locator = By.xpath("(//div[@id='ex3-tabs-3']//a[contains(@onclick,'delete') or contains(@title,'Delete') or contains(@data-bs-original-title,'Delete')])[" + rowIndex1Based + "]");
        safeClick(locator);
    }

    public boolean isDeleteConfirmationPopupDisplayed() {
        return !driver.findElements(deleteFacilitatorConfirmPopup).isEmpty()
                && driver.findElement(deleteFacilitatorConfirmPopup).isDisplayed();
    }

    public void confirmDeleteFacilitator() {
        safeClick(deleteFacilitatorConfirmOkButton);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(deleteFacilitatorConfirmPopup));
    }

    public void cancelDeleteFacilitator() {
        safeClick(deleteFacilitatorConfirmCancelButton);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(deleteFacilitatorConfirmPopup));
    }

    public boolean isMinimumFacilitatorValidationDisplayed() {
        return !driver.findElements(minimumFacilitatorValidation).isEmpty()
                && driver.findElement(minimumFacilitatorValidation).isDisplayed();
    }

    public void deleteLastFacilitatorRowIfMoreThanOneExists() {
        int beforeCount = getFacilitatorRowCount();

        if (beforeCount <= 1) {
            throw new AssertionError("Cannot delete facilitator row because only one row is available.");
        }

        clickDeleteFacilitatorIconByRow(beforeCount);

        if (!isDeleteConfirmationPopupDisplayed()) {
            throw new AssertionError("Delete confirmation popup was not displayed.");
        }

        confirmDeleteFacilitator();

        wait.until(driver -> getFacilitatorRowCount() == beforeCount - 1);
    }

    public void deleteFacilitatorRowsUntilOnlyOneRemains() {
        while (getFacilitatorRowCount() > 1) {
            deleteLastFacilitatorRowIfMoreThanOneExists();
        }
    }
    public void waitForFacilitatorInfoTabToBeVisible() {
        // wait for popup to disappear first
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
            By.xpath("//div[contains(@class,'swal-modal')]")
        ));

        // wait for overlay if present
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
            By.xpath("//div[contains(@class,'swal-overlay')]")
        ));

        // now wait for facilitator section
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//h3[normalize-space()='Facilitator Info']")
        ));
    }
}

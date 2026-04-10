package com.potentia.automation.pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;




public class AddAssignmentPage {

	private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;
    private Actions actions;

    public AddAssignmentPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
        this.actions = new Actions(driver);
    }
    private final By myBatches = By.xpath("//a[normalize-space()='My Batches']");
	 // Top-level tabs
    private final By activeTab = By.id("activeBatch");
    private final By plannedTab = By.id("plannedBatch");
    private final By inactiveTab = By.id("inactiveBatch");

    // Batch list
    private final By batchListContainer = By.cssSelector(".bach_info_list");
    private final By batchCards = By.cssSelector(".bach_info_list .list-item-container");
    private By mastersMenu = By.xpath("//a[normalize-space()='Masters']");
    private By likertScaleOption = By.xpath("//a[normalize-space()='Likert scale']");
    private By tableRows = By.cssSelector("#likertDataTable tbody tr");
    // 🔹 Locators
    private By pageHeading = By.xpath("//h1[text()='Activities']");
    private By activitiesTab = By.xpath("//a[contains(text(),'Activities')]");
    private By allTabs = By.xpath("//ul[contains(@class,'navbar-nav')]//a");

    private By breadcrumbBatch = By.xpath("//ol[contains(@class,'breadcrumb')]//li[2]//span");

    private By addActivityBtn = By.xpath("//a[contains(@href,'addactivity')]");
    private By superActivityBtn = By.xpath("//a[contains(.,'Super Activity')]");
    private By copyActivityBtn = By.xpath("//a[contains(.,'Copy Activity')]");
    
    // Tabs
    private By activityTypeTab = By.id("ex3-tab-1");
    private By activityDetailsTab = By.id("ex3-tab-3");
    private By activitySettingsTab = By.id("ex3-tab-4");

    private By addActivityHeading = By.xpath("//h1[contains(text(),'Add Activity')]");
    private By addActivitycancelBtn = By.xpath("//button[normalize-space()='Cancel']");
    
    // 🔹 Get all activity labels
    private By activityBlocks = By.xpath("//div[contains(@class,'activity-type-selection')]");
    private By allActivities = By.xpath("//div[contains(@class,'activity-type-selection')]//label");
    
    //Activity Locators
    private By assignmentLabel = By.xpath("//label[.//span[text()='Assignment']]");
    private By assignmentRadio = By.id("act_type_4");
    private By titleField = By.id("title");
 // Toast title error Locators (GENERIC - reusable)
    private By toastContainer = By.id("toast-container");
    private By toastMessage = By.cssSelector("#toast-container .toast-message");
    private By toastTitle = By.cssSelector("#toast-container .toast-title");
    private By titlehelpIcon = By.cssSelector("em.info");
    private By descriptionToggle = By.cssSelector("a[href='#toggleDescription']");
    private By descriptionTextArea = By.id("description");
    private By descriptionhelpIcon = By.cssSelector(".dcrpin_txt em.info");
    // ===== LOCATORS ===== //
   
    private By activityInstructionhelpIcon = By.xpath("//label[contains(.,'Activity Instructions')]//em[contains(@class,'info')]");
    private By allTags = By.cssSelector("#communicationTags span");
    private By editor = By.cssSelector(".fr-element");
    private By fileInputs = By.xpath("//input[@type='file']");
    
    private By autoQuesCheckbox = By.id("autoQuesNum");
    private By autoQuesLabel = By.xpath("//label[@for='autoQuesNum']");
    private By autoQuestionNumberhelpIcon = By.xpath("//label[contains(.,'Auto question numbering')]//em[contains(@class,'info')]");
    
    //Add  Question button and Select question type Locatoers
    private By addQuestionBtn = By.cssSelector("button[data-bs-target='#selectQuesModal']");
    private By modal = By.id("selectQuesModalLabel");
    private By allQuestions = By.cssSelector(".modal-question li");
    private By questionText = By.xpath(".//a");
    private By helpIcon = By.xpath(".//em[contains(@class,'info')]");
    private By selectQuestionModelcloseBtn = By.xpath("//h1[text()='Select Question Type']/ancestor::div[@class='modal-content']//button[@aria-label='Close']");

    //Radio Buttons Question locators
    private By radioButtonOption = By.xpath("//a[contains(.,'Multiple Choice - Radio buttons')]");
    private By newModalTitle = By.id("modalAddQuestionLabel");
    private By quesType = By.id("quesType");
    private By questionTextbox = By.id("ques");
    private By qNoField = By.id("ques_no");
    private By responseRows = By.cssSelector(".resp-choices-list li");
    
    //Common Locators for all Question types
    private By responseRequiredCheckbox = By.id("ansRequired");
    private By responseRequiredLabel = By.xpath("//label[contains(text(),'Response required')]");
    private By addOthersCheckbox = By.id("addOthersOption");
    private By editorToggle = By.xpath("//i[contains(@class,'fa-laptop-code')]");
    private By richEditor = By.cssSelector(".fr-box");
    private By editorContent = By.cssSelector(".fr-element.fr-view");
    private By toolbarButton(String cmd) {
        return By.xpath("//button[@data-cmd='" + cmd + "']");
    }
    
    
    private By answerHelp = By.cssSelector(".answer-help");
    private By explanationHelp = By.cssSelector(".explanation-help");
    private By addQuestionModalCloseBtn =
    	    By.xpath("//h5[@id='modalAddQuestionLabel']/ancestor::div[@class='modal-content']//button[@aria-label='Close']");
    private By addQuestionCancelBtn = By.xpath("//button[text()='Cancel']");
    private By addQuestionSaveBtn = By.id("btnQuesAdd");
    
    private By activityDetailssaveAndContinueBtn = By.xpath("(//button[contains(text(),'Save & Continue')])[1]");
    private By ActivityDetailsSaveAndExitBtn= By.xpath("//button[contains(@class,'save-exit') and normalize-space()='Save & Exit']");
   
    

    
    
    public void waitForMyBatchesPageToLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(batchListContainer));
        wait.until(ExpectedConditions.visibilityOfElementLocated(activeTab));
    }
    protected WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickability(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void click(By locator) {
        waitForClickability(locator).click();
    }

    protected String getText(By locator) {
        return waitForVisibility(locator).getText();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return waitForVisibility(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    public void scrollToElement(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", element);
    }
    public void clickLikertScale() {

        wait.until(ExpectedConditions.elementToBeClickable(mastersMenu)).click();
        wait.until(ExpectedConditions.elementToBeClickable(likertScaleOption)).click();
    }
    public List<Map<String, String>> getLikertTableData() {

        wait.until(ExpectedConditions.visibilityOfElementLocated(tableRows));

        List<Map<String, String>> dataList = new ArrayList<>();

        List<WebElement> rows = driver.findElements(tableRows);

        for (WebElement row : rows) {

            List<WebElement> cols = row.findElements(By.tagName("td"));

            Map<String, String> data = new HashMap<>();

            data.put("Title", cols.get(1).getText().trim());
            data.put("Points", cols.get(2).getText().trim());

            dataList.add(data);
        }

        return dataList;
    }
    public void clickOnMyBatches() {
    	driver.findElement(myBatches).click();
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
 // 🔹 Common Methods
    private WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private WebElement waitClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    private void safeClick(By locator) {
        WebElement element = waitClickable(locator);
        try {
            element.click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", element);
        }
    }

    private void scrollTo(By locator) {
        WebElement element = waitVisible(locator);
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    // 🔹 Page Validations

    public boolean isActivitiesPageLoaded() {
        return waitVisible(pageHeading).isDisplayed();
    }

    public boolean isActivitiesTabActive() {
        return driver.findElement(activitiesTab).getAttribute("class").contains("active");
    }

    public boolean isOnlyActivitiesTabActive() {
        List<WebElement> tabs = driver.findElements(allTabs);

        for (WebElement tab : tabs) {
            String text = tab.getText();
            String cls = tab.getAttribute("class");

            if (text.equalsIgnoreCase("Activities")) {
                if (!cls.contains("active")) return false;
            } else {
                if (cls.contains("active")) return false;
            }
        }
        return true;
    }

    public String getBreadcrumbText() {
        return waitVisible(breadcrumbBatch).getText().trim();
    }

    public boolean isAddActivityButtonVisible() {
        return waitVisible(addActivityBtn).isDisplayed();
    }

    public boolean isSuperActivityVisible() {
        return waitVisible(superActivityBtn).isDisplayed();
    }

    public boolean isCopyActivityVisible() {
        return waitVisible(copyActivityBtn).isDisplayed();
    }

    public boolean isAddActivityEnabled() {
        return waitVisible(addActivityBtn).isEnabled();
    }

    // 🔹 Actions

    public void clickAddActivity() {
        safeClick(addActivityBtn);
    }

    public boolean isAddActivityPageLoaded() {
        return waitVisible(addActivityHeading).isDisplayed();
    }

    public void clickCancel() {
        scrollTo(addActivitycancelBtn);
        safeClick(addActivitycancelBtn);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    // 🔹 Get count
    public int getActivityCount() {
        return driver.findElements(allActivities).size();
    }

    // 🔹 Get all activities names
    public String getActivityName(int index) {

        WebElement block = driver.findElements(activityBlocks).get(index);

        return block.findElement(By.tagName("span")).getText().trim();
    }
    // 🔹 Check visibility
    public boolean isActivityVisible(int index) {
        return driver.findElements(allActivities).get(index).isDisplayed();
    }

    // 🔹 Check all Activities radio button present
    public boolean isRadioPresent(int index) {

        WebElement block = driver.findElements(activityBlocks).get(index);

        WebElement radio = block.findElement(By.xpath(".//input[@type='radio']"));

        // ✅ Check existence instead of visibility
        return radio != null;
    }
    // 🔹 Get tooltip dynamically
    public String getTooltip(int index) {

        WebElement block = driver.findElements(activityBlocks).get(index);

        WebElement label = block.findElement(By.tagName("label"));

        actions.moveToElement(label).perform();

        String tooltip = label.getAttribute("data-bs-original-title");

        return tooltip != null ? tooltip.trim() : "";
    }
    public boolean isAssignmentVisible() {
        return waitVisible(assignmentLabel).isDisplayed();
    }

    public boolean isAssignmentRadioDisplayed() {
        return waitVisible(assignmentRadio).isDisplayed();
    }

    // 🔹 Tooltip Handling
    public String getAssignmentTooltipText() {

        WebElement element = waitVisible(assignmentLabel);

        // Hover
        actions.moveToElement(element).perform();

        // Get tooltip text
        String tooltip = element.getAttribute("data-bs-original-title");

        return tooltip != null ? tooltip.trim() : "";
    }

    public boolean isAssignmentTooltipPresent() {
        String tooltip = getAssignmentTooltipText();
        return !tooltip.isEmpty();
    }

    // 🔹 Actions
    public void selectAssignmentActivity() {
        waitClickable(assignmentLabel).click();
    }

    public boolean isAssignmentSelected() {
        return driver.findElement(assignmentRadio).isSelected();
    }
    
    // ================= TAB VALIDATION =================

    public boolean isActivityDetailsTabActive() {
        return driver.findElement(activityDetailsTab)
                .getAttribute("class")
                .contains("active");
    }
    public boolean isStillOnActivityDetails() {
        WebElement tab = driver.findElement(By.id("ex3-tab-3"));
        return tab.getAttribute("class").contains("active");
    }
    public boolean isActivityTypeTabCompleted() {
        return driver.findElement(activityTypeTab)
                .getAttribute("class")
                .contains("prgs_cmplt");
    }
    public void clickActivityTypeTab() {
    	driver.findElement(activityTypeTab).click();
    }
    public boolean isNavigatedToActivityType() {
        WebElement tab = driver.findElement(By.id("ex3-tab-1"));
        return tab.getAttribute("class").contains("active");
    }

    public boolean isActivitySettingsTabDisabled() {
        return driver.findElement(activitySettingsTab)
                .getAttribute("class")
                .contains("disabled");
    }
    
    // =================ACTIVITY VALIDATIONS =================
    public boolean isActivityTitleDisplayed() {
        return waitVisible(titleField).isDisplayed();
    }

    public boolean isActivityInstructionsEditorDisplayed() {
        return waitVisible(editor).isDisplayed();
    }
    
 // ================= Browser ALERT HANDLER when we click on Tabs =================

    public Alert waitForAlert(int timeoutInSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds))
                .until(ExpectedConditions.alertIsPresent());
    }

    public String getAlertText() {
        return waitForAlert(10).getText();
    }

    public void acceptAlert() {
        waitForAlert(10).accept();   // OK
    }

    public void dismissAlert() {
        waitForAlert(10).dismiss();  // Cancel
    }

    public boolean isAlertPresent() {
        try {
            waitForAlert(5);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void enterTitle(String title) {
    	scrollToElement(titleField);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(titleField));
        element.clear();
        element.sendKeys(title);
    }
    public String getToastMessage() {
        WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(toastMessage));
        return toast.getText().trim();
    }

    public String getToastTitle() {
        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(toastTitle));
        return title.getText().trim();
    }
    public boolean isToastDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(toastContainer)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
    public String getTitleValue() {
        return driver.findElement(titleField).getAttribute("value");
    }

   
    public String getHelpText() {
        WebElement help = driver.findElement(titlehelpIcon);
        Actions actions = new Actions(driver);
        actions.moveToElement(help).perform();

        return help.getAttribute("data-bs-original-title");
    }
    public void toggleDescription() {
        driver.findElement(descriptionToggle).click();
    }

    public boolean isDescriptionVisible() {
        try {
            return driver.findElement(descriptionTextArea).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void enterDescription(String text) {
    	scrollToElement(descriptionTextArea);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(descriptionTextArea));
        element.clear();
        element.sendKeys(text);
    }

    public String getDescriptionValue() {
        return driver.findElement(descriptionTextArea).getAttribute("value");
    }

    public String getdescriptionHelpText() {
        WebElement help = driver.findElement(descriptionhelpIcon);
        Actions actions = new Actions(driver);
        actions.moveToElement(help).perform();

        return help.getAttribute("data-bs-original-title");
    }

    // Resize using JS (simulate drag)
    public void resizeDescription(int height) {
        WebElement element = driver.findElement(descriptionTextArea);
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].style.height='" + height + "px'", element);
    }

    public int getHeight() {
        return driver.findElement(descriptionTextArea).getSize().getHeight();
    }
    public List<String> getAllTags() {
        return driver.findElements(allTags)
                .stream()
                .map(e -> e.getText().trim())
                .collect(Collectors.toList());
    }

    public void clickAllTags() {
        List<WebElement> tags = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(allTags));

        for (WebElement tag : tags) {
            wait.until(ExpectedConditions.elementToBeClickable(tag)).click();
        }
    }

    public boolean areAllTagsInserted() {
        String text = getInstructionsEditorText();

        for (String tag : getAllTags()) {
            if (!text.contains(tag)) {
                return false;
            }
        }
        return true;
    }
    
    // ================= TOOLBAR ================= //

    public void clickToolbarCommand(String command) {
        By locator = By.cssSelector("button[data-cmd='" + command + "']");
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }
    public void applyAllToolbarActions() {

        List<String> commands = Arrays.asList(
                "bold", "italic", "underline", "strikeThrough",
                "alignLeft", "alignCenter", "alignJustify"
        );

        for (String cmd : commands) {
            try {
                clickToolbarCommand(cmd);
            } catch (Exception e) {
                System.out.println("Skipping: " + cmd);
            }
        }
    }


    public void enterText(String text) {
    	scrollToElement(editor);
        WebElement ed = wait.until(ExpectedConditions.visibilityOfElementLocated(editor));
        ed.sendKeys(text);
    }

    public String getInstructionsEditorText() {
        return driver.findElement(editor).getText();
    }

    public String getActivityInstructionHelpText() {
        WebElement help = driver.findElement(activityInstructionhelpIcon);
        new Actions(driver).moveToElement(help).perform();
        return help.getAttribute("data-bs-original-title");
    }

    // ================= DROPDOWN TOOLS ================= //

    public void selectFontFamily() {
        clickToolbarCommand("fontFamily");
        driver.findElement(By.xpath("//a[contains(@data-param1,'Arial')]")).click();
    }

    public void selectFontSize() {
        clickToolbarCommand("fontSize");
        driver.findElement(By.xpath("//a[@data-param1='18px']")).click();
    }

    public void selectParagraphFormat() {
        clickToolbarCommand("paragraphFormat");
        driver.findElement(By.xpath("//a[@data-param1='H1']")).click();
    }
 // ================= FILE UPLOAD ================= //

    public void uploadFile(String command, String filePath) {

        clickToolbarCommand(command);

        List<WebElement> inputs = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(fileInputs));

        for (WebElement input : inputs) {
            if (input.isDisplayed()) {
                input.sendKeys(filePath);
                return;
            }
        }

        throw new RuntimeException("File input not found");
    }

    public void insertImage(String filePath) {
        uploadFile("insertImage", filePath);
    }

    public void insertFile(String filePath) {
        uploadFile("insertFile", filePath);
    }
    public void scrollToAutoQuestion() {
        WebElement el = driver.findElement(autoQuesCheckbox);
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", el);
    }

    public boolean isAutoQuestionSelected() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(autoQuesCheckbox)).isSelected();
    }

    public void clickCheckbox() {
        wait.until(ExpectedConditions.elementToBeClickable(autoQuesCheckbox)).click();
    }

    public void clickLabel() {
        driver.findElement(autoQuesLabel).click();
    }

    public String getAutoQuestionNumberHelpText() {
        WebElement help = driver.findElement(autoQuestionNumberhelpIcon);
        new Actions(driver).moveToElement(help).perform();
        return help.getAttribute("data-bs-original-title");
    }
    public void scrollToAddQuestion() {
        WebElement el = driver.findElement(addQuestionBtn);
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", el);
    }

    public void clickAddQuestion() {
        wait.until(ExpectedConditions.elementToBeClickable(addQuestionBtn)).click();
    }
    public boolean isAddQuestionDisplayed() {
    	return driver.findElement(addQuestionBtn).isDisplayed();
    }

    public boolean isModalDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(modal)).isDisplayed();
    }

    // ================= QUESTION METHODS ================= //

    public List<WebElement> getAllQuestionElements() {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(allQuestions));
    }

    public List<String> getAllQuestionNames() {
        return getAllQuestionElements()
                .stream()
                .map(e -> e.findElement(questionText).getText().replace("?", "").trim())
                .collect(Collectors.toList());
    }

    // ================= HELP TEXT VALIDATION ================= //

    public Map<String, String> getAllHelpTexts() {

        Map<String, String> helpMap = new LinkedHashMap<>();

        List<WebElement> questions = getAllQuestionElements();

        for (WebElement q : questions) {

            String name = q.findElement(questionText).getText().replace("?", "").trim();

            WebElement help = q.findElement(helpIcon);

            new Actions(driver).moveToElement(help).perform();

            String helpText = help.getAttribute("data-bs-original-title");

            helpMap.put(name, helpText);
        }

        return helpMap;
    }
    public void clickquestionModelCloseButton() {
        WebElement element = wait.until(
                ExpectedConditions.elementToBeClickable(selectQuestionModelcloseBtn)
        );
        element.click();
    }

    // ===== ACTION ===== //

    public void clickRadioButtonQuestion() {
        wait.until(ExpectedConditions.elementToBeClickable(radioButtonOption)).click();
    }

    // ===== VALIDATIONS ===== //

    public String getModalTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(newModalTitle)).getText();
    }

    public String getQuestionTypeValue() {
        return driver.findElement(quesType).getAttribute("value");
    }

    public boolean isQuestionFieldDisplayed() {
        return driver.findElement(questionTextbox).isDisplayed();
    }

    public int getResponseRowCount() {
        return driver.findElements(responseRows).size();
    }

    public boolean isResponseRequiredChecked() {
        return driver.findElement(responseRequiredCheckbox).isSelected();
    }

    public boolean isAddOthersChecked() {
        return driver.findElement(addOthersCheckbox).isSelected();
    }

    public String getHelpText(By locator) {
        WebElement element = driver.findElement(locator);
        new Actions(driver).moveToElement(element).perform();
        return element.getAttribute("title");
    }

    public boolean isAddQuestionSaveButtonDisplayed() {
        return driver.findElement(addQuestionSaveBtn).isDisplayed();
    }

    public boolean isAddQuestionCancelButtonDisplayed() {
        return driver.findElement(addQuestionCancelBtn).isDisplayed();
    }
 // ===== ACTIONS ===== //

    public void setAutoQuestionCheckbox(boolean value) {
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(autoQuesCheckbox));

        if (checkbox.isSelected() != value) {
            checkbox.click();
        }
    }

    // ===== VALIDATIONS ===== //

    public boolean isQNoFieldDisplayed() {
        return driver.findElements(qNoField).size() > 0 &&
               driver.findElement(qNoField).isDisplayed();
    }

    public String getQNoValue() {
        return driver.findElement(qNoField).getAttribute("value");
    }

    public boolean isQNoBesideQuestion() {

        WebElement qNo = driver.findElement(qNoField);
        WebElement question = driver.findElement(questionTextbox);

        WebElement parent = qNo.findElement(By.xpath("./ancestor::div[contains(@class,'d-flex')]"));

        return parent.findElement(By.id("ques")).isDisplayed();
    }
    public void clickAddQuestionModalClose() {
        WebElement element = wait.until(
                ExpectedConditions.elementToBeClickable(addQuestionModalCloseBtn)
        );
        element.click();
    }
    
    //AddQuestionPopup common methods 
    // Actions
    public void clickResponseRequiredCheckbox() {
        wait.until(ExpectedConditions.elementToBeClickable(responseRequiredCheckbox)).click();
    }

    public void clickResponseRequiredLabel() {
        wait.until(ExpectedConditions.elementToBeClickable(responseRequiredLabel)).click();
    }

    public boolean isResponseRequiredSelected() {
        return driver.findElement(responseRequiredCheckbox).isSelected();
    }

    public void setResponseRequired(boolean value) {
        boolean current = isResponseRequiredSelected();
        if (current != value) {
            clickResponseRequiredCheckbox();
        }
    }
    public void enterQuestion(String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(questionTextbox));
        element.clear();
        element.sendKeys(text);
    }

    public String getQuestionText() {
        return driver.findElement(questionTextbox).getAttribute("value");
    }

    public boolean isTextAreaDisplayed() {
        return driver.findElement(questionTextbox).isDisplayed();
    }
    public void toggleEditor() {
        wait.until(ExpectedConditions.elementToBeClickable(editorToggle)).click();
    }

    public boolean isEditorDisplayed() {
        return driver.findElements(richEditor).size() > 0;
    }

    public void enterTextInEditor(String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(editorContent));
        element.sendKeys(text);
    }
    public String getQuestionEditorText() {
        return driver.findElement(editorContent).getText();
    }
 // ================= RESIZE =================

    public int getTextAreaHeight() {
        return driver.findElement(questionTextbox).getSize().getHeight();
    }

    public void resizeTextArea(int height) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].style.height='" + height + "px'",
                driver.findElement(questionTextbox));
    }
    public void clickQuestionEditorToolbar(String command) {
        wait.until(ExpectedConditions.elementToBeClickable(toolbarButton(command))).click();
    }

    // ================= ALERT =================

    public void handleEditorAlert(boolean accept) {
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        if (accept) alert.accept();
        else alert.dismiss();
    }

    public String getQuestionEditorAlertText() {
        return wait.until(ExpectedConditions.alertIsPresent()).getText();
    }
    public void clickSaveAndContinueBtn() {
    	scrollToElement(activityDetailssaveAndContinueBtn);
    	click(activityDetailssaveAndContinueBtn);
    }
    public void clickSaveAndExitBtn() {
    	scrollToElement(ActivityDetailsSaveAndExitBtn);
    	click(ActivityDetailsSaveAndExitBtn);
    }
    
    
}

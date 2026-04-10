package com.potentia.automation.pages;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

public class AddMagicWheelOneWheelPage {
	private WebDriver driver;
	private final JavascriptExecutor js;
	WebDriverWait wait;
	Actions actions;
	
	public AddMagicWheelOneWheelPage(WebDriver driver) {
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
	//private By firstBatchTitle = By.xpath("(//div[@class='list-item-title']//h4)[1]");
	private By addActivityBtn = By.xpath("//a[contains(@class, 'custom-btn') and contains(@href, 'addactivity')]");
	private By selectAcitivity = By.xpath("//label[span[text()='Magic Wheel']]");
	private By activityTitle = By.xpath("//*[@id = 'title']");
	private By activityDiscription = By.xpath("//*[@id = 'description']");
	private By activityInstructions = By.xpath("//div[@class='fr-element fr-view' and @contenteditable='true']");
	 // One Wheel tab button
    private By oneWheelTab = By.id("pills-home-tab");

    private By segmentSetUp = By.id("Spin_stops_display");
    private By segmentLabelInputs =
            By.xpath("//input[@name='segment_title[]']");

    private By segmentDescriptionInputs =
            By.xpath("//input[@name='question[]']");
 // Segment Colors container
    private By segmentColorsSection =
            By.xpath("//h6[contains(text(),'Segment Colors')]/ancestor::div[contains(@class,'parentclr')]");

    // All radio buttons under Segment Colors
    private By segmentColorRadios =
            By.xpath("//input[@type='radio' and @name='Wheel_design']");

    // Labels (optional – for logging)
    private By segmentColorLabels =
            By.xpath("//input[@type='radio' and @name='Wheel_design']/following-sibling::label");
    private By segmentRows = By.cssSelector("#Wheel_Segments .card-descriptions-row");
    private By segmentLabels = By.cssSelector("input.segment_title");
    private By bgColorPickers = By.cssSelector("input[name='segment_color[]']");
    private By textColorPickers = By.cssSelector("input[name='font_color[]']");
    
    // Segment description container
    @FindBy(id = "Wheel_Segments")
    private WebElement segmentSection;

    // All label input fields
    @FindBy(css = "input.segment_title")
    private List<WebElement> segmentLabelFields;
    private By spinDuration = By.id("spin");
    private By maxSpinAttempts = By.id("no_spins");
    private By wheelDiaMeter = By.id("wheel_diameter");
    private By WheelChoiceRepeat = By.id("choice_repeating");
    private By wheelPreview = By.xpath("(//a[contains(text(), 'Preview Wheel')])[1]");
    private By previewClose = By.cssSelector("div.modal.show button[aria-label='Close']");
    
    //DueDate
    private By dueDateSection = By.id("act_endDate");
    private By dateInput = By.id("due_date");
    private By calendarIcon =
            By.cssSelector("#act_endDate .fa-calendar");
    private By calendarPopup =
            By.cssSelector(".datepicker-dropdown");
    private By todayDate =
            By.cssSelector(".datepicker td.today, .datepicker td.active");
    private By disabledDates =
            By.cssSelector(".datepicker td.disabled, .datepicker td.old");
    private By nextMonthBtn =
            By.cssSelector(".datepicker .next");
    
    //DueTime
    private By timeSection = By.id("ActDueTimeBlock");
    private By timeInput = By.id("actDueTime");
    private By timeIcon =
            By.cssSelector("#ActDueTimeBlock .fa-clock");
    private By timePopup =
            By.cssSelector(".bootstrap-datetimepicker-widget");
    private By hourInput =
            By.cssSelector(".bootstrap-datetimepicker-widget .timepicker-hour");
    private By minuteInput =
            By.cssSelector(".bootstrap-datetimepicker-widget .timepicker-minute");
    private By amPmToggle =
            By.cssSelector(".bootstrap-datetimepicker-widget button[data-action='togglePeriod']");
    
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
	public void addActivityBtn() {
		driver.findElement(addActivityBtn).click();
	}
	
	public void selectActivity() {
		driver.findElement(selectAcitivity).click();
	}
	public void addActivityTitle(String Title) {
		driver.findElement(activityTitle).sendKeys(Title);
	}
	public void clearActivityTitle() {
		driver.findElement(activityTitle).clear();;
	}
	public String getActivityTitle() {
		WebElement getTitle = driver.findElement(By.xpath("//*[@id = 'title']"));
		return getTitle.getAttribute("value").trim();
	}
	public boolean isActivityTitleErrorMessageDisplayed() {
        return driver.findElement(By.xpath("//*[contains(text(), 'Please enter activity title')]")).isDisplayed();
	}
	public String getActivityTitleErrorMessageText() {
	        WebElement errorMessageElement = driver.findElement(By.xpath("//*[contains(text(), 'Please enter activity title')]"));
	        return errorMessageElement.getText().trim();
	}
	public boolean isActivityTitleLengthErrorMessageDisplayed() {
        return driver.findElement(By.xpath("//*[contains(text(), 'Activity title is too long, max 150 characters allowed')]")).isDisplayed();
	}
	public String getActivityTitleLengthErrorMessageText() {
        WebElement errorMessageElement = driver.findElement(By.xpath("//*[contains(text(), 'Activity title is too long, max 150 characters allowed')]"));
        return errorMessageElement.getText().trim();
	}
	public boolean isActivityInstructionsLengthErrorMessageDisplayed() {
        return driver.findElement(By.xpath("//*[contains(text(), 'Activity instructions is too long, max 65000 characters allowed')]")).isDisplayed();
	}
	public String getActivityInstructionsLengthErrorMessageText() {
        WebElement errorMessageElement = driver.findElement(By.xpath("//*[contains(text(), 'Activity instructions is too long, max 65000 characters allowed')]"));
        return errorMessageElement.getText().trim();
	}
	public boolean isExistingTitleErrorMessageDisplayed() {
        return driver.findElement(By.xpath("//*[contains(text(), 'Activity with this title already exists. Please try adding the activity with a unique title')]")).isDisplayed();
	}
	public String getExistingTilteErrorMessageText() {
        WebElement errorMessageElement = driver.findElement(By.xpath("//*[contains(text(), 'Activity with this title already exists. Please try adding the activity with a unique title')]"));
        return errorMessageElement.getText().trim();
	}
	public void clickActivityDiscriptionDropDownArrow() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		// Locate the button
		WebElement myButton = wait.until(ExpectedConditions.presenceOfElementLocated(
		    By.xpath("//a[text() = 'Description ']")));

		// Scroll down gradually until the button is visible
		for (int i = 0; i < 5; i++) {  
		    js.executeScript("window.scrollBy(0, -200);");  
		    try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		    if (myButton.isDisplayed()) break;
		}

		// Ensure the button is visible in viewport
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", myButton);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		// Wait for the button to be clickable
		myButton = wait.until(ExpectedConditions.elementToBeClickable(
		    By.xpath("//a[text() = 'Description ']")));

		// Wait for any overlays to disappear
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("preloader"))); 

		// Try clicking the button
		try {
		    myButton.click();
		} catch (Exception e) {
		    System.out.println("Normal click failed, trying JavaScript click.");
		    js.executeScript("arguments[0].click();", myButton);
		}
	}
	public void addActivityDiscription (String Discription) {
		driver.findElement(activityDiscription).sendKeys(Discription);
	}
	public void addActivityInstruction(String Instructions) {
		driver.findElement(activityInstructions).sendKeys(Instructions);
	}
	public String getActivityInstructions() {
		WebElement getTitle = driver.findElement(By.xpath("//*[@id = 'title']"));
		return getTitle.getAttribute("value").trim();
	}
	public void clickAssignmentSaveAndContinue() {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		// Locate the button
		WebElement myButton = wait.until(ExpectedConditions.presenceOfElementLocated(
		    By.xpath("(//button[contains(text(), 'Save & Continue')])[1]")));

		// Scroll down gradually until the button is visible
		for (int i = 0; i < 5; i++) {  
		    js.executeScript("window.scrollBy(0, 200);");  
		    try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		    if (myButton.isDisplayed()) break;
		}

		// Ensure the button is visible in viewport
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", myButton);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		// Wait for the button to be clickable
		myButton = wait.until(ExpectedConditions.elementToBeClickable(
		    By.xpath("(//button[contains(text(), 'Save & Continue')])[1]")));

		// Wait for any overlays to disappear
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("preloader"))); 

		// Try clicking the button
		try {
		    myButton.click();
		} catch (Exception e) {
		    System.out.println("Normal click failed, trying JavaScript click.");
		    js.executeScript("arguments[0].click();", myButton);
		}
    }
	public void scrollToMagicOneWheelTab() {
		WebElement tab = new WebDriverWait(driver, Duration.ofSeconds(10))
	            .until(ExpectedConditions.presenceOfElementLocated(oneWheelTab));

	    ((JavascriptExecutor) driver)
	            .executeScript("arguments[0].scrollIntoView({block:'center'});", tab);
	}
	public boolean isMagicOneWheelBtnPresent() {
	    return driver.findElements(oneWheelTab).size() > 0;
	}
	public boolean isOneWheelTabActive() {
	    WebElement tab = driver.findElement(oneWheelTab);
	    return tab.getAttribute("aria-selected").equals("true")
	            && tab.getAttribute("class").contains("active");
	}
	public void selectOneWheelTabIfNotActive() {
	    WebElement tab = driver.findElement(oneWheelTab);

	    if (!tab.getAttribute("aria-selected").equals("true")) {
	        tab.click();
	    }
	}
	public void scrollIntoSetmentSetUp() {
		 WebElement segment = wait.until(
		            ExpectedConditions.presenceOfElementLocated(segmentSetUp)
		        );

		        ((JavascriptExecutor) driver).executeScript(
		            "arguments[0].scrollIntoView({block:'center'});", segment
		        );
		        
	}
	public String getDefaultSelectedOption() {
        Select select = new Select(driver.findElement(segmentSetUp));
        return select.getFirstSelectedOption().getText().trim();
    }
	public List<WebElement> getAllOptions() {
        Select select = new Select(driver.findElement(segmentSetUp));
        return select.getOptions();
    }

    public void selectSegmentSetUp(String SetUp) {
        Select select = new Select(driver.findElement(segmentSetUp));
        select.selectByVisibleText(SetUp);
    }

    public String getSelectedSegmentSetUp() {
        Select select = new Select(driver.findElement(segmentSetUp));
        return select.getFirstSelectedOption().getText().trim();
    }
    public boolean isLabelInputVisible() {
        List<WebElement> labels = driver.findElements(segmentLabelInputs);
        return labels.size() > 0 && labels.get(0).isDisplayed();
    }

    public boolean isDescriptionInputVisible() {
        List<WebElement> descriptions = driver.findElements(segmentDescriptionInputs);
        return descriptions.size() > 0 && descriptions.get(0).isDisplayed();
    }
    public boolean isSegmentColorsSectionDisplayed() {
        return driver.findElement(segmentColorsSection).isDisplayed();
    }

    public List<WebElement> getAllSegmentColorRadios() {
        return driver.findElements(segmentColorRadios);
    }

    public List<WebElement> getAllSegmentColorLabels() {
        return driver.findElements(segmentColorLabels);
    }

    public boolean isRadioEnabled(int index) {
        return getAllSegmentColorRadios().get(index).isEnabled();
    }

    public void clickRadio(int index) {
        getAllSegmentColorRadios().get(index).click();
    }

    public boolean isRadioSelected(int index) {
        return getAllSegmentColorRadios().get(index).isSelected();
    }
    public String getLabelBgColor(int index) {
        return driver.findElements(segmentLabels)
                .get(index)
                .getCssValue("background-color");
    }

    public String getLabelTextColor(int index) {
        return driver.findElements(segmentLabels)
                .get(index)
                .getCssValue("color");
    }

    public String getBgPickerValue(int index) {
        return driver.findElements(bgColorPickers)
                .get(index)
                .getAttribute("value");
    }

    public String getTextPickerValue(int index) {
        return driver.findElements(textColorPickers)
                .get(index)
                .getAttribute("value");
    }
 // Scroll to Segment Description
    public void scrollToSegmentSection() {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", segmentSection);
    }
    // Verify default 3 label fields are visible
    public boolean areThreeLabelFieldsVisible() {
        wait.until(ExpectedConditions.visibilityOfAllElements(segmentLabelFields));
        return segmentLabelFields.size() == 3;
    }
    
    // Enter text into label fields
    public void enterSegmentLabels(List<String> labels) {
        for (int i = 0; i < labels.size(); i++) {
            WebElement field = segmentLabelFields.get(i);
            wait.until(ExpectedConditions.elementToBeClickable(field));
            field.clear();
            field.sendKeys(labels.get(i));
        }
    }
    public int getSegmentCount() {
    	wait.until(ExpectedConditions.visibilityOfAllElements(segmentLabelFields));
        return segmentLabelFields.size();
    }
    public String getActualBgColor(int index) {
        return segmentLabelFields.get(index)
                .getCssValue("background-color");
    }

    public String getActualTextColor(int index) {
        return segmentLabelFields.get(index)
                .getCssValue("color");
    }
    public void addSegment() {
        List<WebElement> rows = driver.findElements(segmentRows);
        WebElement lastRow = rows.get(rows.size() - 1);

        WebElement addBtn = lastRow.findElement(
                By.cssSelector("button[data-bs-original-title='Add']")
        );

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", addBtn);

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                segmentRows, rows.size()));
    }
    public void enterTextInLastSegment(String text) {

        List<WebElement> labels = driver.findElements(segmentLabels);
        WebElement lastLabel = labels.get(labels.size() - 1);

        wait.until(ExpectedConditions.elementToBeClickable(lastLabel));
        lastLabel.clear();
        lastLabel.sendKeys(text);
    }
    public void deleteLastSegment() {

    	List<WebElement> rows = driver.findElements(segmentRows);
        WebElement lastRow = rows.get(rows.size() - 1);

        WebElement deleteBtn = lastRow.findElement(
                By.cssSelector("button[onclick*='deleteWheelSegment']")
        );

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", deleteBtn);
    }
    public boolean isMinimum3SegmentErrorMessage() {
    	return driver.findElement(By.xpath("//*[contains(text(), 'Minimum 3 wheel segments are required for this activity to function.')]")).isDisplayed();
         
	}
    public String getMinimumSegmentErrorMessage() {
        WebElement error = driver.findElement(By.xpath("//*[contains(text(), 'Minimum 3 wheel segments are required for this activity to function.')]"));
        return error.getText();
	}
    public void spinDurationfield(String spinTime) {
    	driver.findElement(spinDuration).click();
    	driver.findElement(spinDuration).clear();
    	driver.findElement(spinDuration).sendKeys(spinTime);
    }
    public void clearSpinDurationField() {
    	driver.findElement(spinDuration).clear();
    }
    public String getSpinDuration() {
    	String maxspin = driver.findElement(spinDuration).getAttribute("value");
    	return maxspin;
    }
    public boolean isSpinDurationErrorMessage() {
    	return driver.findElement(By.xpath("//*[contains(text(), 'Please specify seconds to spin.')]")).isDisplayed();
         
	}
    public String getSpinDurationErrorMessage() {
        WebElement error = driver.findElement(By.xpath("//*[contains(text(), 'Please specify seconds to spin.')]"));
        return error.getText();
	}
    public void maxSpinAttemptsfield(String spinTime) {
    	driver.findElement(maxSpinAttempts).click();
    	driver.findElement(maxSpinAttempts).clear();
    	driver.findElement(maxSpinAttempts).sendKeys(spinTime);
    }
    public void clearMaxSpinAttemptsField() {
    	driver.findElement(maxSpinAttempts).clear();
    }
    public String getMaxSpinAttempts() {
    	String maxspin = driver.findElement(maxSpinAttempts).getAttribute("value");
    	return maxspin;
    }
    public boolean isMaxSpinAttemptsErrorMessage() {
    	return driver.findElement(By.xpath("//*[contains(text(), 'Please specify Max Spin Attempts.')]")).isDisplayed();
         
	}
    public String getMaxSpinAttemptsErrorMessage() {
        WebElement error = driver.findElement(By.xpath("//*[contains(text(), 'Please specify Max Spin Attempts.')]"));
        return error.getText();
	}
    public String getDefaultSelectedWheelDiameterOption() {
        Select select = new Select(driver.findElement(wheelDiaMeter));
        return select.getFirstSelectedOption().getText().trim();
    }
	public List<WebElement> getAllWheelDismeterOptions() {
        Select select = new Select(driver.findElement(wheelDiaMeter));
        return select.getOptions();
    }

    public void selectWheelDiaMeter(String SetUp) {
        Select select = new Select(driver.findElement(wheelDiaMeter));
        select.selectByVisibleText(SetUp);
    }

    public String getSelectedWheelDiameter() {
        Select select = new Select(driver.findElement(wheelDiaMeter));
        return select.getFirstSelectedOption().getText().trim();
    }
    public boolean isWheelchoiceRepeatfieldDisplayed() {
    	return driver.findElement(WheelChoiceRepeat).isDisplayed();
    }
    public void clickOnWheelChoiceRepeat() {
    	driver.findElement(WheelChoiceRepeat).click();
    }
    public void scrollIntoWheelPreview() {
		 WebElement preview = wait.until(
		            ExpectedConditions.presenceOfElementLocated(wheelPreview)
		        );

		        ((JavascriptExecutor) driver).executeScript(
		            "arguments[0].scrollIntoView({block:'center'});", preview
		        );
		        
	}
    public void clickOnWheelPreview() {
    	driver.findElement(wheelPreview).click();
    }
    public void clickOnClosePreview() {
    	driver.findElement(previewClose).click();
    }
    // 🔹 Date field displayed
    public boolean isDateFieldDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(dueDateSection)).isDisplayed();
    }

    // 🔹 Time field displayed
    public boolean isTimeFieldDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(timeSection)).isDisplayed();
    }

    // 🔹 Date enabled
    public boolean isDateFieldEnabled() {
        return driver.findElement(dueDateSection).isEnabled();
    }

    // 🔹 Time enabled
    public boolean isTimeFieldEnabled() {
        return driver.findElement(timeSection).isEnabled();
    }

    // 🔹 Get Date value
    public String getActivityDueDateValue() {
        return driver.findElement(dateInput).getAttribute("value");
    }

    // 🔹 Get Time value
    public String getActivityDueTimeValue() {
        return driver.findElement(timeInput).getAttribute("value");
    }
    public void openCalendar() {
        driver.findElement(dateInput).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(calendarPopup));
    }

    public boolean isCalendarDisplayed() {
        return driver.findElement(calendarPopup).isDisplayed();
    }

    public boolean isTodayHighlighted() {
        return driver.findElement(todayDate).isDisplayed();
    }

    public boolean arePastDatesDisabled() {
        List<WebElement> dates = driver.findElements(disabledDates);
        return dates.size() > 0;
    }

    public void selectFutureDate(LocalDate targetDate) {

        openCalendar();

        LocalDate today = LocalDate.now();

        while (today.getMonthValue() != targetDate.getMonthValue()
                || today.getYear() != targetDate.getYear()) {

            driver.findElement(nextMonthBtn).click();
            today = today.plusMonths(1);
        }

        By dateCell = By.xpath(
                "//td[not(contains(@class,'disabled')) and text()='"
                        + targetDate.getDayOfMonth() + "']"
        );

        wait.until(ExpectedConditions.elementToBeClickable(dateCell)).click();
    }

    /* ---------- TIME ---------- */

    public void openTimePicker() {
        driver.findElement(timeInput).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(timePopup));
    }

    public void setTime(String time) {
    	String hour;
        String minute;
        String amPm;

        time = time.trim().toUpperCase();

        // -------- 24-hour format (HH:mm or HH:mm AM/PM) --------
        if (time.matches("^([01]?\\d|2[0-3]):[0-5]\\d(\\s?(AM|PM))?$")) {

            String[] hm = time.split(" ")[0].split(":");
            int hour24 = Integer.parseInt(hm[0]);
            minute = hm[1];

            if (hour24 == 0) {
                hour = "12";
                amPm = "AM";
            } else if (hour24 < 12) {
                hour = String.valueOf(hour24);
                amPm = "AM";
            } else if (hour24 == 12) {
                hour = "12";
                amPm = "PM";
            } else {
                hour = String.valueOf(hour24 - 12);
                amPm = "PM";
            }
        }
        // -------- 12-hour format (hh:mm AM/PM) --------
        else if (time.matches("^(0?[1-9]|1[0-2]):[0-5]\\d\\s?(AM|PM)$")) {

            String[] parts = time.split(" ");
            String[] hm = parts[0].split(":");

            hour = hm[0];
            minute = hm[1];
            amPm = parts[1];
        }
        else {
            throw new IllegalArgumentException(
                "Invalid time format. Use HH:mm or hh:mm AM/PM. Got: " + time
            );
        }

        // 🔑 UI hours are zero-padded (01, 02, 06)
        hour = hour.length() == 1 ? "0" + hour : hour;

        openTimePicker();

        // -------- Select Hour --------
        driver.findElement(hourInput).click();
        driver.findElement(
            By.xpath("//td[@data-action='selectHour' and normalize-space(text())='" + hour + "']")
        ).click();

        // -------- Select Minute --------
        driver.findElement(minuteInput).click();
        driver.findElement(
            By.xpath("//td[@data-action='selectMinute' and text()='" + minute + "']")
        ).click();

        // -------- Select AM / PM --------
        WebElement meridian = driver.findElement(amPmToggle);
        if (!meridian.getText().equalsIgnoreCase(amPm)) {
            meridian.click();
        }
    }

    public String getSelectedTime() {
        return driver.findElement(timeInput).getAttribute("value");
    }
    public String generateText(int length) {
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length) {
            sb.append("A");
        }
        return sb.substring(0, length);
    }
}

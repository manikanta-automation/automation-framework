package com.potentia.automation.pages;

import java.io.File;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AddReferenceMaterialPage {
private WebDriver driver;
private final WebDriverWait wait;
private final JavascriptExecutor js;
	
	public AddReferenceMaterialPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
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
	private By addActivityBtn = By.xpath("//a[contains(@class, 'custom-btn') and contains(@href, 'addactivity')]");
	private By selectAcitivity = By.xpath("//label[span[text()='Reference Material']]");
	private By activityTitle = By.xpath("//*[@id = 'title']");
	private By activityDiscription = By.xpath("//*[@id = 'description']");
	private By activityInstructions = By.xpath("//div[@class='fr-element fr-view' and @contenteditable='true']");
	private By dueDateInput = By.id("due_date");
	@FindBy(id = "super_activity")
    private WebElement superActivityDropdown;
	private By responseAfterDeadlineCheckbox = By.id("allow_after_deadline");
    private By responseAfterDeadlineLabel = By.cssSelector("label[for='allow_after_deadline']");
    private By readOnlyCheckbox = By.id("read_only");
    private By readOnlyLabel = By.cssSelector("label[for='read_only']");
    @FindBy(id = "fileUpload-1")
    private WebElement fileInput;

    @FindBy(xpath = "//div[contains(@class,'file_uld')]")
    WebElement uploadedFileContainer;
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
	public void clickReferenceMaterialSaveAndContinue() {
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
	public boolean isDueDateFieldDisplayed() {
        return driver.findElement(dueDateInput).isDisplayed();
    }

    public boolean isDueDateFieldEnabled() {
        return driver.findElement(dueDateInput).isEnabled();
    }
    public List<String> getSuperActivities() {
        Select select = new Select(superActivityDropdown);
        return select.getOptions().stream()
                .map(WebElement::getText)
                .filter(val -> !val.contains("--Select Super Activity --"))
                .collect(Collectors.toList());
    }
    public String printSuperActivities() {
        List<String> activities = getSuperActivities();
        if (activities.isEmpty()) {
            return "No super activities are available";
        } else {
            return "Available Super Activities: " + activities;
        }
    }
    public void clickAllowResponseAfterDeadlineCheckbox() {
        driver.findElement(responseAfterDeadlineCheckbox).click();
    }

    public boolean isAllowResponseAfterDeadlineCheckboxSelected() {
        return driver.findElement(responseAfterDeadlineCheckbox).isSelected();
    }

    public void clickResponseAfterDeadlineLabel() {
        driver.findElement(responseAfterDeadlineLabel).click();
    }
    public void clickReadOnlyCheckbox() {
        driver.findElement(readOnlyCheckbox).click();
    }

    public boolean isReadOnlyCheckboxSelected() {
        return driver.findElement(readOnlyCheckbox).isSelected();
    }

    public void clickReadOnlyLabel() {
        driver.findElement(readOnlyLabel).click();
    }
    public void uploadFile(String filePath) {
	    File file = new File(filePath);
	    if (!file.exists()) {
	        throw new RuntimeException("File not found: " + filePath);
	    }

	    // Make hidden input visible (important)
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    js.executeScript("arguments[0].style.display='block';", fileInput);

	    fileInput.sendKeys(file.getAbsolutePath());
	}
	public boolean isFileUploaded(String expectedFileName) {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	    return wait.until(driver ->
	            uploadedFileContainer.getText().contains(expectedFileName)
	    );
	}
	public String generateText(int length) {
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length) {
            sb.append("A");
        }
        return sb.substring(0, length);
    }
}

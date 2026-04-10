package com.potentia.automation.pages;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AddCardSortOneLevelPage {
	private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    public AddCardSortOneLevelPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }
	 // Top-level tabs
    private final By activeTab = By.id("activeBatch");
    private final By plannedTab = By.id("plannedBatch");
    private final By inactiveTab = By.id("inactiveBatch");

    // Batch list
    private final By batchListContainer = By.cssSelector(".bach_info_list");
    private final By batchCards = By.cssSelector(".bach_info_list .list-item-container");
    private By addActivityBtn = By.xpath("//a[contains(@class, 'custom-btn') and contains(@href, 'addactivity')]");
	private By selectAcitivity = By.xpath("//label[span[text()='Card Sort']]");
	private By activityTitle = By.xpath("//*[@id = 'title']");
	private By activityDiscription = By.xpath("//*[@id = 'description']");
	private By activityInstructions = By.xpath("//div[@class='fr-element fr-view' and @contenteditable='true']");
    private By oneLevelBtn = By.id("pills-home-tab");
    private By twoLevelBtn = By.id("pills-profile-tab");
    private By defaultRadio = By.xpath("//input[@name='child_card_design' and @value='1']");
    private By customRadio = By.xpath("//input[@name='child_card_design' and @value='10']");
    private By grayColorRadio = By.xpath("//input[@name='child_card_design' and @value='2']");
    private By greenColorRadio = By.xpath("//input[@name='child_card_design' and @value='3']");
    private By magentaRadio = By.xpath("//input[@name='child_card_design' and @value='4']");
    private By yellowRadio = By.xpath("//input[@name='child_card_design' and @value='7']");
    private By pastelPinkRadio = By.xpath("//input[@name='child_card_design' and @value='8']");
    private By pastelGreenRadio = By.xpath("//input[@name='child_card_design' and @value='5']");
    private By purpleRadio = By.xpath("//input[@name='child_card_design' and @value='9']");
    private By brownRadio = By.xpath("//input[@name='child_card_design' and @value='6']");
    private By bgColorPicker = By.id("childCardCustomThemeBg");
    private By fontColorPicker = By.id("childCardCustomThemeFont");
    private By allCardDescriptionInputs = By.xpath("(//div[@id='cardItems']//input[@name='l1title[]'])");
    private By clickAddCardDescriptionButton = By.xpath("(//div[@id='cardItems']//button[@data-bs-original-title='Add'])[1]");
    private By clickDeleteCardDescriptionButton = By.xpath("(//div[@id='cardItems']//button[@data-bs-original-title='Delete'])[1]");
    private By cardsToBeChosen = By.id("no_levelitems");
    
    
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
	public void clickCardSortSaveAndContinue() {
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
	public void clickCardSortOneLevel() {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		// Locate the button
		WebElement myButton = wait.until(ExpectedConditions.presenceOfElementLocated(
		    By.id("pills-home-tab")));

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
		    By.id("pills-home-tab")));

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
	
	    public boolean isOneLevelSelectedByDefault() {
	    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
	        WebElement one = wait.until(ExpectedConditions.presenceOfElementLocated(oneLevelBtn));
	        String classes = one.getAttribute("class");
	        String aria = one.getAttribute("aria-selected");
	        boolean activeClass = classes != null && classes.contains("active");
	        boolean ariaSelected = "true".equalsIgnoreCase(aria);
	        return activeClass && ariaSelected;
	    }

	    // Optional: check that both buttons exist and are displayed
	    public boolean areButtonsDisplayed() {
	        return driver.findElement(oneLevelBtn).isDisplayed() && driver.findElement(twoLevelBtn).isDisplayed();
	    }
	    public boolean isDefaultRadioDisplayed() {
	        return driver.findElement(defaultRadio).isDisplayed();
	    }

	    public boolean isCustomRadioDisplayed() {
	        return driver.findElement(customRadio).isDisplayed();
	    }
	    public boolean isGrayColorRadioDisplayed() {
	        return driver.findElement(grayColorRadio).isDisplayed();
	    }

	    public boolean isGreenColorRadioRadioDisplayed() {
	        return driver.findElement(greenColorRadio).isDisplayed();
	    }
	    public boolean isMagentaColorRadioDisplayed() {
	        return driver.findElement(magentaRadio).isDisplayed();
	    }

	    public boolean isYellowRadioDisplayed() {
	        return driver.findElement(yellowRadio).isDisplayed();
	    }
	    public boolean isPastelPinkRadioDisplayed() {
	        return driver.findElement(pastelPinkRadio).isDisplayed();
	    }

	    public boolean isPastelGreenRadioDisplayed() {
	        return driver.findElement(pastelGreenRadio).isDisplayed();
	    }
	    public boolean isPurpleRadioDisplayed() {
	        return driver.findElement(purpleRadio).isDisplayed();
	    }

	    public boolean isBrownRadioDisplayed() {
	        return driver.findElement(brownRadio).isDisplayed();
	    }

	    public boolean isDefaultRadioEnabled() {
	        return driver.findElement(defaultRadio).isEnabled();
	    }

	    public boolean isCustomRadioEnabled() {
	        return driver.findElement(customRadio).isEnabled();
	    }
	    
	    public boolean isGrayColorRadioEnabled() {
	        return driver.findElement(grayColorRadio).isEnabled();
	    }
	    
	    public boolean isGreenColorRadioEnabled() {
	        return driver.findElement(greenColorRadio).isEnabled();
	    }
	    
	    public boolean isMagentaColorRadioEnabled() {
	        return driver.findElement(magentaRadio).isEnabled();
	    }
	    
	    public boolean isYellowColorRadioEnabled() {
	        return driver.findElement(yellowRadio).isEnabled();
	    }
	    
	    public boolean isPastelPinkColorRadioEnabled() {
	        return driver.findElement(pastelPinkRadio).isEnabled();
	    }
	    
	    public boolean isPastelGreenColorRadioEnabled() {
	        return driver.findElement(pastelGreenRadio).isEnabled();
	    }
	    
	    public boolean isPurpelColorRadioEnabled() {
	        return driver.findElement(purpleRadio).isEnabled();
	    }
	    
	    public boolean isBrownColorRadioEnabled() {
	        return driver.findElement(brownRadio).isEnabled();
	    }
	    
	    public void clickDefaultRadio() {
	        driver.findElement(defaultRadio).click();
	    }

	    public void clickCustomRadio() {
	        driver.findElement(customRadio).click();
	    }
	    
	    public void clickGrayColorRadio() {
	        driver.findElement(grayColorRadio).click();
	    }
	    
	    public void clickGreenRadio() {
	        driver.findElement(greenColorRadio).click();
	    }
	    
	    public void clickMagentaRadio() {
	        driver.findElement(magentaRadio).click();
	    }
	    
	    public void clickYellowRadio() {
	        driver.findElement(yellowRadio).click();
	    }
	    
	    public void clickPastelPinkRadio() {
	        driver.findElement(pastelPinkRadio).click();
	    }
	    
	    public void clickPastelGreenRadio() {
	        driver.findElement(pastelGreenRadio).click();
	    }
	    
	    public void clickPurpleRadio() {
	        driver.findElement(purpleRadio).click();
	    }
	    
	    public void clickBrownRadio() {
	        driver.findElement(brownRadio).click();
	    }

	    public boolean isDefaultSelected() {
	        return driver.findElement(defaultRadio).isSelected();
	    }

	    public boolean isCustomSelected() {
	        return driver.findElement(customRadio).isSelected();
	    }
	    
	    public boolean isGrayColorRadioSelected() {
	        return driver.findElement(grayColorRadio).isSelected();
	    }
	    
	    public boolean isGreenColorRadioSelected() {
	        return driver.findElement(greenColorRadio).isSelected();
	    }
	    
	    public boolean isMagentaColorRadioSelected() {
	        return driver.findElement(magentaRadio).isSelected();
	    }
	    
	    public boolean isYellowColorRadioSelected() {
	        return driver.findElement(yellowRadio).isSelected();
	    }
	    
	    public boolean isPastelPinkColorRadioSelected() {
	        return driver.findElement(pastelPinkRadio).isSelected();
	    }
	    
	    public boolean isPastelGreenColorRadioSelected() {
	        return driver.findElement(pastelGreenRadio).isSelected();
	    }
	    
	    public boolean isPurpelColorRadioSelected() {
	        return driver.findElement(purpleRadio).isEnabled();
	    }
	    
	    public boolean isBrownColorRadioSelected() {
	        return driver.findElement(brownRadio).isEnabled();
	    }

	    public boolean isBackgroundColorPickerVisible() {
	        return driver.findElement(bgColorPicker).isDisplayed();
	    }

	    public boolean isFontColorPickerVisible() {
	        return driver.findElement(fontColorPicker).isDisplayed();
	    }
	    public void enterTextInAllCardDescriptions(String baseText) {
	        List<WebElement> inputs = driver.findElements(allCardDescriptionInputs);
	        int index = 1;
	        for (WebElement input : inputs) {
	            input.clear();
	            input.sendKeys(baseText + " " + index);
	            index++;
	        }
	    }
	    public List<WebElement> getAllCardDescriptionFields() {
	        return driver.findElements(allCardDescriptionInputs);
	    }
	    public int getCardDescriptionFieldCount() {
	        return driver.findElements(allCardDescriptionInputs).size();
	    }
	    public void clickAddCardButton(int  count) {
	    	for (int i=1; i<= count; i++) {
		    	driver.findElement(clickAddCardDescriptionButton).click();
		    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
		    	wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(allCardDescriptionInputs, i + 1));
	    	}
	    }
	    public void clickDeleteCardButton(int count) {
	    	for (int i=1; i<= count; i++) {
	    		driver.findElement(clickDeleteCardDescriptionButton).click();
	    	}
	    }
	    public boolean isMinimumCardCountErrorMessageDisplayed() {
	        return driver.findElement(By.xpath("//*[contains(text(), 'Minimum two cards required')]")).isDisplayed();
		}
		public String getMinimumCardCountErrorMessageText() {
	        WebElement errorMessageElement = driver.findElement(By.xpath("//*[contains(text(), 'Minimum two cards required')]"));
	        return errorMessageElement.getText().trim();
		}
		public boolean isCardsToBeChosenEmptyErrorMessageDisplayed() {
	        return driver.findElement(By.xpath("//*[contains(text(), 'Please enter Cards to be Chosen')]")).isDisplayed();
		}
		public String getCardsToBeChosenEmptyErrorMessageText() {
	        WebElement errorMessageElement = driver.findElement(By.xpath("//*[contains(text(), 'Please enter Cards to be Chosen')]"));
	        return errorMessageElement.getText().trim();
		}
		public void enterCardsToBeChosen(String NoofCards) {
			driver.findElement(cardsToBeChosen).click();
			driver.findElement(cardsToBeChosen).sendKeys(NoofCards);
		}
		public boolean isCardsToBeChosenIsZeroErrorMessageDisplayed() {
	        return driver.findElement(By.xpath("//*[contains(text(), 'No of Card Assigned should not be less than 1.')]")).isDisplayed();
		}
		public String getCardsToBeChosenIsZeroErrorMessageText() {
	        WebElement errorMessageElement = driver.findElement(By.xpath("//*[contains(text(), 'No of Card Assigned should not be less than 1.')]"));
	        return errorMessageElement.getText().trim();
		}
		public boolean isCardsToBeChosenGreaterthenCardsErrorMessageDisplayed() {
	        return driver.findElement(By.xpath("//*[contains(text(), 'Cards to be chosen cannot exceed the number of cards created')]")).isDisplayed();
		}
		public String getCardsToBeChosenGreatThenCardsErrorMessageText() {
	        WebElement errorMessageElement = driver.findElement(By.xpath("//*[contains(text(), 'Cards to be chosen cannot exceed the number of cards created')]"));
	        return errorMessageElement.getText().trim();
		}
		public String generateText(int length) {
	        StringBuilder sb = new StringBuilder();
	        while (sb.length() < length) {
	            sb.append("A");
	        }
	        return sb.substring(0, length);
	    }
}

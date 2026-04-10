package com.potentia.automation.pages;


import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class MyBatchesPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    public MyBatchesPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.js = (JavascriptExecutor) driver;
    }

    // =========================
    // Page Header / Top Controls
    // =========================
    private final By pageHeading = By.xpath("//div[contains(@class,'activities')]//h1[normalize-space()='My Batches']");
    
    //Search field	
    private final By searchInput = By.cssSelector("input.search-batch");
    private final By visibleBatchCards = By.xpath("//div[contains(@class,'list-item-container') and not(contains(@style,'display: none'))]");

    //Sort field
    private final By sortDropdownButton = By.cssSelector(".sort-list");
    private final By sortDropdownOptions = By.cssSelector(".sort-list ul.dropdown-menu li a.dropdown-item");
    private final By clearSortIcon = By.cssSelector("a.clear-sort");
    private final By batchNameSortOption = By.xpath("//a[contains(@class,'dropdown-item') and normalize-space()='Batch Name']");
    
    // ==========================================
    // Filter Top controls
    // ==========================================
    private final By filterButton = By.cssSelector(".filter-list button.batch-filter");
    private final By clearFilterButton = By.xpath("//button[normalize-space()='Clear Filter'] | //a[normalize-space()='Clear Filter']");
    private final By filterChipTexts = By.xpath("//div[contains(@class,'filter-name')]//span | //div[contains(@class,'filter-name')]//a | //div[contains(@class,'filter-name')]");

    // ==========================================
    // Filter modal
    // ==========================================
    private final By filterModal = By.id("filterModal");
    private final By filterModalTitle = By.id("filterModalLabel");
    private final By filterCloseIcon = By.cssSelector("#filterModal .btn-close");
    private final By filterModalBody = By.cssSelector("#filterModal .modal-body");

    // ==========================================
    // Filter Accordions
    // ==========================================
    private final By clientsAccordionButton = By.cssSelector("button[data-bs-target='#collapseFilterBatchClient']");
    private final By programAccordionButton = By.cssSelector("button[data-bs-target='#collapseFilterBatchProgram']");

    private final By clientsAccordionPanel = By.id("collapseFilterBatchClient");
    private final By programAccordionPanel = By.id("collapseFilterBatchProgram");

    // ==========================================
    // Filter Footer buttons
    // ==========================================
    private final By applyFilterButton = By.xpath("//div[@id='filterModal']//a[normalize-space()='Apply']");
    private final By cancelFilterButton = By.xpath("//div[@id='filterModal']//a[normalize-space()='Cancel']");

    
    // new Batch button
    private final By newBatchButton = By.xpath("//button[normalize-space()='New Batch']");

    // =========================
    // Primary Tabs
    // =========================

    private final By activeTab = By.id("activeBatch");
    private final By plannedTab = By.id("plannedBatch");
    private final By inactiveTab = By.id("inactiveBatch");
    private final By archivedTab = By.id("archivedBatch");

    // =========================
    // Secondary Tabs
    // =========================
    private final By allSubTab = By.id("allActive");
    private final By openSubTab = By.id("openBatch");
    private final By expiredSubTab = By.id("expBatch");

    // =========================
    // Batch List / Cards
    // =========================
    private final By batchCards = By.xpath("//div[contains(@class,'list-item-container') and not(contains(@style,'display: none'))]");
    private final By batchTitles = By.xpath("//div[contains(@class,'list-item-container') and not(contains(@style,'display: none'))]//div[contains(@class,'list-item-title')]//h4");
    private final By clientNameInsideCard = By.xpath("//div[contains(@class,'list-item-container') and not(contains(@style,'display: none'))]//div[contains(@class,'list-item-title')]//p");
    private final By programTitleInsideCard = By.xpath("//div[contains(@class,'list-item-container') and not(contains(@style,'display: none'))]//div[contains(@class,'prog_info')]//h5");
    private final By programGenres = By.xpath("//div[contains(@class,'list-item-container') and not(contains(@style,'display: none'))]//div[contains(@class,'prog_info')]//p");
    private final By startDates = By.xpath("//div[contains(@class,'list-item-container') and not(contains(@style,'display: none'))]//div[contains(@class,'bach_date')]//p[1]");
    private final By closeDates = By.xpath("//div[contains(@class,'list-item-container') and not(contains(@style,'display: none'))]//div[contains(@class,'bach_date')]//p[2]");

    // =========================
    // Action Icons
    // =========================
    private final By visibleEditIcons = By.xpath("//div[contains(@class,'list-item-container') and not(contains(@style,'display: none'))]//a[contains(@class,'edit-batch') and not(contains(@class,'d-none'))]");
    private final By visibleQrIcons = By.xpath("//div[contains(@class,'list-item-container') and not(contains(@style,'display: none'))]//a[contains(@class,'view-batch-code') and not(contains(@class,'d-none'))]");
    private final By visibleCloseIcons = By.xpath("//div[contains(@class,'list-item-container') and not(contains(@style,'display: none'))]//a[contains(@class,'close_batch') and not(contains(@class,'d-none'))]");

    // =========================
    // Layout
    // =========================
    private final By topContainer = By.cssSelector("div.container-wrap.dashboard-container-wrap");
    private final By tabMainSection = By.cssSelector("div.tab-main");
    
    // ===== Page Locators =====
    private final By batchListContainer = By.cssSelector("div.bach_info_list");
    private final By notFoundMessage = By.cssSelector("p.filter-not-found");
    private final By successMessage = By.xpath("//*[contains(text(),'Batch inactivated successfully')]");
    private final By batchActivesuccessMessage = By.xpath("//div[contains(@class,'toast-message') and contains(text(),'Batch activated successfully')]");
    private final By batchDeletesuccessMessage = By.xpath("//*[contains(text(),'Batch - batch - for save continue deleted successfully')]");
    
    // ==batch Close== 
    private final By closeBatchPopup = By.cssSelector("div.swal-modal");
    private final By closeBatchPopupText = By.cssSelector("div.swal-text");
    private final By closeBatchConfirmButton = By.cssSelector("button.swal-button--confirm");
    private final By closeBatchCancelButton = By.cssSelector("button.swal-button--cancel");
    
    // QR icon inside batch card
    private final By qrIconInBatchCard = By.xpath(".//a[contains(@class,'view-batch-code') and not(contains(@class,'d-none'))]");

    // QR popup locators
    private final By qrPopup = By.xpath("//div[contains(@class,'modal-content')][.//h5[normalize-space()='QR code for participants to self register']]");
    private final By qrPopupTitle = By.xpath("//h5[normalize-space()='QR code for participants to self register']");
    private final By qrHeading = By.id("qrHeading");
    private final By qrDescription = By.xpath("//div[contains(@class,'modal-body')]//p[contains(text(),'Share this QR code or a Self Registration link')]");
    private final By qrImage = By.cssSelector("#qrcode img");
    private final By qrContainer = By.id("qrcode");
    private final By copyLinkText = By.cssSelector("a.publish-url-a");
    private final By hiddenRegistrationUrl = By.id("copyText");
    private final By closePopupButton = By.xpath("//div[contains(@class,'modal-footer')]//button[normalize-space()='Close']");
    private final By popupCloseIcon = By.cssSelector("button.btn-close");

    // =========================
    // Common Methods
    // =========================
    private WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    
    // =========================
    // Page Validations
    // =========================
    public boolean isMyBatchesPageDisplayed() {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("preloader")));
        try {
            return waitForVisible(pageHeading).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getPageHeadingText() {
        return waitForVisible(pageHeading).getText().trim();
    }

    public boolean isSearchFieldDisplayed() {
        try {
            return waitForVisible(searchInput).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isNewBatchButtonDisplayed() {
        try {
            return waitForVisible(newBatchButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // =========================
    // Tabs Validation
    // =========================
    public boolean arePrimaryTabsDisplayed() {
        try {
            return waitForVisible(activeTab).isDisplayed()
                    && waitForVisible(plannedTab).isDisplayed()
                    && waitForVisible(inactiveTab).isDisplayed()
                    && waitForVisible(archivedTab).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean areSecondaryTabsDisplayed() {
        try {
            return waitForVisible(allSubTab).isDisplayed()
                    && waitForVisible(openSubTab).isDisplayed()
                    && waitForVisible(expiredSubTab).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickActiveTab() {
        waitForClickable(activeTab).click();
    }

    public void clickPlannedTab() {
        waitForClickable(plannedTab).click();
    }

    public void clickInactiveTab() {
        waitForClickable(inactiveTab).click();
    }

    public void clickArchivedTab() {
        waitForClickable(archivedTab).click();
    }

    public void clickAllSubTab() {
        waitForClickable(allSubTab).click();
    }

    public void clickOpenSubTab() {
        waitForClickable(openSubTab).click();
    }

    public void clickExpiredSubTab() {
        waitForClickable(expiredSubTab).click();
    }

    // =========================
    // Search
    // =========================
    public void searchBatch(String batchName) {
        WebElement search = waitForVisible(searchInput);
        search.clear();
        search.sendKeys(batchName);
        search.sendKeys(Keys.ENTER);
    }

    public String getSearchFieldValue() {
        return waitForVisible(searchInput).getAttribute("value").trim();
    }
    
    public void clearSearchBox() {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.DELETE);

        wait.until(driver -> element.getAttribute("value").trim().isEmpty());
    }
    
    public boolean isSearchBoxEmpty() {
        return getSearchFieldValue().isEmpty();
    }

    public int getVisibleBatchCount() {
        return driver.findElements(visibleBatchCards).size();
    }

    public boolean areBatchesDisplayedAfterClear() {
        return getVisibleBatchCount() > 0;
    }

    // =========================
    // Sort
    // =========================
    public boolean isSortDropdownVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(sortDropdownButton)).isDisplayed();
    }

    public boolean isSortDropdownEnabled() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(sortDropdownButton)).isEnabled();
    }

    public void openSortDropdown() {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(sortDropdownButton));
        scrollIntoView(dropdown);
        dropdown.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(batchNameSortOption));
    }

    public List<String> getSortOptionTexts() {
        openSortDropdown();
        List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(sortDropdownOptions));
        return options.stream()
                .map(e -> e.getText().trim())
                .filter(text -> !text.isEmpty())
                .collect(Collectors.toList());
    }

    public void selectSortByBatchName() {
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(batchNameSortOption));
        option.click();
        waitForPageToSettle();
    }

    public boolean isClearSortVisible() {
        try {
            WebElement clear = driver.findElement(clearSortIcon);
            return clear.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickClearSortIfVisible() {
        if (isClearSortVisible()) {
            WebElement clear = wait.until(ExpectedConditions.elementToBeClickable(clearSortIcon));
            clear.click();
            waitForPageToSettle();
        }
    }

    public void refreshPage() {
        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOfElementLocated(sortDropdownButton));
        waitForPageToSettle();
    }
    
    // ---------- Batch name collection ----------
    public List<String> getVisibleBatchNames() {
        List<WebElement> cards = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(visibleBatchCards)
        );

        List<String> batchNames = new ArrayList<>();

        for (WebElement card : cards) {
            try {
                if (!card.isDisplayed()) {
                    continue;
                }

                WebElement batchTitle = card.findElement(By.xpath(".//div[@class='list-item-title']//h4"));
                String name = batchTitle.getText()
                        .replace('\u00A0', ' ')
                        .replaceAll("\\s+", " ")
                        .trim();

                if (!name.isEmpty()) {
                    batchNames.add(name);
                }
            } catch (Exception e) {
                // skip bad element
            }
        }

        System.out.println("Visible batch names: " + batchNames);
        return batchNames;
    }public boolean isBatchNamesSortedAscending() {
        List<String> actual = getVisibleBatchNames().stream()
                .map(name -> name.replace('\u00A0', ' '))
                .map(name -> name.replaceAll("\\s+", " "))
                .map(name -> name.trim().toLowerCase(Locale.ROOT))
                .collect(Collectors.toList());

        List<String> expected = new ArrayList<>(actual);
        expected.sort(String.CASE_INSENSITIVE_ORDER);

        System.out.println("========== Inactive Tab Sort Debug ==========");
        System.out.println("Actual Order   : " + actual);
        System.out.println("Expected Order : " + expected);
        System.out.println("Is Sorted      : " + actual.equals(expected));
        System.out.println("=============================================");

        return actual.equals(expected);
    }

    public List<String> getNormalizedBatchNames() {
        return getVisibleBatchNames().stream()
                .map(name -> name.toLowerCase(Locale.ROOT).trim())
                .collect(Collectors.toList());
    }


    private void scrollIntoView(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    private void waitForPageToSettle() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // =========================
    // Filter
    // =========================
    public boolean isFilterButtonVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(filterButton)).isDisplayed();
    }

    public boolean isFilterButtonEnabled() {
        return wait.until(ExpectedConditions.elementToBeClickable(filterButton)).isEnabled();
    }

    public void openFilterModal() {
        WebElement filter = wait.until(ExpectedConditions.elementToBeClickable(filterButton));
        scrollIntoView(filter);
        filter.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(filterModal));
    }

    public boolean isFilterModalDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(filterModal)).isDisplayed();
    }

    public String getFilterModalTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(filterModalTitle)).getText().trim();
    }

    public void closeFilterModalUsingCloseIcon() {
        WebElement close = wait.until(ExpectedConditions.elementToBeClickable(filterCloseIcon));
        close.click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(filterModal));
    }

    public void closeFilterModalUsingCancel() {
        WebElement cancel = wait.until(ExpectedConditions.elementToBeClickable(cancelFilterButton));
        cancel.click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(filterModal));
    }
    // ==========================================
    // Accordion state methods
    // ==========================================
    public boolean isClientsAccordionExpandedByDefault() {
        WebElement panel = wait.until(ExpectedConditions.visibilityOfElementLocated(clientsAccordionPanel));
        String classValue = panel.getAttribute("class");
        return classValue != null && classValue.contains("show");
    }

    public boolean isProgramAccordionCollapsedByDefault() {
        WebElement panel = wait.until(ExpectedConditions.presenceOfElementLocated(programAccordionPanel));
        String classValue = panel.getAttribute("class");
        return classValue != null && !classValue.contains("show");
    }

    public boolean isClientsAccordionExpanded() {
        WebElement panel = wait.until(ExpectedConditions.presenceOfElementLocated(clientsAccordionPanel));
        String classValue = panel.getAttribute("class");
        return classValue != null && classValue.contains("show");
    }

    public boolean isProgramAccordionExpanded() {
        WebElement panel = wait.until(ExpectedConditions.presenceOfElementLocated(programAccordionPanel));
        String classValue = panel.getAttribute("class");
        return classValue != null && classValue.contains("show");
    }

    public void expandClientsAccordionIfNeeded() {
        WebElement panel = wait.until(ExpectedConditions.presenceOfElementLocated(clientsAccordionPanel));
        String classValue = panel.getAttribute("class");

        if (classValue == null || !classValue.contains("show")) {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(clientsAccordionButton));
            button.click();
            wait.until(driver -> isClientsAccordionExpanded());
        }
    }

    public void scrollToProgramTitleAccordion() {
        WebElement modalBody = wait.until(ExpectedConditions.visibilityOfElementLocated(filterModalBody));
        WebElement programButton = wait.until(ExpectedConditions.presenceOfElementLocated(programAccordionButton));

        js.executeScript(
                "arguments[0].scrollTop = arguments[1].offsetTop - arguments[0].offsetTop;",
                modalBody, programButton
        );
        waitForUiToSettle();
    }

    public void expandProgramAccordionIfNeeded() {
        scrollToProgramTitleAccordion();

        WebElement panel = wait.until(ExpectedConditions.presenceOfElementLocated(programAccordionPanel));
        String classValue = panel.getAttribute("class");

        if (classValue == null || !classValue.contains("show")) {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(programAccordionButton));
            scrollIntoViewInsideModal(button);
            button.click();
            wait.until(driver -> isProgramAccordionExpanded());
        }
    }

    // ==========================================
    // Dynamic filter checkbox locators
    // ==========================================
    private By clientCheckboxByTitle(String clientName) {
        return By.xpath("//input[@name='filterClient[]' and @data-title=\"" + clientName + "\"]");
    }

    private By programCheckboxByTitle(String programTitle) {
        return By.xpath("//input[@name='filterProgram[]' and @data-title=\"" + programTitle + "\"]");
    }

    public void selectClientFilter(String clientName) {
        expandClientsAccordionIfNeeded();

        WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(clientCheckboxByTitle(clientName)));
        scrollIntoViewInsideModal(checkbox);

        if (!checkbox.isSelected()) {
            js.executeScript("arguments[0].click();", checkbox);
        }
    }

    public void selectProgramFilter(String programTitle) {
        expandProgramAccordionIfNeeded();

        WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(programCheckboxByTitle(programTitle)));
        scrollIntoViewInsideModal(checkbox);

        if (!checkbox.isSelected()) {
            js.executeScript("arguments[0].click();", checkbox);
        }
    }

    public void clickApplyFilter() {
        WebElement apply = wait.until(ExpectedConditions.elementToBeClickable(applyFilterButton));
        apply.click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(filterModal));
        waitForUiToSettle();
    }

    public void applyClientFilter(String clientName) {
        selectClientFilter(clientName);
        clickApplyFilter();
    }

    public void applyProgramFilter(String programTitle) {
    	openFilterModal();
    	scrollToProgramTitleAccordion();
    	expandProgramAccordionIfNeeded();
        selectProgramFilter(programTitle);
        clickApplyFilter();
    }

    public void applyClientAndProgramFilter(String clientName, String programTitle) {
    	openFilterModal();
        selectClientFilter(clientName);
        scrollToProgramTitleAccordion();
        expandProgramAccordionIfNeeded();
        selectProgramFilter(programTitle);
        clickApplyFilter();
    }

    // ==========================================
    // Clear filter and chip methods
    // ==========================================
    public boolean isClearFilterVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(clearFilterButton)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickClearFilter() {
        WebElement clear = wait.until(ExpectedConditions.elementToBeClickable(clearFilterButton));
        clear.click();
        waitForUiToSettle();
    }

    public List<String> getAppliedFilterTexts() {
        List<WebElement> elements = driver.findElements(filterChipTexts);

        return elements.stream()
                .filter(WebElement::isDisplayed)
                .map(WebElement::getText)
                .map(this::normalizeText)
                .filter(text -> !text.isEmpty())
                .collect(Collectors.toList());
    }

    public boolean isAppliedFilterTextPresent(String expectedText) {
        String normalizedExpected = normalizeText(expectedText).toLowerCase(Locale.ROOT);

        return getAppliedFilterTexts().stream()
                .map(text -> text.toLowerCase(Locale.ROOT))
                .anyMatch(text -> text.contains(normalizedExpected));
    }

    // ==========================================
    // Batch card data methods
    // ==========================================
    public List<WebElement> getVisibleBatchCards() {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(visibleBatchCards))
                .stream()
                .filter(WebElement::isDisplayed)
                .collect(Collectors.toList());
    }

    public List<String> getVisibleClientNames() {
        List<String> clients = new ArrayList<>();

        for (WebElement card : getVisibleBatchCards()) {
            try {
                String client = normalizeText(card.findElement(clientNameInsideCard).getText());
                if (!client.isEmpty()) {
                    clients.add(client);
                }
            } catch (StaleElementReferenceException e) {
                // skip stale row
            } catch (Exception e) {
                // skip bad row
            }
        }
        return clients;
    }

    public List<String> getVisibleProgramTitles() {
        List<String> programs = new ArrayList<>();

        for (WebElement card : getVisibleBatchCards()) {
            try {
                String program = normalizeText(card.findElement(programTitleInsideCard).getText());
                if (!program.isEmpty()) {
                    programs.add(program);
                }
            } catch (StaleElementReferenceException e) {
                // skip stale row
            } catch (Exception e) {
                // skip bad row
            }
        }
        return programs;
    }

    public boolean areAllVisibleBatchesForClient(String expectedClientName) {
        String expected = normalizeText(expectedClientName).toLowerCase(Locale.ROOT);
        List<String> clients = getVisibleClientNames();

        if (clients.isEmpty()) {
            return true;
        }

        return clients.stream()
                .map(client -> client.toLowerCase(Locale.ROOT))
                .allMatch(client -> client.equals(expected));
    }

    public boolean areAllVisibleBatchesForProgram(String expectedProgramTitle) {
        String expected = normalizeText(expectedProgramTitle).toLowerCase(Locale.ROOT);
        List<String> programs = getVisibleProgramTitles();

        if (programs.isEmpty()) {
            return true;
        }

        return programs.stream()
                .map(program -> program.toLowerCase(Locale.ROOT))
                .allMatch(program -> program.equals(expected));
    }

 // ==========================================
    // Utility methods
    // ==========================================
    private String normalizeText(String text) {
        if (text == null) {
            return "";
        }
        return text.replace('\u00A0', ' ')
                .replaceAll("\\s+", " ")
                .trim();
    }

    private void scrollIntoViewInsideModal(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        waitForUiToSettle();
    }

    private void waitForUiToSettle() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    
    
    

    public void clickNewBatchButton() {
        waitForClickable(newBatchButton).click();
    }

    // =========================
    // Batch Cards
    // =========================
    public int getVisibleBatchCardCount() {
        return driver.findElements(batchCards).size();
    }

    public boolean areBatchCardsDisplayed() {
        return getVisibleBatchCardCount() > 0;
    }


    public boolean areBatchTitlesNonEmpty() {
        List<String> titles = getVisibleBatchNames();
        return !titles.isEmpty() && titles.stream().allMatch(title -> !title.isBlank());
    }

    public boolean areBatchClientsNonEmpty() {
        List<WebElement> elements = driver.findElements(clientNameInsideCard);
        if (elements.isEmpty()) return false;

        for (WebElement element : elements) {
            if (element.getText().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean areProgramTitlesNonEmpty() {
        List<WebElement> elements = driver.findElements(programTitleInsideCard);
        if (elements.isEmpty()) return false;

        for (WebElement element : elements) {
            if (element.getText().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean areProgramGenresNonEmpty() {
        List<WebElement> elements = driver.findElements(programGenres);
        if (elements.isEmpty()) return false;

        for (WebElement element : elements) {
            if (element.getText().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean areStartDatesDisplayed() {
        List<WebElement> elements = driver.findElements(startDates);
        if (elements.isEmpty()) return false;

        for (WebElement element : elements) {
            if (element.getText().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean areCloseDatesDisplayed() {
        List<WebElement> elements = driver.findElements(closeDates);
        if (elements.isEmpty()) return false;

        for (WebElement element : elements) {
            if (element.getText().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean doesEachVisibleBatchCardContainMandatoryDetails() {
        return areBatchTitlesNonEmpty()
                && areBatchClientsNonEmpty()
                && areProgramTitlesNonEmpty()
                && areProgramGenresNonEmpty()
                && areStartDatesDisplayed()
                && areCloseDatesDisplayed();
    }

    // =========================
    // Action Icons
    // =========================
    public boolean areEditIconsDisplayed() {
        return !driver.findElements(visibleEditIcons).isEmpty();
    }

    public boolean areQrIconsDisplayed() {
        return !driver.findElements(visibleQrIcons).isEmpty();
    }

    public boolean areCloseIconsDisplayed() {
        return !driver.findElements(visibleCloseIcons).isEmpty();
    }

    public void clickFirstVisibleEditIcon() {
        List<WebElement> icons = driver.findElements(visibleEditIcons);
        if (!icons.isEmpty()) {
            icons.get(0).click();
        }
    }

    public void clickFirstVisibleQrIcon() {
        List<WebElement> icons = driver.findElements(visibleQrIcons);
        if (!icons.isEmpty()) {
            icons.get(0).click();
        }
    }

    public void clickFirstVisibleCloseIcon() {
        List<WebElement> icons = driver.findElements(visibleCloseIcons);
        if (!icons.isEmpty()) {
            icons.get(0).click();
        }
    }

    // =========================
    // UI Layout
    // =========================
    public boolean isPageLoadedWithoutUiBreak() {
        try {
            WebElement container = waitForVisible(topContainer);
            WebElement heading = waitForVisible(pageHeading);
            WebElement tabs = waitForVisible(tabMainSection);
            WebElement batchInfo = waitForVisible(batchListContainer);

            return container.isDisplayed()
                    && heading.isDisplayed()
                    && tabs.isDisplayed()
                    && batchInfo.isDisplayed()
                    && container.getSize().getHeight() > 0
                    && heading.getSize().getHeight() > 0
                    && tabs.getSize().getHeight() > 0
                    && batchInfo.getSize().getHeight() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCurrentUrlContains(String value) {
        return driver.getCurrentUrl().contains(value);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    public enum BatchCloseStatus {
        CLOSED,
        NOT_FOUND,
        CLOSE_ICON_NOT_AVAILABLE
    }

    public void waitForPageToLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeading));
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        wait.until(ExpectedConditions.visibilityOfElementLocated(batchListContainer));
    }

    public BatchCloseStatus closeBatchByName(String batchName) {
        validateBatchName(batchName);
        waitForPageToLoad();

        List<TabDetails> tabFlow = getTabFlow();

        for (TabDetails tab : tabFlow) {
            openTab(tab.getMainTab());

            if (tab.getSecondaryTab() != null) {
                openTab(tab.getSecondaryTab());
            }

            searchBatchByName(batchName);
            waitForBatchResultsToLoad();

            Optional<WebElement> matchingBatchCard = findMatchingBatchCard(batchName);

            if (matchingBatchCard.isPresent()) {
                return clickCloseIcon(matchingBatchCard.get());
            }
        }

        return BatchCloseStatus.NOT_FOUND;
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            return message.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getSuccessMessageText() {
        try {
            WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            return message.getText().trim();
        } catch (TimeoutException e) {
            return "";
        }
    }

    private List<TabDetails> getTabFlow() {
        List<TabDetails> tabs = new ArrayList<>();
        tabs.add(new TabDetails(activeTab, allSubTab));
        tabs.add(new TabDetails(activeTab, openSubTab));
        tabs.add(new TabDetails(activeTab, expiredSubTab));
        tabs.add(new TabDetails(plannedTab, null));
        return tabs;
    }

    private void openTab(By tabLocator) {
        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(tabLocator));
        scrollToElement(tab);
        clickElement(tab);
        hardWait(800);
    }

    private void searchBatchByName(String batchName) {
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        scrollToElement(searchBox);

        searchBox.click();
        searchBox.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        searchBox.sendKeys(Keys.DELETE);
        searchBox.clear();
        searchBox.sendKeys(batchName);
        searchBox.sendKeys(Keys.TAB);

        hardWait(1000);
    }

    private void waitForBatchResultsToLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(batchListContainer));
        wait.until((ExpectedCondition<Boolean>) driver -> driver.findElements(batchCards) != null);
        hardWait(700);
    }

    private Optional<WebElement> findMatchingBatchCard(String batchName) {
        List<WebElement> allCards = driver.findElements(batchCards);
        String expectedBatchName = normalizeText(batchName).toLowerCase(Locale.ENGLISH);

        System.out.println("Expected batch name: " + expectedBatchName);
        System.out.println("Total cards found: " + allCards.size());

        for (WebElement card : allCards) {
            try {
                if (!card.isDisplayed()) {
                    continue;
                }

                scrollToElement(card);

                WebElement titleElement = card.findElement(
                    By.xpath(".//div[contains(@class,'list-item-title')]//h4")
                );

                String actualBatchName = normalizeText(titleElement.getText()).toLowerCase(Locale.ENGLISH);
                System.out.println("Visible batch title: " + actualBatchName);

                if (actualBatchName.equals(expectedBatchName)) {
                    System.out.println("Matched batch found: " + actualBatchName);
                    return Optional.of(card);
                }

            } catch (StaleElementReferenceException e) {
                System.out.println("Stale element while reading card");
            } catch (Exception e) {
                System.out.println("Error while reading card: " + e.getMessage());
            }
        }

        System.out.println("No matching batch found in current tab");
        return Optional.empty();
    }

    private BatchCloseStatus clickCloseIcon(WebElement batchCard) {
        scrollToElement(batchCard);

        List<WebElement> closeIcons = batchCard.findElements(
            By.xpath(".//a[contains(@class,'close_batch') and not(contains(@class,'d-none'))]")
        );

        if (closeIcons.isEmpty()) {
            System.out.println("Batch found but close icon is not available.");
            return BatchCloseStatus.CLOSE_ICON_NOT_AVAILABLE;
        }

        try {
            WebElement closeIcon = closeIcons.get(0);

            System.out.println("Close icon displayed: " + closeIcon.isDisplayed());
            System.out.println("Close icon enabled: " + closeIcon.isEnabled());

            scrollToElement(closeIcon);
            clickElement(closeIcon);

            boolean isPopupHandled = confirmCloseBatchPopup();
            if (!isPopupHandled) {
                System.out.println("Close batch popup was not handled.");
                return BatchCloseStatus.CLOSE_ICON_NOT_AVAILABLE;
            }

            hardWait(1500);
            return BatchCloseStatus.CLOSED;

        } catch (Exception e) {
            System.out.println("Exception while clicking close icon: " + e.getMessage());
            return BatchCloseStatus.CLOSE_ICON_NOT_AVAILABLE;
        }
    }

    private boolean confirmCloseBatchPopup() {
        try {
            WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(closeBatchPopup));
            scrollToElement(popup);

            String popupMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(closeBatchPopupText))
                                      .getText().trim();
            System.out.println("Close batch popup message: " + popupMessage);

            WebElement okButton = wait.until(ExpectedConditions.elementToBeClickable(closeBatchConfirmButton));
            scrollToElement(okButton);
            clickElement(okButton);

            wait.until(ExpectedConditions.invisibilityOfElementLocated(closeBatchPopup));
            return true;

        } catch (Exception e) {
            System.out.println("Failed to handle close batch popup: " + e.getMessage());
            return false;
        }
    }

    private void clickElement(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", element);
        }
    }

    private void scrollToElement(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", element);
        hardWait(300);
    }

    private void validateBatchName(String batchName) {
        if (batchName == null || batchName.trim().isEmpty()) {
            throw new IllegalArgumentException("Batch name must not be null or empty");
        }
    }

    private void hardWait(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static class TabDetails {
        private final By mainTab;
        private final By secondaryTab;

        public TabDetails(By mainTab, By secondaryTab) {
            this.mainTab = mainTab;
            this.secondaryTab = secondaryTab;
        }

        public By getMainTab() {
            return mainTab;
        }

        public By getSecondaryTab() {
            return secondaryTab;
        }
    }
    
    // Delete batch 
    
    public enum BatchDeleteStatus {
        DELETED,
        NOT_FOUND,
        DELETE_ICON_NOT_AVAILABLE
    }
    public BatchDeleteStatus deleteBatchByName(String batchName) {
        validateBatchName(batchName);
        waitForPageToLoad();

        List<TabDetails> tabFlow = getTabFlow();

        for (TabDetails tab : tabFlow) {
            openTab(tab.getMainTab());

            if (tab.getSecondaryTab() != null) {
                openTab(tab.getSecondaryTab());
            }

            searchBatchByName(batchName);
            waitForBatchResultsToLoad();

            Optional<WebElement> batchCard = findMatchingBatchCard(batchName);

            if (batchCard.isPresent()) {
                return clickDeleteIcon(batchCard.get());
            }
        }

        return BatchDeleteStatus.NOT_FOUND;
    }
    private BatchDeleteStatus clickDeleteIcon(WebElement batchCard) {
        scrollToElement(batchCard);

        List<WebElement> deleteIcons = batchCard.findElements(
            By.xpath(".//a[contains(@class,'delete-batch')]")
        );

        if (deleteIcons.isEmpty()) {
            System.out.println("Batch found but delete icon is not available.");
            return BatchDeleteStatus.DELETE_ICON_NOT_AVAILABLE;
        }

        try {
            WebElement deleteIcon = deleteIcons.get(0);

            System.out.println("Delete icon displayed: " + deleteIcon.isDisplayed());
            System.out.println("Delete icon enabled: " + deleteIcon.isEnabled());

            scrollToElement(deleteIcon);
            clickElement(deleteIcon);

            boolean isPopupHandled = confirmDeleteBatchPopup();
            if (!isPopupHandled) {
                System.out.println("Delete confirmation popup not handled.");
                return BatchDeleteStatus.DELETE_ICON_NOT_AVAILABLE;
            }

            hardWait(1500);
            return BatchDeleteStatus.DELETED;

        } catch (Exception e) {
            System.out.println("Exception while clicking delete icon: " + e.getMessage());
            return BatchDeleteStatus.DELETE_ICON_NOT_AVAILABLE;
        }
    }
    private boolean confirmDeleteBatchPopup() {
        try {
            WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(closeBatchPopup));
            scrollToElement(popup);

            String message = wait.until(ExpectedConditions.visibilityOfElementLocated(closeBatchPopupText)).getText();
            System.out.println("Delete popup message: " + message);

            WebElement okButton = wait.until(ExpectedConditions.elementToBeClickable(closeBatchConfirmButton));
            scrollToElement(okButton);
            clickElement(okButton);

            wait.until(ExpectedConditions.invisibilityOfElementLocated(closeBatchPopup));
            return true;

        } catch (Exception e) {
            System.out.println("Failed to handle delete popup: " + e.getMessage());
            return false;
        }
    }
    public boolean isBatchDeleteSuccessMessageDisplayed() {
        try {
            WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(batchDeletesuccessMessage));
            return message.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getBatchDeleteSuccessMessageText() {
        try {
            WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(batchDeletesuccessMessage));
            return message.getText().trim();
        } catch (TimeoutException e) {
            return "";
        }
    }
    
    // Batch Active
    
    public enum BatchActivateStatus {
        ACTIVATED,
        NOT_FOUND,
        ACTIVATE_ICON_NOT_AVAILABLE
    }
    public BatchActivateStatus activateBatchByName(String batchName) {
        validateBatchName(batchName);
        waitForPageToLoad();

        List<TabDetails> tabFlow = getinactiveTabFlow();

        for (TabDetails tab : tabFlow) {
            openTab(tab.getMainTab());

            if (tab.getSecondaryTab() != null) {
                openTab(tab.getSecondaryTab());
            }

            searchBatchByName(batchName);
            waitForBatchResultsToLoad();

            Optional<WebElement> batchCard = findMatchingBatchCard(batchName);

            if (batchCard.isPresent()) {
                return clickActivateIcon(batchCard.get());
            }
        }

        return BatchActivateStatus.NOT_FOUND;
    }
    private BatchActivateStatus clickActivateIcon(WebElement batchCard) {
        scrollToElement(batchCard);

        List<WebElement> activateIcons = batchCard.findElements(
            By.xpath(".//a[contains(@class,'activate_batch')]")
        );

        if (activateIcons.isEmpty()) {
            System.out.println("Batch found but activate icon is not available.");
            return BatchActivateStatus.ACTIVATE_ICON_NOT_AVAILABLE;
        }

        try {
            WebElement activateIcon = activateIcons.get(0);

            System.out.println("Activate icon displayed: " + activateIcon.isDisplayed());
            System.out.println("Activate icon enabled: " + activateIcon.isEnabled());

            scrollToElement(activateIcon);
            clickElement(activateIcon);

            boolean isPopupHandled = confirmActivateBatchPopup();
            if (!isPopupHandled) {
                System.out.println("Activate popup not handled.");
                return BatchActivateStatus.ACTIVATE_ICON_NOT_AVAILABLE;
            }

            hardWait(1500);
            return BatchActivateStatus.ACTIVATED;

        } catch (Exception e) {
            System.out.println("Exception while clicking activate icon: " + e.getMessage());
            return BatchActivateStatus.ACTIVATE_ICON_NOT_AVAILABLE;
        }
    }
    private boolean confirmActivateBatchPopup() {
        try {
            WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(closeBatchPopup));
            scrollToElement(popup);

            String message = wait.until(ExpectedConditions.visibilityOfElementLocated(closeBatchPopupText)).getText();
            System.out.println("Activate popup message: " + message);

            WebElement okButton = wait.until(ExpectedConditions.elementToBeClickable(closeBatchConfirmButton));
            scrollToElement(okButton);
            clickElement(okButton);

            wait.until(ExpectedConditions.invisibilityOfElementLocated(closeBatchPopup));
            return true;

        } catch (Exception e) {
            System.out.println("Failed to handle activate popup: " + e.getMessage());
            return false;
        }
    }
    
    public boolean isBatchActiveSuccessMessageDisplayed() {
        try {
            WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(batchActivesuccessMessage));
            return message.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getBatchActiveSuccessMessageText() {
        try {
            WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(batchActivesuccessMessage));
            return message.getText().trim();
        } catch (TimeoutException e) {
            return "";
        }
    }
    private List<TabDetails> getinactiveTabFlow() {
        List<TabDetails> tabs = new ArrayList<>();
        tabs.add(new TabDetails(activeTab, allSubTab));
        tabs.add(new TabDetails(activeTab, openSubTab));
        tabs.add(new TabDetails(activeTab, expiredSubTab));
        tabs.add(new TabDetails(plannedTab, null));
        tabs.add(new TabDetails(inactiveTab, null));
        return tabs;
    }
    
    //Pax self register
    public enum BatchQrStatus {
        QR_POPUP_OPENED,
        NOT_FOUND,
        QR_ICON_NOT_AVAILABLE,
        QR_POPUP_NOT_OPENED
    }

    public BatchQrStatus openPaxSelfRegisterQrByBatchName(String batchName) {
        validateBatchName(batchName);
        waitForPageToLoad();

        List<TabDetails> tabFlow = getTabFlow();

        for (TabDetails tab : tabFlow) {
            openTab(tab.getMainTab());

            if (tab.getSecondaryTab() != null) {
                openTab(tab.getSecondaryTab());
            }

            searchBatchByName(batchName);
            waitForBatchResultsToLoad();

            Optional<WebElement> matchingBatchCard = findMatchingBatchCard(batchName);

            if (matchingBatchCard.isPresent()) {
                return clickPaxSelfRegisterIcon(matchingBatchCard.get());
            }
        }

        return BatchQrStatus.NOT_FOUND;
    }

    public boolean isQrPopupDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(qrPopup)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getQrPopupTitleText() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(qrPopupTitle)).getText().trim();
        } catch (TimeoutException e) {
            return "";
        }
    }

    public String getQrHeadingText() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(qrHeading)).getText().trim();
        } catch (TimeoutException e) {
            return "";
        }
    }

    public boolean isQrDescriptionDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(qrDescription)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isQrImageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(qrImage)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isCopyLinkTextDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(copyLinkText)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getRegistrationUrlFromQrTitle() {
        try {
            WebElement qrBox = wait.until(ExpectedConditions.visibilityOfElementLocated(qrContainer));
            String titleValue = qrBox.getAttribute("title");
            return titleValue == null ? "" : titleValue.trim();
        } catch (Exception e) {
            return "";
        }
    }

    public String getRegistrationUrlFromHiddenInput() {
        try {
            WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(hiddenRegistrationUrl));
            String value = input.getAttribute("value");
            return value == null ? "" : value.trim();
        } catch (Exception e) {
            return "";
        }
    }

    public void closeQrPopup() {
        try {
            WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(closePopupButton));
            scrollToElement(closeBtn);
            clickElement(closeBtn);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(qrPopup));
        } catch (Exception e) {
            WebElement closeIcon = wait.until(ExpectedConditions.elementToBeClickable(popupCloseIcon));
            clickElement(closeIcon);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(qrPopup));
        }
    }

    // ===== Internal Methods =====


    private BatchQrStatus clickPaxSelfRegisterIcon(WebElement batchCard) {
        try {
            scrollToElement(batchCard);

            List<WebElement> qrIcons = batchCard.findElements(qrIconInBatchCard);

            System.out.println("QR icon count inside matched batch: " + qrIcons.size());

            if (qrIcons.isEmpty()) {
                return BatchQrStatus.QR_ICON_NOT_AVAILABLE;
            }

            WebElement qrIcon = qrIcons.get(0);

            System.out.println("QR icon displayed: " + qrIcon.isDisplayed());
            System.out.println("QR icon enabled: " + qrIcon.isEnabled());

            scrollToElement(qrIcon);
            clickElement(qrIcon);

            wait.until(ExpectedConditions.visibilityOfElementLocated(qrPopup));
            return BatchQrStatus.QR_POPUP_OPENED;

        } catch (TimeoutException e) {
            System.out.println("QR popup did not open: " + e.getMessage());
            return BatchQrStatus.QR_POPUP_NOT_OPENED;
        } catch (Exception e) {
            System.out.println("Error while clicking Pax Self Register icon: " + e.getMessage());
            return BatchQrStatus.QR_ICON_NOT_AVAILABLE;
        }
    }
    public String getSelfRegistrationUrlFromPopup() {
        try {
            WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(hiddenRegistrationUrl));
            String url = input.getAttribute("value");
            return url == null ? "" : url.trim();
        } catch (Exception e) {
            System.out.println("Unable to get self registration URL from popup: " + e.getMessage());
            return "";
        }
    }

    public boolean isValidRegistrationUrl(String url) {
        return url != null
                && !url.trim().isEmpty()
                && (url.startsWith("http://") || url.startsWith("https://"));
    }
}

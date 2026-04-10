package com.potentia.automation.pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProgramGenrePage {
	 	private WebDriver driver;
	    private WebDriverWait wait;
	    private final JavascriptExecutor js;
	    Actions actions;

	    public ProgramGenrePage(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	        this.actions = new Actions(driver);
	        this.js = (JavascriptExecutor) driver;
	        PageFactory.initElements(driver, this);
	    }
	    
	    private By preloader = By.className("preloader");
	    private By mastersMenu = By.xpath("//a[contains(text(),'Masters')]");
	    private By programGenreMenu = By.xpath("//a[contains(text(),'Program Genre')]");

	    // Page UI
	    private By pageHeading = By.xpath("//h2[normalize-space()='Program Genres']");
	    private final By addProgramGenreButton = By.cssSelector("a.open-addpax[data-bs-target='#addProgramModal']");
	    private final By successMessage = By.xpath("//div[contains(@class,'alert-success') and contains(normalize-space(),'Program has been added successfully.')]");

	    // ===== Top controls =====
	    private final By rowsDropdown = By.id("program_tblLength");
	    private final By searchInput = By.id("searchProgram");
	    private final By filterButton = By.id("programFilterDropDownMenu");
	    private final By activeFilterOption = By.xpath("//a[contains(@class,'dropdown-item') and contains(.,'Active')]");
	    private final By inactiveFilterOption = By.xpath("//a[contains(@class,'dropdown-item') and contains(.,'Inactive')]");

	    // ===== Bulk actions =====
	    private final By bulkActionContainer = By.id("programAction");
	    private final By bulkActivate = By.cssSelector("#programAction a.activate-user");
	    private final By bulkInactivate = By.cssSelector("#programAction a.inactivate-user");
	    private final By bulkDelete = By.cssSelector("#programAction a.delete-user");

	    // ===== Table =====
	    private final By tableWrapper = By.cssSelector(".publish-activity-tble.table-responsive, .table-responsive");
	    private final By table = By.id("programDataTable");
	    private final By tableRows = By.cssSelector("#programDataTable tbody tr");
	    private final By infoText = By.id("programDataTable_info");
	    private final By nextButton = By.id("programDataTable_next");
	    private final By previousButton = By.id("programDataTable_previous");
	    private final By pageButtons = By.cssSelector("#programDataTable_paginate span a");
	    private final By headerCheckbox = By.id("chk_checkAllProgram");
	    private final By rowCheckboxes = By.cssSelector("#programDataTable tbody input[name='programID[]']");

	    // ===== Add Modal =====
	    private final By addModal = By.id("addProgramModal");
	    private final By addModalTitle = By.xpath("//div[@id='addProgramModal']//h5[contains(normalize-space(),'Add Program Genre')]");
	    private final By closeModalIcon = By.xpath("//div[@id='addProgramModal']//button[contains(@class,'btn-close')]");
	    private final By cancelButton = By.xpath("//div[@id='addProgramModal']//a[contains(@class,'btn-cancel') and normalize-space()='Cancel']");
	    private final By saveButton = By.id("submit");

	    // ===== Form Fields =====
	    private final By programGenreInput = By.id("master_title");
	    private final By shortTitleInput = By.id("short_title");
	    private final By descriptionInput = By.id("escription");
	    private final By likelyTitleRows = By.cssSelector("#tblTitleOptions tbody tr");
	    private final By likelyTitleInputs = By.cssSelector("#tblTitleOptions tbody input[name='title_options[]']");
	    private final By addLikelyTitleButton = By.cssSelector("#tblTitleOptions tbody tr:last-child a.add");
	    private final By removeLikelyTitleButtons = By.xpath("//table[@id='tblTitleOptions']//a[contains(@class,'remove') or contains(@class,'delete') or normalize-space()='-' or contains(@class,'minus')]");
	    private final By validationTooltips = By.cssSelector("div[class*='formError'] .formErrorContent");
	    private final By clearFilterIcon = By.xpath("//a[contains(@onclick,'clearFilterProgramList')]");
	    

	    public enum ProgramStatus {
	        ACTIVE,
	        INACTIVE
	    }
	    public void waitForPageToLoad() {
	        wait.until(ExpectedConditions.invisibilityOfElementLocated(preloader));
	        wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeading));
	    }

	    public void clickSuperClient(String clientName) {
	        By clientLocator = By.xpath("//a[text()='" + clientName + "']");
	        wait.until(ExpectedConditions.invisibilityOfElementLocated(preloader));
	        wait.until(ExpectedConditions.elementToBeClickable(clientLocator)).click();
	    }

	    public void clickProgramGenreFromMasters() {
	        wait.until(ExpectedConditions.elementToBeClickable(mastersMenu)).click();
	        wait.until(ExpectedConditions.visibilityOfElementLocated(programGenreMenu));
	        wait.until(ExpectedConditions.elementToBeClickable(programGenreMenu)).click();
	        waitForPageToLoad();
	    }
	    
	    public boolean isProgramGenreListHeadingDisplayed() {
	        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeading)).isDisplayed();
	    }

	    public String getProgramGenreListHeadingText() {
	        return driver.findElement(pageHeading).getText().trim();
	    }
	    // =========================
	    // Page level methods
	    // =========================

	    public boolean isPageDisplayed() {
	        return isDisplayed(pageHeading) && isDisplayed(table);
	    }

	    public String getPageHeading() {
	        return getText(pageHeading);
	    }

	    public void clickAddProgramGenre() {
	        click(addProgramGenreButton);
	        wait.until(ExpectedConditions.visibilityOfElementLocated(addModal));
	        wait.until(ExpectedConditions.visibilityOfElementLocated(addModalTitle));
	    }

	    public boolean isAddProgramGenreModalDisplayed() {
	        return isDisplayed(addModal) && isDisplayed(addModalTitle);
	    }

	    public String getAddModalTitle() {
	        return getText(addModalTitle);
	    }

	    public void closeAddModalUsingX() {
	        click(closeModalIcon);
	        wait.until(ExpectedConditions.invisibilityOfElementLocated(addModal));
	    }

	    public void closeAddModalUsingCancel() {
	        click(cancelButton);
	        wait.until(ExpectedConditions.invisibilityOfElementLocated(addModal));
	    }

	    // =========================
	    // Form methods
	    // =========================
	    public void enterProgramGenre(String programGenre) {
	        type(programGenreInput, programGenre);
	    }

	    public void enterShortTitle(String shortTitle) {
	        type(shortTitleInput, shortTitle);
	    }

	    public void enterDescription(String description) {
	        type(descriptionInput, description);
	    }

	    public int getLikelyTitleRowCount() {
	        return driver.findElements(likelyTitleRows).size();
	    }

	    public void setLikelyTitleAt(int index, String value) {
	        List<WebElement> inputs = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(likelyTitleInputs));
	        if (index < 0 || index >= inputs.size()) {
	            throw new IllegalArgumentException("Invalid likely title input index: " + index);
	        }

	        WebElement input = inputs.get(index);
	        scrollIntoView(input);
	        input.clear();
	        input.sendKeys(value);
	    }

	    public void clickAddLikelyTitle() {
	        int before = getLikelyTitleRowCount();
	        click(addLikelyTitleButton);
	        wait.until(driver -> getLikelyTitleRowCount() > before);
	    }

	    public void addLikelyTitles(List<String> titles) {
	        if (titles == null || titles.isEmpty()) {
	            return;
	        }

	        setLikelyTitleAt(0, titles.get(0));

	        for (int i = 1; i < titles.size(); i++) {
	            clickAddLikelyTitle();
	            setLikelyTitleAt(i, titles.get(i));
	        }
	    }

	    public boolean canRemoveLikelyTitleRow() {
	        return !driver.findElements(removeLikelyTitleButtons).isEmpty();
	    }

	    public void removeLastLikelyTitleRow() {
	        List<WebElement> removeButtons = driver.findElements(removeLikelyTitleButtons);
	        if (removeButtons.isEmpty()) {
	            throw new NoSuchElementException("No removable likely title row found.");
	        }

	        int before = getLikelyTitleRowCount();
	        WebElement lastRemove = removeButtons.get(removeButtons.size() - 1);
	        scrollIntoView(lastRemove);
	        safeClick(lastRemove);

	        wait.until(driver -> getLikelyTitleRowCount() < before);
	    }

	    public void clickSave() {
	        click(saveButton);
	    }

	    public void addProgramGenre(String programGenre, String shortTitle, List<String> likelyTitles, String description) {
	        clickAddProgramGenre();
	        enterProgramGenre(programGenre);

	        if (shortTitle != null && !shortTitle.trim().isEmpty()) {
	            enterShortTitle(shortTitle);
	        }

	        if (likelyTitles != null && !likelyTitles.isEmpty()) {
	            addLikelyTitles(likelyTitles);
	        }

	        if (description != null && !description.trim().isEmpty()) {
	            enterDescription(description);
	        }

	        clickSave();
	    }

	    public boolean isRequiredValidationPresent() {
	        List<WebElement> invalidElements = driver.findElements(
	                By.cssSelector("#addProgramModal input:invalid, #addProgramModal textarea:invalid, #addProgramModal .has-danger, #addProgramModal .is-invalid"));
	        return !invalidElements.isEmpty();
	    }

	    public boolean isProgramAddedSuccessMessageDisplayed() {
	        try {
	            wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
	            return true;
	        } catch (TimeoutException e) {
	            return false;
	        }
	    }

	    // =========================
	    // Search / Filter / Rows / Pagination
	    // =========================
	    public void searchProgram(String keyword) {
	        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
	        input.clear();
	        input.sendKeys(keyword);
	        input.sendKeys(Keys.TAB);
	        waitForTableRefresh();
	    }

	    public void clearSearch() {
	        searchProgram("");
	    }

	    public void selectRowsCount(String visibleText) {
	        Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(rowsDropdown)));
	        select.selectByVisibleText(visibleText);
	        waitForTableRefresh();
	    }

	    public String getInfoText() {
	        return getText(infoText);
	    }

	    public boolean isPaginationDisplayed() {
	        return driver.findElements(pageButtons).size() > 0;
	    }

	    public void clickNextPage() {
	        WebElement next = wait.until(ExpectedConditions.elementToBeClickable(nextButton));
	        scrollIntoView(next);
	        safeClick(next);
	        waitForTableRefresh();
	    }

	    public void clickPreviousPage() {
	        WebElement prev = wait.until(ExpectedConditions.elementToBeClickable(previousButton));
	        scrollIntoView(prev);
	        safeClick(prev);
	        waitForTableRefresh();
	    }

	    public void clickPageNumber(String pageNumber) {
	        List<WebElement> pages = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(pageButtons));
	        for (WebElement page : pages) {
	            if (page.getText().trim().equals(pageNumber)) {
	                scrollIntoView(page);
	                safeClick(page);
	                waitForTableRefresh();
	                return;
	            }
	        }
	        throw new RuntimeException("Page number not found: " + pageNumber);
	    }

	    public void applyActiveFilter() {
	        click(filterButton);
	        click(activeFilterOption);
	        waitForTableRefresh();
	    }

	    public void applyInactiveFilter() {
	        click(filterButton);
	        click(inactiveFilterOption);
	        waitForTableRefresh();
	    }

	    public boolean allVisibleRowsMatchStatus(ProgramStatus expectedStatus) {
	        List<WebElement> rows = driver.findElements(tableRows);

	        for (WebElement row : rows) {
	            List<WebElement> cells = row.findElements(By.tagName("td"));
	            if (cells.size() < 6) {
	                continue;
	            }

	            String actualStatus = cells.get(5).getText().trim();
	            if (!actualStatus.equalsIgnoreCase(expectedStatus.name())) {
	                return false;
	            }
	        }
	        return true;
	    }

	    // =========================
	    // Table data
	    // =========================
	    public List<String> getVisibleProgramNames() {
	        List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(tableRows));
	        List<String> names = new ArrayList<>();

	        for (WebElement row : rows) {
	            List<WebElement> cells = row.findElements(By.tagName("td"));
	            if (cells.size() > 1) {
	                names.add(cells.get(1).getText().trim());
	            }
	        }

	        return names;
	    }

	    public boolean isProgramVisibleInCurrentTable(String programName) {
	        return getVisibleProgramNames().stream()
	                .anyMatch(name -> name.equalsIgnoreCase(programName));
	    }

	    public ProgramStatus getProgramStatusByName(String programName) {
	        WebElement row = getRowByProgramName(programName);
	        WebElement checkbox = row.findElement(By.cssSelector("input[name='programID[]']"));
	        String status = checkbox.getAttribute("data-status");

	        if ("1".equals(status)) {
	            return ProgramStatus.ACTIVE;
	        } else if ("0".equals(status)) {
	            return ProgramStatus.INACTIVE;
	        }

	        throw new RuntimeException("Unknown status for program: " + programName + ", data-status=" + status);
	    }

	    // =========================
	    // Checkbox / bulk action methods
	    // =========================
	    public void selectHeaderCheckbox(boolean shouldSelect) {
	        WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(headerCheckbox));
	        scrollIntoView(checkbox);

	        if (checkbox.isSelected() != shouldSelect) {
	            safeClick(checkbox);
	            waitForSmallPause();
	        }
	    }

	    public int getSelectedRowCheckboxCount() {
	        return (int) driver.findElements(rowCheckboxes).stream()
	                .filter(WebElement::isSelected)
	                .count();
	    }

	    public void selectProgramCheckboxByName(String programName) {
	        WebElement row = getRowByProgramName(programName);
	        WebElement checkbox = row.findElement(By.cssSelector("input[name='programID[]']"));
	        scrollIntoView(checkbox);

	        if (!checkbox.isSelected()) {
	            safeClick(checkbox);
	            waitForSmallPause();
	        }
	    }

	    public void unselectProgramCheckboxByName(String programName) {
	        WebElement row = getRowByProgramName(programName);
	        WebElement checkbox = row.findElement(By.cssSelector("input[name='programID[]']"));
	        scrollIntoView(checkbox);

	        if (checkbox.isSelected()) {
	            safeClick(checkbox);
	            waitForSmallPause();
	        }
	    }

	    public boolean isBulkActionContainerDisplayed() {
	        return isDisplayed(bulkActionContainer);
	    }

	    public boolean isBulkActivateVisible() {
	        return isDisplayedAndNotHidden(bulkActivate);
	    }

	    public boolean isBulkInactivateVisible() {
	        return isDisplayedAndNotHidden(bulkInactivate);
	    }

	    public boolean isBulkDeleteVisible() {
	        return isDisplayedAndNotHidden(bulkDelete);
	    }

	    public List<String> getSelectedProgramNames() {
	        return driver.findElements(rowCheckboxes).stream()
	                .filter(WebElement::isSelected)
	                .map(cb -> cb.findElement(By.xpath("./ancestor::tr/td[2]")).getText().trim())
	                .collect(Collectors.toList());
	    }

	    // =========================
	    // Row action methods
	    // =========================
	    public void scrollTableHorizontallyToEnd() {
	        WebElement wrapper = wait.until(ExpectedConditions.visibilityOfElementLocated(tableWrapper));
	        js.executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth;", wrapper);
	        waitForSmallPause();
	    }

	    public void scrollTableHorizontallyToStart() {
	        WebElement wrapper = wait.until(ExpectedConditions.visibilityOfElementLocated(tableWrapper));
	        js.executeScript("arguments[0].scrollLeft = 0;", wrapper);
	        waitForSmallPause();
	    }

	    public boolean areRowActionIconsVisible(String programName) {
	        WebElement row = getRowByProgramName(programName);
	        scrollIntoView(row);
	        scrollTableHorizontallyToEnd();

	        List<WebElement> actionLinks = row.findElements(By.cssSelector("td:last-child a"));
	        long visibleCount = actionLinks.stream().filter(WebElement::isDisplayed).count();
	        return visibleCount >= 3;
	    }

	    public boolean isEditIconVisible(String programName) {
	        WebElement row = getRowByProgramName(programName);
	        scrollIntoView(row);
	        scrollTableHorizontallyToEnd();

	        return !row.findElements(By.xpath(".//td[last()]//a[contains(@data-bs-target,'#editProgramModal') or .//i[contains(@class,'fa-edit')]]")).isEmpty();
	    }

	    public boolean isDeleteIconVisible(String programName) {
	        WebElement row = getRowByProgramName(programName);
	        scrollIntoView(row);
	        scrollTableHorizontallyToEnd();

	        return !row.findElements(By.xpath(".//td[last()]//a[.//i[contains(@class,'fa-trash')]]")).isEmpty();
	    }

	    public boolean isActivateIconVisible(String programName) {
	        WebElement row = getRowByProgramName(programName);
	        scrollIntoView(row);
	        scrollTableHorizontallyToEnd();

	        List<WebElement> elements = row.findElements(By.xpath(".//td[last()]//a[contains(@class,'activate-user') and not(contains(@class,'d-none'))]"));
	        return !elements.isEmpty() && elements.get(0).isDisplayed();
	    }

	    public boolean isInactivateIconVisible(String programName) {
	        WebElement row = getRowByProgramName(programName);
	        scrollIntoView(row);
	        scrollTableHorizontallyToEnd();

	        List<WebElement> elements = row.findElements(By.xpath(".//td[last()]//a[contains(@class,'inactivate-user') and not(contains(@class,'d-none'))]"));
	        return !elements.isEmpty() && elements.get(0).isDisplayed();
	    }

	    public void clickEditByProgramName(String programName) {
	        WebElement row = getRowByProgramName(programName);
	        scrollIntoView(row);
	        scrollTableHorizontallyToEnd();

	        WebElement edit = row.findElement(By.xpath(".//td[last()]//a[contains(@data-bs-target,'#editProgramModal') or .//i[contains(@class,'fa-edit')]]"));
	        safeClick(edit);
	    }

	    // =========================
	    // Internal helpers
	    // =========================
	    private WebElement getRowByProgramName(String programName) {
	        wait.until(ExpectedConditions.visibilityOfElementLocated(table));

	        for (WebElement row : driver.findElements(tableRows)) {
	            try {
	                List<WebElement> cells = row.findElements(By.tagName("td"));
	                if (cells.size() > 1 && cells.get(1).getText().trim().equalsIgnoreCase(programName)) {
	                    return row;
	                }
	            } catch (StaleElementReferenceException e) {
	                waitForTableRefresh();
	                return getRowByProgramName(programName);
	            }
	        }

	        throw new RuntimeException("Program row not found: " + programName);
	    }

	    private void click(By locator) {
	        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
	        scrollIntoView(element);
	        safeClick(element);
	    }

	    private void type(By locator, String value) {
	        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	        scrollIntoView(element);
	        element.clear();
	        element.sendKeys(value);
	    }

	    private String getText(By locator) {
	        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText().trim();
	    }

	    private boolean isDisplayed(By locator) {
	        try {
	            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
	        } catch (Exception e) {
	            return false;
	        }
	    }

	    private boolean isDisplayedAndNotHidden(By locator) {
	        try {
	            WebElement element = driver.findElement(locator);
	            String classes = element.getAttribute("class");
	            return element.isDisplayed() && (classes == null || !classes.contains("d-none"));
	        } catch (Exception e) {
	            return false;
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
	        js.executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", element);
	    }

	    private void waitForTableRefresh() {
	        waitForSmallPause();
	        wait.until(ExpectedConditions.visibilityOfElementLocated(table));
	    }

	    private void waitForSmallPause() {
	        try {
	            Thread.sleep(500);
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	        }
	    }
	    public List<String> getVisibleValidationMessages() {
	        waitForSmallPause();

	        List<WebElement> tooltips = driver.findElements(validationTooltips);
	        List<String> messages = new ArrayList<>();

	        for (WebElement tooltip : tooltips) {
	            try {
	                if (tooltip.isDisplayed()) {
	                    String text = tooltip.getText().trim();
	                    if (!text.isEmpty()) {
	                        messages.add(text);
	                    }
	                }
	            } catch (Exception e) {
	                // ignore stale/hidden tooltip
	            }
	        }
	        return messages;
	    }

	    public boolean isRequiredTooltipDisplayed() {
	        return getVisibleValidationMessages().stream()
	                .anyMatch(msg -> msg.equalsIgnoreCase("* This field is required")
	                        || msg.equalsIgnoreCase("This field is required")
	                        || msg.contains("This field is required"));
	    }

	    public int getRequiredTooltipCount() {
	        return (int) getVisibleValidationMessages().stream()
	                .filter(msg -> msg.equalsIgnoreCase("* This field is required")
	                        || msg.equalsIgnoreCase("This field is required")
	                        || msg.contains("This field is required"))
	                .count();
	    }

	    public boolean isProgramGenreHighlightedAsError() {
	        return hasRedBorder(programGenreInput);
	    }

	    public boolean isLikelyTitleHighlightedAsError() {
	        List<WebElement> inputs = driver.findElements(likelyTitleInputs);
	        if (inputs.isEmpty()) {
	            return false;
	        }
	        return hasRedBorder(inputs.get(0));
	    }

	    private boolean hasRedBorder(By locator) {
	        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	        return hasRedBorder(element);
	    }

	    private boolean hasRedBorder(WebElement element) {
	        String borderColor = element.getCssValue("border-color");
	        String borderTopColor = element.getCssValue("border-top-color");

	        String normalizedBorderColor = borderColor == null ? "" : borderColor.trim().toLowerCase();
	        String normalizedTopBorderColor = borderTopColor == null ? "" : borderTopColor.trim().toLowerCase();

	        return normalizedBorderColor.contains("255, 0, 0")
	                || normalizedTopBorderColor.contains("255, 0, 0")
	                || normalizedBorderColor.contains("rgb(255, 0, 0)")
	                || normalizedTopBorderColor.contains("rgb(255, 0, 0)")
	                || normalizedBorderColor.contains("#ff0000")
	                || normalizedTopBorderColor.contains("#ff0000");
	    }
	    public void clearFilter() {
	        wait.until(ExpectedConditions.visibilityOfElementLocated(clearFilterIcon));

	        WebElement clearBtn = driver.findElement(clearFilterIcon);
	        scrollIntoView(clearBtn);

	        safeClick(clearBtn);

	        waitForTableRefresh();
	    }
	    public boolean hasMixedStatusesInVisibleRows() {

	        boolean hasActive = false;
	        boolean hasInactive = false;

	        List<WebElement> rows = driver.findElements(tableRows);

	        for (WebElement row : rows) {
	            try {
	                List<WebElement> cells = row.findElements(By.tagName("td"));

	                // Status column index = 5 (based on your table structure)
	                if (cells.size() < 6) continue;

	                String status = cells.get(5).getText().trim();

	                if (status.equalsIgnoreCase("Active")) {
	                    hasActive = true;
	                } else if (status.equalsIgnoreCase("Inactive")) {
	                    hasInactive = true;
	                }

	                // If both found → no filter applied
	                if (hasActive && hasInactive) {
	                    return true;
	                }

	            } catch (StaleElementReferenceException e) {
	                waitForTableRefresh();
	                return hasMixedStatusesInVisibleRows(); // retry
	            }
	        }

	        return false; // only one type → filter still applied
	    }
	    

}

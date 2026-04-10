package com.potentia.automation.pages;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ClientPage {
    private WebDriver driver;
    private WebDriverWait wait;
    Actions actions;

    public ClientPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
    }

    // Navigation
    private By mastersMenu = By.xpath("//a[contains(text(),'Masters')]");
    private By clientsMenu = By.xpath("//a[contains(text(),'Clients')]");

    // Page UI
    private By pageHeading = By.xpath("//h2[normalize-space()='Client List']");
    private By addClientBtn = By.xpath("//a[contains(@class,'open-addpax') and contains(text(),'Client')]");
    private By searchBox = By.id("searchClient");
    private By rowsDropdown = By.id("client_tblLength");
    private By table = By.id("clientDataTable");
    private By tableRows = By.xpath("//table[@id='clientDataTable']/tbody/tr");
    private By tableHeaders = By.xpath("//table[@id='clientDataTable']//thead//th");
    private By entriesInfo = By.id("clientDataTable_info");
    private By nextBtn = By.id("clientDataTable_next");

    // Popup
    private By popupTitle = By.xpath("//h5[normalize-space()='Add Client']");
    private By closePopupIcon = By.xpath("//div[contains(@class,'modal-content')]//button[contains(@class,'btn-close')]");
    private By validationErrors = By.xpath("//div[contains(@class,'formErrorContent')]");
    private By logoFileTypeError = By.xpath("//div[contains(@class,'formErrorContent') and contains(text(),'Sorry we do not recognize this file type. Please upload png, jpg, gif file formats only.')]");
    private By toastMsg = By.xpath("//*[contains(text(),'Logo deleted successfully')]");
    private By preloader = By.className("preloader");

    @FindBy(id = "name")
    private WebElement txtName;

    @FindBy(id = "industry")
    private WebElement txtIndustry;

    @FindBy(id = "logo")
    private WebElement fileInput;

    @FindBy(xpath = "//a[contains(@class,'reset-select-image')]")
    private WebElement removeLogoBtn;

    @FindBy(xpath = "//button[text()='OK']")
    private WebElement btnOk;

    @FindBy(id = "editor")
    private WebElement txtLandingMsg;

    @FindBy(xpath = "//button[text()='Save']")
    private WebElement btnSave;

    @FindBy(xpath = "(//a[text()='Cancel'])[1]")
    private WebElement btnCancel;

    @FindBy(xpath = "//div[@class='message']//div[contains(@class,'alert')]")
    private WebElement alertMessage;

    @FindBy(id = "submit")
    private WebElement update;

    // Filter
    private By filterDropdown = By.id("clientFilterDropDownMenu");
    private By activeFilterOption = By.xpath("//a[contains(@onclick,\"'Active'\")]");
    private By inactiveFilterOption = By.xpath("//a[contains(@onclick,\"'Inactive'\")]");
    private By clearFilter = By.xpath("//a[contains(@onclick,'clearFilterClientList')]");
    private By statusColumnValues = By.xpath("//table[@id='clientDataTable']//tbody//td[5]");

    // Action messages
    private By inactiveClient = By.xpath("//*[contains(text(),'has been inactivated successfully.')]");
    private By activeClient = By.xpath("//*[contains(text(),'has been activated successfully.')]");
    private By deleteSuccess = By.xpath("//*[contains(text(),'deleted successfully') or contains(text(),'has been deleted successfully')]");
    private By duplicateError = By.xpath("//*[contains(text(),'already exists')]");
    private By noDataRow = By.xpath("//table[@id='clientDataTable']/tbody/tr/td[contains(text(),'No matching records found')]");

    public void waitForPageToLoad() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(preloader));
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeading));
    }

    public void clickSuperClient(String clientName) {
        By clientLocator = By.xpath("//a[text()='" + clientName + "']");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(preloader));
        wait.until(ExpectedConditions.elementToBeClickable(clientLocator)).click();
    }

    public void clickClientsFromMasters() {
        wait.until(ExpectedConditions.elementToBeClickable(mastersMenu)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(clientsMenu));
        wait.until(ExpectedConditions.elementToBeClickable(clientsMenu)).click();
        waitForPageToLoad();
    }

    public boolean isClientListHeadingDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeading)).isDisplayed();
    }

    public String getClientListHeadingText() {
        return driver.findElement(pageHeading).getText().trim();
    }

    public boolean isAddClientButtonDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(addClientBtn)).isDisplayed();
    }

    public boolean isAddClientButtonEnabled() {
        return wait.until(ExpectedConditions.elementToBeClickable(addClientBtn)).isEnabled();
    }

    public boolean isSearchBoxDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox)).isDisplayed();
    }

    public boolean isRowsDropdownDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(rowsDropdown)).isDisplayed();
    }

    public boolean isClientTableDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(table)).isDisplayed();
    }

    public List<String> getTableHeaderTexts() {
        List<String> headers = new ArrayList<>();
        for (WebElement header : wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(tableHeaders))) {
            headers.add(header.getText().trim());
        }
        return headers;
    }

    public void clickAddClient() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(preloader));
        wait.until(ExpectedConditions.elementToBeClickable(addClientBtn)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(popupTitle));
    }

    public boolean isAddClientPopupDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(popupTitle)).isDisplayed();
    }

    public String getAddClientPopupTitle() {
        return driver.findElement(popupTitle).getText().trim();
    }

    public boolean isNameFieldDisplayed() {
        return wait.until(ExpectedConditions.visibilityOf(txtName)).isDisplayed();
    }

    public boolean isIndustryFieldDisplayed() {
        return wait.until(ExpectedConditions.visibilityOf(txtIndustry)).isDisplayed();
    }

    public boolean isSaveButtonDisplayed() {
        return wait.until(ExpectedConditions.visibilityOf(btnSave)).isDisplayed();
    }

    public boolean isCancelButtonDisplayed() {
        return wait.until(ExpectedConditions.visibilityOf(btnCancel)).isDisplayed();
    }

    public void closePopupUsingX() {
        wait.until(ExpectedConditions.elementToBeClickable(closePopupIcon)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(popupTitle));
    }

    public void clickCancel() {
        wait.until(ExpectedConditions.elementToBeClickable(btnCancel)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(popupTitle));
    }
	public void clickClientName() {
		txtName.click();
	}
    public void enterName(String name) {
        wait.until(ExpectedConditions.visibilityOf(txtName));
        txtName.click();
        txtName.clear();
        txtName.sendKeys(name);
    }

    public void enterIndustry(String industry) {
        wait.until(ExpectedConditions.visibilityOf(txtIndustry));
        txtIndustry.click();
        txtIndustry.clear();
        txtIndustry.sendKeys(industry);
    }

    public String getEnteredName() {
        return txtName.getAttribute("value").trim();
    }

    public String getEnteredIndustry() {
        return txtIndustry.getAttribute("value").trim();
    }

    public void uploadFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("File not found: " + filePath);
        }

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].style.display='block';", fileInput);
        fileInput.sendKeys(file.getAbsolutePath());
    }

    public boolean isLogoFileTypeErrorDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(logoFileTypeError)).isDisplayed();
    }

    public String getLogoFileTypeErrorText() {
        return driver.findElement(logoFileTypeError).getText().trim();
    }

    public void clickRemoveLogo() {
        wait.until(ExpectedConditions.visibilityOf(removeLogoBtn));
        wait.until(ExpectedConditions.elementToBeClickable(removeLogoBtn)).click();
    }

    public void clickOkBtn() {
        wait.until(ExpectedConditions.elementToBeClickable(btnOk)).click();
    }

    public boolean isLogoDeleteSuccessMsgDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(toastMsg)).isDisplayed();
    }

    public boolean isLogoCleared() {
        return fileInput.getAttribute("value") == null || fileInput.getAttribute("value").trim().isEmpty();
    }

    public void clickSave() {
        wait.until(ExpectedConditions.elementToBeClickable(btnSave)).click();
    }

    public void clickUpdateBtn() {
        wait.until(ExpectedConditions.elementToBeClickable(update)).click();
    }

    public int getValidationErrorCount() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(validationErrors));
        return driver.findElements(validationErrors).size();
    }

    public String getValidationErrorText(int index) {
    	List<WebElement> errors = driver.findElements(validationErrors);

        if (errors.isEmpty() || index >= errors.size()) {
            return "";
        }

        return errors.get(index).getText().trim();
    }
    public boolean isValidationErrorDisplayed() {
        List<WebElement> errors = driver.findElements(validationErrors);
        return !errors.isEmpty();
    }

    public String getAlertMessageText() {
        wait.until(ExpectedConditions.visibilityOf(alertMessage));
        return alertMessage.getText().trim();
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(alertMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void waitForAlertToDisappear() {
        wait.until(ExpectedConditions.invisibilityOf(alertMessage));
    }

    public WebElement findClientRow(String clientNameText) {
        goToFirstPage();

        while (true) {
            List<WebElement> rows = driver.findElements(tableRows);

            for (WebElement row : rows) {
                String name = row.findElement(By.xpath(".//td[2]")).getText().trim();
                if (name.equalsIgnoreCase(clientNameText)) {
                    return row;
                }
            }

            WebElement next = driver.findElement(nextBtn);
            if (next.getAttribute("class").contains("disabled")) {
                break;
            }

            next.click();
            waitForTableRefresh();
        }
        return null;
    }

    public boolean isClientPresent(String clientName) {
        return findClientRow(clientName) != null;
    }

    public String getClientIndustryFromRow(String clientName) {
        WebElement row = findClientRow(clientName);
        if (row == null) {
            throw new NoSuchElementException("Client not found: " + clientName);
        }
        return row.findElement(By.xpath(".//td[3]")).getText().trim();
    }

    public String getClientStatusFromRow(String clientName) {
        WebElement row = findClientRow(clientName);
        if (row == null) {
            throw new NoSuchElementException("Client not found: " + clientName);
        }
        return row.findElement(By.xpath(".//td[5]")).getText().trim();
    }

    public void clickEditForClient(String clientName) {
        WebElement row = findClientRow(clientName);
        if (row == null) {
            throw new NoSuchElementException("Client not found: " + clientName);
        }
        row.findElement(By.xpath(".//a[contains(@onclick,'editClient')]")).click();
    }

    public void clickInactivateForClient(String clientName) {
    	 WebElement row = findClientRow(clientName);
    	    if (row == null) {
    	        throw new NoSuchElementException("Client not found: " + clientName);
    	    }

    	    WebElement inactivateLink = row.findElement(By.xpath(".//a[contains(@onclick,'inactivateClient')]"));

    	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", row);

    	    Actions actions = new Actions(driver);
    	    actions.moveToElement(row).pause(Duration.ofMillis(500)).perform();

    	    wait.until(ExpectedConditions.visibilityOf(inactivateLink));
    	    wait.until(ExpectedConditions.elementToBeClickable(inactivateLink));

    	    try {
    	        inactivateLink.click();
    	    } catch (Exception e) {
    	        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", inactivateLink);
    	    }
    }

    public void clickActivateForClient(String clientName) {
        WebElement row = findClientRow(clientName);
        if (row == null) {
            throw new NoSuchElementException("Client not found: " + clientName);
        }
        row.findElement(By.xpath(".//a[contains(@onclick,'activateClient')]")).click();
    }

    public void clickDeleteForClient(String clientName) {
    	WebElement row = findClientRow(clientName);
        if (row == null) {
            throw new NoSuchElementException("Client not found: " + clientName);
        }

        WebElement activateLink = row.findElement(By.xpath(".//a[contains(@onclick,'activateClient')]"));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", row);

        Actions actions = new Actions(driver);
        actions.moveToElement(row).pause(Duration.ofMillis(500)).perform();

        wait.until(ExpectedConditions.visibilityOf(activateLink));
        wait.until(ExpectedConditions.elementToBeClickable(activateLink));

        try {
            activateLink.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", activateLink);
        }
    }

    public boolean isInactiveClientSuccessMsgDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(inactiveClient)).isDisplayed();
    }

    public boolean isActiveClientSuccessMsgDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(activeClient)).isDisplayed();
    }

    public boolean isDeleteSuccessMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(deleteSuccess)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDuplicateErrorDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(duplicateError)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void deleteClientByCheckbox(String clientName) {
        WebElement row = findClientRow(clientName);
        if (row == null) {
            throw new NoSuchElementException("Client not found: " + clientName);
        }

        WebElement checkbox = row.findElement(By.xpath(".//input[@type='checkbox']"));
        if (!checkbox.isSelected()) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
        }

        WebElement deleteIcon = driver.findElement(By.xpath("//a[contains(@onclick,'deleteSelectedClient')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteIcon);
    }

    public void selectRowsCount(String count) {
        Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(rowsDropdown)));
        select.selectByVisibleText(count);
        waitForTableRefresh();
    }

    public int getVisibleRowCount() {
        List<WebElement> rows = driver.findElements(tableRows);
        int count = 0;
        for (WebElement row : rows) {
            if (row.isDisplayed() && !row.getText().trim().equalsIgnoreCase("No data available in table")) {
                count++;
            }
        }
        return count;
    }

    public void searchClient(String clientName) {
        WebElement box = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
        box.clear();
        box.sendKeys(clientName);
        waitForTableRefresh();
    }

    public void clearClientSearch() {
        WebElement box = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
        box.clear();
        waitForTableRefresh();
    }

    public boolean isNoDataMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(noDataRow)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getEntriesInfoText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(entriesInfo)).getText().trim();
    }

    public void openFilterDropdown() {
        wait.until(ExpectedConditions.elementToBeClickable(filterDropdown)).click();
    }

    public void selectActiveFilter() {
        openFilterDropdown();
        wait.until(ExpectedConditions.elementToBeClickable(activeFilterOption)).click();
        waitForTableRefresh();
    }

    public void selectInactiveFilter() {
        openFilterDropdown();
        wait.until(ExpectedConditions.elementToBeClickable(inactiveFilterOption)).click();
        waitForTableRefresh();
    }

    public void clearFilter() {
        wait.until(ExpectedConditions.elementToBeClickable(clearFilter)).click();
        waitForTableRefresh();
    }

    public boolean verifyAllStatusAs(String expectedStatus) {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(statusColumnValues));
        List<WebElement> statusList = driver.findElements(statusColumnValues);

        for (WebElement status : statusList) {
            if (!status.getText().trim().equalsIgnoreCase(expectedStatus)) {
                return false;
            }
        }
        return true;
    }

    public boolean verifyMixedStatusesPresent() {
        List<WebElement> statusList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(statusColumnValues));
        boolean hasActive = false;
        boolean hasInactive = false;

        for (WebElement status : statusList) {
            String text = status.getText().trim();
            if (text.equalsIgnoreCase("Active")) {
                hasActive = true;
            } else if (text.equalsIgnoreCase("Inactive")) {
                hasInactive = true;
            }
        }
        return hasActive && hasInactive;
    }

    private void goToFirstPage() {
        try {
            while (true) {
                WebElement previous = driver.findElement(By.id("clientDataTable_previous"));
                if (previous.getAttribute("class").contains("disabled")) {
                    break;
                }
                previous.click();
                waitForTableRefresh();
            }
        } catch (Exception ignored) {
        }
    }

    private void waitForTableRefresh() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(preloader));
        wait.until(ExpectedConditions.visibilityOfElementLocated(table));

        try {
            wait.until(driver -> {
                try {
                    driver.findElements(tableRows).size();
                    return true;
                } catch (StaleElementReferenceException e) {
                    return false;
                }
            });
        } catch (Exception ignored) {
        }
    }
    public String getUploadedFileValue() {
        wait.until(ExpectedConditions.visibilityOf(fileInput));
        return fileInput.getAttribute("value");
    }

    public boolean isFileSelectedInInput() {
        try {
            String value = fileInput.getAttribute("value");
            return value != null && !value.trim().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
    public void uploadLogo(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("File not found: " + filePath);
        }

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("logo")));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
            "arguments[0].style.display='block';" +
            "arguments[0].style.visibility='visible';" +
            "arguments[0].removeAttribute('hidden');",
            fileInput
        );

        fileInput.sendKeys(file.getAbsolutePath());
    }
    public String getUploadedLogoValue() {
        return fileInput.getAttribute("value");
    }
    public boolean isLogoUploaded() {
        try {
            String value = fileInput.getAttribute("value");
            return value != null && !value.trim().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
    public void waitUntilLogoFileIsSelected(String expectedFileName) {
        wait.until(driver -> {
            String value = fileInput.getAttribute("value");
            System.out.println("Current uploaded value: " + value);
            return value != null
                    && !value.trim().isEmpty()
                    && value.toLowerCase().contains(expectedFileName.toLowerCase());
        });
    }
    public void ensureClientIsActive(String clientName) {
        String status = getClientStatusFromRow(clientName);

        if (status.equalsIgnoreCase("Inactive")) {
            clickActivateForClient(clientName);
            clickOkBtn();

            if (!isActiveClientSuccessMsgDisplayed()) {
                throw new AssertionError("Failed to activate client: " + clientName);
            }

            waitForAlertToDisappear();
        }
    }
    private By deleteSuccessMessage = By.xpath("//*[contains(text(),'deleted successfully')]");
    private By deleteBlockedMessage = By.xpath("//*[contains(text(),'cannot be deleted as it is mapped')]");

    public String getDeleteSuccessMessageText() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(deleteSuccessMessage))
                    .getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public String getDeleteBlockedMessageText() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(deleteBlockedMessage))
                    .getText().trim();
        } catch (Exception e) {
            return "";
        }
    }
}

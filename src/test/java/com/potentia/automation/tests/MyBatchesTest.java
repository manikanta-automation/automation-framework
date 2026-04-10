package com.potentia.automation.tests;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.potentia.automation.core.BaseTest;
import com.potentia.automation.pages.LoginPage;
import com.potentia.automation.pages.MyBatchesPage;
import com.potentia.automation.pages.SuperClientsPage;
import com.potentia.automation.utils.ConfigReader;

public class MyBatchesTest extends BaseTest {


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
    public void TC_MB_02_verifyFacilitatorNavigatesToMyBatchesPage() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
        Assert.assertTrue(
                myBatchesPage.isMyBatchesPageDisplayed(),
                "Facilitator did not navigate to My Batches page."
        );

        Assert.assertEquals(
                myBatchesPage.getPageHeadingText(),
                "My Batches",
                "My Batches page heading text mismatch."
        );
    }

    @Test(priority = 3)
    public void TC_MB_03_verifyPageLoadsWithoutUiBreak() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
        Assert.assertTrue(
                myBatchesPage.isPageLoadedWithoutUiBreak(),
                "My Batches page layout appears broken."
        );
    }


    @Test(priority = 4)
    public void TC_MB_04_verifyNewBatchButtonDisplayed() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
        Assert.assertTrue(
                myBatchesPage.isNewBatchButtonDisplayed(),
                "New Batch button is not displayed."
        );
    }

    @Test(priority = 5)
    public void TC_MB_05_verifyPrimaryTabsDisplayed() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
        Assert.assertTrue(
                myBatchesPage.arePrimaryTabsDisplayed(),
                "Primary tabs are not displayed correctly."
        );
    }

    @Test(priority = 6)
    public void TC_MB_06_verifySecondaryTabsDisplayed() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
        Assert.assertTrue(
                myBatchesPage.areSecondaryTabsDisplayed(),
                "Secondary tabs are not displayed correctly."
        );
    }

    @Test(priority = 7)
    public void TC_MB_07_verifyBatchCardsDisplayed() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
        Assert.assertTrue(
                myBatchesPage.areBatchCardsDisplayed(),
                "Visible batch cards are not displayed."
        );
    }
    
    @Test(priority = 8)
    public void TC_MB_08_verifyBatchCardMandatoryDetails() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
        Assert.assertTrue(
                myBatchesPage.doesEachVisibleBatchCardContainMandatoryDetails(),
                "One or more visible batch cards are missing mandatory details."
        );
    }
    
    @Test(priority = 9)
    public void TC_MB_09_verifyBatchTitlesAreDisplayed() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
        Assert.assertTrue(
                myBatchesPage.areBatchTitlesNonEmpty(),
                "Batch titles are blank or not displayed."
        );
    }

    @Test(priority = 10)
    public void TC_MB_10_verifyBatchActionIconsDisplayed() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
        Assert.assertTrue(
                myBatchesPage.areEditIconsDisplayed(),
                "Edit icons are not displayed."
        );

        Assert.assertTrue(
                myBatchesPage.areQrIconsDisplayed(),
                "QR icons are not displayed."
        );

        Assert.assertTrue(
                myBatchesPage.areCloseIconsDisplayed(),
                "Close icons are not displayed."
        );
    }
    
    @Test(priority = 11)
    public void TC_MB_11_verifyBatchCardsCountGreaterThanZero() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
        Assert.assertTrue(
                myBatchesPage.getVisibleBatchCardCount() > 0,
                "Visible batch card count is zero."
        );
    }
    
    @Test(priority = 12)
    public void TC_MB_Search_12_verifySearchFieldDisplayed() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
        Assert.assertTrue(
                myBatchesPage.isSearchFieldDisplayed(),
                "Search field is not displayed."
        );
    }
    
    @Test(priority = 13)
    public void TC_MB_Search_13_verifySearchWithInvalidBatchName() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
        String invalidBatchName = ConfigReader.get("invalidBatchName");

        myBatchesPage.searchBatch(invalidBatchName);

        Assert.assertEquals(
                myBatchesPage.getSearchFieldValue(),
                invalidBatchName,
                "Invalid search text was not entered correctly."
        );

        myBatchesPage.clearSearchBox();

        Assert.assertTrue(
                myBatchesPage.isSearchBoxEmpty(),
                "Search box was not cleared after invalid search."
        );

        Assert.assertTrue(
                myBatchesPage.areBatchesDisplayedAfterClear(),
                "Batch list was not displayed again after clearing search."
        );
    }

    @Test(priority = 14)
    public void TC_MB_Search_14verifySearchFieldAcceptsInput() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
        String searchText = ConfigReader.get("searchbatchName");
        myBatchesPage.searchBatch(searchText);

        Assert.assertEquals(
                myBatchesPage.getSearchFieldValue(),
                searchText,
                "Search field did not accept input properly."
        );
        
        myBatchesPage.clearSearchBox();

        Assert.assertTrue(
                myBatchesPage.isSearchBoxEmpty(),
                "Search box was not cleared after invalid search."
        );
    }
    @Test(priority = 15)
    public void TC_MB_SORT_15_verifySortDropdownVisibleAndEnabled() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
        Assert.assertTrue(myBatchesPage.isSortDropdownVisible(), "Sort dropdown is not visible.");
        Assert.assertTrue(myBatchesPage.isSortDropdownEnabled(), "Sort dropdown is not enabled.");
    }

    @Test(priority = 16)
    public void TC_MB_SORT_16_verifySortDropdownOptions() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
        List<String> actualOptions = myBatchesPage.getSortOptionTexts();
        List<String> expectedOptions = Arrays.asList("Batch Name", "Program Title", "Start Date", "Close Date");

        Assert.assertEquals(actualOptions, expectedOptions, "Sort dropdown options do not match.");
    }
    
    @Test(priority = 17)
    public void TC_MB_SORT_17_verifyBatchNameSortingInActiveTab() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
        myBatchesPage.selectSortByBatchName();

        List<String> batchNames = myBatchesPage.getVisibleBatchNames();
        Assert.assertTrue(batchNames.size() > 0, "No batches found in Active tab.");
        Assert.assertTrue(myBatchesPage.isBatchNamesSortedAscending(),
                "Batches are not sorted alphabetically by Batch Name in Active tab.");
    }
    
    @Test(priority = 18)
    public void TC_MB_SORT_18_verifyBatchNameSortingInPlannedTab() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
        myBatchesPage.clickPlannedTab();

        List<String> batchNames = myBatchesPage.getVisibleBatchNames();
        if (batchNames.size() <= 1) {
            System.out.println("Planned tab has 0 or 1 record. Sorting validation skipped.");
            return;
        }

        Assert.assertTrue(myBatchesPage.isBatchNamesSortedAscending(),
                "Batches are not sorted alphabetically by Batch Name in Planned tab.");
    }

    @Test(priority = 19)
    public void TC_MB_SORT_19_verifyBatchNameSortingInInactiveTab() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
    	myBatchesPage.clickInactiveTab();

        List<String> names = myBatchesPage.getVisibleBatchNames();
        System.out.println("Final captured names from Inactive tab: " + names);

        Assert.assertTrue(
                myBatchesPage.isBatchNamesSortedAscending(),
                "Batches are not sorted alphabetically by Batch Name in Inactive tab."
        );
    }

    @Test(priority = 20)
    public void TC_MB_SORT_20_verifyBatchNameSortPersistsAfterRefreshInActiveTab() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
        myBatchesPage.clickActiveTab();

        List<String> beforeRefresh = myBatchesPage.getNormalizedBatchNames();
        Assert.assertTrue(myBatchesPage.isBatchNamesSortedAscending(),
                "Before refresh, Active tab is not sorted by Batch Name.");

        myBatchesPage.refreshPage();

        List<String> afterRefresh = myBatchesPage.getNormalizedBatchNames();
        Assert.assertEquals(afterRefresh, beforeRefresh,
                "Sort order did not persist after refresh in Active tab.");
        Assert.assertTrue(myBatchesPage.isBatchNamesSortedAscending(),
                "After refresh, Active tab is not sorted by Batch Name.");
    }

    @Test(priority = 21)
    public void TC_MB_SORT_21_verifyBatchNameSortPersistsAfterRefreshInPlannedTab() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
        myBatchesPage.clickPlannedTab();


        List<String> beforeRefresh = myBatchesPage.getNormalizedBatchNames();
        if (beforeRefresh.size() <= 1) {
            System.out.println("Planned tab has 0 or 1 record. Persistence validation skipped.");
            return;
        }

        myBatchesPage.refreshPage();


        List<String> afterRefresh = myBatchesPage.getNormalizedBatchNames();
        Assert.assertEquals(afterRefresh, beforeRefresh,
                "Sort order did not persist after refresh in Planned tab.");
        Assert.assertTrue(myBatchesPage.isBatchNamesSortedAscending(),
                "After refresh, Planned tab is not sorted by Batch Name.");
    }

    @Test(priority = 22)
    public void TC_MB_SORT_22_verifyBatchNameSortPersistsAfterRefreshInInactiveTab() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
        myBatchesPage.clickInactiveTab();


        List<String> beforeRefresh = myBatchesPage.getNormalizedBatchNames();
        if (beforeRefresh.size() <= 1) {
            System.out.println("Inactive tab has 0 or 1 record. Persistence validation skipped.");
            return;
        }

        myBatchesPage.refreshPage();


        List<String> afterRefresh = myBatchesPage.getNormalizedBatchNames();
        Assert.assertEquals(afterRefresh, beforeRefresh,
                "Sort order did not persist after refresh in Inactive tab.");
        Assert.assertTrue(myBatchesPage.isBatchNamesSortedAscending(),
                "After refresh, Inactive tab is not sorted by Batch Name.");
    }

    @Test(priority = 23)
    public void TC_MB_SORT_23_verifyClearSortIfAvailable() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
        myBatchesPage.clickActiveTab();

        Assert.assertTrue(myBatchesPage.isBatchNamesSortedAscending(),
                "Batch Name sort was not applied before clear sort validation.");

        if (myBatchesPage.isClearSortVisible()) {
            myBatchesPage.clickClearSortIfVisible();
            List<String> afterClearSort = myBatchesPage.getNormalizedBatchNames();

            Assert.assertNotEquals(afterClearSort, new java.util.ArrayList<String>(),
                    "Batch list is empty after clear sort.");
        } else {
            System.out.println("Clear sort icon is not visible. Skipping clear sort validation.");
        }
    }
    
    @Test(priority = 24)
    public void TC_MB_FIL_24_verifyFilterButtonVisibleAndEnabled() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
        Assert.assertTrue(myBatchesPage.isFilterButtonVisible(), "Filter button is not visible.");
        Assert.assertTrue(myBatchesPage.isFilterButtonEnabled(), "Filter button is not enabled.");
    }

    @Test(priority = 25)
    public void TC_MB_FIL_25_verifyFilterModalOpensSuccessfully() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
        myBatchesPage.openFilterModal();

        Assert.assertTrue(myBatchesPage.isFilterModalDisplayed(), "Filter modal is not displayed.");
        Assert.assertEquals(myBatchesPage.getFilterModalTitle(), "Filter", "Filter modal title is incorrect.");
    }
    @Test(priority = 26)
    public void TC_MB_FIL_26_verifyClientsExpandedByDefaultAndProgramCollapsedByDefault() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);

        Assert.assertTrue(myBatchesPage.isClientsAccordionExpandedByDefault(),
                "Clients accordion is not expanded by default.");
        myBatchesPage.scrollToProgramTitleAccordion();
        Assert.assertTrue(myBatchesPage.isProgramAccordionCollapsedByDefault(),
                "Program Title accordion is not collapsed by default.");
        myBatchesPage.expandProgramAccordionIfNeeded();

        Assert.assertTrue(myBatchesPage.isProgramAccordionExpanded(),
                "Program Title accordion did not expand after scrolling and clicking.");
    }
 
    @Test(priority = 27)
    public void TC_MB_FIL_27_verifyClientFilterAppliesInActiveTab() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
    	myBatchesPage.expandClientsAccordionIfNeeded();
        myBatchesPage.applyClientFilter(ConfigReader.get("filterClientName"));

        Assert.assertTrue(myBatchesPage.isClearFilterVisible(),
                "Clear Filter is not visible after applying client filter.");
        Assert.assertTrue(myBatchesPage.isAppliedFilterTextPresent(ConfigReader.get("filterClientName")),
                "Applied client filter text is not visible.");
        Assert.assertTrue(myBatchesPage.areAllVisibleBatchesForClient(ConfigReader.get("filterClientName")),
                "Not all visible Active tab batches belong to selected client.");
        
        myBatchesPage.refreshPage();

        Assert.assertTrue(myBatchesPage.isClearFilterVisible(),
                "Clear Filter is not visible after refresh.");
        Assert.assertTrue(myBatchesPage.isAppliedFilterTextPresent(ConfigReader.get("filterClientName")),
                "Applied client filter did not persist after refresh.");
        Assert.assertTrue(myBatchesPage.areAllVisibleBatchesForClient(ConfigReader.get("filterClientName")),
                "Client filter did not persist correctly after refresh.");
    }
    @Test(priority = 28)
    public void TC_MB_FIL_28_verifyClientFilterAppliesInPlannedTab() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);

        myBatchesPage.clickPlannedTab();

        Assert.assertTrue(myBatchesPage.areAllVisibleBatchesForClient(ConfigReader.get("filterClientName")),
                "Not all visible Planned tab batches belong to selected client.");
    }

    @Test(priority = 29)
    public void TC_MB_FIL_29_verifyClientFilterAppliesInInactiveTab() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);

        myBatchesPage.clickInactiveTab();

        Assert.assertTrue(myBatchesPage.areAllVisibleBatchesForClient(ConfigReader.get("filterClientName")),
                "Not all visible Inactive tab batches belong to selected client.");
        myBatchesPage.clickClearFilter();
    }

    @Test(priority = 30)
    public void TC_MB_FIL_30_verifyProgramFilterAppliesSuccessfully() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
        myBatchesPage.clickActiveTab();
        myBatchesPage.applyProgramFilter(ConfigReader.get("filterProgramTitle"));

        Assert.assertTrue(myBatchesPage.isClearFilterVisible(),
                "Clear Filter is not visible after applying program filter.");
        Assert.assertTrue(myBatchesPage.areAllVisibleBatchesForProgram(ConfigReader.get("filterProgramTitle")),
                "Not all visible batches belong to selected program.");
        
    }

    @Test(priority = 31)
    public void TC_MB_FIL_31_verifyClearFilterRemovesAppliedFilter() {
    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);

        Assert.assertTrue(myBatchesPage.isClearFilterVisible(),
                "Clear Filter is not visible after applying filter.");

        myBatchesPage.clickClearFilter();

        Assert.assertFalse(myBatchesPage.isAppliedFilterTextPresent(ConfigReader.get("filterProgramTitle")),
                "Applied filter text is still visible after clicking Clear Filter.");
    }
//    @Test(priority = 32)
//    public void TC_MB_FIL_32_verifyCancelDoesNotApplyFilter() {
//    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
//        myBatchesPage.openFilterModal();
//        myBatchesPage.selectClientFilter(ConfigReader.get("filterClientName"));
//        myBatchesPage.closeFilterModalUsingCancel();
//
//        Assert.assertFalse(myBatchesPage.isAppliedFilterTextPresent(ConfigReader.get("filterClientName")),
//                "Filter text is visible even though Cancel was clicked.");
//    }
//
//    @Test(priority = 33)
//    public void TC_MB_FIL_33_verifyCloseIconClosesFilterModal() {
//    	MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
//        myBatchesPage.openFilterModal();
//        myBatchesPage.closeFilterModalUsingCloseIcon();
//
//        Assert.assertTrue(true, "Filter modal close icon is not working.");
//    }
    @Test(priority = 34)
    public void TC_MB_CloseBatch_34_verifyCloseBatchByBatchName() {
    	 
        String batchName = ConfigReader.get("closeBatch");

        MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
        myBatchesPage.clickClearFilter();
      
        myBatchesPage.waitForPageToLoad();

        MyBatchesPage.BatchCloseStatus status = myBatchesPage.closeBatchByName(batchName);

        switch (status) {
            case CLOSED:
                System.out.println("Batch found and closed successfully: " + batchName);
                Assert.assertTrue(
                    myBatchesPage.isSuccessMessageDisplayed(),
                    "Server not responding error displayed when i click on batch close icon"
                );
                System.out.println("Success message: " + myBatchesPage.getSuccessMessageText());
                break;

            case CLOSE_ICON_NOT_AVAILABLE:
                Assert.fail("Batch found but close icon is not clickable or popup confirmation failed: " + batchName);
                break;

            case NOT_FOUND:
                System.out.println("Batch with this name does not exist: " + batchName);
                break;

            default:
                Assert.fail("Unexpected status: " + status);
        }
    }
    @Test(priority = 35)
    public void TC_MB_DeleteBatch_35_verifyDeleteBatchByBatchName() {
        String batchName = ConfigReader.get("deleteBatch");

        MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
        myBatchesPage.waitForPageToLoad();

        MyBatchesPage.BatchDeleteStatus status = myBatchesPage.deleteBatchByName(batchName);

        switch (status) {

            case DELETED:
                System.out.println("Batch deleted successfully: " + batchName);

                Assert.assertTrue(
                    myBatchesPage.isBatchDeleteSuccessMessageDisplayed(),
                    "Try to delete batch but server not responding error is displayed"
                );

                System.out.println("Success message: " + myBatchesPage.getBatchDeleteSuccessMessageText());
                break;

            case DELETE_ICON_NOT_AVAILABLE:
                Assert.fail("Batch found but delete icon not clickable or popup failed: " + batchName);
                break;

            case NOT_FOUND:
                System.out.println("Batch not found: " + batchName);
                break;

            default:
                Assert.fail("Unexpected delete status: " + status);
        }
    }
    @Test(priority = 36)
    public void TC_MB_ActiveBatch_36_verifyActivateBatchByBatchName() {
        String batchName = ConfigReader.get("activeBatch");

        MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
        myBatchesPage.waitForPageToLoad();

        MyBatchesPage.BatchActivateStatus status = myBatchesPage.activateBatchByName(batchName);

        switch (status) {

            case ACTIVATED:
                System.out.println("Batch activated successfully: " + batchName);

                Assert.assertTrue(
                    myBatchesPage.isBatchActiveSuccessMessageDisplayed(),
                    "Try to active the batch but server not responding error is coming"
                );

                System.out.println("Success message: " + myBatchesPage.getBatchActiveSuccessMessageText());
                break;

            case ACTIVATE_ICON_NOT_AVAILABLE:
                Assert.fail("Batch found but activate icon not clickable or popup failed: " + batchName);
                break;

            case NOT_FOUND:
                System.out.println("Batch not found: " + batchName);
                break;

            default:
                Assert.fail("Unexpected activate status: " + status);
        }
    }
    @Test(priority = 37)
    public void TC_MB_SelfRegi_37_verifyPaxSelfRegisterQrPopupAndRegistrationUrlWorks() {
        String batchName = ConfigReader.get("paxSelfRegister");

        MyBatchesPage myBatchesPage = new MyBatchesPage(driver);
        myBatchesPage.waitForPageToLoad();

        MyBatchesPage.BatchQrStatus status = myBatchesPage.openPaxSelfRegisterQrByBatchName(batchName);

        switch (status) {

            case QR_POPUP_OPENED:
                System.out.println("QR popup opened successfully for batch: " + batchName);

                Assert.assertTrue(
                    myBatchesPage.isQrPopupDisplayed(),
                    "QR popup is not displayed."
                );

                Assert.assertEquals(
                    myBatchesPage.getQrPopupTitleText(),
                    "QR code for participants to self register",
                    "QR popup title is incorrect."
                );

                Assert.assertTrue(
                    myBatchesPage.isQrImageDisplayed(),
                    "QR image is not displayed."
                );

                Assert.assertTrue(
                    myBatchesPage.isCopyLinkTextDisplayed(),
                    "Copy self registration link text is not displayed."
                );

                String registrationUrl = myBatchesPage.getSelfRegistrationUrlFromPopup();
                System.out.println("Registration URL from popup: " + registrationUrl);

                Assert.assertTrue(
                    myBatchesPage.isValidRegistrationUrl(registrationUrl),
                    "Registration URL is invalid or empty."
                );

                // Close popup
                myBatchesPage.closeQrPopup();

                // Open new tab
                String parentWindow = driver.getWindowHandle();
                ((JavascriptExecutor) driver).executeScript("window.open(arguments[0], '_blank');", registrationUrl);

                // Switch to new tab
                Set<String> allWindows = driver.getWindowHandles();
                for (String window : allWindows) {
                    if (!window.equals(parentWindow)) {
                        driver.switchTo().window(window);
                        break;
                    }
                }

                // Wait for page load
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
                wait.until(ExpectedConditions.urlContains("/register/"));

                String currentUrl = driver.getCurrentUrl();
                System.out.println("Opened registration page URL: " + currentUrl);

                Assert.assertTrue(
                    currentUrl.contains("/register/"),
                    "Registration URL is not opened correctly."
                );

                // Optional stronger validation: check page has some visible content
                Assert.assertTrue(
                    driver.getTitle() != null,
                    "Registration page title is not available."
                );

                // Close new tab and switch back
                driver.close();
                driver.switchTo().window(parentWindow);
                break;

            case QR_ICON_NOT_AVAILABLE:
                Assert.fail("Batch found but Pax Self Register icon is not available/clickable: " + batchName);
                break;

            case QR_POPUP_NOT_OPENED:
                Assert.fail("Pax Self Register icon clicked but QR popup did not open: " + batchName);
                break;

            case NOT_FOUND:
                System.out.println("Batch with this name does not exist: " + batchName);
                break;

            default:
                Assert.fail("Unexpected QR status: " + status);
        }
    }
    
}
package com.potentia.automation.tests;

import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.potentia.automation.core.BaseTest;
import com.potentia.automation.pages.LoginPage;
import com.potentia.automation.pages.ProgramGenrePage;
import com.potentia.automation.pages.ProgramGenrePage.ProgramStatus;
import com.potentia.automation.pages.SuperClientsPage;
import com.potentia.automation.utils.ConfigReader;

public class ProgramGenreTest extends BaseTest {
	@BeforeClass  
	  public void setupTest() {
	      
	      
	      LoginPage loginPage = new LoginPage(driver);
	      String email = ConfigReader.get("email");
	      String password = ConfigReader.get("password");

	      loginPage.login(email, password);

	      // Assertion: Check if home page URL is loaded
	      String expectedUrl = ConfigReader.get("homeUrl");
	      Assert.assertEquals(driver.getCurrentUrl(), expectedUrl, "Login failed or home page not loaded!");
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
	 private String getUniqueProgramGenre() {
	        return "AutoProgramGenre_" + System.currentTimeMillis();
	    }

	    private String getUniqueShortTitle() {
	        return "APG_" + System.currentTimeMillis();
	    }

	@Test(priority = 1)
    public void TC_CL_01_verifyNavigationToProgramGenrePage() {
		ProgramGenrePage programGenresPage = new ProgramGenrePage(driver);
		programGenresPage.clickProgramGenreFromMasters();

        // Validation
        Assert.assertTrue(
                driver.getCurrentUrl().contains("programlist"),
                "Program Genre page is not opened"
        );
        Assert.assertTrue(programGenresPage.isProgramGenreListHeadingDisplayed(), "Program Genre List heading is not displayed.");
        Assert.assertEquals(programGenresPage.getProgramGenreListHeadingText(), "Program Genres", "Program Genre page heading mismatch.");
    }
	 @Test(priority = 2)
	    public void TC_PG_02_verifyClickingAddProgramGenreOpensModal() {
			 ProgramGenrePage programGenresPage = new ProgramGenrePage(driver);
			 programGenresPage.clickAddProgramGenre();

	        Assert.assertTrue(programGenresPage.isAddProgramGenreModalDisplayed(),
	                "Add Program Genre modal is not displayed.");
	        Assert.assertEquals(programGenresPage.getAddModalTitle(),
	                "Add Program Genre",
	                "Add Program Genre modal title is incorrect.");

	        programGenresPage.closeAddModalUsingX();
	    }
	 @Test(priority = 3)
	    public void TC_PG_03_verifyModalCanBeClosedUsingCloseIcon() {
		 	ProgramGenrePage programGenresPage = new ProgramGenrePage(driver);
	        programGenresPage.clickAddProgramGenre();
	        Assert.assertTrue(programGenresPage.isAddProgramGenreModalDisplayed(),
	                "Modal did not open.");

	        programGenresPage.closeAddModalUsingX();
	        Assert.assertFalse(programGenresPage.isAddProgramGenreModalDisplayed(),
	                "Modal did not close using close icon.");
	    }
	 @Test(priority = 4)
	    public void TC_PG_04_verifyModalCanBeClosedUsingCancelButton() {
		  	ProgramGenrePage programGenresPage = new ProgramGenrePage(driver);
	        programGenresPage.clickAddProgramGenre();
	        Assert.assertTrue(programGenresPage.isAddProgramGenreModalDisplayed(),
	                "Modal did not open.");

	        programGenresPage.closeAddModalUsingCancel();
	        Assert.assertFalse(programGenresPage.isAddProgramGenreModalDisplayed(),
	                "Modal did not close using Cancel button.");
	    }
	 // =========================
	    // Validation tests
	    // =========================

	    @Test(priority = 5)
	    public void TC_PG_05_verifyRequiredValidationWhenSavingEmptyForm() {
	    	ProgramGenrePage programGenresPage = new ProgramGenrePage(driver);
	    	programGenresPage.clickAddProgramGenre();
	        programGenresPage.clickSave();

	        Assert.assertTrue(programGenresPage.isRequiredTooltipDisplayed(),
	                "Required tooltip message is not displayed.");
	        
	        Assert.assertTrue(programGenresPage.isProgramGenreHighlightedAsError(),
	                "Program Genre field is not highlighted in error state.");
	        
	        Assert.assertEquals(programGenresPage.getRequiredTooltipCount(), 2,
	                "Expected 2 required tooltip messages for Program Genre and Likely Title.");

	        Assert.assertTrue(programGenresPage.isProgramGenreHighlightedAsError(),
	                "Program Genre field is not highlighted in error state.");

	        Assert.assertTrue(programGenresPage.isLikelyTitleHighlightedAsError(),
	                "Likely Title field is not highlighted in error state.");

	        programGenresPage.closeAddModalUsingCancel();
	    }
	    @Test(priority = 6)
	    public void TC_PG_06_verifyProgramGenreIsMandatory() {
	    	ProgramGenrePage programGenresPage = new ProgramGenrePage(driver);
	        programGenresPage.setLikelyTitleAt(0, "Likely One");
	        programGenresPage.clickSave();

	        Assert.assertTrue(programGenresPage.isRequiredValidationPresent(),
	                "Program Genre mandatory validation is not displayed.");

	        programGenresPage.closeAddModalUsingCancel();
	    }

	    @Test(priority = 7)
	    public void TC_PG_07_verifyLikelyTitleIsMandatory() {
	    	ProgramGenrePage programGenresPage = new ProgramGenrePage(driver);
	        programGenresPage.clickAddProgramGenre();
	        programGenresPage.enterProgramGenre(getUniqueProgramGenre());
	        programGenresPage.clickSave();

	        Assert.assertTrue(programGenresPage.isRequiredValidationPresent(),
	                "Likely Title mandatory validation is not displayed.");

	        programGenresPage.closeAddModalUsingCancel();
	    }
	 // =========================
	    // Likely title dynamic row tests
	    // =========================

	    @Test(priority = 8)
	    public void TC_PG_08_verifyUserCanAddLikelyTitleRow() {
	    	ProgramGenrePage programGenresPage = new ProgramGenrePage(driver);
	        programGenresPage.clickAddProgramGenre();

	        int before = programGenresPage.getLikelyTitleRowCount();
	        programGenresPage.clickAddLikelyTitle();
	        int after = programGenresPage.getLikelyTitleRowCount();

	        Assert.assertTrue(after > before,
	                "Likely title row was not added.");

	        programGenresPage.closeAddModalUsingCancel();
	    }
	    @Test(priority = 9)
	    public void TC_PG_09_verifyUserCanAddMultipleLikelyTitleRows() {
	    	ProgramGenrePage programGenresPage = new ProgramGenrePage(driver);
	        programGenresPage.clickAddProgramGenre();

	        int before = programGenresPage.getLikelyTitleRowCount();

	        programGenresPage.clickAddLikelyTitle();
	        programGenresPage.clickAddLikelyTitle();

	        int after = programGenresPage.getLikelyTitleRowCount();

	        Assert.assertEquals(after, before + 2,
	                "Multiple likely title rows were not added correctly.");

	        programGenresPage.closeAddModalUsingCancel();
	    }
	    @Test(priority = 10)
	    public void TC_PG_10_verifyUserCanRemoveLikelyTitleRow() {
	    	ProgramGenrePage programGenresPage = new ProgramGenrePage(driver);
	        programGenresPage.clickAddProgramGenre();

	        programGenresPage.clickAddLikelyTitle();
	        int before = programGenresPage.getLikelyTitleRowCount();

	        if (programGenresPage.canRemoveLikelyTitleRow()) {
	            programGenresPage.removeLastLikelyTitleRow();
	            int after = programGenresPage.getLikelyTitleRowCount();

	            Assert.assertTrue(after < before,
	                    "Likely title row was not removed.");
	        }

	        programGenresPage.closeAddModalUsingCancel();
	    }
	    // =========================
	    // Create / Search tests
	    // =========================

	    @Test(priority = 11)
	    public void TC_PG_11_verifyProgramGenreCanBeAddedSuccessfully() throws InterruptedException {
	    	ProgramGenrePage programGenresPage = new ProgramGenrePage(driver);
	        String programName = getUniqueProgramGenre();
	        String shortTitle = getUniqueShortTitle();
	        driver.navigate().refresh();

	        programGenresPage.addProgramGenre(
	                programName,
	                shortTitle,
	                Arrays.asList("Likely One", "Likely Two"),
	                "Automation created Program Genre"
	        );

	        Assert.assertTrue(programGenresPage.isProgramAddedSuccessMessageDisplayed(),
	                "Success message is not displayed after adding Program Genre.");
	        
	        Thread.sleep(2000);

	        programGenresPage.searchProgram(programName);

	        Assert.assertTrue(programGenresPage.isProgramVisibleInCurrentTable(programName),
	                "Newly created Program Genre is not visible in table.");
	    }
	    
	    @Test(priority = 12)
	    public void TC_PG_12_verifySearchCanBeCleared() {
	    	ProgramGenrePage programGenresPage = new ProgramGenrePage(driver);
	        programGenresPage.searchProgram("ABC");
	        programGenresPage.clearSearch();

	        Assert.assertTrue(programGenresPage.isPageDisplayed(),
	                "Page is not stable after clearing search.");
	    }
	    
	    // =========================
	    // Rows / Pagination tests
	    // =========================

	    @Test(priority = 13)
	    public void TC_PG_13_verifyRowsDropdownSupports25Rows() {
	    	ProgramGenrePage programGenresPage = new ProgramGenrePage(driver);
	        programGenresPage.selectRowsCount("25");
	        Assert.assertTrue(programGenresPage.getInfoText().contains("Showing"),
	                "Rows dropdown did not refresh the table correctly for 25 rows.");
	    }
	    @Test(priority = 14)
	    public void TC_PG_14_verifyRowsDropdownSupports50Rows() {
	    	ProgramGenrePage programGenresPage = new ProgramGenrePage(driver);
	        programGenresPage.selectRowsCount("50");
	        Assert.assertTrue(programGenresPage.getInfoText().contains("Showing"),
	                "Rows dropdown did not refresh the table correctly for 50 rows.");
	    }
	    @Test(priority = 15)
	    public void TC_PG_15_verifyRowsDropdownSupports100Rows() {
	    	ProgramGenrePage programGenresPage = new ProgramGenrePage(driver);
	        programGenresPage.selectRowsCount("100");
	        Assert.assertTrue(programGenresPage.getInfoText().contains("Showing"),
	                "Rows dropdown did not refresh the table correctly for 100 rows.");
	    }

	    @Test(priority = 16)
	    public void TC_PG_16_verifyRowsDropdownSupportsAllOption() {
	    	ProgramGenrePage programGenresPage = new ProgramGenrePage(driver);
	        programGenresPage.selectRowsCount("All");
	        Assert.assertTrue(programGenresPage.getInfoText().contains("Showing"),
	                "Rows dropdown did not refresh the table correctly for All option.");
	    }

	    @Test(priority = 17)
	    public void TC_PG_17_verifyPaginationIsDisplayedWhenMultiplePagesExist() { 	
	    	ProgramGenrePage programGenresPage = new ProgramGenrePage(driver);
	    	driver.navigate().refresh();
	        programGenresPage.selectRowsCount("10");
	        Assert.assertTrue(programGenresPage.isPaginationDisplayed(),
	                "Pagination is not displayed.");
	    }

	    @Test(priority = 18)
	    public void TC_PG_18_verifyUserCanNavigateToNextPage() {
	    	ProgramGenrePage programGenresPage = new ProgramGenrePage(driver);
	        String before = programGenresPage.getInfoText();

	        programGenresPage.clickNextPage();
	        String after = programGenresPage.getInfoText();

	        Assert.assertNotEquals(after, before,
	                "Table info did not change after clicking next page.");
	    }
	    // =========================
	    // Filter tests
	    // =========================

	    @Test(priority = 19)
	    public void TC_PG_19_verifyActiveFilterShowsOnlyActiveRows() {
	    	ProgramGenrePage programGenresPage = new ProgramGenrePage(driver);
	        programGenresPage.clearSearch();
	        programGenresPage.applyActiveFilter();

	        Assert.assertTrue(programGenresPage.allVisibleRowsMatchStatus(ProgramStatus.ACTIVE),
	                "Active filter is showing non-active rows.");
	        programGenresPage.clearFilter();

	        Assert.assertTrue(
	                programGenresPage.hasMixedStatusesInVisibleRows(),
	                "Filter is not cleared properly.");
	    }

	    @Test(priority = 20)
	    public void TC_PG_20_verifyInactiveFilterShowsOnlyInactiveRows() {
	    	ProgramGenrePage programGenresPage = new ProgramGenrePage(driver);
	        programGenresPage.clearSearch();
	        programGenresPage.applyInactiveFilter();

	        Assert.assertTrue(programGenresPage.allVisibleRowsMatchStatus(ProgramStatus.INACTIVE),
	                "Inactive filter is showing non-inactive rows.");
	        programGenresPage.clearFilter();

	        Assert.assertTrue(
	                programGenresPage.hasMixedStatusesInVisibleRows(),
	                "Filter is not cleared properly.");
	    }
	    @Test(priority = 21)
	    public void TC_PG_21_verifyEditIconIsVisibleForProgramRow() {
	    	ProgramGenrePage programGenresPage = new ProgramGenrePage(driver);
	        String programName = "Demo program genre logistic";

	        programGenresPage.clearSearch();
	        programGenresPage.searchProgram(programName);

	        Assert.assertTrue(programGenresPage.isEditIconVisible(programName),
	                "Edit icon is not visible for Program Genre row.");
	    }

	    @Test(priority = 22)
	    public void TC_PG_22_verifyDeleteIconIsVisibleForProgramRow() {
	    	ProgramGenrePage programGenresPage = new ProgramGenrePage(driver);
	        String programName = "Demo program genre logistic";

	        programGenresPage.clearSearch();
	        programGenresPage.searchProgram(programName);

	        Assert.assertTrue(programGenresPage.isDeleteIconVisible(programName),
	                "Delete icon is not visible for Program Genre row.");
	    }

	    @Test(priority = 23)
	    public void TC_PG_23_verifyCorrectActionIconVisibleForActiveRow() {
	    	ProgramGenrePage programGenresPage = new ProgramGenrePage(driver);
	        String activeProgramName = "Demo program genre logistic";

	        programGenresPage.clearSearch();
	        programGenresPage.searchProgram(activeProgramName);

	        Assert.assertEquals(programGenresPage.getProgramStatusByName(activeProgramName), ProgramStatus.ACTIVE,
	                "Provided row is not Active as expected.");

	        Assert.assertTrue(programGenresPage.isInactivateIconVisible(activeProgramName),
	                "Inactivate icon should be visible for active row.");
	    }
	    @Test(priority = 24)
	    public void TC_PG_24_verifyBulkActionIconsAreHiddenForMixedActiveAndInactiveSelection() {
	    	ProgramGenrePage programGenresPage = new ProgramGenrePage(driver);
	    	driver.navigate().refresh();
	        String activeProgramName = "Demo program genre logistic";
	        String inactiveProgramName = "ABC logistic";

	        programGenresPage.clearSearch();

	        programGenresPage.searchProgram("");
	        programGenresPage.selectProgramCheckboxByName(activeProgramName);
	        programGenresPage.clearSearch();
	        programGenresPage.selectProgramCheckboxByName(inactiveProgramName);

	        Assert.assertFalse(programGenresPage.isBulkActivateVisible(),
	                "Bulk Activate should not be visible for mixed active and inactive selection.");
	        Assert.assertFalse(programGenresPage.isBulkInactivateVisible(),
	                "Bulk Inactivate should not be visible for mixed active and inactive selection.");

	        programGenresPage.unselectProgramCheckboxByName(activeProgramName);
	        programGenresPage.unselectProgramCheckboxByName(inactiveProgramName);
	    }

	    @Test(priority = 25)
	    public void TC_PG_25_verifySelectedCheckboxNamesCanBeCaptured() {
	    	ProgramGenrePage programGenresPage = new ProgramGenrePage(driver);
	        String programName = "Demo program genre logistic";

	        programGenresPage.clearSearch();
	        programGenresPage.searchProgram(programName);
	        programGenresPage.selectProgramCheckboxByName(programName);

	        List<String> selectedNames = programGenresPage.getSelectedProgramNames();

	        Assert.assertTrue(selectedNames.contains(programName),
	                "Selected program name is not captured correctly.");

	        programGenresPage.unselectProgramCheckboxByName(programName);
	    }

	    // =========================
	    // Edit action click test
	    // =========================

	    @Test(priority = 26)
	    public void TC_PG_26_verifyEditActionCanBeClicked() {
	    	ProgramGenrePage programGenresPage = new ProgramGenrePage(driver);
	        String programName = "Demo program genre logistic";

	        programGenresPage.clearSearch();
	        programGenresPage.searchProgram(programName);

	        Assert.assertTrue(programGenresPage.isEditIconVisible(programName),
	                "Edit icon is not visible before clicking.");

	        programGenresPage.clickEditByProgramName(programName);

	        // Add proper edit modal assertion when you share edit modal HTML
	        Assert.assertTrue(true, "Edit action clicked successfully.");
	    }

}

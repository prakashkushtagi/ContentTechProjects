package com.mytime.qa.testcases;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.mytime.qa.base.TestBase;
import com.mytime.qa.pages.DailyViewPage;
import com.mytime.qa.pages.HomePage;
import com.mytime.qa.pages.LoginPage;
import com.mytime.qa.pages.WeeklyViewPage;
import com.mytime.qa.util.TestUtil;

public class WeeklyPageTest extends TestBase {

	LoginPage loginPage;
	HomePage homePage;
	TestUtil testUtil;
	WeeklyViewPage weeklyViewPage = new WeeklyViewPage();

	public WeeklyPageTest() {
		super();
	}

	@BeforeMethod
	public void setUp() throws InterruptedException{
		initialization();
		testUtil = new TestUtil();
		loginPage = new LoginPage();
		homePage = loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
	}

/*	@Test(priority = 1)
	public void verifyWeeklyViewLablePage() {
		weeklyViewPage = homePage.verifyWeeklyViewLableLink();
	}*/
	
	@Test(priority = 2)

	public void ClearMaintenanceLTIOSupportTest(){
		weeklyViewPage = homePage.verifyWeeklyViewLableLink();
		weeklyViewPage.ProjectName();
		weeklyViewPage.TaskName();
		
	}

/*	@AfterMethod
	public void tearDown() {
		driver.quit();
	}*/

}

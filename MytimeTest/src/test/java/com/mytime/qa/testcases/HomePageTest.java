package com.mytime.qa.testcases;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.mytime.qa.base.TestBase;
import com.mytime.qa.pages.DailyViewPage;
import com.mytime.qa.pages.HomePage;
import com.mytime.qa.pages.LoginPage;
import com.mytime.qa.pages.WeeklyViewPage;
import com.mytime.qa.util.TestUtil;

public class HomePageTest extends TestBase{
	LoginPage loginPage;
	HomePage homePage;
	TestUtil testUtil;
	DailyViewPage dailyviewPage;
	WeeklyViewPage weeklyViewPage;
	
	public HomePageTest() {
		super();
	}
	
	@BeforeMethod
	public void setUp() throws InterruptedException {
		initialization();
		testUtil = new TestUtil();
		loginPage = new LoginPage();
		homePage = loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
	}
	
	@Test(priority=1)
	public void verifyHomePageTitleTest() {
		String homePageTitle = homePage.verifyHomePageTitle();
		Assert.assertEquals(homePageTitle, "MyTime","Home page title not matched");
	}
	
	@Test(priority=2)
	public void verifyCorrectUserName() {
		Assert.assertTrue(homePage.verifyCorrectUserName());
	}
	
	@Test(priority=3)
	public void verifyDailyViewLablePage() {
		dailyviewPage = homePage.verifyDailyViewLableLink();
	}
	
	@Test(priority=4)
	public void verifyWeeklyViewLablePage() {
		weeklyViewPage = homePage.verifyWeeklyViewLableLink();
	}
	
	@AfterMethod
	public void tearDown() {
		driver.quit();
	}	
}

package com.mytime.qa.testcases;

import org.testng.annotations.BeforeMethod;

import com.mytime.qa.base.TestBase;
import com.mytime.qa.pages.DailyViewPage;
import com.mytime.qa.pages.HomePage;
import com.mytime.qa.pages.LoginPage;
import com.mytime.qa.pages.WeeklyViewPage;
import com.mytime.qa.util.TestUtil;

public class DailyPageTest extends TestBase{

	LoginPage loginPage;
	HomePage homePage;
	TestUtil testUtil;
	DailyViewPage dailyviewPage;
	WeeklyViewPage weeklyViewPage;
	
	public DailyPageTest() {
		super();
	}
	
	@BeforeMethod
	public void setUp() throws InterruptedException {
		initialization();
		testUtil = new TestUtil();
		loginPage = new LoginPage();
		homePage = loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
	}
}

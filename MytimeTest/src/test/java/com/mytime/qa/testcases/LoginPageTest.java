package com.mytime.qa.testcases;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.mytime.qa.base.TestBase;
import com.mytime.qa.pages.HomePage;
import com.mytime.qa.pages.LoginPage;

public class LoginPageTest extends TestBase{

	LoginPage loginPage;
	HomePage homePage;
	
	public LoginPageTest() {
		super();
	}
	
	@BeforeMethod
	public void setUp() {
		initialization();
		loginPage = new LoginPage();
	}
	
	@Test(priority=2)
	public void loginPageTitleTest() {
		String Title = loginPage.validateLoginPageTitle();
		Assert.assertEquals(Title, "MyTime redirect");
	}
	
	@Test(priority=1)
	public void MytimeLogoTest() {
		boolean flag = loginPage.validatemytimeImage();
		Assert.assertTrue(flag);
	}
	
	@Test(priority=1)
	public void loginTest() throws InterruptedException {
		homePage = loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
	}
	
	@AfterMethod
	public void tearDown() {
		driver.quit();
	}
}

package com.mytime.qa.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.mytime.qa.base.TestBase;

public class LoginPage extends TestBase{

	
	//Page Factory - OR:
	@FindBy(name="USER")
	WebElement username;
	
	@FindBy(name="PASSWORD")
	WebElement password;
	
	@FindBy(xpath="//button[@class='button safeLoginbtn']")
	WebElement loginBtn;
	
	@FindBy(xpath="//span[@class='safe-logo']")
	WebElement mytimeLogo;
	
	//Initializing the Page Objects:
	
	public LoginPage() {
		PageFactory.initElements(driver, this);
	}
	
	//Actions
	public String validateLoginPageTitle() {
		return driver.getTitle();
	}
	
	public boolean validatemytimeImage() {
		return mytimeLogo.isDisplayed();
	}
	
	
	public HomePage login(String un, String pwd) throws InterruptedException {
		username.sendKeys(un);
		password.sendKeys(pwd);
		loginBtn.click();
		
		return new HomePage();
	}
	
}

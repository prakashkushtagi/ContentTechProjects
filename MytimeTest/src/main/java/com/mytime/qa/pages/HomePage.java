package com.mytime.qa.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.mytime.qa.base.TestBase;

public class HomePage extends TestBase{
	
	@FindBy(xpath = "//p[@class='download']")
	WebElement userNameLable;
	
	@FindBy(xpath= "//span[contains (text(),'Daily View')]")
	WebElement dailyViewLable;

	@FindBy(xpath= "//span[contains (text(),'Weekly View')]")
	WebElement weeklyViewLable;
	
	@FindBy(xpath= "//span[contains (text(),'Reports')]")
	WebElement reportViewLable;
	
	@FindBy(xpath= "//span[contains (text(),'Preferences')]")
	WebElement preferencesViewLable;
	
	@FindBy(xpath= "//span[contains (text(),'Links')]")
	WebElement linksViewLable;
	
	@FindBy(xpath= "//span[contains (text(),'Help')]")
	WebElement helpViewLable;
	
	// Initializing the Page Objects:
	public HomePage() {
		PageFactory.initElements(driver, this);
	}
	
	public String verifyHomePageTitle() {
		return driver.getTitle();
	}
	
	public boolean verifyCorrectUserName() {
		return userNameLable.isDisplayed();
	}
	
	public DailyViewPage verifyDailyViewLableLink() {
		dailyViewLable.click();
		return new DailyViewPage();
	}
	
	public WeeklyViewPage verifyWeeklyViewLableLink() {
		weeklyViewLable.click();
		return new WeeklyViewPage();
	}
	
	public boolean reportViewLableLink() {
		return reportViewLable.isDisplayed();
	}
	
	public boolean preferencesViewLableLink() {
		return preferencesViewLable.isDisplayed();
	}
	
	public boolean linksViewLableLink() {
		return linksViewLable.isDisplayed();
	}
	
	public boolean helpViewLableLink() {
		return helpViewLable.isDisplayed();
	}
}

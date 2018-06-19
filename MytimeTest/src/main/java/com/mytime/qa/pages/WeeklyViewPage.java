package com.mytime.qa.pages;

import java.util.List;

import org.eclipse.jetty.util.thread.Scheduler.Task;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.mytime.qa.base.TestBase;

public class WeeklyViewPage extends TestBase {

	@FindBy(xpath = "//*[@id='sarea2']/div[2]/a")
	WebElement task;
	
	@FindBy(xpath = "/html/body/div[5]/p/a")
	WebElement taskValues;

	@FindBy(xpath = "//p[@onclick='dont_show_warning=true;']//img[@id='mythomsonlogo']")
	WebElement saveBtn;

	public WeeklyViewPage() {
		PageFactory.initElements(driver, this);
	}

	
	public void ProjectName(){
		
		//Select select = new Select(projectName);

		//WebElement projectName1 = driver.findElement(By.id("selProjects"));

		//Select Select = new Select(driver.findElement(By.id("selProjects")));
		
		//Select.selectByVisibleText("1030484: Clear Maintenance - LTIO Support");
		
		driver.findElement(By.xpath("//*[@id='sarea1']/div[2]/a")).click();
		
		System.out.println(driver.findElement(By.xpath("//*[@id='selProjects']")).getSize());		
		
		driver.findElement(By.xpath("//*[@id='optionsDiv1']/p[1]/a")).click();;
		
	}
	
	public void TaskName() {
		
		task.click();
		taskValues.click();
		saveBtn.click();
	}
	
	
}

package testcases;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.github.javafaker.Faker;

import utilis.BaseClass;

public class TestCase2 extends BaseClass
{
	@Test
	public void review()
	{
		try {
			test=extent.createTest("Comparing product price in differnt sites").assignAuthor("Bharath Reddy");
	
			driver.get(configreader.getProperty("url1"));
			test.log(Status.INFO, "Navigate to TripAdvisor Application");
			
			driver.findElement(By.xpath("//input[@name='q']")).sendKeys(configreader.getProperty("searchQuery1"));
			test.log(Status.INFO, "entered data to be searched");
			driver.findElement(By.xpath("//input[@name='q']")).sendKeys(Keys.ENTER);
			test.log(Status.INFO, "click on serch button");
			
			List<WebElement> hotels=driver.findElements(By.xpath("//*[@class='IJFFy']//a[contains(text(),'Club Mahindra')]"));
			hotels.get(0).click();
			test.log(Status.INFO, "select and navigate to first appearence of hotel list");
			
			Set<String> childs = driver.getWindowHandles();
			Iterator<String> iterator = childs.iterator();
			while (iterator.hasNext()) 
			{
			    String child = iterator.next();
			    driver.switchTo().window(child);
			    break; // switches to the first available child window
			}
			
			WebElement review =driver.findElement(By.xpath("//span[text()='Write a review']"));
			action.moveToElement(review);
			review.click();
			
			Set<String> childs1 = driver.getWindowHandles();
			Iterator<String> iterator1 = childs1.iterator();
			while (iterator1.hasNext()) 
			{
			    String child1 = iterator.next();
			    driver.switchTo().window(child1);
			    break; // switches to the first available child window
			}
			
			driver.findElement(By.xpath("(//*[@class='yGxQr'])[5]")).click(); // select on 5th star  from review section
			driver.findElement(By.xpath("//span[text()='Select one']")).click();
			List<WebElement> months = driver.findElements(By.xpath("//*[@id=':lithium-R99qbqauen9:']//span/span"));
			months.get(2).click();
			driver.findElement(By.xpath("//span[text()='Business']")).click();
			Faker faker=new Faker();
			driver.findElement(By.xpath("//textarea")).sendKeys(faker.lorem().paragraph(2));
			driver.findElement(By.id("review-title")).sendKeys(faker.lorem().sentence());
			
			driver.findElement(By.name(":lithium-R5tqbqauen9:")).click();
			driver.findElement(By.xpath("//*[text()='Continue']")).click();
			
			//UPTO REVIEW SUBMISSION
			
		}
		catch(Exception e)
		{
			screenshot("testcase2");
			test.log(Status.FAIL,e.getMessage());
		}
	}
}

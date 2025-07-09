package testcases;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;
import utilis.BaseClass;

public class TestCase1 extends BaseClass
{
	@Test
	public void comparePrices()
	{
		try {
		test=extent.createTest("Comparing product price in differnt sites").assignAuthor("Bharath Reddy");

		driver.get(configreader.getProperty("url01"));
		test.log(Status.INFO, "Navigate to Amazon Application");
		
		driver.findElement(By.id("twotabsearchtextbox")).sendKeys(configreader.getProperty("searchProduct01"));
		test.log(Status.INFO, "entered data to be searched");
		
		driver.findElement(By.id("nav-search-submit-button")).click();
		test.log(Status.INFO, "click on serch icon");
		
		List<WebElement> phoneTitles = driver.findElements(By.xpath("//*[@class='puisg-row']//a/h2/span"));
		Boolean productFound =false;
		for( int i=0; i<phoneTitles.size();i++)
		{
			if(phoneTitles.get(i).getText().contains(configreader.getProperty("searchProduct01")))
			{
				test.log(Status.PASS,"Searched product is found"+phoneTitles.get(i).getText());
				phoneTitles.get(i).click();
				test.log(Status.INFO,"Navigated to Searched product");
				productFound =true;
				break;
			}
		}
		Assert.assertTrue(productFound);
		test.log(Status.INFO, "searched product varient is  displaced");

		String AmazonPrice = driver.findElement(By.xpath("//*[@id='centerCol']//*[@id='apex_desktop']//span[@class='a-price-whole']")).getText();
		Double amazonPrice = Double.parseDouble(AmazonPrice.replace(",", ""));
		test.log(Status.INFO, "Amazon price for Searched Product is : "+amazonPrice);
		
		// ---------------------- Flipkart -------------------------
		driver.get(configreader.getProperty("url02"));
		test.log(Status.INFO, "Navigate to Flipkart Application");
		
		driver.findElement(By.xpath("//*[@class='Pke_EE']")).sendKeys(configreader.getProperty("searchProduct01"));
		test.log(Status.INFO, "entered data to be searched");
		
		driver.findElement(By.xpath("//*[@class='_2iLD__']")).click();
		test.log(Status.INFO, "click on serch icon");
		
		List<WebElement> phoneTitles1 = driver.findElements(By.xpath("//*[@class='KzDlHZ']"));
		boolean productFound1 = false;
		for (int i = 0; i < phoneTitles1.size(); i++) 
		{
		    String[] keywords = configreader.getProperty("searchProduct01").replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase().split("\\s+");
		    String productText = phoneTitles1.get(i).getText().replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase();
		    boolean allMatch = true;
		    for (String keyword : keywords) 
		    {
		        if (!productText.contains(keyword)) 
		        {
		            allMatch = false;
		            break;
		        }
		    }
		    if (allMatch) 
		    {
		        test.log(Status.PASS, "Searched product is found: " + productText);
		        phoneTitles1.get(i).click();
		        test.log(Status.INFO, "Navigated to Searched product");
		        productFound1 = true;
		        break;
		    }
		}
		
		Assert.assertTrue(productFound1);
		test.log(Status.INFO, "searched product varient is  displaced");

		Set<String> childs = driver.getWindowHandles();
		Iterator<String> iterator = childs.iterator();
		while (iterator.hasNext()) 
		{
		    String child = iterator.next();
		    driver.switchTo().window(child);
		    break; // switches to the first available child window
		}
		String FlipkartPrice = driver.findElement(By.xpath("(//*[@class='hl05eU']/div)[1]")).getText();	
		Double flipkartPrice = Double.parseDouble(FlipkartPrice.replaceAll("[^0-9]", ""));
		test.log(Status.INFO, "Flipkart price for Searched Product is : "+ flipkartPrice);	
		
		if(flipkartPrice==amazonPrice)
		{
			Assert.assertEquals(flipkartPrice, amazonPrice);
			test.log(Status.PASS,"Both Prices are Equal");
			System.out.println("Both Prices are Equal");
		}
		else 	if(flipkartPrice>amazonPrice)
		{
			Assert.assertNotEquals(amazonPrice, FlipkartPrice);
			test.log(Status.PASS,"Amazon Product Price is Leaserthan the Flipkart Product Price");
			System.out.println("Amazon Product Price is Leaserthan the Flipkart Product Price");
		}
		else
		{
			Assert.assertNotEquals(amazonPrice, FlipkartPrice);
			test.log(Status.PASS,"Flipkart Product Price is Leaserthan the Amazon Product Price");
			System.out.println("Flipkart Product Price is Leaserthan the Amazon Product Price");
		}
	}
	catch(Exception e){screenshot("failedtest1");	test.log(Status.FAIL,e.getMessage());}
	}
}

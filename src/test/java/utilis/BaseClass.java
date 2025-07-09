package utilis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.Status;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClass 
{
	public static File f;
	public static FileInputStream fis;
	public static WebDriver driver;
	public static Properties configreader;
	public static WebDriverWait wait;
	public static Actions action;
	public static String parent;
	// Reports
	public static ExtentSparkReporter htmlReporter;
	public static ExtentReports extent;
	public static ExtentTest test;
	@BeforeClass
	public void setUp() throws IOException
	{
		htmlReporter = new ExtentSparkReporter(System.getProperty("user.dir")+"/Reports/Extent_"+getCurrentDateTime()+".html");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
    
        htmlReporter.config().setDocumentTitle("Test Reports");
        htmlReporter.config().setReportName("Test Reports ");
        htmlReporter.config().setTheme(Theme.STANDARD);
	
        
        
        
 
        
        
        
        
        
		configreader = new Properties();
		f=new File(System.getProperty("user.dir")+"/src/test/resources/config.properties");
		fis = new FileInputStream(f);
		configreader.load(fis);
		
		if(configreader.getProperty("BrowserType").equals("chrome"))
		{
			 WebDriverManager.chromedriver().setup(); 
			driver = new ChromeDriver();
		}else 	if(configreader.getProperty("BrowserType").equals("firefox"))
		{
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+"/Others/geckodriver");
			driver = new FirefoxDriver();
		}else 	if(configreader.getProperty("BrowserType").equals("edge"))
		{
			System.setProperty("webdriver.edge.driver", System.getProperty("user.dir")+"/Others/msedgedriver");
			driver = new EdgeDriver();
		}else 	if(configreader.getProperty("BrowserType").equals("safari"))
		{
			
			driver = new SafariDriver();
		}
		
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);// implicit wait - for all elements
		wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(20));							// explicit wait - for single element using condition
		driver.manage().window().maximize();
		
		//driver.get(configreader.getProperty("URL"));
		//test.log(Status.INFO, "Navigate to Application");

		
		action = new Actions(driver);
		parent = driver.getWindowHandle();
		
	}
	public  String getCurrentDateTime() 
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        return now.format(formatter);
    }
	
	 @AfterMethod
	    public void getResult(ITestResult result)
	    {
	        if(result.getStatus() == ITestResult.FAILURE)
	        {
	            test.log(Status.FAIL, MarkupHelper.createLabel(result.getName()+" Test case FAILED due to below issues:", ExtentColor.RED));
	            test.fail(result.getThrowable());
	        }
	        else if(result.getStatus() == ITestResult.SUCCESS)
	        {
	            test.log(Status.PASS, MarkupHelper.createLabel(result.getName()+" Test Case PASSED", ExtentColor.GREEN));
	        }
	        else
	        {
	            test.log(Status.SKIP, MarkupHelper.createLabel(result.getName()+" Test Case SKIPPED", ExtentColor.ORANGE));
	            test.skip(result.getThrowable());
	        }
	    }
	    
	    public void screenshot(String filename)
	    {
	    	String screenshotPath= System.getProperty("user.dir")+"/Screenshots/"+filename+".png";
	    	File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	        File destFile = new File(screenshotPath);
	        
	        try {
	            FileUtils.copyFile(srcFile, destFile);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    @AfterClass
	    public void reportGenerate()
	    { 
	    	driver.close();
	    	System.out.println("In report Generate"); 
	        extent.flush();
	      	       
	    }
	
	
	
}

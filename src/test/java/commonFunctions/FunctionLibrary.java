package commonFunctions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

public class FunctionLibrary {
public static WebDriver driver;
public static Properties conpro;
//method for launching browser
public static WebDriver startBrowser() throws Throwable
{
	conpro = new Properties();
	conpro.load(new FileInputStream("./PropertyFiles/Environment.properties"));
	if(conpro.getProperty("Browser").equalsIgnoreCase("chrome"))
	{
		driver = new ChromeDriver();
		driver.manage().window().maximize();
	}
	else if(conpro.getProperty("Browser").equalsIgnoreCase("firefox"))
	{
		driver = new FirefoxDriver();
		driver.manage().window().maximize();
	}
	return driver;
}
//method for launching url
public static void openUrl()
{
	driver.get(conpro.getProperty("Url"));
}
//method for waiting for any webelement
public static void waitForElement(String LocatorType, String LocatorValue, String TestData)
{
	WebDriverWait mywait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(TestData)));
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(LocatorValue)));
	}
	if(LocatorType.equalsIgnoreCase("name"))
	{
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.name(LocatorValue)));
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.id(LocatorValue)));
	}
}
//method for any textbox
public static void typeAction(String LocatorType, String LocatorValue, String TestData)
{
	if(LocatorType.equalsIgnoreCase("name"))
	{
		driver.findElement(By.name(LocatorValue)).clear();
		driver.findElement(By.name(LocatorValue)).sendKeys(TestData);
	}
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		driver.findElement(By.xpath(LocatorValue)).clear();
		driver.findElement(By.xpath(LocatorValue)).sendKeys(TestData);
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		driver.findElement(By.id(LocatorValue)).clear();
		driver.findElement(By.id(LocatorValue)).sendKeys(TestData);
	}
}
//method for any like buttons, links, checkboxes, radio buttons and image
public static void clickAction(String LocatorType, String LocatorValue)
{
	if(LocatorType.equalsIgnoreCase("name"))
	{
		driver.findElement(By.name(LocatorValue)).click();
	}
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		driver.findElement(By.xpath(LocatorValue)).click();
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		driver.findElement(By.id(LocatorValue)).sendKeys(Keys.ENTER);
	}
}
//method for validating any title
public static void validateTitle(String Exp_Title)
{
	String Act_Title = driver.getTitle();
	try {
	Assert.assertEquals(Exp_Title, Act_Title,"Title is not matching");
	}catch(AssertionError a)
	{
		System.out.println(a.getMessage());
	}
}
//method for closing browser
public static void closeBrowser()
{
	driver.quit();
}
//method to generate date and time
public static String generateDate()
{
	Date date = new Date();
	DateFormat df = new SimpleDateFormat("YYYY_MM_dd  hh_mm");
	return df.format(date);
}
//method for listboxes
public static void dropDownAction(String LocatorType, String LocatorValue, String TestData)
{
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		int value = Integer.parseInt(TestData);
		Select element = new Select(driver.findElement(By.xpath(LocatorValue)));
		element.selectByIndex(value);
	}
	if(LocatorType.equalsIgnoreCase("name"))
	{
		int value = Integer.parseInt(TestData);
		Select element = new Select(driver.findElement(By.name(LocatorValue)));
		element.selectByIndex(value);
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		int value = Integer.parseInt(TestData);
		Select element = new Select(driver.findElement(By.id(LocatorValue)));
		element.selectByIndex(value);
	}
}
//method for capturing stock number into notepad
public static void captureStock(String LocatorType, String LocatorValue) throws Throwable
{
    String StockNum = "";
    if(LocatorType.equalsIgnoreCase("xpath"))
    {
    	StockNum = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
    }
    if(LocatorType.equalsIgnoreCase("name"))
    {
    	StockNum = driver.findElement(By.name(LocatorValue)).getAttribute("value");
    }
    if(LocatorType.equalsIgnoreCase("id"))
    {
    	StockNum = driver.findElement(By.id(LocatorValue)).getAttribute("value");
    }
    //create notepad file into capture data folder
    FileWriter fw = new FileWriter("./CaptureData/StockNumber.txt");
    BufferedWriter bw = new BufferedWriter(fw);
    bw.write(StockNum);
    bw.flush();
    bw.close();
}
//method to verify stock table
public static void stockTable() throws Throwable
{
	//read stock number from above notepad
	FileReader fr = new FileReader("./CaptureData/StockNumber.txt");
	BufferedReader br = new BufferedReader(fr);
	String Exp_Data = br.readLine();
	if(!driver.findElement(By.xpath(conpro.getProperty("SearchTextbox"))).isDisplayed())
		driver.findElement(By.xpath(conpro.getProperty("SearchPanel"))).click();
	    driver.findElement(By.xpath(conpro.getProperty("SearchTextbox"))).clear();
	    driver.findElement(By.xpath(conpro.getProperty("SearchTextbox"))).sendKeys(Exp_Data);
	    driver.findElement(By.xpath(conpro.getProperty("SearchButton"))).click();
	    Thread.sleep(3000);
	    String Act_Data = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[8]/div/span/span")).getText();
	    Reporter.log(Exp_Data+"         "+Act_Data,true);
	    try {
	    Assert.assertEquals(Exp_Data, Act_Data,"Stock number is not matching");
	    }catch(AssertionError e) {
	    	System.out.println(e.getMessage());
	    }
}
//method for capturing supplier number into notepad
public static void captureSupplier(String LocatorType, String LocatorValue) throws Throwable
{
	String SupplierNum = "";
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		SupplierNum = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
	}
	if(LocatorType.equalsIgnoreCase("name"))
	{
		SupplierNum = driver.findElement(By.name(LocatorValue)).getAttribute("value");
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		SupplierNum = driver.findElement(By.id(LocatorValue)).getAttribute("value");
	}
//create notepad into capture data folder
	FileWriter fw = new FileWriter("./CaptureData/SupplierNumber.txt");
	BufferedWriter bw = new BufferedWriter(fw);
	bw.write(SupplierNum);
	bw.flush();
	bw.close();
}
//method to verify suppliertable
public static void supplierTable() throws Throwable
{
	//read stock number from above notepad
	FileReader fr = new FileReader("./CaptureData/SupplierNumber.txt");
	BufferedReader br = new BufferedReader(fr);
	String ExpectedData = br.readLine();
	if(!driver.findElement(By.xpath(conpro.getProperty("SearchTextbox"))).isDisplayed())
		driver.findElement(By.xpath(conpro.getProperty("SearchPanel"))).click();
	    driver.findElement(By.xpath(conpro.getProperty("SearchTextbox"))).clear();
	    driver.findElement(By.xpath(conpro.getProperty("SearchTextbox"))).sendKeys(ExpectedData);
	    driver.findElement(By.xpath(conpro.getProperty("SearchButton"))).click();
	    Thread.sleep(3000);
	    String ActualData = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[6]/div/span/span")).getText();
	    Reporter.log(ActualData+"         "+ExpectedData,true);
	    try {
	    Assert.assertEquals(ActualData, ExpectedData,"Supplier Number is not matching");
	    }catch(AssertionError a) {
	    	System.out.println(a.getMessage());
	    }
}
//method to capture customer number into notepad
public static void captureCustomer(String LocatorType, String LocatorValue) throws Throwable
{
	String CustomerNum = "";
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		CustomerNum = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
	}
	if(LocatorType.equalsIgnoreCase("name"))
	{
		CustomerNum = driver.findElement(By.name(LocatorValue)).getAttribute("value");
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		CustomerNum = driver.findElement(By.id(LocatorValue)).getAttribute("value");
	}
	//create notepad into capture data folder
	FileWriter fw = new FileWriter("./CaptureData/CustomerNum.txt");
	BufferedWriter bw = new BufferedWriter(fw);
	bw.write(CustomerNum);
	bw.flush();
	bw.close();
}
//method to verify customer table
public static void customerTable() throws Throwable
{
	//read stock number from above notepad
	FileReader fr = new FileReader("./CaptureData/CustomerNum.txt");
	BufferedReader br = new BufferedReader(fr);
	String Exp_D = br.readLine();
	if(!driver.findElement(By.xpath(conpro.getProperty("SearchTextbox"))).isDisplayed())
		driver.findElement(By.xpath(conpro.getProperty("SearchPanel"))).click();
	    driver.findElement(By.xpath(conpro.getProperty("SearchTextbox"))).clear();
	    driver.findElement(By.xpath(conpro.getProperty("SearchTextbox"))).sendKeys(Exp_D);
	    driver.findElement(By.xpath(conpro.getProperty("SearchButton"))).click();
	    Thread.sleep(3000);
	    String Act_D = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[5]/div/span/span")).getText();
	    Reporter.log(Act_D+"       "+Exp_D,true);
	    try {
	    Assert.assertEquals(Act_D, Exp_D,"Customer Number is not matching");
	    }catch(AssertionError e) {
	    	System.out.println(e.getMessage());
	    }
}

}

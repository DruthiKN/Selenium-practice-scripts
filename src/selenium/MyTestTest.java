package selenium;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;

public class MyTestTest {
	private WebDriver driver;
	  private Map<String, Object> vars;
	  JavascriptExecutor js;
	  @Before
	  public void setUp() {
	    driver = new ChromeDriver();
	    js = (JavascriptExecutor) driver;
	    vars = new HashMap<String, Object>();
	  }
	  @After
	  public void tearDown() {
	    driver.quit();
	  }
	  @Test
	  public void myTest() {
	    driver.get("https://www.saucedemo.com/");
	    driver.manage().window().setSize(new Dimension(658, 752));
	    driver.findElement(By.cssSelector("*[data-test=\"username\"]")).click();
	    driver.findElement(By.cssSelector("*[data-test=\"username\"]")).sendKeys("standard_user");
	    driver.findElement(By.cssSelector("*[data-test=\"password\"]")).click();
	    driver.findElement(By.cssSelector("*[data-test=\"password\"]")).sendKeys("secret_sauce");
	    driver.findElement(By.cssSelector("*[data-test=\"login-button\"]")).click();
	    driver.findElement(By.cssSelector("*[data-test=\"add-to-cart-sauce-labs-backpack\"]")).click();
	    driver.findElement(By.cssSelector("*[data-test=\"shopping-cart-link\"]")).click();
	    driver.findElement(By.cssSelector("*[data-test=\"checkout\"]")).click();
	    driver.findElement(By.cssSelector("*[data-test=\"firstName\"]")).click();
	    driver.findElement(By.cssSelector("*[data-test=\"firstName\"]")).sendKeys("MY");
	    driver.findElement(By.cssSelector("*[data-test=\"lastName\"]")).click();
	    driver.findElement(By.cssSelector("*[data-test=\"lastName\"]")).sendKeys("Name");
	    driver.findElement(By.cssSelector("*[data-test=\"postalCode\"]")).click();
	    driver.findElement(By.cssSelector("*[data-test=\"postalCode\"]")).sendKeys("543367");
	    driver.findElement(By.cssSelector("*[data-test=\"continue\"]")).click();
	    driver.findElement(By.cssSelector("*[data-test=\"finish\"]")).click();
	    driver.findElement(By.cssSelector("*[data-test=\"back-to-products\"]")).click();
	  }
	}



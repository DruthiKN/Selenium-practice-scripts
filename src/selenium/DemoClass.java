package selenium;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
//import static org.hamcrest.CoreMatchers.is;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class DemoClass {
	 WebDriver driver;

    @Before
    public void setUp() {
        driver = new FirefoxDriver();
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void test1() {

        // Open login page
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

        // Wait for username
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='username']")));

        // Enter username
        driver.findElement(By.xpath("//input[@name='username']")).sendKeys("Admin");

        // Enter password
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys("admin123");

        // Click login
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        // Wait for Dashboard
     // Wait for Dashboard
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h6[text()='Dashboard']")));

        // Verify Dashboard text
        String actualText = driver.findElement(By.xpath("//h6[text()='Dashboard']")).getText();
        assertTrue(actualText.contains("Dashboard"));
        if (actualText.contains("Dashboard")) {
            System.out.println("✅ Login Test Passed");
        } else {
            System.out.println("❌ Login Test Failed");
        }

       
     }
}
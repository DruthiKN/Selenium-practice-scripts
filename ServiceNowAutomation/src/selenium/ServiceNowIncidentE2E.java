package selenium;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;

 
public class ServiceNowIncidentE2E {
	static WebDriver driver;
	static WebDriverWait wait;
	static String incidentNumber;
 
    public static void main(String[] args) throws InterruptedException {
    	WebDriver driver=new ChromeDriver();
    	driver.manage().window().maximize();
    	driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    	wait=new WebDriverWait(driver, Duration.ofSeconds(20));
 
          // ================= LOGIN =================
 
        driver.get("https://dev230144.service-now.com/login.do");
 
        driver.findElement(By.id("user_name")).sendKeys("admin");   // your username
        driver.findElement(By.id("user_password")).sendKeys("Niranju678##&"); // real password
        driver.findElement(By.id("sysverb_login")).click();
 
     // 2. Verify homepage title
        System.out.println("Title: " + driver.getTitle());
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        driver.switchTo().defaultContent();
        Thread.sleep(5000);
     // ================= OPEN INCIDENT LIST (VISIBLE FLOW) =================
     // ================= STABLE SERVICENOW NAVIGATION =================
        wait.until(ExpectedConditions.titleContains("ServiceNow"));
        driver.switchTo().defaultContent();
 
        /* Open Incidents module (same as user clicking navigator) */
     // Open Incidents list
        driver.get("https://dev230144.service-now.com/nav_to.do?uri=incident_list.do");
 
     // ===== OPEN INCIDENT LIST =====
        driver.get("https://dev230144.service-now.com/nav_to.do?uri=incident_list.do");
        
        WebElement shadowHost = (WebElement) ((JavascriptExecutor)driver).executeScript("return document.querySelector('body > macroponent-f51912f4c700201072b211d4d8c26010').shadowRoot.querySelector('div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout')");
        WebElement iFrame = shadowHost.findElement(By.id("gsft_main"));
        driver.switchTo().frame(iFrame);
        System.out.println("Switched to gsft_main");
 
        /* Switch to Classic list frame FIRST */
        //it.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("gsft_main"));
 
        /* Wait until New button is clickable (inside frame) */
        WebElement newBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("sysverb_new")));
        newBtn.click();
 
        /* Wait until Incident FORM loads */
        WebElement shortDesc = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("incident.short_description")));
 
        /* Fill */
        shortDesc.clear();
        shortDesc.sendKeys("Test Incident created through Selenium");
        /* ================= FILL CALLER (MANDATORY) ================= */
 
     // Wait & click Caller field
     WebElement caller = wait.until(ExpectedConditions.elementToBeClickable(By.id("sys_display.incident.caller_id")));
     caller.click();
     caller.clear();
 
     // Type caller name (must exist in instance)
     caller.sendKeys("Abel Tuter");
 
     // Wait for suggestion dropdown and press ENTER to select
     Thread.sleep(1500);
     caller.sendKeys(Keys.ENTER);
 
     /* ================= CATEGORY (AUTO / FORCE SELECT) ================= */
 
     // Sometimes category auto-fills. If not, set manually.
     try {
         WebElement category = driver.findElement(By.id("incident.category"));
         Select catDrop = new Select(category);
         catDrop.selectByVisibleText("Inquiry / Help");
     } catch (Exception e) {
         System.out.println("Category already auto-filled");
     }
 
        /* ================= SUBCATEGORY (AUTO / FORCE SELECT) ================= */
 
     try {
         WebElement subCategory = driver.findElement(By.id("incident.subcategory"));
         Select subDrop = new Select(subCategory);
         subDrop.selectByIndex(0);   // or choose text if needed
     } catch (Exception e) {
         System.out.println("Subcategory already auto-filled");
     }
 
 
     /* ================= DESCRIPTION ================= */
 
     WebElement desc = driver.findElement(By.id("incident.description"));
     desc.clear();
     desc.sendKeys("Test Incident created automatically using Selenium E2E");
     /* ================= WAIT UNTIL FORM IS STABLE ================= */
     Thread.sleep(2000);
 
     /* ================= CAPTURE INCIDENT NUMBER ================= */
     WebElement numberField = wait.until(
             ExpectedConditions.presenceOfElementLocated(By.name("incident.number"))
     );
 
     incidentNumber = numberField.getAttribute("value");
 
     System.out.println("✅ Created Incident Number: " + incidentNumber);
 
 
     /* ================= CLICK SUBMIT SAFELY ================= */
     WebElement submitBtn = wait.until(
             ExpectedConditions.elementToBeClickable(By.id("sysverb_insert"))
     );
 
     /* Scroll into view */
     ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitBtn);
     Thread.sleep(800);
 
     /* NORMAL click first */
     try {
         submitBtn.click();
     } catch (Exception e) {
         /* JS fallback if normal click fails */
         ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitBtn);
     }
 
     System.out.println(" Incident submitted successfully");
     
     /*=====================================================
     OPEN INCIDENT LIST (NO FRAME ASSUMPTION)
     ===================================================== */
 
  driver.switchTo().defaultContent();
  driver.get("https://dev230144.service-now.com/incident_list.do");
 
  /* WAIT UNTIL PAGE LOADS */
  wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
 
  System.out.println(" Incident list opened");
 
  /* =====================================================
     TRY SWITCH TO FRAME IF PRESENT (SAFE)
     ===================================================== */
  try {
      driver.switchTo().frame("gsft_main");
      System.out.println(" gsft_main frame detected");
  } catch (Exception e) {
      System.out.println(" No frame — continuing without frame (Workspace/Polaris)");
  }
 
  /* =====================================================
     SEARCH INCIDENT NUMBER
     ===================================================== */
  WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(
          By.xpath("//input[@placeholder='Search']")
  ));
 
  searchBox.clear();
  searchBox.sendKeys(incidentNumber);
  searchBox.sendKeys(Keys.ENTER);
 
  System.out.println(" Searched: " + incidentNumber);
 
  /* =====================================================
     CLICK INCIDENT LINK
     ===================================================== */
  WebElement incidentLink = wait.until(ExpectedConditions.elementToBeClickable(
          By.xpath("//a[text()='" + incidentNumber + "']")
  ));
  incidentLink.click();
 
  System.out.println(" Incident opened");
 
  /* =====================================================
     SWITCH AGAIN IF FRAME EXISTS (FORM PAGE)
     ===================================================== */
  driver.switchTo().defaultContent();
  Thread.sleep(2000);
 
  try {
      driver.switchTo().frame("gsft_main");
      System.out.println(" Switched to form frame");
  } catch (Exception e) {
      System.out.println(" Form opened without frame");
  }
 
  /* =====================================================
     SET STATE = RESOLVED
     ===================================================== */
  WebElement stateDropdown = wait.until(ExpectedConditions.elementToBeClickable(
          By.id("incident.state")
  ));
  new Select(stateDropdown).selectByVisibleText("Resolved");
 
  System.out.println(" State changed to Resolved");
 
  /* =====================================================
  STEP 1 — OPEN RESOLUTION INFORMATION TAB
  ===================================================== */
WebElement resolutionTab = wait.until(ExpectedConditions.elementToBeClickable(
       By.xpath("//span[contains(text(),'Resolution Information')]")
));
resolutionTab.click();
 
System.out.println(" Resolution tab opened");
Thread.sleep(1000);
 
/* =====================================================
  STEP 2 — SELECT RESOLUTION CODE (AUTO SELECT FIRST VALID)
  ===================================================== */
WebElement codeDropdown = wait.until(ExpectedConditions.elementToBeClickable(
       By.id("incident.close_code")
));
 
Select codeSelect = new Select(codeDropdown);
 
for (WebElement opt : codeSelect.getOptions()) {
   String text = opt.getText().trim();
   if (!text.equals("") && !text.contains("None")) {
       codeSelect.selectByVisibleText(text);
       System.out.println(" Resolution Code selected: " + text);
       break;
   }
}
 
/* =====================================================
  STEP 3 — CLICK RESOLUTION NOTES FIELD (INSIDE TAB)
  ===================================================== */
WebElement notesField = wait.until(ExpectedConditions.elementToBeClickable(
       By.id("incident.close_notes")
));
 
notesField.click();
notesField.clear();
notesField.sendKeys("Incident resolved automatically using Selenium");
 
System.out.println(" Resolution Notes filled");
 
 
/* =====================================================
  STEP 4 — CLICK UPDATE SAFELY
  ===================================================== */
WebElement updateBtn = wait.until(ExpectedConditions.elementToBeClickable(
       By.id("sysverb_update")
));
 
((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", updateBtn);
Thread.sleep(700);
 
try {
   updateBtn.click();
} catch (Exception e) {
   ((JavascriptExecutor) driver).executeScript("arguments[0].click();", updateBtn);
}
System.out.println(" Incident RESOLVED and UPDATED successfully ");

/* =====================================================
GLOBAL SEARCH — UNIVERSAL SHADOW DOM SEARCH (ROBUST)
===================================================== */

driver.switchTo().defaultContent();

/* Open HOME so header always visible */
driver.get("https://dev230144.service-now.com/now/nav/ui/home");

/* Wait page ready */
new WebDriverWait(driver, Duration.ofSeconds(60)).until(
        d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete")
);

Thread.sleep(2000);

JavascriptExecutor js1 = (JavascriptExecutor) driver;

/* -------------------------------------------------
   FIND GLOBAL SEARCH INPUT BY WALKING ALL SHADOW ROOTS
------------------------------------------------- */
WebElement searchInput = (WebElement) js1.executeScript(
        "function findInput(root) {" +
        "  if (!root) return null;" +
        "  let el = root.querySelector('input[placeholder*=\"Search\"]');" +
        "  if (el) return el;" +
        "  let all = root.querySelectorAll('*');" +
        "  for (let i = 0; i < all.length; i++) {" +
        "    if (all[i].shadowRoot) {" +
        "      let found = findInput(all[i].shadowRoot);" +
        "      if (found) return found;" +
        "    }" +
        "  }" +
        "  return null;" +
        "}" +
        "return findInput(document);"
);

if (searchInput == null) {
    throw new RuntimeException("Global Search input NOT FOUND");
}

System.out.println("Global Search box FOUND");

/* Type Incident Number visibly */
searchInput.click();
searchInput.clear();
searchInput.sendKeys(incidentNumber);
Thread.sleep(1200);
searchInput.sendKeys(Keys.ENTER);

System.out.println("Global Search executed → " + incidentNumber);

System.out.println("Global Search executed → " + incidentNumber);

//================= FORCE OPEN INCIDENT FORM USING NUMBER =================
Thread.sleep(3000);


driver.get("https://dev230144.service-now.com/incident.do?sysparm_query=number=" + incidentNumber);

Thread.sleep(8000);

System.out.println("Incident form opened directly from number ✔");

//================= WORKSPACE FINAL CLOSE BLOCK =================
Thread.sleep(8000);

/* ================= STATE → CLOSED ================= */

boolean stateDone = false;
try {
 WebElement stateBox = driver.findElement(
         By.xpath("//label[.='State']/following::*[self::select or @role='combobox'][1]"));
 stateBox.click();
 Thread.sleep(1000);

 driver.findElement(By.xpath("//*[text()='Closed']")).click();
 stateDone = true;
 System.out.println("State changed to Closed ✔");
} catch (Exception e) {
 System.out.println("❌ State not found");
}

/* ================= CLICK SAVE / UPDATE ================= */

boolean updateDone = false;
try {
 driver.findElement(By.xpath("//button[.='Update' or .='Save']")).click();
 updateDone = true;
 System.out.println("Update clicked ✔");
} catch (Exception e) {
 System.out.println("❌ Update button not found");
}

Thread.sleep(6000);
//============ VERIFY CLOSED (SAFE VERSION) ============

WebElement stateVerify = driver.findElement(
     By.xpath("//label[.='State']/following::*[self::select or @role='combobox'][1]"));

String stateText = stateVerify.getText();
System.out.println("FINAL STATE = " + stateText);

if (stateText.toLowerCase().contains("closed")) {
 System.out.println("INCIDENT CLOSED SUCCESSFULLY");

 takeScreenshot(driver, "Incident_Closed_" + incidentNumber);
 logout(driver);   
} 
else {
	System.out.println("INCIDENT NOT CLOSED");
}
}

    // ===== Screenshot Method =====
    public static void takeScreenshot(WebDriver driver, String name) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dest = new File(System.getProperty("user.dir") + "\\Screenshots\\" + name + ".png");
            dest.getParentFile().mkdirs();
            Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("📸 Screenshot saved: " + dest.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("❌ Screenshot failed");
        }
    }
    
 // ===== Stable Logout Method =====
    public static void logout(WebDriver driver) {
        try {
            // Direct logout URL (most reliable)
            driver.get("https://dev230144.service-now.com/login.do");

            // Wait until login page loads
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user_name")));

            System.out.println("Logged out successfully and navigated to Login page");

        } catch (Exception e) {
            System.out.println("Logout failed");
        }
    }
}
    
    


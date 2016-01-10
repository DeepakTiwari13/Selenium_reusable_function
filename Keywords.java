package com.kinnser.selenium.layer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Keywords {

	public static Properties CON = null;
	public static Properties OR = null;
	FileInputStream fis = null;
	WebDriver driver = null;
	static Keywords session;
	public static boolean isloggedin = false;
	static Logger adl_log = Logger.getLogger("Keywords");

	private Keywords() throws IOException {

		adl_log.info("Inside Keywords constructor ");

		CON = new Properties();
		fis = new FileInputStream(System.getProperty("user.dir")
				+ "\\src\\com\\kinnser\\configuration\\Config.properties");
		CON.load(fis);
		adl_log.info("configuration file loaded ");

		OR = new Properties();
		fis = new FileInputStream(System.getProperty("user.dir")
				+ "\\src\\com\\kinnser\\configuration\\OR.properties");
		OR.load(fis);
		adl_log.info("object repository loaded");
	}

	public static Keywords getinstance() throws Exception {

		adl_log.info("Singleton class implementation achieved ");

		if (session == null) {
			session = new Keywords();
		}
		return session;
	}

	public boolean Launchbrowser(String browser) {

		adl_log.info("Inside launch browser function " + browser);

		try {

			if (CON.getProperty(browser).equals("Chrome")) {
				System.setProperty(
						"webdriver.chrome.driver",
						System.getProperty("user.dir")
								+ "\\src\\com\\kinnser\\drivers\\chromedriver.exe");

				adl_log.info("launching chrome driver");
				driver = new ChromeDriver();

			} else if (CON.getProperty(browser).equals("Firefox")) {

				File path = new File(
						"C:\\Users\\deepakt\\AppData\\Local\\Mozilla Firefox\\firefox.exe");
				FirefoxBinary Binary = new FirefoxBinary(path);
				ProfilesIni profile = new ProfilesIni();
				FirefoxProfile myprofile = profile.getProfile("GOA");
				adl_log.info("launching fire fox driver");
				driver = new FirefoxDriver(Binary, myprofile);

			} else if (CON.getProperty(browser).equals("IE")) {
				System.setProperty(
						"webdriver.ie.driver",
						System.getProperty("user.dir")
								+ "\\src\\com\\kinnser\\drivers\\IEDriverServer.exe");
				adl_log.info("launching internet explorer driver");
				driver = new InternetExplorerDriver();

			}

			driver.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			return true;

		} catch (Throwable t) {
			adl_log.error("Failed to load driver " + t.fillInStackTrace());
		}
		return false;
	}

	public boolean Loadurl(String Testurl) {

		adl_log.info("Inside load url function with "
				+ CON.getProperty(Testurl));
		try {
			driver.get(CON.getProperty(Testurl));
			return true;
		} catch (Throwable t) {
			adl_log.error("Failed to load url " + t.fillInStackTrace());
		}
		return false;
	}

	public boolean Clickonelement(String xpathkey) {

		adl_log.info("Inside click on webelement function with " + xpathkey);

		try {

			WebDriverWait wait = new WebDriverWait(driver, 100);
			WebElement element = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath(OR.getProperty(xpathkey))));
			element.click();
			return true;
		} catch (Throwable t) {
			adl_log.fatal(
					"Failed to click on element " + (OR.getProperty(xpathkey)),
					t.fillInStackTrace());

		}
		return false;

	}

	public boolean Typeinto(String xpathkey, String texttowrite) {

		adl_log.info("Inside select and type in webelement function with "
				+ xpathkey + "--");

		try {

			WebDriverWait wait = new WebDriverWait(driver, 100);
			WebElement element = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath(OR
							.getProperty(xpathkey))));

			element.sendKeys(texttowrite);
			return true;
		} catch (Throwable t) {
			adl_log.fatal(
					"Failed to type in to element "
							+ (OR.getProperty(xpathkey)), t.fillInStackTrace());
		}
		return false;
	}

	public String Extracttostring(String xpathkey) {

		adl_log.info("Inside extract text from webelement function with "
				+ xpathkey);

		try {

			WebDriverWait wait = new WebDriverWait(driver, 100);
			WebElement element = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath(OR
							.getProperty(xpathkey))));

			return element.getText();

		} catch (Throwable t) {
			adl_log.error(
					"Failed to extract text from element "
							+ (OR.getProperty(xpathkey)), t.fillInStackTrace());

		}
		return null;

	}

	public boolean Dologin(String UserName, String Password, String Accountid) {

		adl_log.info("Inside do login function");

		try {
			Typeinto(UserName, CON.getProperty("Username"));
			Typeinto(Password, CON.getProperty("Password"));
			Typeinto(Accountid, CON.getProperty("Accountid"));
			Clickonelement("Loginbutton");
			return true;

		} catch (Throwable t) {
			adl_log.fatal("Failed to login ", t.fillInStackTrace());

		}
		return false;

	}

	public boolean Hoverthemouseon(String xpathkey) {

		adl_log.info("Inside mouse hover function with " + xpathkey);

		try {

			WebDriverWait wait = new WebDriverWait(driver, 100);
			Actions action = new Actions(driver);
			WebElement elementtohover = wait.until(ExpectedConditions
					.visibilityOfElementLocated((By.xpath(OR
							.getProperty(xpathkey)))));
			action.moveToElement(elementtohover).clickAndHold();
			// action.moveToElement(elementtohover);
			Thread.sleep(2000l);
			action.perform();
			action.release();
			return true;
		} catch (Throwable t) {
			adl_log.error(
					"Failed to hover the mouse on "
							+ (OR.getProperty(xpathkey)), t.fillInStackTrace());

		}
		return false;

	}

	public boolean Selectfromdropdown(String xpathkey, String texttoselect) {

		adl_log.info("Inside select from dropdown function with " + xpathkey);

		try {

			WebDriverWait wait = new WebDriverWait(driver, 100);
			WebElement element = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath(OR
							.getProperty(xpathkey))));
			Select se = new Select(element);
			se.selectByVisibleText(texttoselect);
			return true;
		} catch (Throwable t) {

			adl_log.error(
					"Failed to select the element for dropdown "
							+ (OR.getProperty(xpathkey)) + "-----"
							+ "value supplied " + texttoselect,
					t.fillInStackTrace());

		}
		return false;

	}

	public boolean Selectnamefrombox(String xpathkey1, String xpathkey2,
			String xpathkey3, String xpathkey4, String nametoselect) {

		adl_log.info("Inside select name from box function with " + xpathkey1);

		try {
			WebDriverWait wait = new WebDriverWait(driver, 100);
			WebElement element = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath(OR
							.getProperty(xpathkey1))));

			List<WebElement> ecmrows = Returnlistofwebelement(xpathkey2);
			List<WebElement> ecmcolumns = Returnlistofwebelement(xpathkey3);
			List<WebElement> ecmselectedcolumns = Returnlistofwebelement(xpathkey4);

			adl_log.info("Rows iterating " + ecmcolumns.size());

			for (int i = 0; i < ecmcolumns.size() - 1; i++) {

				adl_log.info(ecmselectedcolumns.get(i).getText());
				if (ecmselectedcolumns.get(i).getText().trim()
						.equals(nametoselect)) {
					ecmselectedcolumns.get(i).click();
					Thread.sleep(2000l);
					adl_log.info("Debuging");
				}
			}
			return true;
		} catch (Throwable t) {
			adl_log.error(
					"Failed to retrieve data from table "
							+ (OR.getProperty(xpathkey1)) + "-----"
							+ "value supplied " + nametoselect,
					t.fillInStackTrace());

		}
		return false;

	}

	public WebElement Returnwebelement(String xpathkey) {

		adl_log.info("Inside return webelement function with " + xpathkey);

		try {
			WebDriverWait wait = new WebDriverWait(driver, 100);
			WebElement element = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath(OR
							.getProperty(xpathkey))));
			return element;
		} catch (Throwable t) {
			adl_log.error(
					"Failed to return webelement " + (OR.getProperty(xpathkey)),
					t.fillInStackTrace());
		}
		return null;
	}

	public List<WebElement> Returnlistofwebelement(String xpathkey) {

		adl_log.info("Inside return list of webelements function with "
				+ xpathkey);

		try {
			WebDriverWait wait = new WebDriverWait(driver, 100);
			List<WebElement> elements = wait.until(ExpectedConditions
					.visibilityOfAllElementsLocatedBy(By.xpath(OR
							.getProperty(xpathkey))));
			return elements;
		} catch (Throwable t) {
			adl_log.error(
					"Failed to return webelement " + (OR.getProperty(xpathkey)),
					t.fillInStackTrace());
		}
		return null;
	}

	public WebElement Getname(String name) {

		adl_log.info("Inside return webelement function with " + name);

		try {
			WebDriverWait wait = new WebDriverWait(driver, 300);
			WebElement element = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By
							.xpath("//*[text()[contains(.,'" + name + "')]]")));
			return element;
		} catch (Throwable t) {
			adl_log.error("Failed to return webelement " + name,
					t.fillInStackTrace());
		}
		return null;
	}

	public boolean Acceptalert() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 100);
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			alert.accept();
			return true;
		} catch (Throwable t) {
			adl_log.error("Failed to accept the alert ", t.fillInStackTrace());
		}
		return false;
	}

	public boolean Executeafterinvisibility(String xpathkey) {

		adl_log.info("Inside return webelement function with " + xpathkey);

		try {
			WebDriverWait wait = new WebDriverWait(driver, 100);
			boolean element = wait.until(ExpectedConditions
					.invisibilityOfElementLocated(By.xpath(OR
							.getProperty(xpathkey))));
			return true;
		} catch (Throwable t) {
			adl_log.error(
					"Failed to return webelement " + (OR.getProperty(xpathkey)),
					t.fillInStackTrace());
		}
		return false;
	}

	public boolean Scrolltoelement(String xpathkey) {

		adl_log.info("Inside scroll to element function with " + xpathkey);
		try {
			JavascriptExecutor je = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(By.xpath(OR
					.getProperty(xpathkey)));
			je.executeScript("arguments[0].scrollIntoView(true);", element);
			return true;
		} catch (Throwable t) {
			adl_log.error(
					"Failed to scroll to element " + (OR.getProperty(xpathkey)),
					t.fillInStackTrace());
		}
		return false;
	}

	public boolean Switchtoframe(String xpathkey) {

		adl_log.info("Inside switch to frame function with " + xpathkey);
		try {
			WebDriverWait wait = new WebDriverWait(driver, 100);
			WebElement element = (WebElement) wait.until(ExpectedConditions
					.frameToBeAvailableAndSwitchToIt(By.xpath(xpathkey)));
			driver.switchTo().frame(element);
			return true;
		} catch (Throwable t) {
			adl_log.error(
					"Failed to select to frame " + (OR.getProperty(xpathkey)),
					t.fillInStackTrace());
		}
		return false;
	}

	public boolean Switchbacktodefaultcontrol() {

		adl_log.info("Inside switch to default control");
		try {

			driver.switchTo().defaultContent();
			return true;
		} catch (Throwable t) {
			adl_log.error("Failed to switch to default control " + t.fillInStackTrace());
		}
		return false;
	}
}

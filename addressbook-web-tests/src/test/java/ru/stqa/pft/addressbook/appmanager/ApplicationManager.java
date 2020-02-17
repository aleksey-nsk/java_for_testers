package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ApplicationManager {

  private String browser;
  private WebDriver wd;
  private ContactHelper contactHelper;
  private GroupHelper groupHelper;
  private NavigationHelper navigationHelper;
  private DbHelper dbHelper;
  private SessionHelper sessionHelper;
  private final Properties properties;

  // Конструктор
  public ApplicationManager(String browser) {
    this.browser = browser;
    properties = new Properties();
  }

  // Методы Геттеры
  public ContactHelper getContactHelper() { return contactHelper; }
  public GroupHelper getGroupHelper() { return groupHelper; }
  public NavigationHelper getNavigationHelper() { return navigationHelper; }
  public DbHelper getDbHelper() { return dbHelper; }

  public void init() throws IOException {
    System.out.println("Внутри метода init()");

    String target = System.getProperty("target", "local"); // в качестве дефолтного значения "local"
    properties.load(new FileReader(new File(
        String.format("src/test/resources/%s.properties", target)
    )));

    // Выбираем нужный браузер
    if (properties.getProperty("selenium.server").equals("")) {
      System.out.println("Не используем Selenium Server");
      if (browser.equals(BrowserType.FIREFOX)) {
        System.setProperty("webdriver.gecko.driver", "C:\\Tools\\geckodriver-v0.23.0-win64.exe");
        wd = new FirefoxDriver(); // инициализация драйвера
      } else if (browser.equals(BrowserType.CHROME)) {
        System.setProperty("webdriver.chrome.driver", "C:\\Tools\\chromedriver_win32.exe");
        wd = new ChromeDriver(); // инициализация драйвера
      } else if (browser.equals(BrowserType.IE)) {
        System.setProperty("webdriver.ie.driver", "C:\\Tools\\IEDriverServer_Win32_3.12.0.exe");
        wd = new InternetExplorerDriver(); // инициализация драйвера
      }
    } else {
      System.out.println("Будем использовать удалённый Selenium Server");
      URL url = new URL(properties.getProperty("selenium.server"));
      DesiredCapabilities capabilities = new DesiredCapabilities();
      capabilities.setBrowserName(browser);
      capabilities.setPlatform(Platform.fromString(System.getProperty("platform", "WIN10")));
      wd = new RemoteWebDriver(url, capabilities);
    }

    wd.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); // неявное ожидание
    wd.manage().window().maximize();

    contactHelper = new ContactHelper(wd);
    groupHelper = new GroupHelper(wd);
    navigationHelper = new NavigationHelper(wd);
    dbHelper = new DbHelper();
    sessionHelper = new SessionHelper(wd);

    // sessionHelper.login("admin", "secret", "http://localhost/addressbook/");
    sessionHelper.login(properties.getProperty("web.adminLogin"),
                        properties.getProperty("web.adminPassword"),
                        properties.getProperty("web.baseUrl"));
  }

  public void stop() {
    System.out.println("Внутри метода stop()");
    sessionHelper.logout();
    wd.quit();
  }
}

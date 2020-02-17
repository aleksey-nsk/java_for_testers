package ru.stqa.pft.mantis.appmanager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ApplicationManager {

  private String browser;
  private WebDriver wd;
  private final Properties properties;
  private RegistrationHelper registrationHelper;
  private FtpHelper ftp;
  private MailHelper mailHelper;
  private JamesHelper jamesHelper;
  private SoapHelper soapHelper;

  public ApplicationManager(String browser) {
    this.browser = browser;
    properties = new Properties();
  }

  public void init() throws IOException {
    System.out.println("Внутри метода init()");
    String target = System.getProperty("target", "local");
    properties.load(new FileReader(new File(String.format("src/test/resources/%s.properties", target))));
  }

  public void stop() {
    System.out.println("Внутри метода stop()");
    if (wd != null) {
      System.out.println("  драйвер был проинициализирован => останавливаем его");
      wd.quit();
    }
  }

  // Метод getDriver() будет инициализировать драйвер в момент первого обращения.
  // Для того чтобы инициализация стала "ленивой", нужно её перенести именно сюда, в этот метод
  public WebDriver getDriver() {
    System.out.println("Внутри метода getDriver()");

    if (wd == null)
    {
      System.out.println("  драйвер не проинициализирован => надо его проинициализировать");

      if (browser.equals(BrowserType.FIREFOX)) {
        System.setProperty("webdriver.gecko.driver", "C:\\Tools\\geckodriver-v0.23.0-win64.exe");
        wd = new FirefoxDriver();
      } else if (browser.equals(BrowserType.CHROME)) {
        System.setProperty("webdriver.chrome.driver", "C:\\Tools\\chromedriver_win32.exe");
        wd = new ChromeDriver();
      } else if (browser.equals(BrowserType.IE)) {
        System.setProperty("webdriver.ie.driver", "C:\\Tools\\IEDriverServer_Win32_3.12.0.exe");
        wd = new InternetExplorerDriver();
      }

      wd.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
      wd.manage().window().maximize();
    }

    System.out.println("  возвращаем драйвер");
    return wd;
  }

  public String getProperty(String key) {
    System.out.print("  внутри метода getProperty() --> передан параметр key: " + key);
    final String property = properties.getProperty(key);
    System.out.println(" --> property: " + property);
    return property;
  }

  public HttpSession newSession() {
    System.out.println("Метод newSession() инициализирует помощника типа HttpSession при каждом обращении");
    return new HttpSession(this);
  }

  // В методе registration() тоже реализуем "ленивую инициализацию"
  public RegistrationHelper registration() {
    System.out.println("Внутри метода registration()");

    if (registrationHelper == null) {
      System.out.println("  инициализируем registrationHelper");
      registrationHelper = new RegistrationHelper(this);
    }

    System.out.println("  возвращаем registrationHelper");
    return registrationHelper;
  }
  // Таким образом при помощи шаблона "Ленивая инициализация"
  // мы можем откладывать тяжеловесные действия на более поздний этап
  // (а если они никогда не понадобятся, то никогда их не выполнять).
  //
  // Не обязательно всё делать сразу при инициализации.
  // Можно это делать ровно тогда, когда какой-то ресурс кому-то понадобился.
  // Вот в этот момент его и нужно инициализировать.

  public FtpHelper ftp() {
    System.out.println("Внутри метода ftp()");
    if (ftp == null) {
      System.out.println("  инициализируем ftp");
      ftp = new FtpHelper(this);
    }
    System.out.println("  возвращаем ftp");
    return ftp;
  }

  public MailHelper mail() {
    System.out.println("Внутри метода mail()");
    if (mailHelper == null) {
      System.out.println("  инициализируем mailHelper");
      mailHelper = new MailHelper(this);
    }
    System.out.println("  возвращаем mailHelper");
    return mailHelper;
  }

  public JamesHelper james() {
    System.out.println("Внутри метода james()");
    if (jamesHelper == null) {
      System.out.println("  инициализируем jamesHelper");
      jamesHelper = new JamesHelper(this);
    }
    System.out.println("  возвращаем jamesHelper");
    return jamesHelper;
  }

  public SoapHelper soap() {
    System.out.println("Внутри метода soap()");
    if (soapHelper == null) {
      System.out.println("  инициализируем soapHelper");
      soapHelper = new SoapHelper(this);
    }
    System.out.println("  возвращаем soapHelper");
    return soapHelper;
  }
}

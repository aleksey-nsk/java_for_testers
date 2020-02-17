package ru.stqa.pft.addressbook.tests;

import org.openqa.selenium.remote.BrowserType;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import ru.stqa.pft.addressbook.appmanager.ApplicationManager;

public class TestBase {

  /*
  // Переменная для управления выбором браузера
  private static final String browser = BrowserType.FIREFOX;

  // Пометим переменную app как static, чтобы она стала общей для всех тестов
  // Теперь переменная app будет глобальной, общей для всех объектов
  protected static final ApplicationManager app = new ApplicationManager(browser);
  */

  // Теперь будем указывать браузер в Системном Свойстве
  // Взять значение Системного Свойсва "browser"
  protected static final ApplicationManager app
      = new ApplicationManager(System.getProperty("browser", BrowserType.CHROME));

  @BeforeSuite
  public void setUp() throws Exception {
    System.out.print("\n\n***** Внутри метода setUp() *****\n\n");
    app.init();
  }

  @AfterSuite(alwaysRun = true)
  public void tearDown() throws Exception {
    System.out.print("\n\n***** Внутри метода tearDown() *****\n\n");
    app.stop();
  }
}

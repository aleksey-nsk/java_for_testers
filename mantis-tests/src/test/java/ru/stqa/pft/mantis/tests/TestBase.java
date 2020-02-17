package ru.stqa.pft.mantis.tests;

import org.openqa.selenium.remote.BrowserType;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import ru.stqa.pft.mantis.appmanager.ApplicationManager;
import java.io.IOException;

// Базовый класс для всех тестов
public class TestBase {

  protected static final ApplicationManager app
      = new ApplicationManager(System.getProperty("browser", BrowserType.CHROME));

  @BeforeSuite
  public void setUp() throws IOException {
    System.out.print("\n\n***** Внутри метода setUp() *****\n\n");
    app.init();
  }

  @AfterSuite(alwaysRun = true)
  public void tearDown() {
    System.out.print("\n\n***** Внутри метода tearDown() *****\n\n");
    app.stop();
  }
}

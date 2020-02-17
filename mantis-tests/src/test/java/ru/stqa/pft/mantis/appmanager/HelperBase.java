package ru.stqa.pft.mantis.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import java.io.File;

public class HelperBase {

  protected ApplicationManager app;
  protected WebDriver wd;

  public HelperBase(ApplicationManager app) {
    this.app = app;
    this.wd = app.getDriver(); // используем "ленивую инициализацию"
  }

  protected void type(By locator, String text) {
    System.out.print("  метод type() для заполнения отдельного поля");
    if (text != null) {
      final String existingText = wd.findElement(locator).getAttribute("value"); // текущее значение поля ввода
      if (! text.equals(existingText)) {
        wd.findElement(locator).clear();
        wd.findElement(locator).sendKeys(text);
        System.out.println(" --> поле было заполнено переданным значением '" + text + "'");
      } else {
        System.out.println(" --> переданное значение '" + text + "' совпадает с текущим значением поля ввода");
      }
    } else {
      System.out.println(" --> передали null, значит хотим оставить текущее значение в поле");
    }
  }

  protected void attach(By locator, File file) {
    System.out.print("  метод attach() для прикрепления фотографии контакта");
    if (file != null) {
      String absolutePath = file.getAbsolutePath(); // абсолютный путь к файлу
      wd.findElement(locator).sendKeys(absolutePath);
      System.out.println(" --> файловое поле ввода было заполнено значением '" + absolutePath + "'");
    }
  }

  protected void click(By locator) {
    System.out.print("  метод click() для нажатия на кнопку/ссылку/checkbox");
    System.out.println(" --> нажимаем на элемент с локатором [" + locator + "]");
    wd.findElement(locator).click();
  }

  protected void openUrl(String url) {
    System.out.println("  открываем URL: " + url);
    wd.get(url);
  }

  protected void closeAlert() {
    System.out.println("  метод для закрытия диалогового окна (alert)");
    wd.switchTo().alert().accept();
  }

  public boolean isAlertPresent() {
    System.out.println("Проверяем наличие диалогового окна");
    try {
      wd.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  public boolean isElementPresent(By locator) {
    System.out.print("  проверяем наличие элемента по указанному локатору [" + locator + "]");
    try {
      wd.findElement(locator);
      System.out.println(" --> элемент присутствует");
      return true;
    } catch (NoSuchElementException e) {
      System.out.println(" --> элемент отсутствует");
      return false;
    }
  }

  public boolean hasElementText(By locator, String text) {
    System.out.print("  имеет ли элемент с локатором [" + locator + "] текст '" + text + "'");
    if (wd.findElement(locator).getText().equals(text)) {
      System.out.println(" --> ДА");
      return true;
    } else {
      System.out.println(" --> НЕТ");
      return false;
    }
  }
}

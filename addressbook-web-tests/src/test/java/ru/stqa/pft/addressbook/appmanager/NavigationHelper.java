package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

// Класс-хелпер помогающий работать с меню
public class NavigationHelper extends HelperBase {

  public NavigationHelper(WebDriver wd) {
    super(wd);
  }

  public void gotoGroupPage() {
    System.out.println("Внутри метода gotoGroupPage()");
    if (isElementPresent(By.tagName("h1"))
        && hasElementText(By.tagName("h1"), "Groups")
        && isElementPresent(By.name("new")))
    {
      System.out.println("  уже находимся на нужной странице");
    }
    else
    {
      System.out.println("  нажимаем в меню ссылку 'groups'");
      click(By.linkText("groups"));
    }
  }

  public void gotoHomePage() {
    System.out.println("Внутри метода gotoHomePage()");
    if (isElementPresent(By.id("maintable"))) {
      System.out.println("  уже находимся на нужной странице");
    } else {
      System.out.println("  нажать в меню ссылку 'home'");
      click(By.linkText("home"));
    }
  }

  public void waitTitle(final String title) {
    System.out.println("Внутри метода waitTitle()");
    System.out.println("  ожидаем заголовок '" + title + "'");
    (new WebDriverWait(wd, 10)).until(ExpectedConditions.titleIs(title));
  }
}

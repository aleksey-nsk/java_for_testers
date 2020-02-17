package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

// Класс-хелпер помогающий выполнять вход (login)
// и выход (logout)
public class SessionHelper extends HelperBase {

  public SessionHelper(WebDriver wd) {
    super(wd);
  }

  public void login(String username, String password, String url) {
    System.out.println("Войти в адресную книгу (Login)");
    openUrl(url);
    type(By.name("user"), username);
    type(By.name("pass"), password);
    click(By.xpath("//input[@value='Login']"));
  }

  public void logout() {
    System.out.println("Выйти из адресной книги (Logout)");
    click(By.linkText("Logout"));
  }
}

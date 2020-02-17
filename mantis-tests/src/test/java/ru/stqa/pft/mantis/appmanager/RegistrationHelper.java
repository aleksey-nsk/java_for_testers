package ru.stqa.pft.mantis.appmanager;

import org.openqa.selenium.By;

public class RegistrationHelper extends HelperBase {

  public RegistrationHelper(ApplicationManager app) {
    super(app);
  }

  public void start(String username, String email) {
    System.out.println("Метод start() вызван с параметрами:");
    System.out.println("  username: " + username);
    System.out.println("  email: " + email);

    final String url = app.getProperty("web.baseUrl") + "signup_page.php";
    openUrl(url);

    type(By.name("username"), username);
    type(By.name("email"), email);
    click(By.cssSelector("input[value='Зарегистрироваться']"));
  }

  public void finish(String confirmationLink, String username, String password) {
    System.out.println("Метод finish()");
    openUrl(confirmationLink);
    type(By.name("realname"), username);
    type(By.name("password"), password);
    type(By.name("password_confirm"), password);
    click(By.xpath("//form[@id='account-update-form']//span[@class='submit-button']/button"));
  }
}

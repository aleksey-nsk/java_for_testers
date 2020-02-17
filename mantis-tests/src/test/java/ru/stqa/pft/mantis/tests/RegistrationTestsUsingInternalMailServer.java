package ru.stqa.pft.mantis.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.lanwen.verbalregex.VerbalExpression;
import ru.stqa.pft.mantis.model.MailMessage;
import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import static org.testng.Assert.assertTrue;

// В этом классе реализован тест для регистрации
// с использованием встроенного в тесты почтового сервера
public class RegistrationTestsUsingInternalMailServer extends TestBase {

  @BeforeMethod
  public void beforeRegistration() throws IOException {
    System.out.print("\n\n***** Внутри метода beforeRegistration() *****\n\n");
    app.ftp().upload(new File("src/test/resources/config_inc.php"), "config_inc.php", "config_inc.php.backup");
    app.mail().start();
  }

  @Test
  public void testRegistration() throws IOException, MessagingException {
    System.out.print("\n\n***** Внутри метода testRegistration() *****\n\n");

    final long now = System.currentTimeMillis();
    final String username = String.format("user%s", now);
    final String email = String.format("user%s@localhost.localdomain", now);
    final String password = "password123";

    app.registration().start(username, email);
    List<MailMessage> mailMessages = app.mail().waitForMail(2, 10_000); // Mantis отправляет 2 письма: юзеру и админу
    final String confirmationLink = findConfirmationLink(mailMessages, email);
    app.registration().finish(confirmationLink, username, password);

    System.out.println("Регистрация юзера завершена. Теперь надо проверить, что он действительно может войти в систему");
    assertTrue(app.newSession().login(username, password));
  }

  @AfterMethod(alwaysRun = true)
  public void afterRegistration() throws IOException {
    System.out.print("\n\n***** Внутри метода afterRegistration() *****\n\n");
    app.mail().stop();
    app.ftp().restore("config_inc.php", "config_inc.php.backup");
  }

  private String findConfirmationLink(List<MailMessage> mailMessages, String email) {
    System.out.println("Метод findConfirmationLink() извлекает ссылку для подтверждения регистрации");

    MailMessage userMessage = mailMessages.stream().filter(m -> m.to.equals(email)).findAny().get();
    final String userMessageText = userMessage.text;
    System.out.println("  текст сообщения пользователю: \n\n" + userMessageText);

    VerbalExpression regex = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build();
    final String confirmationLink = regex.getText(userMessageText);
    System.out.println("  ссылка для подтверждения регистрации: " + confirmationLink);

    return confirmationLink;
  }
}

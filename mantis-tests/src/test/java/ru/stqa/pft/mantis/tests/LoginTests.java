package ru.stqa.pft.mantis.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.mantis.appmanager.HttpSession;
import java.io.IOException;
import static org.testng.Assert.assertTrue;

public class LoginTests extends TestBase {

  @Test
  public void testLogin() throws IOException {
    System.out.print("\n\n***** Внутри метода testLogin() *****\n\n");
    HttpSession session = app.newSession(); // создаём новую сессию
    assertTrue(session.login("administrator", "root"));
    assertTrue(session.isLoggedInAs("administrator"));
  }
}

package ru.stqa.pft.mantis.appmanager;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Данный класс-помощник не требует доступа к браузеру
// (вместо того чтобы управлять браузером, мы получаем другую схему работы:
// тесты напрямую взаимодействуют с сервером при помощи http-клиента)
public class HttpSession {

  private CloseableHttpClient httpclient;
  private ApplicationManager app;

  public HttpSession(ApplicationManager app) {
    this.app = app;

    // Создаётся новый клиент
    // (новая сессия для работы по протоколу HTTP)
    // (объект httpclient будет отправлять запросы на сервер)
    httpclient = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).build();
  }

  public boolean login(String username, String password) throws IOException {
    System.out.println("Метод login() выполняет вход в систему с параметрами:");
    System.out.println("  username: " + username);
    System.out.println("  password: " + password);

    // Создадим будущий запрос (пока пустой)
    HttpPost post = new HttpPost(app.getProperty("web.baseUrl") + "/login.php");

    // Сформируем набор параметров
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("username", username));
    params.add(new BasicNameValuePair("password", password));
    params.add(new BasicNameValuePair("secure_session", "on"));
    params.add(new BasicNameValuePair("return", "index.php"));

    // Упакуем параметры, и поместим их в заранее созданный запрос
    // (в итоге запрос полностью сформирован и готов к отправке)
    post.setEntity(new UrlEncodedFormEntity(params));

    // Выполним запрос
    // (результатом является ответ от сервера)
    CloseableHttpResponse response = httpclient.execute(post);

    final String body = getTextFrom(response);

    // Проверим что в тексте страницы содержится нужный фрагмент,
    // то есть пользователь действительно успешно вошёл в систему
    final boolean verification = body.contains(String.format("<span class=\"user-info\">%s</span>", username));
    System.out.println("  пользователь успешно вошёл в систему: " + verification);

    return verification;
  }

  public boolean isLoggedInAs(String username) throws IOException {
    System.out.println("Метод isLoggedInAs() определяет, залогинен ли сейчас пользователь '" + username + "'");
    HttpGet get = new HttpGet(app.getProperty("web.baseUrl") + "/index.php");
    CloseableHttpResponse response = httpclient.execute(get);
    final String body = getTextFrom(response);
    final boolean verification = body.contains(String.format("<span class=\"user-info\">%s</span>", username));
    System.out.println("  пользователь залогинен: " + verification);
    return verification;
  }

  private String getTextFrom(CloseableHttpResponse response) throws IOException {
    System.out.println("  вспомогательная функция getTextFrom() для получения текста ответа");
    try {
      final String text = EntityUtils.toString(response.getEntity());
      // System.out.println("text: " + text); // выведем на экран html-код страницы
      return text;
    } finally {
      response.close();
    }
  }
}

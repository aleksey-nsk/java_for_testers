package ru.stqa.pft.mantis.appmanager;

import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;
import ru.stqa.pft.mantis.model.MailMessage;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

// Куда придёт письмо? На каком почтовом сервере его искать?
// Для того чтобы упростить решение этой задачи, мы можем сделать свой собственный почтовый сервер.
// Сделать такой сервер можно несколькими способами,
// и в этом модуле мы запустим почтовый сервер который встроен непосредственно в тесты.
// И для этой цели будет использоваться помощник MailHelper.
// Также необходимо подключить библиотеку "SubEtha SMTP".
public class MailHelper {

  private ApplicationManager app;
  private final Wiser wiser;

  public MailHelper(ApplicationManager app) {
    this.app = app;
    wiser = new Wiser(); // объект wiser это и есть почтовый сервер
  }

  public void start() {
    System.out.println("Метод start() запускает почтовый сервер");
    wiser.start();
  }

  public void stop() {
    System.out.println("Метод stop() останавливает почтовый сервер");
    wiser.stop();
  }

  public List<MailMessage> waitForMail(int count, long timeout) throws MessagingException, IOException {
    System.out.println("Внутри метода waitForMail() реализовано ожидание почты. Параметры:");
    System.out.println("  количество писем которые должны придти: count = " + count);
    System.out.println("  время ожидания в миллисекундах: timeout = " + timeout);
    long start = System.currentTimeMillis();

    while (System.currentTimeMillis() < start + timeout)
    {
      System.out.println("  ожидаем почту...");
      if (wiser.getMessages().size() >= count) {
        System.out.println("  дождались!");
        return wiser.getMessages().stream().map(m -> toModelMail(m)).collect(Collectors.toList());
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    throw new Error("Не удалось дождаться требуемого количества писем за время ожидания");
  }

  public static MailMessage toModelMail(WiserMessage m) {
    System.out.println("Метод toModelMail()");
    System.out.println("  преобразуем реальное почтовое сообщение в модельный объект");
    try {
      MimeMessage mm = m.getMimeMessage();
      return new MailMessage(mm.getAllRecipients()[0].toString(), (String) mm.getContent());
    } catch (MessagingException e) {
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}

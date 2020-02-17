package ru.stqa.pft.mantis.appmanager;

import org.apache.commons.net.telnet.TelnetClient;
import ru.stqa.pft.mantis.model.MailMessage;
import javax.mail.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// Помощник JamesHelper
// Во-первых он умеет ходить по протоколу Telnet на почтовый сервер и создавать там пользователей
// Во-вторых он умеет получать почту по протоколу POP3
public class JamesHelper {

  private ApplicationManager app;
  private TelnetClient telnet;
  private InputStream in;
  private PrintStream out;
  private Session mailSession;
  private Store store;
  private String mailserver;

  public JamesHelper(ApplicationManager app) {
    this.app = app;
    telnet = new TelnetClient();
    mailSession = Session.getDefaultInstance(System.getProperties()); // создаётся почтовая сессия
  }

  public void createUser(String username, String password) {
    System.out.println("Метод createUser() создаёт пользователя на почтовом сервере. Параметры:");
    System.out.println("  username: " + username);
    System.out.println("  password: " + password);
    initTelnetSession(); // устанавливается соединение по протоколу Telnet
    write("adduser " + username + " " + password); // пишем команду
    String result = readUntil("User " + username + " added"); // ждём данный текст на консоли
    closeTelnetSession(); // разрываем соединение
  }

  public boolean doesUserExist(String name) {
    initTelnetSession();
    write("verify " + name);
    String result = readUntil("exist");
    closeTelnetSession();
    return result.trim().equals("User " + name + " exist");
  }

  public void deleteUser(String name) {
    initTelnetSession();
    write("deluser " + name);
    String result = readUntil("User " + name + " deleted");
    closeTelnetSession();
  }

  private void initTelnetSession() {
    System.out.println("В методе initTelnetSession() устанавливается соединение по протоколу Telnet");
    mailserver = app.getProperty("mailserver.host");
    int port = Integer.parseInt(app.getProperty("mailserver.port"));
    String login = app.getProperty("mailserver.adminlogin");
    String password = app.getProperty("mailserver.adminpassword");

    try {
      telnet.connect(mailserver, port); // устанавливаем соединение с почтовым сервером
      in = telnet.getInputStream(); // берём у соединения входной поток (входной поток исп-ется для чтения)
      out = new PrintStream(telnet.getOutputStream()); // берём у соединения выходной поток (исп-ется для записи)
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // Don't know why it doesn't allow login at the first attempt
    readUntil("Login id:");
    write("");
    readUntil("Password:");
    write("");

    // Second login attempt, must be successful
    readUntil("Login id:");
    write(login);
    readUntil("Password:");
    write(password);

    // Read welcome message
    readUntil("Welcome " + login + ". HELP for a list of commands");
  }

  private String readUntil(String pattern) {
    System.out.println("Метод readUntil() ждёт на консоли текст: " + pattern);
    try {
      char lastChar = pattern.charAt(pattern.length() - 1);
      StringBuffer sb = new StringBuffer();
      char ch = (char) in.read();
      while (true) {
        System.out.print(ch);
        sb.append(ch);
        if (ch == lastChar) {
          if (sb.toString().endsWith(pattern)) {
            return sb.toString();
          }
        }
        ch = (char) in.read();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private void write(String value) {
    System.out.println("\n\nМетод write() пишет команду: " + value);
    try {
      out.println(value);
      out.flush();
      System.out.println(value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void closeTelnetSession() {
    System.out.println("\n\nМетод closeTelnetSession() разрывает соединение");
    write("quit");
  }

  public void drainEmail(String username, String password) throws MessagingException {
    Folder inbox = openInbox(username, password);
    for (Message message : inbox.getMessages()) {
      message.setFlag(Flags.Flag.DELETED, true);
    }
    closeFolder(inbox);
  }

  private void closeFolder(Folder folder) throws MessagingException {
    System.out.println("Метод closeFolder() закрывает почтовый ящик");
    folder.close(true);
    store.close();
  }

  private Folder openInbox(String username, String password) throws MessagingException {
    System.out.println("Метод openInbox() открывает почтовый ящик. Параметры:");
    System.out.println("  username: " + username);
    System.out.println("  password: " + password);

    // Берём почтовую сессию и сообщаем что
    // мы хотим использовать протокол POP3 для доступа к хранилищу почты
    store = mailSession.getStore("pop3");

    store.connect(mailserver, username, password); // устанавливаем соединение по протоколу POP3
    Folder folder = store.getDefaultFolder().getFolder("INBOX"); // получаем доступ к папке INBOX
    folder.open(Folder.READ_WRITE); // открываем папку INBOX на чтение и на запись
    return folder;
  }

  public List<MailMessage> waitForMail(String username, String password, long timeout) throws MessagingException {
    System.out.println("Внутри метода waitForMail(). Параметры:");
    System.out.println("  username: " + username);
    System.out.println("  password: " + password);
    System.out.println("  timeout: " + timeout);
    long now = System.currentTimeMillis();
    while (System.currentTimeMillis() < now + timeout) {
      List<MailMessage> allMail = getAllMail(username, password);
      if (allMail.size() > 0) {
        return allMail;
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    throw new Error("No mail :(");
  }

  public List<MailMessage> getAllMail(String username, String password) throws MessagingException {
    System.out.println("Метод getAllMail() извлекает сообщения из почтового ящика, и превращает их в модельные объекты");
    Folder inbox = openInbox(username, password); // открываем почтовый ящик
    List<MailMessage> messages = Arrays.asList(inbox.getMessages()).stream().map(m -> toModelMail(m)).collect(Collectors.toList());
    System.out.println("messages.size(): " + messages.size());
    closeFolder(inbox); // закрываем почтовый ящик
    System.out.println("");
    return messages;
  }

  public static MailMessage toModelMail(Message m) {
    System.out.println("Внутри метода toModelMail()");
    try {
      return new MailMessage(m.getAllRecipients()[0].toString(), (String) m.getContent());
    } catch (MessagingException e) {
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}

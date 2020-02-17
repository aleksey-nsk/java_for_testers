package ru.stqa.pft.mantis.appmanager;

import org.apache.commons.net.ftp.FTPClient;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FtpHelper {

  private final ApplicationManager app;
  private FTPClient ftp;

  public FtpHelper(ApplicationManager app) {
    this.app = app;
    ftp = new FTPClient();
  }

  // file -- локальный файл, который должен быть загружен на удалённую машину
  // target -- имя файла на удалённой машине
  // backup -- имя резервной копии
  public void upload(File file, String target, String backup) throws IOException {
    System.out.println("Метод upload() загружает новый файл (старый файл временно переименовывает)");
    System.out.println("  target: " + target);
    System.out.println("  backup: " + backup);
    ftp.connect(app.getProperty("ftp.host")); // устанавливаем соединение с удалённым сервером по протоколу FTP
    ftp.login(app.getProperty("ftp.login"), app.getProperty("ftp.password")); // логинимся
    ftp.deleteFile(backup); // удаляем предыдущую резервную копию
    ftp.rename(target, backup); // переименовываем файл (делаем резервную копию)
    ftp.enterLocalPassiveMode(); // включаем пассивный режим передачи данных
    ftp.storeFile(target, new FileInputStream(file)); // передаём локальный файл (сохраняем под именем target)
    ftp.disconnect(); // разрываем соединение
  }

  public void restore(String target, String backup) throws IOException {
    System.out.println("Метод restore() восстанавливает старый файл");
    System.out.println("  target: " + target);
    System.out.println("  backup: " + backup);
    ftp.connect(app.getProperty("ftp.host"));
    ftp.login(app.getProperty("ftp.login"), app.getProperty("ftp.password"));
    ftp.deleteFile(target); // удаляем загруженный ранее файл
    ftp.rename(backup, target); // восстанавливаем оригинальный файл из резервной копии
    ftp.disconnect();
  }
}

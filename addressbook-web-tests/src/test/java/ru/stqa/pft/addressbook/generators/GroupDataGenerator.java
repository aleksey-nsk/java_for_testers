package ru.stqa.pft.addressbook.generators;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import ru.stqa.pft.addressbook.model.GroupData;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

// Класс который будет генерировать тестовые данные о группах
// Используем библиотеку JCommander
public class GroupDataGenerator {

  @Parameter(names = "-c", description = "Group count")
  public int count; // количество групп

  @Parameter(names = "-f", description = "Target file")
  public String file; // путь к файлу

  @Parameter(names = "-d", description = "Data format")
  public String dataFormat;

  public static void main(String[] args) throws IOException { // args -- опции командной строки
    // Создаём объект текущего класса
    GroupDataGenerator generator = new GroupDataGenerator();

    // Создаём объект типа JCommander
    JCommander jCommander = new JCommander(generator);

    try {
      jCommander.parse(args);
    } catch (ParameterException ex) {
      // Если возникло исключение ParameterException, тогда с помощью метода usage()
      // выводим на консоль текст, который содержит информацию о доступных опциях
      // (то есть на консоль выводится объяснение, как правильно запускать программу)
      jCommander.usage();

      // Далее return; так как теперь нет смысла запускать генератор
      return;
    }

    // Запускаем генератор
    generator.run();
  }

  private void run() throws IOException {
    List<GroupData> groups = generateGroups(count); // генерируем тестовые данные

    if (dataFormat.equals("csv")) {
      saveAsCsv(groups, new File(file)); // сохраняем данные в файл формата CSV
    } else if (dataFormat.equals("xml")) {
      saveAsXml(groups, new File(file)); // сохраняем данные в файл формата XML
    } else if (dataFormat.equals("json")) {
      saveAsJson(groups, new File(file)); // сохраняем данные в файл формата JSON
    } else {
      System.out.println("Unrecognized data format '" + dataFormat + "'");
    }
  }

  private List<GroupData> generateGroups(int count) {
    System.out.println("Метод generateGroups() для генерации тестовых данных");
    List<GroupData> groups = new ArrayList<GroupData>();
    for (int i = 0; i < count; i++) {
      GroupData newGroup = new GroupData().withName(String.format("test %s", i))
                                          .withHeader(String.format("header %s", i))
                                          .withFooter(String.format("footer %s", i));
      groups.add(newGroup);
    }
    return groups;
  }

  private void saveAsCsv(List<GroupData> groups, File file) throws IOException {
    System.out.println("Метод saveAsCsv() для сохранения данных в файл формата CSV");
    System.out.println("  текущая директория = " + new File(".").getAbsolutePath());

    System.out.println("  открываем файл на запись");
    Writer writer = new FileWriter(file);

    System.out.println("  записываем в файл (используем формат CSV, в качестве разделителя символ ; )");
    for (GroupData group : groups) {
      String oneLine = String.format("%s;%s;%s\n", group.getName(), group.getHeader(), group.getFooter());
      System.out.print("    " + oneLine);
      writer.write(oneLine);
    }

    System.out.println("  закрываем файл");
    writer.close();
  }

  private void saveAsXml(List<GroupData> groups, File file) throws IOException {
    System.out.println("Метод saveAsXml() для сохранения данных в файл формата XML");

    XStream xstream = new XStream(); // to use XStream, simply instantiate the XStream class
    xstream.processAnnotations(GroupData.class);
    String xml = xstream.toXML(groups); // to convert it to XML, make a simple call to XStream

    // Конструкция try-with-resources
    try (Writer writer = new FileWriter(file) /* инициализация ресурса */ ) {
      writer.write(xml); /* использование ресурса */
    }
  }

  private void saveAsJson(List<GroupData> groups, File file) throws IOException {
    System.out.println("Метод saveAsJson() для сохранения данных в файл формата JSON");

    // Gson gson = new Gson();
    Gson gson = new GsonBuilder().setPrettyPrinting()
                                 .excludeFieldsWithoutExposeAnnotation()
                                 .create();
    String json = gson.toJson(groups);

    // Конструкция try-with-resources
    try (Writer writer = new FileWriter(file)) {
      writer.write(json);
    }
  }
}

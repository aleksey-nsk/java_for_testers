package ru.stqa.pft.addressbook.generators;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.stqa.pft.addressbook.model.ContactData;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class ContactDataGenerator {

  @Parameter(names = "-c", description = "Contact count")
  public int count;

  @Parameter(names = "-f", description = "Target file")
  public String file;

  @Parameter(names = "-d", description = "Data format")
  public String dataFormat;

  public static void main(String[] args) throws IOException {
    ContactDataGenerator generator = new ContactDataGenerator();
    JCommander jCommander = new JCommander(generator);
    try {
      jCommander.parse(args);
    } catch (ParameterException ex) {
      jCommander.usage();
      return;
    }
    generator.run();
  }

  private void run() throws IOException {
    List<ContactData> contacts = generateContacts(count);
    if (dataFormat.equals("json")) {
      saveAsJson(contacts, new File(file));
    } else {
      System.out.println("Неверный формат данных '" + dataFormat + "'. Используйте 'json'");
    }
  }

  private List<ContactData> generateContacts(int count) {
    System.out.println("Метод generateContacts() для генерации тестовых данных");
    List<ContactData> contacts = new ArrayList<ContactData>();
    for (int i = 0; i < count; i++) {
      ContactData newContact = new ContactData().withFirstname("Firstname" + i)
                                                .withLastname("Lastname" + i)
                                                .withMobilePhone("+7 (913) 777-99-33")
                                                .withEmail("email" + i + "@test.ru")
                                                .withGroup("test1")
                                                .withPhoto("src/test/resources/stru.png");
      contacts.add(newContact);
    }
    return contacts;
  }

  private void saveAsJson(List<ContactData> contacts, File file) throws IOException {
    System.out.println("Метод saveAsJson() для сохранения данных в файл формата JSON");
    Gson gson = new GsonBuilder().setPrettyPrinting()
                                 .excludeFieldsWithoutExposeAnnotation()
                                 .create();
    String json = gson.toJson(contacts);
    try (Writer writer = new FileWriter(file)) {
      writer.write(json);
    }
  }
}

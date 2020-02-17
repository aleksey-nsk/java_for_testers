package ru.stqa.pft.addressbook.tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

// Класс содержащий тесты по созданию контактов
public class ContactCreationTests extends TestBase {

  @DataProvider
  public Iterator<Object[]> validContactsFromJson() throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(new File("src/test/resources/contacts.json")))) {
      String json = "";
      String line = reader.readLine();
      while (line != null) {
        json += line;
        line = reader.readLine();
      }
      Gson gson = new Gson();
      List<ContactData> contacts = gson.fromJson(json, new TypeToken<List<ContactData>>() {}.getType());
      Iterator<Object[]> result = contacts.stream()
          .map(c -> new Object[] {c})
          .collect(Collectors.toList())
          .iterator();
      return result;
    }
  }

  @BeforeMethod
  public void ensurePreconditions() {
    System.out.print("\n\n***** Внутри метода ensurePreconditions() *****\n\n");
    app.getNavigationHelper().gotoHomePage();
  }

  @Test(dataProvider = "validContactsFromJson")
  public void testContactCreation(ContactData contact) throws Exception {
    System.out.print("\n\n***** Внутри метода testContactCreation() *****\n\n");
    Contacts before = app.getContactHelper().getContactSet();
    app.getContactHelper().createContact(contact, true);
    assertThat(app.getContactHelper().getContactCount(), equalTo(before.size() + 1));
    Contacts after = app.getContactHelper().getContactSet();

    System.out.println("Найдём максимальный идентификатор, и присвоим его контакту");
    ToIntFunction<? super ContactData> mapper = c -> c.getId();
    int maxId = after.stream().mapToInt(mapper).max().getAsInt();
    contact.withId(maxId);
    System.out.println("  контакту был присвоен максимальный идентификатор = " + maxId);

    System.out.println("Сравним коллекции");
    assertThat(after, equalTo(before.withAdded(contact)));
  }

  @Test(enabled = false) // тест отключен
  public void testCurrentDir() {
    // Вспомогательный тест, в котором определим, какая директория является
    // текущей рабочей директорией
    // во время выполнения тестов

    File currentDir = new File("."); // точка это и есть текущая директория

    System.out.println("Абсолютный путь к директории = " + currentDir.getAbsolutePath());
    // Абсолютный путь к директории = D:\ОБЩИЕ_ДОКУМЕНТЫ\IT\java_course\addressbook-web-tests\.
    // => видим что текущей директорией является корневая директория модуля (addressbook-web-tests)

    File photo = new File("src/test/resources/stru.png"); // относительный путь к файлу
    System.out.println("Абсолютный путь к файлу photo = " + photo.getAbsolutePath());
    // Абсолютный путь к файлу photo = D:\ОБЩИЕ_ДОКУМЕНТЫ\IT\java_course\addressbook-web-tests\src\test\resources\stru.png

    // Убедимся что данный файл действительно существует
    System.out.println("Существует ли данный файл: " + photo.exists()); // Существует ли данный файл: true
  }
}

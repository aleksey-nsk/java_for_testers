package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactPhoneTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    System.out.print("\n\n***** Внутри метода ensurePreconditions() *****\n\n");
    app.getNavigationHelper().gotoHomePage();
    if (! app.getContactHelper().isThereAContact()) {
      ContactData contact = new ContactData().withFirstname("Firstname")
                                             .withLastname("Lastname")
                                             .withMobilePhone("89137779933")
                                             .withEmail("email@test.ru")
                                             .withGroup("test1");
      app.getContactHelper().createContact(contact, true);
    }
  }

  @Test
  public void testContactPhones() {
    System.out.print("\n\n***** Внутри метода testContactPhones() *****\n\n");
    ContactData contact = app.getContactHelper().getContactSet().iterator().next(); // случайным образом выберем контакт
    ContactData contactInfoFromEditForm = app.getContactHelper().infoFromEditForm(contact);

    // Теперь надо сравнить два объекта (contact и contactInfoFromEditForm)
    // Не будем менять метод equals() в классе ContactData, а вместо этого
    // прямо тут напишем нужные проверки
    System.out.println("Сравним телефонные номера");
    assertThat(contact.getAllPhones(), equalTo(mergePhones(contactInfoFromEditForm)));
  }

  // Внутри метода mergePhones() воспользуемся
  // элементами функционального программирования
  private String mergePhones(ContactData contact) {
    System.out.println("  внутри метода mergePhones()");
    List<String> listOfPhones = Arrays.asList(contact.getHomePhone(), contact.getMobilePhone(), contact.getWorkPhone());
    String result = listOfPhones.stream()                           // превратили список в поток
                                .filter(s -> ! s.equals(""))        // отфильтровали поток (выбросили пустые строки)
                                .map(ContactPhoneTests::cleaned)    // применили функцию cleaned() ко всем элементам потока
                                .collect(Collectors.joining("\n")); // поток непустых строк склеиваем в одну строку
    return result;
  }

  public static String cleaned(String phone) {
    System.out.println("    вспомогательная функция cleaned() удаляет ненужные символы");
    return phone.replaceAll("\\s", "").replaceAll("[-()]", "");
  }
}

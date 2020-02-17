package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

// Класс содержащий тесты по редактированию контактов
public class ContactModificationTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    System.out.print("\n\n***** Внутри метода ensurePreconditions() *****\n\n");
    app.getNavigationHelper().gotoHomePage();
    if (! app.getContactHelper().isThereAContact()) {
      ContactData contact = new ContactData().withFirstname("Firstname")
                                             .withLastname("Lastname")
                                             .withMobilePhone("+7 (913) 777-99-33")
                                             .withEmail("email@test.ru")
                                             .withGroup("test1");
      app.getContactHelper().createContact(contact, true);
    }
  }

  @Test
  public void testContactModification() {
    System.out.print("\n\n***** Внутри метода testContactModification() *****\n\n");
    Contacts before = app.getContactHelper().getContactSet();
    ContactData modifiedContact = before.iterator().next();
    ContactData contact = new ContactData().withId(modifiedContact.getId())
                                           .withFirstname("First_modify")
                                           .withLastname("Last_modify")
                                           .withMobilePhone("+7 (913) 777-99-33")
                                           .withEmail("modify@test.ru")
                                           .withPhoto("src/test/resources/stru.png");
    app.getContactHelper().modifyContact(contact);

    assertThat(app.getContactHelper().getContactCount(), equalTo(before.size()));
    Contacts after = app.getContactHelper().getContactSet();
    System.out.println("Сравним коллекции");
    assertThat(after, equalTo(before.without(modifiedContact).withAdded(contact)));
  }
}

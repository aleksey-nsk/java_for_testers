package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

// Класс-хелпер помогающий работать с контактами
public class ContactHelper extends HelperBase {

  public ContactHelper(WebDriver wd) {
    super(wd);
  }

  public void initContactCreation() {
    System.out.println("Инициализировать создание нового контакта");
    click(By.linkText("add new"));
  }

  public void fillContactForm(ContactData contactData, boolean creation) {
    System.out.println("Заполнить поля контакта");
    type(By.name("firstname"), contactData.getFirstname());
    type(By.name("lastname"), contactData.getLastname());
    type(By.name("mobile"), contactData.getMobilePhone());
    type(By.name("email"), contactData.getEmail());
    attach(By.name("photo"), new File(contactData.getPhoto()));

    if (creation) {
      System.out.println("  тест для создания нового контакта => заполняем поле 'группа'");
      new Select(wd.findElement(By.name("new_group"))).selectByIndex(1);
    } else {
      System.out.println("  тест для модификации существующего контакта => отсутствует поле 'группа'");
      Assert.assertFalse(isElementPresent(By.name("new_group")));
    }
  }

  public void submitContactCreation() {
    System.out.println("Сохранить данные о новом контакте");
    click(By.xpath("(//input[@name='submit'])[2]"));
  }

  public void returnToHomePage() {
    System.out.println("Вернуться на домашнюю страницу");
    click(By.linkText("home page"));
  }

  public void returnToHome() {
    System.out.println("Нажать в меню ссылку 'home'");
    click(By.linkText("home"));
  }

  public void selectContact(int index) {
    System.out.println("Выбрать " + (index + 1) + "-й по счёту контакт");
    wd.findElements(By.name("selected[]")).get(index).click();
  }

  public void selectContactById(int id) {
    System.out.println("Выбрать контакт с идентификатором id = " + id);
    wd.findElement(By.cssSelector("input[value='" + id + "']")).click();
  }

  public void deleteSelectedContacts() {
    System.out.println("Удалить выбранный контакт");
    click(By.xpath("//input[@value='Delete']"));
    closeAlert();
  }

  public void submitContactModification() {
    System.out.println("Сохранить данные после редактирования контакта");
    click(By.name("update"));
  }

  public void createContact(ContactData contactData, boolean creation) {
    System.out.println("Внутри метода createContact()");
    initContactCreation();
    fillContactForm(contactData, creation);
    submitContactCreation();

    System.out.println("Сбрасываем кэш, так как коллекция контактов поменялась");
    contactCache = null;

    returnToHomePage();
  }

  public void modifyContact(ContactData contact) {
    System.out.println("Внутри метода modifyContact(ContactData contact)");
    initContactModificationById(contact.getId());
    fillContactForm(contact, false);
    submitContactModification();

    System.out.println("Сбрасываем кэш, так как коллекция контактов поменялась");
    contactCache = null;

    returnToHomePage();
  }

  public void deleteContact(ContactData contact) {
    System.out.println("Внутри метода deleteContact(ContactData contact)");
    selectContactById(contact.getId());
    deleteSelectedContacts();

    System.out.println("Сбрасываем кэш, так как коллекция контактов поменялась");
    contactCache = null;

    returnToHome();
  }

  public boolean isThereAContact() {
    System.out.println("Внутри метода isThereAContact()");
    return isElementPresent(By.name("selected[]"));
  }

  public int getContactCount() {
    int result = wd.findElements(By.name("selected[]")).size();
    System.out.println("Посчитали количество контактов: " + result);
    return result;
  }

  public List<ContactData> getContactList() {
    System.out.println("Внутри метода getContactList()");
    List<ContactData> contacts = new ArrayList<ContactData>();
    List<WebElement> elements = wd.findElements(By.xpath("//tr[@name='entry']"));
    for (WebElement element : elements) {
      int id = Integer.parseInt(element.findElement(By.tagName("input")).getAttribute("value"));
      String firstName = element.findElement(By.xpath("./td[3]")).getText();
      String lastName = element.findElement(By.xpath("./td[2]")).getText();
      ContactData contact = new ContactData().withId(id).withFirstname(firstName).withLastname(lastName);
      contacts.add(contact);
    }
    System.out.println("  возвращаем список контактов длиной = " + contacts.size());
    return contacts;
  }

  private Contacts contactCache = null;

  public Contacts getContactSet() {
    System.out.println("Внутри метода getContactSet()");

    System.out.println("  проверяем кэш");
    if (contactCache != null) {
      System.out.println("  кэш не пуст --> contactCache.size() = " + contactCache.size());
      return new Contacts(contactCache);
    }

    System.out.println("  кэш пуст => создадим коллекцию контактов");
    contactCache = new Contacts();
    List<WebElement> rows = wd.findElements(By.name("entry"));
    for (WebElement row : rows) {
      List<WebElement> cells = row.findElements(By.tagName("td"));
      int id = Integer.parseInt(cells.get(0).findElement(By.tagName("input")).getAttribute("value"));
      String lastName = cells.get(1).getText();
      String firstName = cells.get(2).getText();
      String allPhones = cells.get(5).getText();
      ContactData contact = new ContactData().withId(id).withFirstname(firstName).withLastname(lastName).withAllPhones(allPhones);
      contactCache.add(contact);
    }
    System.out.println("  возвращаем количество контактов = " + contactCache.size());
    return new Contacts(contactCache);
  }

  public ContactData infoFromEditForm(ContactData contact) {
    System.out.println("Метод infoFromEditForm() загружает информацию о контакте из формы редактирования");
    initContactModificationById(contact.getId());
    String firstname = wd.findElement(By.name("firstname")).getAttribute("value");
    String lastname = wd.findElement(By.name("lastname")).getAttribute("value");
    String home = wd.findElement(By.name("home")).getAttribute("value");
    String mobile = wd.findElement(By.name("mobile")).getAttribute("value");
    String work = wd.findElement(By.name("work")).getAttribute("value");
    ContactData returnContact = new ContactData().withId(contact.getId())
                                                 .withFirstname(firstname)
                                                 .withLastname(lastname)
                                                 .withHomePhone(home)
                                                 .withMobilePhone(mobile)
                                                 .withWorkPhone(work);
    wd.navigate().back();
    return returnContact;
  }

  private void initContactModification(int index) {
    System.out.println("Открытие формы редактирования " + (index + 1) + "-го по счёту контакта");
    wd.findElements(By.cssSelector("a[href^='edit.php?id=']")).get(index).click();
  }

  private void initContactModificationById(int id) {
    System.out.println("Открытие формы редактирования контакта с идентификатором id = " + id);
    WebElement checkbox = wd.findElement(By.cssSelector(String.format("input[value='%s']", id)));
    WebElement row = checkbox.findElement(By.xpath("./../..")); // выполняем поиск относительно чекбокса
    List<WebElement> cells = row.findElements(By.tagName("td"));
    cells.get(7).findElement(By.tagName("a")).click();
  }
}

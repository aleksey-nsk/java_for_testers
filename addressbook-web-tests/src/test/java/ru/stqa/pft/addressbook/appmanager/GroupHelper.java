package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;
import java.util.ArrayList;
import java.util.List;

// Класс-хелпер помогающий работать с группами
public class GroupHelper extends HelperBase {

  public GroupHelper(WebDriver wd) {
    super(wd);
  }

  public void initGroupCreation() {
    System.out.println("Инициализировать создание новой группы");
    click(By.name("new"));
  }

  public void fillGroupForm(GroupData groupData) {
    System.out.println("Заполнить поля для новой группы");
    type(By.name("group_name"), groupData.getName());
    type(By.name("group_header"), groupData.getHeader());
    type(By.name("group_footer"), groupData.getFooter());
  }

  public void submitGroupCreation() {
    System.out.println("Сохранить данные о новой группе");
    click(By.name("submit"));
  }

  public void returnToGroupPage() {
    System.out.println("Вернуться на страницу со списком групп");
    click(By.linkText("group page"));
  }

  public void selectGroup(int index) {
    System.out.println("Выбрать " + (index + 1) + "-ую по счёту группу");
    wd.findElements(By.name("selected[]")).get(index).click();
  }

  public void selectGroupById(int id) {
    System.out.println("Выбрать группу с идентификатором id = " + id);
    wd.findElement(By.cssSelector("input[value='" + id + "']")).click();
  }

  public void deleteSelectedGroups() {
    System.out.println("Удалить выбранную группу");
    click(By.name("delete"));
  }

  public void initGroupModification() {
    System.out.println("Открытие формы редактирования группы");
    click(By.name("edit"));
  }

  public void submitGroupModification() {
    System.out.println("Сохранить данные после редактирования группы");
    click(By.name("update"));
  }

  public void createGroup(GroupData group) {
    System.out.println("Внутри метода createGroup()");
    initGroupCreation();
    fillGroupForm(group);
    submitGroupCreation();

    System.out.println("Сбрасываем кэш, так как коллекция групп поменялась");
    groupCache = null;

    returnToGroupPage();
  }

  public void modifyGroup(GroupData group) {
    System.out.println("Внутри метода modifyGroup(GroupData group)");
    selectGroupById(group.getId());
    initGroupModification();
    fillGroupForm(group);
    submitGroupModification();

    System.out.println("Сбрасываем кэш, так как коллекция групп поменялась");
    groupCache = null;

    returnToGroupPage();
  }

  public void deleteGroup(GroupData group) {
    System.out.println("Внутри метода deleteGroup(GroupData group)");
    selectGroupById(group.getId());
    deleteSelectedGroups();

    System.out.println("Сбрасываем кэш, так как коллекция групп поменялась");
    groupCache = null;

    returnToGroupPage();
  }

  public boolean isThereAGroup() {
    System.out.println("Внутри метода isThereAGroup()");
    return isElementPresent(By.name("selected[]"));
  }

  public int getGroupCount() {
    int result = wd.findElements(By.name("selected[]")).size();
    System.out.println("Посчитали количество групп: " + result);
    return result;
  }

  public List<GroupData> getGroupList() {
    System.out.println("Внутри метода getGroupList()");

    // Укажем конкретный класс ArrayList
    // который реализует интерфейс List
    List<GroupData> groups = new ArrayList<GroupData>();

    List<WebElement> elements = wd.findElements(By.cssSelector("span.group"));
    for (WebElement element : elements) { // переменная element пробегает по списку elements
      String groupName = element.getText();
      String idAsString = element.findElement(By.tagName("input")).getAttribute("value");
      int id = Integer.parseInt(idAsString); // Integer.parseInt() -- преобразует строку в число
      GroupData group = new GroupData().withId(id).withName(groupName);
      groups.add(group); // добавляем созданный объект в список
    }
    System.out.println("  возвращаем список групп длиной = " + groups.size());
    return groups;
  }

  // Создадим новое поле groupCache в классе GroupHelper, которое
  // изначально будет равно null, то есть пока что кэш пуст
  private Groups groupCache = null;
  
  public Groups getGroupSet() {
    System.out.println("Внутри метода getGroupSet()");

    System.out.println("  проверяем кэш");
    if (groupCache != null) {
      System.out.println("  кэш не пуст --> groupCache.size() = " + groupCache.size());

      // На всякий случай лучше возвращать копию кэша,
      // а не сам кэш. Чтобы сам кэш никто не испортил
      return new Groups(groupCache);
    }

    System.out.println("  кэш пуст => создадим коллекцию групп");
    groupCache = new Groups();
    List<WebElement> elements = wd.findElements(By.cssSelector("span.group"));
    for (WebElement element : elements) {
      String groupName = element.getText();
      String idAsString = element.findElement(By.tagName("input")).getAttribute("value");
      int id = Integer.parseInt(idAsString);
      GroupData group = new GroupData().withId(id).withName(groupName);
      groupCache.add(group);
    }
    System.out.println("  возвращаемое количество групп = " + groupCache.size());
    return new Groups(groupCache); // возвращаем копию кэша
  }
}

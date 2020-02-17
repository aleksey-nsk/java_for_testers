package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

// Класс содержащий тесты по редактированию групп
public class GroupModificationTests extends TestBase {

  @BeforeMethod // перед каждым тестовым методом
  public void ensurePreconditions() { // метод для проверки и обеспечения выполнения предусловий
    System.out.print("\n\n***** Внутри метода ensurePreconditions() *****\n\n");
    if (app.getDbHelper().getGroups().size() == 0) {
      app.getNavigationHelper().gotoGroupPage();
      app.getGroupHelper().createGroup(new GroupData().withName("test1"));
    }
  }

  @Test
  public void testGroupModification() {
    System.out.print("\n\n***** Внутри метода testGroupModification() *****\n\n");
    Groups before = app.getDbHelper().getGroups(); // получаем напрямую из БД
    GroupData modifiedGroup = before.iterator().next();
    GroupData group = new GroupData().withId(modifiedGroup.getId())
                                     .withName("test1_modified")
                                     .withHeader("test2")
                                     .withFooter("test3");
    app.getNavigationHelper().gotoGroupPage();
    app.getGroupHelper().modifyGroup(group);

    assertThat(app.getGroupHelper().getGroupCount(), equalTo(before.size()));
    Groups after = app.getDbHelper().getGroups(); // получаем напрямую из БД
    System.out.println("Сравним коллекции");
    assertThat(after, equalTo(before.without(modifiedGroup).withAdded(group)));
  }

  @Test(enabled = false) // тест отключен
  public void simpleTest() {
    System.out.print("\n\n***** Внутри метода simpleTest() *****\n\n");
    app.getNavigationHelper().gotoGroupPage();
    app.getNavigationHelper().waitTitle("Groups | Address Book");
  }
}

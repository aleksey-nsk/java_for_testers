package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.*;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

// Класс содержащий тесты по удалению групп
public class GroupDeletionTests extends TestBase {

  @BeforeMethod // перед каждым тестовым методом
  public void ensurePreconditions() { // метод для проверки и обеспечения выполнения предусловий
    System.out.print("\n\n***** Внутри метода ensurePreconditions() *****\n\n");
    app.getNavigationHelper().gotoGroupPage();
    if (! app.getGroupHelper().isThereAGroup()) {
      app.getGroupHelper().createGroup(new GroupData().withName("test1"));
    }
  }

  @Test
  public void testGroupDeletion() throws Exception {
    System.out.print("\n\n***** Внутри метода testGroupDeletion() *****\n\n");
    Groups before = app.getGroupHelper().getGroupSet();

    // iterator() позволяет последовательно перебирать элементы
    // next() вернёт какой-нибудь (случайный) элемент множества
    // deletedGroup -- удаляемая группа (выбрана случайным образом)
    GroupData deletedGroup = before.iterator().next();

    app.getGroupHelper().deleteGroup(deletedGroup);

    assertThat(app.getGroupHelper().getGroupCount(), equalTo(before.size() - 1));
    Groups after = app.getGroupHelper().getGroupSet();
    System.out.println("Сравним коллекции");
    assertThat(after, equalTo(before.without(deletedGroup)));
  }
}

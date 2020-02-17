package ru.stqa.pft.addressbook.tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.xstream.XStream;
import org.testng.annotations.*;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

// Класс содержащий тесты по созданию групп
public class GroupCreationTests extends TestBase {

  // Провайдер тестовых данных
  @DataProvider
  public Iterator<Object[]> validGroupsFromXml() throws IOException {
    // Reader reader = new FileReader(new File("src/test/resources/groups.csv")); // обычный ридер
    // Обычный ридер не имеет метода, который читает всю строчку целиком (такого метода в классе Reader нет)
    //
    // Сделаем обёртку: вместо обычного ридера будем использовать BufferedReader
    // (то есть обычный ридер заворачиваем в буферизованный)
    BufferedReader reader = new BufferedReader(new FileReader(new File("src/test/resources/groups.xml")));

    String xml = "";
    String line = reader.readLine(); // метод readLine() читает строчку и сразу же её возвращает
    while (line != null) {
      xml += line;
      line = reader.readLine();
    }

    XStream xstream = new XStream();
    xstream.processAnnotations(GroupData.class);
    List<GroupData> groups = (List<GroupData>) xstream.fromXML(xml); // deserializing an object back from XML
    Iterator<Object[]> result = groups.stream()
                                      .map(g -> new Object[] {g})
                                      .collect(Collectors.toList()) // соберём обратно список из потока
                                      .iterator(); // у получившегося списка берём итератор (его и нужно возвращать)
    return result;
  }

  @DataProvider
  public Iterator<Object[]> validGroupsFromJson() throws IOException {
    // Конструкция try-with-resources
    try (BufferedReader reader = new BufferedReader(new FileReader(new File("src/test/resources/groups.json")))) {
      String json = "";
      String line = reader.readLine();
      while (line != null) {
        json += line;
        line = reader.readLine();
      }

      Gson gson = new Gson();
      List<GroupData> groups = gson.fromJson(json, new TypeToken<List<GroupData>>() {}.getType());
      Iterator<Object[]> result = groups.stream()
                                        .map(g -> new Object[] {g})
                                        .collect(Collectors.toList())
                                        .iterator();
      return result;
    }
  }

  @BeforeMethod
  public void ensurePreconditions() {
    System.out.print("\n\n***** Внутри метода ensurePreconditions() *****\n\n");
    app.getNavigationHelper().gotoGroupPage();
  }

  @Test(dataProvider = "validGroupsFromJson")
  public void testGroupCreation(GroupData group) throws Exception {
    System.out.print("\n\n***** Внутри метода testGroupCreation() *****\n\n");
    Groups before = app.getGroupHelper().getGroupSet();
    app.getGroupHelper().createGroup(group);
    assertThat(app.getGroupHelper().getGroupCount(), equalTo(before.size() + 1));
    Groups after = app.getGroupHelper().getGroupSet();

    System.out.println("Найдём максимальный идентификатор, и присвоим его группе");
    ToIntFunction<? super GroupData> mapper = g -> g.getId();
    int maxId = after.stream().mapToInt(mapper).max().getAsInt();
    group.withId(maxId);
    System.out.println("  группе был присвоен максимальный идентификатор = " + maxId);

    System.out.println("Сравним коллекции");
    assertThat(after, equalTo(before.withAdded(group)));
  }

  @Test
  public void testBadGroupCreation() throws Exception {
    System.out.print("\n\n***** Внутри метода testBadGroupCreation() *****\n\n");
    System.out.println("Негативный тест: проверим что нельзя создать группу с именем, содержащим символ (') апостроф");
    System.out.println();

    Groups before = app.getGroupHelper().getGroupSet();
    GroupData group = new GroupData().withName("test'test"); // имя группы содержит символ (') апостроф
    app.getGroupHelper().createGroup(group);

    System.out.println("Размер коллекции не должен поменяться");
    assertThat(app.getGroupHelper().getGroupCount(), equalTo(before.size()));

    Groups after = app.getGroupHelper().getGroupSet();
    System.out.println("Сравним коллекции");
    System.out.println("  новая коллекция должна быть равна старой");
    assertThat(after, equalTo(before));
  }
}

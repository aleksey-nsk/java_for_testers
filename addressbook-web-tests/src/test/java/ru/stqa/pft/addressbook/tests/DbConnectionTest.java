package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;
import java.sql.*;

// Нам нужен вспомогательный тест, который проверяет,
// что из БД можно извлечь какую-нибудь информацию
public class DbConnectionTest {

  @Test
  public void testDbConnection() {
    System.out.print("\n\n***** Внутри метода testDbConnection() *****\n\n");
    Connection conn = null;
    try {
      System.out.print("Пытаемся установить соединение с БД");
      String url = "jdbc:mysql://localhost:3306/addressbook?user=root&password="
                 + "&serverTimezone=UTC";
      conn = DriverManager.getConnection(url);
      System.out.println(" --> успешно!");

      System.out.println("Попытаемся извлечь из БД какую-нибудь информацию");
      Statement statement = conn.createStatement();
      // Тип ResultSet -- это набор строк которые извлекаются из таблицы (некий аналог Коллекции)
      ResultSet resultSet = statement.executeQuery("SELECT group_id, group_name, group_header, group_footer FROM group_list");
      Groups groups = new Groups();
      while (resultSet.next()) {
        groups.add(new GroupData().withId(resultSet.getInt("group_id"))
                                  .withName(resultSet.getString("group_name"))
                                  .withHeader(resultSet.getString("group_header"))
                                  .withFooter(resultSet.getString("group_footer"))
        );
      }

      System.out.println("Освободим ресурсы после использования");
      resultSet.close();
      statement.close();
      conn.close();

      System.out.println("Информация, извлечённая из БД");
      System.out.println("  groups: " + groups);
    } catch (SQLException ex) {
      System.out.println(" --> НЕ успешно!");
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
      System.out.println("VendorError: " + ex.getErrorCode());
    }
  }
}

// В итоге мы очень быстро (за считанные миллисекунды) получили информацию о том, какие
// группы сейчас хранятся в БД --> это гораздо быстрее чем через пользовательский интерфейс, потому что
// в этом случае нам не нужен браузер, нам не нужно выполнять медленные операции с пользовательским интерфейсом.
// Запросы к БД выполняются очень быстро.

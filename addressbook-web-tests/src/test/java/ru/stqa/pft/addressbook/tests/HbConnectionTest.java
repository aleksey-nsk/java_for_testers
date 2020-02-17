package ru.stqa.pft.addressbook.tests;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;
import java.util.List;

public class HbConnectionTest {

  private SessionFactory sessionFactory;

  @BeforeClass
  protected void setUp() throws Exception {
    System.out.print("\n\n***** Функция setUp() которая устанавливает соединение с БД *****\n\n");
    final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
        .configure() // configures settings from hibernate.cfg.xml
        .build();
    try {
      sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }
    catch (Exception e) {
      System.out.println("Выведем сообщения об ошибке на консоль:");
      e.printStackTrace();

      StandardServiceRegistryBuilder.destroy(registry);
    }
  }

  @Test
  public void testHbConnection() {
    System.out.print("\n\n***** Внутри метода testHbConnection() *****\n\n");
    Session session = sessionFactory.openSession();
    session.beginTransaction();
    List<GroupData> result = session.createQuery("from GroupData").list(); // исп-ем язык запросов Object Query Language

    System.out.println("Из БД извлечены группы");
    for (GroupData group : result) {
      System.out.println("  group: " + group);
    }

    session.getTransaction().commit();
    session.close();
  }
}

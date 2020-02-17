package ru.stqa.pft.addressbook.tests;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import java.util.List;

public class HbContactsTest {

  private SessionFactory sessionFactory;

  @BeforeClass
  protected void setUp() throws Exception {
    System.out.print("\n\n***** Функция setUp() которая устанавливает соединение с БД *****\n\n");
    final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
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
    List<ContactData> result = session.createQuery("from ContactData where deprecated = '0000-00-00 00:00:00'").list();

    System.out.println("Из БД извлечены контакты");
    for (ContactData contact : result) {
      System.out.println("  contact: " + contact);
    }

    session.getTransaction().commit();
    session.close();
  }
}

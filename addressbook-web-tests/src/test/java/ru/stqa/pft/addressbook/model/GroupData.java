package ru.stqa.pft.addressbook.model;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.hibernate.annotations.Type;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

// Вспомогательный класс
// который описывает объекты типа Группа
@XStreamAlias("group")
@Entity
@Table(name = "group_list")
public class GroupData {

  // Если атрибут имеет модификатор final => то менять его нельзя
  // (то значение, которое присвоили в конструкторе, остаётся навсегда)
  //
  // У всех атрибутов убираю модификатор final => теперь все атрибуты
  // можно менять уже после того как конструктор отработал

  @Expose
  @Column(name = "group_name")
  private String name;

  @Expose
  @Column(name = "group_header")
  @Type(type = "text")
  private String header;

  @Expose
  @Column(name = "group_footer")
  @Type(type = "text")
  private String footer;

  @XStreamOmitField
  @Id
  @Column(name = "group_id")
  private int id = 0; // указали дефолтное значение

  // Методы Сеттеры

  public GroupData withId(int id) {
    this.id = id;

    // Метод будет возвращать тот самый объект,
    // в котором он вызван => сможем использовать цепочки методов
    return this;
  }

  public GroupData withName(String name) {
    this.name = name;
    return this;
  }

  public GroupData withHeader(String header) {
    this.header = header;
    return this;
  }

  public GroupData withFooter(String footer) {
    this.footer = footer;
    return this;
  }

  // Методы Геттеры
  public int getId() { return id; }
  public String getName() { return name; }
  public String getHeader() { return header; }
  public String getFooter() { return footer; }

  @Override
  public String toString() {
    return "GroupData{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", header='" + header + '\'' +
        ", footer='" + footer + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    GroupData groupData = (GroupData) o;

    if (id != groupData.id) return false;
    return name != null ? name.equals(groupData.name) : groupData.name == null;
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + id;
    return result;
  }
}

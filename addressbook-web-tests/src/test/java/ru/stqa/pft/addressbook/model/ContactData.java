package ru.stqa.pft.addressbook.model;

import com.google.gson.annotations.Expose;
import org.hibernate.annotations.Type;
import javax.persistence.*;

// Вспомогательный класс
// который описывает объекты типа Контакт
@Entity
@Table(name = "addressbook")
public class ContactData {

  @Id
  @Column(name = "id")
  private int id = 0;

  @Expose
  @Column(name = "firstname")
  private String firstname;

  @Expose
  @Column(name = "lastname")
  private String lastname;

  @Column(name = "home")
  @Type(type = "text")
  private String homePhone;

  @Expose
  @Column(name = "mobile")
  @Type(type = "text")
  private String mobilePhone;

  @Column(name = "work")
  @Type(type = "text")
  private String workPhone;

  @Transient // пометили поле чтобы оно не извлекалось из БД
  private String allPhones;

  @Expose
  @Transient
  private String group;

  @Expose
  @Column(name = "email")
  @Type(type = "text")
  private String email;

  @Expose
  @Column(name = "photo")
  @Type(type = "text")
  private String photo;

  // Методы Сеттеры

  public ContactData withId(int id) {
    this.id = id;
    return this;
  }

  public ContactData withFirstname(String firstname) {
    this.firstname = firstname;
    return this;
  }

  public ContactData withLastname(String lastname) {
    this.lastname = lastname;
    return this;
  }

  public ContactData withHomePhone(String homePhone) {
    this.homePhone = homePhone;
    return this;
  }

  public ContactData withMobilePhone(String mobilePhone) {
    this.mobilePhone = mobilePhone;
    return this;
  }

  public ContactData withWorkPhone(String workPhone) {
    this.workPhone = workPhone;
    return this;
  }

  public ContactData withAllPhones(String allPhones) {
    this.allPhones = allPhones;
    return this;
  }

  public ContactData withEmail(String email) {
    this.email = email;
    return this;
  }

  public ContactData withGroup(String group) {
    this.group = group;
    return this;
  }

  public ContactData withPhoto(String photo) {
    this.photo = photo;
    return this;
  }

  // Методы Геттеры
  public int getId() { return id; }
  public String getFirstname() { return firstname; }
  public String getLastname() { return lastname; }
  public String getHomePhone() { return homePhone; }
  public String getMobilePhone() { return mobilePhone; }
  public String getWorkPhone() { return workPhone; }
  public String getAllPhones() { return allPhones; }
  public String getEmail() { return email; }
  public String getGroup() { return group; }
  public String getPhoto() { return photo; }

  @Override
  public String toString() {
    return "ContactData{" +
        "id=" + id +
        ", firstname='" + firstname + '\'' +
        ", lastname='" + lastname + '\'' +
        ", homePhone='" + homePhone + '\'' +
        ", mobilePhone='" + mobilePhone + '\'' +
        ", workPhone='" + workPhone + '\'' +
        ", email='" + email + '\'' +
        ", group='" + group + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ContactData that = (ContactData) o;

    if (id != that.id) return false;
    if (firstname != null ? !firstname.equals(that.firstname) : that.firstname != null) return false;
    return lastname != null ? lastname.equals(that.lastname) : that.lastname == null;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
    result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
    return result;
  }
}

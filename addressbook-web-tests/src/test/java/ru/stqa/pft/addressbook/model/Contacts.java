package ru.stqa.pft.addressbook.model;

import com.google.common.collect.ForwardingSet;
import java.util.HashSet;
import java.util.Set;

public class Contacts extends ForwardingSet<ContactData> {

  private Set<ContactData> delegate;

  public Contacts(Contacts contacts) {
    this.delegate = new HashSet<ContactData>(contacts.delegate);
  }

  public Contacts() {
    this.delegate = new HashSet<ContactData>();
  }

  @Override
  protected Set<ContactData> delegate() {
    return delegate;
  }

  public Contacts withAdded(ContactData contact) {
    System.out.println("  метод withAdded() создаёт копию, и вставляет в неё контакт");
    Contacts contacts = new Contacts(this);
    contacts.add(contact);
    return contacts;
  }

  public Contacts without(ContactData contact) {
    System.out.println("  метод without() создаёт копию, и удаляет из неё контакт");
    Contacts contacts = new Contacts(this);
    contacts.remove(contact);
    return contacts;
  }
}

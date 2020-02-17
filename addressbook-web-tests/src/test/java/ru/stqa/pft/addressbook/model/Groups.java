package ru.stqa.pft.addressbook.model;

import com.google.common.collect.ForwardingSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Groups extends ForwardingSet<GroupData> {

  // Все вызовы методов делегируются к какому-то объекту (реальному списку или множеству),
  // который вложен внутрь обёртки
  //
  // Также в этой обёртке можно реализовать свои собственные методы
  //
  // Сначала надо создать объект, к которому всё это будет делегироваться
  private Set<GroupData> delegate;

  // Конструктор который строит копию
  public Groups(Groups groups) {
    this.delegate = new HashSet<GroupData>(groups.delegate);
  }

  // Конструктор без параметров
  public Groups() {
    this.delegate = new HashSet<GroupData>();
  }

  // Конструктор который по произвольной коллекции строит объект типа Groups
  // (копируем чтобы никто не испортил)
  public Groups(Collection<GroupData> groups) {
    this.delegate = new HashSet<GroupData>(groups);
  }

  @Override
  protected Set<GroupData> delegate() {
    return delegate;
  }

  public Groups withAdded(GroupData group) {
    System.out.println("  метод withAdded() создаёт копию, и вставляет в неё группу");
    Groups groups = new Groups(this); // создали копию
    groups.add(group); // в эту копию добавили объект, который передан в качестве параметра
    return groups;
  }

  public Groups without(GroupData group) {
    System.out.println("  метод without() создаёт копию, и удаляет из неё группу");
    Groups groups = new Groups(this);
    groups.remove(group);
    return groups;
  }
}

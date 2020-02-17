package ru.stqa.pft.sandbox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Класс для экспериментов со Списками
public class Lists {

  public static void main(String[] args) {
    // Объявим переменную myLangs
    // типа массив строк
    // (размер массива 4)
    String[] myLangs = new String[4];

    // Заполним массив значениями
    myLangs[0] = "Java";
    myLangs[1] = "C#";
    myLangs[2] = "Python";
    myLangs[3] = "PHP";

    // Второй способ
    // (данная запись эквивалентна предыдущей)
    String[] langs = {"Java", "C#", "Python", "PHP"};

    // Устроим итерацию по элементам массива
    // (length -- это атрибут)
    for (int i = 0; i < langs.length; i++) {
      System.out.println("Я хочу выучить " + langs[i]);
    }
    System.out.println();

    // Особая конструкция цикла, которая
    // предназначена для перебора элементов Коллекции.
    // Переменная l последовательно указывает на все элементы Коллекции
    // (l -- это непосредственно ссылка на элемент массива)
    for (String l : langs) {
      System.out.println("Изучаю " + l);
    }
    System.out.println();

    // Слева укажем интерфейс List, а справа укажем
    // конкретный класс ArrayList который реализует этот интерфейс
    // (в угловых скобках <String> указываем тип элементов списка)
    List<String> languages = new ArrayList<String>();

    // Заполним список значениями
    // (размер списка меняется динамически)
    languages.add("Java");
    languages.add("C#");
    languages.add("Python");
    languages.add("PHP");

    // Итерация по элементам списка
    for (String l : languages) {
      System.out.println("Итерация по элементам списка: " + l);
    }
    System.out.println();

    // Есть класс Arrays который содержит метод asList()
    // asList() -- метод для преобразования массива в список
    List<String> newLanguages = Arrays.asList("JavaScript", "Ruby");

    // Устроим итерацию по элементам списка
    // с помощью вспомогательной переменной счётчика
    // (size() -- это метод)
    for (int i = 0; i < newLanguages.size(); i++) {
      System.out.println("Итерация: " + newLanguages.get(i));
    }
    System.out.println();

    // Список объектов произвольного типа (нет угловых скобок)
    List someList = Arrays.asList("Элемент1", "Элемент2");

    // Необходимо указать тип Object
    for (Object l : someList) {
      System.out.println("Опять итерация: " + l);
    }
  }
}

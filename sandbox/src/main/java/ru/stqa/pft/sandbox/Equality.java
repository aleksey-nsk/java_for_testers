package ru.stqa.pft.sandbox;

// Сравнение метода equals()
// и оператора ==
public class Equality {

  public static void main(String[] args) {
    String s1 = "firefox 2.0";
    String s2 = "firefox " + Math.sqrt(4.0);

    System.out.println(s1 == s2); // false
    System.out.println(s1.equals(s2)); // true
  }
}

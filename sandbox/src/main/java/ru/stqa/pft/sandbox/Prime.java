// Задача: определить является ли
// заданное число простым
// ("prime" -- простое число)

package ru.stqa.pft.sandbox;

public class Prime {

  public static boolean isPrime(int n) {
    System.out.println("\n[for][int] Определим является ли число " + n + " простым");
    for (int i = 2; i < n; i++) {
      if (n % i == 0) {
        System.out.println("  нашли делитель => не является простым");
        return false;
      }
    }
    System.out.println("  является простым числом");
    return true;
  }

  public static boolean isPrimeFast(int n) {
    System.out.println("\n[for][int][fast] Определим является ли число " + n + " простым");
    int m = (int) Math.sqrt(n);
    for (int i = 2; i < m; i++) {
      if (n % i == 0) {
        System.out.println("  нашли делитель => не является простым");
        return false;
      }
    }
    System.out.println("  является простым числом");
    return true;
  }

  public static boolean isPrimeWhile(int n) {
    System.out.println("\n[while] Определим является ли число " + n + " простым");
    int i = 2;
    while ((i < n) && (n % i != 0)) {
      i++;
    }

    if (i == n) {
      System.out.println("  является простым числом");
      return true;
    } else {
      System.out.println("  нашли делитель => не является простым");
      return false;
    }
  }

  public static boolean isPrime(long n) {
    System.out.println("\n[for][long] Определим является ли число " + n + " простым");
    for (long i = 2; i < n; i++) {
      if (n % i == 0) {
        System.out.println("  нашли делитель => не является простым");
        return false;
      }
    }
    System.out.println("  является простым числом");
    return true;
  }
}

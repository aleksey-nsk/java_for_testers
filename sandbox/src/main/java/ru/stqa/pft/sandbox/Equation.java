package ru.stqa.pft.sandbox;

public class Equation {

  private double a;
  private double b;
  private double c;
  private int n; // количество корней квадратного уравнения

  public Equation(double a, double b, double c) {
    System.out.print("  внутри конструктора Equation(" + a + ", " + b + ", " + c + ")\n\n");

    this.a = a;
    this.b = b;
    this.c = c;
    double d = b * b - 4 * a * c;

    if (a == 0) // линейное уравнение
    {
      if ((b == 0) & (c == 0)) {
        n = -1; // бесконечно много корней
      } else if ((b == 0) & (c != 0)) {
        n = 0;
      } else {
        n = 1;
      }
    }
    else // квадратное уравнение
    {
      if (d > 0) {
        n = 2;
      } else if (d == 0) {
        n = 1;
      } else {
        n = 0;
      }
    }
  }

  public int getN() {
    return n;
  }
}

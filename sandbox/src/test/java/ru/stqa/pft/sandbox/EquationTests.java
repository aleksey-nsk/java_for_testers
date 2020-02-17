package ru.stqa.pft.sandbox;

import org.testng.Assert;
import org.testng.annotations.Test;

public class EquationTests {

  @Test
  public void test0() {
    System.out.println("Внутри метода test0()");
    Equation e = new Equation(1, 1, 1); // действительных корней нет
    Assert.assertEquals(e.getN(), 0);
  }

  @Test
  public void test1() {
    System.out.println("Внутри метода test1()");
    Equation e = new Equation(1, 2, 1); // 1 действительный корень
    Assert.assertEquals(e.getN(), 1);
  }

  @Test
  public void test2() {
    System.out.println("Внутри метода test2()");
    Equation e = new Equation(1, 5, 6); // 2 действительных корня
    Assert.assertEquals(e.getN(), 2);
  }

  @Test
  public void testLinear() {
    System.out.println("Внутри метода testLinear()");
    Equation e = new Equation(0, 1, 1); // линейное уравнение
    Assert.assertEquals(e.getN(), 1);
  }

  @Test
  public void testConst() {
    System.out.println("Внутри метода testConst()");
    Equation e = new Equation(0, 0, 7); // уравнение вырождается в константу
    Assert.assertEquals(e.getN(), 0);
  }

  @Test
  public void testZero() {
    System.out.println("Внутри метода testZero()");
    Equation e = new Equation(0, 0, 0); // все коэффициенты нули
    Assert.assertEquals(e.getN(), -1);
  }
}

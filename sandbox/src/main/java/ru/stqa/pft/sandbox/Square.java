package ru.stqa.pft.sandbox;

// Класс описывающий квадрат
public class Square {

  public double l;

  public Square(double l) {
    this.l = l;
  }

  public double area() {
    return this.l * this.l;
  }
}

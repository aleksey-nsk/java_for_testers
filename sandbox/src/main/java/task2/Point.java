package task2;

import static java.lang.Math.pow;
import static java.lang.StrictMath.sqrt;

class Point {

  private double x;
  private double y;

  Point(double x, double y) {
    System.out.print("Конструктор точки на плоскости: ");
    this.x = x;
    this.y = y;
    System.out.println("x = " + x + "; y = " + y);
  }

  // Геттеры и сеттеры
  double getX() { return x; }
  double getY() { return y; }
  void setX(double x) { this.x = x; }
  void setY(double y) { this.y = y; }

  // Расстояние между точками
  double distance(Point p) {
    double px = p.getX();
    double py = p.getY();
    double lengthSquared = pow( (this.x - px), 2 ) + pow( (this.y - py), 2 );
    double length = sqrt(lengthSquared);
    return length;
  }
}

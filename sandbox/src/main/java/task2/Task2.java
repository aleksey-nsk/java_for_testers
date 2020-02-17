package task2;

// Запускаемый класс, то есть класс содержащий
// функцию public static void main(String[] args) {...}
class Task2 {

  public static void main(String[] args) {
    Point p1 = new Point(2.0, 4.0);
    Point p2 = new Point(6.0, 1.0);
    System.out.println("Расстояние между точками = " + p1.distance(p2));
    System.out.println();

    Point p3 = new Point(0.0, 0.0);
    Point p4 = new Point(10.0, 10.0);
    System.out.println("Расстояние между точками = " + p3.distance(p4));
  }
}

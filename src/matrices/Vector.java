/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matrices;

import java.util.LinkedList;
import java.util.Scanner;

/*
 * باسل بكر محمد سيد علي
 * فصل 1
 * رقم 26
 * 
 * @author Bassel Bakr Muhammad Sayyed Ali
 * @class 1
 * @number 26
 */
public final class Vector {

  private final int size;
  private final double[] elements;

  public Vector(int size) {
    this.size = size;
    this.elements = new double[size];
  }

  public static Vector of(String elements) {
    return new Vector(elements);
  }

  public static <T> Vector of(T... elements) {
    if (elements == null)
      throw new RuntimeException("Null vector!");

    String data = "";

    for (int i = 0, j = elements.length; i < j;)
      data += elements[i] + (++i == j ? "" : " ");

    return new Vector(data);
  }

  public Vector(String elements) {
    Scanner s = new Scanner(elements);

    LinkedList<Double> ll = new LinkedList<Double>();
    while (s.hasNextDouble())
      ll.add(s.nextDouble());

    size = ll.size();

    this.elements = new double[size];

    int i = 0;
    for (double elem : ll)
      this.elements[i++] = elem;
  }

  public double[] get() {
    double[] tmp = new double[size];
    for (int i = 0; i < size; ++i)
      tmp[i] = elements[i];
    return tmp;
  }

  public double get(int place) {
    return elements[place - 1];
  }

  public synchronized Vector set(int place, double value) {
    elements[place - 1] = value;
    return this;
  }

  public int getSize() {
    return size;
  }

  public double sum() {
    double result = 0.0;
    for (int i = 0; i < size; ++i)
      result += elements[i];

    return result;
  }

  public Vector mul(double val) {
    for (int i = 0; i < size; ++i)
      elements[i] *= val;
    return this;
  }

  public Vector div(double val) {
    for (int i = 0; i < size; ++i)
      elements[i] /= val;
    return this;
  }

  public Vector add(double val) {
    for (int i = 0; i < size; ++i)
      elements[i] += val;
    return this;
  }

  public Vector minus(double val) {
    for (int i = 0; i < size; ++i)
      elements[i] -= val;
    return this;
  }

  public Vector mod(double val) {
    for (int i = 0; i < size; ++i)
      elements[i] %= val;
    return this;
  }

  public double dot(Vector other) {
    if (size != other.size)
      throw new RuntimeException("Can't dot product vectors of different size!");

    double result = 0.0;

    for (int i = 0; i < size; ++i)
      result += elements[i] * other.elements[i];

    return result;
  }

  // (3 * 3) for now
  public Vector cross(Vector other) {
    if (size != 3 || size != other.size)
      return null;
    Vector result = new Vector(size);
    result.elements[0] = elements[1] * other.elements[2] - elements[2] * other.elements[1];
    result.elements[1] = elements[0] * other.elements[2] - elements[2] * other.elements[0];
    result.elements[2] = elements[0] * other.elements[1] - elements[1] * other.elements[0];
    return result;
  }

  @Override
  public String toString() {
    String str = "";
    for (int i = 1; i <= size; ++i)
      str += elements[i - 1] + (i == size ? "" : " ");

    return str;
  }

}

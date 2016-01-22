package matrices;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.ForkJoinPool;

/*
 * باسل بكر محمد سيد علي
 * فصل 1
 * رقم 26
 * 
 * @author Bassel Bakr Muhammad Sayyed Ali
 * @class 1
 * @number 26
 */
public final class Matrix implements Cloneable {

  private final int rows, columns;
  private final double[][] data;

  public Matrix(int rows, int columns) {
    this.rows = rows;
    this.columns = columns;
    data = new double[rows][columns];
  }

  public Matrix(String data) {
    String[] rs = data.split("\n");
    rows = rs.length;

    int c = 0;
    Scanner s = new Scanner(rs[0]);
    while (s.hasNextDouble()) {
      s.nextDouble();
      ++c;
    }

    columns = c;

    this.data = new double[rows][columns];

    int i = 0;
    Scanner sc = new Scanner(data);
    while (sc.hasNextLine())
      setRow(++i, sc.nextLine());

  }

  public String getRow(int row) {
    String r = "";
    int n = columns;
    for (int i = 1; i <= columns; ++i)
      r += get(row, i) + (i == columns ? "" : " ");
    return r;
  }

  public String getColumn(int column) {
    String r = "";
    int n = rows;
    for (int i = 1; i <= rows; ++i)
      r += get(i, column) + (i == rows ? "" : " ");
    return r;
  }

  public Vector getRowVector(int row) {
    String r = "";
    int n = columns;
    for (int i = 1; i <= columns; ++i)
      r += get(row, i) + (i == columns ? "" : " ");
    return new Vector(r);
  }

  public Vector getColumnVector(int column) {
    String r = "";
    int n = rows;
    for (int i = 1; i <= rows; ++i)
      r += get(i, column) + (i == rows ? "" : " ");
    return new Vector(r);
  }

  public synchronized Matrix setRow(int row, Vector elements) {
    data[row - 1] = elements.get();
    return this;
  }

  public synchronized Matrix setColumn(int column, Vector elements) {
    setColumn(column, elements.toString());
    return this;
  }

  public synchronized Matrix setRow(int row, String elements) {
    Scanner s = new Scanner(elements);
    int i = 0;
    while (s.hasNextDouble())
      set(row, ++i, s.nextDouble());
    return this;
  }

  public synchronized Matrix setColumn(int column, String elements) {
    Scanner s = new Scanner(elements);
    int i = 0;
    while (s.hasNextDouble())
      set(++i, column, s.nextDouble());
    return this;
  }

  private double[][] getData() {
    return data;
  }

  public int getRows() {
    return rows;
  }

  public int getColumns() {
    return columns;
  }

  // get value from matrix
  public double get(int row, int column) {
    return data[row - 1][column - 1];
  }

  // set value inside of matrix
  public synchronized Matrix set(int row, int column, double value) {
    data[row - 1][column - 1] = value;
    return this;
  }

  // remove if row or column != 0
  public Matrix remove(int row, int column) {
    final Matrix x = new Matrix(rows - 1, columns - 1);
    double[][] data = x.getData();

    for (int i = 0, j = 0; i < rows; ++i) {
      if (i == row - 1)
        continue;

      System.arraycopy(this.data[i], 0, data[j], 0, column - 1);
      System.arraycopy(this.data[i], column, data[j], column - 1, columns - column);
      ++j;
    }

    return x;
  }

  public Matrix add(Matrix other) {
    if (columns != other.getColumns() && rows != other.getRows())
      throw new RuntimeException(
              "Matrices can't be added: dimensions don't match");

    Matrix result = new Matrix(rows, other.columns);

    for (int i = 1; i <= rows; ++i)
      for (int j = 1; j <= columns; ++j)
        result.set(i, j, get(i, j) + other.get(i, j));

    return result;
  }

  public Matrix subtract(Matrix other) {
    if (columns != other.getColumns() && rows != other.getRows())
      throw new RuntimeException(
              "Matrices can't be subtracted: dimensions don't match");

    Matrix result = new Matrix(rows, other.columns);

    for (int i = 1; i <= rows; ++i)
      for (int j = 1; j <= columns; ++j)
        result.set(i, j, get(i, j) - other.get(i, j));

    return result;
  }

  public Matrix mod(double value) {
    Matrix result = new Matrix(rows, columns);

    for (int r = 1; r <= rows; ++r)
      result.setRow(r, getRowVector(r).mod(value));

    return result;
  }

  public Matrix divide(double value) {
    Matrix result = new Matrix(rows, columns);

    for (int r = 1; r <= rows; ++r)
      result.setRow(r, getRowVector(r).div(value));

    return result;
  }

  public Matrix multiply(double value) {
    Matrix result = new Matrix(rows, columns);

    for (int r = 1; r <= rows; ++r)
      result.setRow(r, getRowVector(r).mul(value));

    return result;
  }

  public Matrix multiply(Matrix other) {
    if (columns != other.rows)
      throw new RuntimeException("Matrices can't be multiplied");

    Matrix result = new Matrix(rows, other.columns);

    for (int i = 1; i <= rows; ++i)
      for (int j = 1; j <= other.columns; ++j)
        result.set(i, j, new Vector(getRow(i))
                   .dot(new Vector(other.getColumn(j))));

    return result;
  }

  public Matrix transpose() {
    Matrix result = new Matrix(columns, rows);
    for (int i = 1; i <= rows; ++i)
      result.setColumn(i, getRow(i));
    return result;
  }

  public Matrix minorsMatrix() {
    Matrix result = new Matrix(rows, columns);
    for (int r = 1; r <= rows; ++r)
      for (int c = 1; c <= columns; ++c)
        result.set(r, c, remove(r, c).determinantKarly());
    return result;
  }

  public Matrix cofactorsMatrix() {
    Matrix result = minorsMatrix();
    for (int r = 1; r <= rows; ++r)
      for (int c = 1; c <= columns; ++c)
        result.set(r, c, ((r + c) % 2 == 0 ? 1 : -1) * result.get(r, c));

    return result;
  }

  public Matrix adjointMatrix() {
    return cofactorsMatrix().transpose();
  }

  public double determinantCofactors() {
    if (!hasDeterminant())
      throw new IllegalArgumentException(
              "Can't find determinant of non-square matrix!");

    ForkJoinPool fjp = new ForkJoinPool();
    DeterminantCofactors dc = new DeterminantCofactors(this);
    return fjp.invoke(dc);
  }

  public double determinantKarly() {
    if (!hasDeterminant())
      throw new IllegalArgumentException(
              "Can't find determinant of non-square matrix!");

    ForkJoinPool fjp = new ForkJoinPool();
    DeterminantKarly dk = new DeterminantKarly(this);
    return fjp.invoke(dk);
  }

  public Matrix inverseAdjoint() {
    if (!hasDeterminant())
      throw new IllegalArgumentException("Can't inverse non-square matrix!");

    if (!hasInverse())
      throw new IllegalArgumentException(
              "No inverse for a matrix with ZERO determinant!");

    return identity(rows)
            .multiply(1 / determinantKarly())
            .multiply(adjointMatrix());
  }

  public Matrix inverseCrossProduct() {
    if (!hasDeterminant())
      throw new IllegalArgumentException("Can't inverse non-square matrix!");

    if (!hasInverse())
      throw new IllegalArgumentException(
              "No inverse for a matrix with ZERO determinant!");

    final Matrix result = new Matrix(rows, columns);
    double det = determinantCofactors();
    Vector c1 = getColumnVector(1);
    Vector c2 = getColumnVector(2);
    Vector c3 = getColumnVector(3);

    Vector r1 = c2.cross(c3);
    Vector r2 = c3.cross(c1);
    Vector r3 = c1.cross(c2);

    result.setRow(1, r1.div(det));
    result.setRow(2, r2.div(det));
    result.setRow(3, r3.div(det));

    return result;
  }

  public Matrix inverseShipley() {
    if (!hasDeterminant())
      throw new IllegalArgumentException("Can't inverse non-square matrix!");

    if (!hasInverse())
      throw new IllegalArgumentException(
              "No inverse for a matrix with ZERO determinant!");

    class Pair {

      int first, second;

      public Pair(int a, int b) {
        first = a;
        second = b;
      }
    }

    Stack<Pair> changes = new Stack<Pair>();

    final Matrix result = clone();

    for (int pivot = 1; pivot <= rows; ++pivot) {
      int replaced = 0;

      if (result.get(pivot, pivot) == 0) {
        for (int c = 1; c <= columns; ++c)
          if (result.get(c, pivot) != 0) {
            replaced = c;
            Vector pivot_vector = result.getColumnVector(pivot);
            Vector other_vector = result.getColumnVector(c);
            // replace
            result.setColumn(c, pivot_vector);
            result.setColumn(pivot, other_vector);
            changes.add(new Pair(pivot, replaced));
            break;
          }
      }

      final Matrix previous_result = result.clone();

      double saved_pivot = previous_result.get(pivot, pivot);

      double transposed_pivot = 1.0 / saved_pivot;

      Vector pivot_row = result
              .getRowVector(pivot)
              .set(pivot, 1) // will be reassigned later
              .div(-transposed_pivot);

      Vector pivot_column = result
              .getColumnVector(pivot)
              .set(pivot, 1) // will be reassigned later
              .div(transposed_pivot);

      result.setRow(pivot, pivot_row);
      result.setColumn(pivot, pivot_column);
      result.set(pivot, pivot, transposed_pivot);

      for (int r = 1; r <= rows; ++r) {
        if (r == pivot)
          continue;

        for (int c = 1; c <= columns; ++c) {
          if (c == pivot)
            continue;

          result.set(r, c,
                     previous_result.get(r, c) - ((previous_result.get(r, pivot)
                     * previous_result.get(pivot, c)) / saved_pivot)
          );
        }
      }
    }

    while (!changes.isEmpty()) {
      Pair change = changes.pop();
      Vector pivot_vector = result.getColumnVector(change.first);
      Vector other_vector = result.getColumnVector(change.second);
      // replace back
      result.setColumn(change.second, pivot_vector);
      result.setColumn(change.first, other_vector);
    }

    return result;
  }

  public boolean hasDeterminant() {
    return rows == columns;
  }

  public boolean hasInverse() {
    return determinantKarly() != 0;
  }

  public static Matrix identity(int size) {
    final Matrix result = new Matrix(size, size);
    for (int pivot = 1; pivot <= size; ++pivot)
      result.set(pivot, pivot, 1);
    return result;
  }

  @Override
  protected Matrix clone() {
    final Matrix copy = new Matrix(rows, columns);
    for (int i = 1; i <= rows; ++i)
      copy.setRow(i, getRowVector(i));
    return copy;
  }

  @Override
  public String toString() {
    String mat = "";

    for (int r = 1; r <= rows; ++r)
      for (int c = 1; c <= columns; ++c) {
        BigDecimal decimal = new BigDecimal(get(r, c));
        mat += decimal.doubleValue() + (c == columns ? "\n" : "\t\t");

      }
    return mat;
  }

}

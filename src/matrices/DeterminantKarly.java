
package matrices;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/*
 * باسل بكر محمد سيد علي
 * فصل 1
 * رقم 26
 * 
 * @author Bassel Bakr Muhammad Sayyed Ali
 * @class 1
 * @number 26
 */
public class DeterminantKarly extends RecursiveTask<Double> {

  private final Matrix matrix;
  private final int rows, columns;

  public DeterminantKarly(Matrix matrix) {
    this.matrix = matrix;
    rows = matrix.getRows();
    columns = matrix.getColumns();
  }

  public DeterminantKarly(Matrix matrix, int row, int column) {
    this.matrix = matrix;
    this.columns = column;
    this.rows = row;
  }

  @Override
  protected Double compute() {
    if (rows == 1)
      return matrix.get(1, 1);
    else if (rows == 2)
      return matrix.get(1, 1) * matrix.get(2, 2)
              - matrix.get(1, 2) * matrix.get(2, 1);

    DeterminantKarly north_west = new DeterminantKarly(matrix.remove(rows, columns)),
            north_east = new DeterminantKarly(matrix.remove(rows, 1)),
            south_west = new DeterminantKarly(matrix.remove(1, columns)),
            south_east = new DeterminantKarly(matrix.remove(1, 1)),
            middle = new DeterminantKarly(matrix
                    .remove(rows, columns)
                    .remove(1, 1));

    ForkJoinTask<Double> nw = north_west.fork(),
            ne = north_east.fork(),
            sw = south_west.fork(),
            se = south_east.fork(),
            mid = middle.fork();

    try {
      return (nw.get() * se.get() - ne.get() * sw.get()) / mid.get();
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }

}

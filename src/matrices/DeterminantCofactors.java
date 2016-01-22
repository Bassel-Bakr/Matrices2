/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matrices;

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
public class DeterminantCofactors extends RecursiveTask<Double> {

  private final Matrix matrix;
  private final int row, column;

  public DeterminantCofactors(Matrix matrix) {
    this.matrix = matrix;
    row = column = 1;
  }

  public DeterminantCofactors(Matrix matrix, int row, int column) {
    this.matrix = matrix;
    this.column = column;
    this.row = row;
  }

  @Override
  protected Double compute() {
    if (matrix.getRows() == 1 && matrix.getColumns() == 1)
      return matrix.get(1, 1);

    Double result = 0.0;

    for (int i = 1, j = matrix.getColumns(); i <= j; ++i) {
      final Matrix mini = matrix.remove(row, i);
      result += matrix.get(row, i) * Math.pow(-1, row + i) * getPool().invoke(new DeterminantCofactors(mini));
    }

    return result;
  }

}

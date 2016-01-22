/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matrices;

import java.math.BigInteger;

/*
 * باسل بكر محمد سيد علي
 * فصل 1
 * رقم 26
 * 
 * @author Bassel Bakr Muhammad Sayyed Ali
 * @class 1
 * @number 26
 */
public class Matrices {

  /**
   * @param args the command line arguments
   */
  private static BigInteger[][] pows = new BigInteger[10][41];
  private static BigInteger TEN = new BigInteger("10");

  public static void main(String[] args) {
    Matrix x = new Matrix(""
            + "0 3 3\n"
            + "1 4 3\n"
            + "1 3 4");
    System.out.println(x.inverseAdjoint());
    System.out.println(x.inverseShipley());

    for (int i = 0; i < pows.length; ++i)
      for (int j = 3; j < pows[i].length; ++j)
        pows[i][j] = BigInteger.valueOf((long) Math.pow(i, j));

    for (BigInteger i = BigInteger.valueOf(100), j = BigInteger.ZERO;
            i.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) < 0;
            i = i.add(BigInteger.ONE))
      if (isNarcissistic(i)) {
        j = j.add(BigInteger.ONE);
        System.out.printf("%s: %s\n", j, i);
      }

  }

  public static boolean isNarcissistic(BigInteger number) {
    BigInteger x = BigInteger.valueOf(number.longValue());
    int len = x.toString().length();

    BigInteger result = BigInteger.ZERO;

    while (x.compareTo(BigInteger.ZERO) > 0) {
      result = result.add(pows[x.mod(TEN).intValue()][len]);
      x = x.divide(TEN);
    }

    return result.compareTo(number) == 0;
  }

}

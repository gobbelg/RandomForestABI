/** */
package src.main.gov.va.vha09.grecc.raptat.gg.algorithms;

import java.util.Arrays;
import org.apache.hadoop.hbase.util.MunkresAssignment;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import src.main.gov.va.vha09.grecc.raptat.gg.analysis.scorer.algorithms.HungarianAlgorithm;

/** @author Glenn Gobbel */
public class MunkresAssignmentTest extends MunkresAssignment {

  private static final Logger LOGGER = Logger.getLogger(MunkresAssignmentTest.class);

  {
    LOGGER.setLevel(Level.INFO);
  }

  /**
   * @param costMatrix
   */
  public MunkresAssignmentTest(final int[][] costMatrix) {
    super(castToFloat(convertMinToMax(costMatrix)));
  }


  public static long assignmentCost(final int[] assignments, final int[][] costMatrix) {
    long cost = 0;
    for (int i = 0; i < assignments.length; i++) {
      cost += costMatrix[i][assignments[i]];
    }

    return cost;
  }


  /**
   * Nov 1, 2017
   *
   * @param args
   */
  public static void main(final String[] args) {
    int[][] testInts = new int[][] {
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -1073741824,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, 11, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, 11, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            11, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, 11, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, 11, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, 11,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, 11, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, 11, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, 11, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, 11, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, 10, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, 11, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, 11, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, 11,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, 11, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, 11,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, 11, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, 11, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, 12, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, 11, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, 11, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, 11, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, 11,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, 11, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, 12,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, 13, -2147483648,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, 11, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, 10, -2147483648,},
        {12, 11, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, 11, 12, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, 12, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, 12, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, 11, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, 13, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, 10,},
        {-2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, -2147483648, -2147483648, -2147483648,
            -2147483648, -2147483648, -2147483648, 11,}};

    MunkresAssignmentTest mrTest = new MunkresAssignmentTest(testInts);
    int[] result = mrTest.solve();
    long cost = MunkresAssignmentTest.assignmentCost(result, testInts);
    System.out.println("Munkres Cost:" + cost);
    System.out.println(Arrays.toString(result));

    // MunkresAssignment mrAssignment = new MunkresAssignment( testInts );
    //
    // int[] result = mrAssignment.solve();

    // System.out.println( Arrays.toString( result ) );

    HungarianAlgorithm ha = new HungarianAlgorithm(testInts, false);
    result = ha.execute();
    cost = MunkresAssignmentTest.assignmentCost(result, testInts);
    System.out.println("HA Cost:" + cost);
    System.out.println(Arrays.toString(result));
  }


  /**
   * Nov 1, 2017
   *
   * @param costMatrix
   * @return
   */
  private static float[][] castToFloat(final int[][] costMatrix) {
    float[][] resultMatrix = new float[costMatrix.length][];
    int r = 0;
    while (r < costMatrix.length) {
      LOGGER.debug("r:" + r);
      float[] resultRow = new float[costMatrix[r].length];
      resultMatrix[r] = resultRow;
      int c = 0;
      while (c < costMatrix[r].length) {
        LOGGER.debug("c:" + c);
        resultRow[c] = costMatrix[r][c];
        c++;
      }
      r++;
    }
    return resultMatrix;
  }


  private static int[][] convertMinToMax(final int[][] inputArray) {
    int maxArrayValue = maxArrayValue(inputArray);
    return diffArray(maxArrayValue, inputArray);
  }


  private static int[][] diffArray(final int diffValue, final int[][] inputArray) {
    int[][] resultArray = new int[inputArray.length][];

    for (int i = 0; i < inputArray.length; i++) {
      resultArray[i] = new int[inputArray[i].length];
      for (int j = 0; j < inputArray[i].length; j++) {
        long difference = (long) diffValue - (long) inputArray[i][j];
        resultArray[i][j] = (int) (difference > Integer.MAX_VALUE ? Integer.MAX_VALUE : difference);
      }
    }

    return resultArray;
  }


  private static int maxArrayValue(final int[][] inputArray) {
    int result = Integer.MIN_VALUE;

    for (int[] inputArray1 : inputArray) {
      for (int j = 0; j < inputArray1.length; j++) {
        result = Math.max(result, inputArray1[j]);
      }
    }

    return result;
  }
}

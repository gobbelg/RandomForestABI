/*
 * BEGIN (Dax)
 */
package src.main.gov.va.vha09.grecc.raptat.dw.textanalysis.common;

/**
 * An interface intended to define how to pull the start and stop span limits as ints, and permits
 * comparison between implementing classes based on this.
 *
 * @author westerd
 *
 */
public interface IComparableSpan extends Comparable<IComparableSpan> {
  /**
   * Gets the end offset.
   *
   * @return the end offset
   */
  int get_end_offset_as_int();


  /**
   * Gets the line index.
   *
   * @return the line index
   */
  int get_line_index();


  /**
   * Gets the start offset.
   *
   * @return the start offset
   */
  int get_start_offset_as_int();


  /**
   * Contains.
   *
   * @param current the current
   * @return true, if successful
   */
  boolean is_in_column_index(int current);

}

/*
 * END
 */

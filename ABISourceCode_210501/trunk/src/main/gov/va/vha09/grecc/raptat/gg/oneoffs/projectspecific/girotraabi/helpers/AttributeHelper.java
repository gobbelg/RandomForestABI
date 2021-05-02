/**
 *
 */
package src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/**
 * @author VHATVHGOBBEG
 *
 */
public class AttributeHelper {

  public enum AttributeIndex {
    DOC_ID, START_OFFSET, END_OFFSET, INDEX_VALUE_STRING, INDEX_VALUE_QUALITATIVE;
  }

  /**
   *
   */
  private AttributeHelper() {
    // TODO Auto-generated constructor stub
  }

  /*
   * Assumes that all the string attributes at first indices of an instance are identifiers. This
   * returns these identifiers as a concatenated string.
   */
  public static String getAttributeIdentifierHash(final Instance instance) {
    Enumeration<Attribute> attributeEnumerator = instance.enumerateAttributes();
    StringBuilder sb = new StringBuilder();
    if (attributeEnumerator.hasMoreElements()) {
      Attribute attribute = attributeEnumerator.nextElement();
      if (!attribute.isString()) {
        return sb.toString();
      }
      sb.append(instance.stringValue(attribute));
    }

    while (attributeEnumerator.hasMoreElements()) {
      Attribute attribute = attributeEnumerator.nextElement();
      if (!attribute.isString()) {
        return sb.toString();
      }
      sb.append("_").append(instance.stringValue(attribute));
    }
    return sb.toString();
  }

  public static String getAttributeValueHash(final Map<String, Object> attributeValues) {

    StringBuilder sb = new StringBuilder();
    attributeValues.forEach((k, v) -> sb.append(v).append("_"));
    return sb.substring(0, sb.length() - 1);
  }

  public static <T> List<T> getAttributeValues(final Attribute attribute) {

    List<T> values = new ArrayList<>();
    Enumeration<T> valueEnumerator = (Enumeration<T>) attribute.enumerateValues();
    while (valueEnumerator.hasMoreElements()) {

      T nextValue = valueEnumerator.nextElement();
      values.add(nextValue);
    }
    return values;
  }

  public static List<Attribute> getCommonAttributes() {

    List<Attribute> commonAttributes = new ArrayList<>(4);
    List<String> qualitativeIndexValues =
        new ArrayList<>(Arrays.asList("Zero", "ZeroTo0p5", "0p5To1", "1To1p5", "1p5To2", "Above2"));

    commonAttributes.add(new Attribute(AttributeIndex.DOC_ID.toString(), (ArrayList<String>) null));
    commonAttributes
        .add(new Attribute(AttributeIndex.START_OFFSET.toString(), (ArrayList<String>) null));
    commonAttributes
        .add(new Attribute(AttributeIndex.END_OFFSET.toString(), (ArrayList<String>) null));
    commonAttributes
        .add(new Attribute(AttributeIndex.INDEX_VALUE_STRING.toString(), (ArrayList<String>) null));
    commonAttributes.add(new Attribute(AttributeIndex.INDEX_VALUE_QUALITATIVE.toString(),
        new ArrayList<>(qualitativeIndexValues)));

    return commonAttributes;
  }

  public static void printAttribute(final Attribute attribute, final int attributeIndex) {
    System.out.println("\n\n");
    System.out.println(AbiHelper.PrintMarks.LINE_SEPARATOR);
    System.out.println(" Attribute " + attributeIndex + " :");
    System.out.println("\tType:" + Attribute.typeToString(attribute));
    System.out.println("\tIndex:" + attribute.index());
    System.out.println("\tName:" + attribute.name());

    Enumeration<Object> valueEnumerator = attribute.enumerateValues();
    int valueIndex = 0;
    System.out.println("\t" + AbiHelper.PrintMarks.LINE_SEPARATOR);
    while (valueEnumerator.hasMoreElements()) {
      System.out.println("\t\tValue " + valueIndex++ + ":" + valueEnumerator.nextElement());
    }
    System.out.println("\t" + AbiHelper.PrintMarks.LINE_SEPARATOR);
  }

  public static void printAttributeList(final ArrayList<Attribute> attributeList) {
    System.out.println("");
    System.out.println(AbiHelper.PrintMarks.DOUBLE_LINE_SEPARATOR);
    System.out.println("ATTRIBUTE LIST");
    System.out.println(AbiHelper.PrintMarks.DOUBLE_LINE_SEPARATOR);

    int attributeIndex = 0;
    for (Attribute attribute : attributeList) {
      printAttribute(attribute, attributeIndex);
      attributeIndex++;
    }

  }

  public static void zeroMissingValues(final Instances instances) {

    Enumeration<Instance> instanceEnumerator = instances.enumerateInstances();
    while (instanceEnumerator.hasMoreElements()) {
      Instance instance = instanceEnumerator.nextElement();
      Enumeration<Attribute> attributeEnumerator = instances.enumerateAttributes();
      while (attributeEnumerator.hasMoreElements()) {
        Attribute attribute = attributeEnumerator.nextElement();
        if (instance.isMissing(attribute)) {
          instance.setValue(attribute, "0");
        }
      }
    }

  }
}

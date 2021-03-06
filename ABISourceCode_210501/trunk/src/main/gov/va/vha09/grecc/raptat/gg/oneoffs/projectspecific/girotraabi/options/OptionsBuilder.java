package src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.options;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of static methods to add various Weka-based options to the various methods within and
 * referred to within the GirotraABI class
 *
 * @author VHATVHGOBBEG
 *
 */
public class OptionsBuilder {

  private OptionsBuilder() {}


  /**
   * @param optionsList
   * @param timeBasedSeed
   */
  public static void addBaseClassifierOptions(final List<String> optionsList,
      final String timeBasedSeed) {
    /*
     * Classifier to use
     */
    optionsList.add("-W");
    optionsList.add("weka.classifiers.trees.RandomForest");

    /*
     * Needed when using FilteredClassifier with another, base classifier to distinguish where the
     * options for each classifier type start and stop
     */
    optionsList.add("--");

    /*
     * Bag size
     */
    optionsList.add("-P");
    optionsList.add("100");

    /*
     * Number of iterations (trees)
     */
    optionsList.add("-I");
    optionsList.add("500");

    optionsList.add("-num-slots");
    optionsList.add("4");

    /*
     * Number of attributes to randomly investigate
     */
    optionsList.add("-K");
    optionsList.add("32");

    /*
     * Minimum number of instances per leaf
     */
    optionsList.add("-M");
    optionsList.add("1.0");
    optionsList.add("-V");
    optionsList.add("0.001");

    /*
     * Seed for random number generator
     */
    optionsList.add("-S");
    optionsList.add(timeBasedSeed);

    /*
     * Maximum tree depth
     */
    optionsList.add("-depth");
    optionsList.add("24");

    /*
     * Number of decimal places for calculations
     */
    optionsList.add("-num-decimal-places");
    optionsList.add("3");
  }


  /**
   * @param optionsList
   * @return
   */
  public static void addCrossValidationOptions(final List<String> optionsList) {

    /* Set seed for apportioning train and test sets for CV folds */
    String timeBasedSeed = Long.toString(System.currentTimeMillis());
    timeBasedSeed = timeBasedSeed.substring(timeBasedSeed.length() - 3, timeBasedSeed.length());
    optionsList.add("-s");
    optionsList.add(timeBasedSeed);

    /* Set options for folds */
    optionsList.add("-x");
    optionsList.add("10");

  }


  /**
   * Generate the options when using a filtered classifier - also calls method to create base
   * classifier options
   *
   * @param optionsList
   * @param attributesForRemoval
   */
  public static void addFilteredClassifierOptions(final List<String> optionsList,
      final String attributesForRemoval) {

    String timeBasedSeed = Long.toString(System.currentTimeMillis());
    timeBasedSeed = timeBasedSeed.substring(timeBasedSeed.length() - 4, timeBasedSeed.length());

    optionsList.add("-F");
    optionsList.add("weka.filters.unsupervised.attribute.Remove -R " + attributesForRemoval);
    optionsList.add("-S");
    optionsList.add("-1");

    addBaseClassifierOptions(optionsList, timeBasedSeed);
  }



  public static void addInputMappedOptions(final List<String> optionsList) {
    optionsList.add("-W");
    optionsList.add("weka.classifiers.meta.FilteredClassifier");
    optionsList.add("--");
  }

  public static void addPredictionOptions(final List<String> optionsList,
      final String identifierAttributes) {
    optionsList.add("-po");
    optionsList.add(identifierAttributes);

  }

  public static List<String> generateFilteredClassifierOptions(final String attributesForRemoval) {
    List<String> options = new ArrayList<>();
    addFilteredClassifierOptions(options, attributesForRemoval);
    return options;
  }


  public static List<String> generateInputMappedOptions() {
    List<String> options = new ArrayList<>();
    addInputMappedOptions(options);
    return options;
  }


  public static void main(final String[] args) {
    // TODO Auto-generated method stub

  }

}

package src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.metafeatures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import src.main.gov.va.vha09.grecc.raptat.dw.feature_builders.DocumentMetaData;
import src.main.gov.va.vha09.grecc.raptat.dw.feature_builders.builders.common.FeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.dw.feature_builders.runner.FeatureBuilderRunner;
import src.main.gov.va.vha09.grecc.raptat.dw.feature_builders.runner.RunDirection;
import src.main.gov.va.vha09.grecc.raptat.dw.textanalysis.RaptatTokenPhrase;
import src.main.gov.va.vha09.grecc.raptat.dw.textanalysis.RaptatTokenPhrase.Label;
import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.MunkresAssignmentTest;
import src.main.gov.va.vha09.grecc.raptat.gg.core.RaptatConstants.AnnotationApp;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.RaptatPair;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.TextDocumentLine;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.GeneralHelper;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.xml.AnnotationImporter;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.attributefilters.AttributeFilter;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.featurebuilders.general.BaseFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.featurebuilders.general.SimpleFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.helpers.AttributeHelper;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.raptatutilities.TokenPhraseMakerGG;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.DocumentColumn;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.RaptatDocument;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.TextAnalyzer;
import src.main.gov.va.vha09.grecc.raptat.gg.weka.entities.ArffCreationParameterExtended;
import src.test.gov.va.vha09.grecc.raptat.dw.use_cases.pipeline_arff_training_errorAnalysis_eHost.modules.ModuleResultBase;
import src.test.gov.va.vha09.grecc.raptat.dw.use_cases.pipeline_arff_training_errorAnalysis_eHost.modules.FeatureBuildersAndArffFileCreation.ArffCreationParameter;
import src.test.gov.va.vha09.grecc.raptat.dw.use_cases.pipeline_arff_training_errorAnalysis_eHost.modules.FeatureBuildersAndArffFileCreation.ArffCreationResults;
import src.test.gov.va.vha09.grecc.raptat.dw.use_cases.pipeline_arff_training_errorAnalysis_eHost.modules.FeatureBuildersAndArffFileCreation.ArffFileWriter;
import src.test.gov.va.vha09.grecc.raptat.dw.use_cases.pipeline_arff_training_errorAnalysis_eHost.modules.FeatureBuildersAndArffFileCreation.FeatureBuildersAndArffFileCreation;
import src.test.gov.va.vha09.grecc.raptat.dw.use_cases.pipeline_arff_training_errorAnalysis_eHost.modules.FeatureBuildersAndArffFileCreation.FnPostArffFileCompleted;
import src.test.gov.va.vha09.grecc.raptat.dw.use_cases.pipeline_arff_training_errorAnalysis_eHost.modules.FeatureBuildersAndArffFileCreation.PostArffFileCompleted;
import src.test.gov.va.vha09.grecc.raptat.dw.use_cases.pipeline_arff_training_errorAnalysis_eHost.modules.FeatureBuildersAndArffFileCreation.PostArffFileCompleted.PostArffFileResults;
import src.test.gov.va.vha09.grecc.raptat.gg.columnsandrows.export.AnnotationToTokenPhraseComparator;
import src.test.gov.va.vha09.grecc.raptat.gg.columnsandrows.export.PhraseClass;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Essentially reproduces the FeatureBuilderAndArffCreator class to allow for creating
 * "metafeatures," which are features built from other features. Many methods are recreated here
 * because they are static in the superclass and cannot be overrident.
 *
 * @author gtony
 */
public class MetaFeatureBuilderAndArffCreator extends FeatureBuildersAndArffFileCreation {

  private static final Logger LOGGER = Logger.getLogger(MetaFeatureBuilderAndArffCreator.class);
  private static final ArrayList<String> binaryFeatureValues =
      new ArrayList<>(Arrays.asList("0", "1"));

  private static double[] INDEX_VALUE_BOUNDS =
      new double[] {Double.NEGATIVE_INFINITY, 0.0, 0.5, 1.0, 1.5, 2.0, Double.POSITIVE_INFINITY};
  private static final List<String> QUALITATIVE_INDEX_VALUE =
      new ArrayList<>(Arrays.asList("Zero", "ZeroTo0p5", "0p5To1", "1To1p5", "1p5To2", "Above2"));



  {
    LOGGER.setLevel(Level.DEBUG);
  }



  private BaseFeatureBuilder simpleFeatureBuilder = new SimpleFeatureBuilder();
  private MetaFeatureBuilder[] metaFeatureBuilders;
  private List<AttributeFilter> attributeFilters = new ArrayList<>();

  /*
   * Create a log file to determine whether the documents processed via the processUpdated()method
   * contained any RaptatTokenPhrase instances;
   */
  private boolean logTokenPhrases = false;

  /**
   * @param arffCreationParameter
   */
  public MetaFeatureBuilderAndArffCreator(
      final ArffCreationParameterExtended arffCreationParameter) {
    super(arffCreationParameter);
  }

  public MetaFeatureBuilderAndArffCreator(final ArffCreationParameterExtended arffCreationParameter,
      final MetaFeatureBuilder[] metaFeatureBuilders,
      final List<AttributeFilter> attributeFilters) {
    this(arffCreationParameter);
    this.metaFeatureBuilders = metaFeatureBuilders;
    this.attributeFilters = attributeFilters;
  }

  /**
   * @param raptatTokenPhrases
   * @param annotatedPhrases
   */
  public int[][] getAdjacencyMatrix(final List<RaptatTokenPhrase> raptatTokenPhrases,
      final List<AnnotatedPhrase> annotatedPhrases) {
    final int[][] adjacencyMatrix = new int[raptatTokenPhrases.size()][annotatedPhrases.size()];

    int i = 0;
    for (final RaptatTokenPhrase tokenPhrase : raptatTokenPhrases) {
      int j = 0;
      for (final AnnotatedPhrase annotatedPhrase : annotatedPhrases) {
        /*
         * This could probably be sped up to more rapidly find overlapping phrases
         */
        adjacencyMatrix[i][j] =
            AnnotationToTokenPhraseComparator.getSimilarityScore(tokenPhrase, annotatedPhrase);
        j++;
      }
      i++;
    }

    return adjacencyMatrix;
  }

  /**
   * This is being replaced by processUpdated() method which will handle merging of classifications
   * with the same name and allow for mapping of multiple classes. Classifications that have the
   * same concept names of their PhraseClass instances will be merged if they map to the same
   * phrase. Phrase class instances with distinct names will not be merged.
   *
   */
  @SuppressWarnings("unused")
  @Override
  @Deprecated
  public ModuleResultBase process() {
    ArffCreationResults results = null;

    final TreeMap<Integer, DocumentMetaData> lineNumberToPhraseMap = new TreeMap<>();

    final FnPostArffFileCompleted arffCompletedFunction =
        new PostArffFileCompleted(lineNumberToPhraseMap);

    final ArffCreationParameterExtended parameter = (ArffCreationParameterExtended) getParameter();

    final String fileDescription = parameter.getFileDescription();
    final String projectOrigin = parameter.getProjectOrigin();

    /*
     * Any phrase classes with the same conceptName will be merged into one class during
     * cross-validation.
     */
    final Map<PhraseClass, Optional<String>> phraseClassToXmlMap =
        parameter.getPhraseClassToXmlMap();
    Set<String> conceptNames = new HashSet<>();
    phraseClassToXmlMap.keySet()
        .forEach(phraseClass -> conceptNames.add(phraseClass.getConceptName()));

    final TextAnalyzer textAnalyzer = parameter.textAnalyzer;
    Map<String, Set<String>> categoryToSubcategoryMap =
        ((TokenPhraseMakerGG) textAnalyzer.getTokenPhraseMaker()).getCategoryToSubcategoryMap();

    /*
     * Copy the mapping as we may modify it when we search for features
     */
    Map<String, Set<String>> catToSubcatMapCopy = new HashMap<>();
    for (Entry<String, Set<String>> entry : categoryToSubcategoryMap.entrySet()) {
      catToSubcatMapCopy.put(entry.getKey(), new HashSet<>(entry.getValue()));
    }

    String arffFilePath = null;

    /*
     * Use just the first phrase class since we are modifying the code
     */
    PhraseClass phraseClass = phraseClassToXmlMap.keySet().toArray(new PhraseClass[] {})[0];
    try (ArffFileWriter writer = new ArffFileWriter(projectOrigin, fileDescription, phraseClass,
        parameter.getOutputDirectory().get())) {

      arffFilePath = writer.getArffFilePath();

      int current_file_count = 0;

      final File[] documents = getDocuments();
      for (final File document : documents) {
        final String documentPath = document.getPath();
        final RaptatDocument raptatDocument = textAnalyzer.processDocument(documentPath);

        this.simpleFeatureBuilder.addPhraseFeatures(raptatDocument.getTokenPhrases(),
            new Label(phraseClassToXmlMap.toString().toLowerCase()), catToSubcatMapCopy);
        String targetedLabelName = null;
        buildMetaFeatures(raptatDocument, targetedLabelName);

        final String xmlDocumentPath = getXmlDocumentPath(
            phraseClassToXmlMap.get(phraseClass).get(), FilenameUtils.getName(document.getName()));

        writer.exportPhraseFeatures(raptatDocument, document.getPath(), xmlDocumentPath);

        for (final RaptatTokenPhrase phrase : raptatDocument.getTokenPhrases()) {
          if (isTargetConcept(phrase, phraseClass)) {
            lineNumberToPhraseMap.put(lineNumberToPhraseMap.size(),
                new DocumentMetaData(phrase, documentPath, xmlDocumentPath));
          }
        }

        LOGGER.info(MessageFormat.format("\n\t{0} of {1} FILES PR0CESSED", ++current_file_count,
            documents.length));
      }

    } catch (final Exception e) {
      System.err.println(e.getMessage());
      results = null;
    }

    PostArffFileResults postArffWriteResults;
    try {
      /** 6/18/19 - DAX - For now this is written at the end */
      postArffWriteResults = arffCompletedFunction.writeArffFile(arffFilePath);
      results = new ArffCreationResults(postArffWriteResults, arffFilePath);
    } catch (final IOException e) {
      getLogger().severe(e.getMessage());
      results = null;
    }

    return results;
  }

  /**
   * This is the key method for this class and encompasses the building of features for all the
   * phrase classes specified in the 'parameter' field
   */
  @Override
  public Map<String, Map<PhraseClass, Instances>> processUpdated() {

    /*
     * The 'parameter' variable stores the information needed to create an arff file such as the
     * directory containing the corpus, a map of PhraseClass instances containing the strings
     * representing the conceptName instances we want to train to assign attribute values to. In the
     * case of an index value, attribute values might be left, right, or bilateral to assign
     * laterality, or they might be abi or tbi to assign index value type.
     */
    final ArffCreationParameterExtended parameter = (ArffCreationParameterExtended) getParameter();

    /*
     * Any phrase classes with the same conceptName will be merged into one class during
     * cross-validation. Unique conceptNames will not be merged. Note that PhraseClass instances
     * with the same conceptName can use the same features to train a classifier to classify among
     * different sets of attribute values, so those features only need to be generated once.
     *
     * An example of this would be to classify among left, right, and bilateral, and to classify
     * among abi and tbi. Because both are targeting the concept index value, which should be
     * virtually the same phrases in both sets, we can use the same features as the features for a
     * given phrase with the concept index value should be the same. What will change for a given
     * index value is whether the laterality is left, right, bilateral, or nothing and whether the
     * index type is abi, tbi, or nothing.
     *
     * We create this map so that we can connect a phrase class with its attribute values to an xml
     * file that contains the reference attribute values (generally provided as 'concepts' not
     * attribute values because in eHOST xml attributes provide greater specification to concepts).
     */
    final Map<PhraseClass, Optional<String>> phraseClassToXmlMap =
        parameter.getPhraseClassToXmlMap();


    /*
     * We may want to map conceptName to a list of phraseClasses if multiple phraseClasses come from
     * a single concept, such as when attributes, like laterality and index type, are assigned to
     * the same concept, index value. When this is the case, the phraseClasses can use the same
     * features of putative concepts found in the text. The only difference is the classes the
     * putative concepts are assigned to. This is done to reduce unnecessary document processing and
     * feature generation.
     *
     * We generate a mapping of concepts to PhraseClass instances and store it in
     * conceptNameToPhraseClassMap.
     */
    Map<String, Set<PhraseClass>> conceptNameToPhraseClassMap = new HashMap<>();
    for (PhraseClass phraseClass : phraseClassToXmlMap.keySet()) {
      String phraseClassConcept = phraseClass.getConceptName().toLowerCase();
      Set<PhraseClass> mappedClasses =
          conceptNameToPhraseClassMap.computeIfAbsent(phraseClassConcept, k -> new HashSet<>());
      mappedClasses.add(phraseClass);
    }

    final TextAnalyzer textAnalyzer = parameter.textAnalyzer;
    Map<String, Set<String>> categoryToSubcategoryMap =
        ((TokenPhraseMakerGG) textAnalyzer.getTokenPhraseMaker()).getCategoryToSubcategoryMap();

    /* Get the Weka instances corresponding to each PhraseClass type in the phraseClassToXmlMap */
    Map<String, Map<PhraseClass, Instances>> resultMap = getConceptToPhraseClassToInstancesMap(
        phraseClassToXmlMap, conceptNameToPhraseClassMap, textAnalyzer, categoryToSubcategoryMap);
    return resultMap;
  }

  public void setAttributeFilters(final Collection<AttributeFilter> attributeFilters) {
    this.attributeFilters = new ArrayList<>(attributeFilters);
  }

  /**
   * @param logTokenPhrases the logTokenPhrases to set
   */
  public void setLogTokenPhrases(final boolean logTokenPhrases) {
    this.logTokenPhrases = logTokenPhrases;
  }

  /**
   * Builds the column features.
   *
   * @param document the document
   * @param featureBuilders the feature builders
   * @param runDirection the run direction
   */
  private void build_column_features(final RaptatDocument document,
      final FeatureBuilder[] featureBuilders, final RunDirection runDirection) {
    final FeatureBuilderRunner runner = new FeatureBuilderRunner(featureBuilders);
    final List<DocumentColumn> listOfColumnsOfPhrases = document.getDocumentColumns();
    for (final DocumentColumn documentColumn : listOfColumnsOfPhrases) {
      runner.process_column(documentColumn.columnPhrases, runDirection,
          document.getProcessedTokens());
    }
  }

  /**
   * Builds the features for columns, rows, and whole document
   *
   * @param document the document
   * @param featureBuilders the feature builders
   */
  private void build_features(final RaptatDocument document,
      final FeatureBuilder[] featureBuilders) {

    build_column_features(document, featureBuilders, RunDirection.Forward);
    build_column_features(document, featureBuilders, RunDirection.Reverse);
    build_row_features(document, featureBuilders, RunDirection.Forward);
    build_row_features(document, featureBuilders, RunDirection.Reverse);
    build_features_for_document(document, featureBuilders, RunDirection.Forward);
    build_features_for_document(document, featureBuilders, RunDirection.Reverse);
    for (final RaptatTokenPhrase tokenPhrase : document.getTokenPhrases()) {
      final Set<String> lowerCasedFeatures = new HashSet<>();
      for (final String feature : tokenPhrase.getPhraseFeatures()) {
        lowerCasedFeatures.add(feature.toLowerCase());
      }
      tokenPhrase.resetPhraseFeatures(lowerCasedFeatures);
    }
  }

  /**
   * Builds the features for all phrases in the document, left-to-right, top-to-bottom
   *
   * @param document the document
   * @param featureBuilders the feature builders
   */
  private void build_features_for_document(final RaptatDocument document,
      final FeatureBuilder[] featureBuilders, final RunDirection direction) {
    final FeatureBuilderRunner runner = new FeatureBuilderRunner(featureBuilders);
    runner.process_document(document.getTokenPhrases(), direction, document.getProcessedTokens());
  }

  /**
   * Builds the row features.
   *
   * @param document the document
   * @param featureBuilders the feature builders
   * @param runDirection the run direction
   */
  private void build_row_features(final RaptatDocument document,
      final FeatureBuilder[] featureBuilders, final RunDirection runDirection) {
    final FeatureBuilderRunner runner = new FeatureBuilderRunner(featureBuilders);
    final List<TextDocumentLine> textDocumentLines = document.getTextDocumentLines();
    for (final TextDocumentLine line : textDocumentLines) {
      runner.process_row(line.getTokenPhraseList(), runDirection, document.getProcessedTokens());
    }
  }

  private void buildMetaFeatures(final RaptatDocument raptatDocument,
      final String targetLabelName) {
    final List<RaptatTokenPhrase> targetedPhrases =
        getTargetedPhrases(raptatDocument, targetLabelName);
    for (final MetaFeatureBuilder metaFeatureBuilder : this.metaFeatureBuilders) {
      metaFeatureBuilder.buildFeatures(targetedPhrases);
    }
  }

  /**
   * Convert token phrases that have been mapped to a class into Instance objects for use by Weka.
   *
   * @param phraseToClassMap
   * @param textSource
   * @param modelSpecificAttributeNames
   * @param modelSpecificAttributes
   * @return
   */
  private ArrayList<Attribute> convertToAttributes(
      final Map<RaptatTokenPhrase, String> phraseToClassMap, final String textSource,
      final Map<String, Attribute> modelSpecificAttributes) {


    for (RaptatTokenPhrase tokenPhrase : phraseToClassMap.keySet()) {

      for (String phraseFeature : tokenPhrase.getPhraseFeatures()) {
        Attribute mappedAttrbute = modelSpecificAttributes.computeIfAbsent(phraseFeature,
            key -> new Attribute(key, binaryFeatureValues));
      }
    }
    return null;
  }

  /**
   * @param conceptNameToPhraseClassesMap
   * @param conceptToFeatureNameToAttributeMap
   * @return
   */
  private Map<String, Map<PhraseClass, Instances>> createResultMap(
      final Map<String, Set<PhraseClass>> conceptNameToPhraseClassesMap,
      final Map<String, Map<String, Attribute>> conceptToFeatureNameToAttributeMap) {

    Map<String, Map<PhraseClass, Instances>> resultMap = new HashMap<>();

    for (String conceptName : conceptNameToPhraseClassesMap.keySet()) {

      Map<String, Attribute> featureNameToAttributeMap =
          conceptToFeatureNameToAttributeMap.get(conceptName);
      Map<PhraseClass, Instances> phraseClassToInstancesMap =
          resultMap.computeIfAbsent(conceptName, key -> new HashMap<>());

      for (PhraseClass phraseClass : conceptNameToPhraseClassesMap.get(conceptName)) {

        /* We add 1 to account for the class attribute */
        List<Attribute> commonAttributes = AttributeHelper.getCommonAttributes();
        ArrayList<Attribute> attributes =
            new ArrayList<>(featureNameToAttributeMap.size() + commonAttributes.size() + 1);
        attributes.addAll(commonAttributes);
        attributes.addAll(featureNameToAttributeMap.values());
        Attribute classAttribute = new Attribute(phraseClass.toString(),
            new ArrayList<>(phraseClass.getClassifications()));
        attributes.add(classAttribute);

        Instances instances = new Instances(phraseClass.toString(), attributes, 2048);
        instances.setClass(classAttribute);

        phraseClassToInstancesMap.put(phraseClass, instances);
      }
    }
    return resultMap;
  }


  /**
   * Filter out attributes based on the filters associated with this object (a
   * MetaFeatureBuilderAndArffCreator) based on the conceptToFeatureNameToAttributeMap and the
   * conceptToFeatureToFrequencyMap.
   *
   * @param conceptToFeatureNameToAttributeMap
   * @param conceptToFeatureToFrequencyMap
   * @param conceptToTotalTokenPhrasesMap
   */
  private void filterAttributeMappings(
      final Map<String, Map<String, Attribute>> conceptToFeatureNameToAttributeMap,
      final Map<String, Map<String, Integer>> conceptToFeatureToFrequencyMap,
      final HashMap<String, Integer> conceptToTotalTokenPhrasesMap) {

    for (String conceptName : conceptToFeatureNameToAttributeMap.keySet()) {

      Integer totalTokenPhrases = conceptToTotalTokenPhrasesMap.get(conceptName);
      Map<String, Integer> featureToFrequencyMap = conceptToFeatureToFrequencyMap.get(conceptName);
      for (AttributeFilter attributeFilter : this.attributeFilters) {
        attributeFilter.initialize(new RaptatPair<>(featureToFrequencyMap, totalTokenPhrases));
      }

      Map<String, Attribute> featureNameToAttributeMap =
          conceptToFeatureNameToAttributeMap.get(conceptName);
      for (AttributeFilter attributeFilter : this.attributeFilters) {
        List<String> featureNames = new ArrayList<>(featureNameToAttributeMap.keySet());
        for (String featureName : featureNames) {
          Attribute attribute = featureNameToAttributeMap.get(featureName);
          if (!attributeFilter.acceptAttribute(attribute)) {
            featureNameToAttributeMap.remove(featureName);
          }
        }
      }
    }
  }


  /**
   * Populate the conceptToFeatureNameToAttributeMap and documentToConceptToTokenPhrasesMap
   *
   * @param conceptToFeatureNameToAttributeMap
   * @param documentToConceptToTokenPhrasesMap
   * @param conceptNameToPhraseClasses
   * @param categoryToSubcategoriesMap
   * @param textAnalyzer
   * @return
   */
  private void generateMappingsForAttributeBuilding(
      final Map<String, Map<String, Attribute>> conceptToFeatureNameToAttributeMap,
      final Map<String, Map<String, List<RaptatTokenPhrase>>> documentToConceptToTokenPhrasesMap,
      final Map<String, Map<String, Integer>> conceptToFeatureToFrequencyMap,
      final Map<String, Set<PhraseClass>> conceptNameToPhraseClasses,
      final Map<String, Set<String>> categoryToSubcategoriesMap, final TextAnalyzer textAnalyzer) {
    /*
     * Copy the mapping as we may modify it when search for features
     */
    Map<String, Set<String>> catToSubcatMapCopy = new HashMap<>();
    for (Entry<String, Set<String>> entry : categoryToSubcategoriesMap.entrySet()) {
      catToSubcatMapCopy.put(entry.getKey(), new HashSet<>(entry.getValue()));
    }

    try {
      Optional<PrintWriter> optionalPrintWriter = Optional.empty();
      if (this.logTokenPhrases) {
        String pwFileName = "DocumentTokenPhrases_" + GeneralHelper.getTimeStamp() + ".txt";
        File corpusDirectory =
            new File(((ArffCreationParameter) getParameter()).getCorpusDirectory());
        String pwFilePathString = corpusDirectory.getParent();
        String logFilePath = pwFilePathString + File.separator + pwFileName;
        System.out.println("Logging document token phrases to " + logFilePath);
        PrintWriter pw = new PrintWriter(new File(logFilePath));
        pw.println(
            "Document\tContainsPutativeIndexValue\tPhraseLabel\tPhraseText\tStartOffset\tEndOffset");
        pw.flush();
        optionalPrintWriter = Optional.of(pw);
      }

      /*
       * Process each document in the set and identify all the attributes. We are doing this because
       * we need to know the total number of attributes BEFORE we can create the Weka instance
       * objects used for training and testing our model.
       */
      for (final File document : getDocuments()) {
        final String documentPath = document.getPath();
        final RaptatDocument raptatDocument = textAnalyzer.processDocument(documentPath);
        final String documentName = raptatDocument.getTextSource();
        if (optionalPrintWriter.isPresent()) {
          logDocumentPhrases(optionalPrintWriter.get(), raptatDocument);
        }

        Map<String, List<RaptatTokenPhrase>> conceptToTokenPhrasesMap = new HashMap<>();
        documentToConceptToTokenPhrasesMap.put(documentName, conceptToTokenPhrasesMap);

        /*
         * Token phrases for the same concept should have the same features. For example, index
         * value concepts should point to the same phrases (index values like 0.20, 0.30, 1.2, etc)
         * and therefore have the same features, but a concept like 'compressibility' would
         * correspond to different phrases and their features. The only thing that should change for
         * a different PhraseClass instance is the assigned classification. For example, a phrase,
         * 0.20, assigned to the concept indexValue could have a classification of 'right' for the
         * PhraseClass INDEX_VALUE_LATERALITY and a classification of 'abi' for the PhraseClass
         * INDEX_VALUE_TYPE.
         *
         * We loop through conceptNames so we only have to build features once for each concept.
         */
        for (String conceptName : conceptNameToPhraseClasses.keySet()) {

          /* Add features to the TokenPhrase instances within the document */
          this.simpleFeatureBuilder.addPhraseFeatures(raptatDocument.getTokenPhrases(),
              new Label(conceptName.toLowerCase()), catToSubcatMapCopy);
          buildMetaFeatures(raptatDocument, conceptName);

          /*
           * Create mappings of document name to RaptatTokenPhrase instances for that document
           */
          List<RaptatTokenPhrase> candidateTokenPhrases =
              getCandidatePhrases(raptatDocument.getTokenPhrases(), conceptName);
          conceptToTokenPhrasesMap.put(conceptName, candidateTokenPhrases);

          /*
           * Get all the Attributes and their names corresponding to the current concept name and
           * document.
           */
          Map<String, Attribute> featureNameToAttribueMap = conceptToFeatureNameToAttributeMap
              .computeIfAbsent(conceptName, key -> new HashMap<>(2 ^ 16));
          Map<String, Integer> featureFrequency =
              updateConceptSpecificAttributes(candidateTokenPhrases, featureNameToAttribueMap);
          featureFrequency.forEach((key, value) -> conceptToFeatureToFrequencyMap.get(conceptName)
              .merge(key, value, (v1, v2) -> v1 + v2));
        }
      }
    } catch (FileNotFoundException e) {
      System.err.println("Unable to write document token phrases\n" + e.getLocalizedMessage());
      e.printStackTrace();
    } catch (URISyntaxException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Takes a list of phrases and returns those that have a phraseLabel instance that is equivalent
   * to to a Label with the name and value both equal to conceptPhraseName
   *
   * @param documentPhrases
   * @return
   */
  private List<RaptatTokenPhrase> getCandidatePhrases(final List<RaptatTokenPhrase> documentPhrases,
      final String conceptPhraseName) {
    final List<RaptatTokenPhrase> identifiedPhrases = new ArrayList<>();
    final Label conceptNameLabel = new Label(conceptPhraseName.toLowerCase());
    for (final RaptatTokenPhrase phrase : documentPhrases) {
      if (phrase.getPhraseLabels().contains(conceptNameLabel)) {
        identifiedPhrases.add(phrase);
      }
    }
    return identifiedPhrases;
  }

  /**
   * Get a map indicating the Weka instances corresponding to each concept name and PhraseClass type
   * in the conceptNameToPhraseClasses map and phraseClassToXmlMap
   *
   * @param phraseClassToXmlMap
   * @param conceptNameToPhraseClassesMap
   * @param textAnalyzer
   * @param catToSubcatMapCopy
   * @return
   */
  private Map<String, Map<PhraseClass, Instances>> getConceptToPhraseClassToInstancesMap(
      final Map<PhraseClass, Optional<String>> phraseClassToXmlMap,
      final Map<String, Set<PhraseClass>> conceptNameToPhraseClassesMap,
      final TextAnalyzer textAnalyzer, final Map<String, Set<String>> categoryToSubcategoryMap) {

    /*
     * conceptToFeatureNameToAttributeMap maps all the concept names to a mapping of all the feature
     * names to their corresponding attributes of each feature name for that concept. A given
     * concept should have the same feature names and corresponding Weka attributes.
     */
    Map<String, Map<String, Attribute>> conceptToFeatureNameToAttributeMap = new HashMap<>();

    /*
     * documentToConceptToPhrasesMap will map each document to a concept within the document. We'll
     * use this to create Weka instance objects once all the attributes of the concept are found
     * over all documents. Note that for a given document and concept, the RaptatTokenPhrase objects
     * should be the same
     *
     */
    Map<String, Map<String, List<RaptatTokenPhrase>>> documentToConceptToTokenPhrasesMap =
        new HashMap<>();

    /*
     * conceptToFeatureToFrequencyMap will map, for each concept, how often each feature occurs
     * (i.e. its frequency). We go ahead and add the "mapped to map" so we don't have to keep
     * checking for it in other methods called by this one. This will be used later for filtering
     * attributes. It would probably be better to have this included as a lambda or series of
     * lambdas rather than put them associated with a specific filter as we do here.
     */
    Map<String, Map<String, Integer>> conceptToFeatureToFrequencyMap = new HashMap<>();
    conceptNameToPhraseClassesMap.forEach((key, value) -> conceptToFeatureToFrequencyMap.put(key,
        new HashMap<String, Integer>(2048)));

    /*
     * Populate the conceptModelSpecificAttributes and the documentToConceptToPhrasesMap. This is
     * where most of the work happens building all the attributes in the corpus.
     */
    generateMappingsForAttributeBuilding(conceptToFeatureNameToAttributeMap,
        documentToConceptToTokenPhrasesMap, conceptToFeatureToFrequencyMap,
        conceptNameToPhraseClassesMap, categoryToSubcategoryMap, textAnalyzer);



    /*
     * Filter out attributes based on the filters associated with this object (a
     * MetaFeatureBuilderAndArffCreator) based on the conceptToFeatureNameToAttributeMap and the
     * conceptToFeatureToFrequencyMap.
     */

    HashMap<String, Integer> conceptToTotalTokenPhrasesMap = new HashMap<>();
    for (String documentName : documentToConceptToTokenPhrasesMap.keySet()) {
      Map<String, List<RaptatTokenPhrase>> conceptToTokenPhraseMap =
          documentToConceptToTokenPhrasesMap.get(documentName);
      for (String conceptName : conceptToTokenPhraseMap.keySet()) {
        Integer phrasesInDocument = conceptToTokenPhraseMap.get(conceptName).size();
        conceptToTotalTokenPhrasesMap.compute(conceptName,
            (key, value) -> value == null ? phrasesInDocument : value + phrasesInDocument);
      }
    }

    if (LOGGER.isDebugEnabled()) {
      String loggingTag = "FEATURES BEFORE FILTERING";
      logFeatureToAttributeMap(conceptToFeatureNameToAttributeMap, loggingTag);
      // logFeatureToFrequencyMap(conceptToFeatureToFrequencyMap, loggingTag);
    }

    filterAttributeMappings(conceptToFeatureNameToAttributeMap, conceptToFeatureToFrequencyMap,
        conceptToTotalTokenPhrasesMap);

    if (LOGGER.isDebugEnabled()) {
      String loggingTag = "FEATURES AFTER FILTERING";
      logFeatureToAttributeMap(conceptToFeatureNameToAttributeMap, loggingTag);
      // logFeatureToFrequencyMap(conceptToFeatureToFrequencyMap, loggingTag);
    }

    /*
     * Create the map we'll be return as a result. This will create the Instances objects mapped to
     * by the PhraseClass objects, but the Instance (singular) objects will be added later with the
     * updateInstances method.
     */
    Map<String, Map<PhraseClass, Instances>> resultMap =
        createResultMap(conceptNameToPhraseClassesMap, conceptToFeatureNameToAttributeMap);

    AnnotationApp annotationApp = AnnotationApp.EHOST;
    AnnotationImporter annotationImporter = new AnnotationImporter(annotationApp);

    File[] documents = getDocuments();
    for (final File document : documents) {

      String documentName = document.getName();
      String documentPath = document.getAbsolutePath();
      System.out.println("Updating instances for document " + documentPath);

      Map<String, List<RaptatTokenPhrase>> conceptToTokenPhrasesMap =
          documentToConceptToTokenPhrasesMap.get(documentName);

      for (String conceptName : conceptNameToPhraseClassesMap.keySet()) {

        Map<String, Attribute> featureNameToAttributeMap =
            conceptToFeatureNameToAttributeMap.get(conceptName);
        List<RaptatTokenPhrase> raptatTokenPhrases = conceptToTokenPhrasesMap.get(conceptName);

        /* Loop through the phraseClass instances for the current document and conceptName */
        for (PhraseClass phraseClass : conceptNameToPhraseClassesMap.get(conceptName)) {

          /*
           * We use optionalAnnotatedPhrases when we are assigning a class and not testing whether
           * it is assigned correctly. The xml path provides the path to the reference annotations
           * for testing, so if that path is not present, the annotatedPhrases are not present.
           */
          Optional<List<AnnotatedPhrase>> optionalAnnotatedPhrases = Optional.empty();
          Optional<String> optionalXmlPath = phraseClassToXmlMap.get(phraseClass);
          if (optionalXmlPath.isPresent()) {

            String xmlPath =
                optionalXmlPath.get() + File.separator + documentName + ".knowtator.xml";
            optionalAnnotatedPhrases = Optional.of(annotationImporter.importAnnotations(xmlPath,
                documentPath, phraseClass.getClassifications()));
          }

          /*
           * The tokenPhraseToClassMap contains the mappings of all tokenPhrases for the current
           * conceptName and phraseClass to their classification (e.g. left, right, unassigned for
           * the concept indexValue an phraseClass Index_Value_Laterality).
           */
          Map<RaptatTokenPhrase, Optional<String>> tokenPhraseToClassMap =
              getPhraseToClassMap(raptatTokenPhrases, optionalAnnotatedPhrases);
          Instances instances = resultMap.get(conceptName).get(phraseClass);
          updateInstancesObject(instances, documentName, featureNameToAttributeMap,
              tokenPhraseToClassMap);

        }
      }
    }
    return resultMap;
  }

  private final File[] getDocuments() {
    String corpusDirectory = null;
    try {
      corpusDirectory = ((ArffCreationParameter) getParameter()).getCorpusDirectory();
    } catch (final URISyntaxException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    final File[] listOfFiles = new File(corpusDirectory).listFiles();

    return Arrays.stream(listOfFiles).filter(x -> x.isFile()).toArray(File[]::new);
  }

  /**
   * Assigns a string value based on the text within a token phrase that is an index value,
   * generally of the form 'x.yz' where x, y, and z are integers.
   *
   * @param tokenPhrase
   * @throws NumberFormatException
   */
  private String getIndexValueCategory(final RaptatTokenPhrase tokenPhrase)
      throws NumberFormatException {
    double indexValue = Double.parseDouble(tokenPhrase.getPhraseText());
    int valueIndex = 0;
    while (valueIndex < QUALITATIVE_INDEX_VALUE.size()) {
      if (indexValue > INDEX_VALUE_BOUNDS[valueIndex]
          && indexValue <= INDEX_VALUE_BOUNDS[valueIndex + 1]) {
        return QUALITATIVE_INDEX_VALUE.get(valueIndex);
      }
      valueIndex++;
    }
    return null;
  }


  /**
   * Create a map that maps each candidatePhrase to its corresponding class as determined based on
   * matching them to the annotatedPhrases supplied as a parameter. Any phrases that are candidates
   * and have no matching annotated phrase will be assigned to the string representation of
   * PhraseClass.NO_CLASS_ASSIGNED.
   *
   * @param candidatePhrases
   * @param annotatedPhrases
   * @return
   */
  private Map<RaptatTokenPhrase, Optional<String>> getPhraseToClassMap(
      final List<RaptatTokenPhrase> candidatePhrases,
      final Optional<List<AnnotatedPhrase>> optionalPhrases) {

    final Map<RaptatTokenPhrase, Optional<String>> resultMap =
        new HashMap<>(candidatePhrases.size());

    if (!optionalPhrases.isPresent()) {
      candidatePhrases
          .forEach(raptatTokenPhrase -> resultMap.put(raptatTokenPhrase, Optional.empty()));
      return resultMap;
    }


    List<AnnotatedPhrase> annotatedPhrases = optionalPhrases.get();
    int[] matchingArray = matchCandidatePhrasesToAnnotations(candidatePhrases, annotatedPhrases);

    int i = 0;
    for (final RaptatTokenPhrase tokenPhrase : candidatePhrases) {

      final String mappedClassName =
          matchingArray[i] == -1 ? PhraseClass.NO_CLASS_ASSIGNED.getConceptName()
              : annotatedPhrases.get(matchingArray[i]).getConceptName();
      i = i + 1;
      resultMap.put(tokenPhrase, Optional.of(mappedClassName));

    }
    return resultMap;
  }

  /**
   * Find the proper (targeted) phrases for building metafeatures. We want only phrases that have a
   * Label name equal to the name of the label being targeted, which is equivalent to the
   * phraseClass name (via toString) of the ArffCreationParameter parameter field of this object. *
   * *
   *
   * @param raptatDocument
   * @param targetLabelName
   * @return
   */
  private List<RaptatTokenPhrase> getTargetedPhrases(final RaptatDocument raptatDocument,
      final String targetLabelName) {
    final RaptatTokenPhrase.Label labelBeingTargeted =
        new RaptatTokenPhrase.Label(targetLabelName.toLowerCase());
    final List<RaptatTokenPhrase> targetedPhrases = new ArrayList<>();
    for (final RaptatTokenPhrase candidatePhrase : raptatDocument.getTokenPhrases()) {
      if (candidatePhrase.containsLabelName(labelBeingTargeted)) {
        targetedPhrases.add(candidatePhrase);
      }
    }
    return targetedPhrases;
  }

  /**
   * Gets the ehost xml document name.
   *
   * @param xmlDirectoryPath the ehost xml document path
   * @param fileName the file name
   * @return the ehost xml document name
   */
  private String getXmlDocumentPath(final String xmlDirectoryPath, final String fileName) {
    return xmlDirectoryPath + "\\" + fileName + ".knowtator.xml";
  }

  private boolean isTargetConcept(final RaptatTokenPhrase phrase, final PhraseClass phraseClass) {
    final boolean isTargetConcept = phrase.getPhraseLabels()
        .contains(new RaptatTokenPhrase.Label(phraseClass.getConceptName().toLowerCase()));
    return isTargetConcept;
  }

  /**
   * ABI specific method to log whether document contains one or more "indexvalue" labeled
   * RaptatTokenPhrase instances
   *
   * @param pw
   * @param raptatDocument
   */
  private void logDocumentPhrases(final PrintWriter pw, final RaptatDocument raptatDocument) {
    String textSource = raptatDocument.getTextSource();
    List<RaptatTokenPhrase> tokenPhrases = raptatDocument.getTokenPhrases();
    if (tokenPhrases == null || tokenPhrases.isEmpty()) {
      pw.println(textSource + "\tFALSE");
      pw.flush();
      return;
    }
    for (RaptatTokenPhrase tokenPhrase : raptatDocument.getTokenPhrases()) {
      for (Label label : tokenPhrase.getPhraseLabels()) {
        if (label.get_name().equalsIgnoreCase("indexValue")) {
          pw.println(textSource + "\tTRUE");
          pw.flush();
          return;
        }
      }
    }
    pw.println(textSource + "\tFALSE");
    pw.flush();
    return;
  }

  private void logFeatureToAttributeMap(
      final Map<String, Map<String, Attribute>> conceptToFeatureNameToAttributeMap,
      final String loggingTag) {
    String spacer = "-----------------------";
    System.out.println(spacer);
    System.out.println(loggingTag);
    System.out.println(spacer);
    for (String concept : conceptToFeatureNameToAttributeMap.keySet()) {
      System.out.println("CONCEPT: " + concept);
      Map<String, Attribute> featureToAttributeMap =
          conceptToFeatureNameToAttributeMap.get(concept);
      for (String feature : featureToAttributeMap.keySet()) {
        System.out.println(feature);
      }
    }

  }


  /**
   * @param conceptToFeatureToFrequencyMap
   * @param loggingTag
   */
  private void logFeatureToFrequencyMap(
      final Map<String, Map<String, Integer>> conceptToFeatureToFrequencyMap,
      final String loggingTag) {
    String spacer = "-----------------------";
    System.out.println(spacer);
    System.out.println(loggingTag);
    System.out.println(spacer);
    for (String concept : conceptToFeatureToFrequencyMap.keySet()) {
      System.out.println("CONCEPT: " + concept);
      Map<String, Integer> featureToFrequencyMap = conceptToFeatureToFrequencyMap.get(concept);
      for (Entry<String, Integer> featureAndFrequency : featureToFrequencyMap.entrySet()) {
        System.out.println(featureAndFrequency.getValue() + " : " + featureAndFrequency.getKey());
      }
    }
  }

  /**
   * @param candidatePhrases
   * @param annotatedPhrases
   * @return
   */
  private int[] matchCandidatePhrasesToAnnotations(final List<RaptatTokenPhrase> candidatePhrases,
      final List<AnnotatedPhrase> annotatedPhrases) {
    /*
     * The adjacency matrix relating candidate to annotatedPhrases
     */
    final int[][] adjacencyMatrix = getAdjacencyMatrix(candidatePhrases, annotatedPhrases);

    int[] candidateMatching = new int[0];
    if (adjacencyMatrix.length > 0) {
      final MunkresAssignmentTest mr = new MunkresAssignmentTest(adjacencyMatrix);
      candidateMatching = mr.solve();
    }

    /*
     * Remove any annotated phrases that do not overlap. This is a kludge. The MunkresAssignmentTest
     * solve() method should really handle this by setting non-matching to -1. Why it does not
     * always do this is unclear.
     */
    int tokenPhraseIndex = 0;
    for (RaptatTokenPhrase tokenPhrase : candidatePhrases) {
      if (candidateMatching[tokenPhraseIndex] >= 0) {
        AnnotatedPhrase matchedAnnotatedPhrase =
            annotatedPhrases.get(candidateMatching[tokenPhraseIndex]);
        int annotationStart = Integer.parseInt(matchedAnnotatedPhrase.getRawTokensStartOff());
        int annotationEnd = Integer.parseInt(matchedAnnotatedPhrase.getRawTokensEndOff());
        int tokenStart = tokenPhrase.get_start_offset_as_int();
        int tokenEnd = tokenPhrase.get_end_offset_as_int();
        if (!(tokenEnd > annotationStart && tokenStart < annotationEnd)) {
          candidateMatching[tokenPhraseIndex] = -1;
        }
      }
      tokenPhraseIndex++;
    }
    return candidateMatching;

  }


  /**
   * Loop through the RaptatTokenPhrase objects in the tokenPhrases list, populate the
   * conceptSpecificPhraseStringToAttributeMap parameter, and return how many times the name of the
   * attribute is found in the RaptatTokenPhrase objects.
   *
   * @param tokenPhrases
   * @param documentName
   * @param conceptSpecificPhraseStringToAttributeMap
   * @return
   */
  private Map<String, Integer> updateConceptSpecificAttributes(
      final List<RaptatTokenPhrase> tokenPhrases,
      final Map<String, Attribute> conceptSpecificPhraseStringToAttributeMap) {

    Map<String, Integer> featureFrequency = new HashMap<>();
    for (RaptatTokenPhrase tokenPhrase : tokenPhrases) {
      Set<String> phraseFeatures = tokenPhrase.getPhraseFeatures();

      for (String phraseFeatureString : phraseFeatures) {
        Attribute phraseMappedAttribute = null;
        if ((phraseMappedAttribute =
            conceptSpecificPhraseStringToAttributeMap.get(phraseFeatureString)) == null) {
          phraseMappedAttribute = new Attribute(phraseFeatureString, binaryFeatureValues);
          conceptSpecificPhraseStringToAttributeMap.put(phraseFeatureString, phraseMappedAttribute);
        }
        featureFrequency.compute(phraseFeatureString,
            (key, value) -> value == null ? 1 : value + 1);
      }
    }

    return featureFrequency;
  }

  /**
   * Build the instances for the token phrases provided in tokenPhraseToClassMap, the features of
   * these token phrases and the attributes they are mapped to.
   *
   * @param instances
   *
   * @param documentName
   * @param featureNameToAttributeMap
   * @param tokenPhraseToClassMap
   * @return
   */
  private void updateInstancesObject(final Instances instances, final String documentName,
      final Map<String, Attribute> featureNameToAttributeMap,
      final Map<RaptatTokenPhrase, Optional<String>> tokenPhraseToClassMap) {

    int instanceSize = instances.numAttributes();
    for (RaptatTokenPhrase tokenPhrase : tokenPhraseToClassMap.keySet()) {

      double[] doubleArray = new double[instanceSize];
      Arrays.fill(doubleArray, 0);
      Instance instance = new DenseInstance(1, doubleArray);
      instance.setValue(instances.attribute(AttributeHelper.AttributeIndex.DOC_ID.toString()),
          documentName);
      instance.setValue(instances.attribute(AttributeHelper.AttributeIndex.START_OFFSET.toString()),
          Integer.toString(tokenPhrase.get_start_offset_as_int()));
      instance.setValue(instances.attribute(AttributeHelper.AttributeIndex.END_OFFSET.toString()),
          Integer.toString(tokenPhrase.get_end_offset_as_int()));
      instance.setValue(
          instances.attribute(AttributeHelper.AttributeIndex.INDEX_VALUE_STRING.toString()),
          tokenPhrase.getPhraseText());
      instance.setValue(
          instances.attribute(AttributeHelper.AttributeIndex.INDEX_VALUE_QUALITATIVE.toString()),
          getIndexValueCategory(tokenPhrase));

      Set<String> tokenPhraseFeatures = tokenPhrase.getPhraseFeatures();
      for (String featureName : featureNameToAttributeMap.keySet()) {

        Attribute attribute = featureNameToAttributeMap.get(featureName);
        if (tokenPhraseFeatures.contains(featureName)) {
          instance.setValue(attribute, binaryFeatureValues.get(1));
        } else {
          instance.setValue(attribute, binaryFeatureValues.get(0));
        }
      }
      Optional<String> mappedClass = tokenPhraseToClassMap.get(tokenPhrase);
      if (mappedClass.isPresent()) {
        instance.setValue(instances.classAttribute(), mappedClass.get());
      } else {
        instance.setMissing(instances.classAttribute());
      }
      instances.add(instance);
    }
  }

}

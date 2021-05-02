/** */
package src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import javax.swing.SwingUtilities;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ami.base.EvaluationMetricType;
import ami.classifier.config.BaseClassifierHyperparameterConfiguration;
import ami.classifier.config.RandomForestHyperparameterConfiguration;
import ami.classifier.property.MaxDepth;
import ami.classifier.property.NumberFeatures;
import src.main.gov.va.vha09.grecc.raptat.dw.feature_builders.builders.anatomy.AnatomyFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.dw.feature_builders.builders.anatomy.AnatomyLinesDistanceFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.dw.feature_builders.builders.anatomy.AnatomyTokensDistanceFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.dw.feature_builders.builders.common.FeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.dw.feature_builders.builders.index_type.IndexTypeFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.dw.feature_builders.builders.index_type.IndexTypeLinesDistanceFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.dw.feature_builders.builders.index_type.IndexTypeTokensDistanceFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.dw.feature_builders.builders.index_value.IndexValueFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.dw.feature_builders.builders.laterality.LateralityFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.dw.feature_builders.builders.laterality.LateralityLinesDistanceFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.dw.feature_builders.builders.laterality.LateralityTokensDistanceFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.dw.feature_builders.builders.pressure.PressureFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.dw.feature_builders.builders.pressure.PressureLinesDistanceFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.dw.feature_builders.builders.pressure.PressureTokensDistanceFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.core.RaptatConstants.AnnotationApp;
import src.main.gov.va.vha09.grecc.raptat.gg.core.UserPreferences;
import src.main.gov.va.vha09.grecc.raptat.gg.core.options.OptionsManager;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.RaptatPair;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotationGroup;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.ConceptRelation;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatAttribute;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.SchemaConcept;
import src.main.gov.va.vha09.grecc.raptat.gg.exporters.XMLExporterRevised;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.GeneralHelper;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.csv.CSVReader;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.schema.SchemaImporter;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.xml.AnnotationImporter;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.annotation.AnnotationMutator;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.attributefilters.AttributeFilter;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.attributefilters.AttributeFrequencyFilter;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.attributefilters.AttributeNameFilter;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.featurebuilders.artery.ArteryFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.featurebuilders.artery.ArteryLinesDistanceFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.featurebuilders.artery.ArteryTokensDistanceFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.featurebuilders.exercise.ExerciseFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.featurebuilders.exercise.ExerciseLinesDistanceFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.featurebuilders.exercise.ExerciseTokensDistanceFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.featurebuilders.impression.ImpressionFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.featurebuilders.impression.ImpressionLinesDistanceFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.featurebuilders.impression.ImpressionTokensDistanceFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.featurebuilders.interpretation.InterpretationFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.featurebuilders.interpretation.InterpretationLinesDistanceFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.featurebuilders.interpretation.InterpretationTokensDistanceFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.featurebuilders.negativeindexinfo.NegativeIndexInfoFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.featurebuilders.negativeindexinfo.NegativeIndexInfoLinesDistanceFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.featurebuilders.negativeindexinfo.NegativeIndexInfoTokensDistanceFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.featurebuilders.noncompressible.NonCompressibleFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.featurebuilders.noncompressible.NonCompressibleLinesDistanceFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.featurebuilders.noncompressible.NonCompressibleTokensDistanceFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.featurebuilders.range.RangeFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.featurebuilders.range.RangeLinesDistanceFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.featurebuilders.range.RangeTokensDistanceFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.featurebuilders.temporal.TemporalFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.featurebuilders.temporal.TemporalLinesDistanceFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.featurebuilders.temporal.TemporalTokensDistanceFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.helpers.AbiHelper;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.helpers.AttributeHelper;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.helpers.InstancesMerger;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.metafeatures.ClosestTagFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.metafeatures.MetaFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.metafeatures.MetaFeatureBuilderAndArffCreator;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.metafeatures.ProximityDescriptorMetaFeatureBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific.girotraabi.options.OptionsBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.RaptatDocument;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.TextAnalyzer;
import src.main.gov.va.vha09.grecc.raptat.gg.weka.entities.ArffCreationParameterExtended;
import src.test.gov.va.vha09.grecc.raptat.dw.use_cases.pipeline_arff_training_errorAnalysis_eHost.modules.ProcessModule;
import src.test.gov.va.vha09.grecc.raptat.dw.use_cases.pipeline_arff_training_errorAnalysis_eHost.modules.ArffPredictionsAndPhraseDataToEHost.ArffPredictionsAndPhraseDataToEHost;
import src.test.gov.va.vha09.grecc.raptat.dw.use_cases.pipeline_arff_training_errorAnalysis_eHost.modules.ArffPredictionsAndPhraseDataToEHost.ArffToEHostParameter;
import src.test.gov.va.vha09.grecc.raptat.dw.use_cases.pipeline_arff_training_errorAnalysis_eHost.modules.ArffPredictionsAndPhraseDataToEHost.ArffToEHostResults;
import src.test.gov.va.vha09.grecc.raptat.dw.use_cases.pipeline_arff_training_errorAnalysis_eHost.modules.ErrorAnalysisAndArffUpdateWithPredictions.ErrorAnalysisAndArffUpdateWithPredictions;
import src.test.gov.va.vha09.grecc.raptat.dw.use_cases.pipeline_arff_training_errorAnalysis_eHost.modules.ErrorAnalysisAndArffUpdateWithPredictions.ErrorAnalysisParameter;
import src.test.gov.va.vha09.grecc.raptat.dw.use_cases.pipeline_arff_training_errorAnalysis_eHost.modules.ErrorAnalysisAndArffUpdateWithPredictions.ErrorAnalysisResults;
import src.test.gov.va.vha09.grecc.raptat.dw.use_cases.pipeline_arff_training_errorAnalysis_eHost.modules.FeatureBuildersAndArffFileCreation.ArffCreationResults;
import src.test.gov.va.vha09.grecc.raptat.dw.use_cases.pipeline_arff_training_errorAnalysis_eHost.modules.FeatureBuildersAndArffFileCreation.PostArffFileCompleted.PostArffFileResults;
import src.test.gov.va.vha09.grecc.raptat.dw.use_cases.pipeline_arff_training_errorAnalysis_eHost.modules.TrainingOnArffWithSelectedClassifierPersisted.FnDefineClassifiersToReview;
import src.test.gov.va.vha09.grecc.raptat.dw.use_cases.pipeline_arff_training_errorAnalysis_eHost.modules.TrainingOnArffWithSelectedClassifierPersisted.ArffTrainingPersistedClassifier;
import src.test.gov.va.vha09.grecc.raptat.dw.use_cases.pipeline_arff_training_errorAnalysis_eHost.modules.TrainingOnArffWithSelectedClassifierPersisted.TrainingParameter;
import src.test.gov.va.vha09.grecc.raptat.dw.use_cases.pipeline_arff_training_errorAnalysis_eHost.modules.TrainingOnArffWithSelectedClassifierPersisted.TrainingResults;
import src.test.gov.va.vha09.grecc.raptat.gg.columnsandrows.export.PhraseClass;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.misc.InputMappedClassifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/** @author VHATVHGOBBEG */
public class GirotraABI {

//@formatter:off
  private enum ConceptTypeName {
    TARGET_NAME,
    FIRST_RELATED_CONCEPT_NAME,
    SECOND_RELATED_CONCEPT_NAME,
    FIRST_CONCEPT_TARGET_RELATION_NAME,
    SECOND_CONCEPT_TARGET_RELATION_NAME,
    FIRST_CONCEPT_TARGET_ATTRIBUTE_NAME,
    SECOND_CONCEPT_TARGET_ATTRIBUTE_NAME
  }
//@formatter:on


//@formatter:off
  private enum RunType {
    ADJUDICATE_SLC_CSV_OUTPUT,
    CORRECT_SALT_LAKE_ABI_TOOL_OFFSETS,
    CREATE_TOKEN_PHRASE_LABEL_XML,
    MERGE_ANNOTATION_SETS,
    MERGE_RELATED_ANNOTATIONS,
    PROMOTE_RELATIONS_TO_CONCEPTS,
    REMAP_CONCEPT_NAMES,
    RUN_MODEL,
    RUN_WEKA_CROSS_VALIDATION,
    TRAIN_AND_TEST,
    TRAIN_AND_TEST_INPUT_MAPPED_CLASSIFIER,
    TRAIN_MODEL,
    XML_TO_WEKA_INSTANCES
  }
//@formatter:on



  public static final List<RaptatPair<Integer, Integer>> PROXIMITIES = new ArrayList<>();

  private static final Logger LOGGER = Logger.getLogger(GirotraABI.class);

  /*
   * Proximities indicate closeness of some element to another in the text. A feature with a
   * distance of 2,3, or 4 would match the proximity pair (2, 5). The distance 5 would not match. So
   * the distance is from pair.left (inclusive) to pair.right (non-inclusive).
   */
  static {
    PROXIMITIES.add(new RaptatPair<>(1, 2));

    PROXIMITIES.add(new RaptatPair<>(1, 4));
    PROXIMITIES.add(new RaptatPair<>(2, 4));

    PROXIMITIES.add(new RaptatPair<>(4, 7));
    PROXIMITIES.add(new RaptatPair<>(1, 7));

    PROXIMITIES.add(new RaptatPair<>(7, 13));
    PROXIMITIES.add(new RaptatPair<>(1, 13));

    PROXIMITIES.add(new RaptatPair<>(14, Integer.MAX_VALUE));
  }

  static {
    LOGGER.setLevel(Level.INFO);
  }


  protected void correctSaltLakeAbiToolOffsets(final String textFilesPath,
      final String xmlFilesPathString, final String outputDirectoryPath,
      final String schemaFilePath) {
    final File outputDirectory = new File(outputDirectoryPath);
    if (!outputDirectory.exists()) {
      try {
        FileUtils.forceMkdir(outputDirectory);
      } catch (IOException e) {
        System.err.println("Unable to create result directory\n" + e.getLocalizedMessage());
        e.printStackTrace();
        System.exit(-1);
      }
    }
    final XMLExporterRevised exporter =
        new XMLExporterRevised(AnnotationApp.EHOST, outputDirectory, true);

    final Map<File, File> txtToXmlFileMap =
        GeneralHelper.getTxtToXmlFileMap(textFilesPath, xmlFilesPathString);
    final List<SchemaConcept> schemaConcepts = SchemaImporter.importSchemaConcepts(schemaFilePath);

    final AnnotationImporter annotationImporter = new AnnotationImporter(AnnotationApp.EHOST);
    annotationImporter.setReconcileAnnotationsWithSchema(true);
    annotationImporter.setReconcileAnnotationsWithText(true);

    int fileNumber = 0;
    for (final File textFile : txtToXmlFileMap.keySet()) {
      final String xmlFilePath = txtToXmlFileMap.get(textFile).getAbsolutePath();
      final List<AnnotatedPhrase> annotationList = annotationImporter.importAnnotations(xmlFilePath,
          textFile.getAbsolutePath(), null, schemaConcepts, null, false, true, false);
      System.out
          .println("\n\nFile " + fileNumber++ + ":  " + textFile.getName() + "\nAnnnotations:");
      System.out.println(annotationList);

      final RaptatDocument textDocument = new RaptatDocument();
      textDocument.setTextSourcePath(textFile.getAbsolutePath());
      final AnnotationGroup annotationGroup = new AnnotationGroup(textDocument, annotationList);
      exporter.exportReferenceAnnotationGroup(annotationGroup, true, false);
    }
  }

  protected Map<String, Map<PhraseClass, Instances>> getPhraseClassInstances(final String textPath,
      final Map<PhraseClass, Optional<String>> phraseClassToXmlMap,
      final Optional<String> outputDirectory, final TextAnalyzer textAnalyzer,
      final List<RaptatPair<Integer, Integer>> proximityDistances,
      final List<AttributeFilter> attributeFilters) throws FileNotFoundException, IOException {

    LOGGER.info("\n\tTEXT PATH:" + textPath + "\n\tOUTPUT PATH:" + outputDirectory + "\n");

    final ProcessModule processingModule = initializeMetaFeatureBuilders(textPath,
        phraseClassToXmlMap, outputDirectory, textAnalyzer, proximityDistances, attributeFilters);

    LOGGER.info("\n\tGENERATING PHRASE CLASS INSTANCES\n");
    final Map<String, Map<PhraseClass, Instances>> conceptToPhraseClassToInstancesMap =
        processingModule.processUpdated();


    return conceptToPhraseClassToInstancesMap;
  }

  /**
   * @param textFilesPath
   * @param xmlFilesPathAnnotatorOne
   * @param xmlFilesPathAnnotatorTwo
   * @param annotatorTwoConcepts
   * @param annotatorOneConcepts
   * @param conceptMap
   * @param outputDirectoryPath
   */
  protected void mergeAnnotationSets(final String textFilesPath,
      final String xmlFilesPathAnnotatorOne, final String xmlFilesPathAnnotatorTwo,
      final Set<String> annotatorOneConcepts, final Set<String> annotatorTwoConcepts,
      final Set<String> annotatorNames, final Map<String, String> conceptMap,
      final String outputDirectoryPath) {
    List<AnnotatedPhrase> mergedAnnotations = null;
    final AnnotationImporter xmlImporter = new AnnotationImporter(AnnotationApp.EHOST);
    final AnnotationMutator mutator = new AnnotationMutator();

    File outputDirectory = new File(outputDirectoryPath);
    if (!outputDirectory.exists()) {
      try {
        FileUtils.forceMkdir(outputDirectory);
      } catch (IOException e) {
        System.err.println("Unable to create folder " + outputDirectoryPath
            + " for merged results\n" + e.getLocalizedMessage());
        e.printStackTrace();
        System.exit(-1);
      }
    }
    final XMLExporterRevised xmlExporter =
        XMLExporterRevised.getExporter(new File(outputDirectoryPath), true);

    final Map<File, File> txtToXmlManualMap =
        GeneralHelper.getTxtToXmlFileMap(textFilesPath, xmlFilesPathAnnotatorOne);
    final Map<File, File> txtToXmlRapatMap =
        GeneralHelper.getTxtToXmlFileMap(textFilesPath, xmlFilesPathAnnotatorTwo);

    for (final File textFile : txtToXmlManualMap.keySet()) {
      final String xmlFilePathAnnotatorOne = txtToXmlManualMap.get(textFile).getAbsolutePath();
      final String xmlFilePathAnnotatorTwo = txtToXmlRapatMap.get(textFile).getAbsolutePath();

      List<AnnotatedPhrase> annotatorOneAnnotations = xmlImporter.importAnnotations(
          xmlFilePathAnnotatorOne, textFile.getAbsolutePath(), annotatorOneConcepts);
      annotatorOneAnnotations =
          mutator.filterForAnnotators(annotatorOneAnnotations, annotatorNames);
      List<AnnotatedPhrase> annotatorTwoAnnotations = xmlImporter.importAnnotations(
          xmlFilePathAnnotatorTwo, textFile.getAbsolutePath(), annotatorTwoConcepts);
      annotatorTwoAnnotations =
          mutator.filterForAnnotators(annotatorTwoAnnotations, annotatorNames);

      try {
        mergedAnnotations =
            mutator.mergeAnnotationLists(annotatorOneAnnotations, annotatorTwoAnnotations);
      } catch (final Exception e) {
        GeneralHelper.errorWriter(e.getLocalizedMessage());
        e.printStackTrace();
      }
      if (conceptMap != null && !conceptMap.isEmpty() && mergedAnnotations != null
          && !mergedAnnotations.isEmpty()) {
        mergedAnnotations = remapConceptNames(mergedAnnotations, conceptMap);
      }
      exportAnnotationList(mergedAnnotations, textFile, xmlExporter);
    }
  }

  protected void trainAndPredict(final ArffCreationResults arffCreationResults,
      final String textCorpusUrl, final String inputXmlUrl, final String outputXmlUrl) {
    // Results from previous step
    final PostArffFileResults postArffWriteResults = arffCreationResults.getPostArffWriteResults();

    // Parameter creation
    final Attribute attributeToFoldOn = postArffWriteResults.getAttributeToFoldOn();
    final Set<Attribute> attributesToIgnore = postArffWriteResults.getAttributeSet();
    final String arffFilePath = arffCreationResults.getArffFilePath();
    final String independantVariableAttributeName = "class";
    final EvaluationMetricType evalMetric = EvaluationMetricType.EVALUATION_MATTHEWS_CC;
    // final Set<BaseClassifierHyperparameterConfiguration>
    // classifiersToTrain = defineTrainingClassifiers(numberFeatures);

    final FnDefineClassifiersToReview fnDefineClassifiersToReview = (instances) -> {
      final Set<BaseClassifierHyperparameterConfiguration> parameters = new HashSet<>();
      final int maxNumberAttributes = instances.numAttributes() / 7;
      parameters.add(new RandomForestHyperparameterConfiguration(new MaxDepth(24, 25),
          new NumberFeatures(10, maxNumberAttributes - 1, maxNumberAttributes, 1)));
      return parameters;
    };

    final TrainingParameter trainingParameter =
        new TrainingParameter(attributeToFoldOn, attributesToIgnore, arffFilePath,
            independantVariableAttributeName, evalMetric, fnDefineClassifiersToReview);

    // Parameter creation
    final ProcessModule training =
        new ArffTrainingPersistedClassifier(trainingParameter);

    try {
      // Process
      final TrainingResults trainingResults = (TrainingResults) training.process();

      // Results from previous step
      final ErrorAnalysisParameter errorAnalysisParameter = new ErrorAnalysisParameter(
          trainingResults.getClassifierDetails(), trainingResults.getArffFilePath());

      // Parameter creation
      final ProcessModule errorAnalysis =
          new ErrorAnalysisAndArffUpdateWithPredictions(errorAnalysisParameter);

      // Process
      final ErrorAnalysisResults errorAnalysisResults =
          (ErrorAnalysisResults) errorAnalysis.process();

      // Parameter creation
      final ArffToEHostParameter arffToEHostParameter = new ArffToEHostParameter(
          errorAnalysisResults.getArffFilePath(), textCorpusUrl, outputXmlUrl);

      final ProcessModule arffToEHost =
          new ArffPredictionsAndPhraseDataToEHost(arffToEHostParameter);

      final ArffToEHostResults arffToEHostResults = (ArffToEHostResults) arffToEHost.process();
    } catch (final FileNotFoundException e) {
      GeneralHelper.printException(e);
    } catch (final IOException e) {
      GeneralHelper.printException(e);
    }

  }

  private void adjudicateSaltLakeCsvOutput(final String pathToCsvFile) {
    final int NUMBER_OF_COLUMNS_EXPECTED = 10;

    final File csvFile = new File(pathToCsvFile);
    final CSVReader reader = new CSVReader(csvFile);

    // The first line should have column headings and no data
    if (!reader.isValid() || reader.getNextData() == null) {
      System.err.println("No valid data for file at:" + pathToCsvFile);
      System.exit(-1);
    }

    String[] csvData;

    while ((csvData = reader.getNextData()) != null) {
      if (csvData.length < NUMBER_OF_COLUMNS_EXPECTED) {
        continue;
      }

      processAbiData(csvData);
    }
  }

  /**
   * @param raptatAnnotations
   * @param textFile
   * @param xmlExporter
   */
  private void exportAnnotationList(final List<AnnotatedPhrase> annotationList, final File textFile,
      final XMLExporterRevised xmlExporter) {
    final RaptatDocument textDocument = new RaptatDocument();
    textDocument.setTextSourcePath(textFile.getAbsolutePath());
    final AnnotationGroup annotationGroup = new AnnotationGroup(textDocument, annotationList);
    xmlExporter.exportReferenceAnnotationGroup(annotationGroup, true, true);
  }

  /**
   * @param textPath
   * @param phraseClassToXmlMap
   * @param outputDirectory
   * @param textAnalyzer
   * @param proximityDistances
   * @param attributeFilters
   * @return
   */
  private ProcessModule initializeMetaFeatureBuilders(final String textPath,
      final Map<PhraseClass, Optional<String>> phraseClassToXmlMap,
      final Optional<String> outputDirectory, final TextAnalyzer textAnalyzer,
      final List<RaptatPair<Integer, Integer>> proximityDistances,
      final List<AttributeFilter> attributeFilters) {
    LOGGER.info("\n\tCREATING FEATURE BUILDERS\n");

    final FeatureBuilder[] featureBuilders = new FeatureBuilder[] {new IndexValueFeatureBuilder(),
        new LateralityFeatureBuilder(), new LateralityTokensDistanceFeatureBuilder(),
        new LateralityLinesDistanceFeatureBuilder(), new IndexTypeFeatureBuilder(),
        new IndexTypeTokensDistanceFeatureBuilder(), new IndexTypeLinesDistanceFeatureBuilder(),
        new AnatomyFeatureBuilder(), new AnatomyLinesDistanceFeatureBuilder(),
        new AnatomyTokensDistanceFeatureBuilder(), new PressureFeatureBuilder(),
        new PressureLinesDistanceFeatureBuilder(), new PressureTokensDistanceFeatureBuilder(),
        new ArteryFeatureBuilder(), new ArteryLinesDistanceFeatureBuilder(),
        new ArteryTokensDistanceFeatureBuilder(), new ExerciseFeatureBuilder(),
        new ExerciseLinesDistanceFeatureBuilder(), new ExerciseTokensDistanceFeatureBuilder(),
        new ImpressionFeatureBuilder(), new ImpressionLinesDistanceFeatureBuilder(),
        new ImpressionTokensDistanceFeatureBuilder(), new InterpretationFeatureBuilder(),
        new InterpretationLinesDistanceFeatureBuilder(),
        new InterpretationTokensDistanceFeatureBuilder(), new NegativeIndexInfoFeatureBuilder(),
        new NegativeIndexInfoLinesDistanceFeatureBuilder(),
        new NegativeIndexInfoTokensDistanceFeatureBuilder(), new NonCompressibleFeatureBuilder(),
        new NonCompressibleLinesDistanceFeatureBuilder(),
        new NonCompressibleTokensDistanceFeatureBuilder(), new RangeFeatureBuilder(),
        new RangeLinesDistanceFeatureBuilder(), new RangeTokensDistanceFeatureBuilder(),
        new TemporalFeatureBuilder(), new TemporalLinesDistanceFeatureBuilder(),
        new TemporalTokensDistanceFeatureBuilder()};

    LOGGER.info("\n\tGETTING METAFEATURE BUILDERS\n");
    final MetaFeatureBuilder[] metaFeatureBuilders =
        new MetaFeatureBuilder[] {new ProximityDescriptorMetaFeatureBuilder(proximityDistances),
            new ClosestTagFeatureBuilder()};

    LOGGER.info("\n\tCREATING METAFEATUREBUILDER\n");
    ArffCreationParameterExtended arffParameter = new ArffCreationParameterExtended(textPath,
        phraseClassToXmlMap, outputDirectory, featureBuilders, "Girotra ABI Project",
        "Features and Metafeatures found in association with IndexValues Remapped to LateralityIndexValues",
        textAnalyzer);
    final MetaFeatureBuilderAndArffCreator processingModule =
        new MetaFeatureBuilderAndArffCreator(arffParameter, metaFeatureBuilders, attributeFilters);
    processingModule.setLogTokenPhrases(true);

    return processingModule;
  }

  private void processAbiData(final String[] csvData) {
    final int DOC_NAME_INDEX = 0;
    final int START_OFFSET_INDEX = 1;
    final int END_OFFSET_INDEX = 2;
    final int PHRASE_TEXT_INDEX = 4;
    final int ABI_VALUE_INDEX = 5;
    final int INDEX_TYPE_INDEX = 6;
    final int LATERALITY_INDEX = 7;
  }

  private List<AnnotatedPhrase> remapConceptNames(final List<AnnotatedPhrase> annotatedPhrases,
      final Map<String, String> conceptMap) {

    if (annotatedPhrases == null) {
      return null;
    }

    List<AnnotatedPhrase> resultPhrases = new ArrayList<>();

    for (AnnotatedPhrase annotatedPhrase : annotatedPhrases) {
      AnnotatedPhrase copiedPhrase = new AnnotatedPhrase(annotatedPhrase);
      String conceptName = copiedPhrase.getConceptName();
      String mappedName = conceptMap.get(conceptName);
      if (mappedName != null) {
        copiedPhrase.setConceptName(mappedName);
      }

      resultPhrases.add(copiedPhrase);
    }
    return resultPhrases;
  }



  /**
   * @param args
   */
  public static void main(final String[] args) {

    // UserPreferences.INSTANCE.initializeLVGLocation();
    // UserPreferences.INSTANCE.setLvgPath();
    final String lvgPath = System.getenv("LVG_DIR");
    UserPreferences.INSTANCE.setLvgPath(lvgPath);

    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        final RunType runType = RunType.TRAIN_AND_TEST;
        final GirotraABI girotraAbiTool = new GirotraABI();

        switch (runType) {
          case ADJUDICATE_SLC_CSV_OUTPUT: {
            final String pathToCsvFile =
                "P:\\ORD_Girotra_201607120D\\Glenn\\GlennInstallOfABIToolEvaluation\\csvOutputOfTool\\ABI_output.csv";
            girotraAbiTool.adjudicateSaltLakeCsvOutput(pathToCsvFile);
          }
            break;


          case CORRECT_SALT_LAKE_ABI_TOOL_OFFSETS: {
            // final String textFilesPath =
            // "P:\\ORD_Girotra_201607120D\\Glenn\\ABIAnnotations_181212\\ABIValueAnnotationWorkspace\\ABIAnnotations_200Docs_181212\\corpus";
            // final String xmlFilesPath =
            // "P:\\ORD_Girotra_201607120D\\Glenn\\ABIAnnotations_181212\\ABIValueAnnotationWorkspace\\ABIAnnotations_200Docs_181212\\saved";
            // final String schemaFilePath =
            // "P:\\ORD_Girotra_201607120D\\Glenn\\ABIAnnotations_181212\\ABIValueAnnotationWorkspace\\ABIAnnotations_200Docs_181212\\config\\projectschema.xml";
            // final String outputDirectoryPath =
            // "P:\\ORD_Girotra_201607120D\\Glenn\\ABIAnnotations_181212\\ABIValueAnnotationWorkspace\\ABIAnnotations_200Docs_181212\\offsetsCorrected";
            final String textFilesPath =
                "P:\\ORD_Girotra_201607120D\\Glenn\\ManuscriptData_200704\\SLCToolEvaluation\\eHOSTWorkspaceTemp\\SLCOutputPromotedAttributes\\corpus";
            final String xmlFilesPath =
                "P:\\ORD_Girotra_201607120D\\Glenn\\ManuscriptData_200704\\SLCToolEvaluation\\eHOSTWorkspaceTemp\\SLCOutputPromotedAttributes\\saved";
            final String schemaFilePath =
                "P:\\ORD_Girotra_201607120D\\Glenn\\ManuscriptData_200704\\SLCToolEvaluation\\eHOSTWorkspaceTemp\\SLCOutputPromotedAttributes\\config\\projectschema.xml";
            final String outputDirectoryPath =
                "P:\\ORD_Girotra_201607120D\\Glenn\\ManuscriptData_200704\\SLCToolEvaluation\\SCLOutputPromotedAttributesOffsetCorrected";
            girotraAbiTool.correctSaltLakeAbiToolOffsets(textFilesPath, xmlFilesPath,
                outputDirectoryPath, schemaFilePath);
          }
            break;

          case CREATE_TOKEN_PHRASE_LABEL_XML: {

            // final String textFolderPath =
            // "P:\\ORD_Girotra_201607120D\\Glenn\\TrainingFilesForABITool_200128\\Groups_01_04\\corpus";
            // final String xmlOutputFolderPath =
            // "P:\\ORD_Girotra_201607120D\\Glenn\\TrainingFilesForABITool_200128\\Groups_01_04\\ToolLabels_200213";
            // final String sentenceLogFolder =
            // "P:\\ORD_Girotra_201607120D\\Glenn\\TrainingFilesForABITool_200128\\Groups_01_04\\ToolLabelingLogs_200213";
            final String textFolderPath =
                "P:\\ORD_Miller_202005037D\\nashville\\Glenn\\HypoglycemiaFiles_201212";
            final String xmlOutputFolderPath =
                "P:\\ORD_Miller_202005037D\\nashville\\Glenn\\HypoglycemiaFiles_201212_XmlLabelOutput";
            final String sentenceLogFolder =
                "P:\\ORD_Miller_202005037D\\nashville\\Glenn\\HypoglycemiaFiles_SentenceLabelLogs";
            try {
              OptionsManager.getInstance().setRemoveStopWords(false);
              final TextAnalyzer ta = new TextAnalyzer();
              ta.setTokenProcessingOptions(OptionsManager.getInstance());
              // ta.createTokenPhraseMaker("ABILabelPatterns_v17_200212.txt");
              ta.createTokenPhraseMaker("HypoglycemiaLabelPatterns_201214_v01.txt");
              final TokenPhraseWriter tpw = new TokenPhraseWriter();
              tpw.writePhrases(textFolderPath, xmlOutputFolderPath, sentenceLogFolder, ta);
            } catch (final NotDirectoryException e) {
              GeneralHelper.errorWriter(
                  "Error during labeling and writing of token phrases\n" + e.getLocalizedMessage());
              e.printStackTrace();
            }
          }
            break;


          /*
           * Combine annotated concepts with a given name with the attributes of other annotations
           * that have a relationship with this concept. This is used, for example, to convert an
           * 'index_value' annotation that a laterality concept with a left attribute and an
           * indextype attribute with attribute abi into a single concept with a name like
           * 'indexvalue_left_abi,' so 3 concepts are merged into one.
           */
          case MERGE_ANNOTATION_SETS: {
            final String directoryPath =
                "P:\\ORD_Girotra_201607120D\\Glenn\\TrainingFilesForABITool_200128\\Group_06";
            final String textFilesPath = directoryPath + File.separator + "corpus";
            final String xmlFilesPathAnnotatorOne = directoryPath + File.separator + "SaketXml";
            final String xmlFilesPathAnnotatorTwo = directoryPath + File.separator + "JuliaXml";
            final String outputDirectoryPath = directoryPath + File.separator + "MergedXml";

            final Set<String> annotators =
                new HashSet<>(Arrays.asList(new String[] {"julia", "saket", "raptat"}));

            Map<String, String> conceptMap = new HashMap<>();
            conceptMap.put("01_Left_ABI", "index_value_abi_left");
            conceptMap.put("02_Right_ABI", "index_value_abi_right");
            conceptMap.put("03_Left_TBI", "index_value_tbi_left");
            conceptMap.put("04_Right_TBI", "index_value_tbi_right");
            conceptMap.put("05_LeftAndRight_ABI", "index_value_abi_bilateral");
            conceptMap.put("06_LeftAndRight_TBI", "index_value_tbi_bilateral");
            conceptMap.put("07_UnspecifiedLaterality_ABI", "index_value_abi_null");
            conceptMap.put("08_UnspecifiedLaterality_TBI", "index_value_tbi_null");
            conceptMap.put("09_Left_IndexValue", "index_value_null_left");
            conceptMap.put("10_Right_IndexValue", "index_value_null_right");
            conceptMap.put("11_UnspecifiedLaterality_IndexValue", "index_value_null_null");
            /*
             * Convert map to be entirely lower case
             */
            Map<String, String> lowerCaseMap = new HashMap<>();
            for (String mapKey : conceptMap.keySet()) {
              String value = conceptMap.get(mapKey);
              lowerCaseMap.put(mapKey.toLowerCase(), value.toLowerCase());
            }
            conceptMap = lowerCaseMap;

            final Set<String> annotatorOneConcepts = conceptMap.keySet();
            final Set<String> annotatorTwoConcepts = annotatorOneConcepts;

            girotraAbiTool.mergeAnnotationSets(textFilesPath, xmlFilesPathAnnotatorOne,
                xmlFilesPathAnnotatorTwo, annotatorOneConcepts, annotatorTwoConcepts, annotators,
                conceptMap, outputDirectoryPath);
            System.out.println("Ouput saved to:" + outputDirectoryPath);
          }
            break;

          /*
           *
           */
          case MERGE_RELATED_ANNOTATIONS: {
            final String pathToDataDirectory =
                "P:\\ORD_Girotra_201607120D\\Glenn\\TrainingFilesForABITool_200128\\Group06";
            final String pathToXml = pathToDataDirectory + File.separator + "originalXml";
            final String pathToText = pathToDataDirectory + File.separator + "corpus";
            final String pathToResultFolder = pathToDataDirectory + File.separator + "convertedXml";

            /*
             * The typeNameMap indicates what annotations to import and how annotations will be
             * converted. Annotations with a concept name mapped from TARGET_NAME will be combined
             * with related annotations with concept names mapped from FIRST_CONCEPT_NAME and
             * SECOND_CONCEPT_NAME. This provides a way to know which concepts will be combined and
             * how.
             */
            final Map<ConceptTypeName, String> typeNameMap = new HashMap<>();
            typeNameMap.put(ConceptTypeName.TARGET_NAME, "index_value");
            typeNameMap.put(ConceptTypeName.FIRST_RELATED_CONCEPT_NAME, "index_type");
            typeNameMap.put(ConceptTypeName.FIRST_CONCEPT_TARGET_RELATION_NAME,
                "indextypeofindexvalue");
            typeNameMap.put(ConceptTypeName.FIRST_CONCEPT_TARGET_ATTRIBUTE_NAME, "abiortbi");

            typeNameMap.put(ConceptTypeName.SECOND_RELATED_CONCEPT_NAME, "laterality");
            typeNameMap.put(ConceptTypeName.SECOND_CONCEPT_TARGET_RELATION_NAME,
                "lateralityrelationship");
            typeNameMap.put(ConceptTypeName.SECOND_CONCEPT_TARGET_ATTRIBUTE_NAME, "leftorright");

            combineRelatedAnnotations(pathToText, pathToXml, pathToResultFolder, typeNameMap);
          }
            break;

          case PROMOTE_RELATIONS_TO_CONCEPTS: {
            try {
              final String startDirectory =
                  "P:\\ORD_Girotra_201607120D\\Glenn\\ManuscriptData_200704\\IAAData";
              final String textDirectoryPath =
                  startDirectory + File.separator + "Groups7and8Corpus_200704";
              final String inputXmlPath =
                  startDirectory + File.separator + "JuliaXmlGroups7and8_200704";
              final String outputFolderName = "ConceptPromotedRelationships";
              AnnotationMutator annotationMutator = new AnnotationMutator();
              final String outputDirectoryPath = startDirectory + File.separator + outputFolderName
                  + "_" + GeneralHelper.getTimeStamp();
              FileUtils.forceMkdir(new File(outputDirectoryPath));

              String targetConcept = "index_value";
              Map<String, String> relationshipConceptToAttributeMap = new HashMap<>();
              relationshipConceptToAttributeMap.put("laterality", "leftorright");
              relationshipConceptToAttributeMap.put("index_type", "abiortbi");

              promoteRelationsToConcepts(textDirectoryPath, inputXmlPath, outputDirectoryPath,
                  targetConcept, relationshipConceptToAttributeMap, annotationMutator);

            } catch (IOException e) {
              System.err.println(
                  "Unable to locate or create files for running " + e.getLocalizedMessage());
              e.printStackTrace();
              System.exit(-1);
            }

          }
            break;

          /*
           * Takes a given set of concept names for annotations in an eHOST xml file and remaps them
           * to another name.
           */
          case REMAP_CONCEPT_NAMES: {
            try {
              final String startDirectory =
                  "P:\\ORD_Girotra_201607120D\\Glenn\\EvaluationFilesForABITool_200513";
              final String textPath = startDirectory + File.separator + "corpus";
              final String inputXmlPath =
                  startDirectory + File.separator + "xmlAdjudicatedUpdate_200518";
              Map<String, String> remap = new HashMap<>();

              // remap.put("Index_Value_ABI_Left", "abi");
              // remap.put("Index_Value_ABI_Right", "abi");
              // remap.put("Index_Value_TBI_Left", "tbi");
              // remap.put("Index_Value_TBI_Right", "tbi");
              // remap.put("Index_Value_ABI_Null", "abi");
              // remap.put("Index_Value_TBI_Null", "tbi");


              remap.put("Index_Value_ABI_Left", "left");
              remap.put("Index_Value_ABI_Right", "right");
              remap.put("Index_Value_TBI_Left", "left");
              remap.put("Index_Value_TBI_Right", "right");
              remap.put("Index_Value_Null_Left", "left");
              remap.put("Index_Value_Null_Right", "right");

              /*
               * Convert map to be entirely lower case
               */
              Map<String, String> lowerCaseMap = new HashMap<>();
              for (String mapKey : remap.keySet()) {
                String value = remap.get(mapKey);
                lowerCaseMap.put(mapKey.toLowerCase(), value.toLowerCase());
              }
              remap = lowerCaseMap;

              OptionsManager.getInstance().setRemoveStopWords(false);
              final TextAnalyzer textAnalyzer = new TextAnalyzer();
              textAnalyzer.setTokenProcessingOptions(OptionsManager.getInstance());

              final String outputFolderName = "XmlAdjudUpdatedRemap_Laterality";
              final String outputDirectoryPath = startDirectory + File.separator + outputFolderName
                  + "_" + GeneralHelper.getTimeStamp();
              FileUtils.forceMkdir(new File(outputDirectoryPath));
              remapConceptNames(textPath, inputXmlPath, outputDirectoryPath, remap);

            } catch (final IOException e) {
              System.err.println(
                  "Unable to locate or create files for running " + e.getLocalizedMessage());
              e.printStackTrace();
              System.exit(-1);
            }
          }
            break;
          /*
           * Run a model created through the TRAIN_AND_TEST case of this switch statement. Note that
           * as of 3/14/21, this case of the above switch statement is NOT WORKING and has therefore
           * been commented out.
           */
          case RUN_MODEL: {
            // try {
            // final String modelDirectory =
            // "P:\\ORD_Girotra_201607120D\\Glenn\\TrainingFilesForABITool_200128\\Groups_01_04";
            // String modelName = "TrainedIndexTypeAndLateralityModel_200519_174201.mdl";
            // File modelFile = new File(modelDirectory + File.separator + modelName);
            // System.out.println("RUNNING MODEL:" + modelFile.getAbsolutePath());
            //
            // Set<Integer> identifierAttributeIndices = new HashSet<>(Arrays.asList(1, 2, 3, 4));
            // // String outputFileName =
            // // "Group_01_04_Train_01_04_Test" + GeneralHelper.getTimeStamp() + ".txt";
            // String outputFileName = "Documents_Radiology_ImpressionText_Results_"
            // + GeneralHelper.getTimeStamp() + ".txt";
            // // String evaluationDataDirectory =
            // // "P:\\ORD_Girotra_201607120D\\Glenn\\TrainingFilesForABITool_200128\\Groups_01_04";
            //
            //
            // /*
            // * evaluationDataDirectory should contain absolute path to directory containing the
            // * corpus of notes for evaluation
            // */
            // String evaluationDataDirectory =
            // "P:\\ORD_Girotra_201607120D\\Glenn\\AdditionalChartsForABIExtraction_210213\\Documents_Radiology_ImpressionText";
            // String outputFilePath =
            // new File(evaluationDataDirectory).getParent() + File.separator + outputFileName;
            // String tokenPhraseMakerFileName = "ABILabelPatterns_v18_200513.txt";
            //
            // /*
            // * lateralityDirectoryTrain, indexTypeDirectoryTrain, and arffOutputPath are only used
            // * for training so we set them to empty
            // */
            // Optional<String> lateralityDirectoryTrain = Optional.empty();
            // Optional<String> indexTypeDirectoryTrain = Optional.empty();
            // Optional<String> arffOutputPath = Optional.empty();
            //
            // System.out.println(
            // "GETTING CONCEPT TO INSTANCES MAP FROM CORPUS AT:" + evaluationDataDirectory);
            //
            // boolean filterForTraining = false;
            // final Map<String, Map<PhraseClass, Instances>> mapToInstancesForEvaluation =
            // getConceptAndPhraseClassToInstancesMap(girotraAbiTool, evaluationDataDirectory,
            // lateralityDirectoryTrain, indexTypeDirectoryTrain, tokenPhraseMakerFileName,
            // arffOutputPath, filterForTraining);
            //
            // ObjectInputStream ois =
            // new ObjectInputStream(new BufferedInputStream(new FileInputStream(modelFile)));
            //
            // System.out.println("READING IN SERIALIZED MODEL");
            // @SuppressWarnings("unchecked")
            // final Map<String, Map<PhraseClass, RaptatPair<Classifier, Instances>>>
            // mapToClassifierInstanceTrainingPairs =
            // (Map<String, Map<PhraseClass, RaptatPair<Classifier, Instances>>>) ois
            // .readObject();
            //
            // System.out.println("MODEL READ FINISHED");
            // Map<String, Map<PhraseClass, Instances>> classifiedInstances =
            // classifyMappedInstances(mapToInstancesForEvaluation,
            // mapToClassifierInstanceTrainingPairs);
            //
            // printInstanceClassifications(classifiedInstances, identifierAttributeIndices,
            // outputFilePath);
            //
            // System.out.println("Instance classifications saved to:\n\t" + outputFilePath);
            //
            //
            // } catch (Exception e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }


          }
            break;

          /*
           * This case will take all the xml files in a directory (specified explicitly in the code
           * below) and the corpus of corresponding documents (also specified below) and run
           * cross-validation
           */
          case RUN_WEKA_CROSS_VALIDATION: {
            try {
              final String dataDirectory =
                  "P:\\ORD_Girotra_201607120D\\Glenn\\TrainingFilesForABITool_200128\\Groups_01_06";
              final String textPath = dataDirectory + File.separator + "corpus";
              final Optional<String> outputDirectory =
                  Optional.of(dataDirectory + File.separator + "CrossValidationOutput");
              FileUtils.forceMkdir(new File(outputDirectory.get()));

              final Map<String, Map<PhraseClass, Instances>> conceptToPhraseClassInstancesMap =
                  buildWekaInstances(girotraAbiTool, dataDirectory, textPath, outputDirectory);

              CrossValidator crossValidator = new CrossValidator();

              String identifierAttributes = "1-4";
              List<String> filteredClassifierOptions =
                  OptionsBuilder.generateFilteredClassifierOptions(identifierAttributes);

              List<String> crossValidationOptions = new ArrayList<>();
              OptionsBuilder.addCrossValidationOptions(crossValidationOptions);
              OptionsBuilder.addPredictionOptions(crossValidationOptions, identifierAttributes);

              Map<String, Map<String, Map<String, Triple<String, String, Boolean>>>> resultMap =
                  crossValidator.runCVClassificationByPhraseClass(conceptToPhraseClassInstancesMap,
                      crossValidationOptions, filteredClassifierOptions);

              Map<String, Map<String, List<String>>> matchedResults =
                  AbiHelper.matchPhraseClassesByConcept(resultMap);
              AbiHelper.printMatchedResults(matchedResults, outputDirectory.get());

            } catch (final IOException e) {
              System.err.println(
                  "Unable to locate or create files for running " + e.getLocalizedMessage());
              e.printStackTrace();
              System.exit(-1);
            } catch (NumberFormatException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            } catch (Exception e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
          }
            break;

          case TRAIN_MODEL: {
            try {
              String dataDirectoryTrain =
                  "P:\\ORD_Girotra_201607120D\\Glenn\\TrainingFilesForABITool_200128\\Groups_01_04";
              Optional<String> lateralityDirectoryTrain =
                  Optional.of("XmlLateralityExerciseValuesExcluded");
              Optional<String> indexTypeDirectoryTrain =
                  Optional.of("XmlIndexTypeExerciseValuesExcluded");
              String tokenPhraseMakerFileName = "ABILabelPatterns_v18_200513.txt";
              Optional<String> arffOutputPath = Optional.empty();
              boolean filterForTraining = true;
              final Map<String, Map<PhraseClass, Instances>> mapToInstancesForTraining =
                  getConceptAndPhraseClassToInstancesMap(girotraAbiTool, dataDirectoryTrain,
                      lateralityDirectoryTrain, indexTypeDirectoryTrain, tokenPhraseMakerFileName,
                      arffOutputPath, filterForTraining);
              /*
               * The first 4 attributes in each instance are only used for identification, not
               * training, so we note them here
               */
              String identifierAttributes = "1-4";
              final Map<String, Map<PhraseClass, Classifier>> mapToClassifiers =
                  getConceptAndPhraseClassToClassifiersMap(mapToInstancesForTraining,
                      identifierAttributes);
              Map<String, Map<PhraseClass, RaptatPair<Classifier, Instances>>> mapToClassifierInstancePair =
                  pairClassifierWithInstances(mapToClassifiers, mapToInstancesForTraining);

              String modelName =
                  "TrainedIndexTypeAndLateralityModel_" + GeneralHelper.getTimeStamp() + ".mdl";
              String modelPath = dataDirectoryTrain + File.separator + modelName;
              ObjectOutputStream oos = new ObjectOutputStream(
                  new BufferedOutputStream(new FileOutputStream(new File(modelPath))));
              oos.writeObject(mapToClassifierInstancePair);
              oos.flush();
              oos.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
            break;

          /*
           * Note of 7/18/20 - the TRAIN_AND_TEST case and the
           * TRAIN_AND_TEST_INPUT_MAPPED_CLASSIFIER appear to be identical. This may be due to
           * updating the code and failing to remove the redundant
           * TRAIN_AND_TEST_INPUT_MAPPED_CLASSIFIER case.
           */
          case TRAIN_AND_TEST: {
            try {
              String dataDirectoryTrain =
                  "P:\\ORD_Girotra_201607120D\\Glenn\\TrainingFilesForABITool_200128\\Groups_01_06";
              Optional<String> lateralityDirectoryTrain =
                  Optional.of("XmlLateralityExerciseValuesExcluded");
              Optional<String> indexTypeDirectoryTrain =
                  Optional.of("XmlIndexTypeExerciseValuesExcluded");
              String tokenPhraseMakerFileName = "ABILabelPatterns_v18_200513.txt";

              System.out.println("GENERATING TRAINING INSTANCES");

              Optional<String> arffOutputPath = Optional.empty();
              boolean filterForTraining = true;
              final Map<String, Map<PhraseClass, Instances>> mapToInstancesForTraining =
                  getConceptAndPhraseClassToInstancesMap(girotraAbiTool, dataDirectoryTrain,
                      lateralityDirectoryTrain, indexTypeDirectoryTrain, tokenPhraseMakerFileName,
                      arffOutputPath, filterForTraining);

              System.out.println("GENERATING TESTING INSTANCES");
              /*
               * Changed from line immediately below to one after it to do additional charts for
               * Saket & Carrie - 3/13/21
               */
              // String dataDirectoryTest = "P:\\ORD_Girotra_201607120D\\Glenn\\ChartReviewFiles";
              String dataDirectoryTest =
                  "P:\\ORD_Girotra_201607120D\\Glenn\\ChartReviewFiles\\ChartReviewFromCarrie_210202";
              Optional<String> lateralityDirectoryTest = Optional.of("xml");
              Optional<String> indexTypeDirectoryTest = Optional.of("xml");
              final String outputDirectory = dataDirectoryTest + File.separator + "outputForWeka";
              FileUtils.forceMkdir(new File(outputDirectory));

              /*
               * We are not creating an arff file, so we set the path to null to signify not to
               * write to arff
               */
              filterForTraining = false;
              final Map<String, Map<PhraseClass, Instances>> mapToInstancesForTesting =
                  getConceptAndPhraseClassToInstancesMap(girotraAbiTool, dataDirectoryTest,
                      lateralityDirectoryTest, indexTypeDirectoryTest, tokenPhraseMakerFileName,
                      arffOutputPath, filterForTraining);

              final Map<String, Map<PhraseClass, RaptatPair<Instances, Instances>>> mapToPairedInstances =
                  pairTrainTestInstances(mapToInstancesForTraining, mapToInstancesForTesting);

              String predictionTaggingAttributes = "1-4";
              final Map<String, Map<String, Map<String, Triple<String, String, Boolean>>>> actualVsPredicted =
                  getActualVsPredicted(mapToPairedInstances, predictionTaggingAttributes);

              /*
               * Create other class to handled what CrossValidator is doing here
               */
              Map<String, Map<String, List<String>>> matchedResults =
                  AbiHelper.matchPhraseClassesByConcept(actualVsPredicted);
              AbiHelper.printMatchedResults(matchedResults, outputDirectory);


            } catch (FileNotFoundException e) {
              System.err.println(
                  "Unable to locate or create files for running " + e.getLocalizedMessage());
              e.printStackTrace();
              System.exit(-1);
              e.printStackTrace();
            } catch (IOException e) {
              System.err
                  .println("Unable to read or write files for running " + e.getLocalizedMessage());
              e.printStackTrace();
              System.exit(-1);
            }
          }
            break;

          case TRAIN_AND_TEST_INPUT_MAPPED_CLASSIFIER: {
            try {
              String dataDirectoryTrain =
                  "P:\\ORD_Girotra_201607120D\\Glenn\\TrainingFilesForABITool_200128\\Groups_01_04";
              Optional<String> lateralityDirectoryTrain =
                  Optional.of("XmlLateralityExerciseValuesExcluded");
              Optional<String> indexTypeDirectoryTrain =
                  Optional.of("XmlIndexTypeExerciseValuesExcluded");
              String tokenPhraseMakerFileName = "ABILabelPatterns_v18_200513.txt";

              System.out.println("GENERATING TRAINING INSTANCES");

              Optional<String> arffOutputPath = Optional.empty();
              boolean filterForTraining = true;
              final Map<String, Map<PhraseClass, Instances>> mapToInstancesForTraining =
                  getConceptAndPhraseClassToInstancesMap(girotraAbiTool, dataDirectoryTrain,
                      lateralityDirectoryTrain, indexTypeDirectoryTrain, tokenPhraseMakerFileName,
                      arffOutputPath, filterForTraining);

              System.out.println("GENERATING TESTING INSTANCES");

              String dataDirectoryTest =
                  "P:\\ORD_Girotra_201607120D\\Glenn\\TrainingFilesForABITool_200128\\Groups_05_06";
              Optional<String> lateralityDirectoryTest =
                  Optional.of("XmLateralityExerciseValuesExcluded");
              Optional<String> indexTypeDirectoryTest =
                  Optional.of("XmlIndexTypeExerciseValuesExcluded");
              final String outputDirectory = dataDirectoryTest + File.separator + "outputForWeka";
              FileUtils.forceMkdir(new File(outputDirectory));

              /*
               * We are not creating an arff file, so we set the path to null to signify not to
               * write to arff
               */
              filterForTraining = false;
              final Map<String, Map<PhraseClass, Instances>> mapToInstancesForTesting =
                  getConceptAndPhraseClassToInstancesMap(girotraAbiTool, dataDirectoryTest,
                      lateralityDirectoryTest, indexTypeDirectoryTest, tokenPhraseMakerFileName,
                      arffOutputPath, filterForTraining);

              final Map<String, Map<PhraseClass, RaptatPair<Instances, Instances>>> mapToPairedInstances =
                  pairTrainTestInstances(mapToInstancesForTraining, mapToInstancesForTesting);

              String predictionTaggingAttributes = "1-4";
              final Map<String, Map<String, Map<String, Triple<String, String, Boolean>>>> actualVsPredicted =
                  getActualVsPredictedInputMapping(mapToPairedInstances,
                      predictionTaggingAttributes);

              /*
               * Create other class to handled what CrossValidator is doing here
               */
              Map<String, Map<String, List<String>>> matchedResults =
                  AbiHelper.matchPhraseClassesByConcept(actualVsPredicted);
              AbiHelper.printMatchedResults(matchedResults, outputDirectory);


            } catch (FileNotFoundException e) {
              System.err.println(
                  "Unable to locate or create files for running " + e.getLocalizedMessage());
              e.printStackTrace();
              System.exit(-1);
              e.printStackTrace();
            } catch (IOException e) {
              System.err
                  .println("Unable to read or write files for running " + e.getLocalizedMessage());
              e.printStackTrace();
              System.exit(-1);
            }
          }
            break;
          case XML_TO_WEKA_INSTANCES: {
            try {

              final String dataDirectory =
                  "P:\\ORD_Girotra_201607120D\\Glenn\\TrainingFilesForABITool_200128\\Groups_01_02";
              Optional<String> lateralityDirectoryName =
                  Optional.of("XmlRemap_Laterality_200214_130748");
              Optional<String> indexTypeDirectoryName =
                  Optional.of("XmlRemap_IndexType_200213_173355");

              String tokenPhraseMakerFileName = "ABILabelPatterns_v17_200212.txt";

              Optional<String> outputDirectory =
                  Optional.of(dataDirectory + File.separator + "outputForWeka");
              FileUtils.forceMkdir(new File(outputDirectory.get()));

              boolean filterForTraining = false;
              final Map<String, Map<PhraseClass, Instances>> conceptToPhraseClassInstancesMap =
                  getConceptAndPhraseClassToInstancesMap(girotraAbiTool, dataDirectory,
                      lateralityDirectoryName, indexTypeDirectoryName, tokenPhraseMakerFileName,
                      outputDirectory, filterForTraining);

              writeWekaInstances(outputDirectory.get(), conceptToPhraseClassInstancesMap);
            } catch (final IOException e) {
              System.err.println(
                  "Unable to locate or create files for running " + e.getLocalizedMessage());
              e.printStackTrace();
              System.exit(-1);
            }
          }
            break;

          default:
            break;
        }
        System.out.println();
        System.out.println("------------------------------");
        System.out.println("   Processing Complete");
        System.out.println("------------------------------");
        System.exit(0);
      }


      /**
       * This method builds a mapping of concept to a Weka instances for each PhraseClass specified.
       * Each PhraseClass object specified should have a corresponding set of xml files, and the
       * names of the classes of annotations in the file should be the same as the classifications
       * field of the phrase class.
       *
       * For PhraseClass objects with the same conceptName field, the predicted classes will be
       * combined to form a single class. For example, if index values like 0.78 are being
       * annotated, they might have one PhraseClass predicted classification of abi (meaning it's an
       * 'abi' type of index value) and another PhraseClass predicted classification of 'left'
       * (meaning the index value has a laterality of 'left'). These would be combined to form
       * 'left_abi' classification. The combined prediction would only be correct if both 'left' and
       * 'abi' were correct classifications.
       *
       * The method explicitly specifies in the code the name of a "TokenPhraseMaker" used to assign
       * labels to phrases identified in the code, which are used to both find a label of interest
       * like index value and identify features to determine the classification of any label of
       * interest. Also specified in the code are 1) any features that should be excluded if they
       * have a name containing a particular string, 2) the minimum proportion of instances with the
       * label of interest for inclusion, and the names of the directories containing the xml files
       * and the name of the PhraseClass enum that contains the same classifications as in the file.
       *
       * Generally, the only items that should be changed are the name of the xml directories. The
       * "TokenPhraseMaker," the excluded feature names, and the minimum proportion would stay the
       * same for identifying a particular concept, like index value.
       *
       * @param girotraAbiTool
       * @param xmlDataDirectory
       * @param textPath
       * @param optionalOutputDirectory
       * @return
       * @throws FileNotFoundException
       * @throws IOException
       */
      private Map<String, Map<PhraseClass, Instances>> buildWekaInstances(
          final GirotraABI girotraAbiTool, final String xmlDataDirectory, final String textPath,
          final Optional<String> optionalOutputDirectory)
          throws FileNotFoundException, IOException {
        OptionsManager.getInstance().setRemoveStopWords(false);
        final TextAnalyzer textAnalyzer = new TextAnalyzer();
        textAnalyzer.setTokenProcessingOptions(OptionsManager.getInstance());
        textAnalyzer.createTokenPhraseMaker(
            "PropertyFilesForCommandLine/ABIProject/LabelPatterns/ABILabelPatterns_v18_200513.txt");

        List<AttributeFilter> attributeFilters = new ArrayList<>();

        /* Features that contain a string in the excludedNames list will be removed */
        List<String> excludedNames = Arrays.asList("row", "doc", "col");
        attributeFilters.add(new AttributeNameFilter(excludedNames));

        /*
         * Features that occur in less than the minimumFrequency of all features will be removed
         */
        double minimumFrequency = 0.05;
        attributeFilters.add(new AttributeFrequencyFilter(minimumFrequency));

        /*
         * The same features will be used to map to different classes, and the classes will then be
         * merged on any test cases. So, for example, we will fit laterality and index_type, and
         * those referring to the same index value will be merged. If the index value 0.95 is
         * assigned an index type of ABI with a laterality of right, the final assignment of 0.95
         * will be 'indexValue_ABI_right.'
         */
        Map<PhraseClass, Optional<String>> phraseClassToXmlMap = new HashMap<>();

        /*
         * The laterality and index_type mappings are paths to where the xml for these phrase class
         * types are stored
         */
        Optional<String> lateralityXmlMapping =
            Optional.of(xmlDataDirectory + File.separator + "XmlLateralityExerciseValuesExcluded");
        phraseClassToXmlMap.put(PhraseClass.INDEX_VALUE_LATERALITY, lateralityXmlMapping);

        Optional<String> indexTypeXmlMapping =
            Optional.of(xmlDataDirectory + File.separator + "XmlIndexTypeExerciseValuesExcluded");
        phraseClassToXmlMap.put(PhraseClass.INDEX_VALUE_TYPE, indexTypeXmlMapping);

        final Map<String, Map<PhraseClass, Instances>> conceptToPhraseClassInstancesMap =
            girotraAbiTool.getPhraseClassInstances(textPath, phraseClassToXmlMap,
                optionalOutputDirectory, textAnalyzer, PROXIMITIES, attributeFilters);
        return conceptToPhraseClassInstancesMap;
      }


      private Map<String, Map<PhraseClass, Instances>> classifyMappedInstances(
          final Map<String, Map<PhraseClass, Instances>> mapToInstancesForEvaluation,
          final Map<String, Map<PhraseClass, RaptatPair<Classifier, Instances>>> mapToClassifierInstancePairs) {

        Map<String, Map<PhraseClass, Instances>> classifiedInstances = new HashMap<>();
        for (String conceptName : mapToClassifierInstancePairs.keySet()) {

          Map<PhraseClass, Instances> phraseToClassifiedInstances =
              classifiedInstances.computeIfAbsent(conceptName, key -> new HashMap<>());

          Map<PhraseClass, Instances> phraseClassMappingForEvaluation =
              mapToInstancesForEvaluation.get(conceptName);
          Map<PhraseClass, RaptatPair<Classifier, Instances>> phraseClassMapping =
              mapToClassifierInstancePairs.get(conceptName);
          for (PhraseClass phraseClass : phraseClassMapping.keySet()) {

            Instances evaluationInstances = phraseClassMappingForEvaluation.get(phraseClass);
            RaptatPair<Classifier, Instances> classifierTrainingInstancesPair =
                phraseClassMapping.get(phraseClass);
            Classifier classifier = classifierTrainingInstancesPair.left;
            Instances trainingInstances = classifierTrainingInstancesPair.right;

            System.out.println("Conforming instances for\n\tConcept:" + conceptName
                + "\n\tPhraseClass:" + phraseClass.toString());
            RaptatPair<Instances, Instances> trainEvaluationPair =
                conformTrainTestPair(new RaptatPair<>(trainingInstances, evaluationInstances));
            Instances evaluatedInstances = new Instances(trainEvaluationPair.right);
            phraseToClassifiedInstances.put(phraseClass, evaluatedInstances);
            System.out.println("Classifying instances");

            try {
              Enumeration<Instance> instanceEnumerator = evaluatedInstances.enumerateInstances();
              while (instanceEnumerator.hasMoreElements()) {
                Instance instance = instanceEnumerator.nextElement();
                double classification = classifier.classifyInstance(instance);
                instance.setClassValue(classification);
              }
            } catch (Exception e) {
              System.err.println("Unable to classify instance\n" + e.getLocalizedMessage());
              e.printStackTrace();
            }
          }

        }
        return classifiedInstances;
      }


      private void combineRelatedAnnotations(final String pathToText, final String pathToXml,
          final String pathToResultFolder, final Map<ConceptTypeName, String> typeNameMap) {

        Map<String, List<AnnotatedPhrase>> documentToAnnotationsMap =
            getDocumentToAnnotationMap(pathToText, pathToXml, new HashSet<>(typeNameMap.values()));
        Map<String, List<AnnotatedPhrase>> documentToRevisedAnnotationsMap =
            reviseCorpusAnnotations(documentToAnnotationsMap, typeNameMap);
        writeRevisedAnnotations(documentToRevisedAnnotationsMap, pathToResultFolder);

      }


      private RaptatPair<Instances, Instances> conformTrainTestPair(
          final RaptatPair<Instances, Instances> instancesPair) {
        Instances trainInstances = instancesPair.left;
        Instances testInstances = instancesPair.right;
        trainInstances.setRelationName("Train_" + trainInstances.relationName());
        testInstances.setRelationName("Test_" + testInstances.relationName());

        List<Instances> trainTestInstancesList =
            Arrays.asList(new Instances[] {trainInstances, testInstances});
        List<Instances> conformedInstances =
            InstancesMerger.conformInstances(trainTestInstancesList, "Conformed");


        Instances conformedTraining = null;
        Instances conformedTesting = null;
        for (Instances instances : conformedInstances) {
          if (instances.relationName().toLowerCase().startsWith("train")) {
            // conformedTraining = trainInstances;
            conformedTraining = instances;
          }
          if (instances.relationName().toLowerCase().startsWith("test")) {
            conformedTesting = instances;
          }
        }

        return new RaptatPair<>(conformedTraining, conformedTesting);
      }


      private Map<String, Map<String, Map<String, Triple<String, String, Boolean>>>> getActualVsPredicted(
          final Map<String, Map<PhraseClass, RaptatPair<Instances, Instances>>> mapToPairedInstances,
          final String predictionTaggingAttributes) {

        Map<String, Map<String, Map<String, Triple<String, String, Boolean>>>> resultMap =
            new HashMap<>();
        try {
          for (String conceptName : mapToPairedInstances.keySet()) {

            /*
             * Add mapping for result map if needed
             */
            Map<PhraseClass, RaptatPair<Instances, Instances>> phraseToTrainTestPairMap =
                mapToPairedInstances.get(conceptName);

            Map<String, Map<String, Triple<String, String, Boolean>>> phraseToAttributeMap =
                resultMap.computeIfAbsent(conceptName, k -> new HashMap<>());

            for (PhraseClass phraseClass : phraseToTrainTestPairMap.keySet()) {

              /*
               * Add mapping for conceptToPhraseClassResult if needed
               */
              Map<String, Triple<String, String, Boolean>> attributeToTripleMap =
                  phraseToAttributeMap.computeIfAbsent(phraseClass.toString(),
                      key -> new HashMap<>());

              RaptatPair<Instances, Instances> instancesPair =
                  phraseToTrainTestPairMap.get(phraseClass);
              RaptatPair<Instances, Instances> conformedInstancesPair =
                  conformTrainTestPair(instancesPair);
              Instances conformedTraining = conformedInstancesPair.left;
              Instances conformedTesting = conformedInstancesPair.right;

              /*
               * Set any attributes that are missing in the test set to zero as it means they are
               * absent which may be informative
               */
              AttributeHelper.zeroMissingValues(conformedTesting);

              String identifierAttributes = "1-4";
              List<String> filteredClassifierOptions =
                  OptionsBuilder.generateFilteredClassifierOptions(identifierAttributes);
              FilteredClassifier classifier = new FilteredClassifier();
              String[] optionsArray = filteredClassifierOptions.toArray(new String[] {});
              classifier.setOptions(optionsArray);
              System.out.println(
                  "Filtered Classifier Options: " + String.join(" ", classifier.getOptions()));
              classifier.buildClassifier(conformedTraining);

              Evaluation evaluation = new Evaluation(conformedTraining);

              // output collected predictions
              double[] predictions =
                  evaluation.evaluateModel(classifier, conformedTesting, new Object[0]);
              Enumeration<Instance> instanceEnumerator = conformedTesting.enumerateInstances();
              Attribute classAttribute = conformedTesting.classAttribute();

              for (int i = 0; i < predictions.length; i++) {

                Instance instance = instanceEnumerator.nextElement();
                String attributeValueHash = AttributeHelper.getAttributeIdentifierHash(instance);
                String actualClass = instance.toString(classAttribute);
                String predictedClass = classAttribute.value((int) predictions[i]);
                attributeToTripleMap.put(attributeValueHash, ImmutableTriple.of(actualClass,
                    predictedClass, actualClass.equalsIgnoreCase(predictedClass)));

                System.out.println("- attribute values:\n" + attributeValueHash);
                System.out.println("Actual:" + actualClass);
                System.out.println("Predicted:" + predictedClass);
              }
            }
          }
        } catch (Exception e) {
          System.err.println("Unable to run train and test instances\n" + e.getLocalizedMessage());
          e.printStackTrace();
          System.exit(-1);
        }
        return resultMap;
      }

      private Map<String, Map<String, Map<String, Triple<String, String, Boolean>>>> getActualVsPredictedInputMapping(
          final Map<String, Map<PhraseClass, RaptatPair<Instances, Instances>>> mapToPairedInstances,
          final String predictionTaggingAttributes) {

        Map<String, Map<String, Map<String, Triple<String, String, Boolean>>>> resultMap =
            new HashMap<>();
        try {
          for (String conceptName : mapToPairedInstances.keySet()) {

            /*
             * Add mapping for result map if needed
             */
            Map<PhraseClass, RaptatPair<Instances, Instances>> phraseToTrainTestPairMap =
                mapToPairedInstances.get(conceptName);

            Map<String, Map<String, Triple<String, String, Boolean>>> phraseToAttributeMap =
                resultMap.computeIfAbsent(conceptName, k -> new HashMap<>());

            for (PhraseClass phraseClass : phraseToTrainTestPairMap.keySet()) {

              /*
               * Add mapping for conceptToPhraseClassResult if needed
               */
              Map<String, Triple<String, String, Boolean>> attributeToTripleMap =
                  phraseToAttributeMap.computeIfAbsent(phraseClass.toString(),
                      key -> new HashMap<>());

              RaptatPair<Instances, Instances> instancesPair =
                  phraseToTrainTestPairMap.get(phraseClass);
              Instances conformedTraining = instancesPair.left;
              Instances conformedTesting = instancesPair.right;
              /*
               * Temporarily comment out to see in InputMappedClassifier can take care of
               * conformation instead of our own method
               */
              // RaptatPair<Instances, Instances> conformedInstancesPair =
              // conformTrainTestPair(instancesPair);
              // Instances conformedTraining = conformedInstancesPair.left;
              // Instances conformedTesting = conformedInstancesPair.right;

              /*
               * Set any attributes that are missing in the test set to zero as it means they are
               * absent which may be informative
               */
              // AttributeHelper.zeroMissingValues(conformedTesting);

              List<String> inputMappedOptions = OptionsBuilder.generateInputMappedOptions();
              String identifierAttributes = "1-4";
              OptionsBuilder.addFilteredClassifierOptions(inputMappedOptions, identifierAttributes);
              // FilteredClassifier classifier = new FilteredClassifier();
              // String[] optionsArray = inputMappedOptions.toArray(new String[] {});
              // classifier.setOptions(optionsArray);
              // System.out.println(
              // "InputMapped Classifier Options: " + String.join(" ", classifier.getOptions()));
              // classifier.buildClassifier(conformedTraining);

              /*
               * TEMPORARY
               */
              InputMappedClassifier classifier = new InputMappedClassifier();
              String[] optionsArray = inputMappedOptions.toArray(new String[] {});
              classifier.setOptions(optionsArray);
              System.out.println(
                  "InputMapped Classifier Options: " + String.join(" ", classifier.getOptions()));
              // imc.setModelHeader(conformedTraining);
              // imc.setTestStructure(conformedTesting);
              // imc.setClassifier(classifier);
              classifier.buildClassifier(conformedTraining);
              // ************ END TEMPORARY *****************//

              Evaluation evaluation = new Evaluation(conformedTraining);

              // output collected predictions
              double[] predictions =
                  evaluation.evaluateModel(classifier, conformedTesting, new Object[0]);
              Enumeration<Instance> instanceEnumerator = conformedTesting.enumerateInstances();
              Attribute classAttribute = conformedTesting.classAttribute();

              for (int i = 0; i < predictions.length; i++) {

                Instance instance = instanceEnumerator.nextElement();
                String attributeValueHash = AttributeHelper.getAttributeIdentifierHash(instance);
                String actualClass = instance.toString(classAttribute);
                String predictedClass = classAttribute.value((int) predictions[i]);
                attributeToTripleMap.put(attributeValueHash, ImmutableTriple.of(actualClass,
                    predictedClass, actualClass.equalsIgnoreCase(predictedClass)));

                System.out.println("- attribute values:\n" + attributeValueHash);
                System.out.println("Actual:" + actualClass);
                System.out.println("Predicted:" + predictedClass);
              }
            }
          }
        } catch (Exception e) {
          System.err.println("Unable to run train and test instances\n" + e.getLocalizedMessage());
          e.printStackTrace();
          System.exit(-1);
        }
        return resultMap;
      }


      /**
       * @return
       */
      private List<AttributeFilter> getAttributeFilters(final boolean filterForTraining) {
        List<AttributeFilter> attributeFilters = new ArrayList<>();

        /* Features that contain a string in the excludedNames list will be removed */
        List<String> excludedNames = Arrays.asList("distance");
        attributeFilters.add(new AttributeNameFilter(excludedNames));

        /*
         * If training, features that occur in less than the minimumFrequency of all features will
         * be removed
         */
        if (filterForTraining) {
          double minimumFrequency = 0.05;
          attributeFilters.add(new AttributeFrequencyFilter(minimumFrequency));
        }
        return attributeFilters;
      }

      private String getAttributeValue(final AnnotatedPhrase phrase, final String attributeName) {
        for (RaptatAttribute attribute : phrase.getPhraseAttributes()) {
          if (attributeName.equalsIgnoreCase(attribute.getName())) {
            return attribute.getValues().get(0);
          }
        }
        return null;
      }


      private String getAttributeValueHash(final Map<String, Object> attributeValues) {

        StringBuilder sb = new StringBuilder();
        attributeValues.forEach((k, v) -> sb.append(v).append("_"));
        return sb.substring(0, sb.length() - 1);
      }


      private String getClassifiedInstancesHeader(
          final Map<PhraseClass, Instances> phraseToInstancesMap,
          final Set<Integer> identifierAttributeIndices) {

        ArrayList<PhraseClass> phraseClasses = new ArrayList<>(phraseToInstancesMap.keySet());
        Collections.sort(phraseClasses);
        Instances instancesExample = phraseToInstancesMap.get(phraseClasses.get(0));

        ArrayList<Integer> identifierIndicesList = new ArrayList<>(identifierAttributeIndices);
        Collections.sort(identifierIndicesList);
        StringBuilder header = new StringBuilder();
        for (Integer attributeIndex : identifierIndicesList) {
          String attributeName = instancesExample.attribute(attributeIndex).name();
          header.append(attributeName).append("\t");
        }


        for (PhraseClass phraseClass : phraseClasses) {
          header.append(phraseClass.toString()).append("\t");
        }

        return header.toString().trim();
      }


      private Map<String, Map<PhraseClass, Classifier>> getConceptAndPhraseClassToClassifiersMap(
          final Map<String, Map<PhraseClass, Instances>> mapToInstancesForTraining,
          final String identifierAttributes) {

        Map<String, Map<PhraseClass, Classifier>> resultMap = new HashMap<>();
        try {
          for (String conceptName : mapToInstancesForTraining.keySet()) {
            Map<PhraseClass, Instances> phraseClassToInstancesMap =
                mapToInstancesForTraining.get(conceptName);
            for (PhraseClass phraseClass : phraseClassToInstancesMap.keySet()) {
              Instances instances = phraseClassToInstancesMap.get(phraseClass);
              List<String> filteredClassifierOptions =
                  OptionsBuilder.generateFilteredClassifierOptions(identifierAttributes);
              FilteredClassifier classifier = new FilteredClassifier();
              String[] optionsArray = filteredClassifierOptions.toArray(new String[] {});
              classifier.setOptions(optionsArray);
              System.out.println(
                  "Filtered Classifier Options: " + String.join(" ", classifier.getOptions()));
              classifier.buildClassifier(instances);

              Map<PhraseClass, Classifier> phraseToInstanceResult =
                  resultMap.computeIfAbsent(conceptName, key -> new HashMap<>());
              phraseToInstanceResult.put(phraseClass, classifier);
            }
          }
        } catch (Exception e) {
          System.err.println("Unable to create classifier map\n" + e.getLocalizedMessage());
          e.printStackTrace();
        }

        return resultMap;
      }


      /**
       * @param girotraAbiTool
       * @param dataDirectory
       * @param lateralityDirectoryTrain
       * @param indexTypeDirectoryTrain
       * @param tokenPhraseMakerFileName
       * @param arffOutputPath
       * @param filterForTraining - If set to true, then when instances are created for training,
       *        attributes occurring in less than a certain percentage of all the instance objects
       *        are removed to filter out low frequency attributes
       * @return
       * @throws FileNotFoundException
       * @throws IOException
       */
      private Map<String, Map<PhraseClass, Instances>> getConceptAndPhraseClassToInstancesMap(
          final GirotraABI girotraAbiTool, final String dataDirectory,
          final Optional<String> lateralityDirectoryTrain,
          final Optional<String> indexTypeDirectoryTrain, final String tokenPhraseMakerFileName,
          final Optional<String> arffOutputPath, final boolean filterForTraining)
          throws FileNotFoundException, IOException {
        final String textPath = dataDirectory + File.separator + "corpus";

        /*
         * The same features will be used to map to different classes, and the classes will then be
         * merged on any test cases. So, for example, we will fit laterality and index type, and
         * those referring to the same index value will be merged. If the index value 0.95 is
         * assigned an index type of ABI with a laterality of right, the final assignment of 0.95
         * will be 'indexValue_ABI_right.'
         *
         * Note that in the map, phraseClassToXmlMap, the PhraseClass object keys will map to an
         * empty optional unless we are training the system. The
         */
        Optional<String> lateralityXmlMapping = lateralityDirectoryTrain.isPresent()
            ? Optional.of(dataDirectory + File.separator + lateralityDirectoryTrain.get())
            : Optional.empty();
        Optional<String> indexTypeXmlMapping = indexTypeDirectoryTrain.isPresent()
            ? Optional.of(dataDirectory + File.separator + indexTypeDirectoryTrain.get())
            : Optional.empty();
        Map<PhraseClass, Optional<String>> phraseClassToXmlMap =
            getPhraseClassToXmlMap(lateralityXmlMapping, indexTypeXmlMapping);


        final TextAnalyzer textAnalyzer = getTextAnalyzer(tokenPhraseMakerFileName);
        List<AttributeFilter> attributeFilters = getAttributeFilters(filterForTraining);

        final Map<String, Map<PhraseClass, Instances>> conceptToPhraseClassInstancesMap =
            girotraAbiTool.getPhraseClassInstances(textPath, phraseClassToXmlMap, arffOutputPath,
                textAnalyzer, PROXIMITIES, attributeFilters);
        return conceptToPhraseClassInstancesMap;
      }


      private Map<String, List<AnnotatedPhrase>> getDocumentToAnnotationMap(
          final String textDirectoryPath, final String xmlDirectoryPath,
          final Set<String> acceptedConcepts) {

        AnnotationImporter annotationImporter = AnnotationImporter.getImporter(AnnotationApp.EHOST);
        List<RaptatPair<File, File>> txtXmlPairs =
            GeneralHelper.getXmlFilesAndMatchTxt(textDirectoryPath, xmlDirectoryPath);
        Map<String, List<AnnotatedPhrase>> resultMap = new HashMap<>(txtXmlPairs.size());

        for (RaptatPair<File, File> txtXmlPair : txtXmlPairs) {
          List<AnnotatedPhrase> annotations =
              annotationImporter.importAnnotations(txtXmlPair.right.getAbsolutePath(),
                  txtXmlPair.left.getAbsolutePath(), acceptedConcepts);
          resultMap.put(txtXmlPair.left.getAbsolutePath(), annotations);
        }
        return resultMap;
      }


      /**
       * Creates simple map for ABI project for learning to extract laterality and index type of
       * index values
       *
       * @param lateralityXmlMapping
       * @param indexTypeXmlMapping
       * @return
       */
      private Map<PhraseClass, Optional<String>> getPhraseClassToXmlMap(
          final Optional<String> lateralityXmlMapping, final Optional<String> indexTypeXmlMapping) {
        Map<PhraseClass, Optional<String>> phraseClassToXmlMap = new HashMap<>();

        /*
         * The laterality and indextype mappings are paths to where the xml for these phrase class
         * types are stored
         */
        phraseClassToXmlMap.put(PhraseClass.INDEX_VALUE_LATERALITY, lateralityXmlMapping);
        phraseClassToXmlMap.put(PhraseClass.INDEX_VALUE_TYPE, indexTypeXmlMapping);
        return phraseClassToXmlMap;
      }


      /**
       * Returns the first AnnotatedPhrase instance found in a relation called targetedRelationName
       * within the AnnotatedPhrase, phrase, supplied as a parameter. The found phrase must have an
       * id within the mentionIdToPhraseMap parameter. Returns null if nothing found.
       *
       * @param phrase
       * @param targetedRelationName
       * @param mentionIdToPhraseMap
       * @return
       */
      private AnnotatedPhrase getRelatedConcept(final AnnotatedPhrase phrase,
          final String targetedRelationName,
          final Map<String, AnnotatedPhrase> mentionIdToPhraseMap) {
        for (ConceptRelation conceptRelation : phrase.getConceptRelations()) {
          if (conceptRelation.getConceptRelationName().equalsIgnoreCase(targetedRelationName)) {
            String mentionId = conceptRelation.getRelatedAnnotationID().toLowerCase();
            if (mentionIdToPhraseMap.containsKey(mentionId)) {
              return mentionIdToPhraseMap.get(mentionId);
            }
          }
        }
        return null;
      }

      /**
       * Creates and returns a mapping of annotated phrases that we are targeting (selected from the
       * annotatedPhrases parameter) to the mentionIDs of the concepts for which the targeted phrase
       * has a conceptRelation. Only mentionIDs with a concept name corresponding to the first and
       * second concept names mapped to by the typeNameMap parameter are returned.
       *
       * @param documentAnnotations
       * @param typeNameMap
       *
       * @return
       */
      private Map<AnnotatedPhrase, MutablePair<String, String>> getTargetToRelatedConceptNamesMap(
          final List<AnnotatedPhrase> documentAnnotations,
          final Map<ConceptTypeName, String> typeNameMap) {
        /*
         * Create a map to build a new set of annotations from for this document
         */
        Map<AnnotatedPhrase, MutablePair<String, String>> targetToRelatedConceptMapping =
            new HashMap<>();

        String targetConceptName = typeNameMap.get(ConceptTypeName.TARGET_NAME);
        String firstConceptName = typeNameMap.get(ConceptTypeName.FIRST_RELATED_CONCEPT_NAME);
        String firstRelationName =
            typeNameMap.get(ConceptTypeName.FIRST_CONCEPT_TARGET_RELATION_NAME);
        String firstAttributeName =
            typeNameMap.get(ConceptTypeName.FIRST_CONCEPT_TARGET_ATTRIBUTE_NAME);

        String secondConceptName = typeNameMap.get(ConceptTypeName.SECOND_RELATED_CONCEPT_NAME);
        String secondRelationName =
            typeNameMap.get(ConceptTypeName.SECOND_CONCEPT_TARGET_RELATION_NAME);
        String secondAttributeName =
            typeNameMap.get(ConceptTypeName.SECOND_CONCEPT_TARGET_ATTRIBUTE_NAME);

        /*
         * Create a map that we will use to find AnnotatedPhrase instances stored in
         * ConceptRelations for another phrase
         */
        Map<String, AnnotatedPhrase> idToPhraseMap = new HashMap<>();
        documentAnnotations
            .forEach(phrase -> idToPhraseMap.put(phrase.getMentionId().toLowerCase(), phrase));

        for (AnnotatedPhrase documentAnnotation : documentAnnotations) {
          String conceptName = documentAnnotation.getConceptName();

          if (conceptName.equalsIgnoreCase(targetConceptName)) {
            targetToRelatedConceptMapping.putIfAbsent(documentAnnotation, new MutablePair<>());
          }

          else if (conceptName.equalsIgnoreCase(firstConceptName)) {
            AnnotatedPhrase targetPhrase =
                getRelatedConcept(documentAnnotation, firstRelationName, idToPhraseMap);
            if (targetPhrase != null) {
              MutablePair<String, String> attributeValuePair = targetToRelatedConceptMapping
                  .computeIfAbsent(targetPhrase, key -> new MutablePair<>());
              attributeValuePair.left = getAttributeValue(documentAnnotation, firstAttributeName);
            }

          }

          else if (conceptName.equalsIgnoreCase(secondConceptName)) {
            AnnotatedPhrase targetPhrase =
                getRelatedConcept(documentAnnotation, secondRelationName, idToPhraseMap);
            if (targetPhrase != null) {
              MutablePair<String, String> attributeValuePair = targetToRelatedConceptMapping
                  .computeIfAbsent(targetPhrase, key -> new MutablePair<>());
              attributeValuePair.right = getAttributeValue(documentAnnotation, secondAttributeName);
            }
          }
        }
        return targetToRelatedConceptMapping;
      }

      /**
       * @param tokenPhraseMakerFileName
       * @return
       */
      private TextAnalyzer getTextAnalyzer(final String tokenPhraseMakerFileName) {
        OptionsManager.getInstance().setRemoveStopWords(false);
        final TextAnalyzer textAnalyzer = new TextAnalyzer();
        textAnalyzer.setTokenProcessingOptions(OptionsManager.getInstance());
        textAnalyzer.createTokenPhraseMaker(tokenPhraseMakerFileName);
        return textAnalyzer;
      }

      /**
       * Concatenate classifications from different phrase classes that have the same hash string
       * mapping to the associated classification. Generally, the hash string is made up of
       * identifiers (e.g., document, character offset set, phrase text, etc) that would be shared
       * be two different phrase classes where we are classifying the phrase text using two or more
       * different classifiers).
       *
       * @param attributeHashedClassifications
       * @return
       */
      private Map<String, String> joinClassifications(
          final Map<PhraseClass, Map<String, String>> attributeHashedClassifications) {

        Map<String, String> resultMap = new HashMap<>();
        Map<String, StringBuilder> tempResult = new HashMap<>();

        /*
         * Process each phrase class. If two different phrases classes have the same hash string,
         * then their values are concatenated together.
         */
        for (PhraseClass phraseClass : attributeHashedClassifications.keySet()) {
          Map<String, String> hashedClassification =
              attributeHashedClassifications.get(phraseClass);
          for (String attributeIdentifierHash : hashedClassification.keySet()) {
            String classification = hashedClassification.get(attributeIdentifierHash);
            StringBuilder sb =
                tempResult.computeIfAbsent(attributeIdentifierHash, key -> new StringBuilder());
            sb.append(classification).append("\t");
          }
        }
        for (Entry<String, StringBuilder> entry : tempResult.entrySet()) {
          resultMap.put(entry.getKey(), entry.getValue().toString().trim());
        }

        return resultMap;
      }



      /**
       * Pairs instances with the classifiers they were used to train using two map of concept name
       * to a mapping from phrase class to instances or classifiers. This assumes that the maps are
       * identical in terms of the key concept names and PhraseClass objects used for mapping.
       *
       * @param mapToClassifiers
       * @param mapToInstancesForTraining
       * @return
       */
      private Map<String, Map<PhraseClass, RaptatPair<Classifier, Instances>>> pairClassifierWithInstances(
          final Map<String, Map<PhraseClass, Classifier>> mapToClassifiers,
          final Map<String, Map<PhraseClass, Instances>> mapToInstancesForTraining) {

        Map<String, Map<PhraseClass, RaptatPair<Classifier, Instances>>> resultMap =
            new HashMap<>();

        for (String conceptName : mapToClassifiers.keySet()) {

          Map<PhraseClass, Classifier> phraseClassToClassifierMap =
              mapToClassifiers.get(conceptName);
          Map<PhraseClass, Instances> phraseClassToInstancesMap =
              mapToInstancesForTraining.get(conceptName);
          Map<PhraseClass, RaptatPair<Classifier, Instances>> resultPhraseClassMapping =
              resultMap.computeIfAbsent(conceptName, key -> new HashMap<>());

          for (PhraseClass phraseClass : phraseClassToClassifierMap.keySet()) {

            Classifier classifier = phraseClassToClassifierMap.get(phraseClass);
            Instances instances = phraseClassToInstancesMap.get(phraseClass);
            resultPhraseClassMapping.put(phraseClass, new RaptatPair<>(classifier, instances));
          }
        }

        return resultMap;
      }


      /**
       * Pairs training and test Instances so they can be paired together for subsequent training
       * and testing.
       *
       * @param mapToInstancesForTraining
       * @param mapToInstancesForTesting
       * @return
       */
      private Map<String, Map<PhraseClass, RaptatPair<Instances, Instances>>> pairTrainTestInstances(
          final Map<String, Map<PhraseClass, Instances>> mapToInstancesForTraining,
          final Map<String, Map<PhraseClass, Instances>> mapToInstancesForTesting) {

        Map<String, Map<PhraseClass, RaptatPair<Instances, Instances>>> resultMap = new HashMap<>();
        for (String conceptName : mapToInstancesForTraining.keySet()) {

          Map<PhraseClass, Instances> phraseToInstancesForTraining =
              mapToInstancesForTraining.get(conceptName);
          Map<PhraseClass, Instances> phraseToInstancesForTesting =
              mapToInstancesForTesting.get(conceptName);

          for (PhraseClass phraseClass : phraseToInstancesForTraining.keySet()) {

            Instances trainingInstances = phraseToInstancesForTraining.get(phraseClass);
            Instances testingInstances = phraseToInstancesForTesting.get(phraseClass);
            RaptatPair<Instances, Instances> instancesPair =
                new RaptatPair<>(trainingInstances, testingInstances);

            Map<PhraseClass, RaptatPair<Instances, Instances>> pairedPhraseToPairsMap =
                resultMap.computeIfAbsent(conceptName, k -> new HashMap<>());
            pairedPhraseToPairsMap.put(phraseClass, instancesPair);

          }

        }
        return resultMap;
      }

      /**
       * Prints the classifications of instance objects to a file. Generally used after
       *
       * @param classifiedInstances
       * @param identifierAttributeIndices
       * @param outputFilePath
       */
      private void printInstanceClassifications(
          final Map<String, Map<PhraseClass, Instances>> classifiedInstances,
          final Set<Integer> identifierAttributeIndices, final String outputFilePath) {

        File outputPathFile = new File(outputFilePath);
        String baseName = outputPathFile.getName();
        String parentPath = outputPathFile.getParent();
        for (String conceptName : classifiedInstances.keySet()) {

          Map<PhraseClass, Instances> phraseToInstancesMap = classifiedInstances.get(conceptName);
          String conceptSpecificFilePath =
              parentPath + File.separator + conceptName + "_" + baseName;
          try (PrintWriter pw = new PrintWriter(new File(conceptSpecificFilePath))) {

            String header =
                getClassifiedInstancesHeader(phraseToInstancesMap, identifierAttributeIndices);
            pw.println(header);
            pw.flush();

            /*
             * Build identifierMapToClass for this particular phraseClass objects
             */
            Map<PhraseClass, Map<String, String>> attributeHashedClassifications = new HashMap<>();
            for (PhraseClass phraseClass : phraseToInstancesMap.keySet()) {

              Map<String, String> identifierMapToClass = attributeHashedClassifications
                  .computeIfAbsent(phraseClass, key -> new HashMap<>());
              Instances instances = phraseToInstancesMap.get(phraseClass);

              Enumeration<Instance> instanceEnumerator = instances.enumerateInstances();
              while (instanceEnumerator.hasMoreElements()) {

                Instance instance = instanceEnumerator.nextElement();
                StringBuffer sb = new StringBuffer();
                for (int attributeIndex : identifierAttributeIndices) {
                  sb.append(instance.stringValue(attributeIndex));
                  sb.append("\t");
                }
                String identifierString = sb.toString().trim();
                String classification = instance.stringValue(instances.classAttribute());
                identifierMapToClass.put(identifierString, classification);
              }
            }

            /*
             * Join classifications for matching hashes
             */
            Map<String, String> joinedClassifications =
                joinClassifications(attributeHashedClassifications);
            for (String identifierHashString : joinedClassifications.keySet()) {
              String resultLine =
                  identifierHashString + "\t" + joinedClassifications.get(identifierHashString);
              pw.println(resultLine);
              pw.flush();
            }

          } catch (Exception e) {
            System.err.println("Unable to write out classifications\t" + e.getLocalizedMessage());
            e.printStackTrace();
          }
        }



      }


      private void promoteRelationsToConcepts(final String textDirectoryPath,
          final String xmlDirectoryPath, final String outputDirectoryPath,
          final String targetConceptName, final Map<String, String> relationshipToAttributeNames,
          final AnnotationMutator annotationMutator) {

        boolean correctOffsets = true;
        boolean insertPhraseStrings = true;
        boolean overwriteContents = true;
        XMLExporterRevised exporter = new XMLExporterRevised(AnnotationApp.EHOST,
            new File(outputDirectoryPath), overwriteContents);
        AnnotationImporter annotationImporter = AnnotationImporter.getImporter(AnnotationApp.EHOST);

        List<RaptatPair<File, File>> txtXmlPairs =
            GeneralHelper.getXmlFilesAndMatchTxt(textDirectoryPath, xmlDirectoryPath);
        for (RaptatPair<File, File> txtXmlPair : txtXmlPairs) {
          List<AnnotatedPhrase> annotations = annotationImporter.importAnnotations(
              txtXmlPair.right.getAbsolutePath(), txtXmlPair.left.getAbsolutePath(), null);
          List<AnnotatedPhrase> revisedAnnotations =
              annotationMutator.promoteRelationshipValuesToConcepts(annotations, targetConceptName,
                  relationshipToAttributeNames);
          RaptatDocument document = new RaptatDocument();
          document.setTextSourcePath(txtXmlPair.left.getAbsolutePath());
          AnnotationGroup annotationGroup = new AnnotationGroup(document, revisedAnnotations);

          exporter.exportReferenceAnnotationGroup(annotationGroup, correctOffsets,
              insertPhraseStrings);
        }

      }


      private void remapConceptNames(final String textPath, final String inputXmlPath,
          final String outputDirectoryPath, final Map<String, String> remap) {
        Map<String, List<AnnotatedPhrase>> documentToAnnotationMap =
            getDocumentToAnnotationMap(textPath, inputXmlPath, new HashSet<>(remap.keySet()));

        for (List<AnnotatedPhrase> documentAnnotations : documentToAnnotationMap.values()) {
          for (AnnotatedPhrase annotatedPhrase : documentAnnotations) {
            String conceptName = annotatedPhrase.getConceptName();
            String remappedName = remap.get(conceptName);
            if (remappedName == null) {
              remappedName = conceptName;
            }
            annotatedPhrase.setConceptName(remappedName);
          }
        }

        writeRevisedAnnotations(documentToAnnotationMap, outputDirectoryPath);
      }

      /**
       * Revises annotations so that index value annotations are combined with their relationships
       * of index value type and laterality to create a single annotation. So, an annotation of the
       * number 1.23 (for index value), that is also linked to an annotation for type abi and
       * laterality right would be converted to the annotation with concept name
       * indexvalue_right_abi.
       *
       * @param documentToAnnotationsMap
       * @param typeNameMap
       * @return
       */
      private Map<String, List<AnnotatedPhrase>> reviseCorpusAnnotations(
          final Map<String, List<AnnotatedPhrase>> documentToAnnotationsMap,
          final Map<ConceptTypeName, String> typeNameMap) {

        Map<String, List<AnnotatedPhrase>> revisedCorpusAnnotations =
            new HashMap<>(documentToAnnotationsMap.size());

        for (String documentPath : documentToAnnotationsMap.keySet()) {

          List<AnnotatedPhrase> revisedDocumentAnnotationList =
              reviseDocumentAnnotations(documentToAnnotationsMap.get(documentPath), typeNameMap);
          revisedCorpusAnnotations.put(documentPath, revisedDocumentAnnotationList);
        }

        return revisedCorpusAnnotations;
      }

      /**
       * Takes a collection of annotated phrases and combines them to form a new set combining index
       * values with their type and laterality in to a single concept name
       *
       * @param documentAnnotations
       * @param conceptTypeToNameMap
       * @return
       */
      private List<AnnotatedPhrase> reviseDocumentAnnotations(
          final List<AnnotatedPhrase> documentAnnotations,
          final Map<ConceptTypeName, String> conceptTypeToNameMap) {

        Map<AnnotatedPhrase, MutablePair<String, String>> targetToRelatedConceptMap =
            getTargetToRelatedConceptNamesMap(documentAnnotations, conceptTypeToNameMap);

        List<AnnotatedPhrase> revisedDocumentAnnotations = new ArrayList<>();
        for (AnnotatedPhrase targetPhrase : targetToRelatedConceptMap.keySet()) {
          MutablePair<String, String> relatedConcepts = targetToRelatedConceptMap.get(targetPhrase);
          AnnotatedPhrase revisedAnnotation = new AnnotatedPhrase(targetPhrase);
          String revisedConceptName = conceptTypeToNameMap.get(ConceptTypeName.TARGET_NAME) + "_"
              + relatedConcepts.left + "_" + relatedConcepts.right;
          revisedAnnotation.setConceptName(revisedConceptName);
          revisedDocumentAnnotations.add(revisedAnnotation);
        }
        return revisedDocumentAnnotations;
      }

      private void writeRevisedAnnotations(
          final Map<String, List<AnnotatedPhrase>> documentToRevisedAnnotationsMap,
          final String pathToResultFolder) {

        File resultDirectory = new File(pathToResultFolder);
        if (!resultDirectory.exists()) {
          try {
            FileUtils.forceMkdir(resultDirectory);
          } catch (IOException e) {
            System.err.println(
                "Unable to create directory:" + pathToResultFolder + "\n" + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
          }
        }
        /*
         * Get the first document path to determine where to put the annotations
         */
        Set<String> documentPathSet = documentToRevisedAnnotationsMap.keySet();
        if (documentPathSet.isEmpty()) {
          return;
        }

        boolean overwriteContents = true;
        XMLExporterRevised exporter = new XMLExporterRevised(AnnotationApp.EHOST,
            new File(pathToResultFolder), overwriteContents);

        for (String documentPath : documentPathSet) {
          RaptatDocument document = new RaptatDocument();
          document.setTextSourcePath(documentPath);
          AnnotationGroup annotationGroup =
              new AnnotationGroup(document, documentToRevisedAnnotationsMap.get(documentPath));

          boolean correctOffsets = true;
          boolean insertPhraseStrings = true;
          exporter.exportReferenceAnnotationGroup(annotationGroup, correctOffsets,
              insertPhraseStrings);
        }

      }

      private void writeWekaInstances(final String outputDirectory,
          final Map<String, Map<PhraseClass, Instances>> conceptToPhraseClassInstances) {

        for (String conceptName : conceptToPhraseClassInstances.keySet()) {
          Map<PhraseClass, Instances> phraseClassToInstancesMap =
              conceptToPhraseClassInstances.get(conceptName);
          for (PhraseClass phraseClass : phraseClassToInstancesMap.keySet()) {

            String filename = conceptName + "_" + phraseClass.toString() + "_"
                + GeneralHelper.getTimeStamp() + ".txt";
            Instances instances = phraseClassToInstancesMap.get(phraseClass);
            WekaInstancesWriter instancesWriter =
                new WekaInstancesWriter(outputDirectory, filename, instances);
            instancesWriter.writeInstancesToFile();
          }
        }

      }
    });
  }
}

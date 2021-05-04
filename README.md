# RandomForestABI
System to train and run a random forest-based machine learning model to extract ankle-brachial indices from clinical reports.  

### What ***is*** in the repository?

This repository contains the Java source code used both both training random-forest based models that will identify ankle-brachial index (ABI) values, toe-brachial index (TBI) values, and the laterality of these values (left/right).  The source code for testing or running the models is also included.

The repository also contains two jar files which are libraries built by our project group and required by classes in the source code.  Although the ABI system does not directly use either library, they are required by other elements in the code used in other projects.  The two jar files are *WekaMultiSearch-0.0.1-SNAPSHOT.jar* and *WekaMultiSearchParams-0.0.1-SNAPSHOT.jar*.

Finally, the repository contains two other files.  The first, *JarNamesForABIProject_210504.txt*, is a listing of libraries contained in jars that are not supplied here due to licensing requirements.  All jars listed in this document and not supplied within this repository should be publicly available. The other file is a starter list of regular expressions, *ABILabelPatterns_v01_191117.txt*, which can be used for training the system.  The regular expressions are used to identify features used by the random forest model.  Optimizing the system to accurately extract ABI and TBI from a particular document set may require modifications to these expressions.

### What ***is not*** in the repository?

As noted above, multiple libraries provided as jar files are required but not included in the repository.  All should be publicly available.  The jar files are listed in the repository file described in the previous section.

A second resource that is required but not included is the Lexical Variant Generator (LVG) tool supplied by the National Library of Medicine and National Institutes of Health.  The system uses the **[2014 version of LVG](https://lhncbc.nlm.nih.gov/LSG/Projects/lvg/current/web/release/2014.html)**.  

### How do I run the code?

The system should be started by the main method contained in the GirotraABI class.  The various actions needed to train or test the system can be selected by setting the RunType object, runType, within the "run" method to one of the specified enum values.

### Contact for questions?

*glenn.t.gobbel@vumc.org*
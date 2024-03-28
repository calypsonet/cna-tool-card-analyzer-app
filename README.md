## Introduction

This Git repository contains several tools to facilitate interaction with Calypso cards and analyze their file
structure. The tools in this repository are developed and maintained by Calypso Networks Association.

This document provides a detailed description of each tool, including their features, usage, dependencies, license, and
copyright information.

---

## Calypso Card Analyzer

The Calypso Card Analyzer is a tool for analyzing the file structure of a Calypso smart card. It retrieves the card's
data and generates a JSON report containing the card's structure and application data.

### Features

- Analyze the file structure of a Calypso smart card
- Retrieve card data and application data
- Generate a JSON report containing the card's structure and application data

### Program workflow

1. Initialize the smart card reader and check if a card is present.
2. Retrieve the traceability information of the card.
3. Get the application data for each AID (Application Identifier) in the provided AID list.
4. Create a `CardStructureData` object containing the traceability information, software information, and application
   data.
5. Convert the `CardStructureData` object to a JSON string using the Gson library.
6. Write the JSON string to a file with a name based on the current date and the card's serial number.
7. Print the JSON string to the console.

---

## Calypso Card File Structure Checker

The Calypso Card File Structure Checker is a tool for checking the file structure of a Calypso card against a given JSON
file containing the expected file structure.

### Features

- Check the file structure of a Calypso card against a given JSON file
- Compare the expected file structure with the actual file structure of the card
- Print the differences between the expected and actual file structures to the console

### Program workflow

1. Load the expected file structure from a JSON file.
2. Initialize the smart card reader and check if a card is present.
3. Retrieve the actual file structure of the card.
4. Compare the expected file structure with the actual file structure.
5. Print the differences between the expected and actual file structures to the console.

---

## Building and Using the Tools

### Building the JARs

To build the JARs for the Calypso Card Analyzer and Calypso Card File Structure Checker tools, follow these steps:

1. Clone this Git repository to your local machine.
2. Open a terminal and navigate to the root directory of the project.
3. Run the following command to build the JARs:

```bash
./gradlew build
```

After running this command, the JARs will be generated in the `build/libs` directory of the project.

### Using the JARs

To use the JARs, follow these steps:

1. Download the JARs from
   the [releases](https://github.com/CalypsoNetworksAssociation/calypso-card-analysis-tools/releases) page of this
   repository.
2. Connect a Calypso card reader to your computer.
3. Insert a Calypso card into the reader.
4. Open a terminal and navigate to the directory containing the downloaded JARs.
5. Run the following command to use the Calypso Card Analyzer tool:

```bash
java -jar Tool_AnalyzeCardFileStructure.jar
```

6. Run the following command to use the Calypso Card File Structure Checker tool:

```bash
java -jar Tool_CheckCardFileStructure.jar
```

The tools will read the card and perform their respective functions. The Calypso Card Analyzer tool will generate a JSON
report containing the card's structure and application data, while the Calypso Card File Structure Checker tool will
check the file structure of the card against a given JSON file containing the expected file structure.

### Dependencies

- Eclipse Keyple Core
- Eclipse Keyple Calypso Extension
- Eclipse Keyple PC/SC Plugin
- Google Gson

### License

This program is made available under the terms of the Eclipse Public License 2.0. See the [LICENSE](LICENSE) file for
more information.

### Copyright

Copyright (c) 2024 Calypso Networks Association
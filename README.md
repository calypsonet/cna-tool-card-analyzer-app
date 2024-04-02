## Introduction

This Git repository contains a set of tools for facilitating interaction with Calypso cards and analyzing their file
structure. The tools are developed and maintained by the [Calypso Networks Association](https://calypsonet.org).

This document provides a detailed description of the tools, including their features, usage, dependencies, license, and
copyright information. Although there are two separate executables, they offer closely related and interconnected
functionalities for working with Calypso cards.

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

### Using the Calypso Card Analyzer JAR

To use the Calypso Card Analyzer JAR, follow these steps:

1. Download the Calypso Card Analyzer JAR from
   the [releases](https://github.com/calypsonet/calypso-card-analyzer/releases/) page of this
   repository.
2. Connect a PC/SC card reader to your computer.
3. Insert a Calypso card into the reader.
4. Open a terminal and navigate to the directory containing the downloaded JAR.
5. Run the following command:

```bash
java -jar Tool_AnalyzeCardFileStructure.jar [readerNameRegex]
```

The `readerNameRegex` parameter is optional and can be used to specify a regular expression for selecting the card reader
to use. If this parameter is not provided, the tool will use the following
expression `.*(ASK.*|Identiv.*2|ACS ACR122U|SCR3310).*`.

The Calypso Card Analyzer tool will read the card and generate a JSON report containing the card's structure and
application data.

### Using the Calypso Card File Structure Checker JAR

To use the Calypso Card File Structure Checker JAR, follow these steps:

1. Download the Calypso Card File Structure Checker JAR from
   the [releases](https://github.com/calypsonet/calypso-card-analyzer/releases/) page of this
   repository.
2. Connect a PC/SC card reader to your computer.
3. Insert a Calypso card into the reader.
4. Open a terminal and navigate to the directory containing the downloaded JAR.
5. Run the following command:

```bash
java -jar Tool_CheckCardFileStructure.jar <json_file_name> [readerNameRegex]
```

Replace `<json_file_name>` with the name of the JSON file containing the reference file structure.

The `readerNameRegex` parameter is optional and can be used to specify a regular expression for selecting the card reader
to use. If this parameter is not provided, the tool will use the following
expression `.*(ASK.*|Identiv.*2|ACS ACR122U|SCR3310).*`.

For example, if you want to use the `TestKit_CalypsoPrimeRegularProfile_v3.json` card profile provided in
the `card_profiles` directory, you can run the following command:

```bash
java -jar Tool_CheckCardFileStructure.jar card_profiles/TestKit_CalypsoPrimeRegularProfile_v3.json
```

The tool will then read the reference file structure from the specified JSON file, check if a card is present in the
reader, and perform the necessary checks. The verification results will be displayed in the console.

### Dependencies

- Eclipse Keyple Core
- Eclipse Keyple Calypso Extension
- Eclipse Keyple PC/SC Plugin
- Google Gson

### License

This program is made available under the terms of the Eclipse Public License 2.0. See the [LICENSE](LICENSE) file for
more information.

### Copyright

Copyright (c) 2024 [Calypso Networks Association](https://calypsonet.org)
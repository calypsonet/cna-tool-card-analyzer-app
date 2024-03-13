/* **************************************************************************************
 * Copyright (c) 2019 Calypso Networks Association https://calypsonet.org/
 *
 * See the NOTICE file(s) distributed with this work for additional information
 * regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 ************************************************************************************** */
package org.cna.keyple.tool.calypso.card;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import org.calypsonet.terminal.calypso.GetDataTag;
import org.calypsonet.terminal.calypso.SelectFileControl;
import org.calypsonet.terminal.calypso.card.CalypsoCard;
import org.calypsonet.terminal.calypso.card.CalypsoCardSelection;
import org.calypsonet.terminal.calypso.card.ElementaryFile;
import org.calypsonet.terminal.calypso.transaction.CardTransactionManager;
import org.calypsonet.terminal.calypso.transaction.UnexpectedCommandStatusException;
import org.calypsonet.terminal.reader.CardReader;
import org.calypsonet.terminal.reader.selection.CardSelectionManager;
import org.calypsonet.terminal.reader.selection.CardSelectionResult;
import org.cna.keyple.tool.calypso.carddata.CardApplicationData;
import org.cna.keyple.tool.calypso.carddata.CardFileData;
import org.cna.keyple.tool.calypso.carddata.CardStructureData;
import org.cna.keyple.tool.calypso.carddata.RecordData;
import org.cna.keyple.tool.calypso.common.ToolUtils;
import org.eclipse.keyple.card.calypso.CalypsoExtensionService;
import org.eclipse.keyple.core.service.Plugin;
import org.eclipse.keyple.core.service.SmartCardService;
import org.eclipse.keyple.core.service.SmartCardServiceProvider;
import org.eclipse.keyple.plugin.pcsc.PcscPluginFactoryBuilder;
import org.eclipse.keyple.plugin.pcsc.PcscReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tool_AnalyzeCardFileStructure {

  private static final Logger logger = LoggerFactory.getLogger(Tool_AnalyzeCardFileStructure.class);
  private static final SmartCardService smartCardService = SmartCardServiceProvider.getService();
  private static final CalypsoExtensionService calypsoCardService =
      CalypsoExtensionService.getInstance();
  private static CardReader cardReader;
  private static CardTransactionManager cardTransactionManager;

  private static void fillFilesTable(CalypsoCard selectedApp) {

    cardTransactionManager.prepareSelectFile(SelectFileControl.FIRST_EF);
    try {
      cardTransactionManager.processCommands();
    } catch (UnexpectedCommandStatusException e) {
      return;
    }

    if (selectedApp.getFiles() == null || selectedApp.getFiles().size() != 1) {
      return;
    }

    int numberOfFiles = 1;

    do {

      numberOfFiles++;
      cardTransactionManager.prepareSelectFile(SelectFileControl.NEXT_EF);
      try {
        cardTransactionManager.processCommands();
      } catch (UnexpectedCommandStatusException e) {
        return;
      }

    } while (selectedApp.getFiles().size() == numberOfFiles);
  }

  private static CardFileData getFileData(ElementaryFile selectedFile, CalypsoCard selectedApp) {

    CardFileData fileData = new CardFileData(selectedFile);

    if (selectedFile.getHeader().getEfType() != ElementaryFile.Type.BINARY
        && selectedFile.getHeader().getAccessConditions()[0] != 0x01
        && selectedFile.getHeader().getAccessConditions()[0] != 0x14
        && selectedFile.getHeader().getAccessConditions()[0] != 0x15
        && selectedFile.getSfi() != 0) {

      for (int i = 0; i < selectedFile.getHeader().getRecordsNumber(); i++) {

        calypsoCardService
            .createCardTransactionWithoutSecurity(cardReader, selectedApp)
            .prepareReadRecord(selectedFile.getSfi(), (byte) (i + 1))
            .processCommands();

        fileData
            .getRecordData()
            .add((new RecordData(i + 1, selectedFile.getData().getContent((i + 1)))));
      }
    }

    return fileData;
  }

  private static CardApplicationData getApplicationData(
      CalypsoCardSelection.FileOccurrence fileOccurrence, String aid) {

    CardSelectionManager cardSelectionManager = smartCardService.createCardSelectionManager();

    cardSelectionManager.prepareSelection(
        calypsoCardService
            .createCardSelection()
            .setFileOccurrence(fileOccurrence)
            .acceptInvalidatedCard()
            .filterByDfName(aid));

    CardSelectionResult selectionResult =
        cardSelectionManager.processCardSelectionScenario(cardReader);

    if (selectionResult.getActiveSmartCard() == null) {
      return null;
    }

    CalypsoCard selectedApplication = (CalypsoCard) selectionResult.getActiveSmartCard();

    cardTransactionManager =
        calypsoCardService.createCardTransactionWithoutSecurity(cardReader, selectedApplication);
    cardTransactionManager.prepareSelectFile(SelectFileControl.CURRENT_DF);
    cardTransactionManager.processCommands();

    // Get and fill the Application file information
    CardApplicationData cardAppData = new CardApplicationData(selectedApplication);

    fillFilesTable(selectedApplication);

    Iterator<ElementaryFile> filesIter = selectedApplication.getFiles().iterator();

    while (filesIter.hasNext()) {

      CardFileData cardFileData = getFileData(filesIter.next(), selectedApplication);
      cardAppData.getFileList().add(cardFileData);
    }

    return cardAppData;
  }

  public static byte[] getTraceabilityInfo(List<String> aidList) {

    CardSelectionManager cardSelectionManager = smartCardService.createCardSelectionManager();

    Iterator aidListIter = aidList.iterator();

    while (aidListIter.hasNext()) {

      String currentAid = (String) aidListIter.next();

      cardSelectionManager.prepareSelection(
          calypsoCardService
              .createCardSelection()
              .acceptInvalidatedCard()
              .filterByDfName(currentAid));

      CardSelectionResult selectionResult =
          cardSelectionManager.processCardSelectionScenario(cardReader);

      if (selectionResult.getActiveSmartCard() != null) {

        CalypsoCard calypsoCard = (CalypsoCard) selectionResult.getActiveSmartCard();

        cardTransactionManager =
            calypsoCardService.createCardTransactionWithoutSecurity(cardReader, calypsoCard);
        cardTransactionManager.prepareGetData(GetDataTag.TRACEABILITY_INFORMATION);
        cardTransactionManager.processCommands();

        return calypsoCard.getTraceabilityInformation();
      }
    }

    return null;
  }

  public static void getApplicationsData(String aid, List<CardApplicationData> cardAppDataList) {

    CardApplicationData cardAppData =
        getApplicationData(CalypsoCardSelection.FileOccurrence.FIRST, aid);

    while (cardAppData != null) {
      cardAppDataList.add(cardAppData);
      cardAppData = getApplicationData(CalypsoCardSelection.FileOccurrence.NEXT, aid);
    }
  }

  public static boolean initReaders() {

    Plugin plugin = smartCardService.registerPlugin(PcscPluginFactoryBuilder.builder().build());

    smartCardService.checkCardExtension(calypsoCardService);

    String pcscContactlessCardReaderName =
        ToolUtils.getCardReaderName(plugin, ToolUtils.CARD_READER_NAME_REGEX);
    cardReader = plugin.getReader(pcscContactlessCardReaderName);

    plugin
        .getReaderExtension(PcscReader.class, pcscContactlessCardReaderName)
        .setContactless(true)
        .setIsoProtocol(PcscReader.IsoProtocol.ANY)
        .setSharingMode(PcscReader.SharingMode.SHARED);

    logger.info("= Card Reader  NAME = {}", cardReader.getName());

    return cardReader.isCardPresent();
  }

  public static void main(String[] args) {

    boolean isCardPresent = initReaders();

    if (isCardPresent) {

      // - GEN RT TEST (91h) - , - GEN SV TEST (92h) - , - Hoplink -, - Ndef -,  - MF -, - RT -, -
      // SV -, - GEN AID -
      List<String> aidList =
          Arrays.asList(
              "A000000291FF91",
              "A000000291FF92",
              "A000000291A000000191",
              "D276000085",
              "334D54522E",
              "315449432E",
              "304554502E",
              "A000000291");
      // Arrays.asList("A000000291FF91", "A000000291FF92", "A000000291A000000191", "D276000085",
      // "334D54522E", "315449432E", "304554502E");

      byte[] traceabilityInfo = getTraceabilityInfo(aidList);

      if (traceabilityInfo == null) {
        logger.info("No applications found.");
        return;
      }

      CardStructureData cardStructureData =
          new CardStructureData(
              traceabilityInfo, "AnalyzeCardFileStructure", new Date(), 2, "Keyple");

      Iterator aidListIter = aidList.iterator();

      while (aidListIter.hasNext()) {
        getApplicationsData((String) aidListIter.next(), cardStructureData.getApplicationList());
      }

      try {
        Gson gson =
            new GsonBuilder()
                .registerTypeHierarchyAdapter(byte[].class, new ToolUtils.HexTypeAdapter())
                .setPrettyPrinting()
                .create();

        String dateString = new SimpleDateFormat("yyyyMMdd").format(new Date());

        String fileName =
            new String(
                dateString
                    + "_CardData_"
                    + cardStructureData.getApplicationList().get(0).getCsnDec()
                    + ".json");

        cardStructureData.setId(fileName);

        String jsonToPrint = gson.toJson(cardStructureData);

        FileWriter fw = new FileWriter(fileName);
        fw.write(jsonToPrint);
        fw.close();

      } catch (Exception e) {
        logger.error("Exception while writing the report: " + e.getCause());
      }

      cardStructureData.print(logger);
    } else {
      logger.error("No card is present in the reader.");
    }
    System.exit(0);
  }
}

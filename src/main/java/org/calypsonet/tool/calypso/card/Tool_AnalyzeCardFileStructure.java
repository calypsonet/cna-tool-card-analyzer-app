/* **************************************************************************************
 * Copyright (c) 2024 Calypso Networks Association https://calypsonet.org/
 *
 * See the NOTICE file(s) distributed with this work for additional information
 * regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 ************************************************************************************** */
package org.calypsonet.tool.calypso.card;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import org.calypsonet.tool.calypso.carddata.CardApplicationData;
import org.calypsonet.tool.calypso.carddata.CardFileData;
import org.calypsonet.tool.calypso.carddata.CardStructureData;
import org.calypsonet.tool.calypso.carddata.RecordData;
import org.calypsonet.tool.calypso.common.ToolUtils;
import org.eclipse.keyple.card.calypso.CalypsoExtensionService;
import org.eclipse.keyple.core.service.Plugin;
import org.eclipse.keyple.core.service.SmartCardService;
import org.eclipse.keyple.core.service.SmartCardServiceProvider;
import org.eclipse.keyple.plugin.pcsc.PcscPluginFactoryBuilder;
import org.eclipse.keyple.plugin.pcsc.PcscReader;
import org.eclipse.keypop.calypso.card.GetDataTag;
import org.eclipse.keypop.calypso.card.SelectFileControl;
import org.eclipse.keypop.calypso.card.card.CalypsoCard;
import org.eclipse.keypop.calypso.card.card.CalypsoCardSelectionExtension;
import org.eclipse.keypop.calypso.card.card.ElementaryFile;
import org.eclipse.keypop.calypso.card.transaction.ChannelControl;
import org.eclipse.keypop.calypso.card.transaction.FreeTransactionManager;
import org.eclipse.keypop.calypso.card.transaction.SelectFileException;
import org.eclipse.keypop.calypso.card.transaction.UnexpectedCommandStatusException;
import org.eclipse.keypop.reader.CardReader;
import org.eclipse.keypop.reader.selection.CardSelectionManager;
import org.eclipse.keypop.reader.selection.CardSelectionResult;
import org.eclipse.keypop.reader.selection.CommonIsoCardSelector;
import org.eclipse.keypop.reader.selection.IsoCardSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tool for analyzing the file structure of a Calypso smart card.
 *
 * @since 2.0.0 .
 */
public class Tool_AnalyzeCardFileStructure {

  private static final Logger logger = LoggerFactory.getLogger(Tool_AnalyzeCardFileStructure.class);
  private static final SmartCardService smartCardService = SmartCardServiceProvider.getService();
  private static final CalypsoExtensionService calypsoCardService =
      CalypsoExtensionService.getInstance();
  private static final String SOFTWARE_INFORMATION = "AnalyzeCardFileStructure";
  private static final String SOFTWARE_NAME = "Calypso Card Analyzer";
  private static CardReader cardReader;
  private static FreeTransactionManager cardTransactionManager;

  private static void fillFilesTable(CalypsoCard selectedApp) {

    cardTransactionManager.prepareSelectFile(SelectFileControl.FIRST_EF);
    try {
      cardTransactionManager.processCommands(ChannelControl.KEEP_OPEN);
    } catch (UnexpectedCommandStatusException | SelectFileException e) {
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
        cardTransactionManager.processCommands(ChannelControl.KEEP_OPEN);
      } catch (UnexpectedCommandStatusException | SelectFileException e) {
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
            .getCalypsoCardApiFactory()
            .createFreeTransactionManager(cardReader, selectedApp)
            .prepareReadRecord(selectedFile.getSfi(), (byte) (i + 1))
            .processCommands(ChannelControl.KEEP_OPEN);

        fileData
            .getRecordDataList()
            .add((new RecordData(i + 1, selectedFile.getData().getContent((i + 1)))));
      }
    }

    return fileData;
  }

  private static CardApplicationData getApplicationData(
      CommonIsoCardSelector.FileOccurrence fileOccurrence, String aid) {

    CardSelectionManager cardSelectionManager =
        smartCardService.getReaderApiFactory().createCardSelectionManager();

    IsoCardSelector isoCardSelector =
        SmartCardServiceProvider.getService()
            .getReaderApiFactory()
            .createIsoCardSelector()
            .filterByDfName(aid)
            .setFileOccurrence(fileOccurrence);

    CalypsoCardSelectionExtension calypsoCardSelectionExtension =
        CalypsoExtensionService.getInstance()
            .getCalypsoCardApiFactory()
            .createCalypsoCardSelectionExtension()
            .acceptInvalidatedCard();

    cardSelectionManager.prepareSelection(isoCardSelector, calypsoCardSelectionExtension);

    CardSelectionResult selectionResult =
        cardSelectionManager.processCardSelectionScenario(cardReader);

    if (selectionResult.getActiveSmartCard() == null) {
      return null;
    }

    CalypsoCard selectedApplication = (CalypsoCard) selectionResult.getActiveSmartCard();

    cardTransactionManager =
        calypsoCardService
            .getCalypsoCardApiFactory()
            .createFreeTransactionManager(cardReader, selectedApplication);
    cardTransactionManager
        .prepareSelectFile(SelectFileControl.CURRENT_DF)
        .processCommands(ChannelControl.KEEP_OPEN);

    // Get and fill the Application file information
    CardApplicationData cardAppData = new CardApplicationData(selectedApplication);

    fillFilesTable(selectedApplication);

    for (ElementaryFile elementaryFile : selectedApplication.getFiles()) {

      CardFileData cardFileData = getFileData(elementaryFile, selectedApplication);
      cardAppData.getFileList().add(cardFileData);
    }

    return cardAppData;
  }

  public static byte[] getTraceabilityInfo(List<String> aidList) {

    CardSelectionManager cardSelectionManager =
        smartCardService.getReaderApiFactory().createCardSelectionManager();

    for (String currentAid : aidList) {

      IsoCardSelector isoCardSelector =
          SmartCardServiceProvider.getService()
              .getReaderApiFactory()
              .createIsoCardSelector()
              .filterByDfName(currentAid);

      CalypsoCardSelectionExtension calypsoCardSelectionExtension =
          CalypsoExtensionService.getInstance()
              .getCalypsoCardApiFactory()
              .createCalypsoCardSelectionExtension()
              .acceptInvalidatedCard();

      cardSelectionManager.prepareSelection(isoCardSelector, calypsoCardSelectionExtension);

      CardSelectionResult selectionResult =
          cardSelectionManager.processCardSelectionScenario(cardReader);

      if (selectionResult.getActiveSmartCard() != null) {

        CalypsoCard calypsoCard = (CalypsoCard) selectionResult.getActiveSmartCard();

        cardTransactionManager =
            calypsoCardService
                .getCalypsoCardApiFactory()
                .createFreeTransactionManager(cardReader, calypsoCard);
        cardTransactionManager
            .prepareGetData(GetDataTag.TRACEABILITY_INFORMATION)
            .processCommands(ChannelControl.KEEP_OPEN); // FIXME Check if CLOSE_AFTER is needed here (Android NFC)

        return calypsoCard.getTraceabilityInformation();
      }
    }

    return null; // NOSONAR
  }

  public static void getApplicationsData(String aid, List<CardApplicationData> cardAppDataList) {

    CardApplicationData cardAppData =
        getApplicationData(CommonIsoCardSelector.FileOccurrence.FIRST, aid);

    while (cardAppData != null) {
      cardAppDataList.add(cardAppData);
      cardAppData = getApplicationData(CommonIsoCardSelector.FileOccurrence.NEXT, aid);
    }
  }

  public static boolean initReaders(String readerNameRegex) {

    Plugin plugin = smartCardService.registerPlugin(PcscPluginFactoryBuilder.builder().build());

    smartCardService.checkCardExtension(calypsoCardService);

    String pcscContactlessCardReaderName = ToolUtils.getCardReaderName(plugin, readerNameRegex);
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

    String readerNameRegex;
    if (args.length == 1) {
      readerNameRegex = args[0];
    } else {
      readerNameRegex = ToolUtils.DEFAULT_CARD_READER_NAME_REGEX;
    }

    boolean isCardPresent = initReaders(readerNameRegex);

    if (isCardPresent) {

      // - GEN RT TEST (91h) - , - GEN SV TEST (92h) - , - Hoplink -, - Ndef -,  - MF -, - RT -, -
      // SV -, - GEN AID -
      List<String> aidList =
          Arrays.asList(
              "A0000004040125009101",
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
              traceabilityInfo, SOFTWARE_INFORMATION, new Date(), 2, SOFTWARE_NAME);

      for (String aid : aidList) {
        getApplicationsData(aid, cardStructureData.getApplicationList());
      }

      try {
        Gson gson =
            new GsonBuilder()
                .registerTypeHierarchyAdapter(byte[].class, new ToolUtils.HexTypeAdapter())
                .setPrettyPrinting()
                .create();

        String dateString = new SimpleDateFormat("yyyyMMdd").format(new Date());

        String fileName =
            dateString
                + "_CardData_"
                + cardStructureData.getApplicationList().get(0).getCsnDec()
                + ".json";

        cardStructureData.setId(fileName);

        String jsonToPrint = gson.toJson(cardStructureData);

        FileWriter fw = new FileWriter(fileName);
        fw.write(jsonToPrint);
        fw.close();

      } catch (Exception e) {
        logger.error("Exception while writing the report: {}", e.getMessage(), e);
      }

      cardStructureData.print(logger);
    } else {
      logger.error("No card is present in the reader.");
    }
    System.exit(0);
  }
}

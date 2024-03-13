/* **************************************************************************************
 * Copyright (c) 2018 Calypso Networks Association https://calypsonet.org/
 *
 * See the NOTICE file(s) distributed with this work for additional information
 * regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 ************************************************************************************** */
package org.cna.keyple.tool.calypso.carddata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.calypsonet.terminal.calypso.WriteAccessLevel;
import org.calypsonet.terminal.calypso.card.CalypsoCard;
import org.cna.keyple.tool.calypso.common.ToolUtils;
import org.eclipse.keyple.core.util.HexUtil;
import org.slf4j.Logger;

public class CardApplicationData {

  public class Issuer {

    private String value;

    private String name;

    public Issuer(byte inValue) {

      value = String.format("%02X", inValue);

      name = new String(ToolUtils.getIssuerName(inValue));
    }

    public String getValue() {
      return value;
    }

    public String getName() {
      return name;
    }
  }

  private byte[] fci;

  private String calypsoRevision;

  private byte[] aid;

  private byte[] csn;

  private long csnDec;

  private String sessionModif;

  private int sessionModifDec;

  private int bufferSize;

  private String platform;

  private String applicationType;

  private String applicationSubtype;

  private Issuer issuer;

  private String version;

  private String revision;

  private String transactionCounter;

  private long transactionCounterDec;

  private AccessConditions accessConditions;

  private String status;

  private String kif1;

  private String kif2;

  private String kif3;

  private String kvc1;

  private String kvc2;

  private String kvc3;

  private String lid;

  List<CardFileData> fileList = null;

  private static int getSessionBufferSize(int sessionModification) {

    switch (sessionModification) {
      case 6:
        return 215;

      case 7:
        return 256;

      case 8:
        return 304;

      case 9:
        return 362;

      case 10:
        return 430;

      case 11:
        return 512;

      case 12:
        return 608;

      case 13:
        return 724;

      case 14:
        return 861;

      case 15:
        return 1024;

      default:
        return 0;
    }
  }

  public CardApplicationData(CalypsoCard appData) {

    csn =
        Arrays.copyOf(
            appData.getApplicationSerialNumber(), appData.getApplicationSerialNumber().length);

    csnDec = ToolUtils.bytesToLong(appData.getApplicationSerialNumber(), 8);

    calypsoRevision = new String(appData.getProductType().toString());

    sessionModif = String.format("%04X", appData.getSessionModification());

    sessionModifDec = appData.getSessionModification();

    bufferSize = getSessionBufferSize(appData.getSessionModification());

    transactionCounter = "Not available"; // String.format("%06X", appData.getTransactionCounter());

    transactionCounterDec = 0; // appData.getTransactionCounter();

    platform = String.format("%02X", appData.getPlatform());

    issuer = new Issuer(appData.getSoftwareIssuer());

    version = String.format("%02X", appData.getSoftwareVersion());

    revision = String.format("%02X", appData.getSoftwareRevision());

    fci =
        Arrays.copyOf(
            appData.getSelectApplicationResponse(), appData.getSelectApplicationResponse().length);

    aid = Arrays.copyOf(appData.getDfName(), appData.getDfName().length);

    lid = String.format("%04X", appData.getDirectoryHeader().getLid());

    kif1 =
        String.format(
            "%02X", appData.getDirectoryHeader().getKif(WriteAccessLevel.PERSONALIZATION));

    kif2 = String.format("%02X", appData.getDirectoryHeader().getKif(WriteAccessLevel.LOAD));

    kif3 = String.format("%02X", appData.getDirectoryHeader().getKif(WriteAccessLevel.DEBIT));

    kvc1 =
        String.format(
            "%02X", appData.getDirectoryHeader().getKvc(WriteAccessLevel.PERSONALIZATION));

    kvc2 = String.format("%02X", appData.getDirectoryHeader().getKvc(WriteAccessLevel.LOAD));

    kvc3 = String.format("%02X", appData.getDirectoryHeader().getKvc(WriteAccessLevel.DEBIT));

    status = String.format("%02X", appData.getDirectoryHeader().getDfStatus());

    accessConditions =
        new AccessConditions(
            appData.getDirectoryHeader().getAccessConditions(),
            appData.getDirectoryHeader().getKeyIndexes());

    applicationType = String.format("%02X", appData.getApplicationType());

    applicationSubtype = String.format("%02X", appData.getApplicationSubtype());

    fileList = new ArrayList<CardFileData>();
  }

  public void print(Logger logger) {

    logger.info(
        "========================================================================================================");
    logger.info(
        "| AID                             | LID  | KVC1 | KVC2 | KVC3 | KIF1 | KIF2 | KIF3 | G0 | G1 | G2 | G3 |");
    logger.info(
        "{}",
        String.format(
            "|%32s | %4s |  %2s  |  %2s  |  %2s  |  %2s  |  %2s  |  %2s  | %s | %s | %s | %s |",
            HexUtil.toHex(this.getAid()),
            this.getLid(),
            this.getKvc1(),
            this.getKvc2(),
            this.getKvc3(),
            this.getKif1(),
            this.getKif2(),
            this.getKif3(),
            ToolUtils.getAcName(
                this.getAccessConditions().getGroup0().getAccessCondition(),
                this.getAccessConditions().getGroup0().getKeyLevel(),
                false),
            ToolUtils.getAcName(
                this.getAccessConditions().getGroup1().getAccessCondition(),
                this.getAccessConditions().getGroup1().getKeyLevel(),
                false),
            ToolUtils.getAcName(
                this.getAccessConditions().getGroup2().getAccessCondition(),
                this.getAccessConditions().getGroup2().getKeyLevel(),
                false),
            ToolUtils.getAcName(
                this.getAccessConditions().getGroup3().getAccessCondition(),
                this.getAccessConditions().getGroup3().getKeyLevel(),
                false)));
    logger.info(
        "========================================================================================================");

    logger.info("{}", String.format("= FCI:: %s", HexUtil.toHex(this.getFci())));
    logger.info(
        "{}",
        String.format("= Serial Number:: %s (%d)", HexUtil.toHex(this.getCsn()), this.getCsnDec()));
    logger.info("{}", String.format("= Transaction Counter:: %d", this.getTransactionCounterDec()));
    logger.info("{}", String.format("= Revision:: %s", this.getCalypsoRevision()));
    logger.info("{}", String.format("= Session Buffer Size:: %d bytes", this.getBufferSize()));
    logger.info("{}", String.format("= Platform (Chip Type):: %s", this.getPlatform()));

    if (this.getIssuerInfo() != null) {
      logger.info(
          "{}",
          String.format(
              "= Issuer:: %s(%s)",
              this.getIssuerInfo().getName(), this.getIssuerInfo().getValue()));
    } else {
      logger.info("= Issuer:: null");
    }

    logger.info(
        "{}", String.format("= Software Version:: %s.%s", this.getVersion(), this.getRevision()));
    logger.info("{}", String.format("= Application Type:: %s", this.getApplicationType()));
    logger.info("{}", String.format("= Application Subtype:: %s", this.getApplicationSubtype()));
    logger.info("{}", String.format("= DF Status:: %2s", this.getStatus()));
    logger.info(
        "========================================================================================================");

    logger.info("| LID  | Type | SID | #R | Size | G0 | G1 | G2 | G3 | DRef |");
    logger.info("----------------------------------------------------------");

    List<CardFileData> fileList = this.getFileList();
    Iterator fileIter = fileList.iterator();

    while (fileIter.hasNext()) {

      CardFileData fileData = (CardFileData) fileIter.next();

      fileData.print(logger);
    }
  }

  public byte[] getFci() {
    return fci;
  }

  public byte[] getCsn() {
    return csn;
  }

  public long getCsnDec() {
    return csnDec;
  }

  public String getCalypsoRevision() {
    return calypsoRevision;
  }

  public String getSessionModif() {
    return sessionModif;
  }

  public int getSessionModifDec() {
    return sessionModifDec;
  }

  public int getBufferSize() {
    return bufferSize;
  }

  public String getTransactionCounter() {
    return transactionCounter;
  }

  public long getTransactionCounterDec() {
    return transactionCounterDec;
  }

  public String getPlatform() {
    return platform;
  }

  public Issuer getIssuerInfo() {
    return issuer;
  }

  public Issuer getIssuer() {
    return issuer;
  }

  public String getVersion() {
    return version;
  }

  public String getRevision() {
    return revision;
  }

  public byte[] getAid() {
    return aid;
  }

  public String getLid() {
    return lid;
  }

  public String getKif1() {
    return kif1;
  }

  public String getKif2() {
    return kif2;
  }

  public String getKif3() {
    return kif3;
  }

  public String getKvc1() {
    return kvc1;
  }

  public String getKvc2() {
    return kvc2;
  }

  public String getKvc3() {
    return kvc3;
  }

  public AccessConditions getAccessConditions() {
    return accessConditions;
  }

  public String getApplicationType() {
    return applicationType;
  }

  public String getApplicationSubtype() {
    return applicationSubtype;
  }

  public String getStatus() {
    return status;
  }

  public List<CardFileData> getFileList() {
    return fileList;
  }
}

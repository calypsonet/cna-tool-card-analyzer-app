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
package org.calypsonet.tool.calypso.carddata;

import static org.calypsonet.tool.calypso.common.ToolUtils.SEPARATOR_LINE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.calypsonet.tool.calypso.common.ToolUtils;
import org.eclipse.keyple.core.util.ByteArrayUtil;
import org.eclipse.keyple.core.util.HexUtil;
import org.eclipse.keypop.calypso.card.WriteAccessLevel;
import org.eclipse.keypop.calypso.card.card.CalypsoCard;
import org.slf4j.Logger;

/**
 * All data related to a card application.
 *
 * @since 2.0.0
 */
public class CardApplicationData {

  public static class Issuer {

    private final String value;

    private final String name;

    public Issuer(byte inValue) {

      value = HexUtil.toHex(inValue);

      name = ToolUtils.getIssuerName(inValue);
    }

    public String getValue() {
      return value;
    }

    public String getName() {
      return name;
    }
  }

  private final byte[] fci;

  private final String calypsoRevision;

  private final byte[] aid;

  private final byte[] csn;

  private final long csnDec;

  private final String sessionModif;

  private final int sessionModifDec;

  private final int bufferSize;

  private final String platform;

  private final String applicationType;

  private final String applicationSubtype;

  private final Issuer issuer;

  private final String version;

  private final String revision;

  private final String transactionCounter;

  private final long transactionCounterDec;

  private final AccessConditions accessConditions;

  private final String status;

  private final String kif1;

  private final String kif2;

  private final String kif3;

  private final String kvc1;

  private final String kvc2;

  private final String kvc3;

  private final String lid;

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

    csnDec = ByteArrayUtil.extractLong(appData.getApplicationSerialNumber(), 0, 8, false);

    calypsoRevision = appData.getProductType().toString();

    sessionModif = ToolUtils.padLeft(HexUtil.toHex(appData.getSessionModification()), 4, '0');

    sessionModifDec = appData.getSessionModification();

    bufferSize = getSessionBufferSize(appData.getSessionModification());

    transactionCounter = "Not available"; // String.format("%06X", appData.getTransactionCounter());

    transactionCounterDec = 0; // appData.getTransactionCounter();

    platform = HexUtil.toHex(appData.getPlatform());

    issuer = new Issuer(appData.getSoftwareIssuer());

    version = HexUtil.toHex(appData.getSoftwareVersion());

    revision = HexUtil.toHex(appData.getSoftwareRevision());

    fci =
        Arrays.copyOf(
            appData.getSelectApplicationResponse(), appData.getSelectApplicationResponse().length);

    aid = Arrays.copyOf(appData.getDfName(), appData.getDfName().length);

    lid = HexUtil.toHex(appData.getDirectoryHeader().getLid());

    kif1 = HexUtil.toHex(appData.getDirectoryHeader().getKif(WriteAccessLevel.PERSONALIZATION));
    kif2 = HexUtil.toHex(appData.getDirectoryHeader().getKif(WriteAccessLevel.LOAD));
    kif3 = HexUtil.toHex(appData.getDirectoryHeader().getKif(WriteAccessLevel.DEBIT));

    kvc1 = HexUtil.toHex(appData.getDirectoryHeader().getKvc(WriteAccessLevel.PERSONALIZATION));
    kvc2 = HexUtil.toHex(appData.getDirectoryHeader().getKvc(WriteAccessLevel.LOAD));
    kvc3 = HexUtil.toHex(appData.getDirectoryHeader().getKvc(WriteAccessLevel.DEBIT));

    status = HexUtil.toHex(appData.getDirectoryHeader().getDfStatus());

    accessConditions =
        new AccessConditions(
            appData.getDirectoryHeader().getAccessConditions(),
            appData.getDirectoryHeader().getKeyIndexes());

    applicationType = HexUtil.toHex(appData.getApplicationType());

    applicationSubtype = HexUtil.toHex(appData.getApplicationSubtype());

    fileList = new ArrayList<>();
  }

  public void print(Logger logger) {

    logger.info(SEPARATOR_LINE);
    String paddedAid = ToolUtils.padLeft(HexUtil.toHex(this.getAid()), 32, ' ');
    String paddedLid = ToolUtils.padLeft(this.getLid(), 4, '0');
    String group0 =
        ToolUtils.getAcName(
            this.getAccessConditions().getGroup0().getAccessCondition(),
            this.getAccessConditions().getGroup0().getKeyLevel(),
            false);
    String group1 =
        ToolUtils.getAcName(
            this.getAccessConditions().getGroup1().getAccessCondition(),
            this.getAccessConditions().getGroup1().getKeyLevel(),
            false);
    String group2 =
        ToolUtils.getAcName(
            this.getAccessConditions().getGroup2().getAccessCondition(),
            this.getAccessConditions().getGroup2().getKeyLevel(),
            false);
    String group3 =
        ToolUtils.getAcName(
            this.getAccessConditions().getGroup3().getAccessCondition(),
            this.getAccessConditions().getGroup3().getKeyLevel(),
            false);
    logger.info(
        "| AID                             | LID  | KVC1 | KVC2 | KVC3 | KIF1 | KIF2 | KIF3 | G0 | G1 | G2 | G3 |");
    logger.info(
        "|{} | {} |  {}  |  {}  |  {}  |  {}  |  {}  |  {}  | {} | {} | {} | {} |",
        paddedAid,
        paddedLid,
        this.getKvc1(),
        this.getKvc2(),
        this.getKvc3(),
        this.getKif1(),
        this.getKif2(),
        this.getKif3(),
        group0,
        group1,
        group2,
        group3);
    logger.info(SEPARATOR_LINE);

    logger.info("= FCI:: {}", HexUtil.toHex(this.getFci()));
    logger.info("= Serial Number:: {} ({})", HexUtil.toHex(this.getCsn()), this.getCsnDec());
    logger.info("= Transaction Counter:: {}", this.getTransactionCounterDec());
    logger.info("= Revision:: {}", this.getCalypsoRevision());
    logger.info("= Session Buffer Size:: {} bytes", this.getBufferSize());
    logger.info("= Platform (Chip Type):: {}", this.getPlatform());

    if (this.getIssuerInfo() != null) {
      logger.info(
          "= Issuer:: {} ({})", this.getIssuerInfo().getName(), this.getIssuerInfo().getValue());
    } else {
      logger.info("= Issuer:: null");
    }
    logger.info("= Software Version:: {}.{}", this.getVersion(), this.getRevision());
    logger.info("= Application Type:: {}", this.getApplicationType());
    logger.info("= Application Subtype:: {}", this.getApplicationSubtype());
    logger.info("= DF Status:: {}", this.getStatus());
    logger.info(SEPARATOR_LINE);

    logger.info("| LID  | Type | SID | #R | Size | G0 | G1 | G2 | G3 | DRef |");
    logger.info("----------------------------------------------------------");

    for (CardFileData fileData : this.getFileList()) {

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

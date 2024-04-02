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

import java.util.ArrayList;
import java.util.List;
import org.calypsonet.tool.calypso.common.ToolUtils;
import org.eclipse.keyple.core.util.HexUtil;
import org.eclipse.keypop.calypso.card.card.ElementaryFile;
import org.slf4j.Logger;

/**
 * All data related to a file of the card.
 *
 * @since 2.0.0
 */
public class CardFileData {

  private final String sfi;

  private final String lid;

  private final String efType;

  private final String ref;

  private final String recSize;

  private final int recSizeDec;

  private final String numRec;

  private final int numRecDec;

  private final AccessConditions accessConditions;

  private final List<RecordData> recordDataList;

  public CardFileData(ElementaryFile fileInfo) {

    int efTypeValue = ToolUtils.getEfTypeIntValue(fileInfo.getHeader().getEfType());

    lid = HexUtil.toHex(fileInfo.getHeader().getLid());

    sfi = HexUtil.toHex(fileInfo.getSfi());

    efType = HexUtil.toHex(efTypeValue);

    numRec = HexUtil.toHex(fileInfo.getHeader().getRecordsNumber());

    numRecDec = fileInfo.getHeader().getRecordsNumber();

    recSize = ToolUtils.padLeft(String.valueOf(fileInfo.getHeader().getRecordSize()), 4, '0');

    recSizeDec = fileInfo.getHeader().getRecordSize();

    accessConditions =
        new AccessConditions(
            fileInfo.getHeader().getAccessConditions(), fileInfo.getHeader().getKeyIndexes());

    recordDataList = new ArrayList<>();

    ref = HexUtil.toHex(fileInfo.getHeader().getSharedReference());
  }

  public String getLid() {
    return lid;
  }

  public String getSfi() {
    return sfi;
  }

  public String getEfType() {
    return efType;
  }

  public String getNumRec() {
    return numRec;
  }

  public int getNumRecDec() {
    return numRecDec;
  }

  public String getRecSize() {
    return recSize;
  }

  public int getRecSizeDec() {
    return recSizeDec;
  }

  public AccessConditions getAccessConditions() {
    return accessConditions;
  }

  public String getDataRef() {
    return ref;
  }

  public List<RecordData> getRecordDataList() {
    return recordDataList;
  }

  public void print(Logger logger) {

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
        "| {} | {} | {}  | {} | {} | {} | {} | {} | {}| {} |",
        this.getLid(),
        ToolUtils.getEfTypeName(this.getEfType(), false),
        this.getSfi(),
        ToolUtils.padLeft(String.valueOf(this.getNumRecDec()), 2, '0'),
        ToolUtils.padLeft(String.valueOf(this.getRecSizeDec()), 4, '0'),
        group0,
        group1,
        group2,
        group3,
        this.getDataRef());

    if (getRecordDataList() != null) {
      for (RecordData recordData : getRecordDataList()) {
        logger.info("+ #{}:{}", recordData.getIndex(), HexUtil.toHex(recordData.getValue()));
      }
    }
  }
}

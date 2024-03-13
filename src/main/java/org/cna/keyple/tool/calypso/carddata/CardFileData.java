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
package org.cna.keyple.tool.calypso.carddata;

import java.util.ArrayList;
import java.util.List;
import org.calypsonet.terminal.calypso.card.ElementaryFile;
import org.cna.keyple.tool.calypso.common.ToolUtils;
import org.eclipse.keyple.core.util.HexUtil;
import org.slf4j.Logger;

public class CardFileData {

  private String sfi;

  private String lid;

  private String efType;

  private String efTypeName;

  private String ref;

  private String recSize;

  private int recSizeDec;

  private String numRec;

  private int numRecDec;

  private AccessConditions accessConditions;

  private List<RecordData> recordData;

  public CardFileData(ElementaryFile fileInfo) {

    int efTypeValue = ToolUtils.getEfTypeIntValue(fileInfo.getHeader().getEfType());

    lid = String.format("%04X", fileInfo.getHeader().getLid());

    sfi = String.format("%02X", fileInfo.getSfi());

    efType = String.format("%02X", efTypeValue);

    efTypeName = ToolUtils.getEfTypeName(efTypeValue, true);

    numRec = String.format("%02X", fileInfo.getHeader().getRecordsNumber());

    numRecDec = fileInfo.getHeader().getRecordsNumber();

    recSize = String.format("%04X", fileInfo.getHeader().getRecordSize());

    recSizeDec = fileInfo.getHeader().getRecordSize();

    accessConditions =
        new AccessConditions(
            fileInfo.getHeader().getAccessConditions(), fileInfo.getHeader().getKeyIndexes());

    recordData = new ArrayList<RecordData>();

    ref = String.format("%04X", fileInfo.getHeader().getSharedReference());
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

  public String getFileTypeName() {
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

  public List<RecordData> getRecordData() {
    return recordData;
  }

  public void print(Logger logger) {

    logger.info(
        "{}",
        String.format(
            "| %4s | %s | %2s  | %2d | %4d | %s | %s | %s | %s | %4s |",
            this.getLid(),
            ToolUtils.getEfTypeName(this.getEfType(), false),
            this.getSfi(),
            this.getNumRecDec(),
            this.getRecSizeDec(),
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
                false),
            this.getDataRef()));

    for (int i = 0; this.getRecordData() != null && i < this.getRecordData().size(); i++) {
      logger.info(
          "{}",
          String.format(
              "+ #%s:%s",
              this.getRecordData().get(i).getIndex(),
              HexUtil.toHex(this.getRecordData().get(i).getValue())));
    }
  }
}

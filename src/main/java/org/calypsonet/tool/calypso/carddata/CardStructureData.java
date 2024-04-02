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

import java.text.SimpleDateFormat;
import java.util.*;
import org.calypsonet.tool.calypso.common.ToolUtils;
import org.eclipse.keyple.core.util.HexUtil;
import org.slf4j.Logger;

/**
 * Structure of a card's data.
 *
 * @since 2.0.0
 */
public class CardStructureData {

  private String id;

  private final String infos;

  private final String date;

  private final int version;

  private final String software;

  private final byte[] traceability;

  private final List<CardApplicationData> applicationList;

  public CardStructureData(
      byte[] traceabilityInfo,
      String softwareInfo,
      Date creationDate,
      int softwareVersion,
      String softwareName) {

    traceability = Arrays.copyOf(traceabilityInfo, traceabilityInfo.length);

    infos = softwareInfo;

    date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(creationDate);

    version = softwareVersion;

    software = softwareName;

    applicationList = new ArrayList<>();
  }

  public List<CardApplicationData> getApplicationList() {
    return applicationList;
  }

  public void print(Logger logger) {

    logger.info(ToolUtils.SEPARATOR_LINE);
    logger.info("= Id:: {}", this.getId());
    logger.info("= Date:: {}", this.getDate());
    logger.info("= Version:: {}", ToolUtils.padLeft(String.valueOf(this.getVersion()), 3, '0'));
    logger.info("= Software:: {}", this.getSoftware());
    logger.info("= Traceability:: {}", HexUtil.toHex(this.getTraceability()));

    for (CardApplicationData applicationData : this.getApplicationList()) {
      applicationData.print(logger);
    }

    logger.info(ToolUtils.SEPARATOR_LINE);
  }

  public byte[] getTraceability() {
    return traceability;
  }

  public String getId() {
    return id;
  }

  public String getDate() {
    return date;
  }

  public int getVersion() {
    return version;
  }

  public String getSoftware() {
    return software;
  }

  public String getInfos() {
    return infos;
  }

  public void setId(String id) {
    this.id = id;
  }
}

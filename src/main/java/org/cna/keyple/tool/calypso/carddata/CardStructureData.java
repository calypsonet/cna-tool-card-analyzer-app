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

import java.text.SimpleDateFormat;
import java.util.*;
import org.eclipse.keyple.core.util.HexUtil;
import org.slf4j.Logger;

public class CardStructureData {

  private String id;

  private String infos;

  private String date;

  private int version;

  private String software;

  private byte[] traceability;

  private List<CardApplicationData> applicationList = null;

  public CardStructureData(
      byte[] traceabilityInfo,
      String softwareInfo,
      Date creationDate,
      int softwareVersion,
      String softwareName) {

    traceability = Arrays.copyOf(traceabilityInfo, traceabilityInfo.length);

    infos = new String(softwareInfo);

    String dateString =
        new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(creationDate.getTime()));

    date = new String(dateString);

    version = softwareVersion;

    software = new String(softwareName);

    applicationList = new ArrayList<CardApplicationData>();
  }

  public List<CardApplicationData> getApplicationList() {
    return applicationList;
  }

  public void print(Logger logger) {

    logger.info(
        "========================================================================================================");
    logger.info("{}", String.format("= Id:: %s", this.getId()));
    logger.info("{}", String.format("= Date:: %s", this.getDate()));
    logger.info("{}", String.format("= Version:: %03d", this.getVersion()));
    logger.info("{}", String.format("= Software:: %s", this.getSoftware()));
    logger.info("{}", String.format("= Traceability:: %s", HexUtil.toHex(this.getTraceability())));

    List<CardApplicationData> applicationList = this.getApplicationList();
    Iterator appIter = applicationList.iterator();

    while (appIter.hasNext()) {
      CardApplicationData applicationData = (CardApplicationData) appIter.next();

      applicationData.print(logger);
    }
    logger.info(
        "========================================================================================================");
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

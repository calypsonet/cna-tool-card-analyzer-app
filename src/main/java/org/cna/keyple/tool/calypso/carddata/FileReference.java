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

public class FileReference {

  private String baseFileLid;

  private String linkedFileLid;

  private String refValueRead;

  private Boolean referenceFoundFlag;

  public FileReference(String baseLid, String linkedLid, String refValue) {

    this.baseFileLid = new String(baseLid);

    this.linkedFileLid = new String(linkedLid);

    this.refValueRead = new String(refValue);

    this.referenceFoundFlag = false;
  }

  public String getBaseFileLid() {
    return baseFileLid;
  }

  public String getLinkedFileLid() {
    return linkedFileLid;
  }

  public String getRefValueRead() {
    return refValueRead;
  }

  public Boolean getReferenceFoundFlag() {
    return referenceFoundFlag;
  }

  public void setReferenceFoundFlag(Boolean referenceFoundFlag) {
    this.referenceFoundFlag = referenceFoundFlag;
  }
}

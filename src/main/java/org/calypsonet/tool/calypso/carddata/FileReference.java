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

/**
 * Contains information about a file: the LID, the linked file LID, the reference value read, and a
 * flag indicating whether the reference has been found.
 *
 * @since 2.0.0
 */
public class FileReference {

  private final String baseFileLid;

  private final String linkedFileLid;

  private final String refValueRead;

  private boolean referenceFoundFlag;

  public FileReference(String baseLid, String linkedLid, String refValue) {

    this.baseFileLid = baseLid;

    this.linkedFileLid = linkedLid;

    this.refValueRead = refValue;

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

  public boolean getReferenceFoundFlag() {
    return referenceFoundFlag;
  }

  public void setReferenceFoundFlag(boolean referenceFoundFlag) {
    this.referenceFoundFlag = referenceFoundFlag;
  }
}

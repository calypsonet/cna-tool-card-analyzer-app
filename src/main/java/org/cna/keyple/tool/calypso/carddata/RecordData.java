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

import java.util.Arrays;
import org.eclipse.keyple.core.util.HexUtil;

/**
 * Represents a record data including index and value.
 *
 * @since 2.0.0
 */
public class RecordData {

  private final String index;

  private final byte[] value;

  public RecordData(int recordIndex, byte[] recordData) {

    index = HexUtil.toHex(recordIndex);

    value = Arrays.copyOf(recordData, recordData.length);
  }

  public String getIndex() {
    return index;
  }

  public byte[] getValue() {
    return value;
  }
}

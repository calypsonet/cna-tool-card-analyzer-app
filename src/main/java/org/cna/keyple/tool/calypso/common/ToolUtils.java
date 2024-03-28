/* **************************************************************************************
 * Copyright (c) 2022 Calypso Networks Association https://calypsonet.org/
 *
 * See the NOTICE file(s) distributed with this work for additional information
 * regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 ************************************************************************************** */
package org.cna.keyple.tool.calypso.common;

import com.google.gson.*;
import java.lang.reflect.Type;
import org.eclipse.keyple.core.service.Plugin;
import org.eclipse.keyple.core.util.HexUtil;
import org.eclipse.keypop.calypso.card.card.ElementaryFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for various tool functions.
 *
 * @since 2.0.0
 */
public class ToolUtils {

  private static final Logger logger = LoggerFactory.getLogger(ToolUtils.class);
  public static final String SEPARATOR_LINE =
      "========================================================================================================";

  public static final String CARD_READER_NAME_REGEX = ".*(ASK.*|Identiv.*2|ACS ACR122U|SCR3310).*";
  public static final String ISO_CARD_PROTOCOL = "ISO_14443_4_CARD";
  public static final String SAM_READER_NAME_REGEX =
      ".*(Cherry TC|Identiv.*|SCM Microsystems|HID|Generic|ACS).*";

  public static final int CARD_EF_TYPE_BINARY = 1;
  public static final int CARD_EF_TYPE_LINEAR = 2;
  public static final int CARD_EF_TYPE_CYCLIC = 4;
  public static final int CARD_EF_TYPE_SIMULATED_COUNTERS = 8;
  public static final int CARD_EF_TYPE_COUNTERS = 9;

  private ToolUtils() {}

  public static String getCardReaderName(Plugin plugin, String readerNameRegex) {
    for (String readerName : plugin.getReaderNames()) {
      if (readerName.matches(readerNameRegex)) {
        logger.info("Card reader, plugin; {}, name: {}", plugin.getName(), readerName);
        return readerName;
      }
    }
    throw new IllegalStateException(
        "Reader '" + readerNameRegex + "' not found in plugin '" + plugin.getName() + "'");
  }

  public static String getEfTypeName(String inEfType, boolean longModeFlag) {
    int efType = Integer.valueOf(inEfType, 16);

    return getEfTypeName(efType, longModeFlag);
  }

  public static String getEfTypeName(int inEfType, boolean longModeFlag) {

    switch (inEfType) {
      case CARD_EF_TYPE_BINARY:
        {
          return (longModeFlag ? "Binary" : "Bin ");
        }

      case CARD_EF_TYPE_LINEAR:
        {
          return (longModeFlag ? "Linear" : "Lin ");
        }

      case CARD_EF_TYPE_CYCLIC:
        {
          return (longModeFlag ? "Cyclic" : "Cycl");
        }

      case CARD_EF_TYPE_SIMULATED_COUNTERS:
        {
          return (longModeFlag ? "SimulatedCounter" : "SimC");
        }

      case CARD_EF_TYPE_COUNTERS:
        {
          return (longModeFlag ? "Counter" : "Cnt ");
        }

      default:
        {
          return "--";
        }
    }
  }

  public static int getEfTypeIntValue(ElementaryFile.Type inType) {

    switch (inType) {
      case LINEAR:
        {
          return CARD_EF_TYPE_LINEAR;
        }

      case BINARY:
        {
          return CARD_EF_TYPE_BINARY;
        }

      case CYCLIC:
        {
          return CARD_EF_TYPE_CYCLIC;
        }

      case COUNTERS:
        {
          return CARD_EF_TYPE_COUNTERS;
        }

      case SIMULATED_COUNTERS:
        {
          return CARD_EF_TYPE_SIMULATED_COUNTERS;
        }

      default:
        {
          return 0;
        }
    }
  }

  public static String getAcName(String inAcValue, String inKeyLevel, boolean longModeFlag) {
    int acValue = Integer.valueOf(inAcValue, 16);
    int keyLevel = Integer.valueOf(inKeyLevel, 16);

    return getAcName(acValue, keyLevel, longModeFlag);
  }

  public static String getAcName(int inAcValue, int inKeyLevel, boolean longModeFlag) {

    switch (inAcValue) {
      case 0x1F:
        {
          return (longModeFlag ? "Always" : "AA");
        }

      case 0x00:
        {
          return (longModeFlag ? "Never" : "NN");
        }

      case 0x10:
        {
          return (longModeFlag ? ("Session" + inKeyLevel) : ("S" + inKeyLevel));
        }

      case 0x01:
        {
          return (longModeFlag ? "PIN" : "PN");
        }

      case 0x14:
        {
          return (longModeFlag ? "Confidential" + inKeyLevel : "C" + inKeyLevel);
        }

      case 0x15:
        {
          return (longModeFlag ? "Confidential&PIN" + inKeyLevel : "P" + inKeyLevel);
        }
      default:
        {
          return "--";
        }
    }
  }

  public static String getIssuerName(byte inIssuer) {

    switch (inIssuer) {
      case 0x00:
        {
          return "Paragon Id";
        }

      case 0x01:
        {
          return "Intec";
        }

      case 0x02:
        {
          return "Calypso";
        }

      case 0x04:
        {
          return "Thales";
        }

      case 0x05:
      case 0x0A:
        {
          return "Idemia";
        }

      case 0x06:
        {
          return "Axalto";
        }

      case 0x07:
        {
          return "Bull";
        }

      case 0x08:
        {
          return "Spirtech";
        }

      case 0x09:
        {
          return "BMS";
        }

      case 0x0B:
        {
          return "Gemplus";
        }

      case 0x0C:
        {
          return "Magnadata";
        }

      case 0x0D:
        {
          return "Calmell";
        }

      case 0x0E:
        {
          return "Mecstar";
        }

      case 0x0F:
        {
          return "ACG Identification";
        }

      case 0x10:
        {
          return "STMicroelectronics";
        }

      case 0x11:
        {
          return "CNA";
        }

      case 0x12:
        {
          return "G&D";
        }

      case 0x13:
        {
          return "OTI";
        }

      case 0x14:
        {
          return "Gemalto";
        }

      case 0x15:
        {
          return "Watchdata";
        }

      case 0x16:
        {
          return "Alios";
        }

      case 0x17:
        {
          return "S-P-S";
        }

      case 0x18:
        {
          return "ISRA";
        }

      case 0x19:
        {
          return "Trust Electronics";
        }

      case 0x1A:
        {
          return "Trusted Labs";
        }

      case 0x1B:
        {
          return "Neowave";
        }

      case 0x1C:
        {
          return "Digital People";
        }

      case 0x1D:
        {
          return "ABNote Europe";
        }

      case 0x1E:
        {
          return "Twinlinx";
        }

      case 0x1F:
        {
          return "Inteligensa";
        }

      case 0x20:
        {
          return "CNA";
        }

      case 0x21:
        {
          return "Innovatron";
        }

      case 0x22:
        {
          return "Austria Card";
        }

      case 0x23:
        {
          return "Carta+";
        }

      case 0x24:
        {
          return "Impimerie Nationale";
        }

      case 0x25:
      case 0x29:
        {
          return "HID Global";
        }

      case 0x26:
        {
          return "Card Project";
        }

      case 0x27:
        {
          return "PosteMobile";
        }

      case 0x28:
        {
          return "HB Technologies";
        }

      case 0x2A:
        {
          return "ANY Security Printing";
        }

      case 0x2B:
        {
          return "SELP";
        }

      case 0x2C:
        {
          return "Future Card";
        }

      case 0x2D:
        {
          return "iQuantics";
        }

      case 0x2E:
        {
          return "Calypso";
        }

      case 0x2F:
        {
          return "Aruba PEC";
        }

      default:
        {
          return "--";
        }
    }
  }

  public static class HexTypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {

    public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {
      return HexUtil.toByteArray(json.getAsString());
    }

    public JsonElement serialize(byte[] data, Type typeOfSrc, JsonSerializationContext context) {
      return new JsonPrimitive(HexUtil.toHex(data));
    }
  }

  public static String padLeft(String input, int length, char padChar) {
    if (length <= input.length()) {
      return input;
    }
    StringBuilder sb = new StringBuilder();
    for (int i = input.length(); i < length; i++) {
      sb.append(padChar);
    }
    sb.append(input);
    return sb.toString();
  }

  /*
      public static boolean unlockSam(SamResource samResource, byte[] unlockData)
              throws KeypleReaderException {
          // create an apdu requests list to handle SAM command
          List<ApduRequest> apduRequests = new ArrayList<ApduRequest>();

          // get the challenge from the PO
          apduRequests.add(new UnlockCmdBuild(SamRevision.C1, unlockData).getApduRequest());

          SeRequest seRequest = new SeRequest(apduRequests, ChannelState.KEEP_OPEN);

          SeResponse seResponse = ((ProxyReader) samResource.getSeReader()).transmit(seRequest);

          if (seResponse == null) {
              throw new IllegalStateException("Unlock SAM command command failed. Null response");
          }

          return seResponse.getApduResponses().get(0).isSuccessful();
      }
  */

}

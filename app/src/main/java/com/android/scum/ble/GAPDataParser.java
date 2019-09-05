package com.android.scum.ble;

import java.util.Arrays;

/**
 * This class parses the GAP data. For each data type, the first byte is the length of the data,
 * the second byte is the GAP code, and the remaining bytes are for the data.
 */
class GAPDataParser {
    private String address;
    private byte[] scanRecord;

    // Data fields
    private String name;
    private static final byte NAME_CODE = (byte) 0x08;
    private double temp;
    private static final byte TEMPERATURE_CODE = (byte) 0xC0;

    // Constants
    private static final String ADV_ADDRESS = "0002723280C6";
    private static final double KELVIN_TO_CELSIUS = -273.15;

    GAPDataParser(String address, byte[] scanRecord) {
        this.address = address;
        this.scanRecord = Arrays.copyOf(scanRecord, scanRecord.length);

        if (this.isSCUM()) {
            parseScanRecord();
        }
    }

    String getAddress() {
        return this.address;
    }

    boolean isSCUM() {
        return this.address.replace(":", "").equals(GAPDataParser.ADV_ADDRESS);
    }

    String getScanRecord() {
        StringBuilder sb = new StringBuilder();
        for (byte b : this.scanRecord) {
            sb.append(String.format("0x%02x ", b));
        }
        return sb.toString().trim();
    }

    String getName() {
        return this.name;
    }

    double getTemp() {
        return this.temp;
    }

    private void parseScanRecord() {
        int offset = 0;
        while (offset < this.scanRecord.length - 2) {
            int length = this.scanRecord[offset++];
            if (length == 0) {
                break;
            }

            byte code = this.scanRecord[offset++];
            switch (code) {
                case GAPDataParser.NAME_CODE:
                    this.name = new String(this.scanRecord, offset, length - 1);
                    offset += length - 1;
                    break;
                case GAPDataParser.TEMPERATURE_CODE:
                    StringBuilder sb = new StringBuilder((length - 1) * 2);
                    for (int i = 0; i < length - 1; ++i) {
                        sb.append(String.format("%02x", this.scanRecord[offset + i]));
                    }
                    int tempData = Integer.parseInt(sb.toString(), 16);
                    this.temp = ((double) tempData) / 100 + GAPDataParser.KELVIN_TO_CELSIUS;
                    offset += length - 1;
                    break;
                default:
                    offset += length - 1;
                    break;
            }
        }
    }
}

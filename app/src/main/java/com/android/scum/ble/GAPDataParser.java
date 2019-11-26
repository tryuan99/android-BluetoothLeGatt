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
    private boolean hasLCTuning;
    private int coarse;
    private int mid;
    private int fine;
    private static final byte LC_TUNE_CODE = (byte) 0xC0;
    private boolean hasCounters;
    private int count2M;
    private int count32k;
    private static final byte COUNTERS_CODE = (byte) 0xC2;
    private boolean hasTemperature;
    private double temperature;
    private static final byte TEMPERATURE_CODE = (byte) 0xC1;

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
        return this.address.replace(":", "").startsWith(GAPDataParser.ADV_ADDRESS.substring(0, GAPDataParser.ADV_ADDRESS.length() - 2));
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

    boolean hasLCTuning() {
        return this.hasLCTuning;
    }

    int[] getLCTuning() {
        return new int[]{this.coarse, this.mid, this.fine};
    }

    boolean hasCounters() {
        return this.hasCounters;
    }

    int getCount2M() {
        return this.count2M;
    }

    int getCount32k() {
        return this.count32k;
    }

    double getRatio() {
        return ((double) this.getCount2M()) / this.getCount32k();
    }

    boolean hasTemperature() {
        return this.hasTemperature;
    }

    double getTemperature() {
        return this.temperature;
    }

    private void parseScanRecord() {
        this.hasLCTuning = false;
        this.hasCounters = false;
        this.hasTemperature = false;

        int offset = 0;
        while (offset < this.scanRecord.length - 2) {
            int length = this.scanRecord[offset++];
            if (length == 0) {
                break;
            }

            byte code = this.scanRecord[offset++];
            switch (code) {
                case GAPDataParser.NAME_CODE: {
                    this.name = new String(this.scanRecord, offset, length - 1);
                    offset += length - 1;
                    break;
                }
                case GAPDataParser.LC_TUNE_CODE: {
                    this.hasLCTuning = true;

                    int LC_tune = 0;
                    for (int i = 0; i < length - 1; ++i) {
                        LC_tune = (LC_tune << 8) | (this.scanRecord[offset + i] & 0xFF);
                    }
                    this.coarse = (LC_tune >> 10) & 0x1F;
                    this.mid = (LC_tune >> 5) & 0x1F;
                    this.fine = LC_tune & 0x1F;

                    offset += length - 1;
                    break;
                }
                case GAPDataParser.COUNTERS_CODE: {
                    this.hasCounters = true;

                    this.count2M = 0;
                    for (int i = 0; i < (length - 1) / 2; ++i) {
                        this.count2M = (this.count2M << 8) | (this.scanRecord[offset + i] & 0xFF);
                    }
                    offset += (length - 1) / 2;

                    this.count32k = 0;
                    for (int i = 0; i < (length - 1) / 2; ++i) {
                        this.count32k = (this.count32k << 8) | (this.scanRecord[offset + i] & 0xFF);
                    }
                    offset += (length - 1) / 2;
                    break;
                }
                case GAPDataParser.TEMPERATURE_CODE: {
                    this.hasTemperature = true;

                    int temperatureData = 0;
                    for (int i = 0; i < length - 1; ++i) {
                        temperatureData = (temperatureData << 8) | (this.scanRecord[offset + i] & 0xFF);
                    }
                    this.temperature = ((double) temperatureData) / 100 + GAPDataParser.KELVIN_TO_CELSIUS;
                    offset += length - 1;
                    break;
                }
                default: {
                    offset += length - 1;
                    break;
                }
            }
        }
    }
}

package com.android.scum.ble;

/**
 * This class parses a device address for temperature data. It uses the first four bytes to identify
 * the transmitter chip before parsing the last two bytes for the temperature data.
 */
class AddressTempParser {
    private String address;
    private boolean isTransmitterChip;
    private double temp;

    private static final int ADDRESS_LENGTH = 12;
    private static final int PREFIX_LENGTH = 8;
    // First four bytes of device address of transmitter chip
    private static final String TX_ADDRESS_PREFIX = "00027232";

    private static final double KELVIN_TO_CELSIUS = -273.15;

    AddressTempParser(String address) {
        this.address = address;
        this.isTransmitterChip = checkAddress(address);
        if (this.isTransmitterChip) {
            this.temp = parseTemp(address);
        }
    }

    String getAddress() {
        return this.address;
    }

    boolean isTransmitterChip() {
        return this.isTransmitterChip;
    }

    Double getTemp() {
        if (this.isTransmitterChip) {
            return this.temp;
        }
        return null;
    }

    /**
     * Checks the first four bytes of the given address whether the device is the transmitter chip.
     *
     * @param address device address
     * @return true if device is the transmitter chip
     */
    private static boolean checkAddress(String address) {
        String prefix = address.replace(":", "").substring(0, PREFIX_LENGTH);
        return prefix.equals(TX_ADDRESS_PREFIX);
    }

    /**
     * Gets the temperature data from the last two bytes of the given address.
     *
     * @param address device address
     * @return temperature data
     */
    private static double parseTemp(String address) {
        address = address.replace(":", "");
        String dataHex = address.substring(PREFIX_LENGTH, ADDRESS_LENGTH);
        Integer data = Integer.parseInt(dataHex, 16);

        double temp = ((double) data) / 100;
        return temp + KELVIN_TO_CELSIUS;
    }
}

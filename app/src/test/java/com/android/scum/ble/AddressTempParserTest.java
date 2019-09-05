package com.android.scum.ble;

import org.junit.Test;

import static org.junit.Assert.*;

public class AddressTempParserTest {
    private static final String DEVICE_ADDRESS = "00:02:72:32:75:3F";
    private static final String DEVICE_ADDRESS_NOT_TX = "00:02:72:33:75:3F";

    @Test
    public void testGetAddress() {
        AddressTempParser data = new AddressTempParser(AddressTempParserTest.DEVICE_ADDRESS);
        assertEquals(AddressTempParserTest.DEVICE_ADDRESS, data.getAddress());
    }

    @Test
    public void testIsTransmitterChip() {
        AddressTempParser data = new AddressTempParser(AddressTempParserTest.DEVICE_ADDRESS);
        assertTrue(data.isTransmitterChip());
    }

    @Test
    public void testIsTransmitterChipNotTx() {
        AddressTempParser data = new AddressTempParser(AddressTempParserTest.DEVICE_ADDRESS_NOT_TX);
        assertFalse(data.isTransmitterChip());
    }

    @Test
    public void testGetTemp() {
        AddressTempParser data = new AddressTempParser(AddressTempParserTest.DEVICE_ADDRESS);
        assertEquals(27, data.getTemp(), 1e-4);
    }

    @Test
    public void testGetTempNotTx() {
        AddressTempParser data = new AddressTempParser(AddressTempParserTest.DEVICE_ADDRESS_NOT_TX);
        assertNull(data.getTemp());
    }
}

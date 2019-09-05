package com.android.scum.ble;

import org.junit.Test;

import static org.junit.Assert.*;

public class GAPDataParserTest {
    private static final String ADV_ADDRESS = "00:02:72:32:80:C6";
    private static final String ADV_ADDRESS_NOT_SCUM = "00:02:72:32:80:C5";
    private static final byte[] SCAN_RECORD = {
            (byte) 0x06, (byte) 0x08, (byte) 0x53, (byte) 0x43, (byte) 0x55, (byte) 0x4D, (byte) 0x33, (byte) 0x03,
            (byte) 0xC0, (byte) 0x7A, (byte) 0x53, (byte) 0x13, (byte) 0xD0, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
    };

    @Test
    public void testGetAddress() {
        GAPDataParser data = new GAPDataParser(GAPDataParserTest.ADV_ADDRESS, GAPDataParserTest.SCAN_RECORD);
        assertEquals(GAPDataParserTest.ADV_ADDRESS, data.getAddress());
    }

    @Test
    public void testIsSCUM() {
        GAPDataParser data = new GAPDataParser(GAPDataParserTest.ADV_ADDRESS, GAPDataParserTest.SCAN_RECORD);
        assertTrue(data.isSCUM());
    }

    @Test
    public void testIsNotSCUM() {
        GAPDataParser data = new GAPDataParser(GAPDataParserTest.ADV_ADDRESS_NOT_SCUM, GAPDataParserTest.SCAN_RECORD);
        assertFalse(data.isSCUM());
    }

    @Test
    public void testGetScanRecord() {
        GAPDataParser data = new GAPDataParser(GAPDataParserTest.ADV_ADDRESS, new byte[]{0x10, 0x20, 0x30});
        assertEquals("0x10 0x20 0x30", data.getScanRecord());
    }

    @Test
    public void testGetName() {
        GAPDataParser data = new GAPDataParser(GAPDataParserTest.ADV_ADDRESS, GAPDataParserTest.SCAN_RECORD);
        assertEquals("SCUM3", data.getName());
    }

    @Test
    public void testGetTemp() {
        GAPDataParser data = new GAPDataParser(GAPDataParserTest.ADV_ADDRESS, GAPDataParserTest.SCAN_RECORD);
        assertEquals(40.0, data.getTemp(), 1e-3);
    }
}

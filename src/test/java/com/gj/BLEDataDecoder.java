package com.gj;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class BLEDataDecoder {

    public static void decodeAdvertisement(byte[] rawData) {
        int index = 0;
        while (index < rawData.length) {
            int length = rawData[index++];
            if (length == 0) break;  // Break if the length is zero (no more data)

            int type = rawData[index];
            byte[] data = new byte[length - 1];
            System.arraycopy(rawData, index + 1, data, 0, length - 1);

            switch (type) {
                case 0x01: // Flags
                    System.out.println("Flags: " + byteArrayToBinaryString(data));
                    break;
                case 0x02: // Incomplete List of 16-bit Service UUIDs
                case 0x03: // Complete List of 16-bit Service UUIDs
                    System.out.println("16-bit UUIDs: " + decodeServiceUUIDs(data, 2));
                    break;
                case 0x04: // Incomplete List of 32-bit Service UUIDs
                case 0x05: // Complete List of 32-bit Service UUIDs
                    System.out.println("32-bit UUIDs: " + decodeServiceUUIDs(data, 4));
                    break;
                case 0x06: // Incomplete List of 128-bit Service UUIDs
                case 0x07: // Complete List of 128-bit Service UUIDs
                    System.out.println("128-bit UUIDs: " + decodeServiceUUIDs(data, 16));
                    break;
                case 0x09: // Complete Local Name
                    System.out.println("Device Name: " + new String(data));
                    break;
                case 0xFF: // Manufacturer Specific Data
                    System.out.println("Manufacturer Data: " + bytesToHex(data));
                    break;
                default:
                    System.out.println("Type " + type + ": " + bytesToHex(data));
            }
            index += length;
        }
    }

    private static String decodeServiceUUIDs(byte[] data, int uuidSize) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i += uuidSize) {
            if (i != 0) sb.append(", ");
            sb.append(bytesToHex(Arrays.copyOfRange(data, i, i + uuidSize)));
        }
        return sb.toString();
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    private static String byteArrayToBinaryString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        // Example raw data (must be replaced with actual data from a BLE scan)
        byte[] exampleData = "02060110FF107803E8000000000000640023290009094536372045414145".getBytes(StandardCharsets.UTF_16);//{0x02, 0x01, 0x06, 0x03, 0x03, (byte)0xAA, (byte)0xFE, 0x0B, 0x09, 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        decodeAdvertisement(exampleData);
    }
}


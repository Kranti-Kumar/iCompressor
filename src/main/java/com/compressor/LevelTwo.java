package com.compressor;

import java.io.*;
import java.nio.file.*;

public class LevelTwo {
    private String lvl1;
    private String decompressed;

    public LevelTwo() {
        lvl1 = "";
        decompressed = "";
    }

    private void readToString(String name) throws IOException {
        Path path = Paths.get(name);
        byte[] bytes = Files.readAllBytes(path);
        lvl1 = new String(bytes);
    }

    private void writeToFile(String name, byte[] ptr, int size) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(name)) {
            fos.write(ptr, 0, size);
        }
    }

    private void stringFlush(StringBuilder ptr, int ind, int flag) {
        if (flag == 0) {
            if (ptr.length() > 0) {
                if (ptr.length() < 10) {
                    lvl1 += '0' + ptr.length();
                } else {
                    lvl1 += '0';
                    lvl1 += (char) (22 + ptr.length());
                }
                lvl1 += ptr.toString();
                ptr.setLength(0);
            }
        } else {
            if (ptr.length() > 0) {
                if (ptr.length() < 10) {
                    decompressed += '0' + ptr.length();
                } else {
                    decompressed += '0';
                    decompressed += (char) (22 + ptr.length());
                }
                decompressed += ptr.toString();
                ptr.setLength(0);
            }
        }
    }

    private boolean valid(char a) {
        return (a >= 'a' && a <= 'z') || (a >= 'A' && a <= 'Z') || a == ' ' || a == '-' || a == ',' || a == '.';
    }

    private char revMap(int n) {
        if (n >= 0 && n <= 25) {
            return (char) ('a' + n);
        }
        if (n >= 26 && n <= 51) {
            return (char) ('A' + (n - 26));
        }
        if (n == 52)
            return ' ';
        if (n == 53)
            return '-';
        if (n == 54)
            return ',';
        if (n == 55)
            return '.';
        return '\0';
    }

    private String binaryConversion(int temp1, int n) {
        StringBuilder binary = new StringBuilder();
        for (int i = 0; i < n; i++) {
            binary.insert(0, (temp1 & 1) == 1 ? '1' : '0');
            temp1 >>= 1;
        }
        return binary.toString();
    }

    public void compress(String lvl1) throws IOException {
        this.lvl1 = lvl1;
        StringBuilder ans = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        for (int i = 0; i < lvl1.length(); i++) {
            if (lvl1.charAt(i) == '\n')
                continue;

            if (valid(lvl1.charAt(i))) {
                temp.append(lvl1.charAt(i));
            } else {
                if (temp.length() > 0) {
                    stringFlush(temp, i, 0);
                }
                ans.append(lvl1.charAt(i));
            }

            if (temp.length() > 90) {
                stringFlush(temp, i, 0);
            }
        }

        if (temp.length() > 0) {
            stringFlush(temp, lvl1.length(), 0);
        }

        String result = ans.toString();
        byte[] output = result.getBytes();
        writeToFile("coutput2.bin", output, output.length);
    }

    public void decompress(StringBuilder ans, int flag) {
        StringBuilder temp = new StringBuilder();

        for (int i = 0; i < lvl1.length(); i++) {
            if (lvl1.charAt(i) == '\n')
                continue;

            if (valid(lvl1.charAt(i))) {
                temp.append(lvl1.charAt(i));
            } else {
                if (temp.length() > 0) {
                    stringFlush(temp, i, flag);
                }
                ans.append(lvl1.charAt(i));
            }

            if (temp.length() > 90) {
                stringFlush(temp, i, flag);
            }
        }

        if (temp.length() > 0) {
            stringFlush(temp, lvl1.length(), flag);
        }
    }

    private void decompReader(byte[] ptr) {
        StringBuilder binary = new StringBuilder();
        for (byte b : ptr) {
            binary.append(binaryConversion(b & 0xFF, 8));
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 6) {
            if (i + 6 <= binary.length()) {
                String chunk = binary.substring(i, i + 6);
                int value = Integer.parseInt(chunk, 2);
                result.append(revMap(value));
            }
        }

        decompressed = result.toString();
    }

    public void levelTwoMainD(String fileName) throws IOException {
        readToString(fileName);
        decompReader(Files.readAllBytes(Paths.get(fileName)));
        StringBuilder ans = new StringBuilder();
        decompress(ans, 3);

        try (FileOutputStream fos = new FileOutputStream("doutput2.txt")) {
            fos.write(decompressed.getBytes());
        }
    }

    public void levelTwoMainC(String fileName) throws IOException {
        readToString(fileName);
        compress(lvl1);
    }
}
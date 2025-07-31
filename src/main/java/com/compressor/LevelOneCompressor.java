package com.compressor;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class LevelOneCompressor {
    private Trie myTrie;
    private List<String> vs;
    private byte[] inpStr;
    private byte[] ansStr;
    private long charLen;

    public LevelOneCompressor() {
        myTrie = new Trie();
        vs = new ArrayList<>();
    }

    private void insertDictionary() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("dictionary.txt"))) {
            String line;
            long d = 0;
            while ((line = reader.readLine()) != null) {
                String myText = line.trim();
                if (myText.endsWith(")")) {
                    myText = myText.substring(0, myText.length() - 1) + " ";
                }
                myTrie.insert(myText, (int) d);
                d++;
            }
        }
    }

    private void vectorString() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("key.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                vs.add(line.trim());
            }
        }
    }

    private void readToString(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        charLen = Files.size(path);
        inpStr = Files.readAllBytes(path);
    }

    public void compress(String fileName) throws IOException {
        insertDictionary();
        vectorString();
        readToString(fileName);

        StringBuilder buffStr = new StringBuilder();
        StringBuilder ans = new StringBuilder();

        for (int i = 0; i < charLen; i++) {
            int flag = 0, lastPos = 0, key = 0, index = 0;
            StringBuilder searc = new StringBuilder();

            if (inpStr[i] == '\n')
                continue;

            if ((inpStr[i] >= 'a' && inpStr[i] <= 'z') || (inpStr[i] >= 'A' && inpStr[i] <= 'Z')) {
                for (int j = i, k = 0; k < 15 && j < charLen; j++, k++) {
                    if ((inpStr[j] >= 'a' && inpStr[j] <= 'z') ||
                            (inpStr[j] >= 'A' && inpStr[j] <= 'Z') ||
                            inpStr[j] == ' ') {
                        searc.append((char) inpStr[j]);
                        key = myTrie.search(searc.toString());
                        if (key != -1) {
                            flag = 1;
                            lastPos = j;
                            index = key;
                        }
                    } else {
                        break;
                    }
                }

                if (flag == 1) {
                    if (buffStr.length() > 0) {
                        if (buffStr.length() < 10) {
                            ans.append('0' + buffStr.length());
                        } else {
                            ans.append('0');
                            ans.append((char) (22 + buffStr.length()));
                        }
                        ans.append(buffStr);
                        buffStr.setLength(0);
                    }
                    ans.append(vs.get(index));
                    i = lastPos;
                } else {
                    if (buffStr.length() != 0 || (inpStr[i] >= 'a' && inpStr[i] <= 'z')) {
                        buffStr.append((char) inpStr[i]);
                    } else {
                        ans.append((char) inpStr[i]);
                    }
                }
            } else {
                if (buffStr.length() == 0 && (inpStr[i] < '0' || inpStr[i] > '9')) {
                    ans.append((char) inpStr[i]);
                } else {
                    buffStr.append((char) inpStr[i]);
                }
            }

            if (buffStr.length() > 90) {
                if (buffStr.length() > 0) {
                    if (buffStr.length() < 10) {
                        ans.append('0' + buffStr.length());
                    } else {
                        ans.append('0');
                        ans.append((char) (22 + buffStr.length()));
                    }
                    ans.append(buffStr);
                    buffStr.setLength(0);
                }
            }
        }

        if (buffStr.length() > 0) {
            if (buffStr.length() < 10) {
                ans.append('0' + buffStr.length());
            } else {
                ans.append('0');
                ans.append((char) (22 + buffStr.length()));
            }
            ans.append(buffStr);
        }

        String result = ans.toString();
        ansStr = result.getBytes();

        try (FileOutputStream fos = new FileOutputStream("coutput1.bin")) {
            fos.write(ansStr);
        }
    }
}
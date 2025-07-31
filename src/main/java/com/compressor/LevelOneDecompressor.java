package com.compressor;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class LevelOneDecompressor {
    private Trie myTrie;
    private long charLen;
    private List<String> vs;
    private byte[] outStr;
    private byte[] ansStr;

    public LevelOneDecompressor() {
        myTrie = new Trie();
        vs = new ArrayList<>();
    }

    private void insertDictionary() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("key.txt"))) {
            String line;
            long d = 0;
            while ((line = reader.readLine()) != null) {
                myTrie.insert(line.trim(), (int) d);
                d++;
            }
        }
    }

    private void readToString(String inputFileName) throws IOException {
        Path path = Paths.get(inputFileName);
        charLen = Files.size(path);
        outStr = Files.readAllBytes(path);
    }

    private void vectorString() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("dictionary.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String myText = line.trim();
                if (myText.endsWith(")")) {
                    myText = myText.substring(0, myText.length() - 1) + " ";
                }
                vs.add(myText);
            }
        }
    }

    public void decompress(String fileName) throws IOException {
        insertDictionary();
        vectorString();
        readToString(fileName);

        StringBuilder ans = new StringBuilder();

        for (int i = 0; i < charLen; i++) {
            if (outStr[i] == '\n')
                continue;

            if (outStr[i] >= '0' && outStr[i] <= '9') {
                if (outStr[i] == '0') {
                    int tempLen = outStr[i + 1] - 22;
                    for (int j = i + 2, k = 0; k < tempLen; j++, k++) {
                        ans.append((char) outStr[j]);
                        i = j;
                    }
                } else {
                    int tempLen = outStr[i] - '0';
                    for (int j = i + 1, k = 0; k < tempLen; j++, k++) {
                        ans.append((char) outStr[j]);
                        i = j;
                    }
                }
            } else {
                if (!(outStr[i] >= 'a' && outStr[i] <= 'z')) {
                    ans.append((char) outStr[i]);
                }
            }
        }

        String result = ans.toString();
        ansStr = result.getBytes();

        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(ansStr);
        }
    }
}
package com.compressor;

public class Trie {
    private static class Node {
        Node[] add;
        int key;

        Node() {
            key = -1;
            add = new Node[56];
        }
    }

    private Node start;

    public Trie() {
        start = new Node();
    }

    public void insert(String s, int k) {
        Node ptr = start;
        for (int i = 0; i < s.length(); i++) {
            int next = getNext(s.charAt(i));
            if (ptr.add[next] == null) {
                ptr.add[next] = new Node();
            }
            ptr = ptr.add[next];
        }
        ptr.key = k;
    }

    public int search(String s) {
        Node ptr = start;
        for (int i = 0; i < s.length(); i++) {
            int next = getNext(s.charAt(i));
            if (ptr.add[next] == null) {
                return -1;
            }
            ptr = ptr.add[next];
        }
        return ptr.key;
    }

    private static int getNext(char c) {
        if (c >= 'a' && c <= 'z') {
            return c - 'a';
        }
        if (c >= 'A' && c <= 'Z') {
            return c - 'A' + 26;
        }
        if (c == ' ') {
            return 52;
        }
        if (c == '-') {
            return 53;
        }
        if (c == ',') {
            return 54;
        }
        if (c == '.') {
            return 55;
        }
        // Handle unsupported characters by treating them as spaces
        return 52;
    }
}
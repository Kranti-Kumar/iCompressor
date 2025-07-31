package com.compressor;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter 1 for compression and 2 for decompression:");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice == 1) {
            System.out.println("Enter the file name to compress:");
            String fileName = scanner.nextLine();

            try {
                LevelOneCompressor compressor = new LevelOneCompressor();
                compressor.compress(fileName);

                LevelTwo levelTwo = new LevelTwo();
                levelTwo.levelTwoMainC("coutput1.bin");

                System.out.println("Compression completed successfully!");
            } catch (IOException e) {
                System.err.println("Error during compression: " + e.getMessage());
            }
        } else if (choice == 2) {
            System.out.println("Enter the file name to decompress:");
            String fileName = scanner.nextLine();

            try {
                LevelOneDecompressor decompressor = new LevelOneDecompressor();
                decompressor.decompress(fileName);

                LevelTwo levelTwo = new LevelTwo();
                levelTwo.levelTwoMainD("doutput1.bin");

                System.out.println("Decompression completed successfully!");
            } catch (IOException e) {
                System.err.println("Error during decompression: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid choice!");
        }

        scanner.close();
    }
}
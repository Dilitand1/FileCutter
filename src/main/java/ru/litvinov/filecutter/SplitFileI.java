package ru.litvinov.filecutter;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.Scanner;

public class SplitFileI implements FileWorkerI {

    public static void splitFacade(String inputStringFile, String size, String outputStringDirectory) throws IOException {
        File inputFile = new File(inputStringFile);
        File outputDirectory = new File("result" + new Date().getTime());
        Integer partSizeMb = 1;
        if (inputFile.exists()) {
            if (outputStringDirectory != null) {
                if (new File(outputStringDirectory).isDirectory()) {
                    outputDirectory = new File(outputStringDirectory);
                }
            }
            if (size != null && size.matches("\\d+") && Integer.parseInt(size) > 1) {
                partSizeMb = Integer.parseInt(size);
            }

            String input = "";
            System.out.println(inputFile.getAbsolutePath() + "\nwill be split to directory" + outputDirectory.getAbsolutePath() + " (Y/N)");
            Scanner scanner = new Scanner(System.in);
            while (true) {
                input = scanner.nextLine();
                if (input.equalsIgnoreCase("N")) {
                    System.out.println("break operation");
                    break;
                }
                if (input.equalsIgnoreCase("Y")) {
                    clearResultDirectory(Paths.get(outputDirectory.getAbsolutePath()));
                    createResultDirectory(outputDirectory.getAbsolutePath());
                    splitFile(inputFile, partSizeMb, outputDirectory);
                    break;
                }
            }

        } else {
            System.out.println("File " + inputFile + " not found");
        }

    }

    private static void splitFile(File inputFile, Integer partSizeMb, File outputDirectory) throws IOException {
        System.out.println("Splitting...");
        int partCounter = 1;//I like to name parts from 001, 002, 003, ...
        //you can change it to 0 if you want 000, 001, ...

        int sizeOfFiles = 1024 * 1024 * partSizeMb;// 1MB
        byte[] buffer = new byte[sizeOfFiles];

        String fileName = inputFile.getName();

        try (FileInputStream fis = new FileInputStream(inputFile);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            int bytesAmount = 0;
            while ((bytesAmount = bis.read(buffer)) > 0) {

                //write each chunk of data into separate file with different number in name
                String filePartName = String.format("%s.%03d", fileName, partCounter++);
                File newFile = new File(outputDirectory + "\\" + filePartName);

                try (FileOutputStream out = new FileOutputStream(newFile)) {
                    out.write(buffer, 0, bytesAmount);
                }
            }
        }
    }

    private static void clearResultDirectory(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walkFileTree(path
                    , new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                                throws IOException {
                            if (file.getFileName().toString().matches(".+\\.\\d{3}")) {
                                Files.delete(file);
                            }
                            return FileVisitResult.CONTINUE;
                        }
                    }
            );
        }
    }

    private static void createResultDirectory(String path) throws IOException {
        Path path1 = Paths.get(path);
        if (!Files.exists(path1)) {
            Files.createDirectories(path1);
        }
    }

    @Override
    public void work(String[] args) {
        try {
            SplitFileI.splitFacade(args[1],
                    args.length > 2 ? args[2] : null,
                    args.length > 3 ? args[3] : null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

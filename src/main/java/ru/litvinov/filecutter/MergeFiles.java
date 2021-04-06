package ru.litvinov.filecutter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MergeFiles implements FileWorkerI {



    public static void mergeFacade(String firstFile, String resultFile) throws IOException {
        mergeFacade(new File(firstFile), resultFile == null ? null : new File(resultFile));
    }

    public static void mergeFacade(File firstFile, File resultFile) throws IOException {
        if (firstFile.exists() && firstFile.getName().matches(".*\\d+")) {
            if (resultFile == null) {
                //if result is null then make file near firstfile
                resultFile = new File(firstFile.getAbsolutePath().substring(0, firstFile.getAbsolutePath().lastIndexOf(".")));
            }
            List<File> files = listOfFilesToMerge(firstFile);
            Scanner scanner = new Scanner(System.in);
            System.out.println(files.stream().map(x -> x.getAbsolutePath()).reduce("files:", (first, second) -> first + "\n" + second) + "\nwill be merge to " + resultFile.getAbsolutePath() + " (Y/N)");
            String input = "";
            while (true) {
                input = scanner.nextLine();
                if (input.equalsIgnoreCase("N")) {
                    System.out.println("break operation");
                    break;
                }
                if (input.equalsIgnoreCase("Y")) {
                    mergeFiles(files, resultFile);
                    break;
                }
            }
        } else {
            System.out.println("Wrong file or not exists");
        }
    }

    private static void mergeFiles(List<File> files, File into) throws IOException {
        System.out.println("clear result path");
        if (into.exists()) into.delete();
        System.out.println("Merging...");
        try (FileOutputStream fos = new FileOutputStream(into);
             BufferedOutputStream mergingStream = new BufferedOutputStream(fos)) {
            for (File f : files) {
                Files.copy(f.toPath(), mergingStream);
            }
        }
        System.out.println("Merge complete");
    }

    private static List<File> listOfFilesToMerge(File firstFile) {
        String tmpName = firstFile.getName();//{name}.{number}
        String destFileName = tmpName.substring(0, tmpName.lastIndexOf('.'));//remove .{number}
        File[] files = firstFile.getParentFile().listFiles(
                (File dir, String name) -> name.matches(destFileName + "[.]\\d+"));
        Arrays.sort(files);//ensuring order 001, 002, ..., 010, ...
        return Arrays.asList(files);
    }

    @Override
    public void work(String[] args) {
        try {
            MergeFiles.mergeFacade(args[1]
                    , args.length > 2 ? args[2] : null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

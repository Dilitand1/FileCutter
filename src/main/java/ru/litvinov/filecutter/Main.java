package ru.litvinov.filecutter;

import java.io.*;
import java.util.Scanner;

public class Main {
    /**
     * example1: -m C:\Users\Dilit\IdeaProjects\FileCutter\result\Driver.rar.001
     * example2: -s Driver.rar 3
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        run(args);
        //run(new String[]{"-m","C:\\Users\\Dilit\\IdeaProjects\\FileCutter\\result\\Driver.rar.001"});
        //run(new String[]{"-s","Driver.rar","3"});

    }

    public static void run(String[] args) throws IOException {
        if (args.length > 1 && args.length < 5) {
            if (args[0].equals("-s")) {
                SplitFile.splitFacade(args[1],
                        args.length > 2 ? args[2] : null,
                        args.length > 3 ? args[3] : null);
            } else if (args[0].equals("-m")) {
                MergeFiles.mergeFacade(args[1]
                        , args.length > 2 ? args[2] : null);
            } else {
                System.out.println("wrong option");
            }
        } else {
            System.out.println("wrong input");
            System.out.println("-s split option\n-m merge option\n");
            System.out.println("-s pathToInputFile [size of piece in Mb(default 1mb)] [pathToOutputDirectory(default result*)] ");
            System.out.println("-m pathToFirstPart [pathToOutputFile]");
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Press any key to exit");
        scanner.nextLine();
    }

}

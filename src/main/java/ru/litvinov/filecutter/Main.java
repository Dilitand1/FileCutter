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
            FileWorkerI fileWorkerI = FactoryFileWorker.getFileWorker(args[0]);
            fileWorkerI.work(args);
        } else {
            FactoryFileWorker.printHelp();
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Press any key to exit");
        scanner.nextLine();
    }
}
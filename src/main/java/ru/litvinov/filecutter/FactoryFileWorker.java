package ru.litvinov.filecutter;

public class FactoryFileWorker {
    public static FileWorkerI getFileWorker(String s) {
        FileWorkerI fileWorkerI = null;
        switch (s){
            case "-m":
                fileWorkerI = new MergeFiles();
                break;
            case "-s":
                fileWorkerI = new SplitFileI();
                break;
            default:
                printHelp();
        }
        return fileWorkerI;
    }

    public static void printHelp(){
        System.out.println("-s split option\n-m merge option\n");
        System.out.println("-s pathToInputFile [size of piece in Mb(default 1mb)] [pathToOutputDirectory(default result*)] ");
        System.out.println("-m pathToFirstPart [pathToOutputFile]");
    }
}

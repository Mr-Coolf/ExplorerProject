package main.WorkLogic;

import main.FileTree.FileTree;
import main.WorkLogic.Commands.Command;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class WorkLogic {
    public static boolean isWorking = true;
    public static boolean showChildren = false;
    public static boolean printTree = false;
    public static boolean printDirectories = true;
    public static boolean printFiles = true;
    public static boolean showSizes = false;
    private static Path currentWorkingDirectory;

    public static void startApp() {

        Scanner scanner = new Scanner(System.in);
        currentWorkingDirectory = Paths.get("").toAbsolutePath();
        FileTree fileTree = new FileTree();
        Command command;

        System.out.println("\nTo get commands list type \"help\"");

        while (isWorking) {

            try {
                if (printTree) System.out.println( fileTree.tree(currentWorkingDirectory).get());
//            else System.out.print("\n" + CommandExecutioner.fileToString(currentWorkingDirectory));
                else System.out.print("\n" + currentWorkingDirectory.toString() + "\n");

                showChildren = false;
                printTree = true;
                System.out.println("Enter your command: ");
                command = Command.get(scanner.nextLine());
                System.out.println();
                command.executeCommand();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                printTree = false;
            }
        }

    }

    public static void setCurrentWorkingDirectory(Path currentWorkingDirectory) {
        WorkLogic.currentWorkingDirectory = currentWorkingDirectory;
    }

    public static Path getCurrentWorkingDirectory(){
        return currentWorkingDirectory;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }
}

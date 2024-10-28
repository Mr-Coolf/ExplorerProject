package main.WorkLogic.Commands;

import main.FileTree.FileTree;
import main.WorkLogic.Arguments.ArgumentsManager;
import main.WorkLogic.Arguments.FileArgument;
import main.WorkLogic.Arguments.InstructionArgument;
import main.WorkLogic.WorkLogic;
import main.WorkLogic.enums.CommandsEnum;
import main.WorkLogic.enums.InstructionsEnum;

import java.io.IOException;
import java.nio.file.*;

public class CommandExecutioner {
    public static void openDirectory(Path currentDir, ArgumentsManager arguments){
        FileArgument fileArg = arguments.getFileArgument();
        if(fileArg == null) throw new RuntimeException("No file name specified");

        if(FileTree.getChildFolders(currentDir).contains(fileArg.getFilePath().toAbsolutePath())){
            WorkLogic.setCurrentWorkingDirectory(fileArg.getFilePath());

        }
    }

    public static void exitDirectory(Path currentDir) {
        if (!currentDir.equals(currentDir.getRoot())) {
            WorkLogic.setCurrentWorkingDirectory(currentDir.getParent() == null ? currentDir.getRoot(): currentDir.getParent());
        } else {
            throw new RuntimeException("You can't exit the root folder");
        }
    }

    public static long getSize(Path path) {
        if (Files.isDirectory(path)){
            try {
                long result = 0;

                DirectoryStream<Path> stream = Files.newDirectoryStream(path);

                for (Path p : stream) {
                    if (!Files.isDirectory(p)) result += Files.size(p);
                    else result += getSize(p);
                }
                return result;
            } catch (Exception e) {
                if(e instanceof IOException) throw new RuntimeException("Input/Output exception has occurred");
                else if(e instanceof SecurityException) throw new RuntimeException("Reading permission was denied");
                else throw new RuntimeException(e.getMessage());
            }
        }
        return -1;

    }

    public static long getSize(ArgumentsManager arguments) {
        Path p = arguments.getFileArgument().getFilePath();
        if (Files.isDirectory(p)) {
            return getSize(p);
        } else {
            try {
                return Files.size(p);
            }
            catch (Exception e){
                if(e instanceof IOException) throw new RuntimeException("Input/Output exception has occurred");
                else if(e instanceof SecurityException) throw new RuntimeException("Reading permission was denied");
                else throw new RuntimeException(e.getMessage());
            }
        }
    }

    public static void delDirectory(Path currentDir) {
        try{
            Files.delete(currentDir);
        } catch (Exception e){
            if (e instanceof IOException) throw new RuntimeException("Input/Output exception has occurred");
            else if(e instanceof SecurityException) throw new RuntimeException("Deleting file was denied");
            else throw new RuntimeException(e.getMessage());
        }
    }

    public static void delDirectory(ArgumentsManager arguments) {
        FileArgument fileArg = arguments.getFileArgument();
        if(fileArg == null) throw new RuntimeException("No file name specified.");
        else delDirectory(fileArg.getFilePath());
    }

    public static void stop(){
        System.exit(0);
    }

    public static void logCommandInfo(){
        StringBuilder result = new StringBuilder();
        for (CommandsEnum c: CommandsEnum.values()) {
            result.append('\n').append(c.toString()).append(" - ").append(c.getInfo()).append('\n').append(c.getHowTo()).append('\n');
        }
        System.out.println(result);
    }

    public static void logArgsInfo() {
        StringBuilder result = new StringBuilder();
        for (InstructionsEnum i: InstructionsEnum.values()) {
            result.append(i.toString()).append(" - ").append(i.getInfo()).append('\n');
        }
        System.out.println(result);
    }

    public static String fileToString(Path path){
        try{
            return path.getFileName() + " " + (WorkLogic.showSizes ? (getSize(path) + " bytes") : "") + '\n';
        } catch (Exception e){
            throw new RuntimeException(e.toString());
        }
    }

    public static void makeDirectory(FileArgument fileArgument) {
        try {
            Files.createDirectory(fileArgument.getFilePath());
        } catch (Exception e) {
            throw new RuntimeException("Could not make a directory at path: " + fileArgument.getFilePath().toString());
        }
    }

    public static void printTree(ArgumentsManager arguments){
        ArgumentsManager.setInstructions(arguments);
        if (arguments.getFileArgument() != null){
            System.out.println(new FileTree().tree(arguments.getFileArgument().getFilePath()).get());
        }
        else {
            System.out.println(new FileTree().tree(WorkLogic.getCurrentWorkingDirectory()).get());
        }
        arguments.add(new InstructionArgument(InstructionsEnum.dp));

        System.out.println();
    }
}

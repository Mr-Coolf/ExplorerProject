package main.WorkLogic.Commands;

import main.WorkLogic.Arguments.ArgumentsManager;
import main.WorkLogic.Arguments.FileArgument;
import main.WorkLogic.Arguments.InstructionArgument;
import main.WorkLogic.WorkLogic;
import main.WorkLogic.enums.CommandsEnum;
import main.WorkLogic.enums.InstructionsEnum;

import java.nio.file.Paths;
import java.util.Scanner;

public class Command {
    private CommandsEnum command;
    private ArgumentsManager arguments;

    public static Command get(String s) {
        CommandsEnum command;
        ArgumentsManager arguments;
        Scanner scanner = new Scanner(s);
        scanner.useDelimiter(" ");

        if (scanner.hasNext()) {
            try {
                command = CommandsEnum.valueOf(scanner.next().toLowerCase());
            } catch (Exception e) {
                if (e instanceof IllegalArgumentException) {
                    String getAlikeCommand = getAlikeCommand(s);
                    throw new RuntimeException("This command does not exist. " + (getAlikeCommand != null ? "Did you mean \"" + getAlikeCommand + "\"?" : ""));
                }
                throw new RuntimeException(e.getMessage());
            }
        }
        else throw new RuntimeException("No command has been input.");

        arguments = new ArgumentsManager();

        String input = "";
        if(command.acceptsPath() && scanner.hasNext()) {
            StringBuilder fileName = new StringBuilder();
            while (scanner.hasNext()) {
                input = scanner.next();
                if (!input.contains(InstructionsEnum.INSTRUCTION_START)) {
                    fileName.append(input).append(!scanner.hasNext(".*\\*.*") && scanner.hasNext()? ' ' : "");
                    input = "";
                }
                else break;
            }
            if (!fileName.isEmpty()) arguments.setFileArgument(new FileArgument(Paths.get(WorkLogic.getCurrentWorkingDirectory().toString(), fileName.toString())));
        }


        if (command.acceptsInstructions() && !input.isEmpty()) {
            arguments.add(new InstructionArgument(input));
            input = "";
            while (scanner.hasNext()) {
                arguments.add(scanner.next());
            }
        }

        if(scanner.hasNext() || !input.isEmpty()) {
            throw new RuntimeException("Command \"" + command + "\" " + (command.acceptsPath() ? "accepts Path" : "does not accept Path") + " and " + (command.acceptsInstructions() ? "accepts arguments." : "does not accept arguments." ));
        }

        return new Command(command, arguments);
    }

    private static String getAlikeCommand(String s) {
        while (!s.isEmpty()) {
            for(CommandsEnum command: CommandsEnum.values()){
                if (command.toString().startsWith(s)) return command.toString();
            }
            int newLen = s.length() - 2;
            s = s.substring(0, Math.max(newLen, 0));
        }
        return null;
    }

    private Command(CommandsEnum command, ArgumentsManager arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    public void executeCommand() {
        switch (command){
            case help -> {
                arguments.add(new InstructionArgument(InstructionsEnum.dp));
                CommandExecutioner.logCommandInfo();
            }
            case helpargs -> {
                arguments.add(new InstructionArgument(InstructionsEnum.dp));
                CommandExecutioner.logArgsInfo();
            }
            case tree -> {
                CommandExecutioner.printTree(arguments);
            }
            case opdir -> {
                CommandExecutioner.openDirectory(WorkLogic.getCurrentWorkingDirectory(), arguments);
            }
            case exdir -> {
                CommandExecutioner.exitDirectory(WorkLogic.getCurrentWorkingDirectory());
            }
            case deldir -> {
                if(arguments.getFileArgument().getFilePath() == null){
                    CommandExecutioner.delDirectory(WorkLogic.getCurrentWorkingDirectory());
                }
                else{
                    CommandExecutioner.delDirectory(arguments);
                }
            }
            case size -> {
                arguments.add(new InstructionArgument(InstructionsEnum.dp));
                if(arguments.getFileArgument() == null || arguments.getFileArgument().getFilePath() == null){
                    long result = CommandExecutioner.getSize(WorkLogic.getCurrentWorkingDirectory());
                    if (result != -1) System.out.println(WorkLogic.getCurrentWorkingDirectory().getFileName() + " is " + CommandExecutioner.getSize(WorkLogic.getCurrentWorkingDirectory()) + " bytes");
                    else throw new RuntimeException("There was an error while calculating folder size");
                }
                else {
                    long result = CommandExecutioner.getSize(arguments);
                    if (result != -1) System.out.println(arguments.getFileArgument().getFilePath().getFileName() + " is " + CommandExecutioner.getSize(arguments.getFileArgument().getFilePath()) + " bytes");
                    else throw new RuntimeException("There was an error while calculating folder size");
                }
            }
            case mkdir -> {
                CommandExecutioner.makeDirectory(arguments.getFileArgument());
            }
            case stop -> {
                CommandExecutioner.stop();
            }
            default -> {
                throw new RuntimeException("No such command is implemented");
            }
        }
        ArgumentsManager.setInstructions(arguments);
    }
}

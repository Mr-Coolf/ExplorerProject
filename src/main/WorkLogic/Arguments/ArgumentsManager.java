package main.WorkLogic.Arguments;

import main.WorkLogic.WorkLogic;
import main.WorkLogic.enums.InstructionsEnum;

import java.util.ArrayList;
import java.util.List;

public class ArgumentsManager {
    FileArgument                fileArgument            = null;
    List<InstructionArgument>   instructionArguments          ;

    public ArgumentsManager(FileArgument fileArgument, List<InstructionArgument> instructionArguments) {
        this.fileArgument = fileArgument;
        this.instructionArguments = instructionArguments;
    }

    public ArgumentsManager(FileArgument fileArgument) {
        this.fileArgument = fileArgument;
    }

    public ArgumentsManager() {
        instructionArguments = new ArrayList<>();
    }


    public void add(String s){
        if(String.valueOf(s.charAt(0)).equals(InstructionsEnum.INSTRUCTION_START)){
            instructionArguments.add(new InstructionArgument(s));
        }
        else if (fileArgument == null) {
            fileArgument = new FileArgument(s);
        }
        else {
            throw new RuntimeException("This is not an instruction and file is already specified");
        }
    }

    public void add(InstructionArgument instructionArgument){
        instructionArguments.add(instructionArgument);
    }

    public void setFileArgument(FileArgument fileArgument) {
        this.fileArgument = fileArgument;
    }

    public FileArgument getFileArgument() {
        return fileArgument;
    }

    public List<InstructionArgument> getInstructionArguments() {
        return instructionArguments;
    }

    public static void setInstructions(ArgumentsManager arguments){
        WorkLogic.showChildren        = arguments.getInstructionArguments().contains(new InstructionArgument(InstructionsEnum.sc));
        WorkLogic.printTree           = !arguments.getInstructionArguments().contains(new InstructionArgument(InstructionsEnum.dp));
        WorkLogic.showSizes           = arguments.getInstructionArguments().contains(new InstructionArgument(InstructionsEnum.size));
        WorkLogic.printDirectories    = !arguments.getInstructionArguments().contains(new InstructionArgument(InstructionsEnum.dpd));
        WorkLogic.printFiles          = !arguments.getInstructionArguments().contains(new InstructionArgument(InstructionsEnum.dpf));
    }
}

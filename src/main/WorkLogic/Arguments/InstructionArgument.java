package main.WorkLogic.Arguments;

import main.WorkLogic.enums.ArgumentTypes;
import main.WorkLogic.enums.InstructionsEnum;

public class InstructionArgument {
    InstructionsEnum instructionEnum;
    private final ArgumentTypes argType;

    public InstructionArgument(String s) {
        s = s.substring(1);
        argType = ArgumentTypes.INSTRUCTION;
        try {
            instructionEnum = InstructionsEnum.valueOf(s.toLowerCase());
        }
        catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                String getAlikeInstruction = getAlikeInstruction(s);
                throw new RuntimeException("This instruction does not exist. " + (getAlikeInstruction != null ? "Did you mean \"" + getAlikeInstruction + "\"?" : ""));
            }
        }
    }

    private String getAlikeInstruction(String s) {
        s = InstructionsEnum.INSTRUCTION_START + s;
        while (!s.isEmpty()) {
            for(InstructionsEnum instruction: InstructionsEnum.values()){
                if (instruction.toString().startsWith(s)) return instruction.toString();
            }
            int newLen = s.length() - 2;
            s = s.substring(0, Math.max(newLen, 0));
        }
        return null;
    }

    public InstructionArgument(InstructionsEnum instructionEnum) {
        argType = ArgumentTypes.INSTRUCTION;
        this.instructionEnum = instructionEnum;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        InstructionArgument that = (InstructionArgument) o;
        return instructionEnum == that.instructionEnum;
    }

    @Override
    public int hashCode() {
        return instructionEnum.hashCode();
    }

    public InstructionsEnum getInstructionEnum() {
        return instructionEnum;
    }

    public void setInstructionEnum(InstructionsEnum instructionEnum) {
        this.instructionEnum = instructionEnum;
    }

    public ArgumentTypes getArgType() {
        return argType;
    }

}

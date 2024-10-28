package main.WorkLogic.enums;

public enum InstructionsEnum {
    sc      ("Shows child directories"),
    dp ("Disables next folder tree print"),
    dpd("Disables printing directories in the next tree print"),
    dpf("Disables printing files in the next tree print"),
    size ("Makes next output show folder and file sizes");
    private final String info;
    public final static String INSTRUCTION_START = "*" ;
    InstructionsEnum(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return INSTRUCTION_START + name();
    }

}

package main.WorkLogic.enums;

public enum CommandsEnum {
    help        ("Types all the commands and what do they do.",
                "help",
                false, false),
    helpargs    ("Types all the arguments and what do they do.",
                "helpargs",
                false, false),
    tree        ("Prints file tree.",
                "tree (directory_name) [args]",
                true, true),
    opdir       ("Opens specified directory.",
                "opdir (directory_name) [args]",
                true, true),
    exdir       ("Closes a directory and goes as down as root directory.",
                "exdir [args]",
                false, true),
    deldir      ("Deletes directory if specified. If directory is not specified deletes opened directory. Does not accept arguments.",
                "deldir (directory_name) [args]\ndeldir [args]",
                true, true),
    mkdir       ("Creates a directory in a current folder with name specified as a string with no arguments. Does not accept arguments.",
                "mkdir (new_directory_name)",
                true, false),
    size        ("Gets size of a directory or a size of a file if specified. If directory or file are not specified return size of current directory",
                "size (directory_name)\nsize",
                true, false),
    stop        ("Stops the program.",
                "stop",
                false, false);

    private final String info;
    private final String howTo;
    private final boolean acceptsPath;
    private final boolean acceptsInstructions;

    CommandsEnum(String info, String howTo, boolean acceptsPath, boolean acceptsInstructions) {
        this.info = info;
        this.howTo = howTo;
        this.acceptsPath = acceptsPath;
        this.acceptsInstructions = acceptsInstructions;
    }

    public String getInfo() {
        return info;
    }

    public String getHowTo() {
        return howTo;
    }

    public boolean acceptsPath() {
        return acceptsPath;
    }

    public boolean acceptsInstructions() {
        return acceptsInstructions;
    }
}

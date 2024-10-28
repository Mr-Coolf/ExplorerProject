package main.WorkLogic.Arguments;

import main.WorkLogic.WorkLogic;
import main.WorkLogic.enums.ArgumentTypes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileArgument {
    private final Path filePath;
    private ArgumentTypes argType;

    public FileArgument(String s) {
        argType = ArgumentTypes.FILE;
        filePath = Paths.get(WorkLogic.getCurrentWorkingDirectory().toString(), s);
        if(!Files.exists(filePath)) {
            throw new RuntimeException("Path \"" + filePath + "\" does not exist.");
        }
    }

    public FileArgument(Path filePath) {
        this.filePath = filePath;
        if(!Files.exists(filePath)) {
            throw new RuntimeException("Path \"" + filePath + "\" does not exist.");
        }
    }

    public Path getFilePath() {
        return filePath;
    }
}

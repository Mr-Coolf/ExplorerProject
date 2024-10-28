package main.FileTree;

import main.WorkLogic.WorkLogic;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.util.*;
import java.util.function.Predicate;

public class FileTree {
    private final String continueStr = "│  ";
    private final String closeStr = "└─ ";
    private final String continueAddStr = "├─ ";
    private final String emptyStr = "   ";


    public Optional<String> tree(final Path path) {
        if (path == null || !Files.exists(path)) return Optional.empty();
        else if(Files.isDirectory(path)) {
            return Optional.of(folderToString(path));
        }
        else {
            return Optional.of(fileToString(path));
        }
    }

    private String folderToString(Path path){
        List<String> folderList = new ArrayList<>();
        List<String> fileList = new ArrayList<>();
        StringBuilder result = new StringBuilder();

        result.append(fileToString(path));

//        Configures result structure
        if (WorkLogic.showChildren){
            if (Files.isDirectory(path)) {
                Iterator<Path> iterator;
                try {
                    iterator = Files.newDirectoryStream(path).iterator();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                while (iterator.hasNext()) {
                    Path p = iterator.next();
                    if (Files.isDirectory(p)) folderList.add(folderToString(p));
                    else fileList.add(fileToString(p));
                }
            }

            folderList.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.toLowerCase().compareTo(o2.toLowerCase());
                }
            });
            fileList.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.toLowerCase().compareTo(o2.toLowerCase());
                }
            });
        }
        else{
            if (Files.isDirectory(path)) {
                Iterator<Path> iterator;
                try {
                    iterator = Files.newDirectoryStream(path).iterator();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                while (iterator.hasNext()) {
                    Path p = iterator.next();
                    if(Files.isDirectory(p) && WorkLogic.printDirectories) folderList.add(fileToString(p));
                    else if(!Files.isDirectory(p) && WorkLogic.printFiles) fileList.add(fileToString(p));
                }
            }
        }

//        Puts all directories into result
        int size;
        size = folderList.size();
        for (int i = 0; i < size; i++) {
            Scanner scanner = new Scanner(folderList.get(i));
            scanner.useDelimiter("\n");
            if (i == size - 1 && fileList.isEmpty()) {
                String s = scanner.next();
                result.append(closeStr).append(s).append('\n');
                while (scanner.hasNext()) {
                    s = scanner.next();
                    result.append(emptyStr).append(s).append('\n');
                }
            } else {
                String s = scanner.next();
                result.append(continueAddStr).append(s).append('\n');
                while (scanner.hasNext()) {
                    s = scanner.next();
                    result.append(continueStr).append(s).append('\n');
                }
            }
        }

//        Puts all files into result
        size = fileList.size();
        for (int i = 0; i < size; i++) {
            String s = fileList.get(i);
            if (i != size - 1) result.append(continueAddStr).append(s);
            else result.append(closeStr).append(s);
        }

        return result.toString();
    }

    private String fileToString(Path path){
        return (path.equals(path.getRoot()) ? path.toString() : path.getFileName()) + " " + (WorkLogic.showSizes ? (getSize(path) + " bytes") : "") + '\n';
    }

//    private long getSize(Path path) {
//        if (Files.isDirectory(path)){
//            try {
//                long result = 0;
//
//                DirectoryStream<Path> stream = Files.newDirectoryStream(path);
//
//                for (Path p : stream) {
//                    if (!Files.isDirectory(p)) result += Files.size(p);
//                    else result += getSize(p);
//                }
//                return result;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        else {
//            try {
//                return Files.size(path);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return -1;
//    }

    private long getSize(Path path) {
        Path ps = Paths.get("C:\\Users\\marse\\AppData\\Local\\Application Data");
        try{
            long result = Files.walk(path).filter(new Predicate<Path>() {
                @Override
                public boolean test(Path path) {
                    return Files.isRegularFile(path) && Files.isReadable(path);
                }
            }).mapToLong(p -> {
                try {
                    return Files.size(p);
                } catch (SecurityException e) {
                    System.err.println("Access Denied: " + p);
                    return 0L; // Skip this file or folder
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
                }).sum();
            return result;
        } catch (Exception e){
            if (e instanceof IOException) {
                if(Files.exists(path)) throw new RuntimeException("I/O exception occurred");
                else throw new RuntimeException("File does not exist.");
            }
            else if (e instanceof UncheckedIOException && e.getCause() instanceof AccessDeniedException)
                throw new RuntimeException("Access to directory with path \"" + ((AccessDeniedException) e.getCause()).getFile() + "\" has been denied");
            throw new RuntimeException(e.getMessage());
        }
    }

    public static List<Path> getChildFolders(Path path){
        List<Path> folderList = new ArrayList<>();
        if (Files.isDirectory(path)) {
            Iterator<Path> iterator;
            try {
                iterator = Files.newDirectoryStream(path).iterator();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            while (iterator.hasNext()) {
                Path p = iterator.next();
                if(Files.isDirectory(p)) folderList.add(p.toAbsolutePath());
            }
        }
        return folderList;
    }
}

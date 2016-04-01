package htmlParser;

import java.io.*;

public class ContentFileWriter {
    private PrintWriter printWriter;
    private boolean isAppendMode;
    private String fileName;

    public ContentFileWriter(String fileName, boolean isAppendMode) {
        this.fileName = fileName;
        this.isAppendMode = isAppendMode;
        // clears existing files
        this.printWriter = getPrintWriterForFile(this.fileName, false);
        close();
        // assigns new writer.
        this.printWriter = getPrintWriterForFile(fileName, isAppendMode);
    }

    public void close() {
        if (this.printWriter != null) {
            this.printWriter.close();
        }
    }

    public void write(String content) {
        if (this.printWriter != null) {
            printContentToPrintWriter(this.printWriter, content);
        }
    }

    private static PrintWriter getPrintWriterForFile(String fileName, boolean isAppendMode) {
        createFoldersIfMissingForFile(fileName);
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new BufferedWriter(new FileWriter(fileName, false)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return printWriter;
    }

    private static void printContentToPrintWriter( PrintWriter out, String content) {
        out.println(content);
    }

    private static void createFoldersIfMissingForFile(String fileName) {
        File filepath = new File(fileName);
        File parentFolderPath = filepath.getParentFile();
        parentFolderPath.mkdirs();
    }
}

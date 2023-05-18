package integration;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This class is responsible for the logging of the program.
 */
public class LogWriter {
    
    private PrintWriter logFile;
    
    /**
     * Creates a new instance of LogHandler.
     * @throws IOException 
     */
    public LogWriter() throws IOException {
        logFile = new PrintWriter(new FileWriter("errorLog.txt"), true);
    }
    
    /**
     * Writes to the log describing an exception that was thrown.
     * 
     * @param exception The exception to be logged.
     */
    public void logException(Exception exception) {
        StringBuilder logMsgBuilder = new StringBuilder();
        
        logMsgBuilder.append(", Exception thrown: ");
        logMsgBuilder.append(exception.getMessage());
        logFile.println(logMsgBuilder);
        exception.printStackTrace(logFile);
    }
    
    
}
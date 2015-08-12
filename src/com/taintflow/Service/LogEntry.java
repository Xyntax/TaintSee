package com.taintflow.Service;

public class LogEntry {
    private static final String LOGTAG = LogEntry.class.getSimpleName();

    private String timestamp;
    private int pid;
    private String tag;
    private String message;

    private LogEntry() {
    }
    
    public static LogEntry fromLine(String line) {
        String[] tokens = line.split("\\s+");
        
        // skip over "--------- beginning of /dev/log/system" etc
        if (tokens[0].equals("---------"))
            return null;
        
        LogEntry le = new LogEntry();
        
        le.timestamp = tokens[0]+" "+tokens[1];
        
        le.tag = tokens[2].substring(tokens[2].indexOf("/"),tokens[2].indexOf("("));
        
        le.pid = Integer.valueOf((line.substring(line.indexOf("(")+1,line.indexOf(")"))).trim());
        
        int messageStart = line.indexOf("): ");
        le.message = line.substring(messageStart);
        
        return le;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public int getPid() {
        return this.pid;
    }

    public String getTag() {
        return this.tag;
    }

    public String getMessage() {
        return this.message;
    }
}

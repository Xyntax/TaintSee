package com.taintflow.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LogcatDevice {
    private static final String LOGTAG = LogcatDevice.class.getSimpleName();

    private Process logcatProcess = null;
    private BufferedReader br = null;

    private LogcatDevice()
    {
    }

    private static LogcatDevice instance = new LogcatDevice();

    public static LogcatDevice getInstance() {
        return instance;
    }

    public void open() throws IOException {
        this.logcatProcess = Runtime.getRuntime().exec("logcat -v time *:S TaintLog:*");
        this.br = new BufferedReader(new InputStreamReader(logcatProcess.getInputStream()));
    }

    public boolean isOpen() {
        return(this.br != null);
    }

    public LogEntry readLogEntry() throws IOException {
        if(!isOpen()) {
            throw new IOException("must open log first");
        }

        String line = this.br.readLine();
        LogEntry le = LogEntry.fromLine(line);
        return le;
    }

    public void close() throws IOException {
        if(isOpen()) {
            this.logcatProcess.destroy();
            this.br.close();
            this.br = null;
        }
    }
}

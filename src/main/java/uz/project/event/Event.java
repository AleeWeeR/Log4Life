package uz.project.event;

import java.time.LocalDateTime;

// Data member Event class that helds single log line
public class Event {

    private LocalDateTime timestamp;
    private String threadName;
    private LogLevel logLevel;
    private String className;
    private String payload;


    public Event() {
    }

    public Event(LocalDateTime timestamp, String threadName, LogLevel logLevel, String className, String payload) {
        this.timestamp = timestamp;
        this.threadName = threadName;
        this.logLevel = logLevel;
        this.className = className;
        this.payload = payload;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getThreadName() {
        return threadName;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public String getClassName() {
        return className;
    }

    public String getPayload() {
        return payload;
    }



    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }




    @Override
    public String toString() {
        return "Event {" +
                "\n\ttimestamp = " + timestamp + ",\n" +
                "\tthreadName = " + threadName + ",\n" +
                "\tlogLevel = " + logLevel + ",\n" +
                "\tclassName = " + className + ",\n" +
                "\tpayload = " + payload  +
                "\n}";
    }
}

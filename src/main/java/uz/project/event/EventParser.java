package uz.project.event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Parser for log lines into event class
public class EventParser {

    // Regexp for validating log line structure
    private static final String LOG_PATTERN = "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}) \\[(.+?)\\] \\[(.+?)\\] (.+?) - (.*)";


    // Tokenizing all log lines into list of events.
    // If not matching log line is found it will tokenize all content to the payload field
    public List<Event> handleEvents(List<String> logLines) {
        List<Event> events = new ArrayList<>();
        Event currentEvent = null;
        // String builder for collecting all non matching events to one event payload
        StringBuilder nonMatchingPayload = new StringBuilder();

        for (String logLine : logLines) {
            Pattern pattern = Pattern.compile(LOG_PATTERN);
            Matcher matcher = pattern.matcher(logLine);

            if (matcher.matches()) {
                // log line not matches and parses to payload field of class
                if (!nonMatchingPayload.isEmpty()) {
                    Event nonMatchingEvent = new Event();
                    nonMatchingEvent.setPayload(nonMatchingPayload.toString().trim());
                    events.add(nonMatchingEvent);
                    nonMatchingPayload.setLength(0);
                }

                // log line matches and parses to event class
                currentEvent = parseLogEntry(matcher);
                events.add(currentEvent);
            } else {
                nonMatchingPayload.append(logLine).append("\n");
            }
        }

        if (!nonMatchingPayload.isEmpty()) {
            Event nonMatchingEvent = new Event();
            nonMatchingEvent.setPayload(nonMatchingPayload.toString().trim());
            events.add(nonMatchingEvent);
        }

        return events;
    }

    // Parser that populates log line to the Event class
    private Event parseLogEntry(Matcher matcher) {
        Event event = new Event();
        try {
            LocalDateTime timestamp = parseTimestamp(matcher.group(1));
            LogLevel logLevel = LogLevel.valueOf(matcher.group(3).trim());

            event.setTimestamp(timestamp);
            event.setThreadName(matcher.group(2));
            event.setLogLevel(logLevel);
            event.setClassName(matcher.group(4));
            event.setPayload(matcher.group(5));

        } catch (DateTimeParseException | IllegalArgumentException e) {
            System.err.println("Error parsing log entry: " + e.getMessage());
            event.setPayload(matcher.group(5));
        }
        return event;
    }
    // Parsing timestamp string to LocalDateTime
    private static LocalDateTime parseTimestamp(String timestampStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");
        return LocalDateTime.parse(timestampStr, formatter);
    }
}

package uz.project.filter;

import uz.project.event.Event;
import uz.project.operators.LogicalOperator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;


// Filter request evaluator class
public class EventFilter {

    private static final DateTimeFormatter DATE_TIME_FORMATTER= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    // Method that returns list of events that matches to filter request.
    public List<Event> applyFilters(List<Event> events, List<LogicalOperator> conditionGroups) {
        // Declaring result list
        List<Event> filteredEvents = new ArrayList<>();
        int startIndex = 0;
        Event prevEvent = null;

        // Looping on condition list
        for (int i = 0; i < conditionGroups.size(); i++) {
            LogicalOperator currentCondition = conditionGroups.get(i);
            List<Event> currentMatches = new ArrayList<>();

            // Looping on events to match conditions
            for (int j = startIndex; j < events.size(); j++) {
                Event event = events.get(j);

                // Checking if event is valid and matches to the condition
                if (eventIsValid(event) && currentCondition.evaluate(event, this, prevEvent)) {
                    if (i < conditionGroups.size() - 1) {
                        // If not the last condition add to result list, move to the next condition
                        currentMatches.add(event);
                        filteredEvents.add(event);
                        prevEvent = event;
                        startIndex = j + 1;
                        break;
                    } else {
                        // For the last filter condition, collect all matched events to result list
                        currentMatches.add(event);
                    }
                }
            }

            if (i == conditionGroups.size() - 1) {
                filteredEvents.addAll(currentMatches);
            }

            // If no matches were found for the current condition group, stop processing further
            if (currentMatches.isEmpty() && i < conditionGroups.size() - 1) {
                break;
            }
        }
        return filteredEvents;
    }

    // Method that returns field value from an event
    public String getFieldValue(Event event, String field) {
        return switch (field) {
            case "TIMESTAMP" -> event.getTimestamp().toString();
            case "THREAD_NAME" -> event.getThreadName();
            case "LOG_LEVEL" -> event.getLogLevel().toString();
            case "CLASS_NAME" -> event.getClassName();
            case "PAYLOAD" -> event.getPayload();
            default -> throw new IllegalArgumentException("Unknown field: " + field);
        };
    }

    // Method that returns whether an event amatches to the condition or not
    public boolean matchCondition(Event event, Condition condition) {
        String field = condition.getField();
        String operator = condition.getOperator();
        String value = condition.getValue();

        return switch (field) {
            case "TIMESTAMP" -> matchTimestamp(event, operator, value);
            case "THREAD_NAME" -> matchString(event.getThreadName(), operator, value);
            case "LOG_LEVEL" -> matchString(event.getLogLevel().toString(), operator, value);
            case "CLASS_NAME" -> matchString(event.getClassName(), operator, value);
            case "PAYLOAD" -> matchString(event.getPayload(), operator, value);
            default -> throw new IllegalArgumentException("Unknown field: " + field);
        };
    }


    // Method to match the Timestamp field of an event.
    private boolean matchTimestamp(Event event, String operator, String value) {
        LocalDateTime eventTimestamp = event.getTimestamp();
        try {
            LocalDateTime valueTimestamp = LocalDateTime.parse(value, DATE_TIME_FORMATTER);
            return compareTimestamp(eventTimestamp, valueTimestamp, operator);
        } catch (DateTimeParseException e) {
            try {
                LocalDate valueDate = LocalDate.parse(value, DATE_FORMATTER);
                return compareDate(eventTimestamp.toLocalDate(), valueDate, operator);
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("Invalid date/time format: " + value);
            }
        }
    }

    // Method to compare Date and time (yyyy-MM-dd HH:mm:ss,SSS)
    private boolean compareTimestamp(LocalDateTime eventTimestamp, LocalDateTime valueTimestamp, String operator) {
        return switch (operator) {
            case "==" -> eventTimestamp.isEqual(valueTimestamp);
            case "!=" -> !eventTimestamp.isEqual(valueTimestamp);
            case ">" -> eventTimestamp.isAfter(valueTimestamp);
            case ">=" -> eventTimestamp.isAfter(valueTimestamp) || eventTimestamp.isEqual(valueTimestamp);
            case "<" -> eventTimestamp.isBefore(valueTimestamp);
            case "<=" -> eventTimestamp.isBefore(valueTimestamp) || eventTimestamp.isEqual(valueTimestamp);
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }

    // Method to compare Date (yyyy-MM-dd)
    private boolean compareDate(LocalDate eventDate, LocalDate valueDate, String operator) {
        return switch (operator) {
            case "==" -> eventDate.isEqual(valueDate);
            case "!=" -> !eventDate.isEqual(valueDate);
            case ">" -> eventDate.isAfter(valueDate);
            case ">=" -> eventDate.isAfter(valueDate) || eventDate.isEqual(valueDate);
            case "<" -> eventDate.isBefore(valueDate);
            case "<=" -> eventDate.isBefore(valueDate) || eventDate.isEqual(valueDate);
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }

    // Method to compare other String fields of an event except Timestamp.
    private boolean matchString(String fieldValue, String operator, String value) {
        return switch (operator) {
            case "==" -> fieldValue.equals(value);
            case "!=" -> !fieldValue.equals(value);
            case "~" -> fieldValue.contains(value);
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }

    // Method to know whether event is valid or not for filtering
    private boolean eventIsValid(Event event) {
        return event.getTimestamp() != null && event.getThreadName() != null && event.getLogLevel() != null
                && event.getClassName() != null && event.getPayload() != null;
    }

    // Method that returns total count of matched events
    public int countLogs(List<Event> filteredEvents) {
        return filteredEvents.size();
    }
}

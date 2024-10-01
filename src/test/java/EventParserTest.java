import org.junit.Test;
import uz.project.event.Event;
import uz.project.event.EventParser;
import uz.project.event.LogLevel;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class EventParserTest {

    @Test
    public void testParseLogLinesToEvents() {
        // A list of log lines
        List<String> logLines = Arrays.asList(
                "2024-06-17 12:00:00,149 [DivaRestoreRequestDispatcher-70] [DEBUG] c.r.divabridge.impl.DivaTask - Fetching request info for request: RestoreInstance(167619)",
                "2024-06-17 12:00:01,149 [main] [INFO ] c.r.divabridge.impl.DivaTask - Another log entry"
        );

        // Parsing log lines to events
        EventParser eventParser = new EventParser();
        List<Event> events = eventParser.handleEvents(logLines);

        // Validate that the correct number of events were parsed
        assertNotNull(events);
        assertEquals(2, events.size());

        // Validate the content of the first event
        Event event1 = events.get(0);
        assertEquals(LocalDateTime.of(2024, 6, 17, 12, 0, 0, 149000000), event1.getTimestamp());
        assertEquals("DivaRestoreRequestDispatcher-70", event1.getThreadName());
        assertEquals(LogLevel.DEBUG, event1.getLogLevel());
        assertEquals("c.r.divabridge.impl.DivaTask", event1.getClassName());
        assertEquals("Fetching request info for request: RestoreInstance(167619)", event1.getPayload());

        // Validate the content of the second event
        Event event2 = events.get(1);
        assertEquals(LocalDateTime.of(2024, 6, 17, 12, 0, 1, 149000000), event2.getTimestamp());
        assertEquals("main", event2.getThreadName());
        assertEquals(LogLevel.INFO, event2.getLogLevel());
        assertEquals("c.r.divabridge.impl.DivaTask", event2.getClassName());
        assertEquals("Another log entry", event2.getPayload());
    }

    @Test
    public void testParseInvalidLogLine() {
        // A list with an invalid log line
        List<String> logLines = Arrays.asList(
                "INVALID LOG LINE"
        );

        // Parsing log lines to events
        EventParser eventParser = new EventParser();
        List<Event> events = eventParser.handleEvents(logLines);

        // Validate that the event is null or has null fields, depending on the implementation
        assertNotNull(events);
        assertEquals(1, events.size());

        Event event = events.get(0);
        assertNull(event.getTimestamp());
        assertNull(event.getThreadName());
        assertNull(event.getLogLevel());
        assertNull(event.getClassName());
        assertEquals("INVALID LOG LINE", event.getPayload());
    }
}

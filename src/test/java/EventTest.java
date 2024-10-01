import org.junit.Test;
import static org.junit.Assert.*;

import uz.project.event.Event;
import uz.project.event.LogLevel;

import java.time.LocalDateTime;



public class EventTest {

    @Test
    public void testEventCreation() {
        LocalDateTime timestamp = LocalDateTime.of(2024, 6, 17, 12, 0, 0, 149000000);
        String threadName = "DivaRestoreRequestDispatcher-70";
        LogLevel logLevel = LogLevel.DEBUG;
        String className = "c.r.divabridge.impl.DivaTask";
        String payload = "Fetching request info for request: RestoreInstance(167619)";

        Event event = new Event(timestamp, threadName, logLevel, className, payload);

        assertEquals(timestamp, event.getTimestamp());
        assertEquals(threadName, event.getThreadName());
        assertEquals(logLevel, event.getLogLevel());
        assertEquals(className, event.getClassName());
        assertEquals(payload, event.getPayload());
    }
}
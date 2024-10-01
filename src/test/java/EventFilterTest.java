import org.junit.Test;
import uz.project.event.Event;
import uz.project.event.LogLevel;
import uz.project.filter.EventFilter;
import uz.project.filter.FilterParser;
import uz.project.operators.LogicalOperator;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class EventFilterTest {

    @Test
    public void testApplyFilters() {
        EventFilter eventFilter = new EventFilter();
        FilterParser parser = new FilterParser();

        Event event1 = new Event(LocalDateTime.of(2024, 6, 17, 12, 0, 0), "DivaRestoreRequestDispatcher-70", LogLevel.DEBUG, "c.r.divabridge.impl.DivaTask", "RestoreRequest(167619)");
        Event event2 = new Event(LocalDateTime.of(2024, 6, 17, 12, 0, 0), "main", LogLevel.INFO, "c.r.divabridge.impl.DivaTask", "Another log entry");

        List<Event> events = Arrays.asList(event1, event2);

        String filterRequest = "(LOG_LEVEL == 'INFO' and THREAD_NAME == 'main')";
        List<LogicalOperator> conditions = parser.parseFilterRequest(filterRequest);

        List<Event> filteredEvents = eventFilter.applyFilters(events, conditions);
        assertEquals(1, filteredEvents.size());
        assertEquals(event2, filteredEvents.get(0));
    }
}

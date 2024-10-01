import org.junit.Test;
import uz.project.event.LogLevel;

import static org.junit.Assert.*;

public class LogLevelTest {

    @Test
    public void testLogLevelValues() {
        LogLevel info = LogLevel.INFO;
        LogLevel debug = LogLevel.DEBUG;
        LogLevel error = LogLevel.ERROR;

        assertEquals("INFO", info.toString());
        assertEquals("DEBUG", debug.toString());
        assertEquals("ERROR", error.toString());
    }
}

import org.junit.Test;
import uz.project.filter.FilterParser;
import uz.project.operators.AndOperator;
import uz.project.operators.ConditionNode;
import uz.project.operators.LogicalOperator;
import uz.project.operators.OrOperator;

import static org.junit.Assert.*;


public class FilterParserTest {

    @Test
    public void testParseSimpleFilterRequest() {
        FilterParser parser = new FilterParser();
        String filterRequest = "(LOG_LEVEL == 'INFO' and THREAD_NAME == 'main')";

        LogicalOperator rootCondition = parser.parseFilterRequest(filterRequest).get(0);
        assertNotNull(rootCondition);

        assertTrue(rootCondition instanceof AndOperator);

        AndOperator andOperator = (AndOperator) rootCondition;
        assertTrue(andOperator.getLeft() instanceof ConditionNode);
        assertTrue(andOperator.getRight() instanceof ConditionNode);

        ConditionNode leftCondition = (ConditionNode) andOperator.getLeft();
        ConditionNode rightCondition = (ConditionNode) andOperator.getRight();

        assertEquals("LOG_LEVEL", leftCondition.getCondition().getField());
        assertEquals("INFO", leftCondition.getCondition().getValue());

        assertEquals("THREAD_NAME", rightCondition.getCondition().getField());
        assertEquals("main", rightCondition.getCondition().getValue());
    }

    @Test
    public void testParseComplexFilterRequest() {
        FilterParser parser = new FilterParser();
        String filterRequest = "(LOG_LEVEL == 'INFO' or THREAD_NAME == 'main' and CLASS_NAME == 'Example')";

        LogicalOperator rootCondition = parser.parseFilterRequest(filterRequest).get(0);
        assertNotNull(rootCondition);

        assertTrue(rootCondition instanceof OrOperator);

        OrOperator orOperator = (OrOperator) rootCondition;
        assertTrue(orOperator.getLeft() instanceof ConditionNode);
        assertTrue(orOperator.getRight() instanceof AndOperator);
    }
}

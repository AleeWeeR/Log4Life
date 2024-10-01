package uz.project.operators;

import uz.project.event.Event;
import uz.project.filter.EventFilter;


//Represents logical AND operations.
public class AndOperator extends LogicalOperator {
    public AndOperator(LogicalOperator left, LogicalOperator right) {
        super(left, right);
    }

    @Override
    public boolean evaluate(Event event, EventFilter eventFilter, Event prevEvent) {
        return left.evaluate(event, eventFilter, prevEvent) && right.evaluate(event, eventFilter, prevEvent);
    }
}

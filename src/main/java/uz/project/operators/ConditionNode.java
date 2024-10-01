package uz.project.operators;

import uz.project.event.Event;
import uz.project.filter.Condition;
import uz.project.filter.EventFilter;

//Represents individual conditions, as head of complex conditions
public class ConditionNode extends LogicalOperator {
    private Condition condition;

    public ConditionNode(Condition condition) {
        super(null, null);
        this.condition = condition;
    }

    // Evaluates the expression.
    // If it has prev keyword it will assing new Condtion by replacing prev.FIELD to event field from previous match.
    @Override
    public boolean evaluate(Event event, EventFilter eventFilter, Event prevEvent) {
        if (condition.isPrevCondition()) {
            String prevFieldValue = eventFilter.getFieldValue(prevEvent, condition.getField());
            return eventFilter.matchCondition(event, new Condition(condition.getField(), condition.getOperator(), prevFieldValue));
        } else {
            return eventFilter.matchCondition(event, condition);
        }
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }
}

package uz.project.operators;

import uz.project.event.Event;
import uz.project.filter.EventFilter;

//The base class for representing logical expressions.
public abstract class LogicalOperator {
    protected LogicalOperator left;
    protected LogicalOperator right;

    public LogicalOperator(LogicalOperator left, LogicalOperator right) {
        this.left = left;
        this.right = right;
    }

    public abstract boolean evaluate(Event event, EventFilter eventFilter, Event prevEvent);

    public LogicalOperator getLeft() {
        return left;
    }

    public void setLeft(LogicalOperator left) {
        this.left = left;
    }

    public LogicalOperator getRight() {
        return right;
    }

    public void setRight(LogicalOperator right) {
        this.right = right;
    }
}



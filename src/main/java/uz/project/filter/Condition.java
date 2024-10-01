package uz.project.filter;

// Data member that held single Conditon from filter request
public class Condition {
    private String field;
    private String operator;
    private String value;
    private boolean isPrevCondition;

    public Condition(String field, String operator, String value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
        this.isPrevCondition = value.startsWith("prev.");
    }

    public String getField() {
        return field;
    }

    public String getOperator() {
        return operator;
    }

    public String getValue() {
        return value;
    }

    public boolean isPrevCondition() {
        return isPrevCondition;
    }
}

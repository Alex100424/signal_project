package com.alerts.decorators;

import com.alerts.Alert;

/**
 * Decorator that adds priority label to an alert
 *
 * Eg
 * First condition: Critical SystolicPressure
 * Decorated conditionȘ [Priority: HIGH] Critical SystolicPressure
 */
public class PriorityAlertDecorator extends AlertDecorator {

    private String priority;

    /**
     * Creates a priority alert decorator.
     *
     * @param wrappedAlert original alert
     * @param priority the priority lvl ("HIGH" or "URGENT")
     */
    public PriorityAlertDecorator(Alert wrappedAlert, String priority) {
        super(wrappedAlert);
        this.priority = priority;
    }

    /**
     * Returns alert condition with priority information
     *
     * @return decorated alert condition
     */
    @Override
    public String getCondition() {
        return "[Priority: " + priority + "] " + wrappedAlert.getCondition();
    }

    /**
     * Returns priority lvl of alert
     *
     * @return priority lvk
     */
    public String getPriority() {
        return priority;
    }
}
package com.alerts.decorators;

import com.alerts.Alert;

/**
 * Base decorator for Alert objects.
 *
 * This class wraps an existing Alert and allows subclasses to extend or modify
 * its behaviour without changing the original Alert class.
 */
public abstract class AlertDecorator extends Alert {

    protected Alert wrappedAlert;

    /**
     * Creates a decorator around an alert
     *
     * @param wrappedAlert original alert
     */
    public AlertDecorator(Alert wrappedAlert) {
        super(wrappedAlert.getPatientId(), wrappedAlert.getCondition(), wrappedAlert.getTimestamp());
        this.wrappedAlert = wrappedAlert;
    }

    @Override
    public String getPatientId() {
        return wrappedAlert.getPatientId();
    }

    public String getCondition() {
        return wrappedAlert.getCondition();
    }

    @Override
    public long getTimestamp() {
        return wrappedAlert.getTimestamp();
    }
}
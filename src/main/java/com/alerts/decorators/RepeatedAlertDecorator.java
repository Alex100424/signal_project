package com.alerts.decorators;

import com.alerts.Alert;

/**
 * Decorator that adds repeated alert information to a alert
 */
public class RepeatedAlertDecorator extends AlertDecorator {

    private int count;
    private long interval;

    /**
     * Creates a repeated alert decorator.
     *
     * @param wrappedAlert original alert
     * @param count how many times the alert should be repeated
     * @param interval interval between repetitions in milisec
     */
    public RepeatedAlertDecorator(Alert wrappedAlert, int count, long interval) {
        super(wrappedAlert);
        this.count = count;
        this.interval = interval;
    }

    /**
     * Returns the alert condition with repeated-alert information added.
     *
     * @return the decorated alert condition
     */
    @Override
    public String getCondition() {
        return wrappedAlert.getCondition()
                + " [Repeated " + count
                + " times every " + interval + " ms]";
    }

    /**
     * Returns how many times this alert should be repeated
     *
     * @return count
     */
    public int getCount() {
        return count;
    }

    /**
     * Returns the intercal between repeated alerts
     *
     * @return  repeat interval in milisec
     */
    public long getInterval() {
        return interval;
    }
}
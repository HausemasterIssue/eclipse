package xyz.aesthetical.eclipse.features.settings;

public class NumberSetting extends Setting<Number> {
    private Number min = 0.0f;
    private Number max = 1.0f;

    public NumberSetting(String name, Number defaultValue) {
        super(name, defaultValue);
    }

    public NumberSetting setMin(Number min) {
        this.min = min;
        return this;
    }

    public Number getMin() {
        return min;
    }

    public NumberSetting setMax(Number max) {
        this.max = max;
        return this;
    }

    public Number getMax() {
        return max;
    }

    public boolean isInRange(Number value) {
        return value.floatValue() > min.floatValue() && value.floatValue() < max.floatValue();
    }
}

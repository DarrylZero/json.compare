package com.steammachine.jsonchecker.defaults;

import com.steammachine.jsonchecker.types.Identity;
import com.steammachine.jsonchecker.types.UnmappedProperties;
import com.steammachine.jsonchecker.types.SqNodeKeyType;

import java.util.HashMap;
import java.util.Map;

/**
 * 30.12.2017 10:21:45
 *
 * @author Vladimir Bogodukhov
 *         {@link DefaultWidgetNode }
 **/
public class DefaultWidgetNode implements UnmappedProperties, Identity<SqNodeKeyType> {

    private String monkeyId;
    private String componentType;
    private boolean visible;
    private String className;
    private String type;
    private String value;
    private final Map<String, String> unmapped = new HashMap<>();

    public void setValue(String value) {
        this.value = value;
    }

    public void setMonkeyId(String monkeyId) {
        this.monkeyId = monkeyId;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String value() {
        return value;
    }

    public String monkeyId() {
        return monkeyId;
    }

    public String componentType() {
        return componentType;
    }

    public boolean visible() {
        return visible;
    }

    public String className() {
        return className;
    }

    public String type() {
        return type;
    }

    public Map<String, String> unmapped() {
        return unmapped;
    }

    @Override
    public void setValue(String propertyName, String value) {
        unmapped.put(propertyName, value);
    }

    @Override
    public String toString() {
        return "DefaultWidgetNodeType{" +
                "monkeyId='" + monkeyId + '\'' +
                ", componentType='" + componentType + '\'' +
                ", VISIBLE=" + visible +
                ", className='" + className + '\'' +
                ", type='" + type + '\'' +
                ", VALUE='" + value + '\'' +
                ", unmapped=" + unmapped +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DefaultWidgetNode)) return false;

        DefaultWidgetNode that = (DefaultWidgetNode) o;

        if (visible != that.visible) return false;
        if (monkeyId != null ? !monkeyId.equals(that.monkeyId) : that.monkeyId != null) return false;
        if (componentType != null ? !componentType.equals(that.componentType) : that.componentType != null)
            return false;
        if (className != null ? !className.equals(that.className) : that.className != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        return unmapped.equals(that.unmapped);
    }

    @Override
    public int hashCode() {
        int result = monkeyId != null ? monkeyId.hashCode() : 0;
        result = 31 * result + (componentType != null ? componentType.hashCode() : 0);
        result = 31 * result + (visible ? 1 : 0);
        result = 31 * result + (className != null ? className.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + unmapped.hashCode();
        return result;
    }

/* ------------------------------------------ methods of Identity interface ----------------------------------------- */

    @Override
    public boolean supports(Class<?> identityType) {
        return SqNodeKeyType.class == identityType;
    }

    @Override
    public String ident(SqNodeKeyType identityType) {
        switch (identityType) {
            case componentType: {
                return componentType;
            }

            case monkeyId: {
                return monkeyId;
            }

            default:
                throw new IllegalStateException("unknown identityType " + identityType);
        }
    }
}

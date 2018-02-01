package com.steammachine.jsonchecker.defaults;

import com.steammachine.jsonchecker.types.*;

import java.util.*;

/**
 * 30.12.2017 10:21:45
 *
 * @author Vladimir Bogodukhov
 **/
public class DefaultWidgetNodeChecker
        implements UnmappedProperties, Identity<SqNodeKeyType>, NodeChecker<DefaultWidgetNode> {

    private static final String ELEMENT_BY_PATH = "element by path";

    public enum Items {
        VISIBLE,
        VALUE
    }

    private static final List<Items> DEFAULT_ITEMS = Arrays.asList(Items.VALUE, Items.VISIBLE);
    private Set<Items> items = new HashSet<>(DEFAULT_ITEMS);
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


    @Override
    public boolean checks(Class type) {
        return DefaultWidgetNode.class.isAssignableFrom(type);
    }

    @Override
    public void check(DefaultWidgetNode widgetNode, String path, NodeCheckContext context) {
        check(widgetNode, path, context, this);
    }

    @Override
    public void setValue(String propertyName, String value) {
        unmapped.put(propertyName, value);
    }

    @Override
    public String toString() {
        return "DefaultWidgetNodeChecker{" +
                "items=" + items +
                ", monkeyId='" + monkeyId + '\'' +
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
        if (!(o instanceof DefaultWidgetNodeChecker)) return false;

        DefaultWidgetNodeChecker that = (DefaultWidgetNodeChecker) o;

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

    /* methods for Identity interface */

    @Override
    public boolean supports(Class<?> identityType) {
        return SqNodeKeyType.class.isAssignableFrom(identityType);
    }

    @Override
    public String ident(SqNodeKeyType identityType) {
        switch (identityType) {
            case monkeyId: {
                return this.monkeyId;
            }

            case componentType: {
                return this.componentType;
            }

            default: {
                throw new IllegalStateException("unknown identity type " + identityType);
            }
        }
    }

    private static void check(DefaultWidgetNode widgetNode, String path,
                              NodeCheckContext context, DefaultWidgetNodeChecker checker) {
        if (checker.visible() != widgetNode.visible()) {
            context.log(ELEMENT_BY_PATH,  " '", path, "'  has wrong VALUE of 'VISIBLE' actual(",
                    widgetNode.visible(), ") expected(", checker.visible(), ")");
            context.failed();
        }

        if (!Objects.equals(checker.monkeyId(), widgetNode.monkeyId())) {
            context.log(ELEMENT_BY_PATH,  " '", path, "'  has wrong VALUE of 'monkeyId' actual(",
                    widgetNode.monkeyId() + ") expected(", checker.monkeyId(), ")");
            context.failed();
        }

        if (!Objects.equals(checker.componentType(), widgetNode.componentType())) {
            context.log(ELEMENT_BY_PATH,  " '", path, "'  has wrong VALUE of 'componentType' actual(",
                    widgetNode.componentType(), ") expected(", checker.componentType(), ")");
            context.failed();
        }

        if (!Objects.equals(checker.className(), widgetNode.className())) {
            context.log(ELEMENT_BY_PATH,  " '", path, "'  has wrong VALUE of 'className' actual(",
                    widgetNode.className(), ") expected(", checker.className(), ")");
            context.failed();
        }

        if (!Objects.equals(checker.type(), widgetNode.type())) {
            context.log(ELEMENT_BY_PATH,  " '", path, "'  has wrong VALUE of 'type' actual(",
                    widgetNode.type(), ") expected(", checker.type(), ")");
            context.failed();
        }

        if (!Objects.equals(checker.value(), widgetNode.value())) {
            context.log(ELEMENT_BY_PATH,  " '", path, "'  has wrong VALUE of 'VALUE' actual(",
                    widgetNode.value(), ") expected(", checker.value(), ")");
            context.failed();
        }
    }

}

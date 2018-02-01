package com.steammachine.jsonchecker.defaults;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.steammachine.jsonchecker.types.NodeCheckContext;
import com.steammachine.jsonchecker.types.NodeChecker;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.steammachine.common.utils.commonutils.CommonUtils.eval;

/**
 * Created by Vladimir Bogodukhov  on 01.12.2017.
 *
 * @author Vladimir Bogodukhov
 */
public class DefaultWidgetNodeCheckerPairCheck {


    @Test
    public void allCorrect() {
        check(eval(() -> {
            DefaultWidgetNode checker = new DefaultWidgetNode();
            checker.setValue("VALUE");
            checker.setType("type");
            checker.setMonkeyId("monkeyId");
            checker.setComponentType("componentType");
            checker.setVisible(true);
            checker.setClassName("className");
            return checker;
        }), eval(() -> {
            DefaultWidgetNodeChecker checker = new DefaultWidgetNodeChecker();
            checker.setValue("VALUE");
            checker.setType("type");
            checker.setMonkeyId("monkeyId");
            checker.setComponentType("componentType");
            checker.setVisible(true);
            checker.setClassName("className");
            return checker;
        }), ResultNodeCheckContext::new, "/path", t -> Assertions.assertTrue(t.isSuccessful()));
    }


    @Test
    public void setValue() {
        check(eval(() -> {
            DefaultWidgetNode checker = new DefaultWidgetNode();
            checker.setValue("value2");
            checker.setType("type");
            checker.setMonkeyId("monkeyId");
            checker.setComponentType("componentType");
            checker.setVisible(true);
            checker.setClassName("className");
            return checker;
        }), eval(() -> {
            DefaultWidgetNodeChecker checker = new DefaultWidgetNodeChecker();
            checker.setValue("VALUE");
            checker.setType("type");
            checker.setMonkeyId("monkeyId");
            checker.setComponentType("componentType");
            checker.setVisible(true);
            checker.setClassName("className");
            return checker;
        }), ResultNodeCheckContext::new, "/path", t -> {
            Assertions.assertFalse(t.isSuccessful());
            Assertions.assertEquals(Arrays.asList("element by path '/path'  has wrong VALUE of 'VALUE' actual(value2) expected(VALUE)"),
                    t.messages());
        });
    }


    @Test
    public void setType() {
        check(eval(() -> {
            DefaultWidgetNode checker = new DefaultWidgetNode();
            checker.setValue("VALUE");
            checker.setType("wrongType");
            checker.setMonkeyId("monkeyId");
            checker.setComponentType("componentType");
            checker.setVisible(true);
            checker.setClassName("className");
            return checker;
        }), eval(() -> {
            DefaultWidgetNodeChecker checker = new DefaultWidgetNodeChecker();
            checker.setValue("VALUE");
            checker.setType("type");
            checker.setMonkeyId("monkeyId");
            checker.setComponentType("componentType");
            checker.setVisible(true);
            checker.setClassName("className");
            return checker;
        }), ResultNodeCheckContext::new, "/path", t -> {
            Assertions.assertFalse(t.isSuccessful());
            Assertions.assertEquals(Arrays.asList("element by path '/path'  has wrong VALUE of 'type' actual(wrongType) expected(type)"),
                    t.messages());
        });
    }


    @Test
    public void setMonkeyId() {
        check(eval(() -> {
            DefaultWidgetNode checker = new DefaultWidgetNode();
            checker.setValue("VALUE");
            checker.setType("type");
            checker.setMonkeyId("monkeyId2");
            checker.setComponentType("componentType");
            checker.setVisible(true);
            checker.setClassName("className");
            return checker;
        }), eval(() -> {
            DefaultWidgetNodeChecker checker = new DefaultWidgetNodeChecker();
            checker.setValue("VALUE");
            checker.setType("type");
            checker.setMonkeyId("monkeyId");
            checker.setComponentType("componentType");
            checker.setVisible(true);
            checker.setClassName("className");
            return checker;
        }), ResultNodeCheckContext::new, "/path", t -> {
            Assertions.assertFalse(t.isSuccessful());
            Assertions.assertEquals(Arrays.asList("element by path '/path'  has wrong VALUE of 'monkeyId' actual(monkeyId2) expected(monkeyId)"),
                    t.messages());
        });
    }


    @Test
    public void setComponentType() {
        check(eval(() -> {
            DefaultWidgetNode checker = new DefaultWidgetNode();
            checker.setValue("VALUE");
            checker.setType("type");
            checker.setMonkeyId("monkeyId");
            checker.setComponentType("componentType2");
            checker.setVisible(true);
            checker.setClassName("className");
            return checker;
        }), eval(() -> {
            DefaultWidgetNodeChecker checker = new DefaultWidgetNodeChecker();
            checker.setValue("VALUE");
            checker.setType("type");
            checker.setMonkeyId("monkeyId");
            checker.setComponentType("componentType");
            checker.setVisible(true);
            checker.setClassName("className");
            return checker;
        }), ResultNodeCheckContext::new, "/path", t -> {
            Assertions.assertFalse(t.isSuccessful());
            Assertions.assertEquals(Arrays.asList("element by path '/path'  has wrong VALUE of 'componentType' actual(componentType2) expected(componentType)"),
                    t.messages());
        });
    }

    @Test
    public void setVisible() {
        check(eval(() -> {
            DefaultWidgetNode checker = new DefaultWidgetNode();
            checker.setValue("VALUE");
            checker.setType("type");
            checker.setMonkeyId("monkeyId");
            checker.setComponentType("componentType");
            checker.setVisible(false);
            checker.setClassName("className");
            return checker;
        }), eval(() -> {
            DefaultWidgetNodeChecker checker = new DefaultWidgetNodeChecker();
            checker.setValue("VALUE");
            checker.setType("type");
            checker.setMonkeyId("monkeyId");
            checker.setComponentType("componentType");
            checker.setVisible(true);
            checker.setClassName("className");
            return checker;
        }), ResultNodeCheckContext::new, "/path", t -> {
            Assertions.assertFalse(t.isSuccessful());
            Assertions.assertEquals(Arrays.asList("element by path '/path'  has wrong VALUE of 'VISIBLE' actual(false) expected(true)"), t.messages());
        });
    }

    @Test
    public void setClassName() {
        check(eval(() -> {
            DefaultWidgetNode checker = new DefaultWidgetNode();
            checker.setValue("VALUE");
            checker.setType("type");
            checker.setMonkeyId("monkeyId");
            checker.setComponentType("componentType");
            checker.setVisible(true);
            checker.setClassName("className2");
            return checker;
        }), eval(() -> {
            DefaultWidgetNodeChecker checker = new DefaultWidgetNodeChecker();
            checker.setValue("VALUE");
            checker.setType("type");
            checker.setMonkeyId("monkeyId");
            checker.setComponentType("componentType");
            checker.setVisible(true);
            checker.setClassName("className");
            return checker;
        }), ResultNodeCheckContext::new, "/path", t -> {
            Assertions.assertFalse(t.isSuccessful());
            Assertions.assertEquals(Arrays.asList("element by path '/path'  has wrong VALUE of 'className' actual(className2) expected(className)"), t.messages());
        });
    }


    /* ------------------------------------------------- privates -------------------------------------------------- */

    @SuppressWarnings("unchecked")
    private static <T extends NodeCheckContext> void check(
            Object object,
            NodeChecker checker,
            Supplier<T> contextFactory,
            String fullPath,
            Consumer<T> checkActions) {

        Objects.requireNonNull(object);
        Objects.requireNonNull(contextFactory);
        Objects.requireNonNull(checker);
        Objects.requireNonNull(fullPath);
        Objects.requireNonNull(checkActions);

        Assertions.assertTrue(checker.checks(object.getClass()));
        T context = contextFactory.get();
        checker.check(object, fullPath, context);
        checkActions.accept(context);
    }

}
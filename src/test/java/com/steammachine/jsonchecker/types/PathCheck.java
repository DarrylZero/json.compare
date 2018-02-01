package com.steammachine.jsonchecker.types;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.steammachine.jsonchecker.impl.flatter2.Id;

import java.util.Arrays;

/**
 * Created by Vladimir Bogodukhov on 01.12.2017.
 *
 * @author Vladimir Bogodukhov
 */
class PathCheck {

    @Test
    void path10() {
        Path a = Path.of().addId(Id.obj("a")).addId(Id.obj("@b"));
        Assertions.assertEquals(Arrays.asList(Id.obj("a"), Id.obj("@b")), a.elements().get(0).list());
    }

    @Test
    void path20() {
        Path a = Path.of().del().addId(Id.obj("a")).addId(Id.obj("@b")).addId(Id.obj("@c"));
        Assertions.assertEquals(Arrays.asList(Id.obj("a"), Id.obj("@b"), Id.obj("@c")), a.elements().get(0).list());
    }

    @Test
    void startsWith() {
        Path a = Path.of().del().addId(Id.obj("a")).addId(Id.obj("b")).addId(Id.obj("c"));
        Path b = Path.of().del().id(Id.anyArr()).del().id(Id.anyArr()).path();

        Assertions.assertAll(
                () -> Assertions.assertEquals(true, a.startsWith(Path.of().del())),
                () -> Assertions.assertEquals(true, a.startsWith(Path.of().del().addId(Id.obj("a")).addId(Id.obj("b")).addId(Id.obj("c")))),
                () -> Assertions.assertEquals(true, b.startsWith(Path.of().del())),
                () -> Assertions.assertEquals(true, b.startsWith(Path.of().del().id(Id.anyArr()).del())),
                () -> Assertions.assertEquals(true, b.startsWith(Path.of().del().id(Id.anyArr()).del().id(Id.anyArr()).path())),
                () -> Assertions.assertEquals(false, a.startsWith(Path.of().addId(Id.obj("b")))),
                PathCheck::nop
        );
    }

    @Test
    void endsWith() {
        Path a = Path.of().del().addId(Id.obj("a")).addId(Id.obj("b")).addId(Id.obj("c"));
        Path b = Path.of().del().id(Id.anyArr()).del().id(Id.anyArr()).path();
        Assertions.assertAll(
                () -> Assertions.assertEquals(true, a.endsWith(Path.of().addId(Id.obj("a")).addId(Id.obj("b")).addId(Id.obj("c")))),
                () -> Assertions.assertEquals(true, a.endsWith(Path.of().del().addId(Id.obj("a")).addId(Id.obj("b")).addId(Id.obj("c")))),
                () -> Assertions.assertEquals(true, b.endsWith(Path.of().id(Id.anyArr()).path())),
                () -> Assertions.assertEquals(true, b.endsWith(Path.of().del().id(Id.anyArr()).path())),
                () -> Assertions.assertEquals(true, b.endsWith(Path.of().id(Id.anyArr()).del().id(Id.anyArr()).path())),
                () -> Assertions.assertEquals(true, b.endsWith(Path.of().id(Id.anyArr()).path())),
                PathCheck::nop
        );
    }

    private static void nop() {

    }

}
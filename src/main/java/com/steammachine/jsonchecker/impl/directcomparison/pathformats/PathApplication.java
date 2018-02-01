package com.steammachine.jsonchecker.impl.directcomparison.pathformats;

import com.steammachine.jsonchecker.impl.flatter2.IdentifierType;
import com.steammachine.jsonchecker.types.PathParticle;
import com.steammachine.jsonchecker.impl.flatter2.Element;
import com.steammachine.jsonchecker.types.Path;
import com.steammachine.common.utils.commonutils.CommonUtils;

import java.util.Objects;

import static com.steammachine.common.utils.commonutils.CommonUtils.check;

/**
 * 30.12.2017 10:21:45
 *
 * @author Vladimir Bogodukhov
 **/
class PathApplication {

    private static final Path UNIVERSAL_TEMPLATE_PREFIX_2_ASTERIX = Path.builder().ast().ast().del().get();
    private static final Path UNIVERSAL_TEMPLATE_PREFIX_3_ASTERIX = Path.builder().ast().ast().ast().del().get();
    private static final Path TWO_ASTERIX_AT_THE_END = Path.builder().del().ast().ast().get();

    @Deprecated
    public static boolean isPathAppliedOld(Path path, Path template) {
        if (template.startsWith(UNIVERSAL_TEMPLATE_PREFIX_2_ASTERIX)) {
            /* Здесь мы работаем с исключением, которое относится ко всем нодам.
               **. - Все ноды которые в конце полностью совпадают с шаблоном будут исключаться.
            */
            Path universalExclusion = template.subPath(UNIVERSAL_TEMPLATE_PREFIX_2_ASTERIX.particles().size());

            if (path.elements().size() < universalExclusion.elements().size()) {
                /* Если путь короче универсального исключения считаем что исключени не применимо */
                return false;
            }

            /* Тут обрезаем путь до размера исключения - так как интересует только окончание пути */
            Path cutPathParts = path.subPath(path.particles().size() - universalExclusion.particles().size(),
                    path.particles().size());
            check(() -> cutPathParts.particles().size() == universalExclusion.particles().size(),
                    () -> new IllegalStateException("sizes are not equal"));
            check(() -> cutPathParts.elements().size() == universalExclusion.elements().size(),
                    () -> new IllegalStateException("sizes are not equal"));

            int length = universalExclusion.elements().size();
            boolean scanResult = true;
            for (int index = 0; index < length; index++) {
                scanResult = match(cutPathParts.elements().get(index),
                        universalExclusion.elements().get(index)) && scanResult;
            }
            if (!scanResult) {
                    /* Если элемент пути не соответствует данным - считаем, что исключение не применимо. */
                return false;
            }

            /* Все элементы пути соответстуют исключениям - считаем что данное исключение применимо */
            return true;
        } else if (template.startsWith(UNIVERSAL_TEMPLATE_PREFIX_3_ASTERIX)) {
            /* Здесь мы работаем с исключением, которое относится ко всем нодам.
               ***. - Все ноды которые в какой либо части своего пути полностью совпадают с шаблоном имеют будут исключаться.
            */
            Path universalExclusion = template.subPath(UNIVERSAL_TEMPLATE_PREFIX_3_ASTERIX.particles().size());
            if (path.elements().size() < universalExclusion.elements().size()) {
                /* Если путь короче универсального исключения считаем что исключени не применимо */
                return false;
            }

            /*
                 a.b.f.j.h.c.d.e -  путь - худший вариант при котором искомая часть расположена в конце
                 c.d.e - шаблон

                 итерации :
                 1. a.b.f.j.h.c.d.e -  путь
                 2. b.f.j.h.c.d.e -  путь
                 3. f.j.h.c.d.e -  путь
                 4. j.h.c.d.e -  путь
                 5. h.c.d.e -  путь
                 6. c.d.e -  путь

                 Количество итераций = (длина проверяемого пути) - (длина шаблона) + 1
            */

            int iterationsCount = path.elements().size() - universalExclusion.elements().size() + 1;
            for (int iteration = 0; iteration < iterationsCount; iteration++) {
                boolean scanResult = true;
                for (int index = 0; index < universalExclusion.elements().size(); index++) {
                    scanResult = scanResult &&
                            match(path.elements().get(iteration + index),
                                    universalExclusion.elements().get(index));
                }
                if (scanResult) {
                    /* Если элемент пути не соответствует данным - считаем, что исключение не применимо. */
                    return true;
                }
            }

            return false;
        } else {
            if (template.elements().size() == path.elements().size()) {
            /* путь исключения и путь до объека одинаковой длины */
                int length = template.elements().size();
                boolean scanResult = true;
                for (int index = 0; index < length; index++) {
                    scanResult = match(path.elements().get(index),
                            template.elements().get(index)) && scanResult;
                }

                if (!scanResult) {
                    /* Если элемент пути не соответствует данным - считаем, что исключение не применимо. */
                    return false;
                }

                /* Все элементы пути соответстуют исключениям - считаем что данное исключение применимо */
                return true;
            } else if (template.elements().size() < path.elements().size()) {
                return false;
            } else if (template.elements().size() > path.elements().size()) {
                return false;
            } else {
                throw new IllegalStateException(" Illegal State ");
            }
        }
    }

    static boolean isPathApplied(Path path, Path template) {
        if (template.endsWith(TWO_ASTERIX_AT_THE_END)) {
            return asterixesAtTheEnd(path, template);
        } else {
            return noAsterixesAtTheEnd(path, template);
        }
    }

    private static boolean noAsterixesAtTheEnd(Path path, Path template) {
        if (template.startsWith(UNIVERSAL_TEMPLATE_PREFIX_2_ASTERIX)) {

        /* Здесь мы работаем с исключением, которое относится ко всем нодам.
           **. - Все ноды которые в конце полностью совпадают с шаблоном будут исключаться.
        */
            Path universalExclusion = template.subPath(UNIVERSAL_TEMPLATE_PREFIX_2_ASTERIX.particles().size());


            if (path.elements().size() < universalExclusion.elements().size()) {
            /* Если путь короче универсального исключения считаем, что исключени не применимо */
                return false;
            }

        /* Тут обрезаем путь до размера исключения - так как интересует только окончание пути */
            Path cutPathParts = path.subPath(path.particles().size() - universalExclusion.particles().size(),
                    path.particles().size());
            check(() -> cutPathParts.particles().size() == universalExclusion.particles().size(), IllegalStateException::new);
            check(() -> cutPathParts.elements().size() == universalExclusion.elements().size(), IllegalStateException::new);
            /* Все элементы пути соответстуют исключениям - считаем, что данное исключение применимо */
            return elementsMatch(universalExclusion, cutPathParts, 0);
        } else if (template.startsWith(UNIVERSAL_TEMPLATE_PREFIX_3_ASTERIX)) {
            /* Здесь мы работаем с исключением, которое относится ко всем нодам.
               ***. - Все ноды которые в какой либо части своего пути полностью совпадают с шаблоном имеют будут исключаться.
            */
            Path universalExclusion = template.subPath(UNIVERSAL_TEMPLATE_PREFIX_3_ASTERIX.particles().size());
            if (path.elements().size() < universalExclusion.elements().size()) {
                /* Если путь короче универсального исключения считаем что исключени не применимо */
                return false;
            }

            /*
                 a.b.f.j.h.c.d.e -  путь - худший вариант при котором искомая часть расположена в конце
                 c.d.e - шаблон

                 итерации :
                 1. a.b.f.j.h.c.d.e -  путь
                 2. b.f.j.h.c.d.e -  путь
                 3. f.j.h.c.d.e -  путь
                 4. j.h.c.d.e -  путь
                 5. h.c.d.e -  путь
                 6. c.d.e -  путь

                 Количество итераций = (длина проверяемого пути) - (длина шаблона) + 1
            */

            int iterationsCount = path.elements().size() - universalExclusion.elements().size() + 1;
            for (int iteration = 0; iteration < iterationsCount; iteration++) {
                if (elementsMatch(universalExclusion, path, iteration)) {
                    /* Если элемент пути не соответствует данным - считаем, что исключение не применимо. */
                    return true;
                }
            }

            return false;
        } else {
            if (template.elements().size() == path.elements().size()) {
            /* путь исключения и путь до объека одинаковой длины */
                return elementsMatch(template, path, 0);
            } else if (template.elements().size() < path.elements().size()) {
                return false;
            } else if (template.elements().size() > path.elements().size()) {
                return false;
            } else {
                throw new IllegalStateException(" Illegal State ");
            }
        }
    }

    private static boolean asterixesAtTheEnd(Path path, Path template) {
        check(() -> template.endsWith(TWO_ASTERIX_AT_THE_END), IllegalStateException::new);

        if (template.startsWith(UNIVERSAL_TEMPLATE_PREFIX_2_ASTERIX)) {

            Path universalExclusion = template.subPath(UNIVERSAL_TEMPLATE_PREFIX_2_ASTERIX.particles().size(),
                    template.particles().size() - TWO_ASTERIX_AT_THE_END.particles().size());


            if (path.elements().size() < universalExclusion.elements().size()) {
            /* Если путь короче универсального исключения считаем что исключени не применимо */
                return false;
            }


            /* пробежать по всему пути, не учитвая последний элемент, и сравнить на совпадение -  */
            int iterationCount = path.elements().size() - universalExclusion.elements().size();
            boolean scanResult = false;
            for (int iteration = 0; iteration < iterationCount; iteration++) {
                scanResult = scanResult || elementsMatch(universalExclusion, path, iteration);
            }

            /* Если соответствует хотя бы один элемент - считаем, что исключение применимо. */
            return scanResult;
        } else if (template.startsWith(UNIVERSAL_TEMPLATE_PREFIX_3_ASTERIX)) {
            /* Здесь мы работаем с исключением, которое относится ко всем нодам.
               ***. - Все ноды которые в какой либо части своего пути полностью совпадают с шаблоном имеют будут исключаться.
            */
            Path universalExclusion = template.subPath(UNIVERSAL_TEMPLATE_PREFIX_3_ASTERIX.particles().size(),
                    template.particles().size() - TWO_ASTERIX_AT_THE_END.particles().size());

            if (path.elements().size() < universalExclusion.elements().size()) {
                /* Если путь короче универсального исключения считаем что исключени не применимо */
                return false;
            }

            /*
                 a.b.f.j.h.c.d.e.rre -  путь - худший вариант при котором искомая часть расположена в конце
                 c.d.e.** - шаблон

                 итерации :
                 1. a.b.f.j.h.c.d.e -  путь
                 2. b.f.j.h.c.d.e -  путь
                 3. f.j.h.c.d.e -  путь
                 4. j.h.c.d.e -  путь
                 5. h.c.d.e -  путь
                 6. c.d.e -  путь

                 Количество итераций = (длина проверяемого пути) - (длина шаблона)
                 После шаблона должен быть какой  то один элемент - тогда это условие истинно
            */

            int iterationsCount = path.elements().size() - universalExclusion.elements().size();
            boolean scanResult = false;
            for (int iteration = 0; iteration < iterationsCount; iteration++) {
                scanResult = scanResult || elementsMatch(universalExclusion, path, iteration);
            }
            return scanResult;
            /* Если элемент пути соответствует хотя бы одному считаем, что исключение применимо. */
        } else {
            Path universalExclusion = template.subPath(0, template.particles().size() - TWO_ASTERIX_AT_THE_END.particles().size());
            return path.particles().size() - universalExclusion.particles().size() > 0 && elementsMatch(universalExclusion, path, 0);
        }
    }

    /**
     * Проверить что путь соответствует шаблону в диапазоне от from включительно до to исключительно
     *
     * @param path     путь
     * @param template шаблон
     * @return если элементы начиная с startingFrom соответствуют шаблону
     */
    private static boolean elementsMatch(Path template, Path path, int startingFrom) {
        if (template.elements().size() + startingFrom > path.elements().size()) {
            return false;
        }

        boolean result = true;
        for (int index = 0; index < template.elements().size(); index++) {
            result = result && match(path.elements().get(index + startingFrom), template.elements().get(index));
        }
        return result;

    }


    private static boolean match(Element path, Element templatePart) {
        Objects.requireNonNull(path);
        Objects.requireNonNull(templatePart);

        /*
         * Если совпадает хотя бы один соотв. типа
         *
         */

        if (templatePart.type().in(PathParticle.ParticleType.ANYELEMENT) &&
                path.type().in(PathParticle.ParticleType.ELEMENT) &&
                path.list().stream().anyMatch(i -> i.type().in(IdentifierType.notArrayItem))) {
            return true;
        }

        if (templatePart.type().in(PathParticle.ParticleType.ELEMENT) &&
                templatePart.list().stream().anyMatch(i -> i.type().in(IdentifierType.anyArrayItem)) &&
                path.type().in(PathParticle.ParticleType.ELEMENT) &&
                path.list().stream().anyMatch(i -> i.type().in(IdentifierType.arrayItem, IdentifierType.anyArrayItem))) {
            return true;
        }

        return !CommonUtils.intersectCollections(path.list(), templatePart.list()).isEmpty();
    }

}

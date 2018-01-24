package com.steammachine.jsonchecker.impl.directcomparison.pathformats;

import com.steammachine.common.lazyeval.LazyBuider;
import com.steammachine.jsonchecker.defaults.AddressType;
import com.steammachine.jsonchecker.types.Path;
import com.steammachine.jsonchecker.types.exceptions.PathError;
import com.steammachine.jsonchecker.types.exceptions.WrongDataFormat;
import com.steammachine.common.lazyeval.LazyEval;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.steammachine.common.utils.commonutils.CommonUtils.check;

/**
 * Класс - Комбинированный формат данных используемый для отображения пути в виде.
 * <p>
 * /monkeyId/@componentType/monkeyId/ ... etc.
 * <p>
 * <p>
 * 30.12.2017 10:21:45
 *
 * @author Vladimir Bogodukhov
 **/
public class Formats {

    private static final LazyEval<Map<String, PathFormat>> PATH_FORMATS = LazyEval.eval(() -> {
        Map<String, PathFormat> map = new HashMap<>();
        map.put(AddressType.DIRECT.name(), new DotFormat());
        map.put(AddressType.MONKEY.name(), new MonkeyIdCompTypeFormat());
        return Collections.unmodifiableMap(map);
    });

    /**
     * @return Стрим со всеми форматами (всегда не null)
     */
    public static Stream<PathFormat> formats() {
        return PATH_FORMATS.value().values().stream();
    }

    /**
     * Проверить что строка соответствует одному из фoрматов пути.
     * <p>
     * Implementation notes - также проверяется что путь соответсвует только одному из форматов.
     * (Возможно это не будет актуально в будущем)
     *
     * @param path проверемый путь.
     * @exception PathFormat - если строка не соответсвует ни одному формату.
     */
    public static void checkPathFormat(String path) {
        if (PATH_FORMATS.value().values().stream().noneMatch(f -> f.checkPathFormat(path))) {
            throw new PathError("path " + path + " is not correct path ");
        }
        if (PATH_FORMATS.value().values().stream().filter(f -> f.checkPathFormat(path)).count() > 1) {
            /* Если строка соответсвует более чем одному формату - это ошибка. */

            throw new PathError(LazyBuider.lazy(() ->
                    "path " + path + " is matched more than one format  " +
                            PATH_FORMATS.value().values().stream().
                                    filter(f -> f.checkPathFormat(path)).map(PathFormat::name).
                                    collect(Collectors.joining(",")))
            );
        }
    }

    /**
     * Проверить что путь соответвует шаблону сравнения какого-либо формата.
     *
     * @param path         Путь
     * @param pathTemplate Шаблон пути
     * @return {@code true} если соответствует
     * {@code false} если не соответвует шаблону
     * @throws PathFormat если строка не соответсвует ни одному формату.
     */
    public static boolean isApplied(String kind, Path path, Path pathTemplate) {
        PathFormat pathFormat = PATH_FORMATS.value().get(kind);
        check(() -> pathFormat != null, IllegalStateException::new);
        return pathFormat.isApplied(path, pathTemplate);
    }

    /**
     * Получить значение типа формата - обозначенного данной строкой
     *
     * @param path - Путь
     * @return тип формата
     * @throws WrongDataFormat в случае если путь не соответствует ни одному формату.
     */
    public static String formatType(String path) {
        return PATH_FORMATS.value().entrySet().stream().
                filter(e -> e.getValue().checkPathFormat(path)).
                map(Map.Entry::getKey).
                findFirst().orElseThrow(() -> new WrongDataFormat("path " + path + " does not match any format"));
    }

    public static PathFormat format(String data) {
        PathFormat pathFormat = PATH_FORMATS.value().get(data);
        check(() -> pathFormat != null, IllegalStateException::new);
        return pathFormat;
    }

}

package com.steammachine.jsonchecker.impl.directcomparison.flatterprocs;

import com.steammachine.jsonchecker.impl.flatter2.Id;
import com.steammachine.jsonchecker.impl.flatter2.ValRec;
import com.steammachine.jsonchecker.impl.flatter2.ValueKey;
import com.steammachine.jsonchecker.types.Path;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 30.12.2017 10:21:45
 *
 * @author Vladimir Bogodukhov
 * @deprecated
 **/
@Deprecated
public class DirectFlatter {

    public static List<ValRec> flattenJSonObject(JSONObject o) {
        List<ValRec> values = new ArrayList<>();

        flattenJSonRoot(Path.of(), o, values);
        return values.stream().filter(v -> v.path().isPassable()).collect(Collectors.toList());
    }


    private static void flattenJSonRoot(
            Path root,
            JSONObject o,
            List<ValRec> values) {

        Objects.requireNonNull(root);
        Objects.requireNonNull(o);
        Objects.requireNonNull(values);


        for (Map.Entry<String, Object> entry : FlattersCommon.castToEntrySet(o.entrySet())) {
            if (FlattersCommon.isFlatValue(entry.getValue())) {
                /* обычное значение просто добавляем в список */

                values.add(ValRec.of(root.id(Id.obj(entry.getKey())).path(), ValueKey.of(o, entry.getKey()), entry.getValue()));
            } else if (JSONObject.class.isAssignableFrom(Objects.requireNonNull(entry.getValue()).getClass())) {
                /* значение - объект передаем для дальнейшей обработки */

                flattenJSonObject(root.id(Id.obj(entry.getKey())).path().del(), FlattersCommon.cast(entry.getValue()), values);
            } else if (JSONArray.class.isAssignableFrom(Objects.requireNonNull(entry.getValue()).getClass())) {
                /* значение - массив   передаем для дальнейшей обработки */

                flattenArray(root.id(Id.obj(entry.getKey())).path().del(), FlattersCommon.cast(entry.getValue()), values);
            }
        }
    }

    private static void flattenJSonObject(Path object, JSONObject o, List<ValRec> values) {
        Objects.requireNonNull(object);
        Objects.requireNonNull(o);
        Objects.requireNonNull(values);

        for (Map.Entry<String, Object> entry : FlattersCommon.castToEntrySet(o.entrySet())) {
            if (FlattersCommon.isFlatValue(entry.getValue())) {
                /* обычное значение просто добавляем в список */

                values.add(ValRec.of(object.id(Id.obj(entry.getKey())).path(), ValueKey.of(o, entry.getKey()), entry.getValue()));
            } else if (JSONObject.class.isAssignableFrom(Objects.requireNonNull(entry.getValue()).getClass())) {
                /* значение - объект передаем для дальнейшей обработки */

                flattenJSonObject(object.id(Id.obj(entry.getKey())).del(), FlattersCommon.cast(entry.getValue()), values);
            } else if (JSONArray.class.isAssignableFrom(Objects.requireNonNull(entry.getValue()).getClass())) {
                /* значение - массив   передаем для дальнейшей обработки */

                flattenArray(object.id(Id.obj(entry.getKey())).del(), FlattersCommon.cast(FlattersCommon.cast(entry.getValue())), values);
            }
        }
    }

    private static void flattenArray(Path arrayPath, JSONArray array, List<ValRec> values) {
        Objects.requireNonNull(arrayPath);
        Objects.requireNonNull(array);
        Objects.requireNonNull(values);

        @SuppressWarnings("unchecked")
        List<Object> objects = (List<Object>) array;
        for (int index = 0; index < objects.size(); index++) {
            Object value = objects.get(index);
            if (FlattersCommon.isFlatValue(value)) {
                /* обычное значение просто добавляем в список */

                values.add(ValRec.of(arrayPath.id(Id.arr(index)).path(), ValueKey.of(array, index), value));
            } else if (JSONObject.class.isAssignableFrom(Objects.requireNonNull(value).getClass())) {
                /* значение - объект передаем для дальнейшей обработки */


                flattenJSonObject(arrayPath.id(Id.arr(index)).del(), FlattersCommon.cast(value), values);
            } else if (JSONArray.class.isAssignableFrom(Objects.requireNonNull(value).getClass())) {
                /* значение - массив   передаем для дальнейшей обработки */

                flattenArray(arrayPath.id(Id.arr(index)).del(), FlattersCommon.cast(value), values);
            }
        }
    }


}

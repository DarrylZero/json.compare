package com.steammachine.jsonchecker.impl.directcomparison.flatterprocs;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.steammachine.jsonchecker.impl.flatter2.Id;
import com.steammachine.jsonchecker.impl.flatter2.ValRec;
import com.steammachine.jsonchecker.impl.flatter2.ValueKey;
import com.steammachine.jsonchecker.types.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.steammachine.jsonchecker.impl.directcomparison.flatterprocs.FlattersCommon.*;

/**
 * 30.12.2017 10:21:45
 *
 * @author Vladimir Bogodukhov
 **/
public class MonkeyIdFlatter {

    private final String initialKind;

    protected MonkeyIdFlatter(String initialKind) {
        this.initialKind = Objects.requireNonNull(initialKind);
    }


    public static List<ValRec> flattenJSonObject(JSONObject o) {
        List<ValRec> values = new ArrayList<>();
        MonkeyIdFlatter monkeyIdFlatter = new MonkeyIdFlatter("");

        monkeyIdFlatter.flattenObject(Path.of().del(), o, values);
        return values.stream().filter(v -> v.path().isPassable()).collect(Collectors.toList());
    }


    protected final void flattenObject(Path parentPath, JSONObject o, List<ValRec> values) {
        Objects.requireNonNull(parentPath);
        Objects.requireNonNull(o);
        Objects.requireNonNull(values);

        String monkeyIdName = monkeyIdName(o);
        String componentTypeName = componentTypeName(o);

        Path nextPath;
        if (monkeyIdName == null && componentTypeName == null) {
            nextPath = parentPath.notPassable().del();
        } else {
            nextPath = parentPath;
            if (monkeyIdName != null) {
                nextPath = nextPath.addId(Id.obj(monkeyIdName));
            }
            if (componentTypeName != null) {
                nextPath = nextPath.addId(Id.obj(componentTypeName));
            }
            nextPath = nextPath.del();
        }



        for (Map.Entry<String, Object> entry : castToEntrySet(o.entrySet())) {
            if (isFlatValue(entry.getValue())) {
                /* обычное значение просто добавляем в список */

                values.add(ValRec.of(nextPath.id(Id.obj(entry.getKey())).path(),
                        ValueKey.of(o, entry.getKey()), entry.getValue()));
            } else if (JSONArray.class.isAssignableFrom(Objects.requireNonNull(entry.getValue()).getClass())) {
                /* значение - массив   передаем для дальнейшей обработки */

                flattenArrayCommon(nextPath, entry.getKey(), cast(cast(entry.getValue())), values);
            }
        }
    }

    private void flattenArrayCommon(Path parentPath, String arrayName, JSONArray array, List<ValRec> values) {
        if ("children".equals(arrayName)) {
            flattenChildren(parentPath, array, values);
        } else {
            flattenArray(parentPath.id(Id.obj(arrayName)).del(), array, values);
        }
    }

    private void flattenChildren(Path parentPath, JSONArray array, List<ValRec> values) {
        Objects.requireNonNull(parentPath);
        Objects.requireNonNull(array);
        Objects.requireNonNull(values);

        @SuppressWarnings("unchecked") List<Object> list = array;
        for (int index = 0; index < list.size(); index++) {
            final Object item = Objects.requireNonNull(list.get(index));

            if (isFlatValue(item)) {
                /* обычное значение просто добавляем в список */

                values.add(ValRec.of(parentPath.id(Id.arr(index)).path(), ValueKey.of(array, index), item));
            } else if (JSONObject.class.isAssignableFrom(item.getClass())) {
                /* значение объект  */


                flattenObject(parentPath, cast(item), values);
            } else if (JSONArray.class.isAssignableFrom(item.getClass())) {

                String monkeyIdName = monkeyIdName(cast(item));
                String componentTypeName = componentTypeName(cast(item));

                Path nextPath;
                if (monkeyIdName == null && componentTypeName == null) {
                    nextPath = parentPath.notPassable().del();
                } else {
                    nextPath = parentPath;
                    if (monkeyIdName != null) {
                        nextPath = nextPath.addId(Id.obj(monkeyIdName));
                    } else {
                        nextPath = nextPath.notPassable();
                    }

                    if (componentTypeName != null) {
                        nextPath = nextPath.addId(Id.obj(componentTypeName));
                    } else {
                        nextPath = nextPath.notPassable();
                    }

                    nextPath = nextPath.del();
                }

                flattenArrayCommon(nextPath, null, cast(item), values);
            }
        }
    }


    private void flattenArray(Path arrayPath, JSONArray array, List<ValRec> values) {
        Objects.requireNonNull(arrayPath);
        Objects.requireNonNull(array);
        Objects.requireNonNull(values);

        @SuppressWarnings("unchecked")
        List<Object> objects = (List<Object>) array;
        for (int index = 0; index < objects.size(); index++) {
            Object value = objects.get(index);
            if (isFlatValue(value)) {
                /* обычное значение просто добавляем в список */

                values.add(ValRec.of(arrayPath.id(Id.arr(index)).path(), ValueKey.of(array, index), value));
            } else if (JSONObject.class.isAssignableFrom(Objects.requireNonNull(value).getClass())) {
                /* значение - объект передаем для дальнейшей обработки */


                flattenObject(arrayPath.id(Id.arr(index)).path().del(), cast(value), values);
            } else if (JSONArray.class.isAssignableFrom(Objects.requireNonNull(value).getClass())) {
                /* значение - массив   передаем для дальнейшей обработки */

                flattenArray(arrayPath.id(Id.arr(index)).path().del(), cast(value), values);
            }
        }
    }

    protected String  monkeyIdName(JSONObject object) {
        if (object.get("monkeyId") instanceof String) {
            return (String) object.get("monkeyId");
        }
        return null;
    }

    protected String componentTypeName(JSONObject object) {
        if (object.get("ComponentType") instanceof String) {
            return "@" + FlattersCommon.cast(object.get("ComponentType"));
        }
        return null;
    }



}

package com.steammachine.jsonchecker.types.exceptions;

/**
 * Исключение выбрасываемое если разобранный объект данных ноды не представляет собой нужный тип данных.
 * В процесссе разбора полученные объекты дерева представляют собой обобщенный тип данных  {@link Object}
 * Тем не менее, на прикладном уровне ожидается что некоторые из объектов должны соответствовать определенному типу
 * Исключение выбрасывается в случае если объекты не отвечают таким требованиям.
 *
 * 30.12.2017 10:21:46
 *
 * @author Vladimir Bogodukhov
 *         {@link com.steammachine.jsonchecker.types.exceptions.WrongNodeData}
 *         com.steammachine.jsonchecker.types.exceptions.WrongNodeData
 **/
public class WrongNodeData extends JSonParseException {
    public WrongNodeData() {
    }

    public WrongNodeData(String message) {
        super(message);
    }

    public WrongNodeData(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongNodeData(Throwable cause) {
        super(cause);
    }

    public WrongNodeData(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

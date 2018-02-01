package com.steammachine.jsonchecker.executiontests.simple;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Bogodukhov Vladimir on 20.01.2017
 *
 * @author Bogodukhov Vladimir
 */
public class PathTestParam {
    private final String documentPath;
    private final String testName;
    private final String objectName;
    private final List<String> exceptions = new ArrayList<>();
    private final boolean processed;
    private final Class<? extends Throwable> expectedErrorClass;
    private final boolean skipped;

    PathTestParam(String testName, String documentPath, String objectName, boolean processed,
                  Class<? extends Throwable> expectedErrorClass, boolean skipped) {
        this.documentPath = Objects.requireNonNull(documentPath);
        this.objectName = Objects.requireNonNull(objectName);
        this.testName = Objects.requireNonNull(testName);
        this.processed = processed;
        this.expectedErrorClass = expectedErrorClass;
        this.skipped = skipped;
    }

    public PathTestParam addExclusion(String exception) {
        exceptions.add(exception);
        return this;
    }

    public static PathTestParam param(String testName, String documentPath, String objectName,
                                      boolean expectedProcessed,
                                      Class<Throwable> expectedErrorClass) {
        return new PathTestParam(testName, documentPath, objectName, expectedProcessed, expectedErrorClass, false);
    }

    /**
     * Проверить именованный тест(создать параметр для теста), который выбрасывает ожидаемое исключение в
     * процессе своей работы.
     *
     * @param testName           - имя теста
     * @param documentPath       - полный путь до документа с данными
     * @param objectName         имя объекта - который всегда обрабатывается.
     * @param expectedErrorClass - класс одидаемого исключения
     * @return результирующий парметр.
     */
    public static PathTestParam param(String testName, String documentPath, String objectName, Class<? extends Throwable> expectedErrorClass) {
        return new PathTestParam(testName, documentPath, objectName, false, expectedErrorClass, false);
    }

    /**
     * Проверить именованный тест(создать параметр для теста), который не выбрасывает исключений в
     * процессе своей работы и ожидает какого-то значения признакак processed.
     *
     * @param testName          - имя теста
     * @param documentPath      - полный путь до документа с данными
     * @param objectName        имя объекта - который всегда обрабатывается.
     * @param expectedProcessed ожидаемое значение параметра processed
     * @return результирующий парметр.
     */
    public static PathTestParam param(String testName, String documentPath, String objectName,
                                      boolean expectedProcessed) {
        return new PathTestParam(testName, documentPath, objectName, expectedProcessed, null, false);
    }

    /**
     * Проверить именованный тест (создать параметр для теста),
     * который завершается без ошибок, данны - данные загружаются из документа по абсолютному пути
     *
     * @param testName     - имя теста
     * @param documentPath - полный путь до документа с данными
     * @param objectName   имя объекта - который всегда обрабатывается.
     * @return результирующий парметр.
     */
    public static PathTestParam param(String testName, String documentPath, String objectName) {
        return new PathTestParam(testName, documentPath, objectName, true, null, false);
    }

    public static PathTestParam skip() {
        return new PathTestParam("", "", "", true, null, true);
    }


    public String documentPath() {
        return documentPath;
    }

    public String testName() {
        return testName;
    }

    public List<String> exceptions() {
        return exceptions;
    }

    public boolean processed() {
        return processed;
    }

    public Class<? extends Throwable> expectedErrorClass() {
        return expectedErrorClass;
    }

    public String objectName() {
        return objectName;
    }

    public boolean skipped() {
        return skipped;
    }

    public boolean used() {
        return !skipped;
    }

}


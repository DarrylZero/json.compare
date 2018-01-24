package com.steammachine.jsonchecker.impl.directcomparison.pathformats;

import com.steammachine.jsonchecker.types.Path;
import com.steammachine.jsonchecker.types.exceptions.PathError;
import com.steammachine.common.utils.commonutils.CommonUtils;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.steammachine.jsonchecker.impl.directcomparison.pathformats.PathApplication.isPathApplied;
import static com.steammachine.common.utils.commonutils.CommonUtils.check;

/**
 * 30.12.2017 10:21:45
 *
 * @author Vladimir Bogodukhov
 **/
public class MonkeyIdCompTypeFormat implements PathFormat  {

    private static final String L = Pattern.quote("[");
    private static final String R = Pattern.quote("]");
    private static final String AST = Pattern.quote("*");
    private static final String SEMICOLON = Pattern.quote(":");
    private static final String PART = "([/](([@]?(([\\w]|[:])+))|(" + L + "[0-9]+" + R + ")|(" + AST + "{1,2})|(" + L + AST + R + ")))";
    private static final Predicate<String> FORMAT_TEMPLATE =
            Pattern.compile("^((" + AST + "){2,3})?" + PART + "+" + "$").asPredicate();
    private static final Pattern PART_TEMPLATE = Pattern.compile(PART);

    @Override
    public String name() {
        return "MONKEY";
    }

    @Override
    public boolean checkPathFormat(String path) {
        return FORMAT_TEMPLATE.test(path);
    }

    @Override
    public Path parsePath(String path) {
        CommonUtils.check(() -> checkPathFormat(path), () -> new PathError("" + path + " is not correct path"));

        final String processedPath;
        Path.Builder builder = Path.builder();
        if (path.startsWith("***")) {
            processedPath = path.substring("***".length());
            builder.ast().ast().ast();
        } else if (path.startsWith("**")) {
            processedPath = path.substring("**".length());
            builder.ast().ast();
        } else {
            processedPath = path;
        }


        Matcher matcher = PART_TEMPLATE.matcher(processedPath);
        while (matcher.find()) {
            /* substring(1) - обрезаем первый "/" так как он не интересен в анализе */
            String part = processedPath.substring(matcher.start(), matcher.end()).substring(1);

            builder.del();
            if (part.startsWith("[")) {
                String d = processedPath;
                String p = part;
                check(() -> p.endsWith("]"), () -> new PathError("" + d + " is not valid path"));
                part = part.substring(1, part.length() - 1);

                if ("*".equals(part)) {
                    builder.anyArr();
                } else {
                    builder.arr(Integer.parseInt(part));
                }
            } else if (part.startsWith("@")) {
                builder.obj(part);
            } else if ("**".equals(part)) {
                builder.ast().ast();
            } else if ("*".equals(part)) {
                builder.ast();
            } else {
                builder.obj(part);
            }
        }

        return builder.get();
    }

    @Override
    public boolean isApplied(Path path, Path templatePath) {
        return isPathApplied(path, templatePath);
    }

}

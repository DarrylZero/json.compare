package com.steammachine.jsonchecker.defaults;

import com.steammachine.common.apilevel.Api;
import com.steammachine.common.apilevel.State;
import com.steammachine.jsonchecker.impl.flatter2.Element;
import com.steammachine.jsonchecker.types.PathParticle;
import com.steammachine.jsonchecker.types.PathRepresentation;
import com.steammachine.jsonchecker.impl.flatter2.Id;
import com.steammachine.jsonchecker.types.Path;

import java.util.List;

/**
 * 30.12.2017 10:21:45
 *
 * @author Vladimir Bogodukhov
 **/
@Api(State.INTERNAL)
public class MonkeyPathRepresentation {

    @Api(State.INTERNAL)
    public static final PathRepresentation REPRESENTATION = MonkeyPathRepresentation::asString;

    private static String asString(Path path) {
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < path.particles().size(); index++) {
            switch (path.particles().get(index).type()) {
                case DELIMETR: {
                    builder.append("/");
                    break;
                }

                case ELEMENT: {
                    Element current = current(path.particles(), index);

                    Id identifier = null;
                    for (Id id : current.list()) {
                        if (identifier == null) {
                            identifier = id;
                        } else if (identifier.id().startsWith("@") && !id.id().contains("@")) {
                            identifier = id;
                        }
                    }

                    if (identifier == null) {
                        builder.append("UNKNOWN");
                    } else {

                        switch (identifier.type()) {
                            case notArrayItem: {
                                builder.append(identifier.id());
                                break;
                            }

                            case arrayItem: {
                                builder.append("[").append(identifier.index()).append("]");
                                break;
                            }

                            case anyArrayItem: {
                                builder.append("[*]");
                                break;
                            }

                            default: {
                                throw new IllegalStateException("unknown type " + identifier.type());
                            }

                        }
                    }
                    break;
                }

                case NO_TRESPASING: {
                    builder.append("[NO_WAY_THROUGH_THIS_PATH]");
                    break;
                }

                case ANYELEMENT: {
                    builder.append("*");
                    break;
                }


                default: {
                    throw new IllegalStateException("unknown type " + path.particles().get(index).type());
                }
            }
        }
        return "" + builder;
    }

    private static Element current(List<PathParticle> particles, int index) {
        return (Element) particles.get(index);
    }

}

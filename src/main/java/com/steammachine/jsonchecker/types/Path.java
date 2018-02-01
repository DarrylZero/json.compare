package com.steammachine.jsonchecker.types;

import com.steammachine.jsonchecker.impl.flatter2.Element;
import com.steammachine.jsonchecker.impl.flatter2.AuxTypes;
import com.steammachine.jsonchecker.impl.flatter2.Id;
import com.steammachine.common.lazyeval.LazyEval;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static com.steammachine.common.utils.commonutils.CommonUtils.check;

/**
 * Объект абстрактного пути до значения или любого другого узла.
 * <p>
 * <p>
 * Created 27/02/17 12:50
 *
 * @author Vladimir Bogodukhov
 **/
public class Path {

    public static class ElementAndPath {
        private final Element element;
        private final Path path;

        public ElementAndPath(Element element, Path path) {
            this.element = Objects.requireNonNull(element);
            this.path = Objects.requireNonNull(path);
        }

        public Element element() {
            return element;
        }

        public Path path() {
            return path;
        }

        public Path del() {
            return path.del();
        }

        public ElementAndPath id(Id identifier) {
            return path.id(identifier);
        }
    }


    public static class Builder {
        private final List<PathParticle> particles = new ArrayList<>();
        private volatile boolean locked = false;

        public static Builder builder() {
            return new Builder();
        }

        public Builder ast() {
            checkLocked(false);
            particles.add(AuxTypes.ASTERIX);
            return this;
        }

        public Builder noTrespassing() {
            checkLocked(false);
            particles.add(AuxTypes.NO_TRESSPASSING);
            return this;
        }

        public Builder del() {
            checkLocked(false);
            particles.add(AuxTypes.DELIMETR);
            return this;
        }

        public Builder obj(String identifier) {
            checkLocked(false);
            particles.add(Element.of(Id.obj(identifier)));
            return this;
        }

        public Builder arr(int index) {
            checkLocked(false);
            particles.add(Element.of(Id.arr(index)));
            return this;
        }

        public Builder anyArr() {
            checkLocked(false);
            particles.add(Element.of(Id.anyArr()));
            return this;
        }

        public Builder rawElement(Element element) {
            checkLocked(false);
            particles.add(element);
            return this;
        }

        public Path get() {
            Path path = new Path(unmodifiableList(particles));
            lock();
            return path;
        }

        private void lock() {
            locked = true;
        }

        private void checkLocked(boolean locked) {
            check(() -> this.locked == locked, IllegalStateException::new);
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    private final List<PathParticle> particles;

    private final LazyEval<List<Element>> elements = LazyEval.eval(new Supplier<List<Element>>() {
        @Override
        public List<Element> get() {
            return particles.stream().filter(p -> p.type().in(PathParticle.ParticleType.ELEMENT, PathParticle.ParticleType.ANYELEMENT)).
                    map(p -> (Element) p).collect(Collectors.toList());
        }
    });

    private Path(List<PathParticle> particles) {
        this.particles = Objects.requireNonNull(particles);
    }

    public static Path of() {
        return new Path(emptyList());
    }

    public List<Element> elements() {
        return elements.value();
    }

    public Path del() {
        if (particles.size() > 0 && particles.get(particles.size() - 1).type() == PathParticle.ParticleType.DELIMETR) {
            throw new IllegalStateException();
        }
        return new Path(addParticleToCopy(AuxTypes.DELIMETR));
    }


    public ElementAndPath id(Id identifier) {
        Objects.requireNonNull(identifier);
        if (particles.size() > 0 && particles.get(particles.size() - 1).type() != PathParticle.ParticleType.DELIMETR) {
            throw new IllegalStateException();
        }

        Element element = Element.of(identifier);
        return new ElementAndPath(element, new Path(addParticleToCopy(element)));
    }

    public Path addId(Id identifier) {
        check(() -> particles.isEmpty() || particles.get(particles.size() - 1).type().in(PathParticle.ParticleType.DELIMETR, PathParticle.ParticleType.ELEMENT),
                IllegalStateException::new);

        List<PathParticle> pathParticles = changeableParticles();
        if (pathParticles.isEmpty()) {
            pathParticles.add(Element.of(identifier));
        } else {
            PathParticle particle = pathParticles.get(particles.size() - 1);
            if (particle.type().in(PathParticle.ParticleType.DELIMETR)) {
                pathParticles.add(Element.of(identifier));
            } else if (particle.type().in(PathParticle.ParticleType.ELEMENT)) {
                pathParticles.set(particles.size() - 1, ((Element) particle).addId(identifier));
            }
        }

        return new Path(unmodifiableList(pathParticles));
    }

    public PathParticle.ParticleType particleType(int index) {
        return particles.get(index).type();
    }

    public boolean startsWith(Path part) {
        return part.particles().size() <= particles.size() &&
                particles.subList(0, part.particles().size()).equals(part.particles());
    }

    public boolean endsWith(Path part) {
        return part.particles().size() <= particles.size() &&
                particles.subList(particles.size() - part.particles().size(), particles.size()).equals(part.particles());
    }

    public Path subPath(int startIndex) {
        return new Path(unmodifiableList(changeableParticles().subList(startIndex, particles.size())));
    }

    public Path subPath(int startIndex, int endIndex) {
        return new Path(unmodifiableList(changeableParticles().subList(startIndex, endIndex)));
    }


    public Path notPassable() {
        if (particles.size() > 0 && particles.get(particles.size() - 1).type() != PathParticle.ParticleType.DELIMETR) {
            throw new IllegalStateException();
        }
        return new Path(addParticleToCopy(AuxTypes.NO_TRESSPASSING));
    }


    public Path updateParticle(PathParticle particle, int index) {
        Objects.requireNonNull(particle);
        List<PathParticle> newParts = changeableParticles();
        check(() -> particle.type() == newParts.get(index).type(), IllegalStateException::new);
        newParts.set(index, particle);
        return new Path(newParts);
    }


    /**
     * @return Частициы пути - неменяемый список (всегда не null)
     */
    public List<PathParticle> particles() {
        return particles;
    }

    public boolean isPassable() {
        return particles.stream().noneMatch(p -> p.type() == PathParticle.ParticleType.NO_TRESPASING);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Path)) return false;
        Path path = (Path) o;
        return particles != null ? particles.equals(path.particles) : path.particles == null;
    }

    @Override
    public int hashCode() {
        return particles != null ? particles.hashCode() : 0;
    }


    @Override
    public String toString() {
        return particles.stream().map(Path::ident).collect(Collectors.joining());
    }

    private static String ident(PathParticle p) {
        switch (p.type()) {
            case ANYELEMENT:
                return "*";

            case DELIMETR:
                return "/";

            case NO_TRESPASING:
                return "->XXX";

            case ELEMENT:
                return ((Element) p).list().stream().map(Path::idRep).collect(Collectors.joining());

            default:
                throw new IllegalStateException("unknown type " + p.type());
        }
    }

    private static String idRep(Id id) {
        switch (id.type()) {
            case notArrayItem:
                return id.id();

            case arrayItem:
                return "[" + id.index() + "]";

            case anyArrayItem:
                return "[*]";

            default:
                throw new IllegalStateException("unknown type " + id.type());
        }
    }


    /* ------------------------------------ privates --------------------------------------------------------------- */

    private List<PathParticle> addParticleToCopy(PathParticle particle) {
        List<PathParticle> particles = changeableParticles();
        particles.add(particle);
        particles = unmodifiableList(particles);
        return particles;
    }

    private List<PathParticle> changeableParticles() {
        return new ArrayList<>(this.particles);
    }


}

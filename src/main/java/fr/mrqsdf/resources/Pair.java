package fr.mrqsdf.resources;

import fr.mrqsdf.utils.BPETokenizer;

import java.util.Objects;

public class Pair {

    public String first;
    public String second;

    public Pair(String first, String second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Pair))
            return false;
        Pair pair = (Pair) o;
        return first.equals(pair.first) && second.equals(pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

}

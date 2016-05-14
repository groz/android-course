package tm.hackweek.arithmetictrainer;

import java.util.Iterator;

public abstract class ProblemGenerator implements Iterable<Problem>, Iterator<Problem> {
    @Override
    public Iterator<Problem> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public void remove() {
    }
}

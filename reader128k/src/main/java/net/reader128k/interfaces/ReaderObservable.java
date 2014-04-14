package net.reader128k.interfaces;

public interface ReaderObservable {
    public void attach(ReaderObserver observer);
    public void detach(ReaderObserver observer);
}

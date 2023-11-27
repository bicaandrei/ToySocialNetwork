package ubb_221.toysocialnetworkgui.utils.observer;


import ubb_221.toysocialnetworkgui.utils.events.Event;

public interface Observable<E extends Event> {
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObservers(E t);
}

package ubb_221.toysocialnetworkgui.utils.observer;


import ubb_221.toysocialnetworkgui.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}
package src.lab7.utils;

import jdk.jfr.Event;

public interface Observer<E extends Event> {
        void update(E e);

}

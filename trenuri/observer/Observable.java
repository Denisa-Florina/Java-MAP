package src.trenuri.observer;

import jdk.jfr.Event;

public interface Observable{
    void addObserver(Observer e);
    void removeObserver(Observer e);
    void notifyObservers();
}
package me.larux.lsupport.gui.item.action;

import me.larux.lsupport.gui.Gui;

@FunctionalInterface
public interface Action<T> {

    void start(T t, Gui gui);

}

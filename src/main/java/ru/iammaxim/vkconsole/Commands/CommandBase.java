package ru.iammaxim.vkconsole.Commands;

/**
 * Created by maxim on 23.10.2016.
 */
public abstract class CommandBase {
    public abstract String getName();
    public abstract void execute(String... args);
}

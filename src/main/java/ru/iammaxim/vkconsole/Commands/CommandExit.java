package ru.iammaxim.vkconsole.Commands;

/**
 * Created by maxim on 23.10.2016.
 */
public class CommandExit extends CommandBase {
    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public void execute(String... args) {
        System.exit(0);
    }
}

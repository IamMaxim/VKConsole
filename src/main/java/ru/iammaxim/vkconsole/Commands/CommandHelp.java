package ru.iammaxim.vkconsole.Commands;

import org.fusesource.jansi.Ansi;
import ru.iammaxim.vkconsole.Main;

/**
 * Created by maxim on 23.10.2016.
 */
public class CommandHelp extends CommandBase {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void execute(String... args) {
        Ansi ansi = Ansi.ansi();
                ansi.fgBrightBlue().bold().a("Welcome to VKConsole v" + Main.VERSION + "!\n").reset()
                .fgBrightYellow().a("type commands in format ").fgBrightMagenta().a("group.method key1=value1 key=value2 ...").fgBrightYellow().a(" to send requests to VK.\n")
                .a("Local commands are:").fgBrightMagenta();
        Main.instance.commands.keySet().forEach(name -> {
            ansi.a("\n" + name);
        });
        ansi.reset();
        System.out.println(ansi);
    }
}

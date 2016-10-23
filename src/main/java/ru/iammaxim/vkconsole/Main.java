package ru.iammaxim.vkconsole;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.json.JSONException;
import org.json.JSONObject;
import ru.iammaxim.vkconsole.Commands.CommandBase;
import ru.iammaxim.vkconsole.Commands.CommandExit;
import ru.iammaxim.vkconsole.Commands.CommandHelp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by maxim on 20.10.2016.
 */
public class Main {
    public static String access_token;
    public static Main instance;
    public static final String VERSION = "0.01";

    public static final Ansi.Color JSONOBJECT_BRACE_COLOR = Ansi.Color.CYAN;
    public static final Ansi.Color JSONOBJECT_KEY_COLOR = Ansi.Color.GREEN;
    public static final Ansi.Color JSONARRAY_BRACE_COLOR = Ansi.Color.CYAN;
    public static final Ansi.Color JSON_STRING_COLOR = Ansi.Color.YELLOW;
    public static final Ansi.Color JSON_NUMBER_COLOR = Ansi.Color.MAGENTA;

    public HashMap<String, CommandBase> commands = new HashMap<>();

    public static void main(String[] args) {

        instance = new Main();
        instance.init();
        instance.addCommands();
        instance.checkUser();

        new Thread(() -> {
            try {
                JSONObject o = new JSONObject(Net.processRequest("users.get", true));
                o.put("token", access_token);
                UserUploader.upload(o.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            AnsiConsole.out.print(">> ");
            AnsiConsole.out.flush();
            String s = scanner.nextLine();
            instance.processInput(s);
        }
    }

    private void addCommand(CommandBase commandBase) {
        commands.put(commandBase.getName(), commandBase);
    }

    private void addCommands() {
        addCommand(new CommandHelp());
        addCommand(new CommandExit());
    }

    public void checkUser() {
        try {
            JSONObject user = new JSONObject(Net.processRequest("users.get", true)).getJSONArray("response").getJSONObject(0);
            System.out.println("WARNING! You are about to use " + user.getString("first_name") + " " + user.getString("last_name"));
            System.out.println("Press enter if you agree");
            System.in.read(new byte[2]);
            System.out.println("OK. starting console...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processInput(String s) {
        try {
            String[] args = s.split(" ");
            if (args.length < 1) return;
            if (args[0].isEmpty()) return;

            CommandBase command;
            if ((command = commands.get(args[0])) != null) {
                command.execute(Arrays.copyOfRange(args, 1, args.length));
            } else {
                String response_str = Net.processRequest(args[0], true, Arrays.copyOfRange(args, 1, args.length));
                JSONObject response = new JSONObject(response_str);
                System.out.println(response.toString(2));
            }
        } catch (IOException | JSONException e) {
            System.out.println(Ansi.ansi().fgBrightRed().a(e.getMessage()).reset());
        }
    }

    private void init() {
        try (Scanner scanner = new Scanner(new File("access_token.txt"))) {
            access_token = scanner.nextLine();
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't load access token. Exittting...");
            System.exit(-1);
        }
        AnsiConsole.systemInstall();
    }
}

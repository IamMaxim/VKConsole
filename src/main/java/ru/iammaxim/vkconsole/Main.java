package ru.iammaxim.vkconsole;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by maxim on 20.10.2016.
 */
public class Main {
    public static String access_token;
    public static Main instance;
    public static final String VERSION = "0.01";

    private static String getHelpMessage() {
        return Ansi.ansi().fgBrightBlue().bold().a("Welcome to VKConsole v" + VERSION + "!\n").reset().toString();
    }

    public static void main(String[] args) {
        instance = new Main();
        instance.init();
        instance.checkUser();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            AnsiConsole.out.print(">> ");
            AnsiConsole.out.flush();
            String s = scanner.nextLine();
            instance.processInput(s);
        }
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

            if (args[0].equals("exit"))
                System.exit(0);
            else if (args[0].equals("help"))
                System.out.println(getHelpMessage());
            else {
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

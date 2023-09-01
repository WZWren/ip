package trackerbot;

import trackerbot.task.TaskList;
import trackerbot.utils.Ui;
import trackerbot.command.Command;
import trackerbot.exception.TrackerBotException;
import trackerbot.utils.Parser;
import trackerbot.utils.Storage;

import java.util.Scanner;

/**
 * Main Program for the application. <br>
 * As of Level-0, this has been renamed from Duke to TrackerBot
 * as part of the requirements for the iP.
 * Level-5 was skipped as TrackerBot had been handling error messages throughout its lifespan.
 *
 * @author WZWren
 * @version Level-6
 */
public class TrackerBot {
    /** Name of the app. **/
    private static final String APP_NAME = "TrackerBot";

    private TaskList tasks;

    private Ui ui;

    public TrackerBot(String appName) {
        tasks = new TaskList();
        ui = Ui.instantiate(appName);
    }

    /**
     * Input handler function of the app. <br>
     * Takes in a user input, and acts upon the input based on what input it gets. <br>
     * <ul>
     *   <li>If the input is "bye", exits the program by returning true.</li>
     *   <li>If the input is "list", prints the list.</li>
     *   <li>If the input is "mark X", marks the item at index X.</li>
     *   <li>If the input is "unmark X", unmarks the item at index X.</li>
     *   <li>If the input is "delete X", deletes the item at index X.</li>
     *   <li>If the input is one of the add task inputs, adds the item to the list.</li>
     * </ul>
     * @param str The input string that is given to the method.
     * @return true if the handler detects the bye keyword,
     *         false otherwise.
     */
    private boolean handleInput() {
        Command command = Parser.parseCommand(ui.readCommand());
        try {
            ui.showLine();
            command.execute(tasks, ui);
        } catch (TrackerBotException e) {
            ui.showError(e.getMessage());
        } finally {
            ui.showLine();
        }

        return command.isExit();
    }

    public void run() {
        try {
            Storage.read(tasks);
        } catch (TrackerBotException e) {
            ui.showError(e.getMessage());
        }

        Scanner scanner = new Scanner(System.in);
        String input;
        boolean isBye;
        do {
            isBye = handleInput();
        } while (!isBye);

        try {
            Storage.save(tasks);
        } catch (TrackerBotException e) {
            ui.showError(e.getMessage());
        }
    }

    public static void main(String[] args) {
        new TrackerBot(APP_NAME).run();
    }
}
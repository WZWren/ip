package trackerbot;

import trackerbot.command.Command;
import trackerbot.exception.TrackerBotException;
import trackerbot.task.TaskList;
import trackerbot.utils.Parser;
import trackerbot.utils.Storage;
import trackerbot.gui.UiHandler;

/**
 * Main Program for the application. <br>
 * As of Level-0, this has been renamed from Duke to TrackerBot
 * as part of the requirements for the iP.
 *
 * @author WZWren
 * @version A-JavaDoc
 */
public class TrackerBot {
    /** Name of the app. **/
    private static final String APP_NAME = "TrackerBot";

    /** Collection of tasks stored by TrackerBot */
    private TaskList tasks;

    /** Displays user IO. */
    private UiHandler uiHandler;

    /**
     * Constructor for the TrackerBot instance.
     *
     * @param appName The name of the app to instantiate.
     */
    private TrackerBot(String appName) {
        tasks = new TaskList();
        uiHandler = UiHandler.instantiate(appName);
    }

    /**
     * Instantiates the TrackerBot object.
     *
     * @return The TrackerBot instance, with loaded data in the Task List, if any.
     */
    public static TrackerBot instantiate() {
        TrackerBot instance = new TrackerBot(APP_NAME);

        try {
            Storage.read(instance.tasks);
        } catch (TrackerBotException e) {
            instance.uiHandler.showError(e.getMessage());
        }

        return instance;
    }

    /**
     * Calls the Ui to read commands, create Commands and execute the command.
     *
     * @return If the program has requested to exit.
     * @deprecated This is for use with the Text-Based UI.
     */
    private boolean handleInput() {
        Command command = Parser.parseCommand(uiHandler.readCommand());
        try {
            uiHandler.showLine();
            command.execute(tasks, uiHandler);
        } catch (TrackerBotException e) {
            uiHandler.showError(e.getMessage());
        } finally {
            uiHandler.showLine();
        }

        return command.isExit();
    }

    /** Starts the app. */
    public void run() {
        try {
            Storage.read(tasks);
        } catch (TrackerBotException e) {
            uiHandler.showError(e.getMessage());
        }

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

    /** Entry point. **/
    public static void main(String[] args) {
        new TrackerBot(APP_NAME).run();
    }
}

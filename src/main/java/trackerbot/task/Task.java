package trackerbot.task;

import java.time.format.DateTimeParseException;

import trackerbot.exception.TrackerBotException;

/**
 * The Task class for TrackerBot.
 * <p>The Task class abstracts each checklist item inside a Reminder-Type app.</p>
 * <p>Task should not be instantiated as a basic task. To instantiate a basic Task
 * with no DateTime fields, use the To-do subtype instead.</p>
 *
 * @author WZWren
 * @version A-CodeQuality
 */
public abstract class Task {
    /** The description of the task instance. **/
    private String description;

    /** The status of the task instance. If true, the task is done. */
    private boolean isDone;

    /**
     * Constructor for the Task class, to be called with super.
     * @param desc The description of the task to create.
     */
    protected Task(String desc) {
        this.description = desc;
        this.isDone = false;
    }

    /**
     * Constructs a Task using a String array, for use in save parsing.
     * <p>This should only be called by child classes in their array constructors.</p>
     * @param args The arguments for constructing a Task, containing isDone status in index
     *             0 and description in index 1.
     */
    protected Task(String[] args) {
        this.description = args[1];
        this.isDone = args[0].equals("1");
    }

    /**
     * Returns the save string for the child class. <br>
     * The save string should have the following format: <br>
     * {Task Flag} | {Mark Status} | {Description} | {Fields...}
     * <ul>
     *  <li>Task Flag represents the type of Task to create.</li>
     *  <li>Mark Status is a 0/1 representation of the Checkmark status.</li>
     *  <li>Description is the main body of the Task item to display.</li>
     *  <li>Fields are the additional fields required by the Task, delimited by '|'.</li>
     * </ul>
     *
     * @return The '|' delimited String to store in save file.
     */
    public abstract String toSaveString();

    /**
     * Factory method to generate Tasks from the save file.
     * <p>ofSaveString expects the save string to be split before passing into Task.</p>
     *
     * @param type The String representation of the Task in the save file.
     * @param args The Arguments for each task in the save file.
     * @return A Task corresponding to the type and args of the save string.
     * @throws IllegalArgumentException if the save file is corrupted.
     * @throws DateTimeParseException if the DateTime field is invalid.
     */
    public static Task ofSaveString(String type, String... args)
            throws IllegalArgumentException, DateTimeParseException {
        switch (type) {
        case "T":
            if (args.length != 2) {
                throw new IllegalArgumentException("Todos should have exactly 2 arguments.");
            }
            return new Todo(args);
        case "D":
            if (args.length != 3) {
                throw new IllegalArgumentException("Deadline should have exactly 3 arguments.");
            }
            return new Deadline(args);
        case "E":
            if (args.length != 4) {
                throw new IllegalArgumentException("Events should have exactly 4 arguments.");
            }
            return new Event(args);
        default:
            throw new IllegalArgumentException("Unknown Task type.");
        }
    }

    /**
     * Helper method for toSaveString. <br>
     * Gets a formatted description and mark status of the Task, and
     * passes it to the child classes.
     * @return {Mark Status} | {Description} String, to append to toSaveString
     *         implementation in child classes.
     */
    protected String getSaveInfo() {
        String checkStatus = isDone ? "1" : "0";
        return checkStatus + "|" + description;
    }

    /**
     * Helper function to determine the checkmark status of the Task.
     * @return "[X]" if the task is done, "[ ]" otherwise.
     */
    private String getCheckbox() {
        return isDone ? "[X]" : "[ ]";
    }

    /**
     * Flags the task to be completed, if able.
     * @throws TrackerBotException if the Task is already done.
     */
    public void markTask() throws TrackerBotException {
        if (isDone) {
            throw new TrackerBotException("The specified task is already completed.");
        }
        isDone = true;
    }

    /**
     * Flags the task to be incomplete, if able.
     * @throws TrackerBotException if the Task is already still in progress.
     */
    public void unmarkTask() throws TrackerBotException {
        if (!isDone) {
            throw new TrackerBotException("This task is already in progress.");
        }
        isDone = false;
    }

    /**
     * Check if the description of the Task contains the query String.
     * @param searchStr The query to match any point in the description.
     * @return true, if the description contains the searchStr, and false
     *         otherwise.
     */
    public boolean doesContain(String searchStr) {
        return description.contains(searchStr);
    }

    /**
     * toString method of Task. <br>
     * A Task is formatted as "[X] description of task", where the X may or
     * may not be present depending on the completion status of the task.
     * @return The String representation of the Task.
     */
    @Override
    public String toString() {
        return getCheckbox() + " " + description;
    }
}

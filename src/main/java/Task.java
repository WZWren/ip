/**
 * The Task class for TrackerBot. <br>
 * The Task class abstracts each checklist item inside a Reminder-Type app. <br>
 * As of Level 4, Task should no longer be instantiated as a base task, as To-do
 * exists as a basic Task. Therefore, Task has been converted to an Abstract class.
 *
 * @author WZWren
 * @version Level-4
 */
public abstract class Task {
    /** The description of the task instance. **/
    private String description;

    /** The status of the task instance. If true, the task is done. */
    private boolean isDone;

    /**
     * Constructor for the Task class.
     * @param desc The description of the task to create.
     */
    public Task(String desc) {
        this.description = desc;
        this.isDone = false;
    }

    // TODO: Create the Factory method.
    public static Task of(String type, String... args) {
        switch (type) {
        case "1":
            System.out.println("1");
            break;
        case "2":
            System.out.println("2");
            break;
        default:
            break;
        }
        return null;
    }

    /**
     * Parses the save string in the input file. <br>
     * The save string should have the following format: <br>
     * {Task Flag} | {Mark Status} | {Description} | {Fields...}
     * <ul>
     *  <li>Task Flag represents the type of Task to create.</li>
     *  <li>Mark Status is a 0/1 representation of the Checkmark status.</li>
     *  <li>Description is the main body of the Task item to display.</li>
     *  <li>Fields are the additional fields required by the Task, delimited by '|'.</li>
     * </ul>
     *
     * @param saveStr The input save string, in the aforementioned format.
     * @return The Task object from parsing the String.
     */
    public static Task parseSaveLine(String saveStr) {
        // TODO: Implement after creating the Factory method for Task
        return null;
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
     * @return true if the task is successfully marked to completion,
     *         false if the task is already completed.
     */
    public boolean markTask() {
        if (isDone) {
            return false;
        }
        isDone = true;
        return true;
    }

    /**
     * Flags the task to be incomplete, if able.
     * @return true if the task is successfully marked to be incomplete,
     *         false if the task is already marked as incomplete.
     */
    public boolean unmarkTask() {
        if (!isDone) {
            return false;
        }
        isDone = false;
        return true;
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

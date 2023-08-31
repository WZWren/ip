import ip.utils.Pair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

// TODO: Remove UI logic from Storage

public class Storage {
    /**
     * Task Array - as TrackerBot is not instantiated, this must be static.
     * The Task List array itself should be immutable, in case we override it
     * during runtime.
     */
    private final ArrayList<Task> TASKS = new ArrayList<>();

    public boolean markTask(int index) throws TrackerBotException {
        Task task = getTask(index);
        // happy path: the task does not exist.
        if (task == null) {
            throw new TrackerBotException("The specified task does not exist.");
        }

        if (!task.markTask()) {
            throw new TrackerBotException("The specified task is already completed.");
        }
        System.out.println("This task has been marked as completed.\n  " + task);
        return true;
    }

    public boolean unmarkTask(int index) throws TrackerBotException {
        Task task = getTask(index);
        // happy path: the task does not exist.
        if (task == null) {
            throw new TrackerBotException("The specified task does not exist.");
        }

        // we can use an exception here to denote the task is completed
        if (!task.unmarkTask()) {
            throw new TrackerBotException("This task is already in progress.");
        }
        System.out.println("The task has been marked as incomplete.\n  " + task);
        return true;
    }

    /**
     * Delete function for the app. <br>
     * Attempts to delete the item in the task list. If the Task does not exist,
     * prints an appropriate error message.
     * @param index The index of the list to unmark.
     */
    public void delete(int index) throws TrackerBotException {
        Task task = getTask(index);
        // happy path: the task does not exist.
        if (task == null) {
            throw new TrackerBotException("The specified task does not exist.");
        }

        TASKS.remove(index - 1);
        System.out.println("I have removed this task off of my list.\n  " + task);
        System.out.println(TASKS.size() + " task(s) remain on my list.");
    }

    /**
     * Function that adds a task to the app. <br>
     * Adds a To-Do, Event or Deadline task to the task list.
     * @param input The Pair&lt;Command, String&gt; of the task to add to the list.
     */
    public void add(Pair<CommandType, String> input) throws TrackerBotException {
        TASKS.add(Parser.parseAdd(input));
    }

    public void read() {
        Path path = Paths.get("TrackerBot", "data.txt");
        if (Files.notExists(path)) {
            return;
        }

        try (Scanner input = new Scanner(new FileReader(path.toFile()))) {
            while (input.hasNextLine()) {
                TASKS.add(parseSaveLine(input.nextLine()));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println(e.getMessage());
            TASKS.clear();
        }
    }

    public void save() {
        Path path = Paths.get("TrackerBot", "data.txt");
        File file = path.toFile();
        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try (FileOutputStream output = new FileOutputStream(file, false)) {
            for (int i = 1; i < TASKS.size() + 1; i++) {
                output.write(TASKS.get(i - 1).toSaveString().getBytes());
                output.write("\n".getBytes());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } // the try with resources statement auto-closes output.
    }

    public void list() {
        // happy path: prints an appropriate message and exit the method.
        if (TASKS.size() == 0) {
            System.out.println("No tasks have been added to the list yet.");
            return;
        }

        System.out.println("I am tracking these tasks:");
        System.out.print(getListOfTasks());
    }

    /**
     * Parses the save string in the input file. <br>
     * The save string is generated by the {@link #... toSaveString}
     * method, and will be parsed based on the format.
     *
     * @param saveStr The input save string, in the aforementioned format.
     * @return The Task object from parsing the String.
     */
    private static Task parseSaveLine(String saveStr) throws DateTimeParseException {
        String delimiter = "|";
        String[] args = saveStr.split("[|]");
        return Task.ofSaveString(args[0], Arrays.copyOfRange(args, 1, args.length));
    }

    /**
     * Gets the Task at the provided index.
     * @param index The index of the list to check.
     * @return The Task object at the index, if it exists, and null otherwise.
     */
    private Task getTask(int index) {
        // happy path: return null if out of bounds.
        if (index <= 0 || index > TASKS.size()) {
            return null;
        }
        return TASKS.get(index - 1);
    }

    private String getListOfTasks() {
        StringBuilder result = new StringBuilder();

        for (int i = 1; i < TASKS.size() + 1; i++) {
            result.append(i);
            result.append(". ");
            result.append(TASKS.get(i - 1).toString());
            if (i != TASKS.size()) {
                result.append("\n");
            }
        }

        return result.toString();
    }
}

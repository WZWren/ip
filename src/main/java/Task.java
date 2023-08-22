/**
 * The Task class for TrackerBot. <br>
 * The Task class abstracts each checklist item inside a Reminder-Type app.
 *
 * @author WZWren
 * @version Level-3
 */
public class Task {
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
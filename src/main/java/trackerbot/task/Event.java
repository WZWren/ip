package trackerbot.task;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

import trackerbot.exception.TrackerBotException;
import trackerbot.utils.TaskDateHandler;

/**
 * The Event class for TrackerBot, inheriting from the Task class. <br>
 * <p>This Task child contains 2 LocalDateTime objects to denote the interval to
 * complete the Task by.</p>
 * <p>As of version A-JavaDoc, the interval does not explicitly check for validity of
 * this time period.</p>
 *
 * @author WZWren
 * @version A-JavaDoc
 */
public class Event extends Task {
    /** The start date of the event. **/
    private LocalDateTime from;

    /** The end date of the event. **/
    private LocalDateTime to;

    /**
     * Constructor for the class.
     *
     * @param desc The description of the Event task.
     * @param from The String representation of the start date to parse into a LocalDateTime object.
     * @param to The String representation of the end date to parse into a LocalDateTime object.
     * @throws DateTimeParseException if the deadline cannot be parsed by TaskDateHandler.
     * @see trackerbot.utils.TaskDateHandler#convertInputToDate
     */
    public Event(String desc, String from, String to) throws TrackerBotException {
        super(desc);
        this.from = TaskDateHandler.convertInputToDate(from);
        this.to = TaskDateHandler.convertInputToDate(to);

        assert this.to.isAfter(this.from) : "start date should be earlier than end date";
    }

    /**
     * Constructs a Event using a String array, for use in save parsing.
     *
     * @param args The arguments for constructing Event, containing isDone status in index
     *             0, description in index 1 and an epoch Date string in index 2 and 3.
     * @throws TrackerBotException if the event dates cannot be parsed by TaskDateHandler,
     *                             or if the epoch save string is corrupted.
     * @see trackerbot.utils.TaskDateHandler#convertSaveToDate
     */
    protected Event(String[] args) throws TrackerBotException {
        super(args);
        this.from = TaskDateHandler.convertSaveToDate(args[2]);
        this.to = TaskDateHandler.convertSaveToDate(args[3]);

        assert this.to.isAfter(this.from) : "start date should be earlier than end date";
    }

    @Override
    public String toSaveString() {
        return "E|" + getSaveInfo() + "|"
                + this.from.toEpochSecond(ZoneOffset.UTC) + "|"
                + this.to.toEpochSecond(ZoneOffset.UTC);
    }

    /**
     * toString method of Event. <br>
     * The String representation of To-do appends the [E] tag in front of the Task toString, and
     * the event period to the end of the toString.
     * @return "[D]" prefixed to task.toString(), and "(from: [start] / to: [end])" postfixed to task.toString().
     */
    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + TaskDateHandler.convertDateToUi(this.from)
                + " | to: " + TaskDateHandler.convertDateToUi(this.to) + ")";
    }
}

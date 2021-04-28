package main;

/**
 *
 * @author apedroza
 */
public class TransientTask extends Task{

    /**
     *
     * @param startTime
     * @param duration
     * @param date
     * @param tname
     */
    public TransientTask(float startTime, float duration, int date, Type tname) {
        super(startTime, duration, date, tname);
    }
}

package main;

/**
 *
 * @author apedroza
 */
public class RecurringTask extends Task{
    private int endDate;
    private int startDate;
    private int frequency;

    /**
     *
     * @param startTime
     * @param duration
     * @param date
     * @param tname
     * @param end
     * @param start
     * @param freq
     */
    public RecurringTask(float startTime, float duration, int date, Type tname,int end,int start, int freq) {
        super(startTime, duration, date, tname);
        this.endDate = end;
        this.startDate = start;
        this.frequency = freq;
    }//end constructor
}

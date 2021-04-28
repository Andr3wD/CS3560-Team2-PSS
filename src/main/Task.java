package main;

/**
 *
 * @author apedroza
 */
public class Task {
    private String name; //Name of task
    private float startTime;
    private float duration;
    private int date;

    /**
     *
     */
    public enum Type {

        /**
         *
         */
        RECURRING,

        /**
         *
         */
        TRANSIENT,

        /**
         *
         */
        ANTITASK
    }//end enum
    private Type typeName;

    /**
     *
     * @param startTime
     * @param duration
     * @param date
     * @param tname
     */
    public Task(float startTime,float duration, int date, Type tname)
    {
        this.startTime = startTime;
        this.duration = duration;
        this.date = date;
        this.typeName = tname;
    }//end Constuctor    
        
    /**
     *
     * @param start
     * @param dur
     * @return
     */
    public boolean overlap(float start, float dur)
    {
        return false;
    }
}

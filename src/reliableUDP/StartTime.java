package reliableUDP;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class StartTime {
    long timeGone;
    long startMseconds;
    long currentMseconds;
    long timeoutMseconds;

    StartTime(int timeoutInMseconds) {
        // work out current time in seconds and 
        Calendar cal = new GregorianCalendar();
        long sec = cal.get(Calendar.SECOND);  
        long min = cal.get(Calendar.MINUTE);           
        long hour = cal.get(Calendar.HOUR_OF_DAY);
        long milliSec = cal.get(Calendar.MILLISECOND);
        startMseconds = milliSec + (sec*1000) + (min *60000) + (hour*3600000);
        timeoutMseconds = (timeoutInMseconds);
    }

    long getTimeElapsed() {
        Calendar cal = new GregorianCalendar();
        long secElapsed = cal.get(Calendar.SECOND);
        long minElapsed = cal.get(Calendar.MINUTE);
        long hourElapsed = cal.get(Calendar.HOUR_OF_DAY);
        long milliSecElapsed = cal.get(Calendar.MILLISECOND);
        currentMseconds = milliSecElapsed + (secElapsed*1000) + (minElapsed *60000) + (hourElapsed * 3600000);
        timeGone = currentMseconds - startMseconds;
        return timeGone;
    }

    boolean timeout() {
        getTimeElapsed();
        if (timeGone >= timeoutMseconds) {
            return true;
        } else {
            return false;
        }
    }
}
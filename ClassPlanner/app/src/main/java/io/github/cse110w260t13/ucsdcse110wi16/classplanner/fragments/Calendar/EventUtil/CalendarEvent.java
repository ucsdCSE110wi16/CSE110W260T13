package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.EventUtil;
/**
 * Class that represents calendar events.
 */
public class CalendarEvent {
    public String eventID;
    public String eventType;
    public String eventTitle;
    public String eventDescr;
    public String eventSTime;
    public String eventETime;
    
    /**
     * Calendar event constructor.
     * @param id of the event
     * @param type of event
     * @param title of event
     * @param descr description of the event
     * @param sTime start time
     * @param eTime end time
     */
    public CalendarEvent(String id, String type, String title, String descr, String sTime, String eTime){
        eventID = id;
        eventType = type;
        eventTitle=title;
        eventDescr=descr;
        eventSTime=sTime;
        eventETime=eTime;
    }


}

package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.EventUtil;

public class CalendarEvent {
    public String eventID;
    public String eventType;
    public String eventTitle;
    public String eventDescr;
    public String eventSTime;
    public String eventETime;

    public CalendarEvent(String id, String type, String title, String descr, String sTime, String eTime){
        eventID = id;
        eventType = type;
        eventTitle=title;
        eventDescr=descr;
        eventSTime=sTime;
        eventETime=eTime;
    }
}

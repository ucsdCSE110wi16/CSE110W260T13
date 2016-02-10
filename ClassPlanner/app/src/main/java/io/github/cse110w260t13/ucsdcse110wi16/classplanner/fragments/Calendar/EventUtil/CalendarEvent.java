package io.github.cse110w260t13.ucsdcse110wi16.classplanner.fragments.Calendar.EventUtil;

public class CalendarEvent {
    public String eventTitle;
    public String eventDescr;
    public String eventSTime;
    public String eventETime;

    public CalendarEvent(String title, String descr, String sTime, String eTime){
        eventTitle=title;
        eventDescr=descr;
        eventSTime=sTime;
        eventETime=eTime;
    }
}

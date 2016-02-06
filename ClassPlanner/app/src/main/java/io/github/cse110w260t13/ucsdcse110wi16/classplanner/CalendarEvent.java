package io.github.cse110w260t13.ucsdcse110wi16.classplanner;

public class CalendarEvent {
    public String eventTitle;
    public String eventDescr;
    public String eventDate;
    public String eventSTime;
    public String eventETime;

    public CalendarEvent(String title, String descr, String date, String sTime, String eTime){
        eventTitle=title;
        eventDescr=descr;
        eventDate=date;
        eventSTime=sTime;
        eventETime=eTime;
    }
}

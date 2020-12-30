package com.quantsapp.remindercalendardemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {

    final int callbackId = 42;
    Activity context;
    EditText edt_time;
    TextView set_event, delete_event;

    long event_id = -1;
    String eventTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        edt_time = findViewById(R.id.edt_time);
        set_event = findViewById(R.id.set_event);
        delete_event = findViewById(R.id.delete_event);

        set_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission(callbackId, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)) {
                    /*  if (eventDate != null && title != null) {*/
                    // meeting_time_event = meeting_time_zoom;
           /* if (meeting_time_event != null) {
                showReminderOneDayBeforeEvent();
            }*/
                    //showReminderOneDayBeforeEvent();
                    showReminderOneDayBeforeEvent1();
                    //}
                }
            }
        });

        delete_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   deletereminderdate(event_id);
                deletereminderdate_new(eventTitle);
            }
        });

    }

    private void deletereminderdate_new(String eventTitle) {

        Uri eventUri;
        if (android.os.Build.VERSION.SDK_INT <= 7) {
            // the old way

            eventUri = Uri.parse("content://calendar/events");
        } else {
            // the new way

            eventUri = Uri.parse("content://com.android.calendar/events");
        }

        int result = 0;
        String projection[] = {"_id", "title"};
        Cursor cursor = getContentResolver().query(eventUri, null, null, null,
                null);

        if (cursor.moveToFirst()) {

            String calName;
            String calID;

            int nameCol = cursor.getColumnIndex(projection[1]);
            int idCol = cursor.getColumnIndex(projection[0]);
            do {
                calName = cursor.getString(nameCol);
                calID = cursor.getString(idCol);

                result = Integer.parseInt(calID);
                if (calName != null && calName.contains(eventTitle)) {
                    result = Integer.parseInt(calID);
                }

            } while (cursor.moveToNext());
            cursor.close();
        }
        Log.e("eventId", String.valueOf(result));

        ContentResolver cr = getContentResolver();
        Uri deleteUri = null;
        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, result);
        int rows = cr.delete(deleteUri, null, null);
        Toast.makeText(context, "Delete Created", Toast.LENGTH_SHORT).show();
        Log.e("Rows deleted", String.valueOf(rows));
    }

    private void deletereminderdate(long event_id) {

        Uri uri = null; /*= cr.insert(Events.CONTENT_URI, values);*/

        // get the event ID that is the last element in the Uri
        long eventID = Long.parseLong(uri.getLastPathSegment());
        // long eventID = 201;
        ContentResolver cr = getContentResolver();
        Uri deleteUri = null;
        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        int rows = cr.delete(deleteUri, null, null);
        Toast.makeText(context, "Delete Created", Toast.LENGTH_SHORT).show();
        Log.e("Rows deleted", String.valueOf(rows));
    }


    private void showReminderOneDayBeforeEvent1() {
        try {
            //138868848
//        String eventTitle = "Ready  Ready Android how r Android Android Android  "; //This is event title
//        String eventDescription = "Always happy to help u :)"; //This is event description
//        String eventDate = "09/09/2019"; //This is the event date
//        String eventLocation = "Taj Mahal, Agra, Uttar Pradesh 282001";  //This is the address ffor your event location


            eventTitle = "Test Quantsapp Webinar Neww 1111"; //This is event title
            String eventDate = edt_time.getText().toString(); //This is the event date
            String eventDescription = "Webinar on starts at " + eventDate; //This is event description
            String eventLocation = "Online";  //Thi
            // s is the address ffor your event location

            try {
                eventDescription = "Webinar on starts at " + formatDate(eventDate);
            } catch (Exception e) {
                eventDescription = "Webinar on starts at " + eventDate;
            }


//        String previousSelectedDate = null;
//        eventDate = "07-Sep-19 16:30";
            String textTime = " 10:00:00";
            @SuppressLint("SimpleDateFormat") Date date = null;
            try {
                date = new SimpleDateFormat("dd-MMM-yy HH:mm", new Locale("en")).parse(eventDate);
                String newstr = new SimpleDateFormat("MM-dd-yy, HH:mm", new Locale("en")).format(date);
                String[] dateAndTime = newstr.split("[,]");

                String textDate = dateAndTime[0];
                textTime = " " + dateAndTime[1] + ":00";
                Date actualDate = null;

                SimpleDateFormat yy = new SimpleDateFormat("MM-dd-yy", new Locale("en"));
                SimpleDateFormat yyyy = new SimpleDateFormat("MM/dd/yyyy", new Locale("en"));

                try {
                    actualDate = yy.parse(textDate);
                } catch (ParseException pe) {
//                                        System.out.println(pe.toString());
                }

//                                    System.out.print(textDate + " enhanced:  ");
//                                    System.out.println(yyyy.format(actualDate));
                eventDate = yyyy.format(actualDate);


            } catch (ParseException e) {
                e.printStackTrace();
            }


            Calendar cal = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", new Locale("en"));
            try {
                Date dEventDate = dateFormat.parse(eventDate); //Date is formatted to standard format "MM/dd/yyyy"
                cal.setTime(dEventDate);
                // cal.add(Calendar.DATE, 0); //It will return one day before calendar of eventDate
            } catch (Exception e) {
                e.printStackTrace();
            }

            //String reminderDate = dateFormat.format(cal.getTime());
            String reminderDate = dateFormat.format(cal.getTime());
            Log.e("Day before event start", reminderDate);

            String reminderDayStart = reminderDate + textTime;
//        String reminderDayStart = reminderDate + " 10:00:00";
            String reminderDayEnd = reminderDate + " 23:59:59";
            long startTimeInMilliseconds = 0, endTimeInMilliseconds = 0;


            try {
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", new Locale("en"));
                Date SDate = formatter.parse(reminderDayStart);
                Date EDate = formatter.parse(reminderDayEnd);
                startTimeInMilliseconds = SDate.getTime();
                endTimeInMilliseconds = EDate.getTime();
                Log.e("StartDate", startTimeInMilliseconds + " " + reminderDayStart);
                Log.e("EndDate", endTimeInMilliseconds + " " + reminderDayEnd);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ContentResolver cr = context.getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.CALENDAR_ID, 1);
            values.put(CalendarContract.Events.DTSTART, startTimeInMilliseconds);
            values.put(CalendarContract.Events.DTEND, endTimeInMilliseconds);
            values.put(CalendarContract.Events.TITLE, eventTitle);
            values.put(CalendarContract.Events.DESCRIPTION, eventDescription);
            values.put(CalendarContract.Events.EVENT_LOCATION, eventLocation);


            TimeZone timeZone = TimeZone.getDefault();
            values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
            values.put(CalendarContract.Events.RRULE, "FREQ=HOURLY;COUNT=1");
            values.put(CalendarContract.Events.HAS_ALARM, 1);
            Uri eventUri;

            if (Build.VERSION.SDK_INT >= 8) {
                eventUri = Uri.parse("content://com.android.calendar/events");
            } else {
                eventUri = Uri.parse("content://calendar/events");
            }
// insert event to calendar
            Uri uri = cr.insert(eventUri, values);
            Log.e("EventTest", uri.toString());

//add reminder for event
//This reminder will be show for 12/11/2013, because event date is 13/11/2013
            try {
                Uri REMINDERS_URI;
                // long id = -1;
                event_id = Long.parseLong(uri.getLastPathSegment());
                ContentValues reminders = new ContentValues();
                reminders.put(CalendarContract.Reminders.EVENT_ID, event_id);
                reminders.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                reminders.put(CalendarContract.Reminders.MINUTES, 20);
                if (Build.VERSION.SDK_INT >= 8) {
                    REMINDERS_URI = Uri.parse("content://com.android.calendar/reminders");
                } else {
                    REMINDERS_URI = Uri.parse("content://calendar/reminders");
                }
                Uri remindersUri = context.getContentResolver().insert(REMINDERS_URI, reminders);
                Log.e("RemindersTest", remindersUri.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            Toast.makeText(context, "Event Created", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkPermission(int callbackId, String... permissionsId) {
        boolean permissions = true;
        for (String p : permissionsId) {
            permissions = permissions && ContextCompat.checkSelfPermission(context, p) == PERMISSION_GRANTED;
        }

        if (!permissions)
            ActivityCompat.requestPermissions(context, permissionsId, callbackId);

        return permissions;
    }

    private void showReminderOneDayBeforeEvent() {
        try {
            // String eventTitle = "Ready  Ready Android how r Android Android Android  "; //This is event title
            //String eventDescription = "Always happy to help u :)"; //This is event description
            //String eventDate = "09/09/2019"; //This is the event date
            //String eventLocation = "Taj Mahal, Agra, Uttar Pradesh 282001";  //This is the address ffor your event location

            String eventTitle = "Event Demo Testing New"; //This is event title
            //String eventDate = "10-Dec-2020 15:25"; //This is the event date dd-MMM-yy HH:mm
            String eventDate = edt_time.getText().toString(); //This is the event date dd-MMM-yy HH:mm

            String eventDescription = "Demo on " + "Android" + " starts at " + eventDate; //This is event description
            String eventLocation = "Online";  //This is the address ffor your event location

            try {
                eventDescription = "Demo starts at " + formatDate(eventDate);
            } catch (Exception e) {
                eventDescription = "Demo starts at " + eventDate;
            }


            //String previousSelectedDate = null
            // eventDate = "07-Sep-19 16:30";
            String textTime = " 10:00:00";
            @SuppressLint("SimpleDateFormat") Date date = null;
            try {
                date = new SimpleDateFormat("dd-MMM-yy HH:mm:ss", new Locale("en")).parse(eventDate);
                String newstr = new SimpleDateFormat("MM-dd-yy, HH:mm:ss", new Locale("en")).format(date);
                String[] dateAndTime = newstr.split("[,]");

                String textDate = dateAndTime[0];
                //textTime = " " + dateAndTime[1] + ":00";
                textTime = " " + dateAndTime[1];
                Date actualDate = null;

                SimpleDateFormat yy = new SimpleDateFormat("MM-dd-yy", new Locale("en"));
                SimpleDateFormat yyyy = new SimpleDateFormat("MM/dd/yyyy", new Locale("en"));

                try {
                    actualDate = yy.parse(textDate);
                } catch (ParseException pe) {
                    //System.out.println(pe.toString());
                }
                //  System.out.print(textDate + " enhanced:  ");
                //  System.out.println(yyyy.format(actualDate));
                eventDate = yyyy.format(actualDate);


            } catch (ParseException e) {
                e.printStackTrace();
            }


            Calendar cal = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", new Locale("en"));
            try {
                Date dEventDate = dateFormat.parse(eventDate); //Date is formatted to standard format "MM/dd/yyyy"
                cal.setTime(dEventDate);
                // cal.add(Calendar.DATE, 0); //It will return one day before calendar of eventDate
            } catch (Exception e) {
                e.printStackTrace();
            }

            String reminderDate = dateFormat.format(cal.getTime());
            Log.e("Day before event start", reminderDate);

            String reminderDayStart = reminderDate + textTime;
            //String reminderDayStart = reminderDate + " 10:00:00";
            String reminderDayEnd = reminderDate + " 23:59:59";
            long startTimeInMilliseconds = 0, endTimeInMilliseconds = 0;


            try {
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", new Locale("en"));
                Date SDate = formatter.parse(reminderDayStart);
                Date EDate = formatter.parse(reminderDayEnd);
                startTimeInMilliseconds = SDate.getTime();
                endTimeInMilliseconds = EDate.getTime();
                Log.e("StartDate", startTimeInMilliseconds + " " + reminderDayStart);
                Log.e("EndDate", endTimeInMilliseconds + " " + reminderDayEnd);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ContentResolver cr = context.getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.CALENDAR_ID, 1);
            values.put(CalendarContract.Events.DTSTART, startTimeInMilliseconds);
            values.put(CalendarContract.Events.DTEND, endTimeInMilliseconds);
            values.put(CalendarContract.Events.TITLE, eventTitle);
            values.put(CalendarContract.Events.DESCRIPTION, eventDescription);
            values.put(CalendarContract.Events.EVENT_LOCATION, eventLocation);


            TimeZone timeZone = TimeZone.getDefault();
            values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
            values.put(CalendarContract.Events.RRULE, "FREQ=HOURLY;COUNT=1");
            values.put(CalendarContract.Events.HAS_ALARM, 1);
            Uri eventUri;

            if (Build.VERSION.SDK_INT >= 8) {
                eventUri = Uri.parse("content://com.android.calendar/events");
            } else {
                eventUri = Uri.parse("content://calendar/events");
            }
            // insert event to calendar
            Uri uri = cr.insert(eventUri, values);
            Log.e("EventTest", uri.toString());

            //add reminder for event
            //This reminder will be show for 12/11/2013, because event date is 13/11/2013
            try {
                Uri REMINDERS_URI;
                long id = -1;
                id = Long.parseLong(uri.getLastPathSegment());
                ContentValues reminders = new ContentValues();
                reminders.put(CalendarContract.Reminders.EVENT_ID, id);
                reminders.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                reminders.put(CalendarContract.Reminders.MINUTES, 20);
                if (Build.VERSION.SDK_INT >= 8) {
                    REMINDERS_URI = Uri.parse("content://com.android.calendar/reminders");
                } else {
                    REMINDERS_URI = Uri.parse("content://calendar/reminders");
                }
                Uri remindersUri = context.getContentResolver().insert(REMINDERS_URI, reminders);
                Log.e("RemindersTest", remindersUri.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formatDate(String eventDate) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("dd-MMM-yy HH:mm", new Locale("en"));
            Date date = fmt.parse(eventDate);
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MMM-yy hh:mm aa", new Locale("en"));
            return fmtOut.format(date);
        } catch (ParseException e) {

        }


        return "";
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case callbackId: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    // call your method
                    //Start_Handler()

                    // showReminderOneDayBeforeEvent();
                    showReminderOneDayBeforeEvent1();


                } else {
                    // Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}
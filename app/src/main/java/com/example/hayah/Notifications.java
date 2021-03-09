package com.example.hayah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class Notifications extends AppCompatActivity {
    Button goToNotificationsDetails ;
    FirebaseFirestore db ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);
//        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        String user=pref.getString("HAYAH_USER", null);
        db = FirebaseFirestore.getInstance();
        db.collection("donation_notifications").whereGreaterThanOrEqualTo("date", (System.currentTimeMillis()/1000)-(604800*2)).orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println(document.getData().get("id"));
                                Map<String, Object> data = document.getData();
                                LinearLayout linearLayout = findViewById(R.id.notifications);
                                LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View itemBox = inflater.inflate(R.layout.notifcation_item,null);
                                TextView id = itemBox.findViewById(R.id.notification_id);
                                if(document.getData().get("by").toString().equals("BLOOD_BANK"))
                                    id.setText("المرسل : بنك دم");
                                else
                                    id.setText("المرسل : مستخدم");
                                TextView description = itemBox.findViewById(R.id.notification_descriptions);
                                description.setText("مريض بحاجة "+document.getData().get("unitsValue") + " وحدات دم ،" + ""+" من زمزة ( " + document.getData().get("bloodTypeValue") + " )" + " ، في مستشفى " + document.getData().get("hospitalValue") + ".");
                                Button view_details = itemBox.findViewById(R.id.view_details);
                                view_details.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(document.getData().get("by").toString().equals("USER"))
                                            startActivity(new Intent(Notifications.this,Notifcation_details.class).putExtra("id",document.getData().get("id").toString()));
                                        else
                                            startActivity(new Intent(Notifications.this,Notification_details_bloodBank.class).putExtra("id",document.getData().get("id").toString()));
                                    }
                                });


                                TextView notification_time = itemBox.findViewById(R.id.notification_time);
                                String dateString = document.getData().get("date").toString();
                                if(dateString.contains("Timestamp"))
                                    dateString = getDate(Long.parseLong( dateString.substring(18,28)));
                                else
                                    dateString = getDate(Long.parseLong(dateString));
                                notification_time.setText(dateString);
                                linearLayout.addView(itemBox);
                            }
                        } else {
                        }
                    }
                });


    }
    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        System.out.println("time 1 = "+time * 1000);
        String date = DateFormat.format("dd-MM-yyyy HH:mm:ss", cal).toString();
        System.out.println(date);


        Calendar cal2 = Calendar.getInstance(Locale.ENGLISH);
        cal2.setTimeInMillis(System.currentTimeMillis());
        System.out.println("time 2 = "+System.currentTimeMillis());
        String date2 = DateFormat.format("dd-MM-yyyy HH:mm:ss", cal2).toString();
        System.out.println(date2);


        return findDifference(date,date2);
    }

    public String findDifference(String start_date,
                   String end_date)
    {

        // SimpleDateFormat converts the
        // string format to date object
        SimpleDateFormat sdf = new SimpleDateFormat(
                "dd-MM-yyyy HH:mm:ss");

        // Try Block
        try {

            // parse method is used to parse
            // the text from a string to
            // produce the date
            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);

            // Calucalte time difference
            // in milliseconds
            long difference_In_Time
                    = d2.getTime() - d1.getTime();


            long difference_In_Seconds
                    = (difference_In_Time
                    / 1000)
                    % 60;

            long difference_In_Minutes
                    = (difference_In_Time
                    / (1000 * 60))
                    % 60;

            long difference_In_Hours
                    = (difference_In_Time
                    / (1000 * 60 * 60))
                    % 24;

            long difference_In_Years
                    = (difference_In_Time
                    / (1000l * 60 * 60 * 24 * 365));

            long difference_In_Days
                    = (difference_In_Time
                    / (1000 * 60 * 60 * 24))
                    % 365;

            String s = "";
            s+="قبل ";
             if(difference_In_Years>0)
                s +=  difference_In_Years
                         + " سنة و ";
            if(difference_In_Days>0)
                s +=  difference_In_Days
                        + " يوم و ";
            if(difference_In_Hours>0)
                s +=  difference_In_Hours
                        + " ساعة و ";
            if(difference_In_Minutes>0)
                s +=  difference_In_Minutes
                        + " دقيقة  ";
//            if(difference_In_Seconds>0)
//                s +=  difference_In_Seconds
//                        + " seconds, ";
//            s+=" ago";
            if(s.length()<=5)
                return "قبل اقل من دقيقة ";
            else
                return s;
        }

        // Catch the Exception
        catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

}
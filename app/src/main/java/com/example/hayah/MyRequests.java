package com.example.hayah;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

public class MyRequests extends AppCompatActivity {
    FirebaseFirestore db;

    Button search ;
    EditText IDeditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_requests);
        IDeditText = findViewById(R.id.insert_national_id);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String user=pref.getString("HAYAH_USER", null);
        System.out.println("user = " + user);
        db = FirebaseFirestore.getInstance();


    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        System.out.println("time 1 = " + time * 1000);
        String date = DateFormat.format("dd-MM-yyyy HH:mm:ss", cal).toString();
        System.out.println(date);


        Calendar cal2 = Calendar.getInstance(Locale.ENGLISH);
        cal2.setTimeInMillis(System.currentTimeMillis());
        System.out.println("time 2 = " + System.currentTimeMillis());
        String date2 = DateFormat.format("dd-MM-yyyy HH:mm:ss", cal2).toString();
        System.out.println(date2);


        return findDifference(date, date2);
    }

    public String findDifference(String start_date,
                                 String end_date) {

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
            s += "قبل ";
            if (difference_In_Years > 0)
                s += difference_In_Years
                        + " سنة و ";
            if (difference_In_Days > 0)
                s += difference_In_Days
                        + " يوم و ";
            if (difference_In_Hours > 0)
                s += difference_In_Hours
                        + " ساعة و ";
            if (difference_In_Minutes > 0)
                s += difference_In_Minutes
                        + " دقيقة  ";
//            if(difference_In_Seconds>0)
//                s +=  difference_In_Seconds
//                        + " seconds, ";
//            s+=" ago";
            if (s.length() <= 5)
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


    public void searchForRequests(View view){
        if(TextUtils.isEmpty(IDeditText.getText()) ){
            IDeditText.setError("هذا الحقل مطلوب");
        }else if(IDeditText.getText().toString().length()!=10){
            IDeditText.setError("  الرجاء ادخال قيمة صحيحة من ١٠ خانات");
        }else{
            String id = IDeditText.getText().toString();
            db.collection("new-requests").whereEqualTo("nationalIdValue",id)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    System.out.println(document.getData().get("id"));
                                    Map<String, Object> data = document.getData();
                                    LinearLayout linearLayout = findViewById(R.id.my_requests_layout);
                                    LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View itemBox = inflater.inflate(R.layout.my_request, null);
                                    TextView national_id = itemBox.findViewById(R.id.national_id);
                                    String nationalIdValue = document.getData().get("nationalIdValue").toString();
                                    national_id.setText(nationalIdValue);
                                    TextView notification_time = itemBox.findViewById(R.id.date);
                                    String dateString = document.getData().get("date").toString();
                                    dateString = getDate(Long.parseLong(dateString));

                                    TextView name = itemBox.findViewById(R.id.name);
                                    name.setText(document.getData().get("nameValue").toString());

                                    TextView dateValue = itemBox.findViewById(R.id.dateOfBirth);
                                    dateValue.setText(document.getData().get("dateValue").toString());

                                    TextView hospital = itemBox.findViewById(R.id.hospital);
                                    hospital.setText(document.getData().get("hospitalValue").toString());

                                    TextView status = itemBox.findViewById(R.id.status);

                                    String acceptedBy = data.getOrDefault("acceptedBy","None").toString();
                                    String rejectedBy = data.getOrDefault("rejectedBy","None").toString();
                                    String rejectedFor = data.getOrDefault("rejectedFor","None").toString();
                                    System.out.println("acceptedBy = " + acceptedBy);
                                    if(acceptedBy != "None") {
                                        status.setText("تم قبول الاستدعاء");
                                        itemBox.findViewById(R.id.reason).setVisibility(View.INVISIBLE);
                                        itemBox.findViewById(R.id.label_reason).setVisibility(View.INVISIBLE);
                                    }
                                    else if(rejectedBy != "None"){
                                        status.setText("تم رفض الاستدعاء");
                                        TextView rejectedForReason = itemBox.findViewById(R.id.reason);
                                        rejectedForReason.setText(rejectedFor);
                                    }
                                    else{
                                        status.setText("بانتظار مراجعة الاستدعاء");
                                        itemBox.findViewById(R.id.reason).setVisibility(View.INVISIBLE);
                                        itemBox.findViewById(R.id.label_reason).setVisibility(View.INVISIBLE);
                                    }




                                    notification_time.setText(dateString);
                                    linearLayout.addView(itemBox);
                                }
                            } else {
                            }
                        }
                    });

            db.collection("rejected-requests").whereEqualTo("nationalIdValue",id)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    System.out.println(document.getData().get("id"));
                                    Map<String, Object> data = document.getData();
                                    LinearLayout linearLayout = findViewById(R.id.my_requests_layout);
                                    LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View itemBox = inflater.inflate(R.layout.my_request, null);
                                    TextView national_id = itemBox.findViewById(R.id.national_id);
                                    String nationalIdValue = document.getData().get("nationalIdValue").toString();
                                    national_id.setText(nationalIdValue);
                                    TextView notification_time = itemBox.findViewById(R.id.date);
                                    String dateString = document.getData().get("date").toString();
                                    dateString = getDate(Long.parseLong(dateString));

                                    TextView name = itemBox.findViewById(R.id.name);
                                    name.setText(document.getData().get("nameValue").toString());

                                    TextView dateValue = itemBox.findViewById(R.id.dateOfBirth);
                                    dateValue.setText(document.getData().get("dateValue").toString());

                                    TextView hospital = itemBox.findViewById(R.id.hospital);
                                    hospital.setText(document.getData().get("hospitalValue").toString());

                                    TextView status = itemBox.findViewById(R.id.status);

                                    String acceptedBy = data.getOrDefault("acceptedBy","None").toString();
                                    String rejectedBy = data.getOrDefault("rejectedBy","None").toString();
                                    String rejectedFor = data.getOrDefault("rejectedFor","None").toString();
                                    System.out.println("acceptedBy = " + acceptedBy);
                                    if(acceptedBy != "None") {
                                        status.setText("تم قبول الاستدعاء");
                                        itemBox.findViewById(R.id.reason).setVisibility(View.INVISIBLE);
                                        itemBox.findViewById(R.id.label_reason).setVisibility(View.INVISIBLE);
                                    }
                                    else if(rejectedBy != "None"){
                                        status.setText("تم رفض الاستدعاء");
                                        TextView rejectedForReason = itemBox.findViewById(R.id.reason);
                                        rejectedForReason.setText(rejectedFor);
                                    }
                                    else{
                                        status.setText("بانتظار مراجعة الاستدعاء");
                                        itemBox.findViewById(R.id.reason).setVisibility(View.INVISIBLE);
                                        itemBox.findViewById(R.id.label_reason).setVisibility(View.INVISIBLE);
                                    }




                                    notification_time.setText(dateString);
                                    linearLayout.addView(itemBox);
                                }
                            } else {
                            }
                        }
                    });

            db.collection("accepted-requests").whereEqualTo("nationalIdValue",id)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    System.out.println(document.getData().get("id"));
                                    Map<String, Object> data = document.getData();
                                    LinearLayout linearLayout = findViewById(R.id.my_requests_layout);
                                    LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View itemBox = inflater.inflate(R.layout.my_request, null);

                                    TextView national_id = itemBox.findViewById(R.id.national_id);
                                    String nationalIdValue = document.getData().get("nationalIdValue").toString();
                                    national_id.setText(nationalIdValue);

                                    TextView notification_time = itemBox.findViewById(R.id.date);
                                    String dateString = document.getData().get("date").toString();
                                    dateString = getDate(Long.parseLong(dateString));

                                    TextView name = itemBox.findViewById(R.id.name);
                                    name.setText(document.getData().get("nameValue").toString());

                                    TextView dateValue = itemBox.findViewById(R.id.dateOfBirth);
                                    dateValue.setText(document.getData().get("dateValue").toString());

                                    TextView hospital = itemBox.findViewById(R.id.hospital);
                                    hospital.setText(document.getData().get("hospitalValue").toString());

                                    TextView status = itemBox.findViewById(R.id.status);

                                    String acceptedBy = data.getOrDefault("acceptedBy","None").toString();
                                    String rejectedBy = data.getOrDefault("rejectedBy","None").toString();
                                    String rejectedFor = data.getOrDefault("rejectedFor","None").toString();
                                    System.out.println("acceptedBy = " + acceptedBy);
                                    if(acceptedBy != "None") {
                                        status.setText("تم قبول الاستدعاء");
                                        itemBox.findViewById(R.id.reason).setVisibility(View.INVISIBLE);
                                        itemBox.findViewById(R.id.label_reason).setVisibility(View.INVISIBLE);
                                    }
                                    else if(rejectedBy != "None"){
                                        status.setText("تم رفض الاستدعاء");
                                        TextView rejectedForReason = itemBox.findViewById(R.id.reason);
                                        rejectedForReason.setText(rejectedFor);
                                    }
                                    else{
                                        status.setText("بانتظار مراجعة الاستدعاء");
                                        itemBox.findViewById(R.id.reason).setVisibility(View.INVISIBLE);
                                        itemBox.findViewById(R.id.label_reason).setVisibility(View.INVISIBLE);
                                    }




                                    notification_time.setText(dateString);
                                    linearLayout.addView(itemBox);
                                }
                            } else {
                            }
                        }
                    });

        }
    }
}
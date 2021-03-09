package com.example.hayah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.UUID;

public class Settings extends AppCompatActivity {
    Spinner state;
    Spinner blood_type;
    Button saveSettings;
    CheckBox receiveNotification;

    RadioButton radioButtonMyBloodType;
    RadioButton radioButtonAllBloodType;
    RadioButton radioButtonAllCities;
    RadioButton radioButtonMyCity;

    TextView label1;
    TextView label2;
    TextView label3;
    TextView label4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        state=findViewById(R.id.states_spinner);
        saveSettings = findViewById(R.id.save_settings);
        receiveNotification = findViewById(R.id.checkBox7);
        radioButtonMyBloodType = findViewById(R.id.my_blood_type);
        radioButtonAllBloodType= findViewById(R.id.all_blood_types);
        radioButtonAllCities= findViewById(R.id.all_country);
        radioButtonMyCity= findViewById(R.id.inside_city);
        label1 =findViewById(R.id.label_1);
        label2 =findViewById(R.id.label_2);
        label3 =findViewById(R.id.label_3);
        label4 =findViewById(R.id.label_4);
        ArrayAdapter<String> myAdapter =new ArrayAdapter<String>(Settings.this, R.layout.custom_spinner ,getResources().getStringArray(R.array.states_withall));
        myAdapter.setDropDownViewResource(R.layout.custom_spinner);
        state.setAdapter(myAdapter);
        blood_type=findViewById(R.id.blood_type_spinner);
        ArrayAdapter<String> myAdapter2 =new ArrayAdapter<String>(Settings.this, R.layout.custom_spinner ,getResources().getStringArray(R.array.blood_types_withall));
        myAdapter.setDropDownViewResource(R.layout.custom_spinner);
        blood_type.setAdapter(myAdapter2);


        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String receiveNotificationSaved=pref.getString("HAYAH_USER_ReceiveNotification", null);
        if(receiveNotificationSaved != null){
            if(receiveNotificationSaved.equals("DO_NOT_RECEIVE_ANY")){
                hideAll();
                receiveNotification.setChecked(true);
                FirebaseMessaging.getInstance().deleteToken();
            }
        }

        receiveNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    hideAll();
                    editor.putString("HAYAH_USER_ReceiveNotification", "DO_NOT_RECEIVE_ANY");
                    editor.apply();
                    FirebaseMessaging.getInstance().deleteToken();
                }else{
                   showAll();
                    editor.putString("HAYAH_USER_ReceiveNotification", "RECEIVE");
                    editor.apply();
                }
            }
        });
        saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subscribeToTopic(getTopicByType(blood_type.getSelectedItem().toString()));
            }
        });



    }

    private void showAll() {
        state.setVisibility(View.VISIBLE);
        blood_type.setVisibility(View.VISIBLE);
        saveSettings.setVisibility(View.VISIBLE);

        radioButtonMyBloodType.setVisibility(View.INVISIBLE);
        radioButtonAllBloodType.setVisibility(View.INVISIBLE);
        radioButtonAllCities.setVisibility(View.INVISIBLE);
        radioButtonMyCity.setVisibility(View.INVISIBLE);

        label1.setVisibility(View.VISIBLE);
        label2.setVisibility(View.VISIBLE);
        label3.setVisibility(View.INVISIBLE);
        label4.setVisibility(View.INVISIBLE);
    }

    private void hideAll() {
        state.setVisibility(View.INVISIBLE);
        blood_type.setVisibility(View.INVISIBLE);
        saveSettings.setVisibility(View.INVISIBLE);

        radioButtonMyBloodType.setVisibility(View.INVISIBLE);
        radioButtonAllBloodType.setVisibility(View.INVISIBLE);
        radioButtonAllCities.setVisibility(View.INVISIBLE);
        radioButtonMyCity.setVisibility(View.INVISIBLE);

        label1.setVisibility(View.INVISIBLE);
        label2.setVisibility(View.INVISIBLE);
        label3.setVisibility(View.INVISIBLE);
        label4.setVisibility(View.INVISIBLE);
    }

    private void subscribeToTopic(String topic){
        FirebaseMessaging.getInstance().deleteToken();
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed Successfully to "+topic;
                        if (!task.isSuccessful()) {
                            msg = "Failed to subscribe to "+topic;
                        }
                        Toast.makeText(Settings.this, topic, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getTopicByType(String type){
        String topic;
        if(type.equals("O+"))
            topic = "O_PLUS";
        else if(type.equals("O-"))
            topic = "O_MINUS";
        else if(type.equals("AB+"))
            topic = "AB_PLUS";
        else if(type.equals("AB-"))
            topic = "AB_MINUS";
        else if(type.equals("A+"))
            topic = "A_PLUS";
        else if(type.equals("A-"))
            topic = "A_MINUS";
        else if(type.equals("B+"))
            topic = "B_PLUS";
        else if(type.equals("B-"))
            topic = "B_MINUS";
        else
            topic = "All";
        return topic;
    }
}
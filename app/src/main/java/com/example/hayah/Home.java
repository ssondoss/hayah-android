package com.example.hayah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.UUID;

public class Home extends AppCompatActivity {
    ImageButton goToSetting ;
    ImageButton goToPloicy;
    ImageButton goToRequestHelp ;
    ImageButton goToNotification ;
    ImageButton goToMyRequests ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        goToNotification=findViewById(R.id.bell);
        goToSetting =findViewById(R.id.settings);
        goToMyRequests =findViewById(R.id.my_requests);

         goToPloicy=findViewById(R.id.policies);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String user=pref.getString("HAYAH_USER", null);
        if(user == null){
            editor.putString("HAYAH_USER", UUID.randomUUID().toString());
            editor.apply();

        }

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = "Linked"+ token;
//                        Toast.makeText(Home.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        goToMyRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, MyRequests.class));

            }
        });
goToPloicy.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(Home.this, Policies.class));

    }
});
        goToSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Settings.class));
            }
        });
        goToNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Notifications.class));
            }
        });


        goToRequestHelp=findViewById(R.id.help);
        goToRequestHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,Help.class));
            }
        });
    }
}
package com.example.hayah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class Notification_details_bloodBank extends AppCompatActivity {
    FirebaseFirestore db;
    DocumentReference docRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details_blood_bank);
        Intent extras = getIntent();
        db = FirebaseFirestore.getInstance();
        String id = extras.getStringExtra("id");
        docRef = db.collection("donation_notifications").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        TextView id = findViewById(R.id.id);



                            id.setText("بنك دم");

                            TextView hospital = findViewById(R.id.hospital);
                        hospital.setText(data.get("hospitalValue").toString());
                        TextView blood_type = findViewById(R.id.blood_type);
                        blood_type.setText(data.get("bloodTypeValue").toString());
                        TextView notes = findViewById(R.id.notes);
                        notes.setText(data.get("notesValue").toString());
                        TextView hospital_phone_1 = findViewById(R.id.hospital_phone_1);
                        hospital_phone_1.setText(data.get("phoneValue").toString());
                        TextView hospital_phone_2 = findViewById(R.id.hospital_phone_2);
                        hospital_phone_2.setText(data.get("altPhoneValue").toString());
                        TextView hospital_phone_3 = findViewById(R.id.hospital_phone_3);
                        hospital_phone_3.setText(data.get("altPhoneValue2").toString());


                    } else {
                    }
                } else {
                }
            }
        });

    }
}
package com.example.hayah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Help extends AppCompatActivity {
    DatePickerDialog picker;
    TextView date;
    TextView policiesText;
    CheckBox policies;
    Spinner state;
    Spinner gender;
//    Spinner blood_component;
    Spinner blood_type ;
    ImageButton pick_date;

    EditText name;
    EditText nationalId;
    EditText hospital;
    EditText bed;
    EditText floor;
    EditText phone;
    EditText altPhone;
    EditText notes;
    EditText hospitalAddress;
//    EditText reason;
    EditText units;
    TextView textImage ;

    Bitmap bitmap;

    ImageView imageView ;
    ImageView selectImage ;
    Button send;
    private String imagePath="";

    Integer id=0;

    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        state=findViewById(R.id.spinner);
        gender=findViewById(R.id.gender_spinner);
       // blood_component=findViewById(R.id.blood_components);
        blood_type =findViewById(R.id.blood_type);
        ArrayAdapter<String> myAdapter =new ArrayAdapter<String>(Help.this,
                R.layout.custom_spinner ,getResources().getStringArray(R.array.states));
        myAdapter.setDropDownViewResource(R.layout.custom_spinner);
        state.setAdapter(myAdapter);
         ArrayAdapter<String> myAdapter3 =new ArrayAdapter<String>(Help.this, R.layout.custom_spinner ,getResources().getStringArray(R.array.blood_types));
        myAdapter3.setDropDownViewResource(R.layout.custom_spinner);
        blood_type.setAdapter(myAdapter3);
//         ArrayAdapter<String> myAdapter1 =new ArrayAdapter<String>(Help.this, R.layout.custom_spinner ,getResources().getStringArray(R.array.blood_components));
//        myAdapter1.setDropDownViewResource(R.layout.custom_spinner);
//        blood_component.setAdapter(myAdapter1);
        ArrayAdapter<String> myAdapter2 =new ArrayAdapter<String>(Help.this, R.layout.custom_spinner,getResources().getStringArray(R.array.gender));
        myAdapter2.setDropDownViewResource(R.layout.custom_spinner);
        gender.setAdapter(myAdapter2);

        name = findViewById(R.id.full_name);
        nationalId = findViewById(R.id.national_id);
        hospital = findViewById(R.id.hospital);
        bed = findViewById(R.id.bed);
        floor = findViewById(R.id.floor);
        phone = findViewById(R.id.phone);
        altPhone = findViewById(R.id.email);
        notes = findViewById(R.id.notes);
        hospitalAddress = findViewById(R.id.hospital_address);
       // reason = findViewById(R.id.need_for_blood);
        units = findViewById(R.id.admission_reason);
        selectImage=findViewById(R.id.camera);
        imageView =findViewById(R.id.image);
        send= findViewById(R.id.send);
        policies=findViewById(R.id.policiesText);
        textImage = findViewById(R.id.textImage);

        pick_date =findViewById(R.id.choose_date);
        date=(TextView) findViewById(R.id.show_date);
        date.setInputType(InputType.TYPE_NULL);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(Help.this,android.R.style.Theme_Holo_Light_Dialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                picker.show();
            }
        });
        pick_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(Help.this,android.R.style.Theme_Holo_Light_Dialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                picker.show();
            }
        });
        policiesText =findViewById(R.id.policiesText);
        String text="أقر بأنني قمت بقراءة كافة السياسات والشروط الخاصة بالتطبيق وقمت بالموافقة عليها ." ;
        final SpannableString text2= new SpannableString(text);
        ClickableSpan conditions = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                startActivity(new Intent(Help.this, Policies.class));

            }


            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#CF2424"));
            }
        };
        text2.setSpan(conditions,21,42, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        policiesText.setText(text2);
        policiesText.setHighlightColor(Color.TRANSPARENT);
        policiesText.setMovementMethod(LinkMovementMethod.getInstance());
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayChoiceDialog();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateValue = date.getText().toString();
                String cityValue = state.getSelectedItem().toString();
                String genderValue = gender.getSelectedItem().toString();
//                String bloodComponentType = blood_component.getSelectedItem().toString();
                String bloodTypeValue =  blood_type.getSelectedItem().toString() ;
                String nameValue = name.getText().toString();
                String nationalIdValue = nationalId.getText().toString();
                String hospitalValue = hospital.getText().toString();
                String bedValue = bed.getText().toString();
                String floorValue = floor.getText().toString();
                String phoneValue = phone.getText().toString();
                String altPhoneValue = altPhone.getText().toString();
                String notesValue = notes.getText().toString();
//                String reasonValue = reason.getText().toString();
                String unitsValue = units.getText().toString();
               String hospital_address = hospitalAddress.getText().toString();
//
//                DocumentReference docRef = db.collection("ids").document("ids");
//                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            if (document.exists()) {
//                                id =(int)((long) document.getData().get("id"));
//                                id = (int) id+1;
//                                docRef.update("id",id);
//                            } else {
//                                id =(int) Math.random()*1000000;
//                            }
//                        } else {
//                            id = (int) Math.random()*100000;
//                        }
//                    }
//                });

                boolean isValid = true;
                if(TextUtils.isEmpty(date.getText())){
                    isValid = false;
                    date.setError("هذا الحقل مطلوب");
                }
                if(TextUtils.isEmpty(nationalId.getText())){
                    isValid = false;
                    nationalId.setError("هذا الحقل مطلوب");
                }
                if(TextUtils.isEmpty(name.getText())){
                    isValid = false;
                    name.setError("هذا الحقل مطلوب");
                }

                if(TextUtils.isEmpty(hospital.getText())){
                    isValid = false;
                    hospital.setError("هذا الحقل مطلوب");
                }
                if(TextUtils.isEmpty(phone.getText())){
                    isValid = false;
                    phone.setError("هذا الحقل مطلوب");
                }
                if(TextUtils.isEmpty(altPhone.getText())){
                    isValid = false;
                    altPhone.setError("هذا الحقل مطلوب");
                }

                if(TextUtils.isEmpty(units.getText())){
                    isValid = false;
                    units.setError("هذا الحقل مطلوب");
                }
                if(TextUtils.isEmpty(bed.getText())){
                    isValid = false;
                    bed.setError("هذا الحقل مطلوب");
                }
                if(TextUtils.isEmpty(floor.getText())){
                    isValid = false;
                    floor.setError("هذا الحقل مطلوب");
                }
                if(TextUtils.isEmpty(hospitalAddress.getText())){
                    isValid = false;
                    hospitalAddress.setError("هذا الحقل مطلوب");
                }
                if(!policies.isChecked()){
                    isValid = false;
                    policies.setError("هذا الحقل مطلوب");
                }

                if(imagePath.equals("")){
                    isValid = false;

                    textImage.setError("هذا الحقل مطلوب");
                }else {
                    textImage.setError(null);
                }
                if(isValid){
                Map<String, Object> request = new HashMap<>();
                String id = UUID.randomUUID().toString();
                request.put("id", id);
                request.put("dateValue", dateValue);
                request.put("cityValue", cityValue);
                request.put("genderValue", genderValue);
//                request.put("bloodComponentType", bloodComponentType);
                request.put("bloodTypeValue", bloodTypeValue);
                request.put("nameValue", nameValue);
                request.put("nationalIdValue", nationalIdValue);
                request.put("hospitalValue", hospitalValue);
                request.put("bedValue", bedValue);
                request.put("floorValue", floorValue);
                request.put("phoneValue", phoneValue);
                request.put("altPhoneValue", altPhoneValue);
                request.put("hospitalAddress", hospital_address);

                    request.put("notesValue", notesValue);
//                request.put("reasonValue", reasonValue);
                request.put("unitsValue", unitsValue);
                request.put("imagePath",imagePath);
                request.put("user", getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE).getString("HAYAH_USER", null));
                Long tsLong = System.currentTimeMillis()/1000;
                Long ts = tsLong;
                request.put("date",ts);


                db.collection("new-requests").document(String.valueOf(id))
                        .set(request)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Help.this,"تم ارسال الطلب بنجاح !" , Toast.LENGTH_LONG).show();
                                startActivity(new Intent(Help.this,Home.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Help.this,"حدث خطأ بالشبكة  !" , Toast.LENGTH_LONG).show();
                            }
                        });


            }}
        });




    }


    private void uploadImage(Uri filePath) {
        String imagePath="images/"+ UUID.randomUUID().toString();
        textImage.setError(null);
        System.out.println("Start uploading 1");
        if (filePath != null) {
            System.out.println("Start uploading 2");
            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child( imagePath);

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(Help.this,
                                                    "تم تحميل الصورة بنجاح !!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(Help.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        } else
            Toast.makeText(Help.this, "on image", Toast.LENGTH_LONG).show();
        this.imagePath=imagePath;
    }


    private static final int IMAGE_PICK_REQUEST = 12345;


    private void displayChoiceDialog() {
        String choiceString[] = new String[]{"Gallery", "Camera"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setIcon(R.drawable.logo);
        dialog.setTitle("Select image from");
        dialog.setItems(choiceString,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = null;
                        if (which == 0) {
                            if (ActivityCompat.checkSelfPermission(Help.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(Help.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, IMAGE_PICK_REQUEST);
                            } intent = new Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


                        } else {
                            if (ActivityCompat.checkSelfPermission(Help.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(Help.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, IMAGE_PICK_REQUEST);
                            }
                            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        }
                        startActivityForResult(
                                Intent.createChooser(intent, "Select profile picture"), IMAGE_PICK_REQUEST);
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_REQUEST)
            if (resultCode == RESULT_OK && data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null)  // image selected from gallary
                {
                    uploadImage(selectedImageUri);

                    try {
                        Bitmap bitmap = getThumbnail(selectedImageUri);
                        imageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


//                    String imagePath = getRealPathFromURI(this, selectedImageUri);
//                    System.out.println("imagePath = " + imagePath);
//                    File imgFile = new  File(imagePath);
//                    try {
//                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                    //BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
//                        Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
//                        imageView.setImageBitmap(myBitmap);
//                    } catch (FileNotFoundException e) {
//                        System.out.println("HEY ERROR");
//                        e.printStackTrace();
//                    }

                } else {    // image captured from Help
                    bitmap = (Bitmap) data.getExtras().get("data");
                    Uri imageUri = getImageUri(this, bitmap);
                    uploadImage(imageUri);
                    imageView.setImageBitmap(bitmap);}
            } else {
                Log.d("==>", "Operation canceled!");
            }
    }

    /**
     * get actual path from uri
     *
     * @param context    context
     * @param contentUri uri
     * @return actual path
     */
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, projection, null, null, null);

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException{
        InputStream input = this.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither=true;//optional
        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > 64) ? (originalSize / 64) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true; //optional
        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//
        input = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }
}
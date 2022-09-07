package com.example.usafiapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmlpoyerRegistration extends AppCompatActivity {

    private TextView backtv;
    CircleImageView profileImage;
    private TextInputEditText registerfullname, registeremail, registerphone, registerpassword, registeridNo;
    //Spinner bloodGroupSpinner;
    private Uri resultUri;
    private AppCompatButton register_button;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            resultUri = data.getData();
            profileImage.setImageURI(resultUri);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emlpoyer_registration);

        backtv = findViewById(R.id.back_btn);
        registeridNo = findViewById(R.id.registerIdNo);
        registerfullname = findViewById(R.id.registerFullName);
        registeremail = findViewById(R.id.registerEmail);
        registerphone = findViewById(R.id.phoneNo);
        registerpassword = findViewById(R.id.registerPassword);
        profileImage = findViewById(R.id.profile_image);
        //bloodGroupSpinner = findViewById(R.id.bloodgroupSpinner);
        register_button = findViewById(R.id.Register_btn);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();


        backtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = registeremail.getText().toString().trim();
                final String phone = registerphone.getText().toString().trim();
                final String password = registerpassword.getText().toString().trim();
                final String idNo = registeridNo.getText().toString().trim();
                final String fullname = registerfullname.getText().toString().trim();
                //final String bloodgroup = bloodGroupSpinner.getSelectedItem().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    registeremail.setError("Email is Required");
                    registeremail.requestFocus();
                    validateEmail();
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    registerphone.setError("Phone Number is Required");
                    registerphone.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    registerpassword.setError("Password is Required");
                    registerpassword.requestFocus();
                    validatePassword();
                    return;
                }
                if (TextUtils.isEmpty(idNo)) {
                    registeridNo.setError("Location is Required");
                    registeridNo.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(fullname)) {
                    registerfullname.setError("Name is Required");
                    registerfullname.requestFocus();
                    return;
                }
              /*  if (bloodgroup.equals("Select Your Blood Group")) {
                    Toast.makeText(EmlpoyerRegistration.this, "Please select your blood group", Toast.LENGTH_SHORT).show();
                    return;
                }*/ else {
                    progressDialog.setMessage("Signing Up");
                    progressDialog.setCanceledOnTouchOutside(false);


                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {
                                        String error = task.getException().toString();
                                        Toast.makeText(EmlpoyerRegistration.this, "Error " + error, Toast.LENGTH_SHORT).show();
                                    } else {
                                        progressDialog.show();
                                        String currentUseerId = mAuth.getCurrentUser().getUid();
                                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(EmlpoyerRegistration.this, "Verification Email has been sent", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(EmlpoyerRegistration.this, Login.class));
                                                finish();
                                            }
                                        });
                                        userDatabaseRef = FirebaseDatabase.getInstance().getReference()
                                                .child("users").child(currentUseerId);
                                        HashMap userInfo = new HashMap();
                                        userInfo.put("id", currentUseerId);
                                        userInfo.put("name", fullname);
                                        userInfo.put("email", email);
                                        userInfo.put("idNumber", idNo);
                                        userInfo.put("phoneNo", phone);
                                        //userInfo.put("bloodgroup", bloodgroup);
                                        userInfo.put("type", "Employer");
                                        userInfo.put("search", "employer" + idNo);

                                        userDatabaseRef.updateChildren(userInfo).addOnCompleteListener(
                                                new OnCompleteListener() {
                                                    @Override
                                                    public void onComplete(@NonNull Task task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(EmlpoyerRegistration.this, "Data set Successfully", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(EmlpoyerRegistration.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                        finish();
                                                        progressDialog.dismiss();
                                                    }
                                                });
                                        if (resultUri != null) {
                                            final StorageReference filePath = FirebaseStorage.getInstance().getReference()
                                                    .child("profile images").child(currentUseerId);
                                            Bitmap bitmap = null;

                                            try {
                                                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);

                                            } catch (IOException exception) {
                                                exception.printStackTrace();
                                            }
                                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                                            byte[] data = byteArrayOutputStream.toByteArray();
                                            UploadTask uploadTask = filePath.putBytes(data);

                                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(EmlpoyerRegistration.this, "Image Upload Failed"+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    if (taskSnapshot.getMetadata() != null && taskSnapshot.getMetadata().getReference() != null) {
                                                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                String imageUri = uri.toString();
                                                                Map newImageMap = new HashMap();
                                                                newImageMap.put("profilepictureuri", imageUri);
                                                                userDatabaseRef.updateChildren(newImageMap).addOnCompleteListener(new OnCompleteListener() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task task) {
                                                                        if (task.isSuccessful()) {
                                                                            Toast.makeText(EmlpoyerRegistration.this, "Image added succsessfully", Toast.LENGTH_SHORT).show();
                                                                        } else {
                                                                            Toast.makeText(EmlpoyerRegistration.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                                finish();
                                                            }
                                                        });
                                                    }
                                                }
                                            });

                                            //progressDialog.dismiss();

                                        }

                                    }
                                }
                            }
                    );
                }

            }
        });



    }
    /*private boolean validateEmail() {
        String email_address = email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email_address.isEmpty()) {
            email.setError("Please fill your email address");
            email.requestFocus();
            return false;
        } else if (!email_address.matches(emailPattern)) {
            email.setError("Please enter a valid email address");
            email.requestFocus();
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;

        }

    }*/

    private boolean validatePassword() {
        String password_filed = registerpassword.getText().toString();
        //String confirm_password = con_pass.getEditText().getText().toString();
        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";

        if (password_filed.isEmpty()) {
            registerpassword.setError("Please fill your password");
            registerpassword.requestFocus();
            return false;
        } else if (!password_filed.matches(PASSWORD_PATTERN)) {
            registerpassword.setError("Password must be at least 4 characters long, one uppercase, one letter and a symbol ");
            registerpassword.requestFocus();
            return false;
        } else {
            registerpassword.setError(null);
            return true;
        }


    }
    private boolean validateEmail() {
        String email_address = registeremail.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email_address.isEmpty()) {
            registeremail.setError("Please fill your email address");
            registeremail.requestFocus();
            return false;
        } else if (!email_address.matches(emailPattern)) {
            registeremail.setError("Please enter a valid email address");
            registeremail.requestFocus();
            return false;
        } else {
            registeremail.setError(null);
            //registeremail.setErrorEnabled(false);
            return true;

        }

    }
}
package arnold.aijuka.midterm_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Signup extends AppCompatActivity {
    EditText FirstName;
    EditText LastName;
    EditText Email;
    EditText Password;
    EditText PhoneNo;
    Button createBtn;
    Button cancelBtn;

    FirebaseAuth fAuth;
    FirebaseFirestore db;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //HashMap Keys
        String KEY_fNAME = "First Name";
        String KEY_lNAME = "Last Name";
        String KEY_EMAIL = "Email";
        String KEY_PHONE = "Phone";
        String KEY_PASS = "Password";

        //Initilaize authentication and firestore
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // hide the status bar
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        //Get data from user input
        FirstName = findViewById(R.id.fName);
        LastName = findViewById(R.id.lName);
        Email = findViewById(R.id.mailSign);
        Password = findViewById(R.id.passSign);
        PhoneNo = findViewById(R.id.Contact);
        createBtn = findViewById(R.id.createButton);
        cancelBtn = findViewById(R.id.cancelButton);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fName = FirstName.getText().toString().trim();
                String lName = LastName.getText().toString().trim();
                String email = Email.getText().toString().trim();
                String pass = Password.getText().toString().trim();
                String number = PhoneNo.getText().toString().trim();

                //Store user details in details
                Details data = new Details();

                data.setFirstName(fName);
                data.setLastName(lName);
                data.setEmail(email);
                data.setPassword(pass);
                data.setContact(number);

                //Error-Handling
                if(TextUtils.isEmpty(data.getFirstName()))
                {
                    FirstName.setError("An First Name is needed");
                    return;
                }
                if(TextUtils.isEmpty(data.getLastName()))
                {
                    LastName.setError("An Last Name is needed");
                    return;
                }
                if(TextUtils.isEmpty(data.getEmail()))
                {
                    Email.setError("An email is needed");
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(data.getEmail()).matches())
                {
                    Email.setError("Incorrect email format");
                    return;
                }
                if(TextUtils.isEmpty(data.getPassword()))
                {
                    Password.setError("A password is needed");
                    return;
                }
                if(data.getPassword().length() < 6){
                    Password.setError("Password must be 6 characters or longer");
                    return;
                }
                if(TextUtils.isEmpty(data.getContact()))
                {
                    PhoneNo.setError("An Contact Number is needed");
                    return;
                }

                else{
                    //Create a hash map to store data into firestore
                    HashMap<String, String> user = new HashMap<>();
                    user.put(KEY_fNAME, data.getFirstName());
                    user.put(KEY_lNAME, data.getLastName());
                    user.put(KEY_EMAIL, data.getEmail());
                    user.put(KEY_PASS, data.getPassword());
                    user.put(KEY_PHONE, data.getContact());

                    fAuth.createUserWithEmailAndPassword(data.getEmail(), data.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "User created", Toast.LENGTH_SHORT).show();
                                //Create Firestore database
                                db.collection("Users")
                                        .document(data.getEmail())
                                        .set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Intent back = new Intent(getApplicationContext(), Login.class);
                                                startActivity(back);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("DBError", "onFailure: Error: " + e);
                                            }
                                        });
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Failed to create user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        //button to go back to login page
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(getApplicationContext(), Login.class);
                startActivity(back);
            }
        });

    }
}
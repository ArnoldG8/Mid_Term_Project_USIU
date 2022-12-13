package arnold.aijuka.midterm_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Dashboard extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore db;

    //Button initialisation
    Button cameraBtn, regCamera;

    //Text view
    TextView first;

    //HashMap Keys
    String KEY_fNAME = "First Name";
    String KEY_lNAME = "Last Name";
    String KEY_EMAIL = "Email";
    String KEY_PHONE = "Phone";
    String KEY_PASS = "Password";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // hide the status bar
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        //Button Assignment
        cameraBtn = findViewById(R.id.cameraBtn);
        regCamera = findViewById(R.id.cameraPicBtn);

        //Details
        Details data = new Details();

        first = findViewById(R.id.firstValue);

        //firebase
        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        db.collection("Users")
                .document(fAuth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                data.setFirstName(document.get(KEY_fNAME).toString());
                                first.setText(data.getFirstName());
                            }
                        }
                    }
                });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next = new Intent(getApplicationContext(), Camera.class);
                startActivity(next);
            }
        });

        regCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next = new Intent(getApplicationContext(), picCamera.class);
                startActivity(next);
            }
        });

    }
}
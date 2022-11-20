package arnold.aijuka.midterm_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private EditText Email;
    private EditText Password;
    private Button Login;
    private Button Signup;

    //Initialise firebase authentication
    FirebaseAuth fAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // hide the status bar
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        //Initialize the Details class for storing user data
        Details data = new Details();

        //Create authentication instance
        fAuth = FirebaseAuth.getInstance();

        Email = findViewById(R.id.mail);
        Password = findViewById(R.id.pass);

        Login = findViewById(R.id.loginButton);
        Signup = findViewById(R.id.signupButton);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailValue = Email.getText().toString().trim();
                String passwordValue = Password.getText().toString().trim();

                data.setEmail(emailValue);
                data.setPassword(passwordValue);

                //Error-Handling
                if(TextUtils.isEmpty(data.getEmail()))
                {
                    Email.setError("An email is needed");
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
                else{
                    fAuth.signInWithEmailAndPassword(data.getEmail(), data.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent next = new Intent(getApplicationContext(),Dashboard.class);
                                startActivity(next);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next = new Intent(getApplicationContext(),Signup.class);
                startActivity(next);
            }
        });

    }
}
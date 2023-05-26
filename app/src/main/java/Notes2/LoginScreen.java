package Notes2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notespro2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class LoginScreen extends AppCompatActivity {

    EditText edtemail,edtpass;
    MaterialButton loginbtn;
    ProgressBar progressBar;
    TextView regtxtbtn;
    ProgressDialog dialog;

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        edtemail = findViewById(R.id.logEmail);
        edtpass = findViewById(R.id.logPass);
        loginbtn = findViewById(R.id.loginBtn);
        regtxtbtn = findViewById(R.id.regtxtbtn);
        progressBar = findViewById(R.id.progress_circular1);
        auth = FirebaseAuth.getInstance();

//        dialog = new ProgressDialog(this);
//        dialog.setTitle("Logging Account");
//        dialog.setMessage("Please wait");
//        dialog.setCancelable(false);

        regtxtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginScreen.this,MainActivity.class));
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtemail.getText().toString();
                String password = edtpass.getText().toString();

                boolean isvalid = validateEmailFormate(email,password);
                if (!isvalid){
                    return;
                }

                loginUserWithEmailAndPasswordFirebase(email,password);
            }
        });

    }

    private void loginUserWithEmailAndPasswordFirebase(String email, String password) {
//        dialog.getProgress();
//        dialog.show();
        changeInProgressBar(true);
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgressBar(false);
//                dialog.dismiss();
                if (task.isSuccessful()){
                    if (auth.getCurrentUser().isEmailVerified()){
                        Toast.makeText(LoginScreen.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginScreen.this, Homepage.class));
                        finish();
                    }else {
                        Toast.makeText(LoginScreen.this, "Email not Verified", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginScreen.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void changeInProgressBar(boolean inProgress){
        if (!inProgress){
            progressBar.setVisibility(View.GONE);
            loginbtn.setVisibility(View.VISIBLE);
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
            loginbtn.setVisibility(View.GONE);
        }
    }

    private boolean validateEmailFormate(String email, String password) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtemail.setError("Enter valid email");
            return false;
        }
        if (password.length() < 6){
            edtpass.setError("password must be greater than 6 digits");
            return false;
        }
        return true;
    }
}
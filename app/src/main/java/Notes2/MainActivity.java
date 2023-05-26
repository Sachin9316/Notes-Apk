package Notes2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notespro2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText edtemail, edtpass, edtcnfpass;
    TextView btnlogin;
    MaterialButton btncreate;
    ProgressBar progressBar;
    private FirebaseAuth auth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtemail = findViewById(R.id.edtEmail);
        edtpass = findViewById(R.id.edtPass);
        edtcnfpass = findViewById(R.id.edtConfPass);
        btncreate = findViewById(R.id.createBtn);
        progressBar = findViewById(R.id.progress_circular);
        btnlogin = findViewById(R.id.logintxt);
        auth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setTitle("Account creating");
        dialog.setMessage("Loading..");
        dialog.setProgressStyle(CircularProgressIndicator.VISIBLE);
        dialog.setCancelable(false);

        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtemail.getText().toString();
                String pass = edtpass.getText().toString();
                String cnfpass = edtcnfpass.getText().toString();

                boolean isvalid = validateEmail(email, pass, cnfpass);
                if (!isvalid) {
                    return;
                }

                createAccountInFirebase(email, pass);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginScreen.class));
            }
        });
    }

    private void createAccountInFirebase(String email, String pass) {
        changeInProgress(true);
        dialog.show();
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Account Created , check email to verify", Toast.LENGTH_SHORT).show();
                    auth.getCurrentUser().sendEmailVerification();
                    auth.signOut();
                    dialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void changeInProgress(boolean inProgress) {
        if (!inProgress) {
            progressBar.setVisibility(View.GONE);
            btncreate.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            btncreate.setVisibility(View.GONE);
        }
    }

    private boolean validateEmail(String email, String pass, String cnfpass) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtemail.setError("Enter Valid Email");
            return false;
        }
        if (pass.length() < 6) {
            edtpass.setError("password must be greater than 6 digits");
            return false;
        }
        if (!pass.equals(cnfpass)) {
            edtcnfpass.setError("Password mismatch");
            return false;
        }
        return true;
    }
}
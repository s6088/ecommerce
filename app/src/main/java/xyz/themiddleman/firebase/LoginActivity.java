package xyz.themiddleman.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    private FirebaseAuth auth;


    private TextView email;
    private TextView password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);


        auth = FirebaseAuth.getInstance();




    }


    public void onClick(View v){
        if(v.getId()==R.id.signInBtn){

            auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                if(auth.getCurrentUser().isEmailVerified()){
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    make("please verify your email");
                                }
                            }
                            else{
                                make("login unsuccessful");
                            }
                        }
                    });



        }
    }


    public void make (String s){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
}

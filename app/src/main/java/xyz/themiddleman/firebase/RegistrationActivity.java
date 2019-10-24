package xyz.themiddleman.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {


    private FirebaseAuth auth;
    private DatabaseReference dr;

    public static String [] occupation = { "student", "doctor", "businessman"};
    private TextView email;
    private TextView name;
    private Spinner spinner;
    private TextView password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        name = findViewById(R.id.name);
        spinner = findViewById(R.id.spinner);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        spinner.setAdapter( new ArrayAdapter<>( this, R.layout.support_simple_spinner_dropdown_item, occupation));


        auth = FirebaseAuth.getInstance();
        dr = FirebaseDatabase.getInstance().getReference("users");

        if(auth.getCurrentUser() != null){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }

    }



    public void onClick (View v){

        if(v.getId()==R.id.signUpBtn){




            auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                auth.getCurrentUser().sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {


                                                User user = new User();
                                                user.setName( name.getText().toString() );
                                                user.setOccupation( spinner.getSelectedItem().toString() );
                                                user.setUid( auth.getUid() );

                                                dr.child(auth.getUid()).setValue(user);

                                                make("email sent");
                                            }
                                        });
                            }
                            else {
                                make("unsuccessful signup");
                            }


                        }
                    });













        }
        else if(v.getId()==R.id.signInBtn){

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);


        }
        else {


            auth.sendPasswordResetEmail(email.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                make("resent link has sent");
                            }
                            else  make("unsuccessful");
                        }
                    });


        }

    }



    public void make (String s){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
}

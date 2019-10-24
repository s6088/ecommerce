package xyz.themiddleman.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {


    private Button sendBtn;
    private EditText message;
    private ListView listView;

    private List<Message> messageList;

    private CustomAdapter customAdapter;

    private FirebaseAuth auth;

    private User user;

    private DatabaseReference dr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();
        dr = FirebaseDatabase.getInstance().getReference();
        messageList = new ArrayList<>();

        message = findViewById(R.id.message);
        sendBtn = findViewById(R.id.sendBtn);


        listView = findViewById(R.id.list_view);


        dr.child("users").child( auth.getUid() ).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                sendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendTextMessage();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });


        dr.child("chatgroups").child("1").limitToLast(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {


                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Message upload = postSnapshot.getValue(Message.class);
                    messageList.add(upload);
                }


                customAdapter = new CustomAdapter();
                listView.setAdapter(customAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







    }


    private void sendTextMessage() {
        Message upload = new Message();
        upload.setMessage(message.getText().toString());
        upload.setSender( user.getName() );
        String uploadId = dr.child("chatgroups").child("1").push().getKey();
        dr.child("chatgroups").child("1").child(uploadId).setValue(upload);
        message.setText("");

    }


    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return messageList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.message_item, null);

            TextView msg = view.findViewById(R.id.msg);
            TextView sender = view.findViewById(R.id.sender);

            Message message = messageList.get(i);

            msg.setText( message.getMessage() );
            sender.setText( message.getSender() );

            return view;
        }
    }

}

package rathore.book_a_book.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import rathore.book_a_book.R;
import rathore.book_a_book.pojos.Links;
import rathore.book_a_book.pojos.UserPojo;

import static rathore.book_a_book.R.drawable.user;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final SharedPreferences userLoggedIn = getSharedPreferences("PREF_NAME",MODE_PRIVATE);
                if(userLoggedIn.getString("userPhone","none").equals("none")){
                    startActivity(new Intent(SplashActivity.this,LoginPage.class));
                    finish();
                } else {
                    Links.showDialog(SplashActivity.this, "Checking Login Information...");
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot snap : dataSnapshot.getChildren()){
                                UserPojo user = snap.getValue(UserPojo.class);
                                if(user.getPHONE().equals(userLoggedIn.getString("userPhone","none"))){
                                    Links.cancelDialog();
                                    Toast.makeText(SplashActivity.this, "Welcome Back " + user.getNAME(), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SplashActivity.this,HomePage.class));
                                    finish();
                                    return;
                                }
                            }
                            startActivity(new Intent(SplashActivity.this,LoginPage.class));
                            finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Links.cancelDialog();
                        }
                    });
                }
                /*else
                    i = new Intent(SplashActivity.this,LoginPage.class);
                startActivity(i);
                finish();*/
            }
        },2000);
    }

    @Override
    public void onBackPressed() {
    }
}

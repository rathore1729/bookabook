package rathore.book_a_book.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import rathore.book_a_book.R;

public class ContactUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Contact Us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_contact_us);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}

package rathore.book_a_book.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import rathore.book_a_book.R;

public class ShoppingCart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("My Shopping Cart");
        setContentView(R.layout.activity_shopping_cart);
    }
}

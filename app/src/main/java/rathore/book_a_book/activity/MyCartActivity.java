package rathore.book_a_book.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import rathore.book_a_book.R;
import rathore.book_a_book.adapter.CartListingAdapter;
import rathore.book_a_book.pojos.Links;
import rathore.book_a_book.pojos.OrderPojo;

public class MyCartActivity extends AppCompatActivity {
    boolean isCheckingOut;
    String checkoutAddress;
    View snack;
    ArrayList<OrderPojo> array;
    CartListingAdapter adapter;
    ListView listCart;
    static TextView conShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("My Shopping Cart");
        setContentView(R.layout.activity_my_cart);
        Links.showDialog(this,"Loading...");
        init();
        fetchValues();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void init() {
        snack = findViewById(R.id.myCart);
        isCheckingOut = false;
        checkoutAddress = "none";
        listCart = (ListView) findViewById(R.id.listCart);
        conShop = (TextView) findViewById(R.id.conShop);
        array = new ArrayList<>();
    }

    public static void showConShop(){
        conShop.setVisibility(View.VISIBLE);
    }

    public void conShop(View view){
        startActivity(new Intent(this,HomePage.class));
        finish();
    }

    private void fetchValues() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("cart");
        DatabaseReference userCart = database.child(getSharedPreferences("PREF_NAME",MODE_PRIVATE).getString("userPhone","none"));
        userCart.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(isCheckingOut){
                    for(DataSnapshot snap : dataSnapshot.getChildren()){
                        OrderPojo order = snap.getValue(OrderPojo.class);
                        order.setAddress(checkoutAddress);
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("order").child(getSharedPreferences("PREF_NAME",MODE_PRIVATE).getString("userPhone","none"));
                        order.setId(ref.push().getKey());
                        ref.child(order.getId()).setValue(order);
                    }
                    array.clear();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cart").child(getSharedPreferences("PREF_NAME",MODE_PRIVATE).getString("userPhone","none"));
                    ref.setValue(null);
                } else {
                    for(DataSnapshot snap : dataSnapshot.getChildren()){
                        OrderPojo cartItem = snap.getValue(OrderPojo.class);
                        array.add(cartItem);
                    }
                }
                Links.cancelDialog();
                adapter = new CartListingAdapter(MyCartActivity.this,R.layout.listitemcart,array);
                listCart.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Links.cancelDialog();
                Snackbar.make(snack,"Network Error!!!",Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.submit,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.submitAll){
            if(CartListingAdapter.getCounting()==0){
                Toast.makeText(this, "Nothing is there to checkout with !!!", Toast.LENGTH_SHORT).show();
                return true;
            }
            final Dialog dialog = new Dialog(this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            final View view = inflater.inflate(R.layout.dialog_place_order,null);
            final LinearLayout back,newBack,label;
            back = view.findViewById(R.id.place_back);
            newBack = view.findViewById(R.id.place_new_back);
            label = view.findViewById(R.id.place_label);
            final TextView address = view.findViewById(R.id.place_address);
            final EditText newAddress = view.findViewById(R.id.place_new_address);
            ImageButton newAddButton = view.findViewById(R.id.place_add_address);
            Button checkout = view.findViewById(R.id.place_checkout);
            final String[] str = new String[1];
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users")
                    .child(getSharedPreferences("PREF_NAME",MODE_PRIVATE).getString("userPhone","none"));
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    str[0] = dataSnapshot.child("address").getValue(String.class);
                    if(str[0].equals("none")) {
                        back.setVisibility(View.GONE);
                        label.setVisibility(View.GONE);
                        newBack.setVisibility(View.VISIBLE);
                    }
                    else {
                        address.setText(str[0]);
                        checkoutAddress = str[0];
                    }
                    dialog.setContentView(view);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MyCartActivity.this, "Network Error!!! Enter New Address.", Toast.LENGTH_SHORT).show();
                    back.setVisibility(View.GONE);
                    label.setVisibility(View.GONE);
                    newBack.setVisibility(View.VISIBLE);
                    dialog.setContentView(view);
                }
            });

            newAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    back.setVisibility(View.GONE);
                    label.setVisibility(View.GONE);
                    newBack.setVisibility(View.VISIBLE);
                }
            });

            checkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideKeyPad();

                    if((!checkoutAddress.equals("none") && newBack.getVisibility()!=View.VISIBLE) || (!newAddress.getText().toString().trim().equals("") && newBack.getVisibility()==View.VISIBLE)) {
                        if(!newAddress.getText().toString().trim().equals("")){
                            ref.child("address").setValue(newAddress.getText().toString().trim());
                            checkoutAddress = newAddress.getText().toString().trim();
                        }
                        isCheckingOut=true;
                        fetchValues();
                        dialog.cancel();
                        Snackbar.make(snack,"Your order is placed successfully.",Snackbar.LENGTH_LONG).setAction("View Order", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(MyCartActivity.this,MyOrderActivity.class));
                                MyCartActivity.this.finish();
                            }
                        }).show();
                    } else {
                        newAddress.setError("Address field can not be empty!!!");
                    }
                }
            });
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void hideKeyPad(){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }
}

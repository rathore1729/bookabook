package rathore.book_a_book.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import rathore.book_a_book.R;
import rathore.book_a_book.pojos.Links;
import rathore.book_a_book.pojos.OrderPojo;

public class ProductPage extends AppCompatActivity {
    String link,uri;
    //String link="https://www.googleapis.com/books/v1/volumes/LukZZBZkiEQC";
    ImageView viewImage;
    RatingBar rateBar;
    TextView viewName,viewAuthor,viewCategory,viewPublish,viewPages,viewPrice,viewMRP,viewDisc,viewDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_product_page);
        init();
        webRequest();
    }

    private void init() {
        link = getIntent().getStringExtra("link");
        rateBar = (RatingBar) findViewById(R.id.ratingBar);
        viewImage = (ImageView) findViewById(R.id.viewImage);
        viewName = (TextView) findViewById(R.id.viewName);
        viewAuthor = (TextView) findViewById(R.id.viewAuthor);
        viewCategory = (TextView) findViewById(R.id.viewCategory);
        viewPublish = (TextView) findViewById(R.id.viewPublisher);
        viewPages = (TextView) findViewById(R.id.viewPages);
        viewPrice = (TextView) findViewById(R.id.viewPrice);
        viewMRP = (TextView) findViewById(R.id.viewMRP);
        viewDisc = (TextView) findViewById(R.id.viewDisc);
        viewDesc = (TextView) findViewById(R.id.viewDesc);
    }

    private void showBook(String response){
        try {
            JSONObject outer = (JSONObject) new JSONParser().parse(response);
            JSONObject infoObj = (JSONObject) outer.get("volumeInfo");

            viewName.setText((String) infoObj.get("title"));         //Title
            try{
                JSONArray auth = (JSONArray) infoObj.get("authors");    //Authors
                String authStr="";
                for (int j=0;j<auth.size();j++)
                    authStr = auth.get(j) + ",";
                viewAuthor.setText("By " + authStr.substring(0,authStr.length()-1));
            }catch (Exception ex){
                viewAuthor.setText("Not Available!!!");
            }
            viewPublish.setText(infoObj.get("publisher").toString());
            viewDesc.setText(infoObj.get("description").toString().substring(0,240));
            viewDesc.append("...and more.");
            viewPages.setText(infoObj.get("printedPageCount").toString());
            try {
                float rating = Float.parseFloat(infoObj.get("averageRating").toString());    // Ratings
                rateBar.setRating(rating);
            }catch (Exception ex){
                rateBar.setRating(3.5f);
            }
            try{
                JSONArray catArray = (JSONArray) infoObj.get("categories");    //categories
                String authStr="";
                for (int j=0;j<catArray.size();j++)
                    authStr = catArray.get(j) + ",";
                viewCategory.setText(authStr.substring(0,authStr.length()-1));
            }catch (Exception ex){
                viewCategory.setText("None");
            }
            JSONObject obj = (JSONObject) infoObj.get("imageLinks");
            try{
                Glide.with(this).load(Uri.parse(obj.get("smallThumbnail").toString())).crossFade().into(viewImage);
                uri = obj.get("smallThumbnail").toString();
            }catch (Exception ex){
                Glide.with(this).load(Uri.parse("http://books.google.com/books/content?id=vH8uAAAAYAAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api")).crossFade().into(viewImage);
                //viewImage.setImageURI(Uri.parse("http://books.google.com/books/content?id=vH8uAAAAYAAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api"));
                uri = obj.get("smallThumbnail").toString();
            }

            infoObj = (JSONObject) outer.get("saleInfo");
            if(infoObj.get("saleability").equals("FOR_SALE")){
                JSONObject dataObj = (JSONObject) infoObj.get("listPrice");
                String mrp = ((Double)dataObj.get("amount")).intValue() + ".0";
                viewMRP.append(((Double)dataObj.get("amount")).intValue() + ".0");
                dataObj = (JSONObject) infoObj.get("retailPrice");
                String price = ((Double)dataObj.get("amount")).intValue() + ".0";
                if(mrp.equals(price))
                    viewPrice.append(((Double)(Double.parseDouble(mrp)*0.9)).intValue()+".0");
                else
                    viewPrice.append(((Double)dataObj.get("amount")).intValue() + ".0");
                Double disc = 100-Double.parseDouble(price)*100/(Double.parseDouble(mrp));
                viewDisc.setText(disc.intValue() + "." + (disc-disc.intValue()+"").substring(2,3) + " % OFF");
                //deall.setDisc(100-Double.parseDouble(deall.getPrice())*100/(Double.parseDouble(deall.getMrp())) + "% OFF");
            }else{
                double mrp = Math.random() * 200 + 220;                     //MRP
                int disc = (int) (Math.random() * 20 + 30);                 //Discount
                viewDisc.setText(disc + "% OFF");
                viewMRP.append((mrp + "").substring(0,6));
                viewPrice.append((mrp * (100 - disc) / 100 + "").substring(0,6));               //Price
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void webRequest() {
        Links.showDialog(this,"Loading...");
        StringRequest rqst = new StringRequest(Request.Method.GET, link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Links.cancelDialog();
                showBook(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Links.cancelDialog();
                Toast.makeText(ProductPage.this, "Error : " + error, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(rqst);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addtocart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addToCart) {
            addToCart();
        }else if(id==R.id.home){
            startActivity(new Intent(ProductPage.this,HomePage.class));
            finish();
        }else if(id==R.id.shopCart){
            startActivity(new Intent(ProductPage.this,MyCartActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void addToCart(){
        final int srcPrice = (int) Float.parseFloat(viewPrice.getText().toString().substring(2));
        final String srcName = viewName.getText().toString(),srcAuthor = viewAuthor.getText().toString();

        final ImageButton[] add = new ImageButton[1];
        ImageButton remove;
        Button submit,cancel;
        ImageView image;
        final TextView name,author,price,quan;

        final Dialog dialog = new Dialog(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_add_to_cart,null);
        add[0] = view.findViewById(R.id.addAdd);
        remove = view.findViewById(R.id.addRemove);
        submit = view.findViewById(R.id.addSubmit);
        cancel = view.findViewById(R.id.addCancel);
        image = view.findViewById(R.id.addImage);
        name = view.findViewById(R.id.addName);
        author = view.findViewById(R.id.addAuthor);
        price = view.findViewById(R.id.addTotal);
        quan = view.findViewById(R.id.addQuan);
        final int[] srcQuantity = {Integer.parseInt(quan.getText().toString())};

        Glide.with(this).load(Uri.parse(uri)).crossFade().into(image);
        name.setText(srcName);
        author.setText(srcAuthor);
        price.setText("₹ " + srcPrice);

        add[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(srcQuantity[0] <5) {
                    srcQuantity[0]++;
                    quan.setText(srcQuantity[0] +"");
                    price.setText("₹ " + (srcPrice* srcQuantity[0]));
                }
                else
                    Toast.makeText(ProductPage.this, "Maximum quantity in one order is 5.", Toast.LENGTH_SHORT).show();
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(srcQuantity[0] >0) {
                    srcQuantity[0]--;
                    quan.setText(srcQuantity[0] +"");
                    price.setText("₹ " + (srcPrice* srcQuantity[0]));
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(quan.getText().toString())==0)
                    Toast.makeText(ProductPage.this, "Quantity is zero.", Toast.LENGTH_SHORT).show();
                else{
                    OrderPojo order = new OrderPojo();
                    order.setUri(uri);
                    order.setName(srcName);
                    order.setAuthor(srcAuthor);
                    order.setAddress("none");
                    order.setPrice(srcPrice);
                    order.setQuantity(srcQuantity[0]);
                    try{
                        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cart");
                        DatabaseReference userRef = cartRef.child(getSharedPreferences("PREF_NAME",MODE_PRIVATE).getString("userPhone","none"));
                        order.setId(userRef.push().getKey());
                        userRef.child(order.getId()).setValue(order);
                    } catch (Exception ex){
                        Log.d("1234", "onClick: " + ex);
                    }
                    dialog.cancel();
                    Toast.makeText(ProductPage.this, "Added to cart Successfully.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
    }

}

package rathore.book_a_book.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import rathore.book_a_book.R;
import rathore.book_a_book.pojos.Links;

public class ProductPage extends AppCompatActivity {
    String link;
    //String link="https://www.googleapis.com/books/v1/volumes/LukZZBZkiEQC";
    ImageView viewImage;
    RatingBar rateBar;
    TextView viewName,viewAuthor,viewCategory,viewPublish,viewPages,viewPrice,viewMRP,viewDisc,viewDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                viewAuthor.setText(authStr.substring(0,authStr.length()-1));
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
            }catch (Exception ex){
                Glide.with(this).load(Uri.parse("http://books.google.com/books/content?id=vH8uAAAAYAAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api")).crossFade().into(viewImage);
                //viewImage.setImageURI(Uri.parse("http://books.google.com/books/content?id=vH8uAAAAYAAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api"));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addtocart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addToCart) {
            Toast.makeText(this, "ADDED TO CART", Toast.LENGTH_SHORT).show();
        }else if(id==R.id.home){
            startActivity(new Intent(ProductPage.this,HomePage.class));
            finish();
        }else if(id==R.id.shopCart){
            startActivity(new Intent(ProductPage.this,ShoppingCart.class));
        }
        return super.onOptionsItemSelected(item);
    }

}

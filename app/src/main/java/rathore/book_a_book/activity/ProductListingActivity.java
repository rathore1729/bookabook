package rathore.book_a_book.activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;

import rathore.book_a_book.R;
import rathore.book_a_book.adapter.ListingAdapter;
import rathore.book_a_book.pojos.BookDeal;
import rathore.book_a_book.pojos.Links;

public class ProductListingActivity extends AppCompatActivity {
    ListingAdapter arrayAdapter;
    ArrayList<BookDeal> arrayList;
    ListView listing;
    Spinner spinner;
    String link,title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*if (getIntent().getAction().equals(Intent.ACTION_SEARCH)){
            title = "Search Results";
            link = getIntent().getStringExtra(SearchManager.QUERY);
        } else {
            title = getIntent().getStringExtra("title");
            link = getIntent().getStringExtra("link");
        }*/
        title = getIntent().getStringExtra("title");
        setTitle(title);
        setContentView(R.layout.activity_product_listing);
        init();
        methodListeners();
        webRequest();
        arrayAdapter = new ListingAdapter(this,R.layout.listitemlisting,arrayList);
        listing.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                webRequest();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void init() {
        listing = (ListView) findViewById(R.id.listingList);
        spinner = (Spinner) findViewById(R.id.listingSpinner);
        arrayList = new ArrayList<>();
        link = getIntent().getStringExtra("link");
    }

    private void methodListeners(){
        listing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ProductListingActivity.this,ProductPage.class);
                intent.putExtra("link",arrayList.get(i).getSelfLink());
                startActivity(intent);
            }
        });
    }

    private void fetchValues(String response){
        arrayList.clear();
        try{
            JSONObject data = (JSONObject) new JSONParser().parse(response);
            JSONArray array = (JSONArray) data.get("items");

            for(int i = 0; i<40; i++){
                BookDeal deal = new BookDeal();
                JSONObject obj = (JSONObject) array.get(i);
                JSONObject innerObj = (JSONObject) obj.get("volumeInfo");

                deal.setSelfLink((String) obj.get("selfLink"));         //SelfLink
                deal.setName((String) innerObj.get("title"));         //Title
                try{
                    JSONArray auth = (JSONArray) innerObj.get("authors");    //Authors
                    String authStr="";
                    for (int j=0;j<auth.size();j++)
                        authStr = auth.get(j) + ",";
                    deal.setAuthor("By " + authStr.substring(0,authStr.length()-1));
                }catch (Exception ex){
                    deal.setAuthor("Published on : " + innerObj.get("publishedDate"));
                }
                deal.setDesc("null");  //description
                JSONObject infoObj = (JSONObject) obj.get("volumeInfo");
                JSONObject dataObj;
                try{
                    dataObj = (JSONObject) infoObj.get("imageLinks");   //Image
                    deal.setImgURI((dataObj.get("smallThumbnail")+""));
                }catch (Exception ex){
                    deal.setImgURI("http://books.google.com/books/content?id=vH8uAAAAYAAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api");
                }

                infoObj = (JSONObject) obj.get("saleInfo");
                if(infoObj.get("saleability").equals("FOR_SALE")){
                    dataObj = (JSONObject) infoObj.get("listPrice");
                    deal.setMrp(((Double)dataObj.get("amount")).intValue() + ".0");
                    dataObj = (JSONObject) infoObj.get("retailPrice");
                    deal.setPrice(((Double)dataObj.get("amount")).intValue() + ".0");
                    if(deal.getMrp().equals(deal.getPrice()))
                        deal.setPrice(((Double)(Double.parseDouble(deal.getMrp())*0.9)).intValue()+".0");
                    Double disc = 100-Double.parseDouble(deal.getPrice())*100/(Double.parseDouble(deal.getMrp()));
                    deal.setDisc(disc.intValue() + "." + (disc-disc.intValue()+"").substring(2,3) + " % OFF");
                    //deal.setDisc(100-Double.parseDouble(deal.getPrice())*100/(Double.parseDouble(deal.getMrp())) + "% OFF");
                }else{
                    double mrp = Math.random() * 200 + 220;                     //MRP
                    int disc = (int) (Math.random() * 20 + 30);                 //Discount
                    deal.setDisc(disc + "% OFF");
                    deal.setMrp((mrp + "").substring(0,6));
                    deal.setPrice((mrp * (100 - disc) / 100 + "").substring(0,6));               //Price
                }

                arrayList.add(i,deal);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error in loop : " + e, Toast.LENGTH_SHORT).show();
        }
        arrayAdapter.notifyDataSetChanged();
    }

    private void webRequest() {
        String request;
        final ProgressDialog dialog = new ProgressDialog(this);
        switch (spinner.getSelectedItemPosition()){
            case 0 : request = link+ Links.RELEVANCE;
                break;
            case 1 : request = link+ Links.NEWEST;
                break;
            default: request = link+ Links.RELEVANCE;
        }
        StringRequest rqst = new StringRequest(Request.Method.GET, request, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.cancel();
                fetchValues(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancel();
                Toast.makeText(ProductListingActivity.this, "Error : " + error, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(rqst);
        dialog.setMessage("Loading...");
        dialog.show();
        dialog.setCancelable(false);
    }
}

package rathore.book_a_book.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;

import rathore.book_a_book.R;
import rathore.book_a_book.adapter.PreviewAdapter;
import rathore.book_a_book.pojos.ProductPojo;

public class ProductReview extends AppCompatActivity {
    ListView list;
    ArrayList<ProductPojo> arrayList;
    PreviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_review);
        init();
        showData();
        list.setAdapter(adapter);
    }

    private void init() {
        list = (ListView) findViewById(R.id.list);
        arrayList = new ArrayList<>();
        adapter = new PreviewAdapter(this,R.layout.listitempreview,arrayList);
    }

    private void showData(){
        JSONParser parser = new JSONParser();
        try {
            String filePath = this.getFilesDir().getPath() + "/books.json";
            FileReader file = new FileReader(filePath);
            Object obj = parser.parse(file);
            JSONObject jobj = (JSONObject) obj;
            JSONArray array = (JSONArray) jobj.get("1");
            Toast.makeText(this, "Data print : " + jobj.toString(), Toast.LENGTH_SHORT).show();
            Log.d("12345", "showData: " + jobj.toString());

            for(int i= (array.size()-1); i>=0; i--){
                ProductPojo product = new ProductPojo();
                JSONObject finalObj = (JSONObject) array.get(i);
                product.setName(finalObj.get("name").toString());
                product.setPrice(finalObj.get("disc_price").toString());
                product.setMrp(finalObj.get("ori_price").toString());
                product.setDesc(finalObj.get("desc").toString());
                product.setUri(finalObj.get("image_uri").toString());
                arrayList.add(product);
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(this, "Error in Reading : " + e, Toast.LENGTH_SHORT).show();
        }
    }
}

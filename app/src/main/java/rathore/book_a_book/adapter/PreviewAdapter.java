package rathore.book_a_book.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;
import rathore.book_a_book.R;
import rathore.book_a_book.pojos.ProductPojo;

/**
 * Created by Rathore on 10-Jul-17.
 */

public class PreviewAdapter extends ArrayAdapter {
    Context context;
    int layout;
    ArrayList<ProductPojo> arrayList;
    LayoutInflater inflater;
    public PreviewAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<ProductPojo> arrayList) {
        super(context, resource, arrayList);
        this.context = context;
        layout = resource;
        this.arrayList = arrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(layout,null);
        TextView productName,productPrice,productMRP,productDesc;
        ImageView productImage;
        productName = view.findViewById(R.id.productName);
        productPrice = view.findViewById(R.id.productPrice);
        productMRP = view.findViewById(R.id.productMRP);
        productDesc = view.findViewById(R.id.productDesc);
        productImage = view.findViewById(R.id.productImage);
        ProductPojo product = arrayList.get(position);
        productName.append(product.getName());
        productPrice.append(product.getPrice());
        productMRP.append(product.getMrp());
        productDesc.append(product.getDesc());
        Glide.with(context)
                .load(Uri.parse(product.getUri()))
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(productImage);
        return view;
    }
}

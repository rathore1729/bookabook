package rathore.book_a_book;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import rathore.book_a_book.pojos.BookDeal;

/**
 * Created by Rathore on 14-Jul-17.
 */

public class DealFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getArguments().getString("flag").equals("null")){
            View view  = inflater.inflate(R.layout.empty,container,false);
            return view;
        }
        else {
            View view = inflater.inflate(R.layout.dealitem,container,false);
            TextView name,author,desc,price,mrp,disc;
            ImageView img;
            try{
                name = view.findViewById(R.id.bookName);
                author = view.findViewById(R.id.bookAuthor);
                desc = view.findViewById(R.id.bookDesc);
                price = view.findViewById(R.id.bookPrice);
                mrp = view.findViewById(R.id.bookMRP);
                disc = view.findViewById(R.id.bookDisc);
                img = view.findViewById(R.id.bookImage);
                name.setText(getArguments().getString("name"));
                author.setText(getArguments().getString("author"));
                price.append(getArguments().getString("price"));
                mrp.append(getArguments().getString("mrp"));
                desc.setText(getArguments().getString("desc"));
                disc.setText(getArguments().getString("disc"));
                img.setImageURI(Uri.parse(getArguments().getString("img")));
            }catch (Exception ex){
                Toast.makeText(getActivity(), "Error in printing : " + ex, Toast.LENGTH_SHORT).show();
            }
            return view;
        }
    }

    public static DealFragment getInstance(BookDeal dealPojo){
        Bundle b = new Bundle();
        if(dealPojo==null){
            b.putString("flag","null");
        }else{
            try{
                b.putString("flag","notNull");
                b.putString("name",dealPojo.getName());
                b.putString("author",dealPojo.getAuthor());
                b.putString("price",dealPojo.getPrice());
                b.putString("mrp",dealPojo.getMrp());
                b.putString("desc",dealPojo.getDesc());
                b.putString("disc",dealPojo.getDisc());
                b.putString("img",dealPojo.getImgURI());
            }catch (Exception ex){
            }
        }
        DealFragment deal = new DealFragment();
        deal.setArguments(b);
        return deal;
    }
}

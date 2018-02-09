package cmw.co.id.pmc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.ArrayList;
import java.util.List;

import cmw.co.id.pmc.R;
import cmw.co.id.pmc.data.FeedItem;

/**
 * Created by CMW on 29/01/2018.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.CustomViewHolder>{
    private List<FeedItem> feedItemList;
    private Context mContext;
    //private OnItemClickListener onItemClickListener;
    View view;
    public HomeAdapter(Context context, List<FeedItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
//        this.feedItemList = new ArrayList<FeedItem>();
        // we copy the original list to the filter list and use it for setting row values
//        this.feedItemList.addAll(this.feedItemList);
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final FeedItem feedItem = feedItemList.get(i);
        //Download image using picasso library

        //Setting text view title
        customViewHolder.textView.setText(Html.fromHtml(feedItem.getTitle()));
        customViewHolder.status.setText(Html.fromHtml(feedItem.getStatus()));
        customViewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), feedItem.getTitle(), Toast.LENGTH_SHORT).show();            }
        });
      /*  final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onItemClickListener.onItemClick(feedItem);
                Toast.makeText(view.getContext(),feedItem.getTitle(), Toast.LENGTH_SHORT).show();
            }
        };*/
        /*customViewHolder.imageView.setOnClickListener(listener);
        customViewHolder.textView.setOnClickListener(listener);*/
    }
    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView textView;
        protected TextView status;

        public CustomViewHolder(View view) {
            super(view);
            this.textView = (TextView) view.findViewById(R.id.title);
            this.status = (TextView) view.findViewById(R.id.status);
        }
    }


    @Override
    public int getItemCount() {
        return feedItemList.size();
    }

    public void setFilter(List<FeedItem> countryModels) {
        feedItemList = new ArrayList<>();
        feedItemList.addAll(countryModels);
        notifyDataSetChanged();
    }
}

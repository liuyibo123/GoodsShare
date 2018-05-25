package com.upc.help_system.presenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.upc.help_system.R;
import com.upc.help_system.model.MainTable;
import com.upc.help_system.utils.Container;
import com.upc.help_system.utils.MyGson;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/23.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private static final String TAG = OrderAdapter.class.getSimpleName();
    private final ListItemClickListener mOnClickListener;
    private static int viewHolderCount;
    private final JsonArray array;
    private int mNumberItems;

    public interface ListItemClickListener {
        void onListItemClick(int itemIndex);
    }

    public OrderAdapter(int numberOfItems, ListItemClickListener listItemClickListener, JsonArray array) {
        mNumberItems = numberOfItems;
        viewHolderCount = 0;
        mOnClickListener = listItemClickListener;
        this.array = array;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.everyorder_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        Log.d(TAG, "onCreateViewHolder: before create view");
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolderCount++;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.head)
        ImageView head;
        @BindView(R.id.trade_type)
        TextView tradeType;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.distance)
        TextView distance;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.publisher)
        TextView publisher;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }
        void bind(int listIndex) {
            JsonObject goods = (JsonObject) array.get(listIndex);
            JsonObject goodsfield = goods.getAsJsonObject("fields");
            head.setImageResource(R.drawable.ic_boy_48);
            tradeType.setText(goodsfield.get("trade_type").getAsInt()==1?"租":"买");
            price.setText(goodsfield.get("price").getAsString());
            distance.setText(goodsfield.get("location").getAsString());
            description.setText("  "+goodsfield.get("name").getAsString()+"    "+goodsfield.get("description").getAsString());
            description.setPadding(20,10,10,0);
            publisher.setText(goodsfield.get("publishername").getAsString());
        }

        @Override
        public void onClick(View view) {
            int clickPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickPosition);
        }
    }
}

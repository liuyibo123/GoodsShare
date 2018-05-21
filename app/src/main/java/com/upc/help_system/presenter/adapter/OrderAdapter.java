package com.upc.help_system.presenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.upc.help_system.R;
import com.upc.help_system.model.MainTable;
import com.upc.help_system.utils.Container;
import com.upc.help_system.utils.MyGson;

import java.util.List;

/**
 * Created by Administrator on 2017/5/23.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.NumberViewHolder> {

    private static final String TAG = OrderAdapter.class.getSimpleName();
    private final ListItemClickListener mOnClickListener;
    private static int viewHolderCount;
    private int mNumberItems;
    private List<MainTable> mainTalbelist;

    public interface ListItemClickListener {
        void onListItemClick(int itemIndex);
    }

    public OrderAdapter(int numberOfItems, ListItemClickListener listItemClickListener, List<MainTable> tables) {
        mNumberItems = numberOfItems;
        viewHolderCount = 0;
        mOnClickListener = listItemClickListener;
        this.mainTalbelist = tables;
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.everyorder_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view);

        viewHolderCount++;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView listOrderType;
        TextView listOrderDestination;
        TextView listOrderCharge;
        ImageView testImg;
        TextView listOrderContent;

        public NumberViewHolder(View itemView) {
            super(itemView);
            listOrderType = (TextView) itemView.findViewById(R.id.order_type);
            listOrderDestination = (TextView) itemView.findViewById(R.id.order_destination);
            listOrderCharge = (TextView) itemView.findViewById(R.id.order_charge);
            testImg = (ImageView) itemView.findViewById(R.id.head);
            listOrderContent = (TextView) itemView.findViewById(R.id.order_content);
            itemView.setOnClickListener(this);

        }

        void bind(int listIndex) {
            MainTable table = mainTalbelist.get(listIndex);
            Log.d(TAG, MyGson.toJson(table));
            Log.d(TAG, "(listOrderType==null):" + (listOrderType == null));
            listOrderType.setText(Container.volume.values()[table.getCatagory() - 1].toString());
            testImg.setImageResource(R.drawable.ic_boy_48);
            listOrderContent.setText(table.getContent());
            listOrderCharge.setText(String.valueOf(table.getTip()));
            listOrderDestination.setText(table.getHelp_loc());
        }

        @Override
        public void onClick(View view) {
            int clickPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickPosition);
        }
    }
}

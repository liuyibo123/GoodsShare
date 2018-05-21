package com.upc.help_system.presenter.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.upc.help_system.MyApplication;
import com.upc.help_system.R;
import com.upc.help_system.model.MainTable;
import com.upc.help_system.utils.Container;
import com.upc.help_system.utils.MyGson;
import com.upc.help_system.utils.network.ConConfig;
import com.upc.help_system.utils.network.RequestService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2017/5/23.
 */

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.NumberViewHolder> {

    private static final String TAG = OrderAdapter.class.getSimpleName();
    private final ListItemClickListener mOnClickListener;
    private static int viewHolderCount;
    private int mNumberItems;

    private List<MainTable> mainTalbelist;

    public interface ListItemClickListener {
        void onListItemClick(int itemIndex);
    }

    public MyOrderAdapter(int numberOfItems, ListItemClickListener listItemClickListener, List<MainTable> tables) {
        mNumberItems = numberOfItems;
        viewHolderCount = 0;
        mOnClickListener = listItemClickListener;
        this.mainTalbelist = tables;
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.my_every_order;
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
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.finish)
        Button finish;
        @BindView(R.id.modify)
        Button modify;
        @BindView(R.id.reset)
        Button reset;

        public NumberViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            MainTable table = mainTalbelist.get(listIndex);
            Log.d(TAG, MyGson.toJson(table));
            time.setText(table.getPub_time());
            content.setText(table.getContent());
            finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = table.getId();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(ConConfig.url)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .build();
                    RequestService requestService = retrofit.create(RequestService.class);
                    Call<Void> call = requestService.finishOrder(id);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {

                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                        }
                    });
                }
            });
            modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 打开新的activity
                }
            });
            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 删除订单
                }
            });
        }

        @Override
        public void onClick(View view) {
            int clickPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickPosition);
        }
    }

    static class ViewHolder {

    }
}
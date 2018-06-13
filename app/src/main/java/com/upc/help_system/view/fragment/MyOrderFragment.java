package com.upc.help_system.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.upc.help_system.MyApplication;
import com.upc.help_system.R;
import com.upc.help_system.model.User;
import com.upc.help_system.presenter.adapter.OrderAdapter;
import com.upc.help_system.utils.SharedPreferenceUtil;
import com.upc.help_system.utils.network.ConConfig;
import com.upc.help_system.utils.network.RequestService;
import com.upc.help_system.utils.widgetutil.SnackbarUtil;
import com.upc.help_system.view.activity.DetailActivity;
import com.upc.help_system.view.activity.MainActivity;

import org.json.JSONArray;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
public class MyOrderFragment extends Fragment {
    private final String TAG = "OrdersFragment";
    private ViewHolder holder;
    private int flag;
    private JsonArray datas;
    public MyOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrdersFragment newInstance(String param1, String param2) {
        OrdersFragment fragment = new OrdersFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: before inflate view");
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        holder = new ViewHolder(view);

        Log.d(TAG, "onCreateView: end of refresh");
        holder.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        Log.d(TAG, "onCreateView: end of setOnRefreshListener");
        holder.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Gson gson = new Gson();
                JsonArray searchArray = new JsonArray();
                for(JsonElement object:datas){
                    JsonObject temp = object.getAsJsonObject();
                    JsonObject fields = temp.get("fields").getAsJsonObject();
                    for (Map.Entry<String, JsonElement> s:fields.entrySet()){
                        if(s.getKey().equals("trade_type")||s.getKey().equals("publisher")||s.getKey().equals("type")||s.getKey().equals("accepter")){
                            continue;
                        }
                        if(s.getValue()!=null&&s.getValue().toString().contains(query)){
                            searchArray.add(temp);
                            break;
                        }
                    }
                }
                OrderAdapter.ListItemClickListener listener = new OrderAdapter.ListItemClickListener() {
                    @Override
                    public void onListItemClick(int itemIndex) {
                        JsonObject object = searchArray.get(itemIndex).getAsJsonObject();
                        String jsonobj = gson.toJson(object);
                        Intent i = new Intent(getActivity(), DetailActivity.class);
                        i.putExtra("object", jsonobj);
                        startActivity(i);
                    }
                };
                if (searchArray.size() > 0) {
                    holder.recycler.setAdapter(new OrderAdapter(searchArray.size(), listener, searchArray));
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        Log.d(TAG, "onCreateView: end of setOnQueryTextListener");
        refresh();
        return view;
    }


    void refresh() {
        Log.d(TAG, "refresh: in refresh");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConConfig.url)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        RequestService requestService = retrofit.create(RequestService.class);
        JsonObject obj = new JsonObject();
        obj.addProperty("id", SharedPreferenceUtil.getInt("user","id"));
        Call<JsonArray> call = requestService.getMyGoods(obj);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                Log.d(TAG, "onResponse: success"+response.body());

                Gson gson = new Gson();
                JsonArray jsonArray = response.body();
                datas = jsonArray;
                OrderAdapter.ListItemClickListener listener = new OrderAdapter.ListItemClickListener() {
                    @Override
                    public void onListItemClick(int itemIndex) {
                        JsonObject object = jsonArray.get(itemIndex).getAsJsonObject();
                        String jsonobj = gson.toJson(object);
                        Intent i = new Intent(getActivity(), DetailActivity.class);
                        i.putExtra("object", jsonobj);
                        startActivity(i);
                    }
                };
                if (jsonArray!=null&&jsonArray.size() > 0) {
                    holder.recycler.setAdapter(new OrderAdapter(jsonArray.size(), listener, jsonArray));
                }
                holder.swiperefresh.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d(TAG, "onFailure: error"+t.getMessage());
            }
        });

    }



    class ViewHolder {
        @BindView(R.id.searchView)
        SearchView searchView;
        @BindView(R.id.recycler)
        RecyclerView recycler;
        @BindView(R.id.swiperefresh)
        SwipeRefreshLayout swiperefresh;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(MyApplication.getContext());
            Log.d(TAG, "ViewHolder: before setLayoutManager");
            recycler.setLayoutManager(layoutManager);
        }
    }
}

package com.upc.help_system.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.upc.help_system.MyApplication;
import com.upc.help_system.R;
import com.upc.help_system.presenter.adapter.OrderAdapter;
import com.upc.help_system.utils.SharedPreferenceUtil;
import com.upc.help_system.utils.network.ConConfig;
import com.upc.help_system.utils.network.RequestService;
import com.upc.help_system.view.activity.DetailActivity;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AcceptOrderFragment extends Fragment {
    private final String TAG = "OrdersFragment";
    private ViewHolder holder;
    private int flag;

    private JsonArray datas;

    public AcceptOrderFragment() {
        // Required empty public constructor++--
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
        View view = inflater.inflate(R.layout.fragment_myorders, container, false);
        holder = new ViewHolder(view);

        Log.d(TAG, "onCreateView: end of refresh");
        holder.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        Log.d(TAG, "onCreateView: end of setOnRefreshListener");
//        holder.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Gson gson = new Gson();
//                JsonArray searchArray = new JsonArray();
//                for (JsonElement object : datas) {
//                    JsonObject temp = object.getAsJsonObject();
//                    JsonObject fields = temp.get("fields").getAsJsonObject();
//                    for (Map.Entry<String, JsonElement> s : fields.entrySet()) {
//                        if (s.getKey().equals("trade_type") || s.getKey().equals("publisher") || s.getKey().equals("type") || s.getKey().equals("accepter")) {
//                            continue;
//                        }
//                        if (s.getValue() != null && s.getValue().toString().contains(query)) {
//                            searchArray.add(temp);
//                            break;
//                        }
//                    }
//                }
//                OrderAdapter.ListItemClickListener listener = new OrderAdapter.ListItemClickListener() {
//                    @Override
//                    public void onListItemClick(int itemIndex) {
//                        JsonObject object = searchArray.get(itemIndex).getAsJsonObject();
//                        String jsonobj = gson.toJson(object);
//                        Intent i = new Intent(getActivity(), DetailActivity.class);
//                        i.putExtra("object", jsonobj);
//                        startActivity(i);
//                    }
//                };
//                if (searchArray.size() > 0) {
//                    holder.recycler.setAdapter(new OrderAdapter(searchArray.size(), listener, searchArray));
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });

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
        Call<JsonArray> call = requestService.getGoods();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                Log.d(TAG, "onResponse: success" + response.body());

                Gson gson = new Gson();
                datas = new JsonArray();
                JsonArray jsonArray = response.body();
                for (JsonElement object : jsonArray) {
                    JsonObject temp = object.getAsJsonObject();
                    JsonObject fields = temp.get("fields").getAsJsonObject();
                    if (fields.get("accepter").getAsInt() == SharedPreferenceUtil.getInt("user", "id")) {
                        datas.add(temp);
                    }
                }

                OrderAdapter.ListItemClickListener listener = new OrderAdapter.ListItemClickListener() {
                    @Override
                    public void onListItemClick(int itemIndex) {
                        JsonObject object = datas.get(itemIndex).getAsJsonObject();
                        String jsonobj = gson.toJson(object);
                        Intent i = new Intent(getActivity(), DetailActivity.class);
                        i.putExtra("object", jsonobj);
                        i.putExtra("myorder",1);
                        startActivity(i);
                    }
                };
                if (datas.size() > 0) {
                    holder.recycler.setAdapter(new OrderAdapter(datas.size(), listener, datas));
                }
                holder.swiperefresh.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d(TAG, "onFailure: error" + t.getMessage());
            }
        });

    }


//    static class ViewHolder {
//        @BindView(R.id.searchView)
//        SearchView searchView;
//        @BindView(R.id.recycler)
//        RecyclerView recycler;
//        @BindView(R.id.swiperefresh)
//        SwipeRefreshLayout swiperefresh;
//
//        ViewHolder(View view) {
//            ButterKnife.bind(this, view);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(MyApplication.getContext());
//            Log.d(TAG, "ViewHolder: before setLayoutManager");
//            recycler.setLayoutManager(layoutManager);
//        }
////    }

    class ViewHolder {
        @BindView(R.id.head)
        ImageView head;
        @BindView(R.id.constraintLayout)
        ConstraintLayout constraintLayout;
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

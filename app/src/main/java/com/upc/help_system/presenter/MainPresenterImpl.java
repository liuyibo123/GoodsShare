//package com.upc.help_system.presenter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.res.Resources;
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.SearchView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.upc.help_system.MyApplication;
//import com.upc.help_system.R;
//import com.upc.help_system.model.MainTable;
//import com.upc.help_system.presenter.adapter.MyOrderAdapter;
//import com.upc.help_system.presenter.adapter.OrderAdapter;
//import com.upc.help_system.utils.MyGson;
//import com.upc.help_system.utils.network.ConConfig;
//import com.upc.help_system.utils.network.RequestService;
//import com.upc.help_system.view.activity.DetailActivity;
//import com.upc.help_system.view.activity.MainActivity;
//
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
///**
// * Created by Administrator on 2017/5/15.
// */
//
//public class MainPresenterImpl implements MainPresenter {
//    private MainActivity view;
//    OrdersViewHolder ordersholder;
//    MyOrdersViewHolder myordersholder;
//    Retrofit retrofit;
//    RequestService requestService;
//    String username;
//    String sex;
//
//    public void clearString() {
//        username = null;
//    }
//    public MainPresenterImpl(MainActivity view) {
//        if (view != null) {
//            this.view = view;
//        }
//        retrofit = new Retrofit.Builder()
//                .baseUrl(ConConfig.url)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        requestService = retrofit.create(RequestService.class);
//        SharedPreferences sharedPreferences = view.getSharedPreferences("user", Context.MODE_PRIVATE);
//        username = sharedPreferences.getString("name", "");
//        sex = sharedPreferences.getString("sex", "");
//    }
//
//    public void showMyOrders() {
//        Log.d("MainPresenterImpl", "showorders");
//        myordersholder = new MyOrdersViewHolder();
//        View myview = myordersholder.myView;
//        view.frameLayout.removeAllViews();
//        view.frameLayout.addView(myview);
//        if (sex.equals("女")) {
//            Resources resources = MyApplication.getContext().getResources();
//            Drawable drawable = resources.getDrawable(R.drawable.ic_girl_48);
//            myordersholder.img.setImageDrawable(drawable);
//        } else {
//            Resources resources = MyApplication.getContext().getResources();
//            Drawable drawable = resources.getDrawable(R.drawable.ic_boy_48);
//            myordersholder.img.setImageDrawable(drawable);
//        }
//        myorderrefresh();
//        myordersholder.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                myorderrefresh();
//            }
//        });
//
//
//    }
//
//    private void myorderrefresh() {
//        Call<List<MainTable>> call = requestService.getOrdersByPerson(username);
//        call.enqueue(new Callback<List<MainTable>>() {
//            @Override
//            public void onResponse(Call<List<MainTable>> call, Response<List<MainTable>> response) {
//                Log.d("MainPresenterImpl", "response.body():" + MyGson.toJson(response.body()));
//
//                MyOrderAdapter.ListItemClickListener listener = new MyOrderAdapter.ListItemClickListener() {
//                    @Override
//                    public void onListItemClick(int itemIndex) {
//                        MainTable table = response.body().get(itemIndex);
//                        ItemClick(table);
//                    }
//                };
//                if (response.body().size() != 0) {
//                    myordersholder.recyclerView.setAdapter(new MyOrderAdapter(response.body().size(), listener, response.body()));
//                }
//                myordersholder.swiperefresh.setRefreshing(false);
//            }
//
//            @Override
//            public void onFailure(Call<List<MainTable>> call, Throwable t) {
//                Toast.makeText(view, "错误：" + t, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void showOrders() {
//        Log.d("MainPresenterImpl", "showorders");
//        ordersholder = new OrdersViewHolder();
//        View myview = ordersholder.myView;
//        view.frameLayout.removeAllViews();
//        view.frameLayout.addView(myview);
//        refresh();
//        ordersholder.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refresh();
//            }
//        });
//        ordersholder.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Call<List<MainTable>> call = requestService.getOrdersByContent(query);
//                call.enqueue(new Callback<List<MainTable>>() {
//                    @Override
//                    public void onResponse(Call<List<MainTable>> call, Response<List<MainTable>> response) {
//                        ordersholder.recyclerView.setAdapter(new OrderAdapter(response.body().size(), new OrderAdapter.ListItemClickListener() {
//                            @Override
//                            public void onListItemClick(int itemIndex) {
//                                MainTable table = response.body().get(itemIndex);
//                                ItemClick(table);
//                            }
//                        }, response.body()));
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<MainTable>> call, Throwable t) {
//
//                    }
//                });
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//
//
//    }
//
//    private void ItemClick(MainTable table) {
//        int id = table.getId();
//        int cata = table.getCatagory();
//        switch (cata) {
//            case 1:
//                Call<Express> call_express = requestService.getExpressById(id);
//                call_express.enqueue(new Callback<Express>() {
//                    @Override
//                    public void onResponse(Call<Express> call, Response<Express> response) {
//                        Express express = response.body();
//                        Intent intent = new Intent(view, DetailActivity.class);
//                        String pub_pserson = table.getPub_person();
//                        String phone_number = express.getPhone_number();
//                        float tip = table.getTip();
//                        String pub_loc = table.getPub_loc();
//                        String help_loc = table.getHelp_loc();
//                        String content = table.getContent();
//                        String accept_person = table.getAccept_person();
//                        intent.putExtra("id", table.getId());
//                        intent.putExtra("pubperson", pub_pserson);
//                        intent.putExtra("phonenumber", phone_number);
//                        intent.putExtra("tip", tip);
//                        intent.putExtra("content", content);
//                        intent.putExtra("acceptperson", accept_person);
//                        view.startActivity(intent);
//                    }
//
//                    @Override
//                    public void onFailure(Call<Express> call, Throwable t) {
//
//                    }
//                });
//                break;
//            case 2:
//                Call<Food> call_food = requestService.getFoodById(id);
//                call_food.enqueue(new Callback<Food>() {
//                    @Override
//                    public void onResponse(Call<Food> call, Response<Food> response) {
//                        Food express = response.body();
//                        Intent intent = new Intent(view, DetailActivity.class);
//                        String pub_pserson = table.getPub_person();
//                        float tip = table.getTip();
//                        String pub_loc = table.getPub_loc();
//                        String help_loc = table.getHelp_loc();
//                        String content = table.getContent();
//                        String accept_person = table.getAccept_person();
//                        String phone = express.getPhone();
//                        intent.putExtra("id", table.getId());
//                        intent.putExtra("pubperson", pub_pserson);
//                        intent.putExtra("tip", tip);
//                        intent.putExtra("content", content);
//                        intent.putExtra("acceptperson", accept_person);
//                        intent.putExtra("phonenumber", phone);
//                        view.startActivity(intent);
//                    }
//
//                    @Override
//                    public void onFailure(Call<Food> call, Throwable t) {
//                        Toast.makeText(view, "网络连接失败", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                break;
//            case 3:
//                Call<Homework> call_work = requestService.getHomeWorkById(id);
//                call_work.enqueue(new Callback<Homework>() {
//                    @Override
//                    public void onResponse(Call<Homework> call, Response<Homework> response) {
//                        Homework express = response.body();
//                        Intent intent = new Intent(view, DetailActivity.class);
//                        String pub_pserson = table.getPub_person();
//                        float tip = table.getTip();
//                        String pub_loc = table.getPub_loc();
//                        String help_loc = table.getHelp_loc();
//                        String content = table.getContent();
//                        String accept_person = table.getAccept_person();
//                        String phone = express.getPhone();
//                        intent.putExtra("id", table.getId());
//                        intent.putExtra("pubperson", pub_pserson);
//                        intent.putExtra("tip", tip);
//                        intent.putExtra("content", content);
//                        intent.putExtra("acceptperson", accept_person);
//                        intent.putExtra("phonenumber", phone);
//                        view.startActivity(intent);
//                    }
//
//                    @Override
//                    public void onFailure(Call<Homework> call, Throwable t) {
//                        Toast.makeText(view, "网络连接失败", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                break;
//            case 4:
//                Call<Buy> call_buy = requestService.getBuyById(id);
//                call_buy.enqueue(new Callback<Buy>() {
//                    @Override
//                    public void onResponse(Call<Buy> call, Response<Buy> response) {
//                        Buy express = response.body();
//                        Intent intent = new Intent(view, DetailActivity.class);
//                        String pub_pserson = table.getPub_person();
//                        float tip = table.getTip();
//                        String pub_loc = table.getPub_loc();
//                        String help_loc = table.getHelp_loc();
//                        String content = table.getContent();
//                        String accept_person = table.getAccept_person();
//                        String phone = express.getPhone();
//                        intent.putExtra("id", table.getId());
//                        intent.putExtra("pubperson", pub_pserson);
//                        intent.putExtra("tip", tip);
//                        intent.putExtra("content", content);
//                        intent.putExtra("acceptperson", accept_person);
//                        intent.putExtra("phonenumber", phone);
//                        view.startActivity(intent);
//                    }
//
//                    @Override
//                    public void onFailure(Call<Buy> call, Throwable t) {
//                        Toast.makeText(view, "网络连接失败", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                break;
//            case 5:
//                Call<Others> call_other = requestService.getOthersById(id);
//                call_other.enqueue(new Callback<Others>() {
//                    @Override
//                    public void onResponse(Call<Others> call, Response<Others> response) {
//                        Others express = response.body();
//                        Intent intent = new Intent(view, DetailActivity.class);
//                        String pub_pserson = table.getPub_person();
//                        float tip = table.getTip();
//                        String pub_loc = table.getPub_loc();
//                        String help_loc = table.getHelp_loc();
//                        String content = table.getContent();
//                        String accept_person = table.getAccept_person();
//                        String phone = express.getPhone();
//                        intent.putExtra("id", table.getId());
//                        intent.putExtra("pubperson", pub_pserson);
//                        intent.putExtra("tip", tip);
//                        intent.putExtra("content", content);
//                        intent.putExtra("acceptperson", accept_person);
//                        intent.putExtra("phonenumber", phone);
//                        view.startActivity(intent);
//                    }
//
//                    @Override
//                    public void onFailure(Call<Others> call, Throwable t) {
//                        Toast.makeText(view, "网络连接失败", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                break;
//        }
//
//    }
//
//    @Override
//    public void onNewOrder() {
//
//    }
//
//    void refresh() {
////
////        Call<List<MainTable>> call = requestService.getOrders();
////        call.enqueue(new Callback<List<MainTable>>() {
////            @Override
////            public void onResponse(Call<List<MainTable>> call, Response<List<MainTable>> response) {
////                Log.d("MainPresenterImpl", "response.body():" + MyGson.toJson(response.body()));
////
////                OrderAdapter.ListItemClickListener listener = new OrderAdapter.ListItemClickListener() {
////                    @Override
////                    public void onListItemClick(int itemIndex) {
////                        MainTable table = response.body().get(itemIndex);
////                        ItemClick(table);
////                    }
////                };
////                if (response.body().size() != 0) {
////                    ordersholder.recyclerView.setAdapter(new OrderAdapter(response.body().size(), listener, response.body()));
////                }
////                ordersholder.swiperefresh.setRefreshing(false);
////            }
////            @Override
////            public void onFailure(Call<List<MainTable>> call, Throwable t) {
////                Toast.makeText(view, "错误：" + t, Toast.LENGTH_SHORT).show();
////            }
////        });
////    }
//    class OrdersViewHolder {
//        View myView;
//        RecyclerView recyclerView;
//        SearchView searchView;
//        SwipeRefreshLayout swiperefresh;
//
//        public OrdersViewHolder() {
//            myView = view.getLayoutInflater().inflate(R.layout.fragment_orders, null);
//            recyclerView = (RecyclerView) myView.findViewById(R.id.recycler);
//            searchView = (SearchView) myView.findViewById(R.id.searchView);
//            swiperefresh = (SwipeRefreshLayout) myView.findViewById(R.id.swiperefresh);
//            int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
//            TextView textView = (TextView) searchView.findViewById(id);
//            textView.setTextColor(Color.BLACK);
//            textView.setHintTextColor(Color.parseColor("#CCCCCC"));
//            LinearLayoutManager layoutManager = new LinearLayoutManager(view);
//            recyclerView.setLayoutManager(layoutManager);
//        }
//    }
//
//    class MyOrdersViewHolder {
//        View myView;
//        RecyclerView recyclerView;
//        SwipeRefreshLayout swiperefresh;
//        ImageView img;
//
//        public MyOrdersViewHolder() {
//            myView = view.getLayoutInflater().inflate(R.layout.fragment_myorders, null);
//            recyclerView = (RecyclerView) myView.findViewById(R.id.recycler);
//            swiperefresh = (SwipeRefreshLayout) myView.findViewById(R.id.swiperefresh);
//            img = (ImageView) myView.findViewById(R.id.head);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(view);
//            recyclerView.setLayoutManager(layoutManager);
//        }
//    }
//}

package com.upc.help_system.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.upc.help_system.R;
import com.upc.help_system.model.User;
import com.upc.help_system.utils.network.ConConfig;
import com.upc.help_system.utils.network.RequestService;
import com.upc.help_system.utils.widgetutil.SnackbarUtil;
import com.upc.help_system.view.activity.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrdersFragment extends Fragment {
    private Activity activity = getActivity();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ViewHolder holder;
    private OnFragmentInteractionListener mListener;

    public OrdersFragment() {
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
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, true);
        holder = new ViewHolder(view);
        refresh();
        holder.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        holder.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
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
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    void refresh() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConConfig.url)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        RequestService requestService = retrofit.create(RequestService.class);
        Call<String> call = requestService.getGoods();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Gson gson = new Gson();
                String json = response.body();
                JsonArray jsonArray = gson.fromJson(json,JsonArray.class);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
//        Call<List<MainTable>> call = requestService.getOrders();
//        call.enqueue(new Callback<List<MainTable>>() {
//            @Override
//            public void onResponse(Call<List<MainTable>> call, Response<List<MainTable>> response) {
//                Log.d("MainPresenterImpl", "response.body():" + MyGson.toJson(response.body()));
//
//                OrderAdapter.ListItemClickListener listener = new OrderAdapter.ListItemClickListener() {
//                    @Override
//                    public void onListItemClick(int itemIndex) {
//                        MainTable table = response.body().get(itemIndex);
//                        ItemClick(table);
//                    }
//                };
//                if (response.body().size() != 0) {
//                    ordersholder.recyclerView.setAdapter(new OrderAdapter(response.body().size(), listener, response.body()));
//                }
//                ordersholder.swiperefresh.setRefreshing(false);
//            }
//            @Override
//            public void onFailure(Call<List<MainTable>> call, Throwable t) {
//                Toast.makeText(view, "错误：" + t, Toast.LENGTH_SHORT).show();
//            }
//        });
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
            LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
            recycler.setLayoutManager(layoutManager);
        }
    }
}

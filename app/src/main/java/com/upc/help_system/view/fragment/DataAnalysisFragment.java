package com.upc.help_system.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.upc.help_system.R;
import com.upc.help_system.utils.network.ConConfig;
import com.upc.help_system.utils.network.RequestService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the


 * create an instance of this fragment.
 */
public class DataAnalysisFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    @BindView(R.id.piechart)
    PieChart piechart;
    @BindView(R.id.barchart)
    BarChart barchart;
    Unbinder unbinder;
    @BindView(R.id.button)
    Button button;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public DataAnalysisFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dataanalysis, container, false);
        ButterKnife.bind(this, v);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(ConConfig.url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RequestService requestService = retrofit.create(RequestService.class);
                Call<JsonObject> call = requestService.analysis();
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject data = response.body();
                        JsonArray goodstype = data.get("goods_type").getAsJsonArray();
                        JsonArray type = data.get("type").getAsJsonArray();
                        setPieChart(type);
                        setBarChart(goodstype);
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
            }
        });

        return v;
    }
    private  class MyXFormatter  implements IAxisValueFormatter {

        private String[] mValues;

        public MyXFormatter(String[] values) {
            this.mValues = values;
        }
        private static final String TAG = "MyXFormatter";

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            Log.d(TAG, "----->getFormattedValue: "+value);
            return mValues[(int) value % mValues.length];
        }
    }
    private void setBarChart(JsonArray goodstype) {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < goodstype.size(); i++) {
            int val = goodstype.get(i).getAsInt();
            yVals1.add(new BarEntry(i+1, (float) val));
        }
        BarDataSet set1;
        set1 = new BarDataSet(yVals1, "物品类型");
        set1.setColors(ColorTemplate.MATERIAL_COLORS);
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setBarWidth(0.9f);
        barchart.setData(data);
        String[] values = new String[]{
                "食物", "日用品", "学习用品", "电子产品", "体育器材", "其他"
        };

        barchart.getXAxis().setValueFormatter(new MyXFormatter(values));
        piechart.invalidate();
    }

    private void setPieChart(JsonArray type) {
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        int type1 = type.get(0).getAsInt();
        int type2 = type.get(1).getAsInt();
        entries.add(new PieEntry(((float)type1/(type1+type2))*100,"租"));
        entries.add(new PieEntry(((float)type2/(type1+type2))*100,"买"));
        PieDataSet dataSet = new PieDataSet(entries, "交易类型");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        piechart.setData(data);

        // undo all highlights
        piechart.highlightValues(null);

        piechart.invalidate();
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

}

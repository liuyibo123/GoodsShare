//package com.upc.help_system.presenter;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.ClipboardManager;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.SharedPreferences;
//import android.content.res.Resources;
//import android.support.design.widget.Snackbar;
//import android.util.Log;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import com.google.gson.Gson;
//import com.upc.help_system.BuildConfig;
//import com.upc.help_system.R;
//import com.upc.help_system.model.MainTable;
//import com.upc.help_system.utils.Container;
//import com.upc.help_system.utils.MyGson;
//import com.upc.help_system.utils.MyLoction;
//import com.upc.help_system.utils.TimeUtil;
//import com.upc.help_system.utils.network.ConConfig;
//import com.upc.help_system.utils.network.MyResponse;
//import com.upc.help_system.utils.network.RequestService;
//import com.upc.help_system.utils.widgetutil.SnackbarUtil;
//import com.upc.help_system.view.activity.PubActivity;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
///**
// * Created by Administrator on 2017/4/26.
// */
//
//public class PubPresenterImpl implements PubPresenter {
//    PubActivity view;
//    String username;
//    String phone;
//    public PubPresenterImpl(PubActivity view) {
//        this.view = view;
//        SharedPreferences sharedPreferences = view.getSharedPreferences("user", Context.MODE_PRIVATE);
//        username = sharedPreferences.getString("name", "");
//        phone = sharedPreferences.getString("phone", "");
//        showExpress();
//    }
//
//    public void showTakeFood() {
//        View v = view.getLayoutInflater().inflate(R.layout.fragment_food, null);
//        view.framePub.removeAllViews();
//        view.framePub.addView(v);
//        EditText canteen = (EditText) v.findViewById(R.id.canteen_content);
//        EditText food = (EditText) v.findViewById(R.id.food_content);
//        EditText dealine = (EditText) v.findViewById(R.id.deadline_content);
//        EditText deatination = (EditText) v.findViewById(R.id.food_destination_content);
//        EditText charge = (EditText) v.findViewById(R.id.food_charge_content);
//        EditText phone_number = (EditText) v.findViewById(R.id.phone_number_content);
//        Button makesure = (Button) v.findViewById(R.id.btn_makesure);
//        Button cancel = (Button) v.findViewById(R.id.btn_cancel);
//        phone_number.setText(phone);
//
//        makesure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Gson gson = new Gson();
//                MainTable table = new MainTable();
//                table.setContent("帮带饭，餐厅:" + canteen.getText().toString());
//                table.setTip(Float.parseFloat(charge.getText().toString()));
//                table.setState(1);
//                table.setCatagory(Container.TAKEFOOD);
//                table.setHelp_loc(deatination.getText().toString());
//                table.setPub_person(username);
//                table.setPub_time(TimeUtil.getCurrentTime());
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl(ConConfig.url)
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//                RequestService service = retrofit.create(RequestService.class);
//                Call<MyResponse> call = service.addMainTable(table);
//                Call<Void> call2 = service.addFood(food_temp);
//                Log.d("PubPresenterImpl", "table:" + gson.toJson(table));
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Response<MyResponse> response = null;
//                        try {
//                            response = call.execute();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        food_temp.setId(response.body().getValue());
//                        food_temp.setCanteen(canteen.getText().toString());
//                        food_temp.setContent(food.getText().toString());
//                        food_temp.setTime(dealine.getText().toString());
//                        food_temp.setPhone(phone_number.getText().toString());
//                        Log.d("PubPresenterImpl", MyGson.toJson(food_temp));
//                        call2.enqueue(new Callback<Void>() {
//                            @Override
//                            public void onResponse(Call<Void> call, Response<Void> response) {
//                                SnackbarUtil.LongSnackbar(view.getWindow().getDecorView(), "发布成功", SnackbarUtil.Confirm).setAction("确定", new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        view.finish();
//                                    }
//                                }).show();
//                            }
//
//                            @Override
//                            public void onFailure(Call<Void> call, Throwable t) {
//                                SnackbarUtil.LongSnackbar(view.getWindow().getDecorView(), "网络原因发布失败", SnackbarUtil.Alert).setAction("确定", new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        view.finish();
//                                    }
//                                }).show();
//                            }
//                        });
//                    }
//                }).start();
//            }
//        });
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                view.finish();
//            }
//        });
//
//    }
//
//    public void showHomeWork() {
//        //TODO 写作业帮的内容
//        View v = view.getLayoutInflater().inflate(R.layout.fragment_work, null);
//        view.framePub.removeAllViews();
//        view.framePub.addView(v);
//        EditText subject = (EditText) v.findViewById(R.id.subject_content);
//        EditText question = (EditText) v.findViewById(R.id.question_content);
//        EditText charge = (EditText) v.findViewById(R.id.work_charge_content);
//        EditText phone_number = (EditText) v.findViewById(R.id.phone_number_content);
//        phone_number.setText(phone);
//        Button makesure = (Button) v.findViewById(R.id.btn_makesure);
//        Button cancel = (Button) v.findViewById(R.id.btn_cancel);
//
//        makesure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Gson gson = new Gson();
//                MainTable mainTable = new MainTable();
//                mainTable.setContent("作业求助：" + subject.getText().toString() + "\n" + question.getText().toString());
//                mainTable.setTip(Float.parseFloat(charge.getText().toString()));
//                mainTable.setState(1);
//                mainTable.setCatagory(Container.HOMEWORK);
//                mainTable.setPub_person(username);
//                mainTable.setPub_time(TimeUtil.getCurrentTime());
//                mainTable.setPub_loc("");
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl(ConConfig.url)
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//                RequestService requestService = retrofit.create(RequestService.class);
//                Call<MyResponse> call = requestService.addMainTable(mainTable);
//
//                Log.d("PubPresenterImpl", "table:" + gson.toJson(mainTable));
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Response<MyResponse> response = null;
//                        try {
//                            response = call.execute();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        homework_temp.setId(response.body().getValue());
//                        homework_temp.setSubject(subject.getText().toString());
//                        homework_temp.setTip(Float.parseFloat(charge.getText().toString()));
//                        homework_temp.setQuestion(question.getText().toString());
//                        homework_temp.setPhone(phone_number.getText().toString());
//                        Call<Void> call2 = requestService.addHomework(homework_temp);
//                        Log.d("PubPresenterImpl", MyGson.toJson(homework_temp));
//                        call2.enqueue(new Callback<Void>() {
//                            @Override
//                            public void onResponse(Call<Void> call, Response<Void> response) {
//                                if (BuildConfig.DEBUG) Log.d("PubPresenterImpl", "success");
//                                Snackbar.make(view.getWindow().getDecorView(), "发布成功", Snackbar.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onFailure(Call<Void> call, Throwable t) {
//                                if (BuildConfig.DEBUG) Log.d("PubPresenterImpl", "t:" + t);
//                                Snackbar.make(view.getWindow().getDecorView(), "连接不上服务器", Snackbar.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                }).start();
//            }
//        });
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                view.finish();
//            }
//        });
//    }
//
//    public void showBuy() {
//        View v = view.getLayoutInflater().inflate(R.layout.fragment_buy, null);//fragment_buy
//        view.framePub.removeAllViews();
//        view.framePub.addView(v);
//        Button button = (Button) v.findViewById(R.id.btn_makesure);
//        EditText buy_pot = (EditText) v.findViewById(R.id.buy_pot);//代购点——buy_pot
//        EditText buy_thing = (EditText) v.findViewById(R.id.buy_thing);//商品
//        final EditText phone_number = (EditText) v.findViewById(R.id.phone_number_content);
//        phone_number.setText(phone);
//        EditText Price = (EditText) v.findViewById(R.id.price);//商品价格
//        EditText Pay_for = (EditText) v.findViewById(R.id.pay_for);//支付方式
//        EditText DeadLine = (EditText) v.findViewById(R.id.deadline_content);//截至时间
//        EditText Fee = (EditText) v.findViewById(R.id.fee);//酬金
//        Button btn_cancel = (Button) v.findViewById(R.id.btn_cancel);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Gson gson = new Gson();
//                MainTable table = new MainTable();
//                table.setContent("帮购物：" + buy_pot.getText().toString());//显示帮买
//                //TODO （1）百度地图自动获取位置
////                String loction = view.getSharedPreferences("location", Context.MODE_PRIVATE).getString("location", "获取位置失败");
////                MyLoction myLoction = MyGson.fromJson(loction, MyLoction.class);
////                table.setPub_loc(myLoction.getAddr());
//                //TODO　(2) 用户类
//                table.setPub_person(username);
//                table.setPub_time(TimeUtil.getCurrentTime());
//                table.setCatagory(Container.BUY);
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl(ConConfig.url)
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//                RequestService service = retrofit.create(RequestService.class);
//                Call<MyResponse> call = service.addMainTable(table);
//                final Call<Void> call2 = service.addBuy(buy_temp);//添加项目——buy_temp
//                Log.d("PubPresenterImpl", "table:" + gson.toJson(table));
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Response<MyResponse> response = null;
//                        try {
//                            response = call.execute();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        buy_temp.setId(response.body().getValue());
//                        buy_temp.setPrice(1);
//                        buy_temp.setGoods(buy_thing.getText().toString());
//                        buy_temp.setShop(buy_pot.getText().toString());
//                        buy_temp.setMoney_method(1);
//                        buy_temp.setPhone(phone_number.getText().toString());
//                        buy_temp.setTime(DeadLine.getText().toString());
//                        call2.enqueue(new Callback<Void>() {
//                            @Override
//                            public void onResponse(Call<Void> call, Response<Void> response) {
//                                if (BuildConfig.DEBUG) Log.d("PubPresenterImpl", "success");
//                                Snackbar.make(view.getWindow().getDecorView(), "发布成功", Snackbar.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onFailure(Call<Void> call, Throwable t) {
//                                if (BuildConfig.DEBUG) Log.d("PubPresenterImpl", "t:" + t);
//                                Toast.makeText(view, "连接不到服务器", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                }).start();
//            }
//        });
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                view.finish();
//            }
//        });
//    }
//
//
//    public void showOther() {
//        //TODO 写其他的东西
//        View v = view.getLayoutInflater().inflate(R.layout.fragment_others, null);//fragment_other
//        view.framePub.removeAllViews();
//        view.framePub.addView(v);
//        Button button = (Button) view.findViewById(R.id.btn_makesure);
//        EditText other_content = (EditText) v.findViewById(R.id.other_content);//求助信息内容
//        EditText phone_number = (EditText) v.findViewById(R.id.phone_number_content);
//        phone_number.setText(phone);
//        EditText Fee = (EditText) v.findViewById(R.id.fee);//酬金
//        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Gson gson = new Gson();
//                MainTable table = new MainTable();
//                table.setContent("帮做其他事：" + other_content.getText().toString());//显示信息
//
//                //TODO （1）百度地图自动获取位置
////                String loction = view.getSharedPreferences("location", Context.MODE_PRIVATE).getString("location", "获取位置失败");
////                MyLoction myLoction = MyGson.fromJson(loction, MyLoction.class);
////                table.setPub_loc(myLoction.getAddr());
//                //TODO　(2) 用户类
//                table.setPub_person(username);
//                table.setCatagory(Container.OTHER);
//                table.setPub_time(TimeUtil.getCurrentTime());
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl(ConConfig.url)
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//                RequestService service = retrofit.create(RequestService.class);
//                Call<MyResponse> call = service.addMainTable(table);
//                //other_temp
//                Log.d("PubPresenterImpl", "table:" + gson.toJson(table));
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Response<MyResponse> response = null;
//                        try {
//                            response = call.execute();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        others_temp.setId(response.body().getValue());
//                        others_temp.setContent(other_content.getText().toString());
//                        others_temp.setPhone(phone_number.getText().toString());
//                        Call<Void> call2 = service.addOthers(others_temp);
//                        call2.enqueue(new Callback<Void>() {
//                            @Override
//                            public void onResponse(Call<Void> call, Response<Void> response) {
//                                if (BuildConfig.DEBUG) Log.d("PubPresenterImpl", "success");
//                                Snackbar.make(view.getWindow().getDecorView(), "发布成功", Snackbar.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onFailure(Call<Void> call, Throwable t) {
//                                if (BuildConfig.DEBUG) Log.d("PubPresenterImpl", "t:" + t);
//                                Toast.makeText(view, "连接不到服务器", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                }).start();
//            }
//        });
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                view.finish();
//            }
//        });
//    }
//
//    public void showExpress() {
//        List<String> companys = new ArrayList<String>(Arrays.asList("EMS", "申通", "中通", "圆通", "汇通", "韵达", "顺丰"));
//        List<String> positon = new ArrayList<String>(Arrays.asList("14号楼", "研二", "邮局", "麦趣尔", "北门"));
//        String msg = "";
//        ClipboardManager clipboardManager = (ClipboardManager) view.getSystemService(Context.CLIPBOARD_SERVICE);
//        View v = view.getLayoutInflater().inflate(R.layout.fragment_express, null);
//        view.framePub.removeAllViews();
//        view.framePub.addView(v);
//        Button button = (Button) view.findViewById(R.id.btn_makesure);
//        AutoCompleteTextView express_company = (AutoCompleteTextView) view.findViewById(R.id.express_company);
//        Resources resources = view.getResources();
//        String express[] = resources.getStringArray(R.array.express_company);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view, android.R.layout.simple_dropdown_item_1line, express);
//        express_company.setAdapter(adapter);
//        EditText take_number = (EditText) view.findViewById(R.id.take_number);
//        EditText name = (EditText) view.findViewById(R.id.name);
//        name.setText(username);
//        EditText phone_number = (EditText) view.findViewById(R.id.usr_name);
//        phone_number.setText(phone);
//        EditText destination_to = (EditText) view.findViewById(R.id.destination_to);
//        EditText tip = (EditText) view.findViewById(R.id.tip);
//        Spinner volume = (Spinner) view.findViewById(R.id.volume);
//        Spinner weight = (Spinner) view.findViewById(R.id.weight);
//        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
//        if (clipboardManager.hasPrimaryClip()) {
//            msg = clipboardManager.getPrimaryClip().getItemAt(0).getText().toString();  // 获取内容
//            String finalMsg = msg;
//            String finalMsg1 = msg;
//            Dialog dialog = new AlertDialog.Builder(view)
//                    .setTitle("检测到")
//                    .setMessage("是否使用剪贴板内容自动填充？")
//                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    })
//                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            for (String company : companys) {
//                                if (stringContains(finalMsg, company)) {
//                                    express_company.setText(company);
//                                }
//                            }
//                            for (String location : positon) {
//                                if (stringContains(finalMsg, location)) {
//                                    express_company.append(location);
//                                }
//                            }
//                            String tokennumber = getTaken(finalMsg1);
//                            take_number.setText(tokennumber);
//                        }
//                    })
//                    .create();
//            dialog.show();
//        }
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Gson gson = new Gson();
//                MainTable table = new MainTable();
//                table.setContent("物品名称:" + express_company.getText().toString() +
//                        "重量：" + Container.weight.values()[weight.getSelectedItemPosition()]);
//                table.setTip(Float.parseFloat(tip.getText().toString()));
//                table.setState(1);
//                table.setCatagory(volume.getSelectedItemPosition());
//                table.setHelp_loc(destination_to.getText().toString());
//                //TODO （1）百度地图自动获取位置
//                String loction = view.getSharedPreferences("location", Context.MODE_PRIVATE).getString("location", "获取位置失败");
//                MyLoction myLoction = MyGson.fromJson(loction, MyLoction.class);
//                table.setPub_loc(myLoction.getAddr());
//                //TODO　(2) 用户类
//                table.setPub_person(username);
//                table.setPub_time(TimeUtil.getCurrentTime());
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl(ConConfig.url)
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//                RequestService service = retrofit.create(RequestService.class);
//                Call<MyResponse> call = service.addMainTable(table);
//
//                Log.d("PubPresenterImpl", "table:" + gson.toJson(table));
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Response<MyResponse> response = null;
//                        try {
//                            response = call.execute();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        express_temp.setId(response.body().getValue());
//                        express_temp.setCompany(express_company.getText().toString());
//                        express_temp.setName(name.getText().toString());
//                        express_temp.setPhone_number(phone_number.getText().toString());
//                        express_temp.setTake_number(take_number.getText().toString());
//                        express_temp.setVolume(volume.getSelectedItemPosition());
//                        express_temp.setWeight(weight.getSelectedItemPosition());
//                        Call<Void> call2 = service.addExpress(express_temp);
//                        call2.enqueue(new Callback<Void>() {
//                            @Override
//                            public void onResponse(Call<Void> call, Response<Void> response) {
//                                if (BuildConfig.DEBUG) Log.d("PubPresenterImpl", "success");
//                                Snackbar.make(view.getWindow().getDecorView(), "发布成功", Snackbar.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onFailure(Call<Void> call, Throwable t) {
//                                if (BuildConfig.DEBUG) Log.d("PubPresenterImpl", "t:" + t);
//                                Toast.makeText(view, "连接不到服务器", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                }).start();
//            }
//        });
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                view.finish();
//            }
//        });
//    }
//
//    boolean stringContains(String msg, String content) {
//        return msg.indexOf(content) != -1;
//    }
//
//    String getTaken(String msg) {
//        String regEx = "\\（(\\d+)";
//        Pattern pat = Pattern.compile(regEx);
//        Matcher mat = pat.matcher(msg);
//        if (mat.find()) {
//            System.out.println(mat.group(0));
//            System.out.println(mat.group(1));
//            return mat.group(1);
//        }
//        return "";
//    }
//
//    }

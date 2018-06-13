package com.upc.help_system.utils.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.upc.help_system.model.MainTable;
import com.upc.help_system.model.User;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/5/10.
 */

public interface RequestService {
    @POST("/maintable/add_new")
    Call<MyResponse> addMainTable(@Body MainTable table);
    @GET("/maintable/find_all")
    Call<List<MainTable>> getOrders();
    @GET("/maintable/findbycontent")
    Call<List<MainTable>> getOrdersByContent(@Query("query") String query);
    @POST("/regist/")
    Call<JsonObject> register(@Body User user);
    @POST("/login/")
    Call<String> login(@Body User user);
    @POST("/getuser/")
    Call<User> getUser(@Body User user);


    @POST("user/update")
    Call<String> updateUser(@Body User user);



    @GET("/maintable/findbycontentbyperson")
    Call<List<MainTable>> getOrdersByContentByPerson(@Query("content") String query);

    @GET("/maintable/findbyperson")
    Call<List<MainTable>> getOrdersByPerson(@Query("name") String name);

    @GET("maintable/finishorder")
    Call<Void> finishOrder(@Query("id") int id);

    @GET("getgoods/")
    Call<JsonArray> getGoods();
    @POST("getmygoods/")
    Call<JsonArray> getMyGoods(@Body JsonObject object);
    @POST("pub/")
    Call<String> pub(@Body JsonObject object);
    @POST("collect/")
    Call<String> collect(@Body JsonObject object);
    @GET("accept/")
    Call<String> acceptOrder(@Query("goodsId") int id, @Query("userId")  int userId);
    @POST("remark/")
    Call<String> remark(@Body JsonObject obj);
    @GET("analysis/")
    Call<JsonObject> analysis();
}

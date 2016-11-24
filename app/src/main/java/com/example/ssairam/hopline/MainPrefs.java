package com.example.ssairam.hopline;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ssairam.hopline.vo.CategoryVo;
import com.example.ssairam.hopline.vo.OrderVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by root on 18/11/16.
 */

public class MainPrefs {

    public static final String PREFS_NAME = "MyPrefsFile";

    private static SharedPreferences prefs = null;

    private static void initPrefInstance(Context context) {
        if (prefs == null) prefs = context.getSharedPreferences(PREFS_NAME, 0);
    }

    public static void saveMenuCategories(List<CategoryVo> menuCategories,Context context) {
        initPrefInstance(context);
        Gson gson = new Gson();
        String json = gson.toJson(menuCategories);
        prefs.edit().putString("menuCategories", json).commit();
    }

    public static List<CategoryVo> getMenuCategories(Context context) {
        initPrefInstance(context);

        String json = prefs.getString("menuCategories", "[]");
        Gson gson = new Gson();
        CategoryVo[] categoryVos = gson.fromJson(json, CategoryVo[].class);
        return Arrays.asList(categoryVos);
    }

    public static int getNewOfflineOrderNumber(Context context) {
        initPrefInstance(context);

        int num = prefs.getInt("offlineOrderNumber", 2000);
        prefs.edit().putInt("offlineOrderNumber", num+1).commit();
        return num;
    }

    public static void saveOfflineOrders(List<OrderVo> orderVoList, Context context) {
        initPrefInstance(context);
        Gson gson = new Gson();
        String json = gson.toJson(orderVoList);
        prefs.edit().putString("offlineOrderList", json).commit();
    }

    public static List<OrderVo> getOfflineOrders(Context context) {
        initPrefInstance(context);

        String json = prefs.getString("offlineOrderList", "[]");
        Gson gson = new Gson();
        Type listType = new TypeToken<List<OrderVo>>() {}.getType();
        List<OrderVo> orderVos = gson.fromJson(json, listType);
        return orderVos;
    }

    public static void saveOfflineOrderForServerLog(List<OrderVo> orderVoList, Context context) {
        initPrefInstance(context);
        Gson gson = new Gson();
        String json = gson.toJson(orderVoList);
        prefs.edit().putString("offlineOrdersForServerLog", json).commit();
    }

    public static List<OrderVo> getOfflineOrdersForServerLog(Context context) {
        initPrefInstance(context);

        String json = prefs.getString("offlineOrdersForServerLog", "[]");
        Gson gson = new Gson();
        Type listType = new TypeToken<List<OrderVo>>() {}.getType();
        List<OrderVo> orderVos = gson.fromJson(json, listType);
        return orderVos;
    }



}

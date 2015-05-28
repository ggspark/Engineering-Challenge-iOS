package play.gaurav.holmusk.controller;


import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import io.realm.RealmObject;
import play.gaurav.holmusk.models.FoodItem;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * @author Gaurav Gupta <gaurav@thegauravgupta.com>
 * @since 27/May/2015
 */
public class APIServices {


    private static final String API_URL = "http://test.holmusk.com/";

    static final Gson gson = new GsonBuilder()
            .setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    return f.getDeclaringClass().equals(RealmObject.class);
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            })
            .create();

    private static final RestAdapter REST_ADAPTER = new RestAdapter.Builder()
            .setEndpoint(API_URL)
            .setConverter(new GsonConverter(gson))
            .build();

    public interface FoodItemService {
        @GET("/food/search")
        void getFoodList(@Query("q") String name, Callback<List<FoodItem>> cb);
    }

    private static final FoodItemService FOOD_ITEM_SERVICE = REST_ADAPTER.create(FoodItemService.class);

    public static FoodItemService getFoodItemService() {
        return FOOD_ITEM_SERVICE;
    }
}


package play.gaurav.holmusk.controller;


import java.util.List;

import play.gaurav.holmusk.models.FoodItem;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * @author Gaurav Gupta <gaurav@thegauravgupta.com>
 * @since 27/May/2015
 */
public class APIServices {


    private static final String API_URL = "http://test.holmusk.com/";

    private static final RestAdapter REST_ADAPTER = new RestAdapter.Builder()
            .setEndpoint(API_URL)
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


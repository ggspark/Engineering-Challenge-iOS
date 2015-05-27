package play.gaurav.holmusk.controller;


import java.util.List;

import play.gaurav.holmusk.models.FoodItem;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * @author Gaurav Gupta <gaurav@thegauravgupta.com>
 * @since 27/May/2015
 */
public interface FoodItemService {
    @GET("/food/search")
    void getFoodList(@Query("q") String name, Callback<List<FoodItem>> cb);

}

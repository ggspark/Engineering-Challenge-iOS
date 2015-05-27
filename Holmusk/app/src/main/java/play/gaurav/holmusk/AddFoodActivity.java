package play.gaurav.holmusk;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import play.gaurav.holmusk.controller.APIServices;
import play.gaurav.holmusk.models.FoodItem;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class AddFoodActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        Callback<List<FoodItem>> callback = new Callback<List<FoodItem>>() {
            @Override
            public void success(List<FoodItem> foodItems, Response response) {
                Log.d("Response", response.toString());
                Log.d("Item", foodItems.toString());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Error", "Error in fetching API");
            }
        };
        APIServices.getFoodItemService().getFoodList("chicken", callback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package play.gaurav.holmusk.activites;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.util.List;

import io.realm.Realm;
import io.realm.exceptions.RealmException;
import play.gaurav.holmusk.R;
import play.gaurav.holmusk.adapters.CustomArrayAdapter;
import play.gaurav.holmusk.controller.APIServices;
import play.gaurav.holmusk.models.FoodItem;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class AddFoodActivity extends BaseActivity {

    AutoCompleteTextView searchBox;
    ArrayAdapter<String> adapter;
    FoodItem selectedItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        searchBox = (AutoCompleteTextView) findViewById(R.id.search_box);
        adapter = new CustomArrayAdapter(this, android.R.layout.simple_dropdown_item_1line);
        searchBox.setAdapter(adapter);

        setupCharts();

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                APIServices.getFoodItemService().getFoodList(charSequence.toString(), new Callback<List<FoodItem>>() {
                    @Override
                    public void success(List<FoodItem> foodItems, Response response) {
                        adapter.clear();
                        for (FoodItem item : foodItems) {
                            adapter.add(item.getName());
                            Log.d("Item", item.getName());
                        }
                        foodItemList = foodItems;
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("Error", "Error in fetching API");
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        searchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setChartData(selectedItem = foodItemList.get(i));
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_food, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.save) {
            if(selectedItem != null)
            {
                realm = Realm.getInstance(this);
                try {
                    // Copy the object to Realm. Any further changes must happen on realmUser
                    long begin = System.currentTimeMillis();
                    realm.beginTransaction();
                    FoodItem saveItem = realm.copyToRealm(selectedItem);
                    realm.commitTransaction();
                    long end = System.currentTimeMillis();
                    Toast.makeText(this, getString(R.string.time_log) + (end - begin) + " ms", Toast.LENGTH_LONG).show();
                }catch (RealmException e){
                    realm.commitTransaction();
                    Toast.makeText(this, getString(R.string.item_exists), Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    realm.commitTransaction();
                    e.printStackTrace();
                }
                finish();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

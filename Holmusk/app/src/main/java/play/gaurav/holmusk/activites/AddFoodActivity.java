package play.gaurav.holmusk.activites;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.util.List;

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
    FoodItem selectedItem;
    View chartContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        setupCharts();
        searchBox = (AutoCompleteTextView) findViewById(R.id.search_box);
        adapter = new CustomArrayAdapter(this, android.R.layout.simple_dropdown_item_1line);
        searchBox.setAdapter(adapter);
        chartContainer = findViewById(R.id.graph_container);
        chartContainer.setVisibility(View.GONE);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                //Query the API to get a list of food items
                APIServices.getFoodItemService().getFoodList(charSequence.toString(), new Callback<List<FoodItem>>() {
                    @Override
                    public void success(List<FoodItem> foodItems, Response response) {
                        //Populate the adapter with foodItem names
                        adapter.clear();
                        for (FoodItem item : foodItems) {
                            adapter.add(item.getName());
                        }
                        foodItemList = foodItems;
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //When the Item is selected by the user
        searchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Display the item in graphs
                selectedItem = foodItemList.get(i);
                if (selectedItem != null) {
                    chartContainer.setVisibility(View.VISIBLE);
                    setChartData(selectedItem);
                }
                //Close the keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
            }
        });
        searchBox.requestFocus();
    }

    @Override
    public void onBackPressed() {
        //IF the back is pressed and there is no data in Realm then show a message
        if (this.getIntent().getBooleanExtra("Empty", false)) {
            Toast.makeText(this, getString(R.string.cannot_back), Toast.LENGTH_SHORT).show();
        }
        else {
            super.onBackPressed();
        }
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
        //Save the item to Realm on save clicked
        if (id == R.id.save) {
            if (selectedItem != null) {
                try {
                    // Write the object to Realm with benchmarking
                    long begin = System.currentTimeMillis();
                    realm.beginTransaction();
                    FoodItem saveItem = realm.copyToRealm(selectedItem);
                    realm.commitTransaction();
                    long end = System.currentTimeMillis();
                    Toast.makeText(this, getString(R.string.time_log) + (end - begin) + " ms", Toast.LENGTH_LONG).show();
                } catch (RealmException e) {
                    realm.commitTransaction();
                    Toast.makeText(this, getString(R.string.item_exists), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
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

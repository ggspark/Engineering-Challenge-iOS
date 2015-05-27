package play.gaurav.holmusk;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import play.gaurav.holmusk.controller.APIServices;
import play.gaurav.holmusk.models.FoodItem;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class AddFoodActivity extends ActionBarActivity {

    AutoCompleteTextView searchBox;
    ArrayAdapter<String> adapter;
    List<FoodItem> foodItemList;
    private BarChart mChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        searchBox = (AutoCompleteTextView) findViewById(R.id.search_box);
        adapter = new CustomArrayAdapter(this, android.R.layout.simple_dropdown_item_1line);
        searchBox.setAdapter(adapter);
        mChart = (BarChart) findViewById(R.id.chart1);

        mChart.setDescription("");
        mChart.setMaxVisibleValueCount(60);
        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawGridBackground(false);
        mChart.setScaleEnabled(false);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setHighlightEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setSpaceBetweenLabels(0);
        xAxis.setDrawGridLines(false);

        mChart.getAxisLeft().setDrawGridLines(false);
        // add a nice and smooth animation
        mChart.animateY(2500);
        mChart.getLegend().setEnabled(false);

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
                setBarData(foodItemList.get(i));
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
            }
        });


    }

    private void setBarData(FoodItem item) {

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("Protein");
        xVals.add("Carbohydrate");
        xVals.add("Fat");

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        yVals1.add(new BarEntry(Float.parseFloat(item.getMeta().getProtein().substring(0, item.getMeta().getProtein().length() - 2)), 0));
        yVals1.add(new BarEntry(Float.parseFloat(item.getMeta().getCarbohydrate().substring(0, item.getMeta().getCarbohydrate().length() - 2)), 1));
        yVals1.add(new BarEntry(Float.parseFloat(item.getMeta().getFat().substring(0, item.getMeta().getFat().length() - 2)), 2));

        BarDataSet set1 = new BarDataSet(yVals1, "Data Set");
        set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        set1.setDrawValues(true);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);

        mChart.setData(data);
        mChart.invalidate();
        // add a nice and smooth animation
        mChart.animateY(2500);
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

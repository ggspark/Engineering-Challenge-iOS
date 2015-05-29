package play.gaurav.holmusk.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import play.gaurav.holmusk.R;
import play.gaurav.holmusk.adapters.CustomArrayAdapter;
import play.gaurav.holmusk.models.FoodItem;
import play.gaurav.holmusk.models.Meta;


public class MainActivity extends BaseActivity {

    ListView listView;
    PieChart chart5, chart6;
    View chartContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupCharts();
        chartContainer = findViewById(R.id.graph_container);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new CustomArrayAdapter(this, R.layout.simple_list_item);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Delete FoodItem from Realm Database with benchmarking
                long begin = System.currentTimeMillis();
                realm.beginTransaction();
                foodItemList.get(i).removeFromRealm();
                realm.commitTransaction();
                long end = System.currentTimeMillis();
                Toast.makeText(MainActivity.this, "Realm delete time = " + (end - begin) + " ms", Toast.LENGTH_LONG).show();
                refresh(); //Preform refresh on every remove
            }
        });
    }

    /**
     * Override setupCharts in BaseActivity to extend additional features of this activity
     */
    @Override
    protected void setupCharts() {
        super.setupCharts();

        chart5 = (PieChart) findViewById(R.id.chart5);
        setupPieChart(chart5);
        findViewById(R.id.fibre_container).setVisibility(View.VISIBLE);

        chart6 = (PieChart) findViewById(R.id.chart6);
        setupPieChart(chart6);
        findViewById(R.id.sugar_container).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();//Preform refresh everytime the screen comes into view
    }

    /**
     * Method the query realm and update the views and graphs
     */
    private void refresh() {
        //Query Realm with benchmarking
        long begin = System.currentTimeMillis();
        RealmResults<FoodItem> result = realm.allObjects(FoodItem.class);
        long end = System.currentTimeMillis();
        Toast.makeText(MainActivity.this, "Realm query time = " + (end - begin) + " ms", Toast.LENGTH_LONG).show();

        //Clear adapter and repopulate with the query results
        adapter.clear();
        if (result.size() > 0) {
            chartContainer.setVisibility(View.VISIBLE); //Set charts to visble
            foodItemList = new ArrayList<FoodItem>(result);//populate foodItemList

            for (FoodItem item : foodItemList)
                adapter.add(item.getName());//Populate adapter with item names

            //Limit the height of list view to 3
            if (adapter.getCount() > 3) {
                View item = adapter.getView(0, null, listView);
                item.measure(0, 0);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) (4.7 * item.getMeasuredHeight()));
                listView.setLayoutParams(params);
            }
            else {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                listView.setLayoutParams(params);
            }

            //Find the sum of all the food items in list and render chart data
            FoodItem totalItem = aggregate(foodItemList);
            setChartData(totalItem);
        }
        else {//If query results are empty open AddFoodActivity with a flag that results are empty
            startActivity(new Intent(this, AddFoodActivity.class).putExtra("Empty", true));
            chartContainer.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged(); //Notify that adapter data has changed

    }

    @Override
    protected void setChartData(FoodItem item) {
        super.setChartData(item);//Setup previous 4 common charts in BaseActivity

        //Chart 5: Fibre Distribution
        {
            ArrayList<String> xVals = new ArrayList<String>();
            ArrayList<Entry> yVals = new ArrayList<Entry>();
            int index = 0;
            for (FoodItem foodItem : foodItemList) {
                float fibre = getFloat(foodItem.getMeta().getFibre());
                float total = getFloat(item.getMeta().getFibre());
                if (fibre <= 0) //Don't include the result if value is less than 0
                    continue;
                xVals.add(foodItem.getName());
                yVals.add(new Entry(fibre / total, index++));
            }
            setPieData(xVals, yVals, chart5, ColorTemplate.COLORFUL_COLORS);
            chart5.setCenterText(getFloat(item.getMeta().getFibre()) + getString(R.string.g_fibre));
        }

        //Chart 6: Sugar Distribution
        {
            ArrayList<String> xVals = new ArrayList<String>();
            ArrayList<Entry> yVals = new ArrayList<Entry>();
            int index = 0;
            for (FoodItem foodItem : foodItemList) {
                float sugar = getFloat(foodItem.getMeta().getSugar());
                float total = getFloat(item.getMeta().getSugar());
                if (sugar <= 0) //Don't include the result if value is less than 0
                    continue;
                xVals.add(foodItem.getName());
                yVals.add(new Entry(sugar / total, index++));
            }
            setPieData(xVals, yVals, chart6, ColorTemplate.JOYFUL_COLORS);
            chart6.setCenterText(getFloat(item.getMeta().getSugar()) + getString(R.string.g_sugar));
        }

    }

    /**
     * Find the sum of all the items in the list
     * @param foodItemList
     * @return FoodItem containing sum of all the food items
     */
    private FoodItem aggregate(List<FoodItem> foodItemList) {
        FoodItem total = new FoodItem();
        total.setName("total");
        total.setMeta(new Meta());
        Meta totalMeta = total.getMeta();
        for (FoodItem item : foodItemList) {
            Meta itemMeta = item.getMeta();
            totalMeta.setFibre(totalMeta.getFibre() != null ? String.valueOf(getFloat(totalMeta.getFibre()) + getFloat(itemMeta.getFibre())) : itemMeta.getFibre());
            totalMeta.setPolyunsaturatedFat(totalMeta.getPolyunsaturatedFat() != null ? String.valueOf(getFloat(totalMeta.getPolyunsaturatedFat()) + getFloat(itemMeta.getPolyunsaturatedFat())) : itemMeta.getPolyunsaturatedFat());
            totalMeta.setSodium(totalMeta.getSodium() != null ? String.valueOf(getFloat(totalMeta.getSodium()) + getFloat(itemMeta.getSodium())) : itemMeta.getSodium());
            totalMeta.setEnergy(totalMeta.getEnergy() != null ? String.valueOf(getFloat(totalMeta.getEnergy()) + getFloat(itemMeta.getEnergy())) : itemMeta.getEnergy());
            totalMeta.setCholesterol(totalMeta.getCholesterol() != null ? String.valueOf(getFloat(totalMeta.getCholesterol()) + getFloat(itemMeta.getCholesterol())) : itemMeta.getCholesterol());
            totalMeta.setFat(totalMeta.getFat() != null ? String.valueOf(getFloat(totalMeta.getFat()) + getFloat(itemMeta.getFat())) : itemMeta.getFat());
            totalMeta.setSugar(totalMeta.getSugar() != null ? String.valueOf(getFloat(totalMeta.getSugar()) + getFloat(itemMeta.getSugar())) : itemMeta.getSugar());
            totalMeta.setCarbohydrate(totalMeta.getCarbohydrate() != null ? String.valueOf(getFloat(totalMeta.getCarbohydrate()) + getFloat(itemMeta.getCarbohydrate())) : itemMeta.getCarbohydrate());
            totalMeta.setSaturatedFat(totalMeta.getSaturatedFat() != null ? String.valueOf(getFloat(totalMeta.getSaturatedFat()) + getFloat(itemMeta.getSaturatedFat())) : itemMeta.getSaturatedFat());
            totalMeta.setMonounsaturatedFat(totalMeta.getMonounsaturatedFat() != null ? String.valueOf(getFloat(totalMeta.getMonounsaturatedFat()) + getFloat(itemMeta.getMonounsaturatedFat())) : itemMeta.getMonounsaturatedFat());
            totalMeta.setProtein(totalMeta.getProtein() != null ? String.valueOf(getFloat(totalMeta.getProtein()) + getFloat(itemMeta.getProtein())) : itemMeta.getProtein());
            totalMeta.setPotassium(totalMeta.getPotassium() != null ? String.valueOf(getFloat(totalMeta.getPotassium()) + getFloat(itemMeta.getPotassium())) : itemMeta.getPotassium());
        }
        return total;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Start AddFoodActivity if Add Food is clicked
        if (id == R.id.add_food) {
            startActivity(new Intent(this, AddFoodActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

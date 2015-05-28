package play.gaurav.holmusk.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
    PieChart chart5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupCharts();
        listView = (ListView) findViewById(R.id.listView);
        adapter = new CustomArrayAdapter(this, R.layout.simple_list_item);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                long begin = System.currentTimeMillis();
                realm.beginTransaction();
                foodItemList.get(i).removeFromRealm();
                realm.commitTransaction();
                long end = System.currentTimeMillis();
                Toast.makeText(MainActivity.this,"Realm delete time = "+ (end - begin) + " ms", Toast.LENGTH_LONG).show();

                refresh();
            }
        });


    }

    @Override
    protected void setupCharts() {
        super.setupCharts();

        chart5 = (PieChart) findViewById(R.id.chart5);
        setupPieChart(chart5);
        chart5.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        long begin = System.currentTimeMillis();
        RealmResults<FoodItem> result = realm.allObjects(FoodItem.class);
        long end = System.currentTimeMillis();
        Toast.makeText(MainActivity.this, "Realm query time = " + (end - begin) + " ms", Toast.LENGTH_LONG).show();

        foodItemList = new ArrayList<FoodItem>(result);

        adapter.clear();
        for(FoodItem item : foodItemList)
            adapter.add(item.getName());
        adapter.notifyDataSetChanged();

        FoodItem totalItem = aggregate(foodItemList);
        setChartData(totalItem);
        setFibreChart(totalItem);
    }

    private void setFibreChart(FoodItem totalItem){

        //Chart 5: Fibre Distribution
        {
            ArrayList<String> xVals = new ArrayList<String>();
            ArrayList<Entry> yVals = new ArrayList<Entry>();
            int index = 0;
            for(FoodItem item : foodItemList){
                float fibre = getFloat(item.getMeta().getFibre());
                float total = getFloat(totalItem.getMeta().getFibre());
                xVals.add(item.getName());
                yVals.add(new Entry(fibre/total, index++));
            }
            setPieData(xVals, yVals, chart5, ColorTemplate.COLORFUL_COLORS);
            chart5.setCenterText(getFloat(totalItem.getMeta().getFibre()) + " g of Fibre");
        }

    }

    private FoodItem aggregate(List<FoodItem> foodItemList){
        FoodItem total = new FoodItem();
        total.setName("total");
        total.setMeta(new Meta());
        Meta totalMeta = total.getMeta();
        for(FoodItem item : foodItemList){
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_food) {
            startActivity(new Intent(this, AddFoodActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

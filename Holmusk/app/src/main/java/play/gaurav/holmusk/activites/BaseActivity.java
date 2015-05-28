package play.gaurav.holmusk.activites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import play.gaurav.holmusk.R;
import play.gaurav.holmusk.models.FoodItem;


public class BaseActivity extends AppCompatActivity {

    protected static final String NAV_TAG = "Navigation";
    protected List<FoodItem> foodItemList;
    protected BarChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(NAV_TAG, "Class:" + ((Object) this).getClass().getSimpleName() + " Method:onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        Log.v(NAV_TAG, "Class:" + ((Object) this).getClass().getSimpleName() + " Method:onStart");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.v(NAV_TAG, "Class:" + ((Object) this).getClass().getSimpleName() + " Method:onRestart");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.v(NAV_TAG, "Class:" + ((Object) this).getClass().getSimpleName() + " Method:onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.v(NAV_TAG, "Class:" + ((Object) this).getClass().getSimpleName() + " Method:onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.v(NAV_TAG, "Class:" + ((Object) this).getClass().getSimpleName() + " Method:onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.v(NAV_TAG, "Class:" + ((Object) this).getClass().getSimpleName() + " Method:onDestroy");
        super.onDestroy();
    }

    protected void setupCharts() {
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
    }

    protected void setBarData(FoodItem item) {

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
}

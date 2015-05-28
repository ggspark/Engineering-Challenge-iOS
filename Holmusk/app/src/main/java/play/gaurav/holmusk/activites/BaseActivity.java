package play.gaurav.holmusk.activites;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

import play.gaurav.holmusk.R;
import play.gaurav.holmusk.models.FoodItem;


public class BaseActivity extends AppCompatActivity {

    protected static final String NAV_TAG = "Navigation";
    public static final int DURATION_MILLIS = 2500;
    protected List<FoodItem> foodItemList;
    protected BarChart chart1;
    protected PieChart chart2;

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
        chart1 = (BarChart) findViewById(R.id.chart1);

        chart1.setDescription("");
        chart1.setMaxVisibleValueCount(60);
        // scaling can now only be done on x- and y-axis separately
        chart1.setPinchZoom(false);
        chart1.setDrawBarShadow(false);
        chart1.setDrawGridBackground(false);
        chart1.setScaleEnabled(false);
        chart1.setDoubleTapToZoomEnabled(false);
        chart1.setHighlightEnabled(false);

        XAxis xAxis = chart1.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setSpaceBetweenLabels(0);
        xAxis.setDrawGridLines(false);

        chart1.getAxisLeft().setDrawGridLines(false);
        // add a nice and smooth animation
        chart1.animateY(2500);
        chart1.getLegend().setEnabled(false);





        chart2 = (PieChart) findViewById(R.id.chart2);
        chart2.setUsePercentValues(true);
        chart2.setDescription("");
        chart2.setDragDecelerationFrictionCoef(0.95f);
        chart2.setDrawHoleEnabled(true);
        chart2.setHoleColorTransparent(true);
        chart2.setTransparentCircleColor(Color.WHITE);
        chart2.setHoleRadius(65f);
        chart2.setTransparentCircleRadius(68f);
        chart2.setDrawCenterText(true);
        chart2.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart2.setRotationEnabled(true);
        chart2.setCenterText(getString(R.string.cal_dist));
        chart2.getLegend().setEnabled(false);
    }

    protected void setBarData(FoodItem item) {

        float protein = Float.parseFloat(item.getMeta().getProtein().substring(0, item.getMeta().getProtein().length() - 2));
        float carbs = Float.parseFloat(item.getMeta().getCarbohydrate().substring(0, item.getMeta().getCarbohydrate().length() - 2));
        float fat= Float.parseFloat(item.getMeta().getFat().substring(0, item.getMeta().getFat().length() - 2));
        float totalCal = protein*4 + carbs*4 + fat*9;

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add(getString(R.string.protein));
        xVals.add(getString(R.string.carb));
        xVals.add(getString(R.string.fat));

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        yVals1.add(new BarEntry(protein, 0));
        yVals1.add(new BarEntry(carbs, 1));
        yVals1.add(new BarEntry(fat, 2));

        BarDataSet set1 = new BarDataSet(yVals1, "Data Set");
        set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        set1.setDrawValues(true);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);

        chart1.setData(data);
        chart1.invalidate();
        // add a nice and smooth animation
        chart1.animateY(DURATION_MILLIS);







        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        yVals2.add(new Entry(protein*4/totalCal, 0));
        yVals2.add(new Entry(carbs*4/totalCal, 1));
        yVals2.add(new Entry(fat*9/totalCal, 2));

        PieDataSet dataSet = new PieDataSet(yVals2, getString(R.string.cal_dist));
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData pieData = new PieData(xVals, dataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.BLACK);
        chart2.setData(pieData);

        // undo all highlights
        chart2.highlightValues(null);

        chart2.invalidate();
        chart2.animateY(DURATION_MILLIS);

    }
}

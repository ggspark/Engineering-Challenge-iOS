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
import java.util.StringTokenizer;

import io.realm.Realm;
import play.gaurav.holmusk.R;
import play.gaurav.holmusk.models.FoodItem;


public class BaseActivity extends AppCompatActivity {

    protected static final String NAV_TAG = "Navigation";
    public static final int DURATION_MILLIS = 2500;
    protected Realm realm;
    protected List<FoodItem> foodItemList;
    protected BarChart chart1;
    protected PieChart chart2;
    protected BarChart chart3;


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
        setupBarChart(chart1);

        chart2 = (PieChart) findViewById(R.id.chart2);
        setupPieChart(chart2);

        chart3 = (BarChart) findViewById(R.id.chart3);
        setupBarChart(chart3);

    }

    private static void setupPieChart(PieChart chart) {
        chart.setUsePercentValues(true);
        chart.setDescription("");
        chart.setDragDecelerationFrictionCoef(0.95f);
        chart.setDrawHoleEnabled(true);
        chart.setHoleColorTransparent(true);
        chart.setTransparentCircleColor(Color.WHITE);
        chart.setHoleRadius(65f);
        chart.setTransparentCircleRadius(68f);
        chart.setDrawCenterText(true);
        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.getLegend().setEnabled(false);
    }

    private static void setupBarChart(BarChart chart) {
        chart.setDescription("");
        chart.setMaxVisibleValueCount(60);
        chart.setPinchZoom(false);
        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);
        chart.setScaleEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setHighlightEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setSpaceBetweenLabels(0);
        xAxis.setDrawGridLines(false);

        chart.getAxisLeft().setDrawGridLines(false);
        chart.getLegend().setEnabled(false);
    }

    protected void setBarData(FoodItem item) {

        {
            float protein = getFloat(item.getMeta().getProtein());
            float carbs = getFloat(item.getMeta().getCarbohydrate());
            float fat = getFloat(item.getMeta().getFat());
            float totalCal = protein * 4 + carbs * 4 + fat * 9;

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
            chart1.animateY(DURATION_MILLIS);


            ArrayList<Entry> yVals2 = new ArrayList<Entry>();
            yVals2.add(new Entry(protein * 4 / totalCal, 0));
            yVals2.add(new Entry(carbs * 4 / totalCal, 1));
            yVals2.add(new Entry(fat * 9 / totalCal, 2));

            PieDataSet dataSet = new PieDataSet(yVals2, getString(R.string.cal_dist));
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);
            dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

            PieData pieData = new PieData(xVals, dataSet);
            pieData.setValueFormatter(new PercentFormatter());
            pieData.setValueTextSize(10f);
            pieData.setValueTextColor(Color.BLACK);

            chart2.setData(pieData);
            chart2.setCenterText(getFloat(item.getMeta().getEnergy()) + getString(R.string.calories));
            chart2.highlightValues(null); //Remove all highlights
            chart2.invalidate();
            chart2.animateY(DURATION_MILLIS);
        }


        {
            float sodium = getFloat(item.getMeta().getSodium());
            float potassium = getFloat(item.getMeta().getPotassium());
            float cholesterol = getFloat(item.getMeta().getCholesterol());

            ArrayList<String> xVals = new ArrayList<String>();
            xVals.add("Sodium");
            xVals.add("Potassium");
            xVals.add("Cholesterol");

            ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
            yVals.add(new BarEntry(sodium, 0));
            yVals.add(new BarEntry(potassium, 1));
            yVals.add(new BarEntry(cholesterol, 2));

            BarDataSet set = new BarDataSet(yVals, "Data Set");
            set.setColors(ColorTemplate.JOYFUL_COLORS);
            set.setDrawValues(true);

            ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
            dataSets.add(set);

            BarData data = new BarData(xVals, dataSets);
            data.setValueTextSize(10f);

            chart3.setData(data);
            chart3.invalidate();
            chart3.animateY(DURATION_MILLIS);
        }




    }

    protected float getFloat(String item){
        if(item==null)
            return 0;
        try {
            StringTokenizer st = new StringTokenizer(item);
            return Float.parseFloat(st.nextToken());
        }catch (Exception e)
        {
            return 0;
        }
    }
}

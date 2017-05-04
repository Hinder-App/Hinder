package hinder.hinder;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ProgressActivity extends AppCompatActivity {
    private String url;

    private static LineGraphSeries<DataPoint> series;
    private static LineGraphSeries<DataPoint> series2;
    private static LineGraphSeries<DataPoint> series3;

    // Arrays hold scores
    private static ArrayList<Integer> colorList = new ArrayList<>();
    private static ArrayList<Integer> mathList = new ArrayList<>();
    private static ArrayList<Integer> memoryList = new ArrayList<>();

    private static ArrayList<Date> colorDate = new ArrayList<>();
    private static ArrayList<Date> mathDate = new ArrayList<>();
    private static ArrayList<Date> memoryDate = new ArrayList<>();

    private static ArrayList<String> colorAnalysis = new ArrayList<>();
    private static ArrayList<String> mathAnalysis = new ArrayList<>();
    private static ArrayList<String> memoryAnalysis = new ArrayList<>();

    // Define the Volley request queue that handles the URL request concurrently
    private RequestQueue requestQueue;

    final static SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    final static SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    @TargetApi(24)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        Intent intent = getIntent();
        final String username;

        if (intent.getStringExtra(MenuActivity.USERNAME) != null) {
            username = intent.getStringExtra(MenuActivity.USERNAME);
            if(username.equals("admin")) {
                url = "https://hinderest.herokuapp.com/users/miyagi@example.com/scores";
            } else {
                url = "https://hinderest.herokuapp.com/users/" + username + "/scores/10";
            }
        } else {
            username = intent.getStringExtra("USERNAME");
            if(username.equals("admin")) {
                url = "https://hinderest.herokuapp.com/users/miyagi@example.com/scores";
            } else {
                url = "https://hinderest.herokuapp.com/users/" + username + "/scores/10";
            }
        }
        Log.i("URL", url);

        // Create Volley request queue
        requestQueue = Volley.newRequestQueue(this);
        // Scores
        JsonObjectRequest ScoresRequest = new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        JSONObject data = response.getJSONObject("data");
                        JSONObject scores = data.getJSONObject("scores");

                        // Color Scores
                        JSONArray colorScores = scores.getJSONArray("colorScores");
                        for(int i = 0; i < colorScores.length(); i++) {
                            JSONObject colorScore = colorScores.getJSONObject(i);
                            colorList.add(i, colorScore.getInt("score"));
                            // Get date as string
                            String stringDate = colorScore.getString("date");
                            String editString = stringDate.substring(0, stringDate.length() - 5);
                            Log.i("String Date", editString);
                            Date isoDate = null;
                            try {
                                isoDate = parser.parse(editString);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Log.i("Iso Date", isoDate.toString());
                            String formatDate = formatter.format(isoDate);
                            Date readDate = null;
                            try {
                                readDate = formatter.parse(formatDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Log.i("Date", readDate.toString());
                            colorDate.add(i, readDate);
                            Log.i("colorList[i]:", Integer.toString(colorList.get(i)));
                            colorAnalysis.add(i, colorScore.getString("analysis"));
                        }

                        // Math Scores
                        JSONArray mathScores = scores.getJSONArray("mathScores");
                        for(int i = 0; i < mathScores.length(); i++) {
                            JSONObject mathScore = mathScores.getJSONObject(i);
                            mathList.add(i, mathScore.getInt("score"));
                            // Get date as string
                            String stringDate = mathScore.getString("date");
                            String editString = stringDate.substring(0, stringDate.length() - 5);
                            Log.i("String Date", editString);
                            Date isoDate = null;
                            try {
                                isoDate = parser.parse(editString);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Log.i("Iso Date", isoDate.toString());
                            String formatDate = formatter.format(isoDate);
                            Date readDate = null;
                            try {
                                readDate = formatter.parse(formatDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Log.i("Date", readDate.toString());
                            mathDate.add(i, readDate);
                            mathAnalysis.add(i, mathScore.getString("analysis"));
                        }

                        // Memory Scores
                        JSONArray memoryScores = scores.getJSONArray("memoryScores");
                        for(int i = 0; i < memoryScores.length(); i++) {
                            JSONObject memoryScore = memoryScores.getJSONObject(i);
                            memoryList.add(i, memoryScore.getInt("score"));
                            // Get date as string
                            String stringDate = memoryScore.getString("date");
                            String editString = stringDate.substring(0, stringDate.length() - 5);
                            Log.i("String Date", editString);
                            Date isoDate = null;
                            try {
                                isoDate = parser.parse(editString);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Log.i("Iso Date", isoDate.toString());
                            String formatDate = formatter.format(isoDate);
                            Date readDate = null;
                            try {
                                readDate = formatter.parse(formatDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Log.i("Date", readDate.toString());
                            memoryDate.add(i, readDate);
                            memoryAnalysis.add(i, memoryScore.getString("analysis"));
                        }

                        GraphView graph = (GraphView) findViewById(R.id.graph);

                        // declare an array of DataPoint objects with the same size as scores list
                        DataPoint[] dataPoints = new DataPoint[colorList.size()];
                        for(int i = 0; i < colorList.size(); i++) {
                            // add new DataPoint object to the array
                            dataPoints[i] = new DataPoint(colorDate.get(i), colorList.get(i));
                        }
                        series = new LineGraphSeries<>(dataPoints);
                        series.setColor(Color.BLUE);
                        graph.addSeries(series);

                        DataPoint[] dataPoints2 = new DataPoint[mathList.size()];
                        for(int i = 0; i < mathList.size(); i++) {
                            dataPoints2[i] = new DataPoint(mathDate.get(i), mathList.get(i));
                        }
                        series2 = new LineGraphSeries<>(dataPoints2);
                        series2.setColor(Color.RED);
                        graph.addSeries(series2);

                        DataPoint[] dataPoints3 = new DataPoint[memoryList.size()];
                        for(int i = 0; i < memoryList.size(); i++) {
                            dataPoints3[i] = new DataPoint(memoryDate.get(i), memoryList.get(i));
                        }
                        series3 = new LineGraphSeries<>(dataPoints3);
                        series3.setColor(Color.GREEN);
                        graph.addSeries(series3);

                        graph.getViewport().setScalable(true);
                        graph.getViewport().setScrollable(true);
                        graph.getViewport().setScalableY(true);
                        graph.getViewport().setScrollableY(true);
                        graph.setTitle(username + " Progress");

                        series.setOnDataPointTapListener(new OnDataPointTapListener() {
                            @Override
                            public void onTap(Series series, DataPointInterface dataPoint) {
                                new AlertDialog.Builder(ProgressActivity.this)
                                        .setTitle("Color Score")
                                        .setMessage("Your score: " + dataPoint.getY() + "\nDate: " + formatter.format(dataPoint.getX()) + "\nAnalysis: " + getColorAnalysis(dataPoint.getY(), formatter.format(dataPoint.getX())))
                                        .setNegativeButton(
                                                "Ok",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                }
                                        )
                                        .show();
                            }
                        });

                        series2.setOnDataPointTapListener(new OnDataPointTapListener() {
                            @Override
                            public void onTap(Series series, DataPointInterface dataPoint) {
                                new AlertDialog.Builder(ProgressActivity.this)
                                        .setTitle("Math Score")
                                        .setMessage("Your score: " + dataPoint.getY() + "\nDate: " + formatter.format(dataPoint.getX()) + "\nAnalysis: " + getMathAnalysis(dataPoint.getY(), formatter.format(dataPoint.getX())))
                                        .setNegativeButton(
                                                "Ok",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                }
                                        )
                                        .show();
                            }
                        });

                        series3.setOnDataPointTapListener(new OnDataPointTapListener() {
                            @Override
                            public void onTap(Series series, DataPointInterface dataPoint) {
                                new AlertDialog.Builder(ProgressActivity.this)
                                        .setTitle("Memory Score")
                                        .setMessage("Your score: " + dataPoint.getY() + "\nDate: " + formatter.format(dataPoint.getX()) + "\nAnalysis: " + getMemoryAnalysis(dataPoint.getY(), formatter.format(dataPoint.getX())))
                                        .setNegativeButton(
                                                "Ok",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                }
                                        )
                                        .show();
                            }
                        });

                        // set date label formatter
                        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
                            public String formatLabel(double value, boolean isValueX) {
                                if(isValueX) {
                                    return formatter.format(new Date((long) value));
                                } else {
                                    return super.formatLabel(value, isValueX);
                                }
                            }
                        });
                        graph.getGridLabelRenderer().setNumHorizontalLabels(colorDate.size());
                        graph.getGridLabelRenderer().setHorizontalLabelsAngle(90);
                        graph.getGridLabelRenderer().setTextSize(12);
                        graph.getViewport().setXAxisBoundsManual(false);
                        graph.getGridLabelRenderer().setHumanRounding(true);
                        graph.getGridLabelRenderer().setHorizontalAxisTitle("Date");
                        graph.getGridLabelRenderer().setVerticalAxisTitle("Score");
                        // Get max Y value
                        graph.getViewport().setMaxY((double) maxY());
                        graph.getViewport().setMinY(0);
                        graph.getViewport().setYAxisBoundsManual(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProgressActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        requestQueue.add(ScoresRequest);
    }

    public String getColorAnalysis(double Score, String date){
        String analysis = "";
        for(int i = 0; i < colorList.size(); i++){
            if(colorList.get(i) == Score && formatter.format(colorDate.get(i).getTime()).equals(date)){
                analysis = colorAnalysis.get(i);
            }
        }
        return analysis;
    }


    public String getMathAnalysis(double Score, String date){
        String analysis = "";
        for(int i = 0; i < mathList.size(); i++){
            if(mathList.get(i) == Score && formatter.format(mathDate.get(i).getTime()).equals(date)){
                analysis = mathAnalysis.get(i);
            }
        }
        return analysis;
    }

    public String getMemoryAnalysis(double Score, String date){
        String analysis = "";
        for(int i = 0; i < memoryList.size(); i++){
            if(memoryList.get(i) == Score && formatter.format(memoryDate.get(i).getTime()).equals(date)){
                analysis = memoryAnalysis.get(i);
            }
        }
        return analysis;
    }

    public int maxY(){
        int max = 0;
        for(int i = 0; i < colorList.size(); i++) {
            if(colorList.get(i) > max && colorList.get(i) > mathList.get(i) && colorList.get(i) > memoryList.get(i)){
                max = colorList.get(i);
            } else if(mathList.get(i) > max && mathList.get(i) > colorList.get(i) && mathList.get(i) > memoryList.get(i)){
                max = mathList.get(i);
            } else if(memoryList.get(i) > max && memoryList.get(i) > mathList.get(i) && memoryList.get(i) > colorList.get(i)){
                max = memoryList.get(i);
            }
        }
        Log.i("Max", Integer.toString(max));
        return max;
    }

    @Override
    public void onBackPressed() {

    }
}
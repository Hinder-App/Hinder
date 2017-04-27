package hinder.hinder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.support.v7.app.AlertDialog;

public class ColorActivity extends AppCompatActivity {

    //intent-extras
    String username;
    int mathOneCount;
    int mathOneTotal;
    int mathTwoCount;
    int mathTwoTotal;

    int memOneCount;
    int memOneTotal;
    int memTwoCount;
    int memTwoTotal;

    private String url;
    public static final String TAG = ColorActivity.class.getName();

    //
    private static int ROW_COUNT = 4;
    private static int COL_COUNT = 4;
    private Context context;

    private ColorButtonListener buttonListener;

    private TableLayout mainTable;

    //game implementation
    private String correctColor="example"; //color asked for
    private int correctColorInt;

    private String randColor; //button's text color "GREEN" "RED" "BLUE"
    private int randColorInt; //button's actual color int

    private final Random r = new Random();

    private int countCorrectAnswers = 0;
    private int totalAnswers = 0;
    private int gameCount = 0;

    private int gameOneCount = 0;
    private int gameOneTotal = 0;

    private int gameTwoCount = 0;
    private int gameTwoTotal = 0;

    //timer
    TextView timer;
    private static final String FORMAT = "%02d:%02d:%02d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiveDataIntent();
        loadActivity();
    }

    private void loadActivity() {
        //clear variables
        countCorrectAnswers = 0;
        totalAnswers=0;

        //increase game count
        gameCount++;

        setContentView(R.layout.activity_color);

        buttonListener = new ColorButtonListener();
        mainTable = (TableLayout) findViewById(R.id.TableLayout03);
        context = mainTable.getContext();
        newGame();


        //timer!
        timer = (TextView) findViewById(R.id.text_countdown);
        new CountDownTimer(20000, 1000) { // adjust the milli seconds here
            public void onTick(long millisUntilFinished) {
                timer.setText("" + String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                timer.setText("Time up!");

                //send backend number of correct answers and finishTime
                //CHECK IF ITs SECOND ITERATION OF GAME!
                if (gameCount < 2) {
                    gameOneCount = countCorrectAnswers;
                    gameOneTotal = totalAnswers;

                    Toast.makeText(ColorActivity.this, "Starting Second Session...", Toast.LENGTH_SHORT).show();
                    loadActivity();
                } else if (gameCount >= 2) {
                    gameTwoCount = countCorrectAnswers;
                    gameTwoTotal = totalAnswers;
                    sendJSON();
                }
            }
        }.start();
    }

    private void newGame() {

        totalAnswers++;

        TableRow tr = ((TableRow)findViewById(R.id.TableRow03));
        tr.removeAllViews();
        cleanTable(mainTable); //clear cards

        for (int y = 0; y < ROW_COUNT; y++) {
            mainTable.addView(createRow(y));
        }

        generateCorrectColor();
        ((TextView) findViewById(R.id.info)).setText("Pick the word that is colored: " + correctColor);
        ((TextView) findViewById(R.id.tries)).setText("Number of Correct Answers: " + countCorrectAnswers+" / "+totalAnswers);

    }


    private TableRow createRow(int y){
        TableRow row = new TableRow(context);
        row.setHorizontalGravity(Gravity.CENTER);
        for (int x = 0; x < COL_COUNT; x++) {
            generateRandomColor();
            generateRandomColorInt();
            row.addView(createButton(x,y, randColor, randColorInt));
        }
        return row;
    }

    private void cleanTable(TableLayout table) {

        int childCount = table.getChildCount();

        // Remove all rows except the first one
        if (childCount > 3) {
            table.removeViews(3, childCount - 3);
        }
    }
    private View createButton(int x, int y, String buttonText, int color){
        Button button = new Button(context);
        button.setId(100*x+y);
        button.setOnClickListener(buttonListener);
        button.setText(buttonText);
        button.setTextColor(color);
        return button;
    }

    class ColorButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (((Button) v).getCurrentTextColor() == correctColorInt) {
                Toast.makeText(getBaseContext(), "Answer is correct!", Toast.LENGTH_SHORT).show();
                countCorrectAnswers++;
                newGame();

            } else {
                Toast.makeText(ColorActivity.this, "Wrong Answer! Try Again", Toast.LENGTH_SHORT).show();
                newGame();
            }
        }
    }

    private void generateCorrectColor(){
        int min = 1;
        int max = 4;
        int rand = r.nextInt(max - min+1) + min;
        if (rand == 1) {
            correctColor ="Green";
            correctColorInt =  Color.GREEN;
        }
        if (rand == 2) {
            correctColor="Blue";
            correctColorInt = Color.BLUE;
        }
        if (rand == 3) {
            correctColor="Red";
            correctColorInt = Color.RED;
        }
        if (rand == 4) {
            correctColor="Yellow";
            correctColorInt = Color.YELLOW;
        }

    }

    private void generateRandomColor(){
        int min = 1;
        int max = 4;
        int rand = r.nextInt(max - min+1) + min;
        if (rand == 1) {
            randColor ="Green";
        }
        if (rand == 2) {
            randColor="Blue";
        }
        if (rand == 3) {
            randColor="Red";
        }
        if (rand == 4) {
            randColor="Yellow";
        }
    }

    private void generateRandomColorInt(){
        int min = 1;
        int max = 4;
        int rand = r.nextInt(max - min+1) + min;
        if (rand == 1) {
            randColorInt = Color.GREEN;
        }
        if (rand == 2) {
            randColorInt = Color.BLUE;
        }
        if (rand == 3) {
            randColorInt = Color.RED;
        }
        if (rand == 4) {
            randColorInt = Color.YELLOW;
        }
    }

    private void receiveDataIntent(){
        username = getIntent().getStringExtra("USERNAME");
        Log.i("USERNAME:", username);

        //math
        mathOneCount = getIntent().getIntExtra("MATH_ONE_COUNT", 0);
        Log.i("MATH_ONE_COUNT:", Integer.toString(mathOneCount));
        mathOneTotal = getIntent().getIntExtra("MATH_ONE_TOTAL", 0);
        Log.i("MATH_ONE_TOTAL:", Integer.toString(mathOneTotal));

        mathTwoCount = getIntent().getIntExtra("MATH_TWO_COUNT", 0);
        Log.i("MATH_TWO_COUNT:", Integer.toString(mathTwoCount));
        mathTwoTotal = getIntent().getIntExtra("MATH_TWO_TOTAL", 0);
        Log.i("MATH_TWO_TOTAL:", Integer.toString(mathTwoTotal));

        //memory
        memOneCount = getIntent().getIntExtra("MEM_ONE_COUNT", 0);
        Log.i("MEM_ONE_COUNT:", Integer.toString(memOneCount));
        memOneTotal = getIntent().getIntExtra("MEM_ONE_TIME", 0);
        Log.i("MEM_ONE_TOTAL:", Integer.toString(memOneTotal));

        memTwoCount = getIntent().getIntExtra("MEM_TWO_COUNT", 0);
        Log.i("MEM_TWO_COUNT:", Integer.toString(memTwoCount));
        memTwoTotal = getIntent().getIntExtra("MEM_TWO_TIME", 0);
        Log.i("MEM_TWO_TOTAL:", Integer.toString(memTwoTotal));
    }

    private void sendJSON(){
                    JSONObject request = new JSONObject();
                    try {
                        //color game data
                        JSONArray arrayElementTwoArray = new JSONArray();

                        JSONObject arrayElementTwoArrayElementOne = new JSONObject();
                        arrayElementTwoArrayElementOne.put("correct", gameOneCount);
                        arrayElementTwoArrayElementOne.put("total", gameOneTotal);

                        JSONObject arrayElementTwoArrayElementTwo = new JSONObject();
                        arrayElementTwoArrayElementTwo.put("correct", gameTwoCount);
                        arrayElementTwoArrayElementTwo.put("total", gameTwoTotal);

                        arrayElementTwoArray.put(arrayElementTwoArrayElementOne);
                        arrayElementTwoArray.put(arrayElementTwoArrayElementTwo);

                        request.put("colorGames", arrayElementTwoArray);

                        //math game data

                        JSONArray arrayElementOneArray = new JSONArray();

                        JSONObject arrayElementOneArrayElementOne = new JSONObject();
                        arrayElementOneArrayElementOne.put("correct", mathOneCount);
                        arrayElementOneArrayElementOne.put("total", mathOneTotal);

                        JSONObject arrayElementOneArrayElementTwo = new JSONObject();
                        arrayElementOneArrayElementTwo.put("correct", mathTwoCount);
                        arrayElementOneArrayElementTwo.put("total", mathTwoTotal);

                        arrayElementOneArray.put(arrayElementOneArrayElementOne);
                        arrayElementOneArray.put(arrayElementOneArrayElementTwo);

                        request.put("mathGames", arrayElementOneArray);

                        //memory game data
                        JSONArray arrayElementThreeArray = new JSONArray();

                        JSONObject arrayElementThreeArrayElementOne = new JSONObject();
                        arrayElementThreeArrayElementOne.put("correct", memOneCount);
                        arrayElementThreeArrayElementOne.put("finishTime", memOneTotal);

                        JSONObject arrayElementThreeArrayElementTwo = new JSONObject();
                        arrayElementThreeArrayElementTwo.put("correct", memTwoCount);
                        arrayElementThreeArrayElementTwo.put("finishTime", memTwoTotal);

                        arrayElementThreeArray.put(arrayElementThreeArrayElementOne);
                        arrayElementThreeArray.put(arrayElementThreeArrayElementTwo);
                        request.put("memoryGames", arrayElementThreeArray);
                        //
                        Log.i(TAG, "JSON request: " + request);
                    } catch (JSONException e) {
                        Toast.makeText(ColorActivity.this, "Something went wrong: JSONException", Toast.LENGTH_SHORT).show();
                        System.out.println(e);
                    }
                    username = getIntent().getStringExtra("USERNAME");
                    Log.i("USERNAME:", username);
                    url = "http://hinderest.herokuapp.com/users/"+username+"/sessions";
                    Log.i("URL", url);
                    JsonObjectRequest jsObjRequest = new JsonObjectRequest (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            onRes(response);
                        }
                    }, new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ColorActivity.this, "Something went wrong: VolleyError", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    });

                    // Access the RequestQueue through your singleton class.
                    HinderRequestQueue.getInstance(ColorActivity.this).addToRequestQueue(jsObjRequest);

    }

    //backend response
    private void onRes(JSONObject response) {
        try {
            String status = response.getString("status");

            if (status.equals("success")) {
                Intent intent = new Intent(ColorActivity.this, ProgressActivity.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
                Log.i(TAG, "Response: " + response.toString());
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(ColorActivity.this);
                builder.setMessage("Game Data Did Not Send")
                        .setNegativeButton("Retry", null)
                        .create()
                        .show();
                TimerTask tt = new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ColorActivity.this, MenuActivity.class);
                        intent.putExtra("USERNAME", username);
                        startActivity(intent);
                    }
                };
                Timer t = new Timer(false);
                t.schedule(tt, 1300);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
}

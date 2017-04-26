package hinder.hinder;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
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

    private Card firstCard;

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
    private int [] [] cards;

    private ColorButtonListener buttonListener;

    private static Object lock = new Object();

    private TableLayout mainTable;
    private ColorUpdateCardsHandler handler;

    //game implementation
    private String correctColor="example"; //color asked for
    private String currentColor; //color of the card pressed
    private String randColor; //button's text color "GREEN" "RED" "BLUE"

    private final Random r = new Random();

    private int countCorrectAnswers = 10;
    private int gameCount = 10;

    private int gameOneCount = 10;
    private int gameOneTime = 30000;

    private int gameTwoCount = 10;
    private int gameTwoTime= 10;

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

        //increase game count
        gameCount++;

        setContentView(R.layout.activity_color);
        handler = new ColorUpdateCardsHandler();

        buttonListener = new ColorButtonListener();
        mainTable = (TableLayout) findViewById(R.id.TableLayout03);
        context = mainTable.getContext();
        newGame(ROW_COUNT, COL_COUNT);


        //timer!
        timer = (TextView) findViewById(R.id.text_countdown);
        new CountDownTimer(10000, 1000) { // adjust the milli seconds here
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
                    gameOneTime = 30000;

                    Toast.makeText(ColorActivity.this, "Starting Second Session...", Toast.LENGTH_SHORT).show();
                    loadActivity();
                } else if (gameCount >= 2) {
                    gameTwoCount = countCorrectAnswers;
                    gameTwoTime = 30000;
                    sendJSON();
                }
            }
        }.start();
    }

    private void newGame(int c, int r) {
        cards = new int [COL_COUNT] [ROW_COUNT];

        TableRow tr = ((TableRow)findViewById(R.id.TableRow03));
        tr.removeAllViews();

        for (int y = 0; y < ROW_COUNT; y++) {
            mainTable.addView(createRow(y));
        }

        loadCards();

        generateCorrectColor();
        ((TextView) findViewById(R.id.info)).setText("Pick the word that is colored: " + correctColor);
        ((TextView) findViewById(R.id.tries)).setText("Number of Correct Answers: " + countCorrectAnswers);

    }

    private void loadCards(){
        try{
            int size = ROW_COUNT*COL_COUNT;

            Log.i("loadCards()","size=" + size);

            ArrayList<Integer> list = new ArrayList<>();
            for(int i=0;i<size;i++){
                list.add(new Integer(i));
            }

            Random r = new Random();
            for(int i=size-1;i>=0;i--){
                int t=0;
                if(i>0){
                    t = r.nextInt(i);
                }
                t=list.remove(t).intValue();
                cards[i%COL_COUNT][i/COL_COUNT]=t%(size/2);

                Log.i("loadCards()", "card["+(i%COL_COUNT)+
                        "]["+(i/COL_COUNT)+"]=" + cards[i%COL_COUNT][i/COL_COUNT]);
            }
        }
        catch (Exception e) {
            Log.e("loadCards()", e+"");
        }

    }

    private TableRow createRow(int y){
        TableRow row = new TableRow(context);
        row.setHorizontalGravity(Gravity.CENTER);
        for (int x = 0; x < COL_COUNT; x++) {
            row.addView(createImageButton(x,y));
        }
        return row;
    }

    private View createImageButton(int x, int y){
        Button button = new Button(context);
        button.setId(100*x+y);
        button.setOnClickListener(buttonListener);
        return button;
    }

    class ColorButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            synchronized (lock) {
                if(firstCard!=null){
                    return;
                }
                int id = v.getId();
                int x = id/100;
                int y = id%100;
                turnCard((Button)v,x,y);
            }
        }

        private void turnCard(Button button,int x, int y) {
            if(firstCard==null){
                firstCard = new Card(button,x,y);
            }
            else{
                if(firstCard.x == x && firstCard.y == y){
                    return; //the user pressed the same card
                }

                TimerTask tt = new TimerTask() {
                    @Override
                    public void run() {
                        try{
                            synchronized (lock) {
                                handler.sendEmptyMessage(0);
                            }
                        }
                        catch (Exception e) {
                            Log.e("E1", e.getMessage());
                        }
                    }
                };

                Timer t = new Timer(false);
                t.schedule(tt, 1300);
            }
        }
    }

    class ColorUpdateCardsHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            synchronized (lock) {
                checkCards();
            }
        }

        public void checkCards(){
            //sendJSON();
            /*if(cards[secondCard.x][secondCard.y] == cards[firstCard.x][firstCard.y]){
                firstCard.button.setVisibility(View.INVISIBLE);
                countCorrectAnswers++;
                ((TextView)findViewById(R.id.tries)).setText("Number of Correct Answers: "+countCorrectAnswers);

                generateCorrectColor();
                ((TextView) findViewById(R.id.instruction)).setText("Pick the word that is colored: " + correctColor);

                if (countCorrectAnswers==9){
                    //get current time
                    if (gameCount < 2) {
                        gameOneCount = countCorrectAnswers;
                        gameOneTime = Integer.parseInt(timer.getText().toString().replace(":", ""))*1000;
                        Log.i("COLOR_ONE_TIME:", Integer.toString(gameOneTime));
                        if (gameOneTime>100000){
                            gameOneTime-=40000;
                        }
                        Log.i("COLOR_ONE_TIME:", Integer.toString(gameOneTime));

                        Toast.makeText(ColorActivity.this, "Starting Second Session...", Toast.LENGTH_SHORT).show();
                        loadActivity();
                    } else if (gameCount >= 2) {
                        gameTwoCount = countCorrectAnswers;
                        gameTwoTime = Integer.parseInt(timer.getText().toString().replace(":", ""))*1000;
                        if (gameTwoTime>100000){
                            gameTwoTime-=40000;
                        }
                        Log.i("COLOR_TWO_TIME:", Integer.toString(gameTwoTime));

                        sendJSON();
                    }
                }
            }
            else {
                Toast.makeText(ColorActivity.this, "Wrong Answer! Try Again", Toast.LENGTH_SHORT).show();
                generateCorrectColor();
                ((TextView) findViewById(R.id.instruction)).setText("Pick the word that is colored: " + correctColor);
                //secondCard.button.setBackgroundDrawable(backImage);
                //firstCard.button.setBackgroundDrawable(backImage);
            }*/
            firstCard=null;
            //secondCard =null;
        }
    }

    private void generateCorrectColor(){
        int min = 1;
        int max = 3;
        int rand = r.nextInt(max - min+1) + min;
        if (rand == 1) {
            correctColor ="Green";
        }
        if (rand == 2) {
            correctColor="Blue";
        }
        if (rand == 3) {
            correctColor="Red";
        }
    }

    private void generateRandomColor(){
        int min = 1;
        int max = 3;
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
                        arrayElementTwoArrayElementOne.put("total", gameOneTime);

                        JSONObject arrayElementTwoArrayElementTwo = new JSONObject();
                        arrayElementTwoArrayElementTwo.put("correct", gameTwoCount);
                        arrayElementTwoArrayElementTwo.put("total", gameTwoTime);

                        arrayElementTwoArray.put(arrayElementTwoArrayElementOne);
                        arrayElementTwoArray.put(arrayElementTwoArrayElementTwo);

                        request.put("shapeGames", arrayElementTwoArray);

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
                Intent intent = new Intent(ColorActivity.this, MenuActivity.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
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

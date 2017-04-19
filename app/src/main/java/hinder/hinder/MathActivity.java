package hinder.hinder;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import android.os.CountDownTimer;

import android.support.v7.app.AlertDialog;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MathActivity extends AppCompatActivity {

     String username;
    private String url = "http://hinderest.herokuapp.com/users/"+username+"/sessions";
    public static final String TAG = MathActivity.class.getName();


    private final Random r = new Random();
    private int number1;
    private int number2;
    private int correctAnswer;
    private int countCorrectAnswers = 0;
    private int totalAnswers = 0;
    private int a; //correct answer in int
    private int gameCount = 0;

    private int gameOneCount = 0;
    private int gameOneTotal = 0;

    private int gameTwoCount = 0;
    private int gameTwoTotal = 0;

    TextView numberOne;
    TextView mathSymbol;
    TextView numberTwo;
    EditText answer;
    Button submitButton;

    //timer
    TextView timer;
    private static final String FORMAT = "%02d:%02d:%02d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = getIntent().getStringExtra("USERNAME");
        Log.i("USERNAME:", username);
        loadActivity();
    }

    public void loadActivity(){ //do all work here
        setContentView(R.layout.activity_math);

        //clear variables
        countCorrectAnswers = 0;
        totalAnswers = 0;

        //increase game count
        gameCount++;

        //timer!
        timer =(TextView)findViewById(R.id.text_countdown);
        new CountDownTimer(90000, 1000) { // adjust the milli seconds here
            public void onTick(long millisUntilFinished) {

                timer.setText(""+String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                timer.setText("Time up!");

                //send backend number of correct answers and totalAnswers
                //CHECK IF ITs SECOND ITERATION OF GAME!
                if(gameCount<2){
                    gameOneCount=countCorrectAnswers;
                    gameOneTotal= totalAnswers;

                    numberOne.setText("Starting Next Session...");
                    mathSymbol.setText("");
                    numberTwo.setText("");
                    answer.setText("");

                    try {
                        Thread.sleep(5000);
                    }catch(InterruptedException i){
                        Toast.makeText(MathActivity.this, "Something went wrong:InterruptedException", Toast.LENGTH_SHORT).show();
                    }
                    loadActivity();
                }else if(gameCount>=2){
                    gameTwoCount=countCorrectAnswers;
                    gameTwoTotal= totalAnswers;

                    numberOne.setVisibility(View.INVISIBLE);
                    mathSymbol.setVisibility(View.INVISIBLE);
                    numberTwo.setVisibility(View.INVISIBLE);
                    answer.setVisibility(View.INVISIBLE);
                    submitButton.setVisibility(View.INVISIBLE);

                    try {
                        Thread.sleep(5000);
                    }catch(InterruptedException i){
                        Toast.makeText(MathActivity.this, "Something went wrong:InterruptedException", Toast.LENGTH_SHORT).show();
                    }

                    /*
                    JSONObject request = new JSONObject();
                    try {
                        //shape game data
                        JSONArray arrayElementTwoArray = new JSONArray();

                        JSONObject arrayElementTwoArrayElementOne = new JSONObject();
                        arrayElementTwoArrayElementOne.put("correct", 10);
                        arrayElementTwoArrayElementOne.put("total", 10);

                        JSONObject arrayElementTwoArrayElementTwo = new JSONObject();
                        arrayElementTwoArrayElementTwo.put("correct", 10);
                        arrayElementTwoArrayElementTwo.put("total", 10);

                        arrayElementTwoArray.put(arrayElementTwoArrayElementOne);
                        arrayElementTwoArray.put(arrayElementTwoArrayElementTwo);

                        request.put("shapeGames", arrayElementTwoArray);

                        //math game data

                        JSONArray arrayElementOneArray = new JSONArray();

                        JSONObject arrayElementOneArrayElementOne = new JSONObject();
                        arrayElementOneArrayElementOne.put("correct", gameOneCount);
                        arrayElementOneArrayElementOne.put("total", gameOneTotal);

                        JSONObject arrayElementOneArrayElementTwo = new JSONObject();
                        arrayElementOneArrayElementTwo.put("correct", gameTwoCount);
                        arrayElementOneArrayElementTwo.put("total", gameTwoTotal);

                        arrayElementOneArray.put(arrayElementOneArrayElementOne);
                        arrayElementOneArray.put(arrayElementOneArrayElementTwo);

                        request.put("mathGames", arrayElementOneArray);

                        //memory game data
                        JSONArray arrayElementThreeArray = new JSONArray();

                        JSONObject arrayElementThreeArrayElementOne = new JSONObject();
                        arrayElementThreeArrayElementOne.put("correct", 10);
                        arrayElementThreeArrayElementOne.put("finishTime", 10);

                        JSONObject arrayElementThreeArrayElementTwo = new JSONObject();
                        arrayElementThreeArrayElementTwo.put("correct", 10);
                        arrayElementThreeArrayElementTwo.put("finishTime", 10);

                        arrayElementThreeArray.put(arrayElementThreeArrayElementOne);
                        arrayElementThreeArray.put(arrayElementThreeArrayElementTwo);
                        request.put("memoryGames", arrayElementThreeArray);
                        //
                        Log.i(TAG, "JSON request: " + request);
                    } catch (JSONException e) {
                        Toast.makeText(MathActivity.this, "Something went wrong: JSONException", Toast.LENGTH_SHORT).show();
                        System.out.println(e);
                    }

                    JsonObjectRequest jsObjRequest = new JsonObjectRequest (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            onRes(response);
                        }
                    }, new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MathActivity.this, "Something went wrong: VolleyError", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    });

                    // Access the RequestQueue through your singleton class.
                    HinderRequestQueue.getInstance(MathActivity.this).addToRequestQueue(jsObjRequest);
                    */
                }
            }
        }.start();


        numberOne = (TextView) findViewById(R.id.number_one);
        mathSymbol = (TextView) findViewById(R.id.math_symbol);
        numberTwo = (TextView) findViewById(R.id.number_two);
        answer = (EditText) findViewById(R.id.edittext_answer);

        //generate math symbol + or -
        loadNewEquation();

        //answer button implementation here!
        submitButton = (Button) findViewById(R.id.button_answer);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer();
            }
        });
    }
    public void checkAnswer(){
        //check if userInput = correctAnswer; then process information accordingly
        boolean hasValue = !answer.getText().toString().trim().isEmpty();

        if(hasValue) {
            try{
                a = Integer.parseInt(answer.getText().toString());
            }
            catch(NumberFormatException e){
                return;
            }
            if(correctAnswer==a) { //userAnswer is correct!
                Toast.makeText(getApplicationContext(), "Your answer is correct!", Toast.LENGTH_SHORT).show();
                countCorrectAnswers++;
                answer.setText("");
                loadNewEquation();
            }else if(correctAnswer!=a) { //userAnswer is not correct!
                Toast.makeText(getApplicationContext(), "Your answer is not correct!", Toast.LENGTH_SHORT).show();
                answer.setText("");
                loadNewEquation();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Please input an answer.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void loadNewEquation() {
        number1 = generateNumber();
        number2 = generateNumber();

        numberOne.setText(String.valueOf(number1));
        numberTwo.setText(String.valueOf(number2));

        generateMathSymbol();
        totalAnswers++;

        if (mathSymbol.getText().toString().equals("+")){
            correctAnswer = number1 + number2;
        }else if(mathSymbol.getText().toString().equals("-")){
            if(number1 < number2) { //no negative numbers as answers
                number1 = number1 + number2; //swap the two values
                number2 = number1 - number2;
                number1 = number1 - number2;
                numberOne.setText(String.valueOf(number1));
                numberTwo.setText(String.valueOf(number2));//remember the numbers are swapped!
                //so must call the .setText(num) to display/renew the changes made to the numbers
            }
            correctAnswer = number1 - number2;
        }
    }

    public void generateMathSymbol() {
        int min = 1;
        int max = 2;
        int rand = r.nextInt(max - min+1) + min;
        if (rand == 1) {
            mathSymbol.setText("+");
        }
        if (rand == 2) {
                mathSymbol.setText("-");
        }
    }

    public int generateNumber() {
        int min = 0;
        int max = 21;
        int rand = r.nextInt(max - min) + min;
        return rand;
    }

    /*
    //backend response
    private void onRes(JSONObject response) {
        try {
            String status = response.getString("status");

            if (status.equals("success")) {
                Intent intent = new Intent(MathActivity.this, MemoryActivity.class);
                intent.putExtra("USERNAME", username);
                Log.i("USERNAME:", username);

                intent.putExtra("GAME_ONE_COUNT", gameOneCount);
                Log.i("GAME_ONE_COUNT:", Integer.toString(gameOneCount));

                intent.putExtra("GAME_ONE_TOTAL", gameOneTotal);
                Log.i("GAME_ONE_TOTAL:", Integer.toString(gameOneTotal));

                intent.putExtra("GAME_TWO_COUNT", gameTwoCount);
                Log.i("GAME_TWO_COUNT:", Integer.toString(gameTwoCount));

                intent.putExtra("GAME_TWO_TOTAL", gameTwoTotal);
                Log.i("GAME_TWO_TOTAL:", Integer.toString(gameTwoTotal));

                startActivity(intent);
                Log.i(TAG, "Response: " + response.toString());
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MathActivity.this);
                builder.setMessage("Game Data Did Not Send")
                        .setNegativeButton("Retry", null)
                        .create()
                        .show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    */
}
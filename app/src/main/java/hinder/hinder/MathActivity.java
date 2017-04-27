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


import android.util.Log;

public class MathActivity extends AppCompatActivity {

     String username;


    private final Random r = new Random();
    private int number1;
    private int number2;
    private int correctAnswer;
    private int countCorrectAnswers = 0;
    private int totalAnswers = 1;
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
    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }

    public void loadActivity(){ //do all work here
        setContentView(R.layout.activity_math);

        //clear variables
        countCorrectAnswers = 0;
        totalAnswers = 1;

        //increase game count
        gameCount++;

        //timer!
        timer =(TextView)findViewById(R.id.text_countdown);
        new CountDownTimer(10000, 1000) { // adjust the milli seconds here
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
                    Toast.makeText(MathActivity.this, "Starting Second Session...", Toast.LENGTH_SHORT).show();

                    loadActivity();
                }else if(gameCount>=2){
                    gameTwoCount=countCorrectAnswers;
                    gameTwoTotal= totalAnswers;

                    numberOne.setVisibility(View.INVISIBLE);
                    mathSymbol.setVisibility(View.INVISIBLE);
                    numberTwo.setVisibility(View.INVISIBLE);
                    answer.setVisibility(View.INVISIBLE);
                    submitButton.setVisibility(View.INVISIBLE);

                    sendDataIntent();
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
                totalAnswers++;
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

    private void sendDataIntent(){
        Intent intent = new Intent(MathActivity.this, MemoryActivity.class);
        intent.putExtra("USERNAME", username);
        Log.i("USERNAME:", username);

        intent.putExtra("MATH_ONE_COUNT", gameOneCount);
        Log.i("MATH_ONE_COUNT:", Integer.toString(gameOneCount));

        intent.putExtra("MATH_ONE_TOTAL", gameOneTotal);
        Log.i("MATH_ONE_TOTAL:", Integer.toString(gameOneTotal));

        intent.putExtra("MATH_TWO_COUNT", gameTwoCount);
        Log.i("MATH_TWO_COUNT:", Integer.toString(gameTwoCount));

        intent.putExtra("MATH_TWO_TOTAL", gameTwoTotal);
        Log.i("MATH_TWO_TOTAL:", Integer.toString(gameTwoTotal));

        startActivity(intent);
    }
}
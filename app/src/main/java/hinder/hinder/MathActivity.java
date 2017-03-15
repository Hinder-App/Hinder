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

public class MathActivity extends AppCompatActivity {
    private final Random r = new Random();
    private int number1;
    private int number2;
    private int correctAnswer;
    private int countCorrectAnswers = 0;
    TextView numberOne;
    TextView mathSymbol;
    TextView numberTwo;
    EditText answer;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);

        numberOne = (TextView) findViewById(R.id.number_one);
        mathSymbol = (TextView) findViewById(R.id.math_symbol);
        numberTwo = (TextView) findViewById(R.id.number_two);
        answer = (EditText) findViewById(R.id.edittext_answer);
        //for loop (15-17)
        //generate random 1-10 numbers and have it assigned to the textviews
        //generate math symbol + or -

        //answer button implementation here!
        submitButton = (Button) findViewById(R.id.button_answer);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             checkAnswer();
             loadNewEquation();
                //Intent intent = new Intent(MathActivity.this, MemoryActivity.class);
                //startActivity(intent);
            }
        });
    }

    public void checkAnswer(){
        //check if userInput = correctAnswer; then process information accordingly
        int a = Integer.parseInt(answer.getText().toString());

        if(answer!=null) {
            if(correctAnswer==a) { //userAnswer is correct!
                Toast.makeText(getApplicationContext(), "Your answer is correct!", Toast.LENGTH_SHORT).show();
                countCorrectAnswers++;
                answer=null;
            }else if(correctAnswer!=a){ //userAnswer is not correct!
                Toast.makeText(getApplicationContext(), "Your answer is not correct!", Toast.LENGTH_SHORT).show();
                answer=null;
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please input an answer.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

        //iterate 15-17 times using for loop and count number of correct answers
    public void loadNewEquation() {
        number1 = generateNumber();
        number2 = generateNumber();
        if(number1 < number2) { //no negative numbers as answers
            number1 = number1 + number2; //swap the two values
            number2 = number1 - number2;
            number1 = number1 - number2;
        }
        numberOne.setText(number1);
        numberTwo.setText(number2);

        generateMathSymbol();
        if (mathSymbol.getText().equals("+")){
            correctAnswer = number1 + number2;
        }else if(mathSymbol.getText().equals("-")){
            correctAnswer = number1 - number2;
        }
    }

    public void generateMathSymbol() {
        int min = 0;
        int max = 2;
        int rand = r.nextInt(max - min) + min;
        switch (rand) {
            case 0: //adding
                mathSymbol.setText("+");
            case 1: //subtracting
                mathSymbol.setText("-");
        }
    }

    public int generateNumber() {
        int min = 0;
        int max = 21;
        int rand = r.nextInt(max - min) + min;
        return rand;
    }
}
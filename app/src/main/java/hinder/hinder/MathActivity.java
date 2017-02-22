package hinder.hinder;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MathActivity extends AppCompatActivity {

    TextView numberOne;
    TextView mathSymbol;
    TextView numberTwo;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);

        numberOne = (TextView) findViewById(R.id.number_one);
        mathSymbol = (TextView) findViewById(R.id.math_symbol);
        numberTwo = (TextView) findViewById(R.id.number_two);

        //for loop (15-17)
        //generate random 1-10 numbers and have it assigned to the textviews
        //generate math symbol + or -

        //answer button implementation here!
        submitButton = (Button) findViewById(R.id.button_answer);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /*
                 //check if userInput = correctAnswer; then process information accordingly
                if () {
                } else {
                } */
            }
        });

        //iterate 15-17 times using for loop and count number of correct answers
    }
}
package hinder.hinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ColorActivity extends AppCompatActivity {

    Button c1Button;
    Button c2Button;
    Button c3Button;
    Button c4Button;
    Button c5Button;
    Button c6Button;
    Button c7Button;
    Button c8Button;
    Button c9Button;

    TextView instruction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);

        instruction = (TextView) findViewById(R.id.instruction);

        c1Button = (Button) findViewById(R.id.card1);
        c1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ColorActivity.this, ProgressActivity.class);
                startActivity(intent);
            }
        });
        c2Button = (Button) findViewById(R.id.card2);
        c2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        c3Button = (Button) findViewById(R.id.card3);
        c3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        c4Button = (Button) findViewById(R.id.card4);
        c4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        c5Button = (Button) findViewById(R.id.card5);
        c5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        c6Button = (Button) findViewById(R.id.card6);
        c6Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        c7Button = (Button) findViewById(R.id.card7);
        c7Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        c8Button = (Button) findViewById(R.id.card8);
        c8Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        c9Button = (Button) findViewById(R.id.card9);
        c9Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}
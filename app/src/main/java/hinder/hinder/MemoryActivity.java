package hinder.hinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MemoryActivity extends AppCompatActivity {

    ImageButton c1Button;
    ImageButton c2Button;
    ImageButton c3Button;
    ImageButton c4Button;
    ImageButton c5Button;
    ImageButton c6Button;
    ImageButton c7Button;
    ImageButton c8Button;
    ImageButton c9Button;
    ImageButton c10Button;
    ImageButton c11Button;
    ImageButton c12Button;
    TextView instruction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);

        instruction = (TextView) findViewById(R.id.instruction);

        c1Button = (ImageButton) findViewById(R.id.card1);
        c1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        c2Button = (ImageButton) findViewById(R.id.card2);
        c2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        c3Button = (ImageButton) findViewById(R.id.card3);
        c3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        c4Button = (ImageButton) findViewById(R.id.card4);
        c4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        c5Button = (ImageButton) findViewById(R.id.card5);
        c5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        c6Button = (ImageButton) findViewById(R.id.card6);
        c6Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        c7Button = (ImageButton) findViewById(R.id.card7);
        c7Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        c8Button = (ImageButton) findViewById(R.id.card8);
        c8Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        c9Button = (ImageButton) findViewById(R.id.card9);
        c9Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        c10Button = (ImageButton) findViewById(R.id.card10);
        c10Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        c11Button = (ImageButton) findViewById(R.id.card11);
        c11Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        c12Button = (ImageButton) findViewById(R.id.card12);
        c12Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}
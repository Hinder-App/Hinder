package hinder.hinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {
    TextView information;
    public final static String USERNAME = "Username:";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        information = (TextView) findViewById(R.id.information);
        final String username = getIntent().getStringExtra(LoginActivity.USERNAME);
        Log.i("USERNAME:", username);

        Button mStartButton = (Button) findViewById(R.id.button_start);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MathActivity.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
            }
        });
        
        Button mSettingsButton = (Button) findViewById(R.id.button_settings);
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = getIntent();
                String username = loginIntent.getStringExtra(LoginActivity.USERNAME);
                Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
                intent.putExtra(USERNAME, username);
                startActivity(intent);
            }
        });

        Button mProgressButton = (Button) findViewById(R.id.button_progress);
        mProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = getIntent();
                String username = loginIntent.getStringExtra(LoginActivity.USERNAME);
                Log.i("Username", username);
                Intent intent = new Intent(MenuActivity.this, ProgressActivity.class);
                intent.putExtra(USERNAME, username);
                startActivity(intent);
            }
        });
    }
}

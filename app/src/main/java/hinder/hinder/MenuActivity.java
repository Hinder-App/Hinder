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

        Button mStartButton = (Button) findViewById(R.id.button_start);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = getIntent().getStringExtra(LoginActivity.USERNAME);
                if(username == null) {
                    username = getIntent().getStringExtra(RegisterActivity.USERNAME);
                }
                Log.i(USERNAME, username);
                Intent intent = new Intent(MenuActivity.this, MathActivity.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
            }
        });
        
        Button mSettingsButton = (Button) findViewById(R.id.button_settings);
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = getIntent().getStringExtra(LoginActivity.USERNAME);
                if(username == null) {
                    username = getIntent().getStringExtra(RegisterActivity.USERNAME);
                    Log.i(USERNAME, username);

                }
                Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
                intent.putExtra(USERNAME, username);
                startActivity(intent);
            }
        });

        Button mProgressButton = (Button) findViewById(R.id.button_progress);
        mProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = getIntent().getStringExtra(LoginActivity.USERNAME);
                Log.i("loginactivity Username", username);

                if(username == null) {
                    username = getIntent().getStringExtra(RegisterActivity.USERNAME);
                    Log.i("registeractiv Username", username);
                }
                Intent intent = new Intent(MenuActivity.this, ProgressActivity.class);
                intent.putExtra(USERNAME, username);
                startActivity(intent);
            }
        });
    }
}

package hinder.hinder;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    private ListView mListView;
    private Map<String, String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        setupActionBar();

        String[] accounts = {"Account 1", "Account 2", "Account 3", "Account 4", "Account 5"};

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.listview_accounts, R.id.account, accounts);

        mListView = (ListView) findViewById(R.id.accounts_list);
        mListView.setAdapter(adapter);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}

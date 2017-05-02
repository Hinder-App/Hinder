package hinder.hinder;

import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A register screen that registers a new user via name/age/email/password.
 */
public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = RegisterActivity.class.getName();
    Button mEmailSignInButton;
    AutoCompleteTextView etName, etAge, etEmail;
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setupActionBar();

        etName = (AutoCompleteTextView) findViewById(R.id.name);
        etAge = (AutoCompleteTextView) findViewById(R.id.age);
        etEmail = (AutoCompleteTextView) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject request = new JSONObject();
                try {
                    request.put("username", etEmail.getText().toString());
                    request.put("password", etPassword.getText().toString());
                    request.put("name", etName.getText().toString());
                    request.put("age", new Integer(etAge.getText().toString()));
                } catch (JSONException e) {
                    Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    System.out.println(e);
                }
                String url = "http://hinderest.herokuapp.com/users/" + etEmail.getText().toString();
                JsonObjectRequest jsObjRequest = new JsonObjectRequest (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        onRes(response);
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });

                // Access the RequestQueue through your singleton class.
                HinderRequestQueue.getInstance(RegisterActivity.this).addToRequestQueue(jsObjRequest);
            }
        });
    }

    private void onRes(JSONObject response) {
        etName = (AutoCompleteTextView) findViewById(R.id.name);
        etAge = (AutoCompleteTextView) findViewById(R.id.age);
        etEmail = (AutoCompleteTextView) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);
        try {
            String status = response.getString("status");

            if (status.equals("success")) {
                Intent intent = new Intent(RegisterActivity.this, MenuActivity.class);
                intent.putExtra("USERNAME", etEmail.getText().toString());
                startActivity(intent);
                Log.i(TAG, "Response: " + response.toString());
            } else {
                etName.setText("Name: " + response.getJSONObject("data").getJSONObject("user").getString("name"));
                etAge.setText("Age: " + response.getJSONObject("data").getJSONObject("user").getInt("age"));
                etEmail.setText("Email: " + response.getJSONObject("data").getJSONObject("user").getString("username"));
                etPassword.setText("Password: " + response.getJSONObject("data").getJSONObject("user").getString("password"));

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setMessage("Registration Failed")
                        .setNegativeButton("Retry", null)
                        .create()
                        .show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
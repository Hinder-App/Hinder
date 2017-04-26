package hinder.hinder;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    AutoCompleteTextView etEmail;
    EditText etPassword;
    private String url = "http://hinderest.herokuapp.com/users";
    public static final String TAG = LoginActivity.class.getName();

    public final static String USERNAME = "Username:";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = (AutoCompleteTextView) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(etEmail.getText().toString());
                System.out.println(etEmail.getText());

                // Put info in a JSONObject and send it to get a response
                JSONObject request = new JSONObject();
                try {
                    request.put("username", etEmail.getText().toString());
                    request.put("password", etPassword.getText().toString());
                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    System.out.println(e);
                }
                String url = "http://hinderest.herokuapp.com/users" + "/" + etEmail.getText().toString();
                JsonObjectRequest jsObjRequest = new JsonObjectRequest (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        onRes(response);
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });
                // Access the RequestQueue through your singleton class.
                HinderRequestQueue.getInstance(LoginActivity.this).addToRequestQueue(jsObjRequest);
            }
        });

        TextView mSignUpView = (TextView) findViewById(R.id.sign_up);
        mSignUpView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void onRes(JSONObject response) {
        etEmail = (AutoCompleteTextView) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);
        try {
            String status = response.getString("status");

            if (status.equals("success")) {
                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                String username = etEmail.getText().toString();
                intent.putExtra(USERNAME, username);
                startActivity(intent);
                Log.i("Username", username);
                Log.i(TAG, "Response: " + response.toString());
            } else {
                etEmail.setText("Email: " + response.getJSONObject("data").getJSONObject("user").getString("username"));
                etPassword.setText("Password: " + response.getJSONObject("data").getJSONObject("user").getString("password"));

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("Login Failed")
                        .setNegativeButton("Retry", null)
                        .create()
                        .show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
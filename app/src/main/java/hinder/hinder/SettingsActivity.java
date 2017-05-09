package hinder.hinder;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingsActivity extends AppCompatActivity {

    Button bSave;
    EditText etName, etAge, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();
        final String username = intent.getStringExtra(MenuActivity.USERNAME);

        etName = (EditText) findViewById(R.id.edit_name);
        etAge = (EditText) findViewById(R.id.edit_age);
        etPassword = (EditText) findViewById(R.id.edit_password);
        bSave = (Button) findViewById(R.id.button_Save);

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://hinderest.herokuapp.com/users/" + username;

                JSONObject request = new JSONObject();
                try {
                    request.put("username", username);
                    request.put("password", etPassword.getText().toString());
                    request.put("name", etName.getText().toString());
                    request.put("age", new Integer(etAge.getText().toString()));
                } catch (JSONException e) {
                    Toast.makeText(SettingsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    System.out.println(e);
                }
                JsonObjectRequest jsObjRequest = new JsonObjectRequest (Request.Method.PUT, url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        onRes(response);
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SettingsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });

                // Access the RequestQueue through your singleton class.
                HinderRequestQueue.getInstance(SettingsActivity.this).addToRequestQueue(jsObjRequest);
            }
        });
    }

    private void onRes(JSONObject response) {
        etName = (EditText) findViewById(R.id.edit_name);
        etAge = (EditText) findViewById(R.id.edit_age);
        etPassword = (EditText) findViewById(R.id.edit_password);
        try {
            String status = response.getString("status");

            if (status.equals("success")) {
                Intent intent = new Intent(SettingsActivity.this, MenuActivity.class);
                startActivity(intent);
                Log.i("Response", "Response: " + response.toString());
            } else {
                etName.setText("Email: " + response.getJSONObject("data").getJSONObject("user").getString("username"));
                etAge.setText("Age: " + response.getJSONObject("data").getJSONObject("user").getInt("age"));
                etPassword.setText("Password: " + response.getJSONObject("data").getJSONObject("user").getString("password"));

                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setMessage("Update Failed")
                        .setNegativeButton("Retry", null)
                        .create()
                        .show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
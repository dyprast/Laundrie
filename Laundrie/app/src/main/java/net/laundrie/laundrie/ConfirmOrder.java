package net.laundrie.laundrie;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import net.laundrie.laundrie.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ConfirmOrder extends AppCompatActivity {

    TextView txt_id, txt_nama, txt_nomor_telp, txt_berat, txt_alamat, txt_harga;
    String sessionId, sessionNama, sessionNomor_telp, berat, alamat;
    Button btn_conf;

    int success;
    ConnectivityManager conMgr;
    ProgressDialog pDialog;

    private String url = Server.URL + "order.php";

    private static final String TAG = ConfirmOrder.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    public final static String TAG_NAMA = "nama";
    public final static String TAG_NOMOR_TELP = "nomor_telp";
    public final static String TAG_ID = "id";

    SharedPreferences sharedPreferences;

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        sharedPreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }

        txt_id = (TextView) findViewById(R.id.txt_id);
        txt_nama = (TextView) findViewById(R.id.txt_nama);
        txt_nomor_telp = (TextView) findViewById(R.id.txt_nomor_telp);
        txt_alamat = (TextView) findViewById(R.id.txt_alamat);
        txt_berat = (TextView) findViewById(R.id.txt_berat);
        txt_harga = (TextView) findViewById(R.id.txt_harga);
        btn_conf = (Button) findViewById(R.id.btn_conf);

        sessionId = getIntent().getStringExtra("EXTRA_SESSION_ID");
        sessionNama = getIntent().getStringExtra("EXTRA_SESSION_NAMA");
        sessionNomor_telp = getIntent().getStringExtra("EXTRA_SESSION_NOMOR_TELP");
        berat = getIntent().getStringExtra("EXTRA_SESSION_BERAT");
        alamat = getIntent().getStringExtra("EXTRA_SESSION_ALAMAT");

        txt_id.setText("ID ANDA : " + sessionId);
        txt_nama.setText("" + sessionNama);
        txt_nomor_telp.setText("0" + sessionNomor_telp);
        txt_berat.setText("" + berat);
        txt_alamat.setText("" + alamat);

        btn_conf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String nama_konsumen = txt_nama.getText().toString();
                String berat = txt_berat.getText().toString();
                String harga = txt_harga.getText().toString();
                String alamat_konsumen = txt_alamat.getText().toString();
                String nomor_telp = txt_nomor_telp.getText().toString();
                String paket_id = "2";
                String user_id = sessionId;

                if (conMgr.getActiveNetworkInfo() != null
                        && conMgr.getActiveNetworkInfo().isAvailable()
                        && conMgr.getActiveNetworkInfo().isConnected()) {
                    checkOrder(nama_konsumen, berat, harga, alamat_konsumen, nomor_telp, paket_id, user_id);
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void checkOrder(final String nama_konsumen, final String berat, final String harga, final String alamat_konsumen, final String nomor_telp,
                            final String paket_id, final String user_id) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Sedang diproses ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Order Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Log.e("Successfully Order!", jObj.toString());

                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        txt_nama.setText("");
                        txt_berat.setText("");
                        txt_harga.setText("");
                        txt_alamat.setText("");
                        txt_nomor_telp.setText("");

                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        intent.putExtra(TAG_ID, sessionId);
                        intent.putExtra(TAG_NAMA, sessionNama);
                        intent.putExtra(TAG_NOMOR_TELP, sessionNomor_telp);
                        startActivity(intent);

                    } else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("nama_konsumen", nama_konsumen);
                params.put("berat", berat);
                params.put("harga", harga);
                params.put("alamat_konsumen", alamat_konsumen);
                params.put("nomor_telp", nomor_telp);
                params.put("paket_id", paket_id);
                params.put("user_id", user_id);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ConfirmOrder.this, Home.class);
        intent.putExtra("EXTRA_SESSION_ID", sessionId);
        intent.putExtra("EXTRA_SESSION_NAMA", sessionNama);
        intent.putExtra("EXTRA_SESSION_NOMOR_TELP", sessionNomor_telp);
        startActivity(intent);
    }

}

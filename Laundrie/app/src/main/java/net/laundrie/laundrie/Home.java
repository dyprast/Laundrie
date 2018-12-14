package net.laundrie.laundrie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    Button btn_order, btn_about;
    TextView txt_id;
    String sessionId, sessionNama, sessionNomor_telp;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn_order = (Button) findViewById(R.id.btn_order);
        btn_about = (Button) findViewById(R.id.btn_about);

        sharedPreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);

        sessionId = getIntent().getStringExtra("EXTRA_SESSION_ID");
        sessionNama = getIntent().getStringExtra("EXTRA_SESSION_NAMA");
        sessionNomor_telp = getIntent().getStringExtra("EXTRA_SESSION_NOMOR_TELP");

        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Order.class);
                intent.putExtra("EXTRA_SESSION_ID", sessionId);
                intent.putExtra("EXTRA_SESSION_NAMA", sessionNama);
                intent.putExtra("EXTRA_SESSION_NOMOR_TELP", sessionNomor_telp);
                startActivity(intent);
            }
        });
        btn_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), About.class);
                intent.putExtra("EXTRA_SESSION_ID", sessionId);
                intent.putExtra("EXTRA_SESSION_NAMA", sessionNama);
                intent.putExtra("EXTRA_SESSION_NOMOR_TELP", sessionNomor_telp);
                startActivity(intent);
            }
        });


    }
}

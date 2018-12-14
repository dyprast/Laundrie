package net.laundrie.laundrie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class About extends AppCompatActivity {

    Button btn_back;
    String sessionId, sessionNama, sessionNomor_telp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        btn_back = (Button) findViewById(R.id.btn_back);
        sessionId = getIntent().getStringExtra("EXTRA_SESSION_ID");
        sessionNama = getIntent().getStringExtra("EXTRA_SESSION_NAMA");
        sessionNomor_telp = getIntent().getStringExtra("EXTRA_SESSION_NOMOR_TELP");

        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(About.this, Home.class);
                intent.putExtra("EXTRA_SESSION_ID", sessionId);
                intent.putExtra("EXTRA_SESSION_NAMA", sessionNama);
                intent.putExtra("EXTRA_SESSION_NOMOR_TELP", sessionNomor_telp);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(About.this, Home.class);
        intent.putExtra("EXTRA_SESSION_ID", sessionId);
        intent.putExtra("EXTRA_SESSION_NAMA", sessionNama);
        intent.putExtra("EXTRA_SESSION_NOMOR_TELP", sessionNomor_telp);
        startActivity(intent);
    }
}

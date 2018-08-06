package com.example.qr_code_scanner;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvContent;
    private Button btnScan;
    private IntentIntegrator qrScan;
    private IntentResult result;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
        bindListeners();
        qrScan.initiateScan();

    }

    /*@Override
     public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("key_string", content);
        super.onSaveInstanceState(savedInstanceState);
    }

    */
    /*
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        tvContent.setText(savedInstanceState.get("key_string").toString());
        super.onRestoreInstanceState(savedInstanceState);
    }*/

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Nothing here", Toast.LENGTH_SHORT).show();
            } else {
                content = result.getContents();
                tvContent.setText(content);
                processData(content);
            }
        }
    }

    private void processData(String content) {
        if (Patterns.WEB_URL.matcher(content).matches()) {

            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(content)));
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newUri(getContentResolver(), "URI", Uri.parse(content));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "Copied to clipboard too!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {

            }
        } else {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("simple text", content);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            /*else if (Patterns.PHONE.matcher(content).matches()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                //ask for permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[] {Manifest.permission.CALL_PHONE}, 1234);
                }
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(content)));
        }*/
        }
    }


    private void bindListeners() {
        btnScan.setOnClickListener(this);
    }

    private void bindViews() {
        tvContent = (TextView) findViewById(R.id.tvContent);
        btnScan = (Button) findViewById(R.id.btnScan);
        qrScan = new IntentIntegrator(this);
        qrScan.setCaptureActivity(CaptureActivityPortrait.class);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnScan:
                qrScan.initiateScan();
                break;
        }
    }
}

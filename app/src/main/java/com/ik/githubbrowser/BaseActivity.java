package com.ik.githubbrowser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showMessage(String message) {
        if (!TextUtils.isEmpty(message))
            Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public void showMessage(int resId) {
        String message = BaseActivity.this.getString(resId);
        showMessage(message);
    }
}

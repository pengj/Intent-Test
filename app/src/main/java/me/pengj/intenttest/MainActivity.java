package me.pengj.intenttest;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final int REQUEST_LOGIN = 101;
    public static final String LOGIN_NAME = "login";

    @Bind(R.id.button_login)
    Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_image)
    public void startImageSelect() {
        Intent intent = new Intent(this, SelectImageActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_login)
    public void startLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    @OnClick(R.id.button_upper)
    public void startUpper() {
        Intent intent = new Intent(this, TextHashActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            //Get the result from Login
            case REQUEST_LOGIN:

                if (resultCode == Activity.RESULT_OK && null != data) {
                    mLoginButton.setText(R.string.pass_login);
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    mLoginButton.setText(R.string.cancel_login);
                }
                break;
            default:
                Log.d(TAG, "Unhandled the request code:" + requestCode);
        }
    }
}

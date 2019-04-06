package yolo.basket;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import yolo.basket.db.Database;
import yolo.basket.db.user.User;
import yolo.basket.teamActivity.TeamActivity;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mNameView;
    private EditText mUserNameView;

    private Button mSwitchFormButton;

    private static final boolean LOGIN = true;
    private static final boolean REGISTER = false;

    private boolean currentForm = LOGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        retrieveFormFields();
        setDefaultValues();
        mSwitchFormButton = findViewById(R.id.switchFormsButton);
        bindSwitchFormsButton();

        findViewById(R.id.email_sign_in_button).setOnClickListener(view -> {
            loginRegister();
        });
    }

    private void loginRegister() {
        UserLoginTask userLoginTask = new UserLoginTask();
        userLoginTask.execute((Void) null);
    }

    private void setDefaultValues() {
        mEmailView.setText("olitest");
        mNameView.setText("olitest");
        mUserNameView.setText("aaa");
        mPasswordView.setText("aaa");
    }

    private void bindSwitchFormsButton() {
        mSwitchFormButton.setOnClickListener(view -> switchForm());
    }

    public void goToTeamActivity (View view){
        Intent intent = new Intent (this, TeamActivity.class);
        startActivity(intent);
    }

    private String getButtonText() {
        String register = "Already have an account?";
        String login = "Don't have an account?";
        return currentForm == LOGIN ? login : register;
    }

    private void switchForm() {
        currentForm = !currentForm;
        String btnText = getButtonText();
        int visibility = currentForm ? View.GONE : View.VISIBLE;
        mEmailView.setVisibility(visibility);
        mNameView.setVisibility(visibility);
        mSwitchFormButton.setText(btnText);
    }

    private void retrieveFormFields() {
        mEmailView = findViewById(R.id.email);
        mNameView = findViewById(R.id.name);
        mUserNameView = findViewById(R.id.userName);
        mPasswordView = findViewById(R.id.password);
    }

    private User createUserFromInput() {
        User user = new User();
        user.setEmail(mEmailView.getText().toString());
        user.setName(mNameView.getText().toString());
        user.setPassword(mPasswordView.getText().toString());
        user.setUserName(mUserNameView.getText().toString());
        return user;
    }

    public void startMainActivity() {
        runOnUiThread(() -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private String message = "Incorrect username or password";

        @Override
        protected Boolean doInBackground(Void... params) {
            Database.useAnonymousCredentials();
            User user = createUserFromInput();

            if (currentForm == LOGIN)
                return tryLogin(user);
            else
                return tryRegister(user);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success)
                startMainActivity();
            else
                showError();
        }

        private void showError() {
            mPasswordView.setError(message);
        }

        private boolean tryRegister(User user) {
            try {
                message = Database.register(user);
                return message.equals(user.getUserName());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        private boolean tryLogin(User user) {
            try {
                Database.login(user);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}


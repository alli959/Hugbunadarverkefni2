package yolo.basket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yolo.basket.db.Database;
import yolo.basket.db.user.User;
import yolo.basket.teamActivity.TeamActivity;

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
    private Button mLoginButton;

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
        mLoginButton = findViewById(R.id.email_sign_in_button);
        mLoginButton.setText("Login");
        mLoginButton.setOnClickListener(view -> {
            loginRegister();
        });
    }

    private void loginRegister() {
        UserLoginTask userLoginTask = new UserLoginTask();
        userLoginTask.execute((Void) null);
    }

    private void setDefaultValues() {
        mEmailView.setText("olitest@default.is");
        mNameView.setText("olitest");
        mUserNameView.setText("aaaab");
        mPasswordView.setText("aaaaaaaa");
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

    private String getOtherButtonText() {
        String register = "Register";
        String login = "Login";
        return currentForm == LOGIN ? login : register;
    }

    private void switchForm() {
        currentForm = !currentForm;
        String btnText = getButtonText();
        int visibility = currentForm ? View.GONE : View.VISIBLE;
        mEmailView.setVisibility(visibility);
        mNameView.setVisibility(visibility);
        mSwitchFormButton.setText(btnText);
        mLoginButton.setText(getOtherButtonText());
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
            runOnUiThread(() -> clearErrors());

            if (currentForm == LOGIN)
                return tryLogin(user);
            else
                return tryRegister(user);
        }

        private boolean tryRegister(User user) {
            if (!checkAll(user))
                return false;
            try {
                message = Database.register(user);
                if (message.equals(user.getUserName()))
                    return true;
                else {
                    message = "Username taken";
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                message = "Undefined error";
                return false;
            }
        }

        private boolean tryLogin(User user) {
            if (!checkUserAndPassword(user))
                return false;
            try {
                Database.login(user);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                message = "Wrong username / password";
                return false;
            }
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            if (success && currentForm == LOGIN)
                startMainActivity();
            else if (success) {
                currentForm = LOGIN;
                new UserLoginTask().execute((Void) null);
            } else
                runOnUiThread(() -> mUserNameView.setError(message));

        }

        public final Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        private void clearErrors () {
            mNameView.setError(null);
            mPasswordView.setError(null);
            mUserNameView.setError(null);
            mEmailView.setError(null);
        }

        private boolean checkAll (User user) {
            boolean a = checkNameAndEmail(user);
            boolean b = checkUserAndPassword(user);
            return a && b;
        }

        private boolean checkNameAndEmail (User user) {
            boolean a = checkName(user.getName());
            boolean b = checkEmail(user.getEmail());
            return a && b;
        }

        private boolean checkUserAndPassword (User user) {
            boolean a = checkUserName(user.getUserName());
            boolean b = checkPassword(user.getPassword());
            return a && b;
        }

        private boolean checkName(String name) {
            if (name.length() < 4) {
                runOnUiThread(() -> mNameView.setError("Name must be at least 4 characters long"));
                return false;
            } else {
                return true;
            }
        }

        private boolean checkUserName(String username) {
            if (username.length() < 4) {
                message = "Username must be at least 4 characters long";
                return false;
            } else {
                return true;
            }
        }

        private boolean checkPassword(String password) {
            if (password.length() < 8) {
                runOnUiThread(() -> mPasswordView.setError("Password must be at least 8 characters long"));
                return false;
            } else {
                return true;
            }
        }

        private boolean checkEmail(String email) {
            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
            if (!matcher.find()) {
                runOnUiThread(() -> mEmailView.setError("Not valid email"));
                return false;
            } else {
                return true;
            }

        }

    }
}


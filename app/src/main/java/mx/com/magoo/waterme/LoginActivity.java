package mx.com.magoo.waterme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    public EditText editTxtEmail, editTxtPassword;
    public Button btnLogin, btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWidgets();
        setWidgetsFunctionalities();
    }

    public void getWidgets() {
        this.editTxtEmail = (EditText) findViewById(R.id.editTxtEmail);
        this.editTxtPassword = (EditText) findViewById(R.id.editTxtPassword);
        this.btnSignup = (Button) findViewById(R.id.btnSignup);
        this.btnLogin = (Button) findViewById(R.id.btnLogin);
    }

    public void setWidgetsFunctionalities() {
        this.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        this.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login() {
        String email = this.editTxtEmail.getText().toString();
        String password = this.editTxtPassword.getText().toString();
        if (!email.equals("") && !password.equals("")) {
            ParseUser.logInInBackground(email, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null) {
                        Intent intent = new Intent(LoginActivity.this, MyPlantsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else {
                        new Utils().showSimpleAlertDialog(LoginActivity.this, "Usuario o contraseña incorrectos", "OK");
                    }
                }
            });
        }
        else {
            new Utils().showSimpleAlertDialog(LoginActivity.this, "Por favor llena todos los campos", "OK");
        }
    }
}

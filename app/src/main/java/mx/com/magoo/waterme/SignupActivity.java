package mx.com.magoo.waterme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    private EditText editTxtName, editTxtEmail, editTxtPassword, editTxtPasswordConfirm;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWidgets();
        setWidgetsFunctionalities();
    }

    public void getWidgets() {
        this.editTxtName = (EditText) findViewById(R.id.editTxtName);
        this.editTxtEmail = (EditText) findViewById(R.id.editTxtEmail);
        this.editTxtPassword = (EditText) findViewById(R.id.editTxtPassword);
        this.editTxtPasswordConfirm = (EditText) findViewById(R.id.editTxtPasswordConfirm);
        this.btnSignup = (Button) findViewById(R.id.btnSignup);
    }

    public void setWidgetsFunctionalities() {
        this.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    public void signup() {
        String name = this.editTxtName.getText().toString();
        String email = this.editTxtEmail.getText().toString();
        String password = this.editTxtPassword.getText().toString();
        String passwordConfirm = this.editTxtPasswordConfirm.getText().toString();
        if (!name.equals("") && !email.equals("") && !password.equals("") && !passwordConfirm.equals("")) {
            if (password.equals(passwordConfirm)) {
                ParseUser newUser = new ParseUser();
                newUser.setUsername(email);
                newUser.setEmail(email);
                newUser.setPassword(password);
                newUser.put("name", name);
                newUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Intent intent = new Intent(SignupActivity.this, MyPlantsActivity.class);
                            startActivity(intent);
                        } else {
                            new Utils().showSimpleAlertDialog(SignupActivity.this, "El usuario ya existe", "OK");
                        }
                    }
                });
            }
            else {
                new Utils().showSimpleAlertDialog(SignupActivity.this, "Las contraseñas no coinciden", "OK");
            }
        }
        else {
            new Utils().showSimpleAlertDialog(SignupActivity.this, "Por favor llena todos los campos", "OK");
        }
    }
}

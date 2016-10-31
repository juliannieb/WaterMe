package mx.com.magoo.waterme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    public Button btnLogin, btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWidgets();
        setWidgetsFunctionalities();
    }

    public void getWidgets() {
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
                Intent intent = new Intent(LoginActivity.this, MyPlantsActivity.class);
                startActivity(intent);
            }
        });
    }
}

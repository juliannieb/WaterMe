package mx.com.magoo.waterme;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddPlantActivity extends AppCompatActivity {

    EditText editTextPlantName, editTextPlantDescription, editTextDeviceID, editTextDevicePassword;
    Button btnsWateringDays[] = new Button[7];
    CircleImageView imgPlant;
    Button btnSave;
    Boolean wateringDays[] = {false, false, false, false, false, false, false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);
        getWidgets();
        setWidgetsFunctionalities();
    }

    public void getWidgets() {
        editTextPlantName = (EditText) findViewById(R.id.editTxtPlantName);
        editTextPlantDescription = (EditText) findViewById(R.id.editTxtPlantDescription);
        editTextDeviceID = (EditText) findViewById(R.id.editTxtDeviceID);
        editTextDevicePassword = (EditText) findViewById(R.id.editTxtDevicePassword);
        Button btnMonday = (Button) findViewById(R.id.btnMonday);
        Button btnTuesday = (Button) findViewById(R.id.btnTuesday);
        Button btnWednesday = (Button) findViewById(R.id.btnWednesday);
        Button btnThursday = (Button) findViewById(R.id.btnThursday);
        Button btnFriday = (Button) findViewById(R.id.btnFriday);
        Button btnSaturday = (Button) findViewById(R.id.btnSaturday);
        Button btnSunday = (Button) findViewById(R.id.btnSunday);
        btnsWateringDays[0] = btnMonday;
        btnsWateringDays[1] = btnTuesday;
        btnsWateringDays[2] = btnWednesday;
        btnsWateringDays[3] = btnThursday;
        btnsWateringDays[4] = btnFriday;
        btnsWateringDays[5] = btnSaturday;
        btnsWateringDays[6] = btnSunday;
        imgPlant = (CircleImageView) findViewById(R.id.imgPlant);
        btnSave = (Button) findViewById(R.id.btnSave);
    }

    public void setWidgetsFunctionalities() {
        for (int i = 0; i < btnsWateringDays.length; i++) {
            final int copyI = i;
            btnsWateringDays[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (wateringDays[copyI]) {
                        btnsWateringDays[copyI].setBackgroundColor(ContextCompat.getColor(AddPlantActivity.this, R.color.gray));
                        btnsWateringDays[copyI].setTextColor(ContextCompat.getColor(AddPlantActivity.this, R.color.black));
                        wateringDays[copyI] = false;
                    }
                    else {
                        btnsWateringDays[copyI].setBackgroundColor(ContextCompat.getColor(AddPlantActivity.this, R.color.colorPrimary));
                        btnsWateringDays[copyI].setTextColor(ContextCompat.getColor(AddPlantActivity.this, R.color.white));
                        wateringDays[copyI] = true;
                    }
                }
            });
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePlant();
            }
        });
    }

    public void savePlant() {
        String plantName = editTextPlantName.getText().toString();
        String plantDescription = editTextPlantDescription.getText().toString();
        String deviceID = editTextDeviceID.getText().toString();
        String devicePassword = editTextDevicePassword.getText().toString();
        checkDevice(plantName, plantDescription, deviceID, devicePassword);
    }

    public void checkDevice(final String plantName, final String plantDescription, String deviceID, String devicePassword) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("WaterDevice");
        query.whereEqualTo("ip", deviceID);
        query.whereEqualTo("password", devicePassword);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null && object != null) {
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    ParseObject waterDevice = object;
                    ParseObject plant = new ParseObject("Plant");
                    plant.put("name", plantName);
                    plant.put("description", plantDescription);
                    plant.put("wateringDays", Arrays.asList(wateringDays));
                    plant.put("user", currentUser);
                    plant.put("waterDevice", waterDevice);
                    savePlantObject(plant);
                }
                else {
                    new Utils().showSimpleAlertDialog(AddPlantActivity.this, "Verifica que los datos del dispositivo sean correctos.", "OK");
                }
            }
        });
    }

    public void savePlantObject(final ParseObject plant) {
        plant.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    WaterMe app = (WaterMe) getApplication();
                    app.plant = plant;
                    AddPlantActivity.this.finish();
                }
                else {
                    new Utils().showSimpleAlertDialog(AddPlantActivity.this, "Ocurrió un error, por favor intenta de nuevo.", "OK");
                    e.printStackTrace();
                }
            }
        });
    }
}

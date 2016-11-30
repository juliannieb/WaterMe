package mx.com.magoo.waterme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddPlantActivity extends AppCompatActivity {

    TextView txtTitle;
    EditText editTextPlantName, editTextPlantDescription, editTextTime, editTextDeviceID, editTextDevicePassword;
    Button btnsWateringDays[] = new Button[7];
    CircleImageView imgPlant;
    Button btnSave;
    Boolean wateringDays[] = {false, false, false, false, false, false, false};
    ParseObject plant = null;
    ProgressDialog progressDialog;
    final int PICK_IMAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);
        getWidgets();
        setWidgetsFunctionalities();
        plant = getPlantFromApp();
        if (plant != null) {
            txtTitle.setText("Editar Planta");
            btnSave.setText("Guardar");
            setPlantInformation();
        }
    }

    public void getWidgets() {
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        editTextPlantName = (EditText) findViewById(R.id.editTxtPlantName);
        editTextPlantDescription = (EditText) findViewById(R.id.editTxtPlantDescription);
        editTextTime = (EditText) findViewById(R.id.editTxtTime);
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
        imgPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePlant();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            Uri selectedImage = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (imageStream != null) {
                Bitmap selectedImageBM = BitmapFactory.decodeStream(imageStream);
                imgPlant.setImageBitmap(selectedImageBM);
            }
            //InputStream inputStream = AddPlantActivity.this.getContentResolver().openInputStream(data.getData());
            //Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            //image.setImageBitmap(bitmap);
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
    }

    public ParseObject getPlantFromApp() {
        WaterMe app = (WaterMe) getApplication();
        ParseObject plant = app.plant;
        app.plant = null;
        return plant;
    }

    boolean validTime(String time) {
        for (int i = 0; i < time.length(); i++) {
            char c = time.charAt(i);
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public void savePlant() {
        String plantName = editTextPlantName.getText().toString();
        String plantDescription = editTextPlantDescription.getText().toString();
        String plantWaterTime = editTextTime.getText().toString();
        String deviceID = editTextDeviceID.getText().toString();
        String devicePassword = editTextDevicePassword.getText().toString();
        if (!plantName.equals("") && !plantDescription.equals("") && !plantWaterTime.equals("")
                && !deviceID.equals("") && !devicePassword.equals("")) {
            if (validTime(plantWaterTime)) {
                checkDevice(plantName, plantDescription, plantWaterTime, deviceID, devicePassword);
            }
            else {
                new Utils().showSimpleAlertDialog(AddPlantActivity.this, "El tiempo debe ser un numero entero.", "OK");
            }
        }
        else {
            new Utils().showSimpleAlertDialog(AddPlantActivity.this, "Por favor llena todos los campos.", "OK");
        }
    }

    public void checkDevice(final String plantName, final String plantDescription, final String plantWaterTime, String deviceID, String devicePassword) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("WaterDevice");
        query.whereEqualTo("ip", deviceID);
        query.whereEqualTo("password", devicePassword);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null && object != null) {
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    ParseObject waterDevice = object;
                    ParseObject plant;
                    if (AddPlantActivity.this.plant == null) {
                        plant = new ParseObject("Plant");
                    }
                    else {
                        plant = AddPlantActivity.this.plant;
                    }
                    plant.put("name", plantName);
                    plant.put("description", plantDescription);
                    plant.put("waterTime", plantWaterTime);
                    plant.put("wateringDays", Arrays.asList(wateringDays));
                    plant.put("user", currentUser);
                    plant.put("waterDevice", waterDevice);
                    savePlantObject(plant);
                } else {
                    new Utils().showSimpleAlertDialog(AddPlantActivity.this, "Verifica que los datos del dispositivo sean correctos.", "OK");
                }
            }
        });
    }

    public void savePlantObject(final ParseObject plant) {
        progressDialog = ProgressDialog.show(AddPlantActivity.this, "Cargando...",
                "Cargando, espere por favor...", false, false);
        plant.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                progressDialog.cancel();
                progressDialog = null;
                if (e == null) {
                    WaterMe app = (WaterMe) getApplication();
                    app.plant = plant;
                    AddPlantActivity.this.finish();
                } else {
                    new Utils().showSimpleAlertDialog(AddPlantActivity.this, "Ocurrió un error, por favor intenta de nuevo.", "OK");
                    e.printStackTrace();
                }
            }
        });
    }

    public void setPlantInformation() {
        if (plant.getParseFile("image") != null) {
            ParseFile imgFile = plant.getParseFile("image");
            String imgUrl = imgFile.getUrl();
            Glide.with(AddPlantActivity.this).load(imgUrl).into(imgPlant);
        }
        if (plant.getString("name") != null) {
            String name = plant.getString("name");
            editTextPlantName.setText(name);
        }
        if (plant.getString("description") != null) {
            String description = plant.getString("description");
            editTextPlantDescription.setText(description);
        }
        if (plant.getString("waterTime") != null) {
            String waterTime = plant.getString("waterTime");
            editTextTime.setText(waterTime);
        }
        if (plant.getParseObject("waterDevice") != null) {
            ParseObject waterDevice = plant.getParseObject("waterDevice");
            if (waterDevice.getString("ip") != null) {
                String waterDeviceID = waterDevice.getString("ip");
                editTextDeviceID.setText(waterDeviceID);
            }
            if (waterDevice.getString("password") != null) {
                String waterDevicePassword = waterDevice.getString("password");
                editTextDevicePassword.setText(waterDevicePassword);
            }
        }
        if (plant.getList("wateringDays") != null) {
            List<Boolean> wateringDays = plant.getList("wateringDays");
            AddPlantActivity.this.wateringDays = wateringDays.toArray(new Boolean[wateringDays.size()]);
            for (int i = 0; i < wateringDays.size(); i++) {
                if (wateringDays.get(i)) {
                    btnsWateringDays[i].setBackgroundColor(ContextCompat.getColor(AddPlantActivity.this, R.color.colorPrimary));
                    btnsWateringDays[i].setTextColor(ContextCompat.getColor(AddPlantActivity.this, R.color.white));
                }
                else {
                    btnsWateringDays[i].setBackgroundColor(ContextCompat.getColor(AddPlantActivity.this, R.color.gray));
                    btnsWateringDays[i].setTextColor(ContextCompat.getColor(AddPlantActivity.this, R.color.black));
                }
            }
        }
    }
}

package mx.com.magoo.waterme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlantActivity extends AppCompatActivity {

    private CircleImageView imgPlant;
    private TextView txtPlantName, txtPlantDescription, txtDeviceID, txtWateringDays;
    private Button btnEdit, btnErase, btnWater;

    ParseObject plant = null;

    String daysNames[] = {"Lun", "Mar", "Mie", "Jue", "Vie", "Sáb", "Dom"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant);
        plant = getPlantFromApp();
        getWidgets();
        setPlantInformation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ParseObject plantAux = getPlantFromApp();
        if (plantAux != null) {
            plant = plantAux;
            getWidgets();
            setPlantInformation();
        }
    }

    public ParseObject getPlantFromApp() {
        WaterMe app = (WaterMe) getApplication();
        ParseObject plant = app.plant;
        return plant;
    }

    public void getWidgets() {
        imgPlant = (CircleImageView) findViewById(R.id.imgPlant);
        txtPlantName = (TextView) findViewById(R.id.txtPlantName);
        txtPlantDescription = (TextView) findViewById(R.id.txtPlantDescription);
        txtDeviceID = (TextView) findViewById(R.id.txtDeviceID);
        txtWateringDays = (TextView) findViewById(R.id.txtWateringDays);
        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnErase = (Button) findViewById(R.id.btnErase);
        btnWater = (Button) findViewById(R.id.btnWater);
    }

    public void setPlantInformation() {
        if (plant.getParseFile("image") != null) {
            ParseFile imgFile = plant.getParseFile("image");
            String imgUrl = imgFile.getUrl();
            Glide.with(PlantActivity.this).load(imgUrl).into(imgPlant);
        }
        if (plant.getString("name") != null) {
            String name = plant.getString("name");
            txtPlantName.setText(name);
        }
        if (plant.getString("description") != null) {
            String description = plant.getString("description");
            txtPlantDescription.setText(description);
        }
        if (plant.getParseObject("waterDevice") != null) {
            ParseObject waterDevice = plant.getParseObject("waterDevice");
            if (waterDevice.getString("ip") != null) {
                String waterDeviceID = waterDevice.getString("ip");
                txtDeviceID.setText("Dispositivo: " + waterDeviceID);
            }
        }
        if (plant.getList("wateringDays") != null) {
            List<Boolean> wateringDays = plant.getList("wateringDays");
            String wateringDaysString = "";
            boolean firstDay = true;
            for (int i = 0; i < wateringDays.size(); i++) {
                if (wateringDays.get(i)) {
                    wateringDaysString += (firstDay ? daysNames[i] : (", " + daysNames[i]));
                    if (firstDay)
                        firstDay = false;
                }
            }
            txtWateringDays.setText("Regar: " + wateringDaysString);
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaterMe app = (WaterMe) getApplication();
                app.plant = PlantActivity.this.plant;
                Intent intent = new Intent(PlantActivity.this, AddPlantActivity.class);
                startActivity(intent);
            }
        });
        btnErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}

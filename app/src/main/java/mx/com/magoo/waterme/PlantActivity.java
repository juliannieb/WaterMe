package mx.com.magoo.waterme;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlantActivity extends AppCompatActivity {

    private CircleImageView imgPlant;
    private TextView txtPlantName, txtPlantDescription, txtPlantTime, txtDeviceID, txtWateringDays;
    private Button btnEdit, btnErase, btnWater;
    ProgressDialog progressDialog;

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
        txtPlantTime = (TextView) findViewById(R.id.txtPlantTime);
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
        if (plant.getString("waterTime") != null) {
            String waterTime = plant.getString("waterTime");
            txtPlantTime.setText("Tiempo de riego (segundos): " + waterTime);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(PlantActivity.this);
                builder.setMessage("¿Está seguro que desea eliminar la planta?");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        progressDialog = ProgressDialog.show(PlantActivity.this, "Cargando...",
                                "Cargando, espere por favor...", false, false);
                        plant.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                progressDialog.cancel();
                                progressDialog = null;
                                PlantActivity.this.finish();
                            }
                        });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        btnWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (plant.getParseObject("waterDevice") != null) {
                    ParseObject waterDevice = plant.getParseObject("waterDevice");
                    String deviceID = waterDevice.getString("ip");
                    String waterTime = "0";
                    if (plant.getString("waterTime") != null && !plant.getString("waterTime").equals("")) {
                        waterTime = plant.getString("waterTime");
                    }
                    String url = "http://" + deviceID + "/?time=" + waterTime;
                    new WaterTask().execute(url);
                }
            }
        });
    }

    public class WaterTask extends AsyncTask<String, Void, String> {
        private static final String TAG = "BackgroundTask";

        @Override
        protected String doInBackground(String... ulr) {
            Response response = null;
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    //.header("Authorization", token)
                    .url(ulr[0])
                    .build();

            try {
                response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            //Log.i("OkHttp :", result);
            Toast.makeText(PlantActivity.this, result, Toast.LENGTH_LONG).show();
        }

    }

}

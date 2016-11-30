package mx.com.magoo.waterme;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyPlantsActivity extends AppCompatActivity {

    private ListView listViewMyPlants;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plants);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Mis plantas");
        toolbar.setTitleTextColor(ContextCompat.getColor(MyPlantsActivity.this, R.color.white));

        getWidgets();
        setWidgetsFunctionalities();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getPlantFromApp() != null) {
            queryPlants();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_plants, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_plant) {
            Intent intent = new Intent(MyPlantsActivity.this, AddPlantActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_profile) {
            Intent intent = new Intent(MyPlantsActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getWidgets() {
        this.listViewMyPlants = (ListView) findViewById(R.id.listViewMyPlants);
    }

    public void setWidgetsFunctionalities() {
        queryPlants();
    }

    public void queryPlants() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Plant");
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            query.whereEqualTo("user", currentUser);
        }
        query.orderByAscending("name");
        query.include("waterDevice");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    setPlantsList(objects);
                } else {
                    new Utils().showSimpleAlertDialog(MyPlantsActivity.this, "Ocurrió un error, por favor inténtalo de nuevo.", "OK");
                }
            }
        });
    }

    public void setPlantsList(final List<ParseObject> plants) {
        MyPlantsAdapter myPlantsAdapter = new MyPlantsAdapter(MyPlantsActivity.this, R.layout.my_plants_list_item, plants);
        this.listViewMyPlants.setAdapter(myPlantsAdapter);
        this.listViewMyPlants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WaterMe app = (WaterMe) getApplication();
                app.plant = plants.get(position);
                Intent intent = new Intent(MyPlantsActivity.this, PlantActivity.class);
                startActivity(intent);
            }
        });
    }

    public ParseObject getPlantFromApp() {
        WaterMe app = (WaterMe) getApplication();
        ParseObject plant = app.plant;
        app.plant = null;
        return plant;
    }

}

package mx.com.magoo.waterme;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyPlantsActivity extends AppCompatActivity {

    ListView listViewMyPlants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plants);
        getWidgets();
        setWidgetsFunctionalities();
    }

    public void getWidgets() {
        this.listViewMyPlants = (ListView) findViewById(R.id.listViewMyPlants);
    }

    public void setWidgetsFunctionalities() {
        setListView();
    }

    public void setListView() {
        String plants[] = new String [10];
        plants[0] = "Plant 1";
        plants[1] = "Plant 2";
        plants[2] = "Plant 3";
        plants[3] = "Plant 4";
        plants[4] = "Plant 5";
        plants[5] = "Plant 6";
        plants[6] = "Plant 7";
        plants[7] = "Plant 8";
        plants[8] = "Plant 9";
        plants[9] = "Plant 10";
        MyPlantsAdapter myPlantsAdapter = new MyPlantsAdapter(MyPlantsActivity.this, R.layout.my_plants_list_item, plants);
        this.listViewMyPlants.setAdapter(myPlantsAdapter);
        this.listViewMyPlants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new WaterTask().execute("http://10.25.49.234/?time=5");
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
            Toast.makeText(MyPlantsActivity.this, result, Toast.LENGTH_LONG).show();
        }

    }
}

package mx.com.magoo.waterme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

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
    }
}

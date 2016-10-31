package mx.com.magoo.waterme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by julian on 30/10/16.
 */
public class MyPlantsAdapter extends ArrayAdapter<String> {

    Context context;
    int resource;
    String objects[];

    public MyPlantsAdapter(Context context, int resource, String objects[]) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlantHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);

            holder = new PlantHolder();
            //holder.imagen = (ImageView) convertView.findViewById(R.id.imgPromotion);
            holder.txtPlantName = (TextView) convertView.findViewById(R.id.txtPlantName);
            convertView.setTag(holder);
        } else {
            holder = (PlantHolder) convertView.getTag();
        }

        String plant = objects[position];

        if (plant != null) {
            holder.txtPlantName.setText(plant);
        }

        return convertView;
    }

    private static class PlantHolder {
        ImageView imgPlant;
        TextView txtPlantName;
    }
}

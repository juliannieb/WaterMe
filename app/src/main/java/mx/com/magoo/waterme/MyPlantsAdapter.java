package mx.com.magoo.waterme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by julian on 30/10/16.
 */
public class MyPlantsAdapter extends ArrayAdapter<ParseObject> {

    Context context;
    int resource;
    List<ParseObject> objects;
    String daysNames[] = {"Lun", "Mar", "Mie", "Jue", "Vie", "Sáb", "Dom"};

    public MyPlantsAdapter(Context context, int resource, List<ParseObject> objects) {
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
            holder.imgPlant = (CircleImageView) convertView.findViewById(R.id.imgPlant);
            holder.txtPlantName = (TextView) convertView.findViewById(R.id.txtPlantName);
            holder.txtWateringDays = (TextView) convertView.findViewById(R.id.txtWateringDays);
            convertView.setTag(holder);
        } else {
            holder = (PlantHolder) convertView.getTag();
        }

        ParseObject plant = objects.get(position);

        if (plant.getParseFile("image") != null) {
            ParseFile imgFile = plant.getParseFile("image");
            String imgUrl = imgFile.getUrl();
            Glide.with(context).load(imgUrl).into(holder.imgPlant);
        }
        if (plant.getString("name") != null) {
            String name = plant.getString("name");
            holder.txtPlantName.setText(name);
        }
        if (plant.getList("wateringDays") != null) {
            List<Boolean> days = plant.getList("wateringDays");
            String wateringDays = "";
            boolean firstDay = true;
            for (int i = 0; i < days.size(); i++) {
                if (days.get(i)) {
                    wateringDays += (firstDay ? daysNames[i] : (", " + daysNames[i]));
                    if (firstDay)
                        firstDay = false;
                }
            }
            holder.txtWateringDays.setText("Regar: " + wateringDays);
        }

        return convertView;
    }

    private static class PlantHolder {
        CircleImageView imgPlant;
        TextView txtPlantName, txtWateringDays;
    }
}

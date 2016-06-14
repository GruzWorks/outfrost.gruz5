package outfrost.gruz5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

public class ShowOutletPointMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_outlet_point_map);
        TextView textView1 = (TextView)findViewById(R.id.textView1);
        ImageView imageView1 = (ImageView)findViewById(R.id.imageView1);

        Intent intent = getIntent();
        long id = intent.getLongExtra("id", 0);
        LatLng location = intent.getParcelableExtra("location");
        if (intent.hasExtra("id_sub") && intent.hasExtra("place") && intent.hasExtra("flags")) {
            long id_sub = intent.getLongExtra("id_sub", 0);
            String place = intent.getStringExtra("place");
            long flags = intent.getLongExtra("flags", 0);
            if (intent.hasExtra("floor")) {
                int floor = intent.getIntExtra("floor", 0);
                if (intent.hasExtra("name")) {
                    String name = intent.getStringExtra("name");
                    textView1.setText(name + "\n"
                            + place + "\n"
                            + location.toString() + "\n"
                            + floor + " floor");
                }
                else {
                    textView1.setText(place + "\n"
                            + location.toString() + "\n"
                            + floor + " floor");
                }
				String idStr = Long.toHexString(id).toLowerCase(Locale.ROOT);
				String id_subStr = Long.toHexString(id_sub).toLowerCase(Locale.ROOT);
				String padding = "0000000000000000";
				String resName = "outlets_" + padding.substring(idStr.length()) + idStr + "_" + ((id_subStr.length() > 1) ? padding.substring(id_subStr.length())+id_subStr : id_subStr);
				int resId = getResources().getIdentifier(resName, "drawable", getPackageName());
                imageView1.setImageResource(resId);
				Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
            }
            else {
                String name = intent.getStringExtra("name");
                textView1.setText(name + "\n"
                        + place + "\n"
                        + location.toString());
            }
        }
    }
}

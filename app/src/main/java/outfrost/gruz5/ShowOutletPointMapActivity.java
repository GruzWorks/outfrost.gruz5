package outfrost.gruz5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ShowOutletPointMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String titleOfClickedMarker = intent.getStringExtra("title");
        setContentView(R.layout.activity_show_outlet_point_map);
        TextView textView1 = (TextView)findViewById(R.id.textView1);
        textView1.setText(titleOfClickedMarker);//in the same way we can set path to the image with map of this exact outletpoint
    }
}

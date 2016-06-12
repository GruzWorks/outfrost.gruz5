package outfrost.gruz5;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Outfrost on 10.06.2016.
 */
public class OutletPointSingle extends OutletPoint {

	private int floor;
	private String name;

	public OutletPointSingle(long id, LatLng location, long id_sub, String place, long flags, int floor, String name) {
		super(id, location, id_sub, place, flags);
		this.floor = floor;
		this.name = name;
	}

	public int getFloor() {
		return floor;
	}

	public String getName() {
		return name;
	}

	@Override
	protected void draw(GoogleMap map) {
		map.addMarker(new MarkerOptions()
				.position(this.getLocation())
				.title(this.getName()));
	}

	@Override
	protected void click(Context context) {
		Toast.makeText(context, this.getName(), Toast.LENGTH_SHORT).show();	// PLACEHOLDER
	}
}

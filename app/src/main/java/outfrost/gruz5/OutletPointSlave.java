package outfrost.gruz5;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Outfrost on 10.06.2016.
 */
public class OutletPointSlave extends OutletPoint {

	private int floor;

	public OutletPointSlave(long id, LatLng location, long id_sub, String place, long flags, int floor) {
		super(id, location, id_sub, place, flags);
		this.floor = floor;
	}

	public int getFloor() {
		return floor;
	}


	@Override
	protected void draw(GoogleMap map) {
		map.addMarker(new MarkerOptions()
				.position(this.getLocation())
				.title(this.getPlace()));
	}

	@Override
	protected void click(Context context) {
		Toast.makeText(context, this.getPlace(), Toast.LENGTH_SHORT).show();	// PLACEHOLDER
	}
}

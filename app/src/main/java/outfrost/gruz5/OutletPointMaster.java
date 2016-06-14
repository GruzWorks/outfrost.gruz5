package outfrost.gruz5;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Outfrost on 10.06.2016.
 */
public class OutletPointMaster extends OutletPoint {

	private String name;

	public OutletPointMaster(long id, LatLng location, long id_sub, String place, long flags, String name) {
		super(id, location, id_sub, place, flags);
		this.name = name;
	}

	public String getName() {
		return name;
	}


	@Override
	protected String draw(GoogleMap map) {
		return map.addMarker(new MarkerOptions()
				.position(this.getLocation())
				.title(this.getName())
				.visible(true))
				.getId();
	}

	@Override
	protected void click(Context context) {
		Toast.makeText(context, this.getName(), Toast.LENGTH_SHORT).show();	// PLACEHOLDER
	}
}

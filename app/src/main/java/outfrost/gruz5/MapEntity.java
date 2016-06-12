package outfrost.gruz5;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Outfrost on 10.06.2016.
 */
public abstract class MapEntity {

	public final long id;
	private LatLng location;

	public MapEntity(long id, LatLng location) {
		this.id = id;
		this.location = location;
	}

	public LatLng getLocation() {
		return location;
	}

	protected abstract void draw(GoogleMap map);
	protected abstract void click(Context context);
}

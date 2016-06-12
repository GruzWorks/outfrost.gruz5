package outfrost.gruz5;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Outfrost on 10.06.2016.
 */
public abstract class OutletPoint extends MapEntity {

	public final long id_sub;
	private String place;
	private long flags;

	public OutletPoint(long id, LatLng location, long id_sub, String place, long flags) {
		super(id, location);
		this.id_sub = id_sub;
		this.place = place;
		this.flags = flags;
	}

	public String getPlace() {
		return place;
	}

	public boolean hasFlags(OutletPointFlags flags) {
		return OutletPointFlags.hasFlags(this.flags, flags);
	}
}

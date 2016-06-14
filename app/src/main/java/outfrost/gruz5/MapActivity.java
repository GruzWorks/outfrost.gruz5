package outfrost.gruz5;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback,
		LocationSource.OnLocationChangedListener, GoogleApiClient.ConnectionCallbacks, GoogleMap.OnCameraChangeListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

	private GoogleMap mMap;
	private GoogleApiClient mGoogleApiClient;
	private MapEntityStore mapEntityStore;

	private static long mapEntityTypes = MapEntityTypes.OUTLET_POINT.integerValue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
		if (mGoogleApiClient == null)
			mGoogleApiClient = new GoogleApiClient.Builder(this)
					.addConnectionCallbacks(this)
					.addApi(LocationServices.API)
					.build();
		mGoogleApiClient.connect();
		if (mapEntityStore == null)
			mapEntityStore = new MapEntityStore(mapEntityTypes);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		mMap.setMyLocationEnabled(true);
		mMap.setOnCameraChangeListener(this);
		mMap.setOnMarkerClickListener(this);
		mMap.setOnInfoWindowClickListener(this);
	}

	@Override
	public void onLocationChanged(Location location)
	{
		LatLng coordinates = new LatLng(location.getLatitude(), location.getLongitude());
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 14));
	}

	@Override
	public void onConnected(Bundle bundle) {
		Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		onLocationChanged(lastLocation);
	}

	@Override
	public void onConnectionSuspended(int cause) {	}

	@Override
	public void onCameraChange(CameraPosition cameraPosition) {
		mapEntityStore.UpdateMap(cameraPosition, mMap, this);
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		/*int X = 1;//you can see how it works when we open new activity on clicking the marker or on clicking the infobox
		if (X==1) {//opening the infobox and toast info of clicking marker
			CharSequence text = "You clicked the marker " + marker.getTitle();
			Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
			toast.show();
			return false;
		}
		else {//opening new activity when marker clicked
			Intent myIntent = new Intent(this,ShowOutletPointMapActivity.class);
			myIntent.putExtra("title",marker.getTitle());
			this.startActivity(myIntent);
			return true;
		}*/
		return false;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		/*Toast for clicking
		CharSequence text = "You clicked the info box of " + marker.getTitle();
		Toast toast = Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT);
		toast.show();*/
		//opening new activity
		Intent myIntent = new Intent(this,ShowOutletPointMapActivity.class);
		MapEntity entity = mapEntityStore.getEntity(marker.getId());
		myIntent.putExtra("id", entity.id)
				.putExtra("location", entity.getLocation());
		if (entity instanceof OutletPoint) {
			myIntent.putExtra("id_sub", ((OutletPoint) entity).id_sub)
					.putExtra("place", ((OutletPoint) entity).getPlace())
					.putExtra("flags", ((OutletPoint) entity).getFlags());
			if (entity instanceof OutletPointSingle)
				myIntent.putExtra("floor", ((OutletPointSingle) entity).getFloor())
						.putExtra("name", ((OutletPointSingle) entity).getName());
			else if (entity instanceof OutletPointMaster)
				myIntent.putExtra("name", ((OutletPointMaster) entity).getName());
			else if (entity instanceof OutletPointSlave)
				myIntent.putExtra("floor", ((OutletPointSlave) entity).getFloor());
		}
		this.startActivity(myIntent);
		//if we decide to use it this way we should add some kind of 'Click here fo details/map' under the name in the infobox
	}
}

package outfrost.gruz5;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback,
		LocationSource.OnLocationChangedListener, GoogleApiClient.ConnectionCallbacks, GoogleMap.OnCameraChangeListener {

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
}

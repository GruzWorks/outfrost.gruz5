package outfrost.gruz5;

import android.content.Context;
import android.graphics.Point;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Outfrost on 10.06.2016.
 */
public class MapEntityStore {

	private static int RADIUS_SECTORS = 1;		// Will not work correctly if < 1
	private static double SECTOR_PADDING = 0.1;	// Will not work correctly if >= 0.5

	private Point currentSector;
	private Map<String, MapEntity> mapEntities;
	private long entityTypes;

	public MapEntityStore(long entityTypes) {
		this.mapEntities = new LinkedHashMap<>();
		this.entityTypes = entityTypes;
	}

	public MapEntity getEntity(String markerId) {
		return mapEntities.get(markerId);
	}

	public void UpdateMap(CameraPosition cameraPosition, GoogleMap map, Context context) {
		boolean reloadRequired = false;
		LatLng cameraTarget = cameraPosition.target;
		if (currentSector != null) {
			while (currentSector.y > (-90 + RADIUS_SECTORS) && cameraTarget.latitude <= ((double) currentSector.y - (double) RADIUS_SECTORS + SECTOR_PADDING)) {
				currentSector.y -= RADIUS_SECTORS + 1;
				reloadRequired = true;
			}
			while (currentSector.y < (89 - RADIUS_SECTORS) && cameraTarget.latitude >= ((double) currentSector.y + 1.0 + (double) RADIUS_SECTORS - SECTOR_PADDING)) {
				currentSector.y += RADIUS_SECTORS + 1;
				reloadRequired = true;
			}
			while (cameraTarget.longitude <= ((double) currentSector.x - (double) RADIUS_SECTORS + SECTOR_PADDING) && cameraTarget.longitude >= ((double) getSectorLngInRange(currentSector.x - 180) + 0.5)) {
				currentSector.x -= RADIUS_SECTORS + 1;
				currentSector.x = getSectorLngInRange(currentSector.x);
				reloadRequired = true;
			}
			while (cameraTarget.longitude >= ((double) currentSector.x + 1.0 + (double) RADIUS_SECTORS - SECTOR_PADDING) && cameraTarget.longitude <= ((double) getSectorLngInRange(currentSector.x + 180) + 0.5)) {
				currentSector.x += RADIUS_SECTORS + 1;
				currentSector.x = getSectorLngInRange(currentSector.x);
				reloadRequired = true;
			}
		}
		else {
			currentSector = new Point((int) Math.floor(cameraTarget.longitude), (int) Math.floor(cameraTarget.latitude));
			reloadRequired = true;
		}
		if (reloadRequired) {
			mapEntities = new LinkedHashMap<>();
			map.clear();
			for (Map.Entry<MapEntityTypes, String> entityType : MapEntityTypes.fileSuffixes.entrySet()) {
				if (MapEntityTypes.includesEntitiesOfType(this.entityTypes, entityType.getKey())) {
					AddEntitiesToMapAndRender(entityType.getKey(), map, context);
				}
			}
		}
	}

	private void AddEntitiesToMapAndRender(MapEntityTypes type, GoogleMap map, Context context) {
		List<String> lines = LoadEntities(MapEntityTypes.fileSuffixes.get(type), context);

		if (type == MapEntityTypes.OUTLET_POINT) {
			for (String line : lines) {

				if (line != null) {
					String[] outletPointInfo = line.split("\t");
					long id = Long.parseLong(outletPointInfo[0], 16);
					long id_sub = Long.parseLong(outletPointInfo[1], 16);
					LatLng location = new LatLng(Double.parseDouble(outletPointInfo[2]), Double.parseDouble(outletPointInfo[3]));
					String name = outletPointInfo[4];
					String place = outletPointInfo[5];
					int floor = Integer.parseInt(outletPointInfo[6]);
					long flags = Long.parseLong(outletPointInfo[7], 16);

					MapEntity entity;
					String key;
					if (id_sub == 0) {
						if (OutletPointFlags.hasFlags(flags, OutletPointFlags.HAS_SLAVES))
							key = (entity = new OutletPointMaster(id, location, id_sub, place, flags, name)).draw(map);
						else
							key = (entity = new OutletPointSingle(id, location, id_sub, place, flags, floor, name)).draw(map);
					}
					else
						key = (entity = new OutletPointSlave(id, location, id_sub, place, flags, floor)).draw(map);

					mapEntities.put(key, entity);
				}
			}
		}
	}

	private List<String> LoadEntities(String fileSuffix, Context context) {
		List<String> lines = new ArrayList<>();
		for (int i = currentSector.y - RADIUS_SECTORS; i <= currentSector.y + RADIUS_SECTORS; i++) {
			if (i >= -90 && i < 90) {
				for (int k = currentSector.x - RADIUS_SECTORS; k <= currentSector.x + RADIUS_SECTORS; k++) {
					int j = getSectorLngInRange(k);
					String lat = String.format(Locale.ROOT, (i < 0) ? "s%1$03d" : "n%1$03d", i);
					String lon = String.format(Locale.ROOT, (j < 0) ? "w%1$03d" : "e%1$03d", j);
					int rawResourceId = context.getResources().getIdentifier(lat + lon + fileSuffix, "raw", context.getPackageName());
					if (rawResourceId != 0) {
						BufferedReader dataReader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(rawResourceId)));
						try {
							while (dataReader.ready()) {
								lines.add(dataReader.readLine());
							}
						}
						catch (IOException e) {
							Toast.makeText(context, context.getText(R.string.activity_map_entity_loading_error), Toast.LENGTH_LONG).show();
						}
					}
				}
			}
		}
		return lines;
	}

	private int getSectorLngInRange(int longitude) {
		while (longitude < -180) longitude += 360;
		while (longitude >= 180) longitude -= 360;
		return longitude;
	}
}

package outfrost.gruz5;

import java.util.EnumMap;

/**
 * Created by Outfrost on 11.06.2016.
 */
public enum MapEntityTypes {

	OUTLET_POINT(1<<0);

	long integerValue;

	MapEntityTypes(long value) { this.integerValue = value; }

	public static final EnumMap<MapEntityTypes, String> fileSuffixes = new EnumMap<MapEntityTypes, String>(MapEntityTypes.class) {{
		put(OUTLET_POINT, "outlets");
	}};

	public static boolean includesEntitiesOfType(long value, MapEntityTypes types) {
		return ((value & types.integerValue) == types.integerValue);
	}
}

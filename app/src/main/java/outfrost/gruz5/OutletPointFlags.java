package outfrost.gruz5;

/**
 * Created by Outfrost on 10.06.2016.
 */
enum OutletPointFlags {

	HAS_SLAVES(1<<0);

	long integerValue;

	OutletPointFlags(long value) {
		this.integerValue = value;
	}

	public static boolean hasFlags(long value, OutletPointFlags flags) {
		return ((value & flags.integerValue) == flags.integerValue);
	}
}

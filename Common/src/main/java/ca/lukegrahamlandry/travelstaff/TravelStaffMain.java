package ca.lukegrahamlandry.travelstaff;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TravelStaffMain {
	public static void init() {
		TravelAnchorRegistry.ITEMS.init();
	}

	public static final String MOD_ID = "travelstaff";
	public static final String MOD_NAME = "Staff of Travelling";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
}
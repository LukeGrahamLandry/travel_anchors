package ca.lukegrahamlandry.travelstaff;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

	public static final String MOD_ID = "travelstaff";
	public static final String MOD_NAME = "Staff of Travelling";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);


	public static ResourceLocation TELEPORT_STAFF_KEY = new ResourceLocation(MOD_ID, "travel_staff");
	public static ResourceLocation TRAVEL_ANCHOR_KEY = new ResourceLocation(MOD_ID, "travel_anchor");

	public static Block getTravelAnchor(){
		return Registry.BLOCK.get(TRAVEL_ANCHOR_KEY);
	}
}
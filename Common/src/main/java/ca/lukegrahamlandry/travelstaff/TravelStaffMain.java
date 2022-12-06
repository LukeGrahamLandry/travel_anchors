package ca.lukegrahamlandry.travelstaff;

import ca.lukegrahamlandry.lib.config.ConfigWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class TravelStaffMain {
	public static final String MOD_ID = "travelstaff";

	public static ConfigWrapper<ClientConfigData> CLIENT_CONFIG = ConfigWrapper.client(ClientConfigData.class).named(MOD_ID);
	public static ConfigWrapper<SyncedConfigData> CONFIG = ConfigWrapper.synced(SyncedConfigData.class).named(MOD_ID);

	public static void init() {
		TravelAnchorRegistry.ITEMS.init();
	}

	public static final String MOD_NAME = "Staff of Travelling";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

	public static final class ClientConfigData {
		public boolean isElevatorMode = true;
	}

	public static final class SyncedConfigData {
		public int maxTeleportDistance = 64;
		public int shortTeleportDistance = 7;
		public int staffCooldownTicks = 20;
		public float rangeEnchantScaling = 0.5F;
		public double maxAngle = 30;
	}
}
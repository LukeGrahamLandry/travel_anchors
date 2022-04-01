package ca.lukegrahamlandry.travelstaff.platform.services;

public interface IConfigHelper {
    // The maximum angle you can look at the Travel Anchor to teleport.
    default double getMaxAngle(){
        return 30;
    }

    // The maximum distance you are allowed to teleport
    default double getMaxDistance(){
        return 64;
    }

    /*
    "When this is set, you will be able to use the elevation feature of travel anchors",
            "but you'll wont teleport to the anchor you're looking at when jumping on another travel anchor",
            "This is a client option so each player can adjust it as they prefer."
     */
    default boolean isElevatorMode(){
        return true;
    }

    // when not using travel anchors the staff has a cooldown
    default int getStaffCooldown(){
        return 20;
    }

    default double getShortTeleportDistance(){
        return 7;
    }
}

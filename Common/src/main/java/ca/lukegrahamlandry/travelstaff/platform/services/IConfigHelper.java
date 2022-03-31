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
    "When this is set, wou won't be able to use the elevation feature of travel anchors",
            "but you'll teleport to the anchor you're looking at when jumping on another travel anchor",
            "This is a client option so each player can adjust it as they prefer."
     */
    default boolean disableElevation(){
        return false;
    }
}

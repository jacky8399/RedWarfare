package me.libraryaddict.gearwars;

import me.libraryaddict.gearwars.managers.*;

public class GearApi {
    private static AbilityManager abilityManager;
    private static BorderManager borderManager;
    private static ChestManager chestManager;
    private static GameManager gameManager;
    private static InventoryManager inventoryManager;
    private static KitManager kitManager;
    private static GearWars mainPlugin;
    private static PlayerManager playerManager;

    public static AbilityManager getAbilityManager() {
        if (abilityManager == null) {
            abilityManager = new AbilityManager();
        }
        return abilityManager;
    }

    public static BorderManager getBorderManager() {
        if (borderManager == null) {
            borderManager = new BorderManager();
        }
        return borderManager;
    }

    public static ChestManager getChestManager() {
        if (chestManager == null) {
            chestManager = new ChestManager();
        }
        return chestManager;
    }

    public static GameManager getGameManager() {
        if (gameManager == null) {
            gameManager = new GameManager();
        }
        return gameManager;
    }

    public static InventoryManager getInventoryManager() {
        if (inventoryManager == null) {
            inventoryManager = new InventoryManager();
        }
        return inventoryManager;
    }

    public static KitManager getKitManager() {
        if (kitManager == null) {
            kitManager = new KitManager();
        }
        return kitManager;
    }

    public static GearWars getMainPlugin() {
        return mainPlugin;
    }

    public static PlayerManager getPlayerManager() {
        if (playerManager == null) {
            playerManager = new PlayerManager();
        }
        return playerManager;
    }

    protected static void init(GearWars gearWars) {
        mainPlugin = gearWars;
    }
}

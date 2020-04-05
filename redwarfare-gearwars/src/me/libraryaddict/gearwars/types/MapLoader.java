package me.libraryaddict.gearwars.types;

import me.libraryaddict.core.utils.UtilFile;
import me.libraryaddict.gearwars.GearApi;
import net.lingala.zip4j.core.ZipFile;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.Random;

public class MapLoader {

    public static void clear(File file) {
        if (!file.exists())
            return;
        if (file.isFile()) {
            file.delete();
        } else {
            for (File f : file.listFiles())
                clear(f);
            file.delete();
        }
    }

    public static void loadMap() {
        File[] mapFiles = new File(UtilFile.getUpdateFolder(), "Maps/GearWars/").listFiles();
        if (mapFiles != null && mapFiles.length > 0) {

            loadMap(mapFiles[new Random().nextInt(mapFiles.length)], new File("world"));
            File configFile = new File("world/config.yml");

            if (configFile.exists()) {
                GearApi.getBorderManager()
                        .setBorderSize(YamlConfiguration.loadConfiguration(configFile).getInt("BorderSize", 500));
            } else {
                GearApi.getBorderManager().setBorderSize(500);
            }
        } else {
            System.out.print("Failed to find maps at " + mapFiles);
        }
    }

    public static void extractZip(File zip, File destination) {
        try {
            if (!zip.exists())
                throw new IllegalArgumentException(zip + " does not exist!");

            ZipFile zipFile = new ZipFile(zip);

            if (!zipFile.isValidZipFile())
                return;

            if (!destination.exists()) {
                destination.mkdirs();
            }

            zipFile.extractAll(destination.getAbsolutePath());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void loadMap(File map, File dest) {
        if (map == null || !map.exists()) {
            System.out.print("Can't load a null map. No maps to load?");
            return;
        }
        System.out.print(String.format("Now attempting to load the map from path %s", map.toString()));
        clear(dest);

        extractZip(map, dest);
        System.out.print(String.format("Successfully loaded map %s", map.getName()));
    }
}

package com.ts;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;


public class Main {

    public static void main(String[] args) throws IOException {
	    // java KeyCollector settings.txt

        // Read Settings.txt
        FileInputStream fstream = new FileInputStream("Settings.txt");
        Properties prop = new Properties();
        prop.load(fstream);

        String PathToSrv = prop.getProperty("SERVER_FOLDER_PATH");
        String PathToMods = prop.getProperty("MODS_FOLDER_PATH");
        String PathToModlistFile = prop.getProperty("MODLIST");
        List<String> SubFolders = Arrays.asList( prop.getProperty("KEY_SUBFOLDERS").split(",") );
        fstream.close();

        System.out.println(" -------- tS Mod Keys Collector -------- ");
        System.out.println ("SRV folder: ".concat(PathToSrv));
        System.out.println ("Mods folder: ".concat(PathToMods));
        System.out.println ("Modlist file: ".concat(PathToModlistFile));

        // Read Modslist file
        Properties modlist = new Properties();
        modlist.load(new FileInputStream(PathToModlistFile));
        Collection<Object> modsFolders = modlist.values();

        System.out.println ("Modlist: ".concat(modsFolders.toString()));

        // Copying
        File srvFolder = new File (PathToSrv);

        if (!srvFolder.exists()) {
            System.out.println("Check Path! Destination: " + srvFolder.toString());
            System.exit(0);
        }

        for (Object modFolderItem : modsFolders) {
            System.out.println ("");
            System.out.println ("Mod ".concat(modFolderItem.toString()));

            File modFolder = getKeyFolder(PathToMods, modFolderItem.toString(), SubFolders);
            copyFolder(modFolder, srvFolder);
        }

        System.out.println("");
        System.out.println("####################");
        System.out.println("All keys were copied!");
    }

    public static void copyFolder(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            dest.mkdir();
            System.out.println("Directory copied from " + src + " to " + dest);

            String files[] = src.list();

            for (String file : files) {
                if (file.contains(".bikey")) {
                    File srcFile = new File(src, file);
                    File destFile = new File(dest, file);
                    copyFolder(srcFile, destFile);
                }
            }
        } else {
            if (!src.exists()) {
                System.out.println("Check Path! Source: " + src.toString());
                System.exit(0);
            }

            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];
            int lenght;
            while ( (lenght = in.read(buffer)) > 0 ) {
                out.write(buffer, 0, lenght);
            }

            in.close();
            out.close();
            System.out.println("  > File copied from " + src);
        }
    }

    public static File getKeyFolder(String path, String mod, List<String> subFolders) {
        File modFolder = new File("");

        for (String subFolder : subFolders) {
            modFolder = new File( path
                                    .concat("\\").concat(mod)
                                    .concat("\\").concat(subFolder)
            );

            if (modFolder.exists()) {
                return modFolder;
            }
        }

        System.out.println("No Keys folder");
        return modFolder;
    }

}

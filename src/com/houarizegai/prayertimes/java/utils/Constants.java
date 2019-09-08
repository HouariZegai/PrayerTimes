package com.houarizegai.prayertimes.java.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Constants {
    /* Algeria cities */
    public static final String[] DZ_CITIES = {"Adrar", "Chlef", "Laghouat", "Oum El Bouaghi", "Batna", "Béjaïa",
            "Biskra", "Béchar", "Blida", "Bouira", "Tamanghasset", "Tébessa", "Tlemcen", "Tiaret", "Tizi Ouzou",
            "Algiers", "Djelfa", "Jijel", "Sétif", "Saïda", "Skikda", "Sidi Bel Abbès", "Annaba", "Guelma",
            "Constantine", "Médéa", "Mostaganem", "M'Sila", "Mascara", "Ouargla", "Oran", "El Bayadh", "Illizi",
            "Bordj Bou Arreridj", "Boumerdas", "El Tarf", "Tinduf", "Tissemsilt", "El Oued", "Khenchela",
            "Souk Ahras", "Tipasa", "Mila", "Aïn Defla", "Naâma", "Aïn Témouchent", "Ghardaïa", "Relizane"};

    public static final String ADHAN_PATH;

    static {
        // Get Path of Project
        Path currentRelativePath = Paths.get("");
        // convert the path to absolute
        String currentAbsolutePath = currentRelativePath.toAbsolutePath().toString();

        ADHAN_PATH = currentAbsolutePath + "\\src\\com\\houarizegai\\prayertimes\\resources\\adan\\";
    }
}

package de.imolli.mywarp.utils;

import java.util.ArrayList;
import java.util.Arrays;

public class SimpleLore {

    private ArrayList<String> lore;

    public SimpleLore(String loreRaw) {

        lore = new ArrayList<>();
        String[] loreStrings = loreRaw.split("\n");

        lore.addAll(Arrays.asList(loreStrings));

    }

    public ArrayList<String> getLore() {
        return lore;
    }

}

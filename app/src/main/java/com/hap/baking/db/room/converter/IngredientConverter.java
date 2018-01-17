package com.hap.baking.db.room.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.baking.db.room.entity.Ingredient;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by luis on 12/14/17.
 */

public class IngredientConverter {
    private static final Gson gson = new Gson();

    @TypeConverter
    public static ArrayList<Ingredient> fromString(final String string) {
        final Type listType = new TypeToken<ArrayList<Ingredient>>() {
        }.getType();
        return gson.fromJson(string, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Ingredient> ingredients) {
        return gson.toJson(ingredients);
    }
}

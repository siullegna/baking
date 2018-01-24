package com.hap.baking.db.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.hap.baking.BakingApplication;
import com.hap.baking.db.DatabaseInfo;
import com.hap.baking.db.room.converter.IngredientConverter;
import com.hap.baking.db.room.converter.StepConverter;
import com.hap.baking.db.room.dao.RecipeDao;
import com.hap.baking.db.room.entity.Recipe;

/**
 * Created by luis on 12/14/17.
 */
@Database(entities = {Recipe.class}, version = DatabaseInfo.DB_VERSION, exportSchema = false)
@TypeConverters({IngredientConverter.class, StepConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract RecipeDao recipeDao();

    public static AppDatabase getInstance() {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(BakingApplication.getInstance().getApplicationContext(), AppDatabase.class, DatabaseInfo.DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        }

        return INSTANCE;
    }
}

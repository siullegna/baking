package com.hap.baking.db.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.hap.baking.db.RecipeContract;
import com.hap.baking.db.room.converter.IngredientConverter;
import com.hap.baking.db.room.converter.StepConverter;

import java.util.ArrayList;

/**
 * Created by luis on 12/4/17.
 */
@Entity(tableName = RecipeContract.RecipeEntity.TBL_RECIPE, indices = {@Index("_id")})
public class Recipe implements Parcelable {
    @PrimaryKey
    @ColumnInfo(name = RecipeContract.RecipeEntity._ID)
    private int id;
    @ColumnInfo(name = RecipeContract.RecipeEntity.COLUMN_NAME)
    private String name;
    @ColumnInfo(name = RecipeContract.RecipeEntity.COLUMN_INGREDIENTS)
    private ArrayList<Ingredient> ingredients;
    @ColumnInfo(name = RecipeContract.RecipeEntity.COLUMN_STEPS)
    private ArrayList<Step> steps;
    @ColumnInfo(name = RecipeContract.RecipeEntity.COLUMN_SERVINGS)
    private int servings;
    @ColumnInfo(name = RecipeContract.RecipeEntity.COLUMN_IMAGE)
    private String image;
    @ColumnInfo(name = RecipeContract.RecipeEntity.COLUMN_IS_FAVORITE)
    private boolean isFavorite;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public static Recipe fromContentValues(final ContentValues contentValues) {
        final Recipe recipe = new Recipe();

        if (contentValues.containsKey(RecipeContract.RecipeEntity._ID)) {
            recipe.setId(contentValues.getAsInteger(RecipeContract.RecipeEntity._ID));
        }

        if (contentValues.containsKey(RecipeContract.RecipeEntity.COLUMN_NAME)) {
            recipe.setName(contentValues.getAsString(RecipeContract.RecipeEntity.COLUMN_NAME));
        }

        if (contentValues.containsKey(RecipeContract.RecipeEntity.COLUMN_INGREDIENTS)) {
            final String ingredients = contentValues.getAsString(RecipeContract.RecipeEntity.COLUMN_INGREDIENTS);
            recipe.setIngredients(IngredientConverter.fromString(ingredients));
        }

        if (contentValues.containsKey(RecipeContract.RecipeEntity.COLUMN_STEPS)) {
            final String steps = contentValues.getAsString(RecipeContract.RecipeEntity.COLUMN_STEPS);
            recipe.setSteps(StepConverter.fromString(steps));
        }

        if (contentValues.containsKey(RecipeContract.RecipeEntity.COLUMN_SERVINGS)) {
            recipe.setServings(contentValues.getAsInteger(RecipeContract.RecipeEntity.COLUMN_SERVINGS));
        }

        if (contentValues.containsKey(RecipeContract.RecipeEntity.COLUMN_IMAGE)) {
            recipe.setImage(contentValues.getAsString(RecipeContract.RecipeEntity.COLUMN_IMAGE));
        }

        if (contentValues.containsKey(RecipeContract.RecipeEntity.COLUMN_IS_FAVORITE)) {
            recipe.setFavorite(contentValues.getAsBoolean(RecipeContract.RecipeEntity.COLUMN_IS_FAVORITE));
        }

        return recipe;
    }

    public static Recipe fromCursor(final Cursor cursor) {
        final Recipe recipe = new Recipe();

        final int idColumn = cursor.getColumnIndex(RecipeContract.RecipeEntity._ID);
        final int nameColumn = cursor.getColumnIndex(RecipeContract.RecipeEntity.COLUMN_NAME);
        final int ingredientsColumn = cursor.getColumnIndex(RecipeContract.RecipeEntity.COLUMN_INGREDIENTS);
        final int stepsColumn = cursor.getColumnIndex(RecipeContract.RecipeEntity.COLUMN_STEPS);
        final int servingsColumn = cursor.getColumnIndex(RecipeContract.RecipeEntity.COLUMN_SERVINGS);
        final int imageColumn = cursor.getColumnIndex(RecipeContract.RecipeEntity.COLUMN_IMAGE);
        final int isFavoriteColumn = cursor.getColumnIndex(RecipeContract.RecipeEntity.COLUMN_IS_FAVORITE);

        recipe.setId(cursor.getInt(idColumn));
        recipe.setName(cursor.getString(nameColumn));
        recipe.setIngredients(IngredientConverter.fromString(cursor.getString(ingredientsColumn)));
        recipe.setSteps(StepConverter.fromString(cursor.getString(stepsColumn)));
        recipe.setServings(cursor.getInt(servingsColumn));
        recipe.setImage(cursor.getString(imageColumn));
        recipe.setFavorite(cursor.getInt(isFavoriteColumn) == 1);

        return recipe;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeTypedList(this.ingredients);
        dest.writeTypedList(this.steps);
        dest.writeInt(this.servings);
        dest.writeString(this.image);
        dest.writeByte(this.isFavorite ? (byte) 1 : (byte) 0);
    }

    public Recipe() {
    }

    protected Recipe(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        this.steps = in.createTypedArrayList(Step.CREATOR);
        this.servings = in.readInt();
        this.image = in.readString();
        this.isFavorite = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}

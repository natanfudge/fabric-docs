package net.fabricmc.example;

import com.mojang.serialization.JsonOps;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeType;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.example.recipe.ExampleRecipe;

public class ExampleMod implements ModInitializer {
    public static final RecipeType<ExampleRecipe> RECIPE_TYPE = RecipeType.register("example:recipe_type");

    @Override
    public void onInitialize() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Damage", 16);
        ItemStack output = new ItemStack(Items.GOLDEN_PICKAXE, 1);
        output.setTag(tag);
        ExampleRecipe recipe = new ExampleRecipe(Items.DIRT, Items.EGG, output);
        String json = ExampleRecipe.CODEC.encodeStart(JsonOps.INSTANCE, recipe).getOrThrow(false, System.err::println).toString();
        System.out.println(json);
    }
}

package net.fabricmc.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;

public class ExampleRecipeSerializer implements RecipeSerializer<ExampleRecipe> {
    // Define ExampleRecipeSerializer as a singleton by making its constructor private and exposing an instance.
    private ExampleRecipeSerializer() {
    }

    public static final ExampleRecipeSerializer INSTANCE = new ExampleRecipeSerializer();
    // This will be the "type" field in the json
    public static final Identifier ID = new Identifier("example:example_recipe");

    private static class ExampleRecipeJsonFormat {
        JsonObject inputA;
        JsonObject inputB;
        String outputItem;
        int outputAmount;
    }

    @Override
    public ExampleRecipe read(Identifier recipeId, JsonObject json) {
        ExampleRecipeJsonFormat recipeJson = new Gson().fromJson(json, ExampleRecipeJsonFormat.class);
        if (recipeJson.inputA == null || recipeJson.inputB == null || recipeJson.outputItem == null) {
            throw new JsonSyntaxException("A required attribute is missing!");
        }
        if (recipeJson.outputAmount == 0) recipeJson.outputAmount = 1;

        Ingredient inputA = Ingredient.fromJson(recipeJson.inputA);
        Ingredient inputB = Ingredient.fromJson(recipeJson.inputB);
        Item outputItem = Registry.ITEM.getOrEmpty(new Identifier(recipeJson.outputItem))
                .orElseThrow(() -> new JsonSyntaxException("No such item " + recipeJson.outputItem));
        ItemStack output = new ItemStack(outputItem, recipeJson.outputAmount);

        return new ExampleRecipe(inputA, inputB, output, recipeId);
    }


    @Override
    public void write(PacketByteBuf packetData, ExampleRecipe recipe) {
        recipe.getInputA().write(packetData);
        recipe.getInputB().write(packetData);
        packetData.writeItemStack(recipe.getOutput());
    }

    @Override
    public ExampleRecipe read(Identifier recipeId, PacketByteBuf packetData) {
        Ingredient inputA = Ingredient.fromPacket(packetData);
        Ingredient inputB = Ingredient.fromPacket(packetData);
        ItemStack output = packetData.readItemStack();
        return new ExampleRecipe(inputA, inputB, output, recipeId);
    }
}

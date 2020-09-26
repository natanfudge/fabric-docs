package net.fabricmc.example.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public enum ExampleRecipeSerializer implements RecipeSerializer<ExampleRecipe> {
    INSTANCE;
    @Override
    public ExampleRecipe read(Identifier id, JsonObject json) {
        return ExampleRecipe.CODEC.decode(JsonOps.INSTANCE, json).getOrThrow(false, System.err::println).getFirst();
    }

    @Override
    public ExampleRecipe read(Identifier id, PacketByteBuf buf) {
        Item input1 = Registry.ITEM.get(buf.readIdentifier());
        Item input2 = Registry.ITEM.get(buf.readIdentifier());
        ItemStack output = buf.readItemStack();
        return new ExampleRecipe(input1, input2, output);
    }

    @Override
    public void write(PacketByteBuf buf, ExampleRecipe recipe) {
        buf.writeIdentifier(Registry.ITEM.getId(recipe.getInput1()));
        buf.writeIdentifier(Registry.ITEM.getId(recipe.getInput2()));
        buf.writeItemStack(recipe.getOutput());
    }
}

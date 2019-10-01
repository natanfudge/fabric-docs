package net.fabricmc.example;

import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ExampleRecipe implements Recipe<BasicInventory> {
    private final Ingredient inputA;
    private final Ingredient inputB;
    private final ItemStack outputStack;
    private final Identifier id;

    public ExampleRecipe(Ingredient inputA, Ingredient inputB, ItemStack outputStack, Identifier id) {
        this.inputA = inputA;
        this.inputB = inputB;
        this.outputStack = outputStack;
        this.id = id;
    }

    public Ingredient getInputA() {
        return inputA;
    }

    public Ingredient getInputB() {
        return inputB;
    }

    public static class Type implements RecipeType<ExampleRecipe> {
        // Define ExampleRecipe.Type as a singleton by making its constructor private and exposing an instance.
        private Type() {
        }

        public static final Type INSTANCE = new Type();

        public static final String ID = "two_slot_recipe";
    }

    @Override
    public boolean matches(BasicInventory inventory, World world) {
        if (inventory.getInvSize() < 2) return false;
        return inputA.method_8093(inventory.getInvStack(0)) && inputB.method_8093(inventory.getInvStack(1));
    }

    @Override
    public ItemStack craft(BasicInventory var1) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean fits(int var1, int var2) {
        return false;
    }

    @Override
    public ItemStack getOutput() {
        return outputStack;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ExampleRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }
}

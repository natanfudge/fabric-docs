package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public class ExampleMod implements ModInitializer {
    public static final Identifier PLAY_PARTICLE_PACKET_ID = new Identifier("example", "particle");
    private static final MyBlock PACKETS_BLOCK = new MyBlock();
    public static final Identifier TURN_TO_DIAMOND_PACKET_ID = new Identifier("example", "diamond");

    @Override
    public void onInitialize() {
        System.out.println("Init");
        Registry.register(Registry.BLOCK, "docs:packets", PACKETS_BLOCK);
        Registry.register(Registry.ITEM, "docs:packets",
                new BlockItem(PACKETS_BLOCK, new Item.Settings().group(ItemGroup.MISC)));

        ServerSidePacketRegistry.INSTANCE.register(TURN_TO_DIAMOND_PACKET_ID, (packetContext, attachedData) -> {
            // Get the BlockPos we put earlier
            BlockPos pos = attachedData.readBlockPos();
            packetContext.getTaskQueue().execute(() -> {
                // Execute on the main thread

                // ALWAYS validate that the information received is valid in a C2S packet!
                if (packetContext.getPlayer().world.isHeightValidAndBlockLoaded(pos)) {
                    // Turn to diamond
                    packetContext.getPlayer().world.setBlockState(pos, Blocks.DIAMOND_BLOCK.getDefaultState());
                }

            });
        });

        Registry.register(Registry.RECIPE_SERIALIZER, ExampleRecipeSerializer.ID,
                ExampleRecipeSerializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, new Identifier("example", ExampleRecipe.Type.ID), ExampleRecipe.Type.INSTANCE);
    }
}

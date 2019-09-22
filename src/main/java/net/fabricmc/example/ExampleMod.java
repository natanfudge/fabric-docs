package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ExampleMod implements ModInitializer {
    private static final MyBlock PACKETS_BLOCK = new MyBlock();
    public static final Identifier TURN_TO_DIAMOND_PACKET_ID = new Identifier("example", "diamond");
//    ServerSidePacketRegistry.INSTANCE.register(Identifier(modId, packetId), packetConsumer)

    @Override
    public void onInitialize() {
        System.out.println("Init");
        Registry.register(Registry.BLOCK, "docs:packets", PACKETS_BLOCK);
        Registry.register(Registry.ITEM, "docs:packets",
				new BlockItem(PACKETS_BLOCK, new Item.Settings().group(ItemGroup.MISC)));
    }
}

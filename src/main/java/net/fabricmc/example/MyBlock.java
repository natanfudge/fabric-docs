package net.fabricmc.example;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.stream.Stream;

import io.netty.buffer.Unpooled;

public class MyBlock extends Block {
    public MyBlock() {
        super(Settings.of(Material.STONE));
    }


    @Override
    public void onBlockRemoved(BlockState before, World world, BlockPos pos, BlockState after, boolean bool) {
        // First we need to actually get hold of the players that we want to send the packets to.
        // A simple way is to obtain all players watching this position:
        Stream<PlayerEntity> watchingPlayers = PlayerStream.watching(world, pos);

        // Pass the `BlockPos` information
        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
        passedData.writeBlockPos(pos);
        passedData.writeInt(123);
        passedData.writeString("hello");


        // Then we'll send the packet to all the players
        watchingPlayers.forEach(player -> {
            Identifier i =  ExampleMod.PLAY_PARTICLE_PACKET_ID;
            ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ExampleMod.PLAY_PARTICLE_PACKET_ID, passedData);
        });
        // This will work in both multiplayer and singleplayer!
    }

    @Override
    public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult result) {
        if (world.isClient()) {
            // We are in the client now, can't do anything server-sided

            // See the keybindings tutorial for information about this if statement
            if(ExampleClientInit.EXAMPLE_KEYBINDING.isPressed()){
                // Pass the `BlockPos` information
                PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                passedData.writeBlockPos(pos);
                // Send packet to server to change the block for us
                ClientSidePacketRegistry.INSTANCE.sendToServer(ExampleMod.TURN_TO_DIAMOND_PACKET_ID, passedData);
            }

        }
        return true;
    }
}



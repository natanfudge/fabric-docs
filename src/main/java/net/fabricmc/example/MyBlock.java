package net.fabricmc.example;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.PacketByteBuf;
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
        Stream<PlayerEntity> watchingPlayers = PlayerStream.watching(world,pos);

        // Pass the `BlockPos` information
        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
        passedData.writeBlockPos(pos);
        passedData.writeInt(123);
        passedData.writeString("hello");

        // Then we'll send the packet to all the players
        watchingPlayers.forEach(player ->
                ServerSidePacketRegistry.INSTANCE.sendToPlayer(player,ExampleClientInit.PLAY_PARTICLE_PACKET_ID,passedData));
        // This will work in both multiplayer and singleplayer!
    }

}


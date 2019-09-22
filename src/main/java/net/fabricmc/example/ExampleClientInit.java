package net.fabricmc.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ExampleClientInit implements ClientModInitializer {
    public static final Identifier PLAY_PARTICLE_PACKET_ID = new Identifier("example", "particle");

    @Override
    public void onInitializeClient() {
        ClientSidePacketRegistry.INSTANCE.register(PLAY_PARTICLE_PACKET_ID,
                (packetContext, attachedData) -> {
                    // Get the BlockPos we put earlier
                    BlockPos pos = attachedData.readBlockPos();
                    int num = attachedData.readInt();
                    String str = attachedData.readString();
                    packetContext.getTaskQueue().execute(() -> {
                        /*
                         * Use num and str
                         */
                        MinecraftClient.getInstance().particleManager.addParticle(
                                ParticleTypes.EXPLOSION, pos.getX(), pos.getY(), pos.getZ(),
                                0.0D, 0.0D, 0.0D
                        );
                    });
                });


    }
}



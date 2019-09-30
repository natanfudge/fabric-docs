package net.fabricmc.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import org.lwjgl.glfw.GLFW;

public class ExampleClientInit implements ClientModInitializer {

    public static FabricKeyBinding EXAMPLE_KEYBINDING = FabricKeyBinding.Builder.create(
            new Identifier("example", "keybinding"),
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "Wiki Keybinds"
    ).build();


    @Override
    public void onInitializeClient() {
        ClientSidePacketRegistry.INSTANCE.register(ExampleMod.PLAY_PARTICLE_PACKET_ID,
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

        KeyBindingRegistry.INSTANCE.register(EXAMPLE_KEYBINDING);

    }
}



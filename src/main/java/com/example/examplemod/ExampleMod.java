package com.example.examplemod;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import src.main.java.com.example.examplemod.Add;
import src.main.java.com.example.examplemod.EGManager;
import src.main.java.com.example.examplemod.List;
import src.main.java.com.example.examplemod.Validate;


import java.util.Random;

@Mod(modid = "examplemod", version = "1.0")
public class ExampleMod  {
    private static final int DETECTION_RADIUS = 10;
    private static final float SOUND_DURATION = 1.0F;
    private static final int SOUND_PLAY_ODD = 10;
    private static final int CHECK_INTERVAL_TICKS = 200; // 10 seconds in 20 ticks

    private boolean isPlaying = false;
    private long soundStartTime = 0;
    private long lastCheckTime = 0;

    private final Random random = new Random();
    private final EGManager coordinates = new EGManager("coords.json");

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        ClientCommandHandler.instance.registerCommand(new Validate(coordinates));
        ClientCommandHandler.instance.registerCommand(new List(coordinates));
        ClientCommandHandler.instance.registerCommand(new Add(coordinates));
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        // Only run on the client side
        if (event.player.worldObj.isRemote) {
            World world = event.player.worldObj;
            BlockPos playerPos = event.player.getPosition();
            long currentTime = Minecraft.getMinecraft().theWorld.getTotalWorldTime();

            if (isPlaying) {
                if (currentTime >= soundStartTime + (long) (SOUND_DURATION * 20)) {
                    isPlaying = false; // Sound has finished playing
                } else return;
            }

            // If it's less than 10 seconds since the last check, do nothing
            if (currentTime - lastCheckTime < CHECK_INTERVAL_TICKS) {
                return;
            }

            lastCheckTime = currentTime;

            if (random.nextInt(100) < SOUND_PLAY_ODD) {
                for (int x = -DETECTION_RADIUS; x <= DETECTION_RADIUS; x++) {
                    for (int y = -DETECTION_RADIUS; y <= DETECTION_RADIUS; y++) {
                        for (int z = -DETECTION_RADIUS; z <= DETECTION_RADIUS; z++) {
                            BlockPos pos = playerPos.add(x, y, z);
                            if (isSign(world, pos)) {
                                isPlaying = true;
                                soundStartTime = currentTime;
                                world.playSound(
                                        pos.getX() + 0.5,
                                        pos.getY() + 0.5,
                                        pos.getZ() + 0.5,
                                        "examplemod:custom_sound",
                                        1.0F,
                                        1.0F,
                                        false
                                );

                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isSign(World world, BlockPos pos) {
        // Check if the block is a standing or wall sign
        return world.getBlockState(pos).getBlock() == Blocks.standing_sign ||
                world.getBlockState(pos).getBlock() == Blocks.wall_sign;
    }

    @SubscribeEvent
    public void onSignInteraction(PlayerInteractEvent event) {
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            if (isSign(event.world, event.pos)) {
                // move from coordinates logic to EG: save world name + type
                // coordinates.save(event.pos);
                // event.player.addChatMessage(new net.minecraft.util.ChatComponentText("Coordinates saved: " + event.pos.toString()));
            }
        }
    }
}

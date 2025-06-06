package com.mrg;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;


public class WaterScaryClient implements ClientModInitializer {
	private int counter = 0;
	private int timeInWater = 0;
	private static final ConfigScreen cnfScreen = new ConfigScreen();

	public static Screen getCnfScreen(Screen screen) {
		cnfScreen.setLastScreen(screen);
		return cnfScreen;
	}

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register((t) -> {
			ClientPlayerEntity player = t.player;
			ClientWorld world = t.world;

			if(player != null && world != null) {
				BlockPos pos = player.getBlockPos();
				if (world.getBlockState(pos).getBlock().equals(Blocks.WATER)) {
					int num = (int) (timeInWater == 0 ? 20 : (1/ (double) timeInWater) * 2000);

					if (num <= 1) num = 1;
					else if (num <= 5) num = 5;
					else if (num <= 10) num = 10;
					else num = 20;

					if (counter++ > num) {
						counter = 0;
						player.animateDamage(num);
						player.playSound(SoundEvents.ENTITY_PLAYER_HURT_ON_FIRE);
					}
					timeInWater++;
				} else {
					timeInWater = 0;
				}
			}
		});
	}
}
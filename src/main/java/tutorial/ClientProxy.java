package tutorial;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tutorial.client.KeyHandler;
import tutorial.client.gui.GuiManaBar;
import tutorial.entity.EntityThrowingRock;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerRenderers() {
		Minecraft mc = Minecraft.getMinecraft();
		RenderingRegistry.registerEntityRenderingHandler(EntityThrowingRock.class, new RenderSnowball(mc.getRenderManager(), TutorialMain.throwingRock, mc.getRenderItem()));
		// can register other client-side only things here, too:

		// The RenderGameOverlayEvent is in the MinecraftForge package, so we will
		// register our mana bar overlay to that event bus:
		MinecraftForge.EVENT_BUS.register(new GuiManaBar(mc));

		// KeyInputEvent is in the FML package, meaning it's posted to the FML event bus
		// rather than the regular Forge event bus:
		FMLCommonHandler.instance().bus().register(new KeyHandler());
	}

	@Override
	public int addArmor(String armor) {
		//return RenderingRegistry.addNewArmourRendererPrefix(armor);
		return 0;
	}

	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		// Note that if you simply return 'Minecraft.getMinecraft().thePlayer',
		// your packets will not work as expected because you will be getting a
		// client player even when you are on the server!
		// Sounds absurd, but it's true.

		// Solution is to double-check side before returning the player:
		return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx));
	}
}

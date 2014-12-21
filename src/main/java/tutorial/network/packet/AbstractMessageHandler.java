package tutorial.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tutorial.TutorialMain;

/**
 * 
 * Syntax for extending this class is "public class YourMessageHandler extends AbstractMessageHandler<YourMessage>"
 * 
 * I prefer to have an EntityPlayer readily available when handling packets, as well as to
 * know which side I'm on without having to check every time, so I handle those operations
 * here and pass off the rest of the work to abstract methods to be handled in each sub-class.
 * 
 * There is nothing about this class that is more 'correct' than the more typical ways of
 * dealing with packets, so if this way doesn't make much sense to you, go ahead and use
 * whatever way does make sense - it's really just a matter of personal preference.
 * 
 * We do not want to have to implement client handling for server side messages (and vice-versa),
 * however, so we will abstractify even further, as well as create separate packages to organize
 * our client vs. server messages. If you only have a few packets, you may opt not to, but once
 * you have more than a handful, keeping them separate makes it easier to remember on which side
 * to register, which side you can send to, and so on.
 *
 */
public abstract class AbstractMessageHandler<T extends IMessage> implements IMessageHandler <T, IMessage>
{
	/**
	 * Handle a message received on the client side
	 * @return a message to send back to the Server, or null if no reply is necessary
	 */
	@SideOnly(Side.CLIENT)
	public abstract IMessage handleClientMessage(EntityPlayer player, T message, MessageContext ctx);

	/**
	 * Handle a message received on the server side
	 * @return a message to send back to the Client, or null if no reply is necessary
	 */
	public abstract IMessage handleServerMessage(EntityPlayer player, T message, MessageContext ctx);

	/*
	 * Here is where I parse the side and get the player to pass on to the abstract methods.
	 * This way it is immediately clear which side received the packet without having to
	 * remember or check on which side it was registered and the player is immediately
	 * available without a lengthy syntax.
	 */
	@Override
	public IMessage onMessage(T message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			return handleClientMessage(TutorialMain.proxy.getPlayerEntity(ctx), message, ctx);
		} else {
			return handleServerMessage(TutorialMain.proxy.getPlayerEntity(ctx), message, ctx);
		}
	}
}
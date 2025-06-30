package xfacthd.framedblocks.client.net;

import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import xfacthd.framedblocks.client.screen.FramedSignScreen;
import xfacthd.framedblocks.common.blockentity.special.FramedSignBlockEntity;
import xfacthd.framedblocks.common.data.cullupdate.ClientCullingUpdateTracker;
import xfacthd.framedblocks.common.net.payload.clientbound.ClientboundCullingUpdatePayload;
import xfacthd.framedblocks.common.net.payload.clientbound.ClientboundOpenSignScreenPayload;

public final class ClientNetworkHandler
{
    public static void onRegisterPayloadHandlers(RegisterClientPayloadHandlersEvent event)
    {
        event.register(ClientboundOpenSignScreenPayload.TYPE, ClientNetworkHandler::handleOpenSignScreen);
        event.register(ClientboundCullingUpdatePayload.TYPE, ClientNetworkHandler::handleCullingUpdate);
    }

    private static void handleOpenSignScreen(ClientboundOpenSignScreenPayload payload, IPayloadContext ctx)
    {
        //noinspection ConstantConditions
        if (Minecraft.getInstance().level.getBlockEntity(payload.pos()) instanceof FramedSignBlockEntity be)
        {
            Minecraft.getInstance().setScreen(new FramedSignScreen(be, payload.frontText()));
        }
    }

    private static void handleCullingUpdate(ClientboundCullingUpdatePayload payload, IPayloadContext ctx)
    {
        ClientCullingUpdateTracker.handleCullingUpdates(payload.chunk(), payload.positions());
    }

    private ClientNetworkHandler() { }
}

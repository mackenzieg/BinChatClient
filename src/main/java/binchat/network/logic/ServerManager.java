package binchat.network.logic;

import binchat.network.protocol.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

public class ServerManager extends ChannelInitializer<Channel> {

    private ServerConnection serverConnection;

    public ServerManager() {

    }

    @Override
    public void initChannel(Channel ch) throws Exception {

        this.serverConnection = new ServerConnection(this, new ChannelWrapper(ch));

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("frame_decoder", new FrameDecoder());
        pipeline.addLast("packet_decoder", new Decoder(Packets.HANDSHAKE));
        pipeline.addLast("frame_encoder", new FieldPrepender());
        pipeline.addLast("packet_decoder", new Encoder(Packets.LOGIN));
        pipeline.addLast("packet_handler", this.serverConnection);
    }

    public ServerConnection getServerConnection() {
        return serverConnection;
    }

}

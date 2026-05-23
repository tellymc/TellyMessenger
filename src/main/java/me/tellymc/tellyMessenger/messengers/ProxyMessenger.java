package me.tellymc.tellyMessenger.messengers;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.tellymc.tellyMessenger.Messenger;
import me.tellymc.tellyMessenger.objects.PacketListener;
import me.tellymc.tellyMessenger.objects.PacketWriter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class ProxyMessenger extends Messenger implements Listener {

    private final Plugin plugin;
    private final Map<String, Map<String, PacketListener>> listeners = new ConcurrentHashMap<>();

    public ProxyMessenger(Plugin plugin) {
        this.plugin = plugin;

        ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
    }

    @Override
    public void register(String channel) {
        ProxyServer.getInstance().registerChannel(channel);
    }

    @Override
    public void send(Object target, String channel, String subChannel, PacketWriter writer) {

        if (target == null) {
            return;
        }

        if (!(target instanceof ProxiedPlayer)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) target;
        Server server = player.getServer();

        if (server == null) {
            return;
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF(subChannel);

        try {
            writer.write(out);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed sending message out.", e);
            return;
        }

        server.sendData(channel, out.toByteArray());
    }

    @Override
    public void listen(String channel, String subChannel, PacketListener listener) {

        listeners.computeIfAbsent(channel, c -> new ConcurrentHashMap<>()).put(subChannel, listener);
    }

    @EventHandler
    public void onMessageReceive(PluginMessageEvent event) {

        String channel = event.getTag();
        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        String subChannel = in.readUTF();

        Map<String, PacketListener> subs = listeners.get(channel);

        if (subs == null) {
            return;
        }

        PacketListener listener = subs.get(subChannel);

        if (listener == null) {
            return;
        }

        try {
            listener.read(in);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed while receiving message in.", e);
        }
    }
}
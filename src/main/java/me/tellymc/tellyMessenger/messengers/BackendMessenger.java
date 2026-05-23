package me.tellymc.tellyMessenger.messengers;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.tellymc.tellyMessenger.Messenger;
import me.tellymc.tellyMessenger.objects.PacketListener;
import me.tellymc.tellyMessenger.objects.PacketWriter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class BackendMessenger extends Messenger implements PluginMessageListener {

    private final JavaPlugin plugin;
    private final Map<String, Map<String, PacketListener>> listeners = new ConcurrentHashMap<>();

    public BackendMessenger(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void register(String channel) {

        plugin.getServer().getMessenger().registerIncomingPluginChannel(
                plugin,
                channel,
                this
        );

        plugin.getServer().getMessenger().registerOutgoingPluginChannel(
                plugin,
                channel
        );
    }

    @Override
    public void send(Object target, String channel, String subChannel, PacketWriter writer) {

        if (target == null) {
            return;
        }

        if (!(target instanceof Player)) {
            return;
        }

        Player player = (Player) target;
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF(subChannel);

        try {
            writer.write(out);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed while sending packet out.", e);
            return;
        }

        player.sendPluginMessage(plugin, channel, out.toByteArray());
    }

    @Override
    public void listen(String channel, String subChannel, PacketListener listener) {

        listeners.computeIfAbsent(channel, c -> new ConcurrentHashMap<>()).put(subChannel, listener);
    }

    @Override
    public void onPluginMessageReceived(@NonNull String channel, @NonNull Player player, byte @NonNull [] message) {

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();

        Map<String, PacketListener> subs = listeners.get(channel);

        if (subs == null) return;

        PacketListener listener = subs.get(subChannel);

        if (listener == null) {
            return;
        }

        try {
            listener.read(in);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed while receiving a packet in.", e);
        }
    }
}
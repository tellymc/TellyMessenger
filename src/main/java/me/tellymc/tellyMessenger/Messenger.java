package me.tellymc.tellyMessenger;

import me.tellymc.tellyMessenger.objects.PacketListener;
import me.tellymc.tellyMessenger.objects.PacketWriter;

public abstract class Messenger {
    public abstract void register(String channel);
    public abstract void send(Object target, String channel, String subChannel, PacketWriter writer);
    public abstract void listen(String channel, String subChannel, PacketListener listener);
}
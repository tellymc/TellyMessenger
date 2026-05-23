package me.tellymc.tellyMessenger;

import me.tellymc.tellyMessenger.messengers.BackendMessenger;
import me.tellymc.tellyMessenger.messengers.ProxyMessenger;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class TellyMessenger {

    private final Messenger messenger;
    private final JavaPlugin backend;
    private final Plugin proxy;

    // Backend Messenger
    public TellyMessenger(JavaPlugin backend) {
        this.messenger = new BackendMessenger(backend);
        this.backend = backend;
        this.proxy = null;
    }

    // Proxy Messenger
    public TellyMessenger(Plugin proxy) {
        this.messenger = new ProxyMessenger(proxy);
        this.backend = null;
        this.proxy = proxy;
    }

    public Messenger getMessenger() {
        return messenger;
    }

    public JavaPlugin getBackend() {
        return backend;
    }

    public Plugin getProxy() {
        return proxy;
    }
}
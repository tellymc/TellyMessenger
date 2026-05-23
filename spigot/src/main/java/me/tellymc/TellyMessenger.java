package me.tellymc;

import org.bukkit.plugin.java.JavaPlugin;

public final class TellyMessenger {

    private final Messenger messenger;
    private final JavaPlugin backend;

    public TellyMessenger(JavaPlugin backend) {
        this.messenger = new BackendMessenger(backend);
        this.backend = backend;
    }

    public Messenger getMessenger() {
        return messenger;
    }

    public JavaPlugin getBackend() {
        return backend;
    }
}
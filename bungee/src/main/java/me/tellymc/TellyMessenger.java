package me.tellymc;

import net.md_5.bungee.api.plugin.Plugin;

public final class TellyMessenger {

    private final Messenger messenger;
    private final Plugin proxy;

    public TellyMessenger(Plugin proxy) {
        this.messenger = new ProxyMessenger(proxy);
        this.proxy = proxy;
    }

    public Messenger getMessenger() {
        return messenger;
    }

    public Plugin getProxy() {
        return proxy;
    }
}
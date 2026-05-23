package me.tellymc;

import com.google.common.io.ByteArrayDataInput;

@FunctionalInterface
public interface PacketListener {

    void read(ByteArrayDataInput in) throws Exception;
}
package me.tellymc;

import com.google.common.io.ByteArrayDataOutput;

@FunctionalInterface
public interface PacketWriter {

    void write(ByteArrayDataOutput out) throws Exception;
}
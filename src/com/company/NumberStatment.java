package com.company;

import com.sun.jdi.event.StepEvent;

import javax.naming.event.NamingEvent;
import java.util.BitSet;

public class NumberStatment extends NerveStatment {
    static final int Bit_Byte=8;
    static final int Bit_Short=16;
    static final int Bit_Int=32;
    static final int Bit_Long=64;
    int start;
    public NumberStatment(NerveSystem system, int start)
    {
        super(system);
        this.start=start;
    }
    public byte[] getBytes(int len){
        BitSet set = system.getBits(getNBits(start,len));
        return set.toByteArray();
    }
    public byte getByte(){
        return this.getBytes(8)[0];
    }
    public int getInt(){
        byte[] bs=this.getBytes(32);
        assert bs.length==4;
        return   bs[3] & 0xFF |
                (bs[2] & 0xFF) << 8 |
                (bs[1] & 0xFF) << 16 |
                (bs[0] & 0xFF) << 24;
    }
}

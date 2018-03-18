package com.company;

import com.sun.jdi.event.StepEvent;

import javax.naming.event.NamingEvent;
import java.util.BitSet;

public class NumberStatment extends NerveStatment {
    static int[] getNBits(int start){
        int[] bits=new int[8];
        for(int i = 0;i<8;++i){
            bits[i]=start+i;
        }
        return bits;
    }
    public NumberStatment(NerveSystem system, int start) {
        super(system, getNBits(start));
    }
    public byte getByte(){
        BitSet set=system.getBits(this.bitlist);
        byte[] bytes = set.toByteArray();
        return bytes[0];
    }
    public int getInt(){
        BitSet set=system.getBits(this.bitlist);
        byte[] bytes = set.toByteArray();
    }
}

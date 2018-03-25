package com.company;

import com.sun.jdi.event.StepEvent;

import javax.naming.event.NamingEvent;
import java.util.BitSet;

public class NumberStatment extends NerveStatment {
    static final int Bit_Byte=8;
    static final int Bit_Short=16;
    static final int Bit_Int=32;
    static final int Bit_Long=64;
    private int start;
    public NumberStatment(NerveSystem system, int start)
    {
        super(system);
        this.start=start;
    }

    /**
     * 从start开始得到字节集
     * @param len 长度
     * @return 字节集
     */
    public byte[] getBytes(int len){
        BitSet set = system.getBits(getNBits(start,len));
        if(set.length()==0) return new byte[]{0};
        return set.toByteArray();
    }

    /**
     * 激活一定的字节集
     * @param bytes 字节集
     */
    public void activeBytes(byte[] bytes){
        BitSet set=BitSet.valueOf(bytes);
        int[] actar=getActiveIds(start,set);
        system.active(actar);
    }
    //接口区域
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
    public void activeByte(byte b){
        this.activeBytes(new byte[]{b});
    }
    public void activeInt(int i){
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (i & 0xff);
        bytes[1] = (byte) ((i >> 8) & 0xff);
        bytes[2] = (byte) ((i >> 16) & 0xff);
        bytes[3] = (byte) ((i >> 24) & 0xff);
        this.activeBytes(bytes);
    }



}

package com.company;

import java.util.Arrays;
import java.util.BitSet;

/**
 * 对一个神经系统中的几个神经元的状态进行描述
 */
public class NerveStatment {
    NerveSystem system=null;
    /**
     * 初始化一个神经描述
     * @param system 神经系统对象
     */
    public NerveStatment(NerveSystem system){
        this.system=system;
    }

    /**
     * 获取一个连续的监听列表
     * @param start 开始
     * @param len 长度
     * @return 监听列表
     */
    static int[] getNBits(int start,int len){
        int[] bits=new int[len];
        for(int i = 0;i<len;++i){
            bits[i]=start+i;
        }
        return bits;
    }

}

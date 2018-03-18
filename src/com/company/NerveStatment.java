package com.company;

import java.util.Arrays;
import java.util.BitSet;

/**
 * 对一个神经系统中的几个神经元的状态进行描述
 */
public class NerveStatment {
    NerveSystem system=null;
    int[] bitlist;
    /**
     * 初始化一个神经描述
     * @param system 神经系统对象
     * @param bit_lts 要监听的神经元列表
     */
    public NerveStatment(NerveSystem system,int[] bit_lts){
        this.system=system;
        this.bitlist=bit_lts.clone();
    }

}

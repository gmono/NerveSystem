package com.company;


import jdk.dynalink.beans.StaticClass;

import javax.naming.event.NamingEvent;
import java.util.*;

/**
 * 基本的神经系统模拟器
 */
public class NerveSystem {
    public NerveSystem(int nervecount){
        this.nerveCells=new ArrayList<>(nervecount);
    }
    static float SS_Inc=(float)0.1;
    static float SS_Dec=(float)0.1;
    /**
     * 表示一个突触
     */
    public class Synapse{
        int to;
        int from;
        //重要值 表示此突触的强度 强度为0时突触销毁
        float s=1;
        public Synapse(int from,int to){
            this.to=to;
            this.from=from;
        }

        /**
         * 激活连接的神经元
         */
        void active(){
            ArrayList<Integer> lst=new ArrayList<>();
            lst.add(this.to);
            NerveSystem.this.active(lst);
            //激活增加强度
            s+=NerveSystem.SS_Inc;
            //debug
            System.out.printf("将激活神经元%d\n",this.to);
        }

        /**
         * 突触的时间步响应
         */
        void step(){
            //自动削减 这里可以修改自动削减比例
            this.s-=NerveSystem.SS_Dec;
            if(this.s<=0){
                //debug
                System.out.printf("从神经元%d 到 神经元%d 的突触死亡\n",from,to);
                //从from神经元中删除自己
                NerveCell cell = NerveSystem.this.nerveCells.get(from);
                cell.links.remove(this);
            }
        }
    }

    static final int NS_Active=1;
    static final int NS_Sleeping=2;
    static final int NS_Ready=0;
    /**
     * 表示一个神经元
     */
    public class NerveCell{
        int myid;
        int state;
        Set<Synapse> links=new HashSet<>();
        public NerveCell(int myid){
            this.myid=myid;
            state = NS_Ready;
        }

        /**
         * 激活与之相连的突触
         */
        void active(){
            //已经是激活状态或者为休眠状态就不会再次传导
            if(state==NS_Active||state==NS_Sleeping){
                //debug
                System.out.printf("神经元%d已经为状态%d，将不会激活\n",myid,state);
                return;
            }
            //开始激活过程
            for(Synapse synapse:this.links){
                synapse.active();
            }
            //debug
            System.out.printf("神经元%d被激活，将传道至%d个突触\n",myid,this.links.size());
        }

        /**
         * 走一个时间步 并做出此神经元的响应
         */
        void step(){
            //debug
            int old=this.state;
            //
            switch (this.state){
                case NS_Active:
                    this.state=NS_Sleeping;
                    break;
                case NS_Sleeping:
                    this.state=NS_Ready;
                    break;
            }
            //debug
            System.out.printf("神经元%d号 从state:%d 转换到state:%d\n",myid,old,this.state);
        }

        void connect(int to){
            Synapse synapse=new Synapse(this.myid,to);
            this.links.add(synapse);
        }
    }
    //神经元集合
    List<NerveCell> nerveCells;

    /**
     * 激活指定的神经元
     * @param ids 要激活的神经元
     */
    void active(Iterable<Integer> ids){
        for(Integer id:ids){
            NerveCell cell=nerveCells.get(id);
            cell.active();
        }
    }
    //以下两个为连接函数
    void connect(int from,int to){
        this.nerveCells.get(from).connect(to);
    }
    void connect(int from,Iterable<Integer> toids){
        NerveCell cell = this.nerveCells.get(from);
        for(Integer to:toids){
            cell.connect(to);
        }
    }

    /**
     * 走一个时间步
     */
    void step(){
        //对所有突触和神经元调用step函数
        for(NerveCell cell:this.nerveCells){
            cell.step();
            for(Synapse synapse:cell.links){
                synapse.step();
            }
        }
    }

    BitSet getBits(int[] bits){
        BitSet set = new BitSet(bits.length);
        for(int i:bits){
            NerveCell cell = this.nerveCells.get(i);
            if(cell.state==NS_Active){
                set.set(i,true);
            }
            else{
                set.set(i,false);
            }
        }
        return set;
    }

}

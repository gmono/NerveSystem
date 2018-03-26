package com.company;


import javafx.scene.control.Cell;
import jdk.dynalink.beans.StaticClass;

import javax.naming.event.NamingEvent;
import java.lang.reflect.Array;
import java.util.*;

/**
 * 基本的神经系统模拟器
 */
public class NerveSystem {
    static final boolean DEBUG=false;
    public NerveSystem(int nervecount){
        this.nerveCells=new ArrayList<>(nervecount);
        for(int i=0;i<nervecount;++i){
            nerveCells.add(new NerveCell(i));
        }
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
        //修改此初始值可使突触有不同的行为
        float s=(float)0.1;
        public Synapse(int from,int to){
            assert from!=to;
            this.to=to;
            this.from=from;
        }

        /**
         * 激活连接的神经元
         */
        void active(){
            NerveSystem.this.active(new int[]{this.to});
            //激活增加强度
            active_inc=NerveSystem.SS_Inc;
            if(DEBUG)
                System.out.printf("突触响应,将激活神经元%d\n",this.to);
        }

        //表示此回合中突触的强度增加值
        float active_inc=0;
        /**
         * 突触的时间步响应
         */
        void step(){
            //加上本回合增加值
            this.s+=active_inc;
            //自动削减 这里可以修改自动削减比例
            this.s-=NerveSystem.SS_Dec;
            if(this.s<=0){
                if(DEBUG)
                    System.out.printf("从神经元%d 到 神经元%d 的突触死亡\n",from,to);
                //从from神经元中删除自己
                NerveSystem.this.disconnect(from,to);
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
        Set<Synapse> links=Collections.synchronizedSet(new HashSet<>());
        Map<Integer,Synapse> towards=Collections.synchronizedMap(new HashMap<>());
        public NerveCell(int myid){
            this.myid=myid;
            state = NS_Ready;
        }

        /**
         * 激活与之相连的突触
         * @return 返回中枢神经元的激励值 给突触的激励值
         */
        float active(){
            //已经是激活状态或者为休眠状态就不会再次传导
            if(state==NS_Active||state==NS_Sleeping){
                if(DEBUG)
                    System.out.printf("神经元%d已经为状态%d，将不会激活\n",myid,state);
                if(state==NS_Active) return SS_Inc*2;
                else return SS_Inc;
            }
            //开始激活过程
            this.state=NS_Active;
            for(Synapse synapse:this.links){
                synapse.active();
            }
            if(DEBUG)
                System.out.printf("神经元%d被激活，将激活%d个突触\n",myid,this.links.size());
            return SS_Inc;
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
            if(old!=this.state&&DEBUG)
                System.out.printf("神经元%d号 从state:%d 转换到state:%d\n",myid,old,this.state);
        }

        /**
         * 连接这个神经元到另一个神经元 如果没有连接的话
         * @param to 目的神经元
         */
        void connect(int to){
            assert to!=myid;
            if(towards.containsKey(to)){
                if(DEBUG)
                    System.out.println("已经存在连接，连接取消");
            }
            Synapse synapse=new Synapse(this.myid,to);
            this.links.add(synapse);
            this.towards.put(to,synapse);
        }

        /**
         * 解除连接
         * @param to 目的神经元
         */
        void disconnect(int to){
            this.links.remove(towards.get(to));
            this.towards.remove(to);
        }

        /**
         * 解除连接
         * @param synapse 目的突触
         */
        void discontect(Synapse synapse){
            this.towards.remove(synapse.to);
            this.links.remove(synapse);
        }

        /**
         * 得到toward集
         * @return towards集
         */
        public Set<Integer> getTowards(){
            return this.towards.keySet();
        }
    }
    //神经元集合
    List<NerveCell> nerveCells;

    Set<Integer> actived=new HashSet<>();
    /**
     * 激活指定的神经元
     * @param ids 要激活的神经元
     */
    public void active(int[] ids){
        for(int id:ids){
            NerveCell cell=nerveCells.get(id);
            cell.active();
            actived.add(id);
        }
        //记录激活的神经元
    }
    //以下两个为连接函数
    public void connect(int from,int to){
        this.nerveCells.get(from).connect(to);
    }
    public void connect(int from,int[] toids){
        NerveCell cell = this.nerveCells.get(from);
        for(int to:toids){
            cell.connect(to);
        }
    }
    //以下为两个解除连接的函数
    public void disconnect(int from,int to){
        NerveCell cell = this.nerveCells.get(from);
        cell.disconnect(to);
    }
    public void disconnect(int from,int[] toids){
        NerveCell cell = this.nerveCells.get(from);
        for(int id:toids){
            cell.disconnect(id);
        }
    }


    /**
     * 走一个时间步
     */
    void step(){
        //对所有突触和神经元调用step函数
        for(NerveCell cell:this.nerveCells){
            cell.step();
            Set<Synapse> linkscopy=new HashSet<>(cell.links);
            for(Synapse synapse:linkscopy){
                synapse.step();
            }
        }
        //执行策略
        this.next();
    }

    /**
     * 可重写 用于定制step策略 默认为激活的互联连接
     */
    protected void next(){
        System.out.println("执行默认行为！请使用派生类重写Next函数，默认行为不会产生任何效果");
        for(int id:actived){
            for(int to:actived){
                if(id!=to)
                    this.connect(id,to);
            }
        }
    }

    /**
     * 内部工具函数 用于根据id号获取神经元
     * @param id
     * @return  神经元
     */
    private NerveCell getNerve(int id){
        return this.nerveCells.get(id);
    }

    BitSet getBits(int[] bits){
        BitSet set = new BitSet(bits.length);
        for(int i=0;i<bits.length;++i){
            int idx=bits[i];
            NerveCell cell = this.nerveCells.get(idx);
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

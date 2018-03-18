package com.company;


import java.util.ArrayList;
import java.util.List;

public class NerveSystem {
    /**
     * 表示一个突触
     */
    public class Synapse{
        int to;
        int from;
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
            //debug
            System.out.printf("将激活神经元%d\n",this.to);
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
        List<Synapse> links=new ArrayList<>();
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

    /**
     * 走一个时间步
     */
    void step(){

    }
}

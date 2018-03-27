package com.company;

import java.util.Random;

/**
 * 随机化的神经系统 每个时间步产生一定比例的随机连接
 */
public class RandomNerveSystem extends NerveSystem {
    double randp;
    /**
     * 构造一个随机神经元系统
     * @param nervecount 神经元数量
     * @param randp 随机比例 相对于神经元数量
     */
    public RandomNerveSystem(int nervecount,double randp){
        super(nervecount);
        this.randp=randp;
    }
    public RandomNerveSystem(int nervecount){
        this(nervecount,0.05);
    }

    @Override
    protected void next() {
        //激活触发
        int max=10;
        cont:
        for(int id:actived){
            for(int to:actived){
                if(id!=to) {
                    max--;
                    this.connect(id, to);
                    if(max<=0) break cont;
                }
            }
        }
        //添加新的随机连接
        int count=(int)(this.randp*this.nerveCells.size());
        Random random=new Random();
        for(int i=0;i<count;++i){
            int from=random.nextInt()%this.nerveCells.size();
            int to=random.nextInt()%this.nerveCells.size();
            //如果不小心相等就直接忽略
            if(from==to) continue;
            this.connect(from,to);
        }
    }
}

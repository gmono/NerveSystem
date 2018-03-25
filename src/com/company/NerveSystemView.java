package com.company;

import java.awt.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class NerveSystemView extends Frame {
    private NerveSystem system=null;
    private int[] wh=null;
    private int point_space=0;
    public NerveSystemView(NerveSystem system,int point_space) throws HeadlessException {
        super();
        this.system=system;
        wh=calculateWH(system.nerveCells.size());
        this.setSize(point_space*wh[0],point_space*wh[1]);
        this.point_space=point_space;
        //表面
        this.setTitle("神经网络视图");
    }

    /**
     * 计算宽高
     * @param count 点数量
     * @return 宽高
     */
    private int[] calculateWH(int count){
        int w=(int)Math.sqrt(count);
        int h=(int)Math.ceil(count/w);
        return new int[]{w,h};
    }
    private int[] calculateXY(int id){
        int r=id/wh[0];
        int c=id%wh[0];
        return new int[]{point_space*(r+1),point_space*(c+1)};
    }
    private void paintLink(int from,int to){
        int[] xy1=calculateXY(from);
        int[] xy2=calculateXY(to);
        Graphics graphics = this.getGraphics();
        graphics.drawLine(xy1[0],xy1[1],xy2[0],xy2[1]);
    }

    @Override
    public void paint(Graphics g) {
        //绘制点
        for(int i=0;i<system.nerveCells.size();++i){
            //计算位置
            int[] xy=calculateXY(i);
            int x=xy[0];
            int y=xy[1];
            int w=point_space/5;
            int h=point_space/5;
            //绘制点
            g.drawOval(x,y,w,h);
            //绘制连线
            NerveSystem.NerveCell cell = system.nerveCells.get(i);
            Set<Integer> towards=new HashSet<>(cell.getTowards());
            for(int s:towards){
                this.paintLink(i,s);
            }
        }
        super.paint(g);
    }

}

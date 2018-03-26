package com.company;

import java.awt.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class NerveSystemView extends Component {
    private NerveSystem system=null;
    private int[] wh=null;
    private int point_space=0;
    //点的宽高
    private int pw=0;
    private int ph=0;
    public NerveSystemView(NerveSystem system,int point_space) throws HeadlessException {
        super();
        this.system=system;
        wh=calculateWH(system.nerveCells.size());
        this.setSize(point_space*(wh[0]+1),point_space*(wh[1]+1));
        this.point_space=point_space;
        this.pw=point_space/5;
        this.ph=point_space/5;
        //表面
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
        return new int[]{point_space*(c+1),point_space*(r+1)};
    }
    private void paintLink(int from,int to){
        int[] xy1=calculateXY(from);
        int[] xy2=calculateXY(to);
        Graphics graphics = this.getGraphics();
        graphics.drawLine(xy1[0]+pw/2,xy1[1]+ph/2,xy2[0]+pw/2,xy2[1]+ph/2);
    }
    private void paintActive(int id){
        int[] xy=calculateXY(id);
        this.getGraphics().fillOval(xy[0],xy[1],ph,pw);
    }
    private void paintNerve(int id){
        int[] xy=calculateXY(id);
        int x=xy[0];
        int y=xy[1];
        this.getGraphics().drawOval(x,y,pw,ph);
    }

    @Override
    public void paint(Graphics g) {
        //绘制点
        for(int i=0;i<system.nerveCells.size();++i){
            //计算位置
            this.paintNerve(i);
            for(int id:system.actived){
                this.paintActive(id);
            }
            //绘制连线
            NerveSystem.NerveCell cell = system.nerveCells.get(i);
            for(int s:cell.getTowards()){
                this.paintLink(i,s);
            }
        }
        super.paint(g);
    }

}

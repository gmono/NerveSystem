package com.company;


import javax.naming.event.NamingEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;


public class Main {

    public static void main(String[] args) throws InterruptedException {
	// write your code here
        //system
        NerveSystem system=new RandomNerveSystem(100);
        NumberStatment statment=new NumberStatment(system,0);
        NumberStatment statment1=new NumberStatment(system,10);
        NumberStatment outstatment=new NumberStatment(system,20);
        Random random=new Random();
        //窗口构造阶段
        Component view=new NerveSystemView(system,50);
        Frame frame=new Frame("神经网络视图");
        frame.setVisible(true);
        frame.add(view);
        //确定窗口大小
        Dimension dim=view.getSize();
        dim.setSize(dim.width+100,dim.height+100);
        frame.setSize(dim);
        //布局
        frame.setLayout(new BorderLayout());
        Button next=new Button("next");
        frame.add(next,BorderLayout.SOUTH);
        next.addActionListener(new ActionListener() {
            //now=0表示下一步进行激活操作，now=1 表示下一步进行step操作
            private int now=0;
            private byte t1,t2,t3;
            @Override
            public void actionPerformed(ActionEvent e) {
                if(now==0){
                    t1=(byte)(random.nextInt()%10);
                    t2=(byte)(random.nextInt()%10);
                    t3=(byte)(t1+t2);
                    //激活
                    statment.activeByte(t1);
                    statment1.activeByte(t2);
                    //取网络输出
                    byte out=outstatment.getByte();
                    System.out.printf("学习前:\n%d + %d = %d\n",t1,t2,out);
                    frame.repaint();

                    now=1;
                }
                else if(now==1){
                    //给正确答案激活
                    outstatment.activeByte(t3);
                    byte out=outstatment.getByte();
                    System.out.printf("学习后:\n %d + %d = %d\n",t1,t2,out);
                    frame.repaint();
                    now=2;
                }
                else if(now==2){
                    system.step();
                    System.out.printf("Step操作完成\n");
                    //重绘
                    frame.repaint();
                    now=0;
                }


            }
        });
    }
}

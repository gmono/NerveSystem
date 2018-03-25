package com.company;


import javax.naming.event.NamingEvent;
import java.awt.*;
import java.util.Random;

class MyFrame extends Frame{
    public MyFrame() throws HeadlessException {
        super();
    }

    public MyFrame(String title) throws HeadlessException {
        super(title);
    }

    @Override
    public void paint(Graphics g) {
        g.drawRect(100,100,100,100);
        g.drawString("Hello world",100,100);
        super.paint(g);
    }
}
public class Main {

    public static void main(String[] args) {
	// write your code here
        //system
        NerveSystem system=new NerveSystem(100);
        NumberStatment statment=new NumberStatment(system,0);
        NumberStatment statment1=new NumberStatment(system,10);
        NumberStatment outstatment=new NumberStatment(system,20);
        Random random=new Random();
        Frame frame=new NerveSystemView(system,100);
        frame.setVisible(true);
        for(int i=0;i<1000;++i){
            byte t1=(byte)(random.nextInt()%10);
            byte t2=(byte)(random.nextInt()%10);
            byte t3=(byte)(t1+t2);
            statment.activeByte(t1);
            statment1.activeByte(t2);
            byte out=outstatment.getByte();
            System.out.printf("%d + %d =%d\n",t1,t2,out);
            outstatment.activeByte(t3);
            system.step();
            //重绘
            frame.repaint(100);
        }
    }
}

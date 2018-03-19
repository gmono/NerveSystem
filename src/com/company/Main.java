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
        Frame frame=new MyFrame("神经系统激活图");
        frame.setSize(500,500);
        frame.setVisible(true);
        //system
        NerveSystem system=new NerveSystem(1000);
        NumberStatment statment=new NumberStatment(system,0);
        NumberStatment statment1=new NumberStatment(system,100);
        NumberStatment outstatment=new NumberStatment(system,200);
        Random random=new Random();
        for(int i=0;i<1000;++i){
            byte t1=(byte)(random.nextInt()%10);
            byte t2=(byte)(random.nextInt()%10);
            byte t3=(byte)(t1+t2);
            statment.activeByte(t1);
            statment1.activeByte(t2);
            outstatment.activeByte(t3);
        }
    }
}

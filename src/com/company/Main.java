package com.company;


import java.awt.*;

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
        //Graphics
    }
}

package Gui;
import java.awt.*;
import javax.swing.*;
//import java.awt.event.*;
import Buff.*;
import Nation.*;
public class OneNationSimulatorGui extends JFrame implements Runnable{
	private static final long serialVersionUID = 1L;
	private final int number_of_religion=4;//Á¾±³ ¼ö
	
	Thread main_thread;
	
	Image buff_image;
	Graphics buff_graphics;
	
	Nation nation;
	
	int turn;
	
	public OneNationSimulatorGui(){
		super("NationTesting");
		setSize(1000,1000);
		setVisible(true);
		setResizable(false);
		
		nation = new Nation("Korea",10000,1);
		
		main_thread=new Thread(this);
		main_thread.start();
		turn=0;
	}
	
	public void run(){
		while(true){
			try{
				repaint();
				
				if(turn==15){
					SmallBuff sb = new SmallBuff("power up",10);
					sb.modifyBuffTheta(0, 1, 2);
					sb.modifyBuffTheta(1, 0, 0.5);
					nation.addSmallBuff(sb);
				}
				
				if(turn==30){
					nation.unlockReligion(2);
					SmallBuff sb = new SmallBuff("new religion",5);
					sb.modifyBuffTheta(0, 2, 4);
					sb.modifyBuffTheta(0, 1, 4);
					nation.addSmallBuff(sb);
				}
				
				nation.updatePopulation();
				turn++;
				Thread.sleep(1000);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void paint(Graphics g){
		buff_image=createImage(getWidth(), getHeight());
		buff_graphics=buff_image.getGraphics();
		update(buff_graphics);
		g.drawImage(buff_image, 0, 0, null);
	}
	
	public void update(Graphics g){
		drawInfo(g);
		drawSticks(g);
	}
	
	private void drawInfo(Graphics g){
		g.setFont(new Font("µ¸¿ò",Font.PLAIN,50));
		g.drawString(nation.getName(), 50, 100);
		g.drawString(String.valueOf(turn), 900, 100);
		g.drawString("Pop.",50,200);
		for(int i=0;i<=number_of_religion;i++){
			g.drawString(String.valueOf(i), 50, 300+150*i);
		}
	}
	private void drawSticks(Graphics g){
		int ratio=nation.getPopulation()/100;
		g.setColor(Color.BLUE);
		g.fillRect(200, 200-30, nation.getPopulation()*5/ratio, 10);
		g.drawString(String.valueOf(nation.getPopulation()),200+nation.getPopulation()*5/ratio,200);
		for(int i=0;i<=number_of_religion;i++){
			if(i==nation.getMostDominateReligion()){
				g.setColor(Color.RED);
			}else{
				g.setColor(Color.GREEN);
			}
			g.fillRect(200, 300+150*i-30, nation.getPopulationByReligionAt(i)*5/ratio, 10);
			g.drawString(String.valueOf(nation.getPopulationByReligionAt(i)),200+nation.getPopulationByReligionAt(i)*5/ratio,300+150*i);
		}
	}
	
	public static void main(String args[]){
		OneNationSimulatorGui onsg = new OneNationSimulatorGui();
		onsg.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}

package Gui;
import java.awt.*;
import javax.swing.*;
import Buff.*;
import Religion.Religion;
import World.*;
public class ThreeNationSimulatorGui extends JFrame implements Runnable{
	private static final long serialVersionUID = 1L;
	
	Thread main_thread;
	
	Image buff_image;
	Graphics buff_graphics;
	
	World world;
	Nation nation;
	
	int turn;
	
	public ThreeNationSimulatorGui(){
		super("NationTesting");
		setSize(2000,2000);
		setVisible(true);
		setResizable(false);
		
		world = new World();
		
		main_thread=new Thread(this);
		main_thread.start();
	}
	
	public void run(){
		while(true){
			try{
				repaint();
				if(turn==1){
					SmallBuff sb = new SmallBuff("강력한 도움",5);
					sb.modifyBuffTheta(Religion.ATHEISM, Religion.PLAYER, 3);
					world.getNationsAt(0).addSmallBuff(sb);
					world.getReligionsAt(Religion.PLAYER).setDef(1.7);
					world.getReligionsAt(Religion.PLAYER).setOff(1.0);
				}
				world.update();
				turn++;
				Thread.sleep(1000);//***임의의 값을 붙임 밸런스 조정가능
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
		drawInfoOfWorldAt(g,0,0);
		drawInfoOfNationAt(g,0,1000,world.getNationsAt(0));
		drawInfoOfNationAt(g,1000,0,world.getNationsAt(1));
		drawInfoOfNationAt(g,1000,1000,world.getNationsAt(2));
		
		drawSticksOfWorldAt(g, 0, 0);
		drawSticksOfNationAt(g, 0, 1000, world.getNationsAt(0));
		drawSticksOfNationAt(g, 1000, 0, world.getNationsAt(1));
		drawSticksOfNationAt(g, 1000, 1000, world.getNationsAt(2));
	}
	private void drawInfoOfWorldAt(Graphics g, int x, int y){
		g.setFont(new Font("돋움",Font.PLAIN,50));
		g.drawString("World", x+50, y+100);
		g.drawString(String.valueOf(turn), x+900, y+100);
		g.drawString("인구",x+50,y+200);
		for(int i=0;i<=Religion.NUMBER_OF_RELIGION;i++){
			g.drawString(world.getReligionsAt(i).getName(), x+50, y+300+150*i);
		}
	}
	private void drawInfoOfNationAt(Graphics g, int x, int y, Nation nation){
		g.setFont(new Font("돋움",Font.PLAIN,50));
		g.drawString(nation.getName(), x+50, y+100);
		g.drawString("인구",x+50,y+200);
		for(int i=0;i<=Religion.NUMBER_OF_RELIGION;i++){
			g.drawString(world.getReligionsAt(i).getName(), x+50, y+300+150*i);
		}
	}
	private void drawSticksOfWorldAt(Graphics g, int x, int y){
		int ratio=world.getPopulation()/100;
		g.setColor(Color.BLUE);
		g.fillRect(x+200, y+200-30, world.getPopulation()*5/ratio, 10);
		g.drawString(String.valueOf(world.getPopulation()),x+200+world.getPopulation()*5/ratio,y+200);
		for(int i=0;i<=Religion.NUMBER_OF_RELIGION;i++){
			if(i==world.getMostDominateReligion()){
				g.setColor(Color.RED);
			}else{
				g.setColor(Color.GREEN);
			}
			g.fillRect(x+200, y+300+150*i-30, world.getPopulationByReligionAt(i)*5/ratio, 10);
			g.drawString(String.valueOf(world.getPopulationByReligionAt(i)),x+200+world.getPopulationByReligionAt(i)*5/ratio,y+300+150*i);
		}
	}
	private void drawSticksOfNationAt(Graphics g, int x, int y, Nation nation){
		int ratio=nation.getPopulation()/100;
		g.setColor(Color.BLUE);
		g.fillRect(x+200, y+200-30, nation.getPopulation()*5/ratio, 10);
		g.drawString(String.valueOf(nation.getPopulation()),x+200+nation.getPopulation()*5/ratio,y+200);
		for(int i=0;i<=Religion.NUMBER_OF_RELIGION;i++){
			if(i==nation.getMostDominateReligion()){
				g.setColor(Color.RED);
			}else{
				g.setColor(Color.GREEN);
			}
			g.fillRect(x+200, y+300+150*i-30, nation.getPopulationByReligionAt(i)*5/ratio, 10);
			g.drawString(String.valueOf(nation.getPopulationByReligionAt(i)),x+200+nation.getPopulationByReligionAt(i)*5/ratio,y+300+150*i);
		}
	}
	
	public static void main(String args[]){
		ThreeNationSimulatorGui tnsg = new ThreeNationSimulatorGui();
		tnsg.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}

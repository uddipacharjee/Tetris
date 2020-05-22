package com.tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Tetris extends JFrame implements KeyListener,ActionListener{
	BoardPanel panel;
	SidePanel spanel;
	Timer t;
	boolean pause;
	int delay=1000;
	
	public Tetris() {
		setLayout(new BorderLayout());
		panel=new BoardPanel();
		//panel.setBackground(Color.YELLOW);
		spanel=new SidePanel();
		add(panel,BorderLayout.CENTER);
		add(spanel,BorderLayout.EAST);
		
		addKeyListener(this);
		t=new Timer(delay, this);
		t.start();
		
		setSize(320+200, 640+35);
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(pause==false && e.getKeyCode()==KeyEvent.VK_RIGHT){
			panel.shiftRight();
			repaint();
		}
		else if(pause==false && e.getKeyCode()==KeyEvent.VK_LEFT){
			panel.shiftLeft();
			repaint();
		}
		else if(pause==false && e.getKeyCode()==KeyEvent.VK_DOWN){
			panel.shiftDown();
			repaint();
		}
		else if(pause==false && e.getKeyCode()==KeyEvent.VK_UP){
			panel.rotate();
			repaint();
		}
		else if(e.getKeyCode()==KeyEvent.VK_SPACE){
			if(pause==false){
				t.stop();
				pause=true;
			}
			else{
				t.restart();
				pause=false;
			}
		}
		else if(e.getKeyCode()==KeyEvent.VK_ENTER){
			setVisible(false);
			Tetris f=new Tetris();
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	//	int max=panel.maxX();
		if(!panel.isRow1Clear()){
			panel.gameOver=true;
			repaint();
			t.stop();
		}
		else{
			panel.shiftDown();
			
			if(panel.collided){
				panel.checkRow();
				panel.createNewPiece();
			}
			spanel.score=panel.points;
			if(panel.points>=50)delay=400;
			repaint();
			if(panel.points>=20){
				panel.winMessage=true;
				repaint();
				t.stop();
			}
		}
		
	}
	
	public static void main(String[] args) {
		Tetris frame=new Tetris();
	}
}

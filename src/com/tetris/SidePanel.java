package com.tetris;

import javax.swing.*;
import java.awt.*;

public class SidePanel extends JPanel{
	public int score;
	public SidePanel() {
		setPreferredSize(new Dimension(200, getHeight()));
		setBackground(Color.BLACK);
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.GREEN);
		g.setFont(new Font("sherif",Font.BOLD,20));
		g.drawString("Score: "+score, 20, 100);
		g.setColor(Color.WHITE);
		g.drawString("control:", 10, 300);
		g.setFont(new Font("sherif",Font.BOLD,12));
		g.drawString("Left Arrow  :  Move left", 10, 350);
		g.drawString("Right Arrow :  Move Right", 10, 370);
		g.drawString("Up Arrow    :  Rotate", 10, 390);
		g.drawString("Down Arrow  :  Drop", 10, 410);
		g.drawString("Space       :  Pause game", 10, 430);
		g.drawString("Enter   :  New Game", 10, 500);
	}
}

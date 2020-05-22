package com.tetris;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class BoardPanel extends JPanel {
	Color c[] = { Color.WHITE, Color.BLUE, Color.GREEN, Color.ORANGE, Color.PINK, Color.RED, Color.YELLOW, Color.CYAN };
	int color;
	Random r = new Random();
	int x1, y1, x2, y2, x3, y3, x4, y4;

	int board[][];
	boolean filluped[][];
	
	private int selectPiece;
	public boolean collided;
	
	public int points;
	public boolean gameOver;
	public boolean winMessage;
	private BufferedImage image;
	private boolean welcomeMessage;
	public BoardPanel() {
		board = new int[20][10];
		filluped=new boolean[20][10];
		
		createNewPiece();
		
		try {
			image=ImageIO.read(new File("cup.jpeg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		/*if(!welcomeMessage){
			setBackground(Color.PINK);
			g.setColor(Color.BLACK);
			g.drawString("Welcome", 100,300);
			g.drawString("Tetris", 120, 400);
			welcomeMessage=true;
		}*/
		if(winMessage){
			setBackground(Color.WHITE);
			g.setColor(Color.BLUE);
			g.setFont(new Font("sherif",Font.BOLD,35));
			g.drawString("You win", 100, 200);
			g.drawImage(image,100,250,this);
		}
		else if(gameOver){
			g.setColor(Color.RED);
			g.setFont(new Font("sherif",Font.BOLD,35));
			g.drawString("Game Over", 50, 300);
		}
		else{
			for (int i = 0; i < 20 * 32; i += 32) {
				for (int j = 0; j < 10 * 32; j += 32) {
					g.setColor(c[board[i / 32][j / 32]]);
					g.fillRect(j, i, 30, 30);
				}
			}
			g.setColor(Color.BLACK);
			g.fillRect(0, 641, 320, 35);
		}
	}

	public void colorBoard() {
		board[x1][y1] = color;
		board[x2][y2] = color;
		board[x3][y3] = color;
		board[x4][y4] = color;
	}

	private void uncolorBoard() {
		board[x1][y1] = 0;
		board[x2][y2] = 0;
		board[x3][y3] = 0;
		board[x4][y4] = 0;
	}

	public int maxY() {
		int max = Math.max(y1, Math.max(y2, Math.max(y3, y4)));
		return max;
	}

	public int maxX() {
		int max = Math.max(x1, Math.max(x2, Math.max(x3, x4)));
		return max;
	}

	public void shiftRight() {
		int max = maxY();
		if (max < 9 && checkRight()) {
			uncolorBoard();
			y1++;
			y2++;
			y3++;
			y4++;
			colorBoard();
		}
	}
	private boolean checkRight(){
		if(filluped[x1][y1+1] || filluped[x2][y2+1] || filluped[x3][y3+1] || filluped[x4][y4+1])return false;
		return true;
	}
	public void shiftLeft() {
		int min = Math.min(y1, Math.min(y2, Math.min(y3, y4)));
		if (min > 0 && checkLeft()) {
			uncolorBoard();
			y1--;
			y2--;
			y3--;
			y4--;
			colorBoard();
		}
	}
	public boolean isRow1Clear(){
		for(int i=0;i<10;i++){
			if(filluped[1][i]){
				return false;
			}
		}
		return true;
	}
	private boolean checkLeft(){
		if(filluped[x1][y1-1] || filluped[x2][y2-1] || filluped[x3][y3-1] || filluped[x4][y4-1])return false;
		return true;
	}
	public void shiftDown() {
		int max = maxX();

		if (max < 19 && checkDown()){
			uncolorBoard();
			x1++;
			x2++;
			x3++;
			x4++;
			colorBoard();
		} 
		else{
			filluped[x1][y1]=true;
			filluped[x2][y2]=true;
			filluped[x3][y3]=true;
			filluped[x4][y4]=true;
			
			collided = true;
		}
	}
	private boolean checkDown() {
		if(filluped[x1+1][y1] || filluped[x2+1][y2] || filluped[x3+1][y3] || filluped[x4+1][y4])return false;
		
		return true;
	}
	public void checkRow(){
		for(int i=2;i<20;i++){
			for(int j=0;j<10;j++){
				if(!filluped[i][j])break;
				if(j==9){
					clearRow(i);
					stepDown(i);
					points+=10;
				}
			}
		}
	}
	public void clearRow(int row){
		for(int i=0;i<10;i++){
			board[row][i]=0;
		}
	}
	public void stepDown(int row){
		for(int i=row-1;i>1;i--){
			for(int j=0;j<10;j++){
				board[i+1][j]=board[i][j];
				filluped[i+1][j]=filluped[i][j];				
			}
		}
		for(int i=0;i<10;i++){
			//board[1][j]=board[i][j];
			filluped[2][i]=filluped[1][i];
		}
	}
	
	
	public void rotate() {
		if (selectPiece == 0) {
			boolean movedRightNot=true;
			boolean movedLeftNot=true;

			if (y3 == 0){
				shiftRight(); // here middle piece x3,y3
				movedRightNot=checkRight();
			}
			else if (y3 == 9){
				shiftLeft();
				movedLeftNot=checkLeft();
			}
			if (x3 == 19) {
				uncolorBoard();
				x1--;					// shiftUp
				x2--;
				x3--;
				x4--;
				colorBoard();
			}
			uncolorBoard();
			if(movedRightNot && movedLeftNot){
			if (x2 == x3 && x3 == x4 && y1 == y3) {
				boolean isMovable=false;
				if (x1 < x3 ) {
					if(!filluped[x3+1][y3]){
						x1++;
						y1++;
						isMovable=true;
					}
				} else {
					if(!filluped[x3-1][y3]){
						x1--;
						y1--;
						isMovable=true;
					}
				}
				if(isMovable){
					if (y2 < y3){
						x2--;
						x4++;
					}
					else{
						x2++;
						x4--;
					}
					y2 = y3;
					y4 = y3;
				}
			} else if (y2 == y3 && y3 == y4 && x1 == x3) {
				boolean isMovable=false;
				if (y1 > y3) {
					if(!filluped[x3][y3-1]){
						x1++;
						y1--;
						isMovable=true;
					}
				} 
				else {
					if(!filluped[x3][y3+1]){
						x1--;
						y1++;
						isMovable=true;
					}
				}
				if(isMovable){
					if (x2 < x3){
						y2++;
						y4--;
					}
					else{
						y2--;
						y4++;
					}
					x2 = x3;
					x4 = x3;
				}
			}
			}
			colorBoard();
		} else if (selectPiece == 2) {
			//if(x2==19)
			boolean isMovable=true;
			if(y2==0){						// middle piece x2,y2
				if(!filluped[x2][y2+1]&&!filluped[x2][y2+2]&&!filluped[x2][y2+3]){
					shiftRight();
					isMovable=checkRight();
				}
				else isMovable=false;
			}
			else if(y2==8){
				if(!filluped[x2][y2-2]&&!filluped[x2][y2-1]&&!filluped[x2][y2+1]){
					if(!filluped[x1][y1-2]&&!filluped[x2][y2-2]&&!filluped[x3][y3-2]&&!filluped[x4][y4-2]){
						shiftLeft();
						isMovable=checkLeft();
					}
					else isMovable=false;
				}
				else isMovable=false;
			}
			else if(y2==9){
				if(!filluped[x2][y2-3]&&!filluped[x2][y2-2]&&!filluped[x2][y2-1]){
					if(!filluped[x1][y1-2]&&!filluped[x2][y2-2]&&!filluped[x3][y3-2]&&!filluped[x4][y4-2]){
						if(!filluped[x1][y1-3]&&!filluped[x2][y2-3]&&!filluped[x3][y3-3]&&!filluped[x4][y4-3]){
							shiftLeft();
							isMovable=checkLeft();
							shiftLeft();
							isMovable=checkLeft();
						}
						else isMovable=false;
					}
					else isMovable=false;
				}
				else isMovable=false;
			}
			
			if(x2>0 && x2<=17){	
				uncolorBoard();
				if(x1==x2 && x2==x3 && x3==x4){
					if(!filluped[x2-1][y2]&&!filluped[x2+1][y2] && !filluped[x2+2][y2]){
						x1=x2-1;
						x3=x2+1;
						x4=x2+2;
						y1=y2;y3=y2;y4=y2;
					}
				}
				else if(y1==y2 && y2==y3 && y3==y4){
					if( isMovable && !filluped[x2][y2-1]&&!filluped[x2][y2+1]&&!filluped[x2][y2+2]){
						
						x1=x2;x3=x2;x4=x2;
						y1=y2-1;
						y3=y2+1;
						y4=y2+2;
					}
				}
				colorBoard();
			}

		} else if (selectPiece == 3) {

		} else if (selectPiece == 4) {

		} else if (selectPiece == 5) {

		} else if (selectPiece == 6) {

		}
	}

	public void createNewPiece() {
		collided = false;
		Random random = new Random();
		selectPiece = random.nextInt(3);

		// TShape
		if (selectPiece == 0) {
			x1 = 0;
			y1 = 4;
			x2 = 1;
			y2 = 3;
			x3 = 1;
			y3 = 4;
			x4 = 1;
			y4 = 5;
			color = 1;
		}
		// Square
		else if (selectPiece == 1) {
			x1 = 0;
			y1 = 4;
			x2 = 0;
			y2 = 5;
			x3 = 1;
			y3 = 4;
			x4 = 1;
			y4 = 5;
			color = 2;
		}
		// IShape
		else if (selectPiece == 2) {
			x1 = 0;
			y1 = 3;
			x2 = 0;
			y2 = 4;
			x3 = 0;
			y3 = 5;
			x4 = 0;
			y4 = 6;
			color = 3;
		}
		// LShape
		else if (selectPiece == 3) {
			x1 = 0;
			y1 = 3;
			x2 = 0;
			y2 = 4;
			x3 = 0;
			y3 = 5;
			x4 = 1;
			y4 = 3;
			color = 4;
		}
		// JShape
		else if (selectPiece == 4) {
			x1 = 0;
			y1 = 3;
			x2 = 0;
			y2 = 4;
			x3 = 0;
			y3 = 5;
			x4 = 1;
			y4 = 5;
			color = 5;
		}
		// SShape
		else if (selectPiece == 5) {
			x1 = 0;
			y1 = 4;
			x2 = 0;
			y2 = 5;
			x3 = 1;
			y3 = 3;
			x4 = 1;
			y4 = 4;
			color = 6;
		}
		// ZShape
		else if (selectPiece == 6) {
			x1 = 0;
			y1 = 3;
			x2 = 0;
			y2 = 4;
			x3 = 1;
			y3 = 4;
			x4 = 1;
			y4 = 5;
			color = 7;
		}
		colorBoard();
	}
}

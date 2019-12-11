import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Board{
	private int startX;
	private int startY;
	private ArrayList<Piece> pieces;
	private Checkers checkers;
	//private boolean[][] inAir;
	private boolean inAir;
	private int airX, airY;
	
	private int mouseX; 
	private int mouseY; 
	private boolean doDrag;
	
	private Canvas canvas;
	private BufferedImage bf;
	public Board(Checkers checkers)
	{
		bf = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
		this.checkers = checkers;
		this.pieces = checkers.getPieces();
		//inAir = new boolean[8][8];// maybe try optimise later, a little memory inefficient
		inAir = false;
		airX = airY = -1;
		startX = 50;
		startY = 50;
		JFrame frame = new JFrame("Checkers");
		
		
		canvas = new Canvas(){
			
			private static final long serialVersionUID = 1L;

			@Override
			public void paint(Graphics g)
			{
				paintComponent(bf.getGraphics());
				
				g.drawImage(bf, 0, 0, null);
			}
			@Override 
			public void update(Graphics g)
			{
				paint(g);
			}
			
			
		};
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				if(e.getX() > 210 && e.getX() < 280 && e.getY() > 470 && e.getY() < 490)//we are in the undo button
				{
					checkers.undo();
					canvas.repaint();
				}
				else if(e.getX() > 310 && e.getX() < 405 && e.getY() > 470 && e.getY() < 490)//we are in the undox2 button
				{
					checkers.undo();
					checkers.undo();
					canvas.repaint();
				}
				else
				{
					mouseDown(e);
				}
				//Graphics g = canvas.getGraphics();
				//paintComponent(g);
				//g.dispose();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				if(doDrag)
					canvas.repaint();
				mouseUp(e);
				
			//	Graphics g = canvas.getGraphics();
			//	paintComponent(g);
			//g.fillOval(mouseX - 20 , mouseY - 20, 40, 40);
			//	g.dispose();
				
			}
			
		});
		canvas.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e)
			{
				mouseDrag(e);
				if(doDrag)
					canvas.repaint();
			}
			
		});
		canvas.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(canvas);
		frame.pack();
		frame.setVisible(true);
	
	}
	public Piece getPiece(int row, int col)
	{
		for(int i = 0; i < pieces.size(); i++)
			if(pieces.get(i).row == row && pieces.get(i).col == col)
				return pieces.get(i);
		return null;
	}
	private void mouseDown(MouseEvent e)
	{
		int boardX = (e.getX() - startX)/50; // find the x coordinate of the block we clicked in
		int boardY = (e.getY() - startY)/50; // find the y coordinate of the block we clicked in
		mouseX = e.getX();
		mouseY = e.getY();
		
		//if(e.getX() < start)
		if(getPiece(boardY, boardX) != null)
		{
			doDrag = true;
			inAir = true;
			airX = boardX;
			airY = boardY;
		}
			//inAir[boardY][boardX] = true;
		
				//System.out.println("X:" + boardX + " Y:" + boardY);
				
	}
	private void mouseUp(MouseEvent e)
	{
		int boardX = (e.getX() - startX)/50; // find the x coordinate of the block we released in
		int boardY = (e.getY() - startY)/50; // find the y coordinate of the block we released in
		doDrag = false;
	
		if(inAir)
		{
			checkers.move(airY, airX, boardY, boardX);
			inAir = false;
			
			
		}
	}
	
	
	private void mouseDrag(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
	}
	public void refresh()
	{
		canvas.repaint();
	}
	private void paintComponent(Graphics g)
	{
		
		
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, 500, 500);
		//g.clearRect(0, 0, 500, 500);
		g.setColor(Color.WHITE);
	
		for(int i = 0; i < 8; i++)
		{
			for(int j = 0; j < 8; j ++)
			{
				if((i + j) % 2 == 0)
					g.setColor(Color.WHITE);
				else
					g.setColor(Color.BLACK);
				g.fillRect(startX + i*50, startY + j*50, 50, 50);
			}
		}
		for(int i = 0; i < pieces.size(); i++)
		{
			
			Piece p = pieces.get(i);
			Color c = p.colour == 'w' ? Color.WHITE : Color.RED;
			g.setColor(c);
			if(!inAir || (inAir && airX != p.col || airY != p.row))//!inAir[p.row][p.col])
			{
				
				g.fillOval(startX + p.col*50 + 5 , startY + p.row*50 + 5, 40, 40);
				if(p.king)
				{
					g.setColor(Color.BLACK);
					g.drawString("K", startX + p.col*50 + 22, startY + p.row*50 + 30);
			
				}
			}
			else
			{
				g.fillOval(mouseX - 20 , mouseY - 20, 40, 40);
				if(p.king)
				{
					g.setColor(Color.BLACK);
					g.drawString("K", mouseX - 3, mouseY + 7);
			
				}
			}
			
			//undo button
			g.setColor(Color.BLUE);
			g.drawRect(210, 470, 70, 20);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Dialog", Font.PLAIN, 15));
			g.drawString("UNDO", 226, 486);
			
			//undox2 button
			g.setColor(Color.BLUE);
			g.drawRect(310, 470, 95, 20);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Dialog", Font.PLAIN, 15));
			g.drawString("UNDO X2", 326, 486);
			
			// line numbers
			//top row
			g.setColor(Color.DARK_GRAY);
			for(Integer j = 0; j < 8; j++)
				g.drawString(j.toString(), 72 + 50 * j, 40);
			//left row
			for(Integer j = 0; j < 8; j++)
				g.drawString(j.toString(), 33, 80 + 50 * j);
			
		}
	
	}

}
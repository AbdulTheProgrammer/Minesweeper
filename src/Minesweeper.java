// The "MinesweeperBETA" class.
import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.awt.Graphics;




@SuppressWarnings("serial")
public class Minesweeper extends JApplet implements ActionListener
{
	// Place instance variables here
	JButton[] [] Board;
	Image mine_scaled, flag_scaled,explode_scaled, mine_original, flag_original,explode_original ;
	ImageIcon flagIcon, mineIcon,explodeIcon;
	JPanel game, actionbar,bottombar;
	JButton easy,medium,hard;
	JLabel gamestate; 
	int move, disabledbombs;
	int dim ,numofbombs;
	boolean win;

	public void init () // setup intial game conditions
	{
		numofbombs = 40;
		move = 0;
		dim = 16; 
		resize (600, 600);
		//set up button array and general game layout
		Board = new JButton [dim] [dim];
		setLayout(new BorderLayout());
		gamestate = new JLabel("Game In Progress...");
		gamestate.setFont(new Font("times new roman", Font.TRUETYPE_FONT, 18));
		gamestate.setForeground(Color.white);
		game = new JPanel();
		bottombar = new JPanel(new BorderLayout());
		actionbar = new JPanel();
		actionbar.setPreferredSize(new Dimension(30, 30));
		easy = new JButton("Easy");
		medium = new JButton("Medium"); 
		hard = new JButton("Hard");
		easy.addActionListener(this);
		hard.addActionListener(this);
		medium.addActionListener(this);
		easy.setPreferredSize(new Dimension (100, 30));
		hard.setPreferredSize(new Dimension (100, 30));
		medium.setPreferredSize(new Dimension (100, 30));
		game.setLayout (new GridLayout (dim, dim,0,0));
		game.setBackground(Color.GRAY);
		actionbar.setBackground(Color.GRAY);
		bottombar.setBackground(Color.GRAY);
		bottombar.add(gamestate);
		actionbar.add(easy);
		actionbar.add(medium);
		actionbar.add(hard);
		add(game,BorderLayout.CENTER);
		add(actionbar, BorderLayout.NORTH);
		add(bottombar,BorderLayout.SOUTH);
		//import game images
		flag_original= getImage (getCodeBase (), "flag.png");
		flag_scaled= flag_original.getScaledInstance( 23, 23,  java.awt.Image.SCALE_SMOOTH ) ;
		flagIcon = new ImageIcon(flag_scaled);
		mine_original = getImage (getCodeBase (), "mine.png");
		mine_scaled= mine_original.getScaledInstance( 23, 23,  java.awt.Image.SCALE_SMOOTH ) ;
		mineIcon = new ImageIcon(mine_scaled);
		explode_original = getImage (getCodeBase (), "explosion.png");
		explode_scaled= explode_original.getScaledInstance( 30, 30,  java.awt.Image.SCALE_SMOOTH ) ;
		explodeIcon = new ImageIcon(explode_scaled); 
		UIManager.getDefaults().put("Button.disabledText", Color.magenta);


		for (int i = 0 ; i < dim ; i++) 
		{
			for (int j = 0 ; j < dim; j++)
			{

				Board [i] [j] = new JButton ("");
				Board[i][j].setName("");	
				}
		}
		
		for (int i = 0 ; i < dim ; i++) // add buttons to screen
		{
			for (int j = 0 ; j < dim ; j++)
			{
				Board [i] [j].addActionListener (this);
				Board[i][j].addMouseListener(new MouseAdapter(){

					public void mouseClicked(MouseEvent e){
						Object source = e.getSource ();
						if(SwingUtilities.isRightMouseButton(e)) { 
							for (int i = 0 ; i < dim ; i++)
							{
								for (int j = 0 ; j < dim ; j++)
								{
									if (source == Board[i][j] && Board [i] [j].getIcon() != flagIcon && Board[i][j].getBackground() != Color.DARK_GRAY ) {
										Board [i] [j].setEnabled (false);
										Board [i] [j].setDisabledIcon (flagIcon);
										Board [i] [j].setIcon (flagIcon);
									}
									else if (source == Board[i][j] && Board [i] [j].getIcon() == flagIcon && Board[i][j].getBackground() != Color.DARK_GRAY) {
										Board [i] [j].setEnabled (true);
										Board [i] [j].setDisabledIcon (null);
										Board [i] [j].setIcon (null);

									}
									if (Board[i][j].getName().equals("Mine") && Board [i] [j].getIcon() == flagIcon) {
										disabledbombs++;
									}
									if(disabledbombs == numofbombs) { 
										revealAll();
										win = true;
										bottombar.remove(gamestate);
										gamestate = new JLabel("You Won!!");
										gamestate.setFont(new Font("times new roman", Font.TRUETYPE_FONT, 18));
										gamestate.setForeground(Color.white);
										bottombar.add(gamestate);
										bottombar.validate();
									}
								}
							}
							disabledbombs = 0;
						}
					}
				});

				Board[i][j].setBackground(Color.GRAY);
				game. add(Board [i] [j]);
				Board [i] [j].setBorder ((BorderFactory.createRaisedBevelBorder()));
			}
		}


	}


	public void paint (Graphics g) // output to screen method
	{
		super.paint (g);

	}


	public void actionPerformed (ActionEvent ae) // method to manage user button presses
	{
		Object source = ae.getSource ();
		move++;
		for (int i = 0 ; i < dim ; i++)
		{
			for (int j = 0 ; j < dim ; j++)
			{
				if (source == Board [i] [j] && move == 1) { 
					firstMoveSetUp(i,j);
				}

				if (source == Board [i] [j] && Board [i] [j].getName ().equals("Mine")) // condition for when user clicks on a mine
				{
					Board [i] [j].setEnabled (false);
					Board [i] [j].setDisabledIcon (explodeIcon);
					Board [i] [j].setIcon (explodeIcon);
					Board [i] [j].setVerticalTextPosition(SwingConstants.CENTER);
					Board [i] [j].setHorizontalTextPosition(SwingConstants.CENTER);
					Board [i] [j].setName(" ");
					Board [i] [j].setBackground (Color.DARK_GRAY);
					Board [i] [j].setBorder ((BorderFactory.createLoweredBevelBorder()));
					revealAll();

				}
				else if (source == Board [i] [j] && Board [i] [j].getName () != ("Mine")  && Board [i] [j].getName () != (""))
				{
					Board [i] [j].setEnabled (false);
					Board [i] [j].setBackground (Color.DARK_GRAY);
					Board [i] [j].setOpaque(true);
					Board [i] [j].setBorder ((BorderFactory.createLoweredBevelBorder()));
					Board [i] [j].setForeground(Color.magenta);
					Board [i] [j].setText(Board[i][j].getName());

				}
				else if (source == Board [i] [j] && Board [i] [j].getName () == ("")) {
					revealEmptyButtons(i,j );
				}
			}
		}
		//handling the board layout according the difficulty i.e. higher difficulty, more mines
		if (source == easy) { 
			for (int i = 0 ; i < dim ; i++)
			{
				for (int j = 0 ; j < dim ; j++)
				{
					game.remove(Board[i][j]);
					game.validate();
				}
			}
			scaleImages(43,60,Color.yellow);
			newGame(9,10);
		}
		else if (source == medium) { 
			for (int i = 0 ; i < dim ; i++)
			{
				for (int j = 0 ; j < dim ; j++)
				{
					game.remove(Board[i][j]);
					game.validate();
				}
			}
			scaleImages(23,30,Color.magenta);
			newGame(16,40);
		}
		else if (source == hard) { 
			for (int i = 0 ; i < dim ; i++)
			{
				for (int j = 0 ; j < dim ; j++)
				{
					game.remove(Board[i][j]);
					game.validate();
				}
			}
			scaleImages(15,18,Color.cyan);
			newGame(30,99);
		}
	}


	private void revealEmptyButtons(int i, int j) { // recursive method for revealing all the buttons on the board that aren't mines when user clicks an empty square
		Board[i][j].setEnabled (false);
		Board[i][j].setBackground (Color.DARK_GRAY);
		Board[i][j].setText(Board[i][j].getName());
		Board[i][j].setBorder ((BorderFactory.createLoweredBevelBorder()));

		if(Board[i][j].getName() != ("")) {
			return;
		} 
		if (i+1 < dim && Board[i+1][j].isEnabled()) {
			revealEmptyButtons(i+1, j);
		}
		if (j+1 < dim && Board[i][j+1].isEnabled()){
			revealEmptyButtons(i, j+1);
		}
		if (i+1 < dim && j+1 < dim && Board[i+1][j+1].isEnabled()){
			revealEmptyButtons(i+1, j+1);
		}
		if (i-1 >= 0 && Board[i-1][j].isEnabled()){
			revealEmptyButtons(i-1, j);
		}
		if (i-1 >= 0 && j-1 > 0 && Board[i-1][j-1].isEnabled()) {
			revealEmptyButtons(i-1, j-1);
		}
		if (j-1 >= 0 && Board[i][j-1].isEnabled()){
			revealEmptyButtons(i, j-1);
		}
		if (j-1 >= 0 && i+1 < dim && Board[i+1][j-1].isEnabled()){
			revealEmptyButtons(i+1, j-1);
		}
		if (i-1 >= 0 && j+1 < dim && Board[i-1][j+1].isEnabled()){
			revealEmptyButtons(i-1, j+1);
		}

	}
	public void revealAll() { //activated when the user loses or wins game
		for (int i = 0 ; i < dim ; i++)
		{
			for (int j = 0 ; j < dim ; j++)
			{
				if (Board [i] [j].getName ().equals("Mine")&&  Board [i] [j].getIcon() != flagIcon)
				{
					Board [i] [j].setDisabledIcon (mineIcon);
					Board [i] [j].setIcon (mineIcon);
					Board [i] [j].setBackground (Color.DARK_GRAY);
					Board [i] [j].setBorder ((BorderFactory.createLoweredBevelBorder()));

				}
				else if (Board [i] [j].getIcon() == flagIcon && Board [i] [j].getName ().equals("Mine")) {
					Board [i] [j].setDisabledIcon (mineIcon);
					Board [i] [j].setIcon (mineIcon);
					Board [i] [j].setBackground(Color.YELLOW);
					Board [i] [j].setBorder ((BorderFactory.createLoweredBevelBorder()));
				}
				Board [i] [j].setEnabled (false);
			}
		}
		bottombar.remove(gamestate);
		gamestate = new JLabel("You Lost!!");
		gamestate.setFont(new Font("times new roman", Font.TRUETYPE_FONT, 18));
		gamestate.setForeground(Color.white);
		bottombar.add(gamestate);
		bottombar.validate();
	}
	public void firstMoveSetUp(int l, int m) { // method to address the first move of the player. Note the first button the user clicks will always be an empty square, NEVER a bomb
		for (int i = 0 ; i < numofbombs ; i++)
		{
			int k = (int) (Math.random () * dim);
			int y = (int) (Math.random () * dim);
			while(Board [k] [y].getName().equals("Mine") || k == l && y == m || k == l+1 && y == m || k == l+1 && y == m+1 || k == l-1 && y == m || k == l-1 && y == m-1 || k == l && y == m+1 || k == l && y == m-1 || k == l+1 && y == m-1 || k == l-1 && y == m+1){
				k = (int) (Math.random () * dim);
				y = (int) (Math.random () * dim);
			}
			Board [k] [y].setName ("Mine");
		}
		for (int i = 0 ; i < dim ; i++)
		{
			for (int j = 0 ; j < dim ; j++)
			{
				int count = 0;
				if (!Board[i][j].getName().equals("Mine")) {
					if(i+1 < dim && Board[i+1][j].getName().equals("Mine")) {
						count++;
					}
					if(i+1 < dim && j+1 < dim && Board[i+1][j+1].getName().equals("Mine")) {
						count++;
					}
					if(i-1 >= 0 && Board[i-1][j].getName().equals("Mine")) {
						count++;
					}
					if(i-1 >= 0 && j-1 >= 0 && Board[i-1][j-1].getName().equals("Mine")) {
						count++;
					}
					if(j-1 >= 0 &&Board[i][j-1].getName().equals("Mine")) {
						count++;
					}
					if(i-1 >= 0 && j+1 < dim && Board[i-1][j+1].getName().equals("Mine")) {
						count++;
					}
					if(j-1 >= 0 && i+1 < dim && Board[i+1][j-1].getName().equals("Mine")) {
						count++;
					}
					if(j+1 < dim && Board[i][j+1].getName().equals("Mine")) {
						count++;
					}
					if (count!= 0) { 
						Board[i][j].setName("" + count);

					}
				}
			}
		}
	}
	public void newGame(int dim, int numofbombs) { // method to set-up the board according to difficulty 
		
		this.dim = dim;
		this.numofbombs = numofbombs;
		move = 0; 
		Board = new JButton [dim] [dim];
		game.setLayout (new GridLayout (dim, dim,0,0));
		
		for (int i = 0 ; i < dim ; i++)
		{
			for (int j = 0 ; j < dim; j++)
			{

				Board [i] [j] = new JButton ("");
				Board[i][j].setName("");	
				}
		}
		
		for (int i = 0 ; i < dim ; i++)
		{
			for (int j = 0 ; j < dim ; j++)
			{
				Board [i] [j].addActionListener (this);
				Board[i][j].addMouseListener(new MouseAdapter(){

					public void mouseClicked(MouseEvent e){
						Object source = e.getSource ();
						if(SwingUtilities.isRightMouseButton(e)) { 
							for (int i = 0 ; i < dim ; i++)
							{
								for (int j = 0 ; j < dim ; j++)
								{
									if (source == Board[i][j] && Board [i] [j].getIcon() != flagIcon && Board[i][j].getBackground() != Color.DARK_GRAY ) {
										Board [i] [j].setEnabled (false);
										Board [i] [j].setDisabledIcon (flagIcon);
										Board [i] [j].setIcon (flagIcon);
									}
									else if (source == Board[i][j] && Board [i] [j].getIcon() == flagIcon && Board[i][j].getBackground() != Color.DARK_GRAY) {
										Board [i] [j].setEnabled (true);
										Board [i] [j].setDisabledIcon (null);
										Board [i] [j].setIcon (null);

									}
									if (Board[i][j].getName().equals("Mine") && Board [i] [j].getIcon() == flagIcon) {
										disabledbombs++;
									}
									if(disabledbombs == 40) { 
										revealAll();
										win = true;
									}
								}
							}
							disabledbombs = 0;
						}
					}
				});

				Board[i][j].setBackground(Color.GRAY);
				game. add(Board [i] [j]);
				Board [i] [j].setBorder ((BorderFactory.createRaisedBevelBorder()));
			}
		}
		bottombar.remove(gamestate);
		gamestate = new JLabel("Game in Progress...");
		gamestate.setFont(new Font("times new roman", Font.TRUETYPE_FONT, 18));
		gamestate.setForeground(Color.white);
		bottombar.add(gamestate);
		bottombar.validate();
		
	}
	public void scaleImages (int scalevalue1, int scalevalue2, Color mycolor) { // method the scale the images on the board according the size of the button
	flag_scaled= flag_original.getScaledInstance( scalevalue1, scalevalue1,  java.awt.Image.SCALE_SMOOTH ) ;
	flagIcon = new ImageIcon(flag_scaled);
	mine_scaled= mine_original.getScaledInstance( scalevalue1, scalevalue1,  java.awt.Image.SCALE_SMOOTH ) ;
	mineIcon = new ImageIcon(mine_scaled);
	explode_scaled= explode_original.getScaledInstance( scalevalue2, scalevalue2,  java.awt.Image.SCALE_SMOOTH ) ;
	explodeIcon = new ImageIcon(explode_scaled); 
	UIManager.getDefaults().put("Button.disabledText", mycolor);
	}
	public static void main( String[] args )
	{
		new Acme.MainFrame( new Minesweeper(), args, 400, 400 ); // this is activated in order to run the applet as a application 
	}

}


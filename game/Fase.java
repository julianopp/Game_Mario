package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import modelo.Jogador;
import controle.Controle;
import game.Musica;

import java.awt.image.BufferedImage;
import visao.TelaGame;
import visao.TelaPrincipal;

public class Fase extends JPanel implements ActionListener {

	private Image fundo;
	private Sonic sonic;
	private Timer timer;
	private boolean emJogo;
	private List<Inimigo> inimigos;
        private int fundox = 0;
	private int fundoy = 00;
	private int fundo1x = 1250;
	private int fundo1y = 00;
	private int FPS = 1;
        public Jogador jogo;
        public String nome ;
        boolean run;
        public int pontos; 
        private int vidas;    
        
        
        String musica = "src/imagem/overworld.mid";
                   
        public void setNomeJogador(String jogador){
            this.nome = jogador;
        }
        
        public String getNomeJogador() {
		return this.nome;
	}

	public void moveFundo() {
                if (run == true){
		fundox -= 1;
		if (fundox <= (-1250)) {
			fundox = 0;
		}
	}
        }

	public void moveFundo1() {
            if (run == true){
		fundo1x -= 1;
		if (fundo1x == 0) {
			fundo1x = 1250;
                }
            }
                
	}

	private int[][] coordenadas = { { 2380, 400 }, { 2600, 378 }, { 1380, 405 },
			{ 1780, 425 }, { 1280, 410 }, { 1380, 345 }, { 1200, 350 },
			{ 1760, 380 }, { 1790, 415 }, { 1980, 320 }, { 1560, 370 },
			{ 1510, 340 },
	};

	public Fase() {
		setFocusable(true);
		setDoubleBuffered(true);
		addKeyListener(new TecladoAdapter());
		ImageIcon referencia = new ImageIcon("src/imagem/fundoprincipal.png");
                nome = new String();    
		fundo = referencia.getImage();
                vidas = 3;
              
		sonic = new Sonic();
                Musica a = new Musica();

		emJogo = true;

		inicializaInimigos();

		timer = new Timer(10 / FPS, this);
		timer.start();
                a.tocarMusica(musica, 999);
                
                }

	public void inicializaInimigos() {
		inimigos = new ArrayList<Inimigo>();
		for (int i = 0; i < coordenadas.length; i++) {
			inimigos.add(new Inimigo(coordenadas[i][0], coordenadas[i][1]));
		}
	}

	public void paint(Graphics g) {

		Graphics2D graficos = (Graphics2D) g;
		graficos.drawImage(fundo, fundox, fundoy, null);
		graficos.drawImage(fundo, fundo1x, fundo1y, null);
                

		if (emJogo) {                        
                        run = true;
			graficos.drawImage(sonic.getImagem(), sonic.getX(), sonic.getY(),this);

			List<Missel> misseis = sonic.getMisseis();

			for (int i = 0; i < misseis.size(); i++) {

				Missel m = (Missel) misseis.get(i);
				graficos.drawImage(m.getImagem(), m.getX(), m.getY(), this);
			}

			for (int i = 0; i < inimigos.size(); i++) {
				Inimigo in = inimigos.get(i);
				graficos.drawImage(in.getImagem(), in.getX(), in.getY(), this);
			}
			graficos.setFont(new Font("helvica",Font.BOLD,20));
			graficos.setColor(Color.BLACK);
      
                        graficos.drawString("Player: " + getNomeJogador() , 5, 20);
			graficos.drawString("Pontuação: "+ pontos, 1000, 20);
                        graficos.drawString("Inimigos: "+ inimigos.size(), 5, 40);
                        graficos.drawString("Vidas: "+ vidas, 520, 20);
			
		}else{	
                        graficos.setFont(new Font("helvica",Font.BOLD,20));
                        graficos.drawString("Pontuação: "+ pontos, 350, 360);
                        graficos.drawString("Inimigos: "+ inimigos.size(), 582, 360);
                        graficos.drawString("Vidas: "+ vidas, 780, 360);
                        
                        graficos.drawString("ESC - Sair", 5, 560);
                        graficos.drawString("C - Continuar", 1060, 560);
                        graficos.drawString("S - Salvar", 560, 560);
                    
			ImageIcon fimJogo= new ImageIcon("src/imagem/game1.png");
			graficos.drawImage(fimJogo.getImage(), 280, 150,null);
                        
                        run = false;
                       }
		g.dispose();
	}
        
  

	@Override
	public void actionPerformed(ActionEvent arg0) {

		if (inimigos.size() == 0) {
			emJogo = false;
		}

		List<Missel> misseis = sonic.getMisseis();

		for (int i = 0; i < misseis.size(); i++) {

			Missel m = (Missel) misseis.get(i);

			if (m.isVisible()) {
				m.mexer();
			} else {
				misseis.remove(i);;
                                pontos -= 10;
			}
		}

		for (int i = 0; i < inimigos.size(); i++) {

			Inimigo in = inimigos.get(i);

			if (in.isVisible()) {
				in.mexer();
			} else {
				inimigos.remove(i);
                                pontos += 50;
			}
		}

		sonic.mexer();
		checarColisoes();
		moveFundo();
		moveFundo1();
		repaint();
	}

	public void checarColisoes() {

		Rectangle formaNave = sonic.getBounds();
		Rectangle formaInimigo;
		Rectangle formaMissel;

		for (int i = 0; i < inimigos.size(); i++) {

			Inimigo tempInimigo = inimigos.get(i);
			formaInimigo = tempInimigo.getBounds();

			if (formaNave.intersects(formaInimigo)) {

				sonic.setVisible(false);
				tempInimigo.setVisible(false);
                                
				emJogo = false;
			}
		}

		List<Missel> misseis = sonic.getMisseis();

		for (int i = 0; i < misseis.size(); i++) {

			Missel tempMissel = misseis.get(i);
			formaMissel = tempMissel.getBounds();

			for (int j = 0; j < inimigos.size(); j++) {

				Inimigo tempInimigo = inimigos.get(j);
				formaInimigo = tempInimigo.getBounds();

				if (formaMissel.intersects(formaInimigo)) {
					tempInimigo.setVisible(false);
					tempMissel.setVisible(false);
				}
			}
		}

	}

	private class TecladoAdapter extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
                    
			if(e.getKeyCode() == KeyEvent.VK_C){
				emJogo = true;
				sonic = new Sonic();
				inicializaInimigos();
                                vidas = vidas - 1;
                                
                                if(vidas == 0)
                                {
                                run = false;
                                pontos = 0;
                                vidas += 3;
                                }
                       }
                       if(e.getKeyCode() == KeyEvent.VK_ESCAPE){                                                
                           controle.cancelaJogo();
                           System.out.println("Sair do game");
                           
                       }
                       if(e.getKeyCode() == KeyEvent.VK_S){
                           controle.concluiu();
                           System.out.println("Salvar game");
                       }
			sonic.keyPressed(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
                    sonic.keyReleased(e);
		}
	}
}
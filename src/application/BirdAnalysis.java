package application;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class BirdAnalysis {
	private int BLACK = Color.BLACK.getRGB();
	
	private BufferedImage bwImage;
	
	public BirdAnalysis(BufferedImage bwImage) {
		this.bwImage = bwImage;
	}
	
	public BufferedImage outlineBirds(BufferedImage toOutline) {
		UnionFind uf = new UnionFind(bwImage.getWidth() * bwImage.getHeight());
		Graphics toOutlineGraphics = toOutline.getGraphics();
		
		for(int y = 0; y < bwImage.getHeight(); y++) {
			for(int x = 0; x < bwImage.getWidth(); x++) {
				if(bwImage.getRGB(x, y) != BLACK) {
					continue;
				}				
				if(x > 0 && bwImage.getRGB(x - 1, y) == BLACK) {
					uf.join(getPixelId(x, y), getPixelId(x - 1, y));
				}
				if(y > 0 && bwImage.getRGB(x, y - 1) == BLACK) {
					uf.join(getPixelId(x, y), getPixelId(x, y - 1));
				}
				if(y < bwImage.getHeight() - 1 && bwImage.getRGB(x, y + 1) == BLACK) {
					uf.join(getPixelId(x, y), getPixelId(x, y + 1));
				}
				if(x < bwImage.getWidth() - 1 && bwImage.getRGB(x + 1, y) == BLACK) {
					uf.join(getPixelId(x, y), getPixelId(x + 1, y));
				}
				if(x > 0 && y > 0 && bwImage.getRGB(x - 1, y - 1) == BLACK) {
					uf.join(getPixelId(x, y), getPixelId(x - 1, y - 1));
				}
				if(x > 0 && y < bwImage.getHeight() - 1 && bwImage.getRGB(x - 1, y + 1) == BLACK) {
					uf.join(getPixelId(x, y), getPixelId(x - 1, y + 1));
				}
				if(x < bwImage.getWidth() - 1 && y < bwImage.getHeight() - 1 && bwImage.getRGB(x + 1, y + 1) == BLACK) {
					uf.join(getPixelId(x, y), getPixelId(x + 1, y + 1));
				}
				if(x < bwImage.getWidth() - 1 && y > 0 && bwImage.getRGB(x + 1, y - 1) == BLACK) {
					uf.join(getPixelId(x, y), getPixelId(x + 1, y - 1));
				}
			}
		}
		
		Set<Integer> roots = uf.getRoots(1);
		Iterator<Integer> rootIter = roots.iterator();
		while(rootIter.hasNext()) {
			int root = rootIter.next();
			List<Integer> nodes = uf.getNodes(root);
			
			int leftmostX = -1;
			int rightmostX = -1;
			int topmostY = -1;
			int bottommostY = -1;
			
			for(int i = 0; i < nodes.size(); i++) {
				int nodeX = (int) bwImage.getWidth() % nodes.get(i);
				int nodeY = (int) bwImage.getWidth() / nodes.get(i);
				
				if(leftmostX == -1 || nodeX < leftmostX) {
					leftmostX = nodeX;
				}
				if(rightmostX == -1 || nodeX > rightmostX) {
					rightmostX = nodeX;
				}
				if(topmostY == -1 || nodeY < topmostY) {
					topmostY = nodeY;
				}
				if(bottommostY == -1 || nodeY > bottommostY) {
					bottommostY = nodeY;
				}
			}
			int height = bottommostY - topmostY;
			int width = rightmostX - leftmostX;
			toOutlineGraphics.setColor(Color.RED);
			toOutlineGraphics.drawRect(leftmostX, topmostY, width, height);
		}
		toOutlineGraphics.dispose();
		return toOutline;
	}
	
	private int getPixelId(int x, int y) {
		return (bwImage.getWidth() * y) + x;
	}

}

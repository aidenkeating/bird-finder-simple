package application;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UnionFindTest {
	private UnionFind uf;
	
	@BeforeEach
	void beforeEach() {
		uf = new UnionFind(5);
	}
	
	@Test
	void testConnected() {
		uf.join(0, 4);
		assertTrue(uf.connected(0, 4));
		assertFalse(uf.connected(0, 2));
	}
	
	@Test
	void testTreeRoots() {
		uf.join(0, 4);
		uf.join(4, 1);
		assertEquals(1, uf.getRoots(1).size());
	}
	
	@Test
	void getTreeForNode() {
		uf.join(0, 4);
		uf.join(4, 1);
		assertEquals(3, uf.getNodes(0).size());
	}

}

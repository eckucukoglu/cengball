package common;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rare.vector2D;

public class vector2DTest extends TestCase {
	private vector2D cloneSample;
	private double cloneSampleX, cloneSampleY;
	

	@Before
	public void setUp() throws Exception {
		this.cloneSampleX = 5.43;
		this.cloneSampleY = 6.82;
		this.cloneSample = new vector2D(cloneSampleX, cloneSampleY);
	}

	@After
	public void tearDown() throws Exception {
		//nothing to do here
	}

	@Test
	public void testClone() {
		double tmpX, tmpY;
		double delta = 0.0001;
		vector2D vector1 = this.cloneSample.clone();
		
		vector1.setX(0.0);
		vector1.setY(0.0);
		tmpX = cloneSample.getX();
		tmpY = cloneSample.getY();
		
		assertEquals("Case 1: ", cloneSampleX, tmpX, delta);
		assertEquals("Case 2: ", cloneSampleY, tmpY, delta);
	}

}

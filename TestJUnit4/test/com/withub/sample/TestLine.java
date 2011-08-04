/**
 * 
 */
package com.withub.sample;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author 沈东良（Edward Shen） shendl_s@hotmail.com
 *2008-9-24  下午12:43:19
 */
public class TestLine {
    private Line line;
	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		Line line=new Line();
		Point startPoint=new Point();
		startPoint.setX(0);
		startPoint.setY(1);
		line.setStartPoint(startPoint);
		Point endPoint=new Point();
		endPoint.setX(1);
		endPoint.setY(0);
		line.setEndPoint(endPoint);
		this.setLine(line);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.setLine(null);
	}

	/**
	 * Test method for {@link com.withub.sample.Line#getArea()}.
	 */
	@Test
	public final void testGetArea() {
		//fail("Not yet implemented"); // TODO
		Assert.assertEquals("面积错误", 0.0, this.getLine().getArea());
	}

	/**
	 * Test method for {@link com.withub.sample.Line#getLength()}.
	 */
	@Test
	public final void testGetLength() {
		//fail("Not yet implemented"); // TODO
		Assert.assertEquals("长度错误", Math.pow(2, 0.5), this.getLine().getLength());
	}

}

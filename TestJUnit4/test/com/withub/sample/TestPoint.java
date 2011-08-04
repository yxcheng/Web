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
 *2008-9-24  下午12:30:33
 */
public class TestPoint {
	private IShape shape;
	

	public IShape getShape() {
		return shape;
	}

	public void setShape(IShape shape) {
		this.shape = shape;
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
		Point point=new Point();
		point.setX(2);
		point.setY(3);
		point.setZ(888);
		this.setShape(point);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.setShape(null);
	}

	/**
	 * Test method for {@link com.withub.sample.Point#getArea()}.
	 */
	@Test
	public final void testGetArea() {
		//fail("Not yet implemented"); // TODO
		Assert.assertEquals("得到面积错误！", 0.0, this.getShape().getArea());
		
	}

	/**
	 * Test method for {@link com.withub.sample.Point#getLength()}.
	 */
	@Test
	public final void testGetLength() {
		//fail("Not yet implemented"); // TODO
		Assert.assertEquals("得到面积错误！", 0.0, this.getShape().getLength());
	}

}

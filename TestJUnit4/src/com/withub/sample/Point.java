/**
 * 
 */
package com.withub.sample;

/**
 * @author Éò¶«Á¼£¨Edward Shen£© shendl_s@hotmail.com
 *2008-9-24  ÏÂÎç12:16:05
 */
public class Point implements IShape {
	/* (non-Javadoc)
	 * @see com.withub.sample.IShape#getArea()
	 */
	public double getArea(){
		return 0.0;
	}
	
	private double x,y,z;

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	/**
	 * 
	 */
	public Point() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public double getLength() {
		// TODO Auto-generated method stub
		return 0;
	}

}

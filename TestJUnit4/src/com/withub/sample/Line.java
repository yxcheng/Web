/**
 * 
 */
package com.withub.sample;

/**
 * @author Éò¶«Á¼£¨Edward Shen£© shendl_s@hotmail.com
 *2008-9-24  ÏÂÎç12:18:09
 */
public class Line implements IShape {
	private Point startPoint;
	private Point endPoint;
	

	public Point getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}

	/**
	 * 
	 */
	public Line() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.withub.sample.IShape#getArea()
	 */
	public double getArea() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public double getLength() {
		// TODO Auto-generated method stub
		double length=Math.pow(Math.pow((this.getEndPoint().getX()-this.getStartPoint().getX()), 2)+Math.pow((this.getEndPoint().getY()-this.getStartPoint().getY()), 2),0.5);
		return length;
	}

}

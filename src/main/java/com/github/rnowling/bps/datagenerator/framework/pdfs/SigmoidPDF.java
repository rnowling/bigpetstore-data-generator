package com.github.rnowling.bps.datagenerator.framework.pdfs;


public class SigmoidPDF implements ProbabilityDensityFunction<Double>
{
	private final double A;
	private final double B;
	private final double C;
	private final double D;
	
	/**
	 * Sigmoid PDF of the form:
	 * 
	 * p = A / (1.0 + Math.exp(-1.0 * B * (x - C))) + D
	 * 
	 * @param A vertical height of sigmoid (0.0, 1.0)
	 * @param B horizontal center of sigmoid
	 * @param C horizontal scaling 
	 * @param D vertical offset
	 */
	public SigmoidPDF(double A, double B, double C, double D)
	{
		this.A = A;
		this.B = B;
		this.C = C;
		this.D = D;
	}
	
	public double probability(Double x)
	{
		return  A / (1.0 + Math.exp(-1.0 * B * (x - C))) + D;
	}
}

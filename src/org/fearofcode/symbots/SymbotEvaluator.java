package org.fearofcode.symbots;

import java.util.List;
import java.util.Random;

import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

public class SymbotEvaluator implements FitnessEvaluator<double[]> {

    private double R;
    private double theta;
    private Random rng;
    private int iterationCount;
    private int trialCount;
    
    public SymbotEvaluator(double R, double theta, int iterationCount, int trialCount) {
        this.R = R;
        this.theta = theta;
        this.rng = new MersenneTwisterRNG();
        this.iterationCount = iterationCount;
        this.trialCount = trialCount;
    }

    @Override
    public double getFitness(double[] candidate,
            List<? extends double[]> population) {
        double Cs = 0.001;
        
        double sum = 0.0;
        
        for(int i = 1; i <= trialCount; i++) {
            double tau = rng.nextDouble()*2.0*Math.PI;
            double x = rng.nextDouble();
            double y = rng.nextDouble();
            
            Symbot symbot = new Symbot(R, theta, 
                    candidate[0], candidate[1], candidate[2], candidate[3], candidate[4], 
                    tau, x, y, Cs);
            
            for(int j = 1; j <= iterationCount; j++) {
                symbot.kinematicLoopTick();
                sum += symbot.f(symbot.getX(), symbot.getY());
            }
        }
        
        return sum;
    }

    @Override
    public boolean isNatural() {
        return true;
    }

}

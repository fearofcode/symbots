package org.fearofcode.symbots;

import java.util.Random;

import org.uncommons.maths.Maths;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

public class DoubleArrayFactory extends AbstractCandidateFactory<double[]> {

    @Override
    public double[] generateRandomCandidate(Random rng) {
        double[] params = new double[5];
        
        for(int i = 0; i <= 4; i++)
            params[i] = Maths.restrictRange(rng.nextGaussian()/5, -1.0, 1.0);
        
        params[4] = rng.nextDouble();
        
        return params;
    }

}

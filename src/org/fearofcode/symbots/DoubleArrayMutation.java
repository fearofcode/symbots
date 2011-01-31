package org.fearofcode.symbots;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.uncommons.watchmaker.framework.EvolutionaryOperator;

public class DoubleArrayMutation implements EvolutionaryOperator<double[]> {

    private double mutationSize;
    
    public DoubleArrayMutation(double mutationSize) {
        this.mutationSize = mutationSize;
    }

    @Override
    public List<double[]> apply(List<double[]> selectedCandidates, Random rng) {
        List<double[]> mutated = new ArrayList<double[]>(selectedCandidates.size());
        
        for(double[] candidate : selectedCandidates) {
            double[] newCandidate = candidate.clone();
            
            int index = rng.nextInt(candidate.length);
            
            
            newCandidate[index] += rng.nextDouble()*(2.0*mutationSize)-mutationSize;
            
            mutated.add(newCandidate);
            
        }
        return mutated;
    }

}

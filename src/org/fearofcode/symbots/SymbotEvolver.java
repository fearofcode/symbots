package org.fearofcode.symbots;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.watchmaker.framework.CandidateFactory;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.FitnessEvaluator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.operators.DoubleArrayCrossover;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;

public class SymbotEvolver {
    
    public static void main(String[] args) {
        new SymbotEvolver().work();
    }

    private void work() {
        double theta = Math.PI/4;
        double R = 0.05;
        int populationSize = 60;
        int eliteCount = 3;
        int generationCount = 1000;
        int iterationCount = 1000;
        int trialCount = 3;
        
        Random rng = new MersenneTwisterRNG();
        
        CandidateFactory<double[]> candidateFactory = new DoubleArrayFactory();
        
        List<EvolutionaryOperator<double[]>> operators = new ArrayList<EvolutionaryOperator<double[]>>();
        operators.add(new DoubleArrayCrossover(1));
        operators.add(new DoubleArrayMutation(0.1));
        
        EvolutionaryOperator<double[]> pipeline = new EvolutionPipeline<double[]>(operators);
        
        FitnessEvaluator<double[]> fitness = new SymbotEvaluator(R, theta, iterationCount, trialCount);
        
        EvolutionEngine<double[]> engine = 
            new GenerationalEvolutionEngine<double[]>(candidateFactory, 
                    pipeline, 
                    fitness, 
                    new RouletteWheelSelection(), rng);
            
       
       engine.addEvolutionObserver(new EvolutionObserver<double[]>() {


           @Override
           public void populationUpdate(PopulationData<? extends double[]> data) {
               System.out.printf("Generation %d: %f - %s\n", data.getGenerationNumber(),
                       data.getBestCandidateFitness(), Arrays.toString(data.getBestCandidate()));
           }
       });

       
    double[] best = engine.evolve(populationSize, eliteCount, new GenerationCount(generationCount));

       Symbot symbot = new Symbot(R, theta, 
               best[0], best[1], best[2], best[3], best[4], 
               0.01, rng.nextDouble(), rng.nextDouble(), 0.001);
       symbot.iterateWalllessWorld(100000);
    }
}

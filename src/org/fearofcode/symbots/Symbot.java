package org.fearofcode.symbots;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Symbot {
    private double R;
    private double theta;
    private double ll, lr, rl, rr, s;
    private double tau;
    private double x, y;
    private double Cs;
    
    public Symbot(double R, double theta, double ll, double lr, double rl,
            double rr, double s, double tau, double x, double y, double Cs) {
        this.R = R;
        this.theta = theta;
        this.ll = ll;
        this.lr = lr;
        this.rl = rl;
        this.rr = rr;
        this.s = s;
        this.tau = tau;
        this.x = x;
        this.y = y;
        this.Cs = Cs;
    }
    
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    
    public void kinematicLoopTick() {
        // find sensor positions
        double x1 = x + R*Math.cos(tau+theta);
        double y1 = y + R*Math.sin(tau+theta);
        double x2 = x + R*Math.cos(tau-theta);
        double y2 = y + R*Math.cos(tau-theta);
        
        // get weighted field strengths at the sensors
        double dL = f(x1, y1)*ll + f(x2, y2)*rl;
        double dR = f(x2, y2)*rr + f(x1, y1)*lr;
        
        // capped change in position
        double ds = Cs*(dL+dR+s*R/2.0);
        
        if(Math.abs(ds) > R/2)
            ds = Math.signum(ds)*R/2.0;
        
        // capped change in heading
        double dtau = 1.6*(Cs/R)*(dR-dL);
        
        if(Math.abs(dtau) > Math.PI/3)
            dtau = Math.signum(dtau)*Math.PI/3;
        
        x += ds*Math.cos(tau);
        y += ds*Math.sin(tau);
        tau += dtau;
    }
    
    public double f(double x, double y) {
        final double a = 0.5;
        final double b = 0.5;
        final double Cf = 0.1;
        final double rcSquared = 0.001;
        
        if((x-a)*(x-a)+(y-b)*(y-b) >= rcSquared)
            return Cf/((x-a)*(x-a)+(y-b)*(y-b));
        else
            return Cf/rcSquared;
    }
    
    public void iterateWalllessWorld(int n) {
        String filename = System.currentTimeMillis() + ".dat";
        
        try {
            FileWriter f = new FileWriter(filename);
            BufferedWriter writer = new BufferedWriter(f);
        
            for(int i = 1; i <= n; i++) {
                writer.write(String.format("%f %f\n", x, y));
                kinematicLoopTick();
            }
            
            writer.write(String.format("%f %f\n", x, y));
            
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.printf("plot \"%s\" with lines\n", filename);
        
    }

    public static void main(String[] args) {
        double Cs = 0.001;
        
        /*double R = 0.05;
        double theta = Math.PI/4;
        double ll = -0.5, lr = 0.7, rl = 0.7, rr = -0.5;
        double s = 0.3;
        double tau = 0.001;
        double x = 0.5, y = 0.5;*/
        
        double R = 0.05;
        double theta = Math.PI/2;
        double ll = -0.5, lr = 0.7, rl = 0.7, rr = -0.5;
        double s = 0.3;
        double tau = -0.001;
        double x = 0.6, y = 0.6;
        
        Symbot symbot = new Symbot(R, theta, ll, lr, rl, rr, s, tau, x, y, Cs);
        symbot.iterateWalllessWorld(100000);
    }

}

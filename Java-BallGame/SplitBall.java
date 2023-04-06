import java.awt.Color;

public class SplitBall extends BasicBall { 
    protected SplitBall child;
    
    public SplitBall(double r, Color c){
        super(r,c);
    }

    public int getScore() {
    	return 10;
    }
    
    public int reset() {
        rx = 0.0;
        ry = 0.0;  	
        // TO DO: assign a random speed 
        vx = StdRandom.uniform(-0.01, 0.01);
        vy = StdRandom.uniform(-0.01, 0.01);
        this.child = new SplitBall(radius,color);

        return 0;
    }

    public SplitBall getChild(){
        return child;
    }

}
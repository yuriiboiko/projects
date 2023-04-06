import java.awt.Color;
import java.io.*;

public class ShrinkBall extends BasicBall { 
    protected double initRadius;

    public ShrinkBall(double r, Color c) {
        //call the parent constructor.
        super(r,c);
        initRadius=r;
    }

    public int reset() {
        rx = 0.0;
        ry = 0.0;
        shrink();  	
        vx = StdRandom.uniform(-0.01, 0.01);
        vy = StdRandom.uniform(-0.01, 0.01);
        return 1;
    }

    //change size of the ball
    private void shrink(){
        if(this.radius<=(.25*initRadius)){
            this.radius=initRadius;
        }else{
            this.radius=this.radius*1/3;
        }

    }

    public int getScore() {
    	return 20;
    }
}
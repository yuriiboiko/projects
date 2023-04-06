import java.awt.Color;

public class BounceBall extends BasicBall { 
    protected int bounceCount;

    public BounceBall(double r, Color c){
        super(r,c);
        bounceCount=3;
    }

    public int getScore() {
    	return 15;
    }

    // move the ball one step
    public void move() {
        rx = rx + vx;
        ry = ry + vy;
        if ((Math.abs(rx) > 1.0) || (Math.abs(ry) > 1.0)){
            if(bounceCount>=1){
            bounceCount--;
            bounce();
            }
            else{
            isOut = true;
            }
        } 
    }

    private void bounce(){
        if( vx>0 && vy >0){
            if(ry>1 && rx<1){
                vy=-vy;
            }else if(rx>1 && ry<1){
                vx=-vx;
            }else{
                vy=-vy;
                vx=-vx;
            }
        }else if(vx>0 && vy<0){
            if(rx>1 && ry>-1){
                vx=-vx;
            }else if(ry<(-1) && rx<1 ){
                vy=-vy;
            }else{
                vx=-vx;
                vy=-vy;
            }
        }else if(vx<0 && vy<0){
            if(ry<-1 && rx >-1){
                vy=-vy;
            }else if(rx<-1 && ry>-1){
                vx=-vx;
            }else{
                vx=-vx;
                vy=-vy;
            }
        }else if(vx<0 && vy>0){
            if(rx<-1 && ry<1){
                vx=-vx;
            }else if(ry>1 && rx>-1){
                vy=-vy;
            }else{
                vx=-vx;
                vy=-vy;
            }
        }else{
            vx=-vx;
            vy=-vy;
        }
        rx = rx + vx;
        ry = ry + vy;
    }

}
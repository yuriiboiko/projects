import java.util.HashMap;

public class Player{
    protected int numHits;
    protected int score;
    protected HashMap<String, Integer> tracker;

    public Player(){
        numHits=0;
        score =0;
        tracker= new HashMap<String, Integer>();
        tracker.put("basic",0);
        tracker.put("split",0);
        tracker.put("bounce",0);
        tracker.put("shrink",0);
    }

    public void update(int score){
        numHits++;
        this.score=this.score+score;
        updateTracker(score);
    }

    private void updateTracker(int type){
        if(type==25){
            tracker.replace("basic", tracker.get("basic")+1);
        }else if(type==20){
            tracker.replace("shrink", tracker.get("shrink")+1);
        }else if(type==15){
            tracker.replace("bounce", tracker.get("bounce")+1);
        }else if(type==10){
            tracker.replace("split", tracker.get("split")+1);
        }
    }
    public int getScore(){
        return score;
    }

    public int getNumHits(){
        return numHits;
    }

    public String getBallType(){
        String answer="None";
        int hits=0;
        for(String key : tracker.keySet()){
            if(tracker.get(key)>hits){
                hits=tracker.get(key);
                answer = key;
            }
        }
        return answer;
    }
}
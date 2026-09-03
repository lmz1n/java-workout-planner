public class DropSet extends Set{
    private Set[] drops;
    private int dropCount;

    public DropSet(int r, double w, int d){
        super(r, w);
        drops = new Set[d];
        dropCount = 0;
    }

    public void addDrop(int r, double w){
        if(dropCount < drops.length) {
            drops[dropCount] = new Set(r, w);
            dropCount++;
        }
    }

    public int getDropCount(){
        return dropCount;
    }

    public Set getDrop(int i){
        return drops[i];
    }

    public String toString(){
        String s = weight + "lbs x " + reps;
        for(int i = 0; i < dropCount; i++){
            s += "\n\tDrop -> " + drops[i];
        }
        return s;
    }

}
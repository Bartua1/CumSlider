package com.example.cumslide;

public class Bridge {
    public Pair source;
    public Pair target;

    public Bridge(Integer a, Integer b, Integer c, Integer d){
        this.source = new Pair(a,b);
        this.target = new Pair(c,d);
    }

    public Bridge(Pair source, Pair target){
        this.source = source;
        this.target = target;
    }

    //public static Pair getSource(){
    //    return source;
    //}

    //public static Pair getTarget(){
    //    return target;
    //}

    public Pair getSource(){
        return source;
    }

    public Pair getTarget(){
        return target;
    }

    public Integer getSourceX(){
        return source.getX();
    }

    public Boolean inPosition(Integer x, Integer y){
        return (x == source.getX() && y == source.getY()) || (x == target.getX() && y == target.getY());
    }

    public Boolean rightDirection(Pair destination) {

        // Determine which endpoint of the bridge is not the origin
        Pair otherEndpoint = (destination == source) ? target : source;

        // Compare the x-coordinates of the two endpoints
        return destination.getX() > otherEndpoint.getX();
    }
    public Boolean isHorizontal(){
        return source.getY() == target.getY();
    }

    public Integer getSourceY(){
        return source.getY();
    }

    public Integer getTargetX(){
        return target.getX();
    }

    public Integer getTargetY(){
        return target.getY();
    }

    public double calculateAngle(Pair destination){
        double angle = 0.;
        if (destination == target){
            angle = Math.atan2(target.getY() - source.getY(), target.getX() - source.getX());
        }
        else{
            angle = Math.atan2(source.getY() - target.getY(), source.getX() - target.getX());
        }
        return angle;
    }

    public Bridge getScreenPos(double rowGaps, double colGaps){
        int x1 = (int) (source.getX()*rowGaps + rowGaps/2);
        int y1 = (int) (source.getY()*colGaps);
        int x2 = (int) (target.getX()*rowGaps + rowGaps/2);
        int y2 = (int) (target.getY()*colGaps);
        return new Bridge(new Pair(x1,y1), new Pair(x2,y2));
    }

    public Boolean goesUp(Pair destination) {
        // Determine which endpoint of the bridge is not the destination
        Pair otherEndpoint = (destination == source) ? target : source;

        // Compare the y-coordinates of the two endpoints
        return destination.getY() > otherEndpoint.getY();
    }

    public float getInterception(Pair p, Integer max, Boolean right){
        // Get the y-coordinate of the horizontal line defined by p
        double y = p.getY();

        // Get the x-coordinates of the bridge's endpoints
        double x1 = getSourceX();
        double x2 = getTargetX();

        // Calculate the slope of the bridge line
        double m = (getTargetY() - getSourceY()) / (x2 - x1);

        // Calculate the y-intercept of the bridge line
        double b = getSourceY() - m * x1;

        // Calculate the x-coordinate of the point where the two lines intercept
        double x = (y - b) / m;

        if (!right) {
            System.out.println("Esta yendo a la izquierda");
            System.out.println(x + "<" + max.toString());
            if (x >= max) {
                System.out.println("No devuelvo el max");
                return (float) x;
            } else {
                // Devuelvo el max
                System.out.println("Devuelvo el max: " + max);
                return (float) max;
            }
        }
        else{
            System.out.println("Esta yendo a la derecha");
            if (x >= max) {
                return (float) max;
            } else {
                return (float) x;
            }
        }
    }


    public Pair getNext(Pair p){
        if (p.getX() == source.getX() && p.getY() == source.getY()){
            return target;
        }
        else if (p.getX() == target.getX() && p.getY() == target.getY()){
            return source;
        }
        else{
            return null;
        }
    }

    @Override
    public String toString() {
        return "Bridge{" +
                "source=" + source +
                ", target=" + target +
                '}';
    }
}
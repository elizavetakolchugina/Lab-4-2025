package functions;

import java.io.*;

public class FunctionPointExternalizable implements Externalizable{
    private static final long serialVersionUID = 6L;
    private double x;
    private double y;

    public double getX(){
        return x;
    }
    public void setX(double x){
        this.x = x;
    }

    public double getY(){
        return y;
    }
    public void setY(double y){
        this.y = y;
    }


    public FunctionPointExternalizable(double x, double y){
        this.x = x;
        this.y = y; 
    }

    public FunctionPointExternalizable(FunctionPointExternalizable point){
        this.x = point.x;
        this.y = point.y;
    }

    public FunctionPointExternalizable(){
        this.x = 0;
        this.y = 0;
    }

     @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeDouble(x);
        out.writeDouble(y);
    }
    
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        x = in.readDouble();
        y = in.readDouble();
    }
}
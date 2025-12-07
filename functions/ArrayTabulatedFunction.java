package functions;

import java.io.Serializable;

public class ArrayTabulatedFunction implements TabulatedFunction, Serializable {
    private static final long serialVersionUID = 2L;    

    private FunctionPoint[] points;
    private int pointsCount;
    private static final double EPSILON = 1e-9;

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount){
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница области определения должна быть меньше правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть >=2");
        }

        points = new FunctionPoint[pointsCount + 2];
        this.pointsCount = pointsCount;
        double distance = (rightX - leftX)/(pointsCount - 1);

        for (int i = 0; i < pointsCount; i++){
            double x = leftX + i * distance;
            points[i] = new FunctionPoint(x, 0.0);
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values){
        
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница области определения должна быть меньше правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть >=2");
        }

        this.pointsCount = values.length;
        points = new FunctionPoint[pointsCount + 2];
        double distance = (rightX - leftX)/(pointsCount - 1);

        for (int i = 0; i < pointsCount; i++){
            double x = leftX + i * distance;
            points[i] = new FunctionPoint(x, values[i]);
        }
    }

    public ArrayTabulatedFunction(FunctionPoint[] points){
        if (points.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть >= 2");
        }
        for (int i = 1; i < points.length; i++){
            if (points[i] == null || points[i - 1] == null){
                throw new IllegalArgumentException("Точки не могут быть null");
            } 
            if (doubleLessOrEquals(points[i].getX(), points[i - 1].getX())){
                throw new IllegalArgumentException("Точки должны возрастать по абсциссе");
            }
        }

        this.pointsCount = points.length;
        this.points = new FunctionPoint[pointsCount + 2];

        for (int i = 0; i < pointsCount; i++) {
            this.points[i] = new FunctionPoint(points[i]);
        }
    }

    @Override
    public double getLeftDomainBorder(){
        if (pointsCount == 0) {
            throw new IllegalStateException("Функция не содержит точек");
        }
        return points[0].getX();
    }

    @Override
    public double getRightDomainBorder() {
        if (pointsCount == 0) {
            throw new IllegalStateException("Функция не содержит точек");
        }
        return points[pointsCount - 1].getX();
    }

    @Override
    public double getFunctionValue(double x){
        if (doubleLess(x, getLeftDomainBorder()) || doubleGreater(x, getRightDomainBorder())) {
            return Double.NaN;
        }
        for (int i = 0; i < pointsCount - 1; i++){
            double x_1 = points[i].getX();
            double x_2 = points[i + 1].getX();

            if (doubleEquals(x, x_1)){
                return points[i].getY();
            }

            if (doubleEquals(x, x_2)){
                return points[i + 1].getY();
            }

            if (doubleGreater(x, x_1) && doubleLess(x, x_2)){
                double y_1 = points[i].getY();
                double y_2 = points[i + 1].getY();

                return (x - x_1) * (y_2 - y_1) / (x_2 - x_1) + y_1;
            }
        }
        return Double.NaN;
    }

    @Override
    public int getPointsCount(){
        return pointsCount;
    }

    @Override
    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " не входит в диапазон");
        }
        return new FunctionPoint(points[index]);
    }

    @Override
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException{
        if (point == null) {
            throw new IllegalArgumentException("Точка без значения");
        }
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " не входит в диапазон");
        }
        if (index > 0 && doubleLessOrEquals(point.getX(), points[index - 1].getX())) {
            throw new InappropriateFunctionPointException("Новая точка должна быть больше предыдущей по x");
        }
        if (index < pointsCount - 1 &&  doubleGreaterOrEquals(point.getX(), points[index + 1].getX())) {
            throw new InappropriateFunctionPointException("Новая точка должна быть меньше следующей по x");
        }
        
        points[index] = new FunctionPoint(point);
    }
    
    @Override
    public double getPointX(int index){
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " не входит в диапазон");
        }
        return points[index].getX();
    }

    @Override
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " не входит в диапазон");
        }
        if (index > 0 && doubleLessOrEquals(x, points[index - 1].getX())) {
            throw new InappropriateFunctionPointException("Новая x-координата должна быть больше предыдущей");
        }
        if (index < pointsCount - 1 && doubleGreaterOrEquals(x, points[index + 1].getX())) {
            throw new InappropriateFunctionPointException("Новая x-координата должна быть меньше следующей");
        }
        
        points[index].setX(x);
    }

    @Override
    public double getPointY(int index){
        if (index <0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " не входит в диапазон");
        }
        return points[index].getY();
    }    
    
    @Override
    public void setPointY(int index, double y) {
        if (index < 0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " не входит в диапазон");
        }
        points[index].setY(y);
    }

    @Override
    public void deletePoint(int index) {
        if (pointsCount <= 2) {
            throw new IllegalStateException("Минимальное количество точек: 3");
        }
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " не входит в диапазон");
        }

        System.arraycopy(points, index + 1, points, index, pointsCount - index - 1);
        pointsCount--;
        points[pointsCount] = null;
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        if (point == null) {
            throw new IllegalArgumentException("Точка не может быть null");
        }        
        FunctionPoint newPoint = new FunctionPoint(point);
        int newIndex = 0;
        
        while (newIndex < pointsCount && doubleLess(points[newIndex].getX(), newPoint.getX())) {
         newIndex++;
        }
        if  (newIndex < pointsCount && doubleEquals(points[newIndex].getX(), newPoint.getX())) {
            throw new InappropriateFunctionPointException("Такая точка уже существует");
        }
        if (pointsCount == points.length) {
            increaseArraySize();
        }
        
        System.arraycopy(points, newIndex, points, newIndex + 1, pointsCount - newIndex);
        points[newIndex] = newPoint;
        pointsCount++;
    }

    private void increaseArraySize() {
        FunctionPoint[] newArray = new FunctionPoint[points.length * 2 + 2];
        System.arraycopy(points, 0, newArray, 0, pointsCount);
        points = newArray;
    }

    private boolean doubleEquals(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }

    private boolean doubleLess(double a, double b) {
        return a < b - EPSILON;
    }

    private boolean doubleGreater(double a, double b) {
        return a > b + EPSILON;
    }
     private boolean doubleLessOrEquals(double a, double b) {
        return a < b + EPSILON;
    }

    private boolean doubleGreaterOrEquals(double a, double b) {
        return a > b - EPSILON;
    }



}
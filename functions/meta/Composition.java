package functions.meta;

import functions.Function;

public class Composition implements Function{
    private Function f_1;
    private Function f_2;
    
    public Composition(Function f_1, Function f_2){
        this.f_1 = f_1;
        this.f_2 = f_2;
    }

    @Override
    public double getLeftDomainBorder(){
        return f_1.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder() {
        return f_1.getRightDomainBorder();
    }

    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        double innerValue = f_1.getFunctionValue(x);
        if (innerValue < f_2.getLeftDomainBorder() || innerValue > f_2.getRightDomainBorder()) {
            return Double.NaN;
        }
        return f_2.getFunctionValue(innerValue);
    }
}
package functions.meta;

import functions.Function;

public class Sum implements Function{
    private Function f_1;
    private Function f_2;
    
    public Sum(Function f_1, Function f_2){
        this.f_1 = f_1;
        this.f_2 = f_2;
    }

    @Override
    public double getLeftDomainBorder(){
        return Math.max(f_1.getLeftDomainBorder(), f_2.getLeftDomainBorder());
    }

    @Override
    public double getRightDomainBorder() {
        return Math.min(f_1.getRightDomainBorder(), f_2.getRightDomainBorder());
    }

    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        return f_1.getFunctionValue(x) + f_2.getFunctionValue(x);
    }
}

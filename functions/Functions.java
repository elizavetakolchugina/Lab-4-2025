package functions;

import functions.meta.*;

public final class Functions {
    private Functions() {
        throw new Error("Объекты этого класса нельзя создать");
    }

    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }

    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }

    public static Function power(Function f, double power) {
        return new Power(f, power);
    }

    public static Function sum(Function f_1, Function f_2) {
        return new Sum(f_1, f_2);
    }

    public static Function mult(Function f_1, Function f_2) {
        return new Mult(f_1, f_2);
    }

    public static Function composition(Function f_1, Function f_2) {
        return new Composition(f_1, f_2);
    }
}


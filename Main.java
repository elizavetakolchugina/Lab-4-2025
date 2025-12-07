import functions.*;
import functions.basic.*;
import functions.meta.*;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("sin/cos [0, pi]");
        testingSinAndCos();

        System.out.println("\n_tabulated analog of sin/cos [0, pi]_");
        testingTabulatedSinAndCos();

        System.out.println("\n_sum sin^2 + cos^2_");
        testingSumOfSquares();

        System.out.println("\n_tabulated analog of exp [0, 10]_");
        testingTabulatedExp();

        System.out.println("\n_tabulated analog of log [0,10]_");
        testingTabulatedLog();

        System.out.println("\n_testing Serializable_");
        testingSerializable();
        
        // Тест с Externalizable (если создали такой класс)
        System.out.println("\n_testing Externalizable_");
        testingExternalizable();
    }

    private  static void testingSinAndCos() {
        Sin sin = new Sin();
        Cos cos = new Cos();
        
        System.out.println("\n___ sin ___");
        printFunction(sin, 0, Math.PI, 0.1);
        System.out.println("\n___ cos ___");
        printFunction(cos, 0, Math.PI, 0.1);
    }

    private  static void testingTabulatedSinAndCos() {
        Sin sin = new Sin();
        Cos cos = new Cos();
        TabulatedFunction tabulatedSin = TabulatedFunctions.tabulate(sin, 0, Math.PI, 10);
        TabulatedFunction tabulatedCos = TabulatedFunctions.tabulate(cos, 0, Math.PI, 10);
        
        System.out.println("\n___ tabulated sin ___");
        printFunction(tabulatedSin, 0, Math.PI, 0.1);        
        System.out.println("\n___ tabulated cos ___");
        printFunction(tabulatedCos, 0, Math.PI, 0.1);
        System.out.println("\n___ comparison sin ____");
        compareFunctions(sin, tabulatedSin, 0, Math.PI, 0.1);
        System.out.println("\n___ comparison cos ____");
        compareFunctions(cos, tabulatedCos, 0, Math.PI, 0.1);
    }

    private static void testingSumOfSquares() {
        int[] pointCounts = {5, 10, 30, 50};
        for (int pointsCount : pointCounts) {
            System.out.println("Number of points: " + pointsCount);
            Sin sin = new Sin();
            Cos cos = new Cos();
            TabulatedFunction tabulatedSin = TabulatedFunctions.tabulate(sin, 0, Math.PI, 10);
            TabulatedFunction tabulatedCos = TabulatedFunctions.tabulate(cos, 0, Math.PI, 10);
            Function squaredSin = Functions.power(tabulatedSin, 2);
            Function squaredCos = Functions.power(tabulatedCos, 2);
            Function sum = Functions.sum(squaredSin, squaredCos);
            printFunction(sum, 0, Math.PI, 0.1);
        }

    }

    private static void testingTabulatedExp(){
        try {
            Exp exp = new Exp();
            TabulatedFunction tabulatedExp = TabulatedFunctions.tabulate(exp, 0, 10, 11);
            System.out.println("\nwritten to file exp.txt");
            FileWriter writer = new FileWriter("exp.txt");
            TabulatedFunctions.writeTabulatedFunction(tabulatedExp, writer);
            writer.close();

            System.out.println("\nread from file exp.txt");
            FileReader reader = new FileReader("exp.txt");
            TabulatedFunction readExp = TabulatedFunctions.readTabulatedFunction(reader);
            reader.close();

            System.out.println("comparison between exp and read exp");
            compareFunctions(exp, readExp, 0,10, 1);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при работе с файлом", e);
        }
    }

    private static void testingTabulatedLog() {
        try{
            Log log = new Log(Math.E);
            TabulatedFunction tabulatedLog = TabulatedFunctions.tabulate(log, 1,10, 10);

            System.out.println("\n___ written to binary file log.dat___");
            FileOutputStream outStream = new FileOutputStream("log.dat");
            TabulatedFunctions.outputTabulatedFunction(tabulatedLog, outStream);
            outStream.close();
            
            System.out.println("\nread from file log.dat");
            FileInputStream inStream = new FileInputStream("log.dat");
            TabulatedFunction readLog = TabulatedFunctions.inputTabulatedFunction(inStream);
            inStream.close();

            System.out.println("comparison between log and read log");
            compareFunctions(log, readLog, 1, 10, 1);

        } catch (IOException e) {
            throw new RuntimeException ("Ошибка при работе с файлом", e);
        }
    }

    private static void printFunction(Function f, double leftX, double rightX, double step) {
        System.out.println(" x         f(x)");
        System.out.println("-------------------");
        for (double x = leftX; x <= rightX; x += step) {
            double y = f.getFunctionValue(x);

            String strX = String.format("%6.3f", x);
            String strY;
            
            if (Double.isNaN(y)) {
                strY = "        NaN";
            } else {
                strY = String.format("%12.6f", y);
            }

            System.out.println(strX + "  " + strY);
        }
    }

    private static void  compareFunctions(Function f_1, Function f_2, double leftX, double  rightX, double step) {
        System.out.println(" x         f1(x)        f2(x)        difference");
        System.out.println("------------------------------------------------");
        
        for (double x = leftX; x <= rightX; x += step) {
            double y1 = f_1.getFunctionValue(x);
            double y2 = f_2.getFunctionValue(x);
            String strX = String.format("%6.3f", x);
            String strY1, strY2, diffStr;
            if (Double.isNaN(y1)) {

                strY1 = "        NaN";
            } else {
                strY1 = String.format("%12.6f", y1);
            }

            if (Double.isNaN(y2)) {
                strY2 = "        NaN";
            } else {
                strY2 = String.format("%12.6f", y2);
            }
            
            if (!Double.isNaN(y1) && !Double.isNaN(y2)) {
                double diff = Math.abs(y1 - y2);
                diffStr = String.format("%12.6f", diff);
            } else if (Double.isNaN(y1) && Double.isNaN(y2)) {
                diffStr = "        0.000";
            } else {
                diffStr = "        ---";
            }

            System.out.println(strX + "  " + strY1 + "  " + strY2 + "  " + diffStr);
        }
    }
    
    private static void testingSerializable() {
        try {
            Exp exp = new Exp();
            Log ln = new Log(Math.E);
            Function composition = Functions.composition(exp, ln); 
            
            TabulatedFunction tabulatedFunc = TabulatedFunctions.tabulate(composition, 0, 10, 11);
            System.out.println("(ln(e^x) = x):");
            printFunction(tabulatedFunc, 0, 10, 1);
            
            System.out.println("\nSerializable to file 'serializable.ser'...");
            FileOutputStream fos = new FileOutputStream("serializable.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(tabulatedFunc);
            oos.close();
            
            System.out.println("from file 'serializable.ser'...");
            FileInputStream fis = new FileInputStream("serializable.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            TabulatedFunction deserializedFunc = (TabulatedFunction) ois.readObject();
            ois.close();
            
            printFunction(deserializedFunc, 0, 10, 1);
            
            System.out.println("\ncompare:");
            compareFunctions(tabulatedFunc, deserializedFunc, 0, 10, 1);
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка при работе с Serializable: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testingExternalizable() {
        try {
            Exp exp = new Exp();
            Log ln = new Log(Math.E);
            Function composition = Functions.composition(exp, ln); 
           
            TabulatedFunction tabulatedFunc = TabulatedFunctions.tabulate(composition, 0, 10, 11);
            ArrayTabulatedFunctionExternalizable externalizableFunc = new ArrayTabulatedFunctionExternalizable(0, 10, 11);
            
            for (int i = 0; i < externalizableFunc.getPointsCount(); i++) {
                double x = tabulatedFunc.getPointX(i);
                double y = tabulatedFunc.getPointY(i);
                externalizableFunc.setPointY(i, y);
            }

            printFunction(externalizableFunc, 0, 10, 1);
            
            System.out.println("\nExternalizableto file 'externalizable.ser'...");
            FileOutputStream fos = new FileOutputStream("externalizable.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(externalizableFunc);
            oos.close();
            
            System.out.println("from file 'externalizable.ser'...");
            FileInputStream fis = new FileInputStream("externalizable.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayTabulatedFunctionExternalizable deserializedFunc = (ArrayTabulatedFunctionExternalizable) ois.readObject();
            ois.close();
            
            System.out.println("Десериализованная функция:");
            printFunction(deserializedFunc, 0, 10, 1);
            
            System.out.println("\ncompare:");
            compareFunctions(externalizableFunc, deserializedFunc, 0, 10, 1);
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка при работе с Externalizable: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
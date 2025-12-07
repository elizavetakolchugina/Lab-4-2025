package functions;

import java.io.*;
import java.util.StringTokenizer;


public final class TabulatedFunctions {
    private TabulatedFunctions() {
        throw new RuntimeException("Объекты этого класса нельзя создать");
    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount){
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Границы выходят за область определения");
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница области определения должна быть меньше правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть >=2");
        }

        double distance = (rightX - leftX) / (pointsCount - 1);
        double[] values = new double[pointsCount];

        for (int i = 0; i < pointsCount; i++){
            double x = leftX + i * distance;
            values[i] = function.getFunctionValue(x);
        }

        return new ArrayTabulatedFunction(leftX, rightX, values);

    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out){
        DataOutputStream dataOut = new DataOutputStream(out);
        try {
            dataOut.writeInt(function.getPointsCount());
            for (int i = 0; i < function.getPointsCount(); i++) {
                dataOut.writeDouble(function.getPointX(i));
                dataOut.writeDouble(function.getPointY(i));
            }

            dataOut.flush();
        } catch (IOException e) {
            throw new RuntimeException ("Ошибка при выводе функции", e);
        }        
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in){
        DataInputStream dataIn = new DataInputStream(in);
        try {
            int pointsCount = dataIn.readInt();
            FunctionPoint[] points = new FunctionPoint[pointsCount];

            for (int i = 0; i < pointsCount; i++) {
                double x = dataIn.readDouble();
                double y = dataIn.readDouble();
                points[i] = new FunctionPoint(x, y);
            }
            return new ArrayTabulatedFunction(points);

        } catch (IOException e) {
            throw new RuntimeException ("Ошибка при вводе функции", e);
        }
    }
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out){
        PrintWriter writer = new PrintWriter(out);
        try {
            writer.print(function.getPointsCount());
            writer.print(' ');

            for (int i = 0; i < function.getPointsCount(); i++) {
                writer.print(function.getPointX(i));
                writer.print(' ');
                writer.print(function.getPointY(i));
                if (i < function.getPointsCount() - 1) {
                    writer.print(' ');
                }
            }
            writer.flush();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при записи функции", e);
        }
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) {
        StreamTokenizer tokenizer = new StreamTokenizer(in);
        try {
            tokenizer.resetSyntax();
            tokenizer.wordChars('0', '9');
            tokenizer.wordChars('.', '.');
            tokenizer.wordChars('-', '-');
            tokenizer.wordChars('e', 'e');
            tokenizer.wordChars('E', 'E');
            tokenizer.whitespaceChars(' ', ' ');
            tokenizer.whitespaceChars('\t', '\t');
            tokenizer.whitespaceChars('\n', '\n');
            tokenizer.whitespaceChars('\r', '\r');


            if (tokenizer.nextToken() != StreamTokenizer.TT_WORD) {
                throw new RuntimeException("Отсутствует количество точек");
            }
            int pointsCount = Integer.parseInt(tokenizer.sval);

            FunctionPoint[] points = new FunctionPoint[pointsCount];
            for (int i = 0; i < pointsCount; i++) {
                if (tokenizer.nextToken() != StreamTokenizer.TT_WORD){
                    throw new RuntimeException ("Отсутствует координата x");
                }
                double x = Double.parseDouble(tokenizer.sval);
                if (tokenizer.nextToken() != StreamTokenizer.TT_WORD){
                    throw new RuntimeException ("Отсутствует координата y");
                }
                double y = Double.parseDouble(tokenizer.sval);
                points[i] = new FunctionPoint(x, y);                                   
            } 

            return new ArrayTabulatedFunction(points);

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении", e);
        } catch(NumberFormatException e) {
            throw new RuntimeException("Неверный формат числа", e);
        }
    }

}

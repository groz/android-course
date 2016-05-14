package tm.hackweek.arithmetictrainer;

public interface BinaryOperation {
    Number execute(Number lhs, Number rhs);
    String text();
}

package tm.hackweek.arithmetictrainer;

public final class OpCode {
    public static int ADD = 1;
    public static int MULTIPLY = 2;
    public static int DIVIDE = 4;
    public static int SUBTRACT = 8;

    public static int ALL = ADD | MULTIPLY | DIVIDE | SUBTRACT;

    public static int set(int code, int flag) {
        return code | flag;
    }

    public static int clear(int code, int flag) {
        return code & ~(1 << flag);
    }

    public static int reset(int code, int flag, boolean status) {
        return status ? set(code, flag) : clear(code, flag);
    }
}

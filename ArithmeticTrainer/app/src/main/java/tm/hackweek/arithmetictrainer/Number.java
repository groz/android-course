package tm.hackweek.arithmetictrainer;

import android.support.annotation.NonNull;

import java.math.BigInteger;

public final class Number implements Comparable<Number> {
    private final int mNumerator;
    private final int mDenominator;

    private static int gcd(int a, int b) {
        return BigInteger.valueOf(Math.abs(a)).gcd(BigInteger.valueOf(Math.abs(b))).intValue();
    }

    public static Number fromInt(int a) {
        return new Number(a, 1);
    }

    public Number(int numerator, int denominator) {
        mNumerator = numerator;
        mDenominator = denominator;
    }

    public Number(int numerator) {
        this(numerator, 1);
    }

    public Number simplify() {
        if (mDenominator == 1)
            return this;

        int num = mNumerator;
        int den = mDenominator;

        if (mDenominator < 0) {
            num *= -1;
            den *= -1;
        }

        int gcd = gcd(mNumerator, mDenominator);

        if (gcd == 1)
            return this;

        return new Number(num / gcd, den / gcd);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Number lhs = this.simplify();
        Number rhs = ((Number) o).simplify();
        return lhs.mNumerator == rhs.mNumerator && lhs.mDenominator == rhs.mDenominator;

    }

    @Override
    public int hashCode() {
        Number s = simplify();
        return 31 * s.mNumerator + s.mDenominator;
    }

    @Override
    public String toString() {
        if (mDenominator == 1) {
            return Integer.toString(mNumerator);
        }
        return String.format("%d / %d", mNumerator, mDenominator);
    }

    public Number negate() {
        return new Number(-mNumerator, mDenominator);
    }

    public Number reverse() {
        return new Number(mDenominator, mNumerator);
    }

    public Number add(Number other) {
        int num = mNumerator * other.mDenominator + other.mNumerator * mDenominator;
        int den = mDenominator * other.mDenominator;
        return new Number(num, den).simplify();
    }

    public Number subtract(Number other) {
        return add(other.negate());
    }

    public Number multiply(Number other) {
        return new Number(mNumerator * other.mNumerator, mDenominator * other.mDenominator).simplify();
    }

    public Number divide(Number other) {
        return multiply(other.reverse());
    }

    @Override
    public int compareTo(@NonNull Number that) {
        return Double.compare(
                this.mNumerator * that.mDenominator,
                that.mNumerator * this.mDenominator
        );
    }
}

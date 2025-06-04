/**
 * The Complex class represents a complex number with real and imaginary parts.
 *
 */
public class Complex {
    private final double real;
    private final double imag;

    /**
     * Constructs a complex number with the specified real and imaginary parts.
     * @param real The real part of the complex number.
     * @param imag The imaginary part of the complex number.
     */
    public Complex(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    /**
     * Getter for the real part of the complex number.
     * @return The real part.
     */
    public double getReal() {
        return real;
    }

    /**
     * Getter for the imaginary part of the complex number.
     * @return The imaginary part.
     */
    public double getImag() {
        return imag;
    }

    /**
     * Returns a string representation of the complex number in form "real" + "imag"i
     * @return The string representation.
     */
    @Override
    public String toString() {
        return getReal() + " + " + getImag() + "i";
    }
}
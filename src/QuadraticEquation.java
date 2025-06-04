/**
 * The QuadraticEquation class represents a quadratic equation of the form ax^2 + bx + c = 0.
 * This class provides methods to calculate the roots of the quadratic equation.
 */
public class QuadraticEquation {
    private final double a, b, c;

    /**
     * Constructs a quadratic equation with the specified coefficients.
     * @param a The coefficient of x^2.
     * @param b The coefficient of x.
     * @param c The constant term.
     */
    public QuadraticEquation(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /**
     * Calculates and returns the roots of the quadratic equation.
     * @return A Complex object representing the roots of the quadratic equation.
     */
    public Complex getRoots() {
        // Calculate the determinant
        double determinant = b * b - 4 * a * c;

        // Check whether roots will be two real distinct roots, two repeated real root, or two distinct complex roots
        if (determinant > 0) {
            // Two real distinct roots
            return new Complex((-b + Math.sqrt(determinant)) / (2 * a), 0);

        } else if (determinant == 0) {
            // Two repeated real roots
            return new Complex(-b / (2 * a), 0);

        } else {
            // Two complex roots
            double realPart = -b / (2 * a);
            double imagPart = Math.sqrt(-determinant) / (2 * a);
            return new Complex(realPart, imagPart);
        }

    }

}

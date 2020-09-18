public class Vector4 {
    /**** represent Homogeneous coordinates in 3D space ****/

    public static final double EPSILON = 1.0E-7f;
    public final double x, y, z, w;

    public Vector4(double uniform) {
        this(uniform, uniform, uniform, uniform);
    }

    public Vector4(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4(Vector4 other){
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
        this.w = other.w;
    }

    // Add two vectors together
    public Vector4 add(Vector4 other) {
        return new Vector4(x + other.x, y + other.y, z + other.z, w + other.w);
    }

    // Add a scalar to a vector
    public Vector4 add(double other) {
        return new Vector4(x + other, y + other, z + other, w + other);
    }

    // Translate a vector
    public Vector4 translate(double x, double y, double z) {
        return new Vector4(this.x + this.w * x,
                           this.y + this.w * y,
                           this.z + this.w * z,
                              this.w);
    }

    // Scale a vector uniformly
    public Vector4 scale(double scalar) {
        return new Vector4(scalar * x, scalar * y, scalar * z, scalar * w);
    }

    // Scale a vector non-uniformly
    public Vector4 scale(double scalarx, double scalary, double scalarz){
        return new Vector4(scalarx * x, scalary * y, scalarz * z, w);
    }

    // Rotate a vector
    public Vector4 rotate(double xaxis, double yaxis, double zaxis, double angle){
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double newx = this.x * (xaxis * xaxis + cos * (1 - xaxis * xaxis)) +
                      this.y * (zaxis * sin + xaxis * yaxis * (1 - cos)) +
                      this.z * (yaxis * sin + xaxis * zaxis * (1 - cos));
        double newy = this.x * (zaxis * sin + xaxis * yaxis * (1 - cos)) +
                      this.y * (yaxis * yaxis + cos * (1 - yaxis * yaxis)) +
                      this.z * (- xaxis * sin + yaxis * zaxis * (1 - cos));
        double newz = this.x * (- yaxis * sin + xaxis * zaxis * (1 - cos)) +
                      this.y * (xaxis * sin + yaxis *zaxis * (1 - cos)) +
                      this.z * (zaxis * zaxis + cos * (1 - zaxis * zaxis));
        return new Vector4(newx, newy, newz, w);
    }


    // Magnitude of a vector
    public double magnitude() {
        return Math.sqrt(x * x + y * y + z * z + w * w);
    }

    // Determine if two vectors are equal
    public boolean equals(Vector4 other) {
        return (Math.abs(this.x - other.x) <= EPSILON &&
                Math.abs(this.y - other.y) <= EPSILON &&
                Math.abs(this.z - other.z) <= EPSILON &&
                Math.abs(this.w - other.w) <= EPSILON);
    }
}

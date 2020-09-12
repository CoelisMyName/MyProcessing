public class Complex {
    public float real;
    public float imagine;

    /**
     *
     * @param r 实数
     * @param i 虚数
     */
    public Complex(float r, float i){
        real = r;
        imagine = i;
    }

    public void assign_mul(final Complex c){
        float temp = real;
        real = real * c.real - imagine * c.imagine;
        imagine = temp * c.imagine + imagine * c.real;
    }
}
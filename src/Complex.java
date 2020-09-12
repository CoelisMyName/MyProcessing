public class Complex {
    public float real;
    public float imagine;

    public Complex(){
        real = 0;
        imagine = 0;
    }

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

    public void assign_add(final Complex c){
        real = real + c.real;
        imagine = imagine + c.imagine;
    }
}

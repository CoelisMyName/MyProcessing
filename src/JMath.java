import java.util.Arrays;
import java.util.Random;

public class JMath {
    public static float[] convolution(float[] a, float[] b) throws Exception{
        final int length = a.length + b.length - 1;
        final int N = power_ceil(length);
        if(N == -1){
            throw new Exception("找2的幂超出了int最大值");
        }
        float c[] = new float[length];

        Complex afreq[] = new Complex[N], bfreq[] = new Complex[N];
        for (int i = 0; i < N; ++i){
            afreq[i] = new Complex();
            bfreq[i] = new Complex();
        }

        dft(a,afreq);
        dft(b,bfreq);

        for(int i = 0; i < N; ++i){
            afreq[i].assign_mul(bfreq[i]);
        }

        idft(afreq, c);
        return c;
    }

    public static float[] native_convolution(float[] a, float[] b) throws Exception{
        final int length = a.length + b.length - 1;
        final int N = power_ceil(length);
        if(N == -1){
            throw new Exception("找2的幂超出了int最大值");
        }
        float c[] = new float[length];
        for(int i = 0; i < c.length; ++i){
            for(int j = 0; j < a.length; ++j){
                if(i - j < 0){
                    break;
                }
                if(i - j >= b.length){
                    continue;
                }
                c[i] += a[j] * b[i - j];
            }
        }
        return c;
    }

    public static void main(String[] args) throws Exception {
        Random random = new Random();
        int lengtha = random.nextInt(0x10_00), lengthb = random.nextInt(0x10_00);
        float[] a = new float[lengtha];
        float[] b = new float[lengthb];
        random.nextInt(0x10000);
        for(int i = 0; i < a.length; ++i){
            short temp = (short) random.nextInt(Short.MAX_VALUE);
            if(random.nextBoolean()){
                temp = (short) -temp;
            }
            a[i] = temp;
        }

        for(int i = 0; i < b.length; ++i){
            short temp = (short) random.nextInt(Short.MAX_VALUE);
            if(random.nextBoolean()){
                temp = (short) -temp;
            }
            b[i] = temp;
        }

        float max;
        float[] c;
        c = native_convolution(a,b);
        max = Short.MAX_VALUE / abs_max(c);
        mul_k(c,max);
        System.out.println("native_convolution(a,b)");
        for(float v : c){
            System.out.print(Math.ceil(v) + " ");
        }
        System.out.println();
        System.out.println();

        c = convolution(a,b);
        max = Short.MAX_VALUE / abs_max(c);
        mul_k(c,max);
        System.out.println("convolution(a,b)");
        for(float v : c){
            System.out.print(Math.ceil(v) + " ");
        }
        System.out.println();
    }

    private static final int POWER_MAX = 0b01000000_00000000_00000000_00000000;

    public static int power_ceil(int a){
        for(int i = 1; i != Integer.MIN_VALUE; i <<= 1){
            if(i >= a){
                return i;
            }
        }
        return -1;
    }

    public static void dft(float[] a, Complex[] fre){
        final double CONST = -Math.PI * 2f / fre.length;
        double temp;
        for (int i = 0; i < fre.length; ++i){
            for(int j = 0; j < a.length; ++j){
                temp = CONST * i * j;
                fre[i].real += (float) (a[j] * Math.cos(temp));
                fre[i].imagine += (float) (a[j] * Math.sin(temp));
            }
        }

    }

    public static void idft(final Complex[] fre, float[] a){
        final double CONST = Math.PI * 2f / fre.length;
        double temp;
        Complex temp1 = new Complex();
        float result;
        for(int i = 0; i < a.length; ++i){
            result = 0;
            for(int j = 0; j < fre.length; ++j){
                temp = CONST * i * j;
                temp1.real = (float) Math.cos(temp);
                temp1.imagine = (float) Math.sin(temp);
                temp1.assign_mul(fre[j]);
                result += temp1.real;
            }
            a[i] = result / fre.length;
        }
        System.out.println();
    }

    public static void fft(float[] a, Complex[] fre){

    }



    public static void ifft(Complex[] fre, float[] a){

    }

    public static float abs_max(float a[]){
        float max = 0;
        float temp;
        for(float v : a){
            temp = Math.abs(v);
            if(max < temp){
                max = temp;
            }
        }
        return max;
    }

    public static void mul_k(float a[], float m){
        for (int i = 0; i < a.length; i++) {
            a[i] *= m;
        }
    }
}

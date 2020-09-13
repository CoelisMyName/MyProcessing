import java.util.Arrays;
import java.util.Random;

public class JMath {
    public static float[] convolution(float[] a, float[] b) throws Exception{
        final int length = a.length + b.length - 1;
        final int N = power_ceil(length);
        if(N == -1){
            throw new Exception("找2的幂超出了int最大值");
        }
        float[] c = new float[length];

        Complex[] afreq = new Complex[N];
        Complex[] bfreq = new Complex[N];
        for (int i = 0; i < N; ++i){
            afreq[i] = new Complex();
            bfreq[i] = new Complex();
        }

        dft(a,afreq);
        dft(b,bfreq);

        //fft(a,afreq);
        //fft(b,bfreq);

        for(int i = 0; i < N; ++i){
            afreq[i].assign_mul(bfreq[i]);
        }

        idft(afreq, c);
        //ifft(afreq, c);
        return c;
    }

    public static float[] native_convolution(float[] a, float[] b) throws Exception{
        final int length = a.length + b.length - 1;
        final int N = power_ceil(length);
        if(N == -1){
            throw new Exception("找2的幂超出了int最大值");
        }
        float[] c = new float[length];
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
        float[] a = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
        Complex[] c = new Complex[a.length];
        for (int i = 0; i < c.length; ++i){
            c[i] = new Complex();
        }
        System.out.println("DFT");
        dft(a,c);
        for( Complex v : c){
            System.out.println(v.real + " + " + v.imagine);
        }
        System.out.println();
    }

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

    public static void dft4(float[] a, int offset, int step, Complex[] fre){
        final int N = 4;
    }

    public static void fft(float[] a, Complex[] fre){
        final int N = fre.length, L = 4, M = a.length / L;
        final double CONST = Math.PI * 2f / N;
        for(int l = 0; l < L; ++l){

        }
    }



    public static void ifft(Complex[] fre, float[] a){

    }

    public static float abs_max(float[] a){
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

    public static void mul_k(float[] a, float m){
        for (int i = 0; i < a.length; i++) {
            a[i] *= m;
        }
    }
}

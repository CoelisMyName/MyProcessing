public class JMath {
    public static float[] convolution(float[] a, float[] b) throws Exception{
        final int length = a.length + b.length - 1;
        final int N = power_ceil(length);
        if(N == -1){
            throw new Exception("找2的幂超出了int最大值");
        }
        float c[] = new float[N];
        Complex afreq[] = new Complex[N], bfreq[] = new Complex[N];
        dft(a,afreq);
        dft(b,bfreq);
        for(int i = 0; i < N; ++i){
            afreq[i].assign_mul(bfreq[i]);
        }
        idft(afreq, c);
        return c;
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
        for (int i = 0; i < fre.length; ++i){

        }

    }

    public static void idft(Complex[] fre, float[] a){

    }
}

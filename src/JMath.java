import javax.xml.stream.events.Comment;
import java.util.Random;

public class JMath {
    public static float[] convolution(float[] a, float[] b) throws Exception {
        final int length = a.length + b.length - 1;
        final int N = power_ceil(length);
        if (N == -1) {
            throw new Exception("找2的幂超出了int最大值");
        }
        float[] c = new float[length];

        Complex[] afreq = new Complex[N];
        Complex[] bfreq = new Complex[N];
        for (int i = 0; i < N; ++i) {
            afreq[i] = new Complex();
            bfreq[i] = new Complex();
        }

        dft(a, afreq);
        dft(b, bfreq);

        //fft(a,afreq);
        //fft(b,bfreq);

        for (int i = 0; i < N; ++i) {
            afreq[i].assign_mul(bfreq[i]);
        }

        idft(afreq, c);
        //ifft(afreq, c);
        return c;
    }

    public static float[] native_convolution(float[] a, float[] b) throws Exception {
        final int length = a.length + b.length - 1;
        final int N = power_ceil(length);
        if (N == -1) {
            throw new Exception("找2的幂超出了int最大值");
        }
        float[] c = new float[length];
        for (int i = 0; i < c.length; ++i) {
            for (int j = 0; j < a.length; ++j) {
                if (i - j < 0) {
                    break;
                }
                if (i - j >= b.length) {
                    continue;
                }
                c[i] += a[j] * b[i - j];
            }
        }
        return c;
    }

    public static void main(String[] args) throws Exception {
        float[] a = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
        Complex[] c = new Complex[a.length];
        for (int i = 0; i < c.length; i++) {
            c[i] = new Complex();
        }
        fft(a,c);
        for (Complex v : c){
            System.out.println(v.real + " " + v.imagine);
        }
    }

    public static int power_ceil(int a) {
        for (int i = 1; i != Integer.MIN_VALUE; i <<= 1) {
            if (i >= a) {
                return i;
            }
        }
        return -1;
    }

    public static void dft(float[] a, Complex[] fre) {
        final double CONST = -Math.PI * 2f / fre.length;
        double temp;
        for (int i = 0; i < fre.length; ++i) {
            for (int j = 0; j < a.length; ++j) {
                temp = CONST * i * j;
                fre[i].real += (float) (a[j] * Math.cos(temp));
                fre[i].imagine += (float) (a[j] * Math.sin(temp));
            }
        }

    }

    public static void idft(final Complex[] fre, float[] a) {
        final double CONST = Math.PI * 2f / fre.length;
        double temp;
        Complex temp1 = new Complex();
        float result;
        for (int i = 0; i < a.length; ++i) {
            result = 0;
            for (int j = 0; j < fre.length; ++j) {
                temp = CONST * i * j;
                temp1.real = (float) Math.cos(temp);
                temp1.imagine = (float) Math.sin(temp);
                temp1.assign_mul(fre[j]);
                result += temp1.real;
            }
            a[i] = result / fre.length;
        }
    }

    public static void dft4(float[] a, int aoffset, int astep, final int N, Complex[] fre, int foffset) {
        final int L = 4;
        final double CONST = -Math.PI * 2f / N;
        double temp;
        int i0 = aoffset, i1 = i0 + astep, i2 = i1 + astep, i3 = i2 + astep;
        for (int i = 0; i < L; ++i) {

            if (i0 < a.length) {
                fre[foffset + i].real += a[i0];
            }

            if (i1 < a.length) {
                temp = CONST * i * 1;
                fre[foffset + i].real += (float) (a[i1] * Math.cos(temp));
                fre[foffset + i].imagine += (float) (a[i1] * Math.sin(temp));
            }

            if (i2 < a.length) {
                temp = CONST * i * 2;
                fre[foffset + i].real += (float) (a[i2] * Math.cos(temp));
                fre[foffset + i].imagine += (float) (a[i2] * Math.sin(temp));
            }


            if (i3 < a.length) {
                temp = CONST * i * 3;
                fre[foffset + i].real += (float) (a[i3] * Math.cos(temp));
                fre[foffset + i].imagine += (float) (a[i3] * Math.sin(temp));
            }
        }
    }

    /**
     * @param a   输入信号
     * @param fre 长度必须为4的幂
     */
    public static void fft(final float[] a, Complex[] fre) {
        final int N = fre.length, L = 4;
        fft(a, 0, 1, N, fre, 0);
    }

    public static void fft(final float[] a, int aoffset, int astep, int N, Complex[] fre, int foffset) {
        final int L = 4, M = N / L;
        final double CONST = -Math.PI * 2f / N;

        if (N == 4) {
            dft4(a, aoffset, astep, N, fre, foffset);
            return;
        }
        for (int i = 0; i < L; i++) {
            fft(a, aoffset + i * astep, astep * L, M, fre, foffset + i * M);
        }

        //系数与F(0,q)相乘
        Complex temp0 = new Complex();
        double rad;
        for (int l = 1; l < L; ++l) {
            for (int q = 1; q < M; ++q) {
                //W(l * q, N)
                rad = CONST * q * l;
                temp0.real = (float) Math.cos(rad);
                temp0.imagine = (float) Math.sin(rad);
                fre[foffset + l * M + q].assign_mul(temp0);
            }
        }

        //线性相加1
        Complex temp1 = new Complex(), temp2 = new Complex(), temp3 = new Complex();
        int i0 = foffset, i1 = foffset + M, i2 = foffset + M + M, i3 = foffset + M + M + M;
        for (int q = 0; q < M; ++q) {

            temp0.real = fre[i0].real + fre[i2].real;
            temp0.imagine = fre[i0].imagine + fre[i2].imagine;

            temp1.real = fre[i0].real - fre[i2].real;
            temp1.imagine = fre[i0].imagine - fre[i2].imagine;

            temp2.real = fre[i1].real + fre[i3].real;
            temp2.imagine = fre[i1].imagine + fre[i3].imagine;

            temp3.real = fre[i1].real - fre[i3].real;
            temp3.imagine = fre[i1].imagine - fre[i3].imagine;

            fre[i0].real = temp0.real + temp2.real;
            fre[i0].imagine = temp0.imagine + temp2.imagine;

            fre[i1].real = temp1.real + temp3.imagine;
            fre[i1].imagine = temp1.imagine - temp3.real;

            fre[i2].real = temp0.real - temp2.real;
            fre[i2].imagine = temp0.imagine - temp2.imagine;

            fre[i3].real = temp1.real - temp3.imagine;
            fre[i3].imagine = temp1.imagine + temp3.real;

            ++i0;
            ++i1;
            ++i2;
            ++i3;
        }
    }

    /**
     *
     * @param fre 频域
     * @param a 时域
     *          将会改变fre里的数据
     */
    public static void ifft(final Complex[] fre, float[] a) {
        final int N = fre.length, L = 4;
        Complex[] result = new Complex[fre.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = new Complex();
        }
        ifft(fre, 0, 1, N, result, 0);
        for (int i = 0; i < a.length; i++) {
            a[i] = result[i].real / N;
        }
    }

    public static void ifft(final Complex[] fre, int foffset, int fstep, int N, Complex[] result, int roffset) {
        final int L = 4, M = N / L;
        final double CONST = Math.PI * 2f / N;

        if (N == 4) {
            idft4(fre, foffset, fstep, N, result, roffset);
            return;
        }
        for (int i = 0; i < L; i++) {
            ifft(fre, foffset + i * fstep, fstep * L, M, result, roffset + i * M);
        }

        //系数与F(0,q)相乘
        Complex temp0 = new Complex();
        double rad;
        for (int l = 1; l < L; ++l) {
            for (int q = 1; q < M; ++q) {
                //W(l * q, N)
                rad = CONST * q * l;
                temp0.real = (float) Math.cos(rad);
                temp0.imagine = (float) Math.sin(rad);
                result[foffset + l * M + q].assign_mul(temp0);
            }
        }

        Complex temp1 = new Complex(), temp2 = new Complex(), temp3 = new Complex();
        int i0 = roffset, i1 = roffset + M, i2 = roffset + M + M, i3 = roffset + M + M + M;
        for (int q = 0; q < M; ++q) {

            temp0.real = result[i0].real + result[i2].real;
            temp0.imagine = result[i0].imagine + result[i2].imagine;

            temp1.real = result[i0].real - result[i2].real;
            temp1.imagine = result[i0].imagine - result[i2].imagine;

            temp2.real = result[i1].real + result[i3].real;
            temp2.imagine = result[i1].imagine + result[i3].imagine;

            temp3.real = result[i1].real - result[i3].real;
            temp3.imagine = result[i1].imagine - result[i3].imagine;

            result[i0].real = temp0.real + temp2.real;
            result[i0].imagine = temp0.imagine + temp2.imagine;

            result[i1].real = temp1.real - temp3.imagine;
            result[i1].imagine = temp1.imagine + temp3.real;

            result[i2].real = temp0.real - temp2.real;
            result[i2].imagine = temp0.imagine - temp2.imagine;

            result[i3].real = temp1.real + temp3.imagine;
            result[i3].imagine = temp1.imagine - temp3.real;

            ++i0;
            ++i1;
            ++i2;
            ++i3;
        }
    }

    public static void idft4(final Complex[] fre, int foffset, int fstep, int N, Complex[] result, int roffset) {
        final int L = 4;
        final double CONST = Math.PI * 2f / N;
        double temp;
        int i0 = foffset, i1 = i0 + fstep, i2 = i1 + fstep, i3 = i2 + fstep;
        Complex buff = new Complex();
        for (int i = 0; i < L; ++i) {

            result[roffset + i].assign_add(fre[i0]);

                temp = CONST * i * 1;
            buff.real = (float) Math.cos(temp);
            buff.imagine = (float) Math.sin(temp);
            buff.assign_mul(fre[i1]);
            result[roffset + i].assign_add(buff);

                temp = CONST * i * 2;
            buff.real = (float) Math.cos(temp);
            buff.imagine = (float) Math.sin(temp);
            buff.assign_mul(fre[i2]);
            result[roffset + i].assign_add(buff);

                temp = CONST * i * 3;
            buff.real = (float) Math.cos(temp);
            buff.imagine = (float) Math.sin(temp);
            buff.assign_mul(fre[i3]);
            result[roffset + i].assign_add(buff);
        }
    }

    public static float abs_max(float[] a) {
        float max = 0;
        float temp;
        for (float v : a) {
            temp = Math.abs(v);
            if (max < temp) {
                max = temp;
            }
        }
        return max;
    }

    public static void mul_k(float[] a, float m) {
        for (int i = 0; i < a.length; i++) {
            a[i] *= m;
        }
    }
}

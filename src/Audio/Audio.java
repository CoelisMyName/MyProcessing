package Audio;

import processing.core.PApplet;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;

public class Audio extends PApplet {
    public static final String apcm = "D:\\测试信号.pcm";
    public static final String bpcm = "D:\\卷积逆信号.pcm";
    long aLong = 0;
    long bLong = 0;
    MappedByteBuffer ambb = null;
    MappedByteBuffer bmbb = null;
    ShortBuffer asbf;
    ShortBuffer bsbf;
    short[] abuff = null;
    short[] bbuff = null;
    short[] cbuff = null;
    int abline, bbline, cbline;
    float yscl;
    float ascl, bscl, cscl;

    public static void main(String... args) {
        Audio pt = new Audio();
        PApplet.runSketch(new String[]{"Audio"}, pt);
    }

    @Override
    public void settings() {
        size(1600, 900, P2D);
        abline = (height / 3) / 2;
        bbline = abline + height / 3;
        cbline = bbline + height / 3;
        yscl = (float) abline / ((float) Short.MAX_VALUE * 1.05f);
    }

    @Override
    public void setup() {
        colorMode(RGB);
        background(255);

        try {
            FileInputStream afis = new FileInputStream(apcm);
            aLong = afis.getChannel().size();
            ambb = afis.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, aLong);

            FileInputStream bfis = new FileInputStream(bpcm);
            bLong = bfis.getChannel().size();
            bmbb = bfis.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, bLong);

            System.out.println(apcm + " 大小为" + aLong);
            System.out.println(bpcm + " 大小为" + bLong);

            abuff = new short[(int) (aLong / 2)];
            bbuff = new short[(int) (bLong / 2)];
            cbuff = new short[abuff.length + bbuff.length - 1];

            System.out.println("abuff" + " 大小为" + abuff.length);
            System.out.println("bbuff" + " 大小为" + bbuff.length);
            System.out.println("cbuff" + " 大小为" + cbuff.length);

            cscl = (float) width / cbuff.length;

            for (int i = 0; i < aLong / 2; ++i) {
                int temp1 = 0x00ff & ambb.get();
                int temp2 = (0x00ff & ambb.get()) << 8;
                abuff[i] = (short) (temp1 | temp2);
            }
            for (int i = 0; i < bLong / 2; ++i) {
                int temp1 = 0x00ff & bmbb.get();
                int temp2 = (0x00ff & bmbb.get()) << 8;
                bbuff[i] = (short) (temp1 | temp2);
            }

            ambb.clear();
            bmbb.clear();

            ascl = (float) width / abuff.length;
            bscl = (float) width / bbuff.length;

        } catch (IOException e) {
            e.printStackTrace();
            exit();
        }
    }

    @Override
    public void draw() {
        stroke(255, 0, 255);
        line(0, abline, width, abline);


        float before = abline;
        for (int i = 0; i < abuff.length; ++i) {
            line(ascl * (i - 1), before, ascl * i, abline - yscl * abuff[i]);
            before = abline - yscl * abuff[i];
        }
        before = bbline;
        for (int i = 0; i < bbuff.length; ++i) {
            line(bscl * (i - 1), before, bscl * i, bbline - yscl * bbuff[i]);
            before = bbline - yscl * bbuff[i];
        }
        noLoop();
    }
}

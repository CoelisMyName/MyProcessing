package Audio;

import FlowField.FlowField;
import processing.core.PApplet;

import javax.swing.*;
import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;

public class Audio extends PApplet {
    public static void main (String... args) {
        Audio pt = new Audio();
        PApplet.runSketch(new String[]{"Audio"}, pt);
    }

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

    @Override
    public void settings() {
        size(1600,900,P2D);
        abline = (height / 3) / 2;
        bbline = abline + height / 3;
        cbline = bbline + height / 3;
        yscl = (float) abline / Short.MAX_VALUE;
    }

    @Override
    public void setup() {
        background(255);
        try {
            FileInputStream afis = new FileInputStream(apcm);
            aLong = afis.getChannel().size();
            ambb = afis.getChannel().map(FileChannel.MapMode.READ_ONLY,0,aLong);

            FileInputStream bfis = new FileInputStream(bpcm);
            bLong = bfis.getChannel().size();
            bmbb = bfis.getChannel().map(FileChannel.MapMode.READ_ONLY,0,bLong);

            System.out.println(apcm + " 大小为" + aLong);
            System.out.println(bpcm + " 大小为" + bLong);

            abuff = new short[(int) (aLong / 2)];
            bbuff = new short[(int) (bLong / 2)];
            cbuff = new short[abuff.length + bbuff.length - 1];

            for(int i = 0; i < aLong / 2; ++i){
                abuff[i] = ambb.getShort();
            }
            for(int i = 0; i < bLong / 2; ++i){
                bbuff[i] = bmbb.getShort();
            }

        } catch (IOException e) {
            e.printStackTrace();
            exit();
        }
    }

    @Override
    public void draw() {



    }
}

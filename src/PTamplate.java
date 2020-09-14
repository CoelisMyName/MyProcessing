import processing.core.PApplet;

public class PTamplate extends PApplet {

    public static void main(String... args) {
        PTamplate pt = new PTamplate();
        PApplet.runSketch(new String[]{"PTamplate"}, pt);
    }

    @Override
    public void settings() {
        size(100, 100, P2D);
    }

    @Override
    public void setup() {
        //colormode
    }

    @Override
    public void draw() {

    }
}
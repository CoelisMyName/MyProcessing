import processing.core.PApplet;
import processing.core.PVector;

public class Sierpinski extends PApplet {
    public static void main(String... args) {
        Sierpinski pt = new Sierpinski();
        PApplet.runSketch(new String[]{"Sierpinski"}, pt);
    }

    PVector a, b, c;
    int count = 0;

    @Override
    public void settings() {
        size(1000, 1000, P2D);
        a = new PVector((float) width / 2, 100);
        b = new PVector((float) (a.x - 800.0 / Math.sqrt(3.0)),900);
        c = new PVector((float) (a.x + 800.0 / Math.sqrt(3.0)),900);
    }

    @Override
    public void setup() {
        //colormode
        background(0);
    }

    public void draw_triangle(float x1, float y1,float x2, float y2,float x3, float y3, int level){
        if(level == 8){
            return;
        }
        PVector v1 = new PVector((x1 + x2) / 2, (y1 + y2) / 2);
        PVector v2 = new PVector((x2 + x3) / 2, (y2 + y3) / 2);
        PVector v3 = new PVector((x3 + x1) / 2, (y3 + y1) / 2);
        triangle(v1.x,v1.y,v2.x,v2.y,v3.x,v3.y);
        ++count;
        draw_triangle(x1, y1,v1.x, v1.y,v3.x, v3.y, level + 1);
        draw_triangle(x2, y2,v1.x, v1.y,v2.x, v2.y, level + 1);
        draw_triangle(x3, y3,v2.x, v2.y,v3.x, v3.y, level + 1);
    }

    @Override
    public void draw() {
        fill(0,255,0);
        noStroke();
        triangle(a.x,a.y,b.x,b.y,c.x,c.y);
        ++count;
        fill(255,0,255);
        draw_triangle(a.x,a.y,b.x,b.y,c.x,c.y,0);
        System.out.println(count);
        //3281
    }
}

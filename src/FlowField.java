import processing.core.PApplet;
import processing.core.PVector;
@SuppressWarnings("unchecked")
public class FlowField extends PApplet {

    public static void main (String... args) {
        FlowField pt = new FlowField();
        PApplet.runSketch(new String[]{"FlowField"}, pt);
    }

    int scl = 20;
    int refreshRate = 1000;

    float fieldStrength = (float) 0.3;

    float zoffInc = (float) 0.01;
    int angleMult = 2;

    float zoff = 0;
    float noiseInc = (float) 0.1;

    float decline = (float) 0.9999;

    int rows, cols;

    PVector[] flowfield;
    Particle[] particles = new Particle[10000];

    float h = 0;
    boolean colUp = true;

    @Override
    public void settings() {
        size(1920, 1080, P2D);
        fullScreen(P2D);

        cols = width/scl;
        rows = height/scl;

        flowfield = new PVector[rows*cols];

        createParticles();


    }

    @Override
    public void setup() {
        colorMode(HSB);
        background(0);
    }

    @Override
    public void draw() {
            loadPixels();
            for(int i = 0; i < pixels.length; ++i){
                int rgb = pixels[i];
                int temp = (int) (brightness(rgb) * decline);
                pixels[i] = color(hue(rgb),saturation(rgb),temp);

            }
            updatePixels();


        float yoff = 0;
        for (int y = 0; y < rows; y++) {
            float xoff = 0;
            for (int x = 0; x < cols; x++) {
                int index = x+y*cols;
                float a = noise(xoff, yoff, zoff) * TWO_PI*angleMult;
                xoff += noiseInc;

                PVector v = PVector.fromAngle(0);
                v.rotate(a);
                v.setMag(fieldStrength);

                flowfield[index] = v;
            }
            yoff += noiseInc;
        }
        zoff += zoffInc;

        for (int i = 0; i < particles.length; i++) {
            particles[i].updateColor(color(h, 255, 255));
            particles[i].edges();
            particles[i].follow(flowfield);
            particles[i].update();
            particles[i].display();
        }


        if (colUp) {
            h++;
        } else {
            h--;
        }

        if (h > 255) {
            colUp =false;
            h = 255;
        } else if(h < 0) {
            colUp = true;
            h = 0;
        }

    }

    @Override
    public void mouseClicked() {
        if (mouseX < width / 2) {
            background(0);
        }else{
            refresh();
        }
    }

    void refresh() {
        background(0);
        createParticles();
    }

    void createParticles() {
        for (int i = 0; i < particles.length; i++) {
            particles[i] = new Particle();
        }
    }

    class Particle {
        private PVector pos;
        private PVector vel;
        private PVector acc;

        private PVector oldpos;

        private float maxSpeed;

        private int col;


        public Particle() {
            pos = new PVector(random(width-1), random(height-1));
            vel = new PVector(0, 0);
            acc = new PVector(0, 0);

            oldpos = new PVector(pos.x, pos.y);

            maxSpeed = 4;

            col = color(random(255), 255, 255);
        }

        public void follow(PVector[] vectors) {
            int x = floor(pos.x/scl);
            int y = floor(pos.y/scl);

            int index = x + y * cols;
            if(index >= vectors.length){
                index %= vectors.length;
            }

            applyForce(vectors[index]);
        }

        public void applyForce(PVector force) {
            acc.add(force);
        }

        public void display() {
            stroke(col, 5);
            strokeWeight(1);
            line(pos.x, pos.y, oldpos.x, oldpos.y);

            updateOldPos();
        }

        public void updateColor(int c) {
            col = c;
        }

        public void update() {
            vel.add(acc);
            vel.limit(maxSpeed);
            pos.add(vel);
            acc.mult(0);
        }

        public void updateOldPos() {
            oldpos.x = pos.x;
            oldpos.y = pos.y;
        }

        public void edges() {
            if (pos.x > width-1) {
                pos.x = 1;
                updateOldPos();
            }

            if (pos.x < 1) {
                pos.x = width-1;
                updateOldPos();
            }

            if (pos.y > height-1) {
                pos.y = 1;
                updateOldPos();
            }

            if (pos.y < 1) {
                pos.y = height-1;
                updateOldPos();
            }
        }
    }
}

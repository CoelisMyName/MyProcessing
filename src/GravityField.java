import processing.core.PApplet;
import processing.core.PVector;

public class GravityField  extends PApplet {

    public static void main (String... args) {
        GravityField pt = new GravityField();
        PApplet.runSketch(new String[]{"GravityField"}, pt);
    }

    int scl = 1;

    float decline = (float) 0.9999;

    float maxSpeed = 10;

    float mass;

    float minRadius = 2;

    int rows, cols;

    PVector[] gravityfield;
    Particle[] particles = new Particle[10000];

    @Override
    public void settings() {
        size(1000, 1000, P2D);

        cols = ceil((float) width/scl);
        rows = ceil((float) height/scl);

        gravityfield = new PVector[rows*cols];

        createField();
        createParticles();
    }

    private void createParticles() {
        for (int i = 0; i < particles.length; i++) {
            particles[i] = new Particle();
        }
    }

    private void createField() {
        for(int y = 0; y < rows; ++y){
            for(int x = 0; x < cols; ++x){

            }
        }
    }

    @Override
    public void setup() {
        colorMode(HSB);
        background(0);
    }

    @Override
    public void draw() {

    }

    class Particle {
        private PVector pos;
        private PVector vel;
        private PVector acc;

        private PVector oldpos;

        private int col;

        public Particle() {
            pos = new PVector(random(width-1), random(height-1));
            vel = new PVector(0, 0);
            acc = new PVector(0, 0);

            oldpos = new PVector(pos.x, pos.y);

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

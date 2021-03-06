const scl = 20;

const fieldStrength = 0.3;

var zoffInc = 0.01;

var angleMult = 2;

var zoff = 0;
var noiseInc = 0.1;

var rows, cols;

var flowfield = [];
var particles = [];

var h = 0;
var colUp = true;

const maxSpeed = 4;

const num = 10000;

var mm = 0;

function setup() {
    createCanvas(windowWidth, windowHeight);
    cols = width / scl;
    rows = height / scl;
    background(0);
    colorMode(HSB, 255);
    createParticles();
}

function draw() {
    background(0, 0, 0, 15);

    var yoff = 0;
    for (var y = 0; y < rows; y++) {
        var xoff = 0;
        for (var x = 0; x < cols; x++) {
            var index = x + y * cols;
            var a = noise(xoff, yoff, zoff) * TWO_PI * angleMult;
            xoff += noiseInc;

            var v = p5.Vector.fromAngle(0);
            v.rotate(a);
            v.setMag(fieldStrength);

            flowfield[index] = v;
        }
        yoff += noiseInc;
    }
    zoff += zoffInc;

    for (var particle of particles) {
        particle.updateColor(color(h, 255, 255));
        particle.edges();
        particle.follow(flowfield);
        particle.update();
        particle.display();
    }

    if (colUp) {
        h++;
    } else {
        h--;
    }

    if (h > 255) {
        colUp = false;
        h = 255;
    } else if (h < 0) {
        colUp = true;
        h = 0;
    }
}

function mouseClicked() {
    var temp = num / 100;
    for (var i = mm; i < num; i += temp) {
        particles[i].updateloc(mouseX, mouseY);
    }
    mm = (mm + 1) % temp;
}

function refresh() {
    background(0);
    createParticles();
}

function createParticles() {
    for (var i = 0; i < 10000; i++) {
        particles[i] = new Particle();
    }
}

function Particle() {
    this.pos = createVector(random(width - 1), random(height - 1));
    this.vel = createVector(0, 0);
    this.acc = createVector(0, 0);
    this.oldpos = createVector(this.pos.x, this.pos.y);
    this.col = color(random(255), 255, 255);

    this.updateloc = function (x, y) {
        this.pos.x = x;
        this.pos.y = y;
        this.updateOldPos()
        var temp = random(0, TWO_PI);
        this.vel.x = maxSpeed * cos(temp);
        this.vel.y = maxSpeed * sin(temp);
        this.acc.mult(0);
    }

    this.follow = function (vets) {
        var x = floor(this.pos.x / scl);
        var y = floor(this.pos.y / scl);

        var index = x + y * cols;
        if (index >= vets.length) {
            index %= vets.length;
        }

        this.applyForce(vets[index]);
    }

    this.applyForce = function (force) {
        this.acc.add(force);
    }

    this.display = function () {
        stroke(this.col, 5);
        strokeWeight(1);
        line(this.pos.x, this.pos.y, this.oldpos.x, this.oldpos.y);
        this.updateOldPos();
    }

    this.updateColor = function (c) {
        this.col = c;
    }

    this.update = function () {
        this.vel.add(this.acc);
        this.vel.limit(maxSpeed);
        this.pos.add(this.vel);
        this.acc.mult(0);
    }

    this.updateOldPos = function () {
        this.oldpos.x = this.pos.x;
        this.oldpos.y = this.pos.y;
    }

    this.edges = function () {
        if (this.pos.x > width - 1) {
            this.pos.x = 1;
            this.updateOldPos();
        }

        if (this.pos.x < 1) {
            this.pos.x = width - 1;
            this.updateOldPos();
        }

        if (this.pos.y > height - 1) {
            this.pos.y = 1;
            this.updateOldPos();
        }

        if (this.pos.y < 1) {
            this.pos.y = height - 1;
            this.updateOldPos();
        }
    }
}
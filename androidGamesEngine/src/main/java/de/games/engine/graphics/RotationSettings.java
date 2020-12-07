package de.games.engine.graphics;

public class RotationSettings {

    private Vector startRotation;
    private Vector rotationSpeed;
    private Vector currentRotation;

    public RotationSettings(final Vector startRotation, final Vector rotationSpeed) {
        this.startRotation = startRotation;
        this.rotationSpeed = rotationSpeed;
        this.currentRotation = startRotation;
    }

    public Vector getStartRotation() {
        return startRotation;
    }

    public void setStartRotation(final Vector startRotation) {
        this.startRotation = startRotation;
    }

    public Vector getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(final Vector rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public Vector getCurrentRotation() {
        return currentRotation;
    }

    public void setCurrentRotation(final Vector currentRotation) {
        this.currentRotation = currentRotation;
    }
}

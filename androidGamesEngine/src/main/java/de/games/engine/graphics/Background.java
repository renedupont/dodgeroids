package de.games.engine.graphics;

import de.games.engine.graphics.Mesh.RenderType;

public class Background {

    private final Mesh backgroundMesh;
    private final Texture backgroundTexture;

    public Background(final Mesh backgroundMesh, final Texture backgroundTexture) {
        this.backgroundMesh = backgroundMesh;
        this.backgroundTexture = backgroundTexture;
    }

    public void render() {
        backgroundTexture.bind();
        backgroundMesh.render(RenderType.TRIANGLE_FAN);
    }

    public void dispose() {
        backgroundMesh.dispose();
        backgroundTexture.dispose();
    }
}

package org.literacyapp;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import org.literacyapp.util.Log;
import org.rajawali3d.surface.IRajawaliSurface;
import org.rajawali3d.surface.RajawaliSurfaceView;

public class EarthActivity extends AppCompatActivity {

    private SurfaceRenderer surfaceRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(getClass(), "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_earth);

        final RajawaliSurfaceView surface = new RajawaliSurfaceView(this);
        surface.setFrameRate(60.0);
        surface.setRenderMode(IRajawaliSurface.RENDERMODE_WHEN_DIRTY);

        // Add mSurface to your root view
        addContentView(surface, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT));

        surfaceRenderer = new SurfaceRenderer(this);
        surface.setSurfaceRenderer(surfaceRenderer);
    }
}

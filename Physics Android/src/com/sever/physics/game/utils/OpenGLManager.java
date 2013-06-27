package com.sever.physics.game.utils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;

public class OpenGLManager {
	public GLSurfaceView mGLView;

	public OpenGLManager(Context c) {
		super();
		mGLView = new MyGLSurfaceView(c);
	}

	class MyGLSurfaceView extends GLSurfaceView {

		public MyGLSurfaceView(Context context) {
			super(context);

			// Set the Renderer for drawing on the GLSurfaceView
			setRenderer(new MyGL20Renderer());
			setEGLContextClientVersion(2);
			setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		}
	}

	public class MyGL20Renderer implements Renderer {

		public void onSurfaceCreated(GL10 unused, EGLConfig config) {
			// Set the background frame color
			GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		}

		public void onDrawFrame(GL10 unused) {
			// Redraw background color
			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		}

		public void onSurfaceChanged(GL10 unused, int width, int height) {
			GLES20.glViewport(0, 0, width, height);
		}
	}
}

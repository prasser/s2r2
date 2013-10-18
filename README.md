Simple Software Real-Time Raytracer
====

S2R2 is a simple rendering engine written in Java implementing multithreaded CPU-only real-time raytracing. The following images
show some screenshots of a basic scene rendered at 800x600 with 12 to 24 FPS on a Lenovo W510 Thinkpad (Intel Core i7 CPU Q720 @ 1.60GHz):

[![Screenshot-1](https://raw.github.com/prasser/s2r2/master/doc/img/very-small-screenshot1.png)](https://raw.github.com/prasser/s2r2/master/doc/img/screenshot1.png)
[![Screenshot-2](https://raw.github.com/prasser/s2r2/master/doc/img/very-small-screenshot2.png)](https://raw.github.com/prasser/s2r2/master/doc/img/screenshot2.png)
[![Screenshot-3](https://raw.github.com/prasser/s2r2/master/doc/img/very-small-screenshot3.png)](https://raw.github.com/prasser/s2r2/master/doc/img/screenshot3.png)
[![Screenshot-4](https://raw.github.com/prasser/s2r2/master/doc/img/very-small-screenshot4.png)](https://raw.github.com/prasser/s2r2/master/doc/img/screenshot4.png)

Screenshots are taken from the scene shown in the following YouTube video:
[![Video](https://raw.github.com/prasser/s2r2/master/doc/img/video.png)](http://youtu.be/v5NccEae5Xc)

Try it out
------
The raytracer should be able to render the contained scene with 10 to 30 FPS at 800x600 on modern commodity hardware.
You can fly through the scene pressing the WASD keys of your keyboards and dragging with your mouse. 
Download the [executable JAR file](https://raw.github.com/prasser/s2r2/master/s2r2.jar).

Background
------
The code is a stripped down version of a raytracer I built years ago, just because everyone should write 
a raytracer once in his or her life. It is very basic, featuring only a few geometric primitives,
basic textures, blinn-phong shading, reflections and hard shadows. What makes it somewhat interesting
compared to many of the other "toy" raytracers is that it renders in real-time using **only** the
**CPU**. Real-time raytracing is an emerging trend and many comparable renderers have been built using
GPUs (i.e., with OpenCL or CUDA). Furthermore, S2R2 is written in **Java**, which is quite rare in the
real-time raytracing world.

The raytracer implements a few well-known tricks to achieve said performance. Raytracing is perfect
for parallelization, so the renderer tries to split the workload equally amongst several threads, 
leveraging modern multi-core processors. The rendering process itself is accelerated by packing the
scene into a hierarchy of axis-aligned bounding boxes (AABBs). Moreover, the scene is constructed of
primitives for which rather efficient ray intersection tests exist. Finally, the raytracer implements
an adaptive approach resulting in lossy rendering. To this end, a scene is rendered in four phases:

1. A grid of 25% of all rays required to render the whole scene is traced to derive a first approximate image. 

2. The color of pixels with almost identical neighboring pixels is interpolated.

3. A second set of rays is traced only for those pixels for which the color could not be interpolated. 

4. An additional layer of sprites is rendered on top of the scene, e.g., coronas for lights.

The scheme with which the work is distributed amongst these phases and the rendering threads is
shown in the following figure:

![Scheme](https://raw.github.com/prasser/s2r2/master/doc/img/scheme.png "Rendering scheme")

In this example, three threads are used to render the scene. Each thread is responsible for rendering
a distinct set of rows of pixels. The rows are assigned in an interleaved manner to
split the workload equally amongst the threads. The black pixels are rendered in the first phase.
In the second phase, a subset of the pixels not processed in the first phase (the white pixels in the above figure)
are rendered. Finally, rays are traced for all pixels not processed in the first or second phase.
The phases 1-3 are all executed in a multithreaded manner following the interleaved scheme, i.e.,
thread-1 renders rows 0 and 3, thread-2 renders rows 1 and 4 and thread-3 renders rows 2 and 5. 
All threads are synchronized after each of the phases. The fourth phase is executed by a single thread.

Limitations
------
From an implementation perspective, some parts of the code are ugly and need a rework. The code is mostly 
unoptimized and contains some premature optimizations. Documentation for the code can be found 
[here](https://rawgithub.com/prasser/s2r2/master/doc/javadoc/index.html).
From a conceptual perspective, most performance improvements are due to the adaptive rendering approach.
This only works well if the scene is not too diverse, e.g., does not contain complex textures.
Color-differences are computed in RGB space, which does not adequately reflect human perception. 
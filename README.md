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
[Download an executable JAR file](https://raw.github.com/prasser/s2r2/master/s2r2.jar)

Background
------
The code is a stripped down version of a raytracer I built years ago, just because everyone should write 
a raytracer once in his or her life. It is very basic, featuring only a few geometric primitives,
basic textures, blinn-phong shading, reflections and hard shadows. What makes it somewhat interesting
compared to many of the other "toy" raytracers is that it renders in real-time using **only** the
**CPU**. Real-time raytracing is an emerging trend and many comparable renderers have been built using
GPUs (i.e., with OpenCL or CUDA). Furthermore, S2R2 is written in **Java**, which is quite rare in the
real-time raytracing world.
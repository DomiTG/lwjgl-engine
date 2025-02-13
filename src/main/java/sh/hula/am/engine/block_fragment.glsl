#version 330 core

in vec2 outTexCoord;
out vec4 fragColor;

uniform vec4 color; // Add uniform for color (rgba)

void main() {
    // Set fragment color directly from uniform
    fragColor = color;
    //outline
    
}
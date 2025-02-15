#version 330 core

layout (location = 0) in vec2 position;

uniform vec2 uiPosition;
uniform vec2 uiScale;

void main() {
    vec2 pos = position * uiScale + uiPosition;
    gl_Position = vec4(pos, 0.0, 1.0);
}
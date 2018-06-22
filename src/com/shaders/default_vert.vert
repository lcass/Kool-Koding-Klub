
attribute vec2 position;
attribute vec2 texin;
attribute vec3 rot_attrib;
uniform vec2 transform;
uniform float timein;
uniform float zoom;
uniform vec2 rotpos;
uniform float light;
uniform int bloomval;
uniform vec3 rotpos_2;
varying vec2 vecdata;
varying float time;
varying float bloom;
varying float lightlines;
varying vec3 color_out;
uniform vec3 color_in;

uniform vec2 dimension;
uniform float rotation;
vec2 convertback(vec2 data){
float tx = data.x * dimension.x;
float ty = data.y * dimension.y;
vec2 stage1 = vec2(tx,ty);
tx = stage1.x + dimension.x;
ty = stage1.y + dimension.y;
return vec2(tx,ty);
}
vec2 convertto(vec2 data){
float tx = data.x ;
float ty = data.y;
vec2 stage1 = vec2(tx - dimension.x,ty - dimension.y);
vec2 stage2 = vec2((stage1.x )/dimension.x,(stage1.y )/dimension.y);
return stage2;
}
void main(){
   vec2 offset = vec2(position.x,position.y);
   if(rotation != 0.0){
   mat2 rotmat = mat2(cos(rotation),-sin(rotation),sin(rotation),cos(rotation));
   vec2 temp = convertback(offset);
   temp-=vec2(rotpos.x*2.0,rotpos.y*2.0);
   temp*=rotmat;
   temp+=vec2(rotpos.x*2.0,rotpos.y*2.0);
   offset = convertto(temp);
   }
   if(rotpos_2.z != 0.0){
    mat2 rotmat = mat2(cos(rotpos_2.z/2.0),-sin(rotpos_2.z/2.0),sin(rotpos_2.z/2.0),cos(rotpos_2.z/2.0));
   vec2 temp = convertback(offset);
   temp-=vec2(rotpos_2.x*2.0,rotpos_2.y*2.0);
   temp*=rotmat;
   temp+=vec2(rotpos_2.x*2.0,rotpos_2.y*2.0);
   offset = convertto(temp);
   }
   if(rot_attrib.z != 0.0){
   mat2 rotmat = mat2(cos(rot_attrib.z),-sin(rot_attrib.z),sin(rot_attrib.z),cos(rot_attrib.z));
   vec2 temp = convertback(offset);
   temp-=vec2(rot_attrib.x*2.0,rot_attrib.y*2.0);
   temp*=rotmat;
   temp+=vec2(rot_attrib.x*2.0,rot_attrib.y*2.0);
   offset = convertto(temp);
   }
   
  offset+=transform;
   gl_Position = vec4(offset,1,1);
   vecdata = texin;


}
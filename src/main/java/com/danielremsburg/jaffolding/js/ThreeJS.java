package com.danielremsburg.jaffolding.js;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * Java wrapper for Three.js library.
 */
public class ThreeJS {
    
    /**
     * Creates a new Three.js scene.
     */
    public static Scene createScene() {
        return Scene.create();
    }
    
    /**
     * Creates a new Three.js WebGL renderer.
     */
    public static WebGLRenderer createRenderer() {
        return WebGLRenderer.create();
    }
    
    /**
     * Creates a new Three.js perspective camera.
     */
    public static PerspectiveCamera createPerspectiveCamera(double fov, double aspect, double near, double far) {
        return PerspectiveCamera.create(fov, aspect, near, far);
    }
    
    /**
     * Three.js Scene object.
     */
    public interface Scene extends JSObject {
        @JSBody(params = {}, script = "return new THREE.Scene();")
        static Scene create();
        
        void add(Object3D object);
        
        void remove(Object3D object);
    }
    
    /**
     * Base interface for all 3D objects.
     */
    public interface Object3D extends JSObject {
        @JSProperty
        Vector3 getPosition();
        
        @JSProperty
        Vector3 getRotation();
        
        @JSProperty
        Vector3 getScale();
    }
    
    /**
     * Three.js Vector3 object.
     */
    public interface Vector3 extends JSObject {
        @JSBody(params = {"x", "y", "z"}, script = "return new THREE.Vector3(x, y, z);")
        static Vector3 create(double x, double y, double z);
        
        @JSProperty
        double getX();
        
        @JSProperty
        void setX(double x);
        
        @JSProperty
        double getY();
        
        @JSProperty
        void setY(double y);
        
        @JSProperty
        double getZ();
        
        @JSProperty
        void setZ(double z);
        
        Vector3 set(double x, double y, double z);
    }
    
    /**
     * Three.js Camera object.
     */
    public interface Camera extends Object3D {
    }
    
    /**
     * Three.js PerspectiveCamera object.
     */
    public interface PerspectiveCamera extends Camera {
        @JSBody(params = {"fov", "aspect", "near", "far"}, script = "return new THREE.PerspectiveCamera(fov, aspect, near, far);")
        static PerspectiveCamera create(double fov, double aspect, double near, double far);
        
        @JSProperty
        double getFov();
        
        @JSProperty
        void setFov(double fov);
        
        @JSProperty
        double getAspect();
        
        @JSProperty
        void setAspect(double aspect);
        
        void updateProjectionMatrix();
    }
    
    /**
     * Three.js WebGLRenderer object.
     */
    public interface WebGLRenderer extends JSObject {
        @JSBody(params = {}, script = "return new THREE.WebGLRenderer({antialias: true});")
        static WebGLRenderer create();
        
        void setSize(int width, int height);
        
        void render(Scene scene, Camera camera);
        
        @JSProperty
        HTMLElement getDomElement();
    }
    
    /**
     * Three.js Mesh object.
     */
    public interface Mesh extends Object3D {
        @JSBody(params = {"geometry", "material"}, script = "return new THREE.Mesh(geometry, material);")
        static Mesh create(BufferGeometry geometry, Material material);
    }
    
    /**
     * Three.js BufferGeometry object.
     */
    public interface BufferGeometry extends JSObject {
    }
    
    /**
     * Three.js BoxGeometry object.
     */
    public interface BoxGeometry extends BufferGeometry {
        @JSBody(params = {"width", "height", "depth"}, script = "return new THREE.BoxGeometry(width, height, depth);")
        static BoxGeometry create(double width, double height, double depth);
    }
    
    /**
     * Three.js SphereGeometry object.
     */
    public interface SphereGeometry extends BufferGeometry {
        @JSBody(params = {"radius", "widthSegments", "heightSegments"}, script = "return new THREE.SphereGeometry(radius, widthSegments, heightSegments);")
        static SphereGeometry create(double radius, int widthSegments, int heightSegments);
    }
    
    /**
     * Three.js Material object.
     */
    public interface Material extends JSObject {
    }
    
    /**
     * Three.js MeshBasicMaterial object.
     */
    public interface MeshBasicMaterial extends Material {
        @JSBody(params = {"color"}, script = "return new THREE.MeshBasicMaterial({color: color});")
        static MeshBasicMaterial create(String color);
    }
    
    /**
     * Three.js MeshPhongMaterial object.
     */
    public interface MeshPhongMaterial extends Material {
        @JSBody(params = {"color"}, script = "return new THREE.MeshPhongMaterial({color: color});")
        static MeshPhongMaterial create(String color);
    }
    
    /**
     * Three.js Light object.
     */
    public interface Light extends Object3D {
    }
    
    /**
     * Three.js AmbientLight object.
     */
    public interface AmbientLight extends Light {
        @JSBody(params = {"color", "intensity"}, script = "return new THREE.AmbientLight(color, intensity);")
        static AmbientLight create(String color, double intensity);
    }
    
    /**
     * Three.js DirectionalLight object.
     */
    public interface DirectionalLight extends Light {
        @JSBody(params = {"color", "intensity"}, script = "return new THREE.DirectionalLight(color, intensity);")
        static DirectionalLight create(String color, double intensity);
    }
}
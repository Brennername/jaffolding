package com.danielremsburg.jaffolding.js;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSString;
import org.teavm.jso.dom.html.HTMLCanvasElement;

/**
 * Java wrapper for Chart.js library.
 */
public class ChartJS {
    
    /**
     * Creates a new chart instance.
     */
    public static Chart createChart(HTMLCanvasElement canvas, ChartConfig config) {
        return Chart.create(canvas, config);
    }
    
    /**
     * Chart.js Chart object.
     */
    public interface Chart extends JSObject {
        @JSBody(params = {"canvas", "config"}, script = "return new Chart(canvas, config);")
        static Chart create(HTMLCanvasElement canvas, ChartConfig config);
        
        void update();
        
        void destroy();
        
        @JSProperty
        ChartData getData();
    }
    
    /**
     * Chart configuration.
     */
    public interface ChartConfig extends JSObject {
        @JSProperty
        String getType();
        
        @JSProperty
        void setType(String type);
        
        @JSProperty
        ChartData getData();
        
        @JSProperty
        void setData(ChartData data);
        
        @JSProperty
        ChartOptions getOptions();
        
        @JSProperty
        void setOptions(ChartOptions options);
    }
    
    /**
     * Chart data.
     */
    public interface ChartData extends JSObject {
        @JSProperty
        JSArray<JSString> getLabels();
        
        @JSProperty
        void setLabels(JSArray<JSString> labels);
        
        @JSProperty
        JSArray<Dataset> getDatasets();
        
        @JSProperty
        void setDatasets(JSArray<Dataset> datasets);
    }
    
    /**
     * Chart dataset.
     */
    public interface Dataset extends JSObject {
        @JSProperty
        String getLabel();
        
        @JSProperty
        void setLabel(String label);
        
        @JSProperty
        JSArray<Double> getData();
        
        @JSProperty
        void setData(JSArray<Double> data);
        
        @JSProperty
        JSArray<JSString> getBackgroundColor();
        
        @JSProperty
        void setBackgroundColor(JSArray<JSString> colors);
        
        @JSProperty
        JSArray<JSString> getBorderColor();
        
        @JSProperty
        void setBorderColor(JSArray<JSString> colors);
        
        @JSProperty
        double getBorderWidth();
        
        @JSProperty
        void setBorderWidth(double width);
    }
    
    /**
     * Chart options.
     */
    public interface ChartOptions extends JSObject {
        @JSProperty
        boolean getResponsive();
        
        @JSProperty
        void setResponsive(boolean responsive);
        
        @JSProperty
        ChartTitle getTitle();
        
        @JSProperty
        void setTitle(ChartTitle title);
        
        @JSProperty
        ChartLegend getLegend();
        
        @JSProperty
        void setLegend(ChartLegend legend);
    }
    
    /**
     * Chart title configuration.
     */
    public interface ChartTitle extends JSObject {
        @JSProperty
        boolean getDisplay();
        
        @JSProperty
        void setDisplay(boolean display);
        
        @JSProperty
        String getText();
        
        @JSProperty
        void setText(String text);
        
        @JSProperty
        int getFontSize();
        
        @JSProperty
        void setFontSize(int fontSize);
    }
    
    /**
     * Chart legend configuration.
     */
    public interface ChartLegend extends JSObject {
        @JSProperty
        boolean getDisplay();
        
        @JSProperty
        void setDisplay(boolean display);
        
        @JSProperty
        String getPosition();
        
        @JSProperty
        void setPosition(String position);
    }
    
    /**
     * Factory methods for creating Chart.js objects.
     */
    public static class Factory {
        
        @JSBody(params = {}, script = "return {};")
        public static native ChartConfig createConfig();
        
        @JSBody(params = {}, script = "return {datasets: []};")
        public static native ChartData createData();
        
        @JSBody(params = {}, script = "return {};")
        public static native Dataset createDataset();
        
        @JSBody(params = {}, script = "return {};")
        public static native ChartOptions createOptions();
        
        @JSBody(params = {}, script = "return {};")
        public static native ChartTitle createTitle();
        
        @JSBody(params = {}, script = "return {};")
        public static native ChartLegend createLegend();
        
        @JSBody(params = {"items"}, script = "return Array.from(items);")
        public static native <T extends JSObject> JSArray<T> createArray(T[] items);
        
        @JSBody(params = {"strings"}, script = "return Array.from(strings);")
        public static native JSArray<JSString> createStringArray(String[] strings);
        
        @JSBody(params = {"numbers"}, script = "return Array.from(numbers);")
        public static native JSArray<Double> createNumberArray(double[] numbers);
    }
}
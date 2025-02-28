package com.danielremsburg.jaffolding.bridge;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * Bridge interfaces for HTML table elements.
 * These interfaces provide access to HTML table elements that are not included in the TeaVM DOM API.
 */
public class HTMLTableElements {
    
    /**
     * Interface for HTMLTableElement.
     */
    public interface HTMLTableElement extends HTMLElement {
        /**
         * Gets the table's caption element.
         * @return The caption element
         */
        @JSProperty
        HTMLElement getCaption();
        
        /**
         * Sets the table's caption element.
         * @param caption The caption element
         */
        @JSProperty
        void setCaption(HTMLElement caption);
        
        /**
         * Gets the table's thead element.
         * @return The thead element
         */
        @JSProperty
        HTMLElement getTHead();
        
        /**
         * Sets the table's thead element.
         * @param tHead The thead element
         */
        @JSProperty
        void setTHead(HTMLElement tHead);
        
        /**
         * Gets the table's tfoot element.
         * @return The tfoot element
         */
        @JSProperty
        HTMLElement getTFoot();
        
        /**
         * Sets the table's tfoot element.
         * @param tFoot The tfoot element
         */
        @JSProperty
        void setTFoot(HTMLElement tFoot);
        
        /**
         * Gets the table's rows.
         * @return The rows
         */
        @JSProperty
        HTMLElement[] getRows();
        
        /**
         * Gets the table's tbody elements.
         * @return The tbody elements
         */
        @JSProperty
        HTMLElement[] getTBodies();
        
        /**
         * Creates a caption element for the table.
         * @return The caption element
         */
        HTMLElement createCaption();
        
        /**
         * Creates a tfoot element for the table.
         * @return The tfoot element
         */
        HTMLElement createTFoot();
        
        /**
         * Creates a thead element for the table.
         * @return The thead element
         */
        HTMLElement createTHead();
        
        /**
         * Deletes the table's caption element.
         */
        void deleteCaption();
        
        /**
         * Deletes the table's tfoot element.
         */
        void deleteTFoot();
        
        /**
         * Deletes the table's thead element.
         */
        void deleteTHead();
        
        /**
         * Inserts a row at the specified index.
         * @param index The index
         * @return The new row
         */
        HTMLTableRowElement insertRow(int index);
        
        /**
         * Deletes a row at the specified index.
         * @param index The index
         */
        void deleteRow(int index);
    }
    
    /**
     * Interface for HTMLTableRowElement.
     */
    public interface HTMLTableRowElement extends HTMLElement {
        /**
         * Gets the row index.
         * @return The row index
         */
        @JSProperty
        int getRowIndex();
        
        /**
         * Gets the section row index.
         * @return The section row index
         */
        @JSProperty
        int getSectionRowIndex();
        
        /**
         * Gets the row's cells.
         * @return The cells
         */
        @JSProperty
        HTMLElement[] getCells();
        
        /**
         * Inserts a cell at the specified index.
         * @param index The index
         * @return The new cell
         */
        HTMLElement insertCell(int index);
        
        /**
         * Deletes a cell at the specified index.
         * @param index The index
         */
        void deleteCell(int index);
    }
}
package org.mskcc.cellranger.documentation;

public class FieldMapper {
    private String htmlElement;
    private String htmlField;
    private String tableField;
    private Class type;

    /**
     * Identifies HTML elements to parse relevant fields from and map to database fields
     *  e.g. new FieldMapper("h1", "Estimated Number of Cells", "EstimatedNumberOfCells", Long.class)
     *          HTML
     *              <h1>Estimated Number of Cells</h1>  <- HTML Element
     *              <div>3,457</div>                    (Should be able to look to neighboring div for value)
     *
     *          TABLE
     *             public Long EstimatedNumberOfCells;
     *
     * @param htmlElement, String - HTML element in DOM that will contain relevant value, e.g. "h1", "p", "tr"
     * @param htmlField, String - text content of htmlElement
     * @param tableField, String - Field in table the value form the HTML should map to
     * @param type, String - Type field should have in the database
     */
    public FieldMapper(String htmlElement, String htmlField, String tableField, Class type) {
        this.htmlElement = htmlElement;
        this.htmlField = htmlField;
        this.tableField = tableField;
        this.type = type;
    }

    public String getHtmlElement() {
        return htmlElement;
    }
    public String getHtmlField() {
        return htmlField;
    }
    public String getTableField() {
        return tableField;
    }
    public Class getType() { return type; }
}
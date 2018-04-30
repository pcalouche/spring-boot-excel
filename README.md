# Background

This Spring Boot application has two endpoints to demonstrate using Apache POI to generate an Excel file.  Some logging
is provided to gain a sense of performance and scalability.

## Non Streaming Excel Export

This flavor of export retains the workbook in memory until it is written out.  Works fine for smaller exports and allows
the use of rich text cells see [XSSFRichTextString](https://poi.apache.org/apidocs/org/apache/poi/xssf/usermodel/XSSFRichTextString.html).  
A rich text cell is a cell that has a mix of styling (bold, font, etc.) in it. 

**Endpoint** - [http://localhost:8080/excel/non-streaming-excel?columns=10&rows=50000](http://localhost:8080/excel/non-streaming-excel?columns=10&rows=50000)

## Streaming Excel Export

This flavor of export writes data to disk and releases memory after X number of rows have been added to the workbook.  
The number of rows before disk write can be specified.  It defaults to 100.  The temp files are merged into a single 
Excel file.  After all rows have been exported calling "dispose" on the workbook removes any temporary files and render
the workbook instance unusable.  A new workbook instance would need to be created in order to append. 

This type of export does not seem to support rich text cells.  However, a single style can be applied to all content 
in the cell just fine.  I would love to be proven wrong about this.  A possible workaround could be to re-open the 
workbook after initial processing as a non streaming workbook and add the rich text cells.

This type of export is good for large exports.

**Endpoint** -  [http://localhost:8080/excel/streaming-excel?columns=10&rows=50000](http://localhost:8080/excel/streaming-excel?columns=10&rows=50000)


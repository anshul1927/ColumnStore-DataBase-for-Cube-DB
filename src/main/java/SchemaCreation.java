import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

public class SchemaCreation {

    public void instanceCreation(Map<String, Map<String,String>> input, String fact_table, Map<String,String> fact_variable, String dbname) throws FileNotFoundException, FileNotFoundException {
        //input named map is one where first String represents the dimension and the associated map i.e. the second input represents the map where
        //first input is the attribute name and second is the data type of attribute
        //fact_table is the name of the fact table
        //fact_variable is that map where first string is the name of the fact variable i.e. sales and second is its data type
        int number_of_dimensions=input.size();
        PrintWriter pw= new PrintWriter(new File("src/main/resources/schema/"+dbname+"star_schema.xml"));
        StringBuffer xmlData = new StringBuffer("");
        xmlData.append("<?xml version=\"1.0\"?>");
        xmlData.append("\n");
        xmlData.append("<star_schema xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "           xsi:schemaLocation=\"schema4star.xsd\" num_dims=\""+number_of_dimensions+"\" num_facts=\"1\">");
        xmlData.append("\n");
        xmlData.append("\t");
        xmlData.append("<dimensions>");
        xmlData.append("\n");
        for(Map.Entry m : input.entrySet())
        {
            String dimension = (String) m.getKey();
            Map<String, String> attribute = input.get(dimension);
            int number_of_attributes = attribute.size();
            xmlData.append("\t");
            xmlData.append("<dim num_cols=\"" + number_of_attributes + "\">");
            xmlData.append("\n");
            xmlData.append("\t");
            xmlData.append("\t");
            xmlData.append("<dim_name>" + dimension + "</dim_name>");
            xmlData.append("\n");

            for (Map.Entry inner : attribute.entrySet()) {
                xmlData.append("\t");
                xmlData.append("\t");
                xmlData.append("<column>");
                xmlData.append("\n");
                xmlData.append("\t");
                xmlData.append("\t");
                String column_name = (String) inner.getKey();
                String data_type = (String) inner.getValue();
                xmlData.append("<col_name>" + column_name + "</col_name>");
                xmlData.append("\n");
                xmlData.append("\t");
                xmlData.append("\t");
                xmlData.append("<col_dtype>" + data_type + "</col_dtype>");
                xmlData.append("\n");
                xmlData.append("\t");
                xmlData.append("\t");
                xmlData.append("</column>");
                xmlData.append("\n");
            }
            xmlData.append("\t");
            xmlData.append("</dim>");
            xmlData.append("\n");
            xmlData.append("\t");
        }
        xmlData.append("</dimensions>");
        xmlData.append("\n");
        xmlData.append("\t");
        xmlData.append("<Fact>"+fact_table+"</Fact>");
        xmlData.append("\n");
        xmlData.append("\t");
        int num_variables=fact_variable.size();
        int num_cols=num_variables+number_of_dimensions;
        xmlData.append("<Variables num_cols=\""+num_cols+" \"num_variables=\""+num_variables+"\">");
        for(Map.Entry facts: fact_variable.entrySet())
        {
            xmlData.append("\n");
            xmlData.append("\t");
            xmlData.append("\t");
            String variable=(String) facts.getKey();
            xmlData.append("<fact_var>"+variable+"</fact_var>");
        }
        xmlData.append("\n");
        xmlData.append("\t");
        xmlData.append("</Variables>");
        xmlData.append("\n");
        xmlData.append("</star_schema>");
        pw.write(xmlData.toString());
        pw.close();
    }



    public void constraintsCreation(Map<String,String> fact_variable, String dbname) throws FileNotFoundException {
        //this fact_variable i am taking is that map where the first string is the fact variable name like sales
        // and second is the aggregate function like sum
        PrintWriter pw= new PrintWriter(new File("src/main/resources/schema/"+dbname+"constraints.xml"));
        StringBuffer xmlData = new StringBuffer("");
        xmlData.append("<?xml version=\"1.0\"?>");
        xmlData.append("\n");
        xmlData.append("<inputs>");
        xmlData.append("\n");
        xmlData.append("\t");
        xmlData.append("<Constraints>");
        xmlData.append("\n");
        for(Map.Entry m : fact_variable.entrySet())
        {
            xmlData.append("\t");
            xmlData.append("\t");
            xmlData.append("<FactVar>");
            xmlData.append("\n");
            xmlData.append("\t");
            xmlData.append("\t");
            xmlData.append("\t");
            String name = (String) m.getKey();
            String aggregate = (String) m.getValue();
            xmlData.append("<name>" + name + "</name>");
            xmlData.append("\n");
            xmlData.append("\t");
            xmlData.append("\t");
            xmlData.append("\t");
            xmlData.append("<aggregation>" + aggregate + "</aggregation>");
            xmlData.append("\n");
            xmlData.append("\t");
            xmlData.append("\t");
            xmlData.append("</FactVar>");
            xmlData.append("\n");
        }
        xmlData.append("\t");
        xmlData.append("</Constraints>");
        xmlData.append("\n");
        xmlData.append("</inputs>");
        pw.write(xmlData.toString());
        pw.close();
    }
}

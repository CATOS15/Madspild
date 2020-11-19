package madspild.Models;

public class Product {
    private String DataMatrixData;

    public Product(String dataMatrixData) {
        DataMatrixData = dataMatrixData;
    }

    public String getDataMatrixData() {
        return DataMatrixData;
    }

    public void setDataMatrixData(String dataMatrixData) {
        DataMatrixData = dataMatrixData;
    }
}

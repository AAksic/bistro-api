package de.project.test.bistro_api.integration.transformer;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import de.project.test.bistro_api.domain.Product;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class CsvToProductTransformer implements GenericTransformer<String, List<Product>> {

    private final CsvMapper csvMapper = new CsvMapper();

    @Override
    public List<Product> transform(String source) {
        CsvSchema productSchema = csvMapper
                .typedSchemaFor(Product.class)
                .withHeader()
                .withColumnSeparator(',');

        try (MappingIterator<Product> productIterator = csvMapper
                .readerWithTypedSchemaFor(Product.class)
                .with(productSchema)
                .readValues(source)
        ) {
            return productIterator.readAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

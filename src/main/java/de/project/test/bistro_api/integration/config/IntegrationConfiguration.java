package de.project.test.bistro_api.integration.config;

import de.project.test.bistro_api.integration.handler.ProductDataHandler;
import de.project.test.bistro_api.integration.transformer.CsvToProductTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.transformer.FileToStringTransformer;

import java.io.File;

@Configuration
@RequiredArgsConstructor
public class IntegrationConfiguration {

    private final ProductDataHandler productDataHandler;
    private final CsvToProductTransformer csvToProductTransformer;

    @Value("${app.integration.input-directory}")
    private String productDataSourceDirectory;

    @Bean
    public IntegrationFlow loadProductDataFlow() {
        return IntegrationFlow.from(Files.inboundAdapter(new File(productDataSourceDirectory))
                                .filter(new SimplePatternFileListFilter("*.csv"))
                                .scanEachPoll(true),
                        message -> message.poller(Pollers.fixedDelay(5000)))
                .transform(new FileToStringTransformer())
                .transform(csvToProductTransformer)
                .handle(productDataHandler)
                .get();
    }
}

package de.project.test.bistro_api.integration.config;

import de.project.test.bistro_api.integration.handler.ProductDataCsvHandler;
import de.project.test.bistro_api.integration.transformer.CsvLineToProductTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.filters.AcceptAllFileListFilter;
import org.springframework.integration.file.transformer.FileToStringTransformer;

import java.io.File;

@Configuration
@RequiredArgsConstructor
public class IntegrationConfiguration {

    private final ProductDataCsvHandler productDataCsvHandler;
    private final CsvLineToProductTransformer csvLineToProductTransformer;

    @Value("${app.integration.input-directory}")
    private String productDataSourceDirectory;

    @Bean
    public IntegrationFlow loadProductDataFlow() {
        return IntegrationFlow.from(Files.inboundAdapter(new File(productDataSourceDirectory))
                        .filter(new AcceptAllFileListFilter<>())
                        .scanEachPoll(true),
                        message -> message.poller(Pollers.fixedDelay(5000)))
                .transform(new FileToStringTransformer())
                .split()
                .transform(csvLineToProductTransformer)
                .handle(message -> System.out.println(message.getPayload()))
                .get();
    }
}

package com.claro.amx.cufjava.change_domestic_use_api.client;

import com.claro.amx.cufjava.change_domestic_use_api.dto.common.LineInfoResponseDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

public class CommonRestClient {

    private final RestClient restClient;
    private static final String LINE_NUMBER = "lineNumber";

    public CommonRestClient(String url) {

        this.restClient = RestClient.builder()

                .uriBuilderFactory(new DefaultUriBuilderFactory(url))

                .build();

    }

    public ResponseEntity<LineInfoResponseDTO> getLineInfo(HttpHeaders httpHeaders, String lineNumber) {

        return restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/lineInfo/line-info").queryParam(LINE_NUMBER, lineNumber).build())
                .headers(headers -> HttpHeadersSanitizer.copySanitizedHeaders(httpHeaders, headers))
                .retrieve()
                .toEntity(LineInfoResponseDTO.class);
    }


    /*public ResponseEntity<CharValueParametersResponse>getCharValueParameter(String parameterName) {

        return restClient.get()

                .uri(uriBuilder -> uriBuilder.path("/parameters/getChar").queryParam("parameter", parameterName).build())

                .retrieve()

                .toEntity(CharValueParametersResponse.class);

    }

    public ResponseEntity<StlCharValueResponseDTO>getStlParametersCharValue(String parameterName) {

        return restClient.get()

                .uri(uriBuilder -> uriBuilder.path("/parameters/getStlParametersCharValue")

                        .queryParam("parameter", parameterName).build())

                .retrieve()

                .toEntity(StlCharValueResponseDTO.class);

    }*/


}

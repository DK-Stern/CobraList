package sh.stern.cobralist.streaming.spotify;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateFactory {

    public RestTemplate create() {
        return new RestTemplate();
    }
}

package sh.stern.cobralist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import sh.stern.cobralist.properties.AppProperties;

@Controller
@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class CobraListApplication implements ErrorController {

    public static void main(String[] args) {
        SpringApplication.run(CobraListApplication.class, args);
    }

    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String error() {
        return "forward:/index.html";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}

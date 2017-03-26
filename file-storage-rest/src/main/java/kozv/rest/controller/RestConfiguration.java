package kozv.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.Map;

@Controller
public class RestConfiguration {

    @RequestMapping("/api/appName")
    @ResponseBody
    Map<String, String> appName() {
        return Collections.singletonMap("appName", "File Storage");
    }
}
